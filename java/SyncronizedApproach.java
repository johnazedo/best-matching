import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncronizedApproach extends ConcurrencyStrategy{
    
    private final Map<String, Record> results = new HashMap<>();

    @Override
    public void run(List<Record> records, List<String> inputSentences) {
        for(String sentence: inputSentences) {
            Record record = this.getBestMatchingByInputSentence(records, sentence);
            synchronized (this) {
                results.compute(sentence, (key, existingRecord) -> {
                    if (existingRecord == null || record.similarity > existingRecord.similarity) {
                        return record;
                    }
                    return existingRecord;
                });
            }
        }
    }

    @Override
    public Map<String, Record> getBestMatching() {
        return results;
    }

    @Override
    public String getApproachName() {
        return "Syncronized";
    }
}
