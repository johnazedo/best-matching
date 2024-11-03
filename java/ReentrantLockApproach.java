import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockApproach extends ConcurrencyStrategy {

    private final Map<String, Record> results = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void run(List<Record> records, List<String> inputSentences) {
        for (String sentence : inputSentences) {
            Record record = this.getBestMatchingByInputSentence(records, sentence);

            lock.lock();  // Acquire the lock
            try {
                results.compute(sentence, (key, existingRecord) -> {
                    if (existingRecord == null || record.similarity > existingRecord.similarity) {
                        return record;
                    }
                    return existingRecord;
                });
            } finally {
                lock.unlock();  // Always release the lock in a finally block
            }
        }
    }

    @Override
    public Map<String, Record> getBestMatching() {
        return results;
    }

    @Override
    public String getApproachName() {
        return "ReentrantLock";
    }
}