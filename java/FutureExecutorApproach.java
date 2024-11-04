import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class FutureExecutorApproach extends Strategy {

    private final Map<String, Record> results = new ConcurrentHashMap<>();
    private ExecutorService executorService;
    protected final static int NUM_OF_THREADS = 8;

    public void run(List<Record> records, List<String> inputSentences) {
        List<Future<Void>> futures = new ArrayList<>();

        for (String sentence : inputSentences) {
            futures.add(executorService.submit(() -> {
                Record record = this.getBestMatchingByInputSentence(records, sentence);
                results.merge(sentence, record, (existingRecord, newRecord) -> 
                    (existingRecord.similarity > newRecord.similarity) ? existingRecord : newRecord
                );
                return null; // Callable requires a return value
            }));
        }

        // Wait for all tasks to complete
        for (Future<Void> future : futures) {
            try {
                future.get(); // Block until the task is done
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(); // Handle exceptions as necessary
            }
        }
    }

    @Override
    public void runBestMatching(List<Record> records, List<String> inputSentences) {
        System.out.println("Run " + getApproachName() + " approach\n");
        executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        
        List<List<Record>> chunks = this.divideListIntoChunks(records);

        for (List<Record> chunk : chunks) {
            run(chunk, inputSentences);
        }
        
        shutdown();
    }

    protected List<List<Record>> divideListIntoChunks(List<Record> records) {
        List<List<Record>> chunks = new ArrayList<>();
        int chunkSize = getChunkSizeByNumberOfThread(records.size());

        for (int i = 0; i < records.size(); i += chunkSize) {
            chunks.add(records.subList(i, Math.min(i + chunkSize, records.size())));
        }
        return chunks;
    }

    protected int getChunkSizeByNumberOfThread(int totalSize) {
        return (int) Math.ceil((double) totalSize / NUM_OF_THREADS);
    }

    @Override
    public Map<String, Record> getBestMatching() {
        return results;
    }

    @Override
    public String getApproachName() {
        return "Future/ExecutorService";
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Force shutdown if not terminated
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
