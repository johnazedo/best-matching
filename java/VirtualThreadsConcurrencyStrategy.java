import java.util.ArrayList;
import java.util.List;

public abstract class VirtualThreadsConcurrencyStrategy extends Strategy{

    protected final static int NUM_OF_THREADS = 512;

    protected List<List<Record>> divideListIntoChunks(List<Record> records) {
        List<List<Record>> chunks = new ArrayList<>();
        int chunkSize = getChunkSizeByNumberOfThread(records.size());

        for (int i = 0; i < records.size(); i += chunkSize) {
            chunks.add(records.subList(i, Math.min(i + chunkSize, records.size())));
        }
        return chunks;
    }

    protected  int getChunkSizeByNumberOfThread(int totalSize) {
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
            Thread thread = Thread.ofVirtual().start(() -> { run(chunk, inputSentences); });
            threads.add(thread);
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
