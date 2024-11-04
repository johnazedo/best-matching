import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockVTApproach extends VirtualThreadsConcurrencyStrategy {
    private final Map<String, Record> results = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void run(List<Record> records, List<String> inputSentences) {
        for (String sentence : inputSentences) {
            Record record = this.getBestMatchingByInputSentence(records, sentence);

            lock.lock();
            try {
                if(results.containsKey(sentence)) {
                    if(results.get(sentence).similarity > record.similarity) return;
                }
                results.put(sentence, record);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public Map<String, Record> getBestMatching() {
        return results;
    }

    @Override
    public String getApproachName() {
        return "ReentrantLock Virtual Threads";
    }
}
