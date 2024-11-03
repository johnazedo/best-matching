import java.util.ArrayList;
import java.util.List;

public abstract class ConcurrencyStrategy extends Strategy {

    protected final static int NUM_OF_THREADS = 8;

    private List<List<Record>> divideListIntoChunks(List<Record> records) {
        List<List<Record>> chunks = new ArrayList<>();
        int chunkSize = getChunkSizeByNumberOfThread(records.size());

        for (int i = 0; i < records.size(); i += chunkSize) {
            chunks.add(records.subList(i, Math.min(i + chunkSize, records.size())));
        }
        return chunks;
    }

    private int getChunkSizeByNumberOfThread(int totalSize) {
        int chunkSize = (int) Math.ceil((double) totalSize / NUM_OF_THREADS);
        return chunkSize;
    }

    protected abstract void run(List<Record> records, List<String> inputSentences);

    @Override
    public void runBestMatching(List<Record> records, List<String> inputSentences) {
        System.out.println("Run " + getApproachName() + " approach\n");
        List<List<Record>> chunks = this.divideListIntoChunks(records);
        List<Thread> threads = new ArrayList<>();

        for(List<Record> chunk: chunks) {
            Thread thread = new Thread(() -> { run(chunk, inputSentences); });
            threads.add(thread);
            thread.start();
        }

        try {
            for(Thread thread: threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Some thread are interrupted");
            e.printStackTrace();
        }
    }  
}
