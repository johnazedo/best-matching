import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class FutureExecutorApproach extends ConcurrencyStrategy {

    private final Map<String, Record> results = new ConcurrentHashMap<>();
    private final ExecutorService executorService;

    public FutureExecutorApproach() {
        this.executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
    }

    @Override
    public void run(List<Record> records, List<String> inputSentences) {
        List<Future<Void>> futures = new ArrayList<>();

        for (String sentence : inputSentences) {
            futures.add(executorService.submit(() -> {
                Record record = this.getBestMatchingByInputSentence(records, sentence);
                results.merge(sentence, record, (existingRecord, newRecord) -> {
                    return (existingRecord.similarity > newRecord.similarity) ? existingRecord : newRecord;
                });
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
        shutdown();
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
