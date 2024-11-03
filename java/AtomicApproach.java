import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtomicApproach extends ConcurrencyStrategy {
    private final ConcurrentHashMap<String, Record> results = new ConcurrentHashMap<>();

    @Override
    public void run(List<Record> records, List<String> inputSentences) {
        for (String sentence : inputSentences) {
            Record record = this.getBestMatchingByInputSentence(records, sentence);
            results.compute(sentence, (key, existingRecord) -> {
                if (existingRecord == null || record.similarity > existingRecord.similarity) {
                    return record;
                }
                return existingRecord;
            });
        }
    }

    @Override
    public Map<String, Record> getBestMatching() {
        return results;
    }

    @Override
    public String getApproachName() {
        return "Atomic";
    }
}
