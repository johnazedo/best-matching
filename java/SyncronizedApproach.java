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
                if(results.containsKey(sentence)) {
                    if(results.get(sentence).similarity > record.similarity) return;
                }
                results.put(sentence, record);
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
