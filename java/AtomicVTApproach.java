import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtomicVTApproach extends VirtualThreadsConcurrencyStrategy {
    private final ConcurrentHashMap<String, Record> results = new ConcurrentHashMap<>();

    @Override
    public void run(List<Record> records, List<String> inputSentences) {
        for (String sentence : inputSentences) {
            Record record = this.getBestMatchingByInputSentence(records, sentence);
            if(results.containsKey(sentence)) {
                if(results.get(sentence).similarity > record.similarity) return;
            }
            results.put(sentence, record);
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
