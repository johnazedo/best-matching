import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequentialApproach extends Strategy{

    private final Map<String, Record> results = new HashMap<>();

    @Override
    public void runBestMatching(List<Record> records, List<String> inputSentences) {
        for(String sentence: inputSentences) {
            results.put(sentence, this.getBestMatchingByInputSentence(records, sentence));
        }
    }

    @Override
    public Map<String, Record> getBestMatching() {
        return results;
    }

    @Override
    public String getApproachName() {
        return "Sequential";
    }
}
