import java.util.List;
import java.util.Map;

public abstract class Strategy {
    public abstract void runBestMatching(List<Record> records, List<String> inputSentences);
    public abstract Map<String, Record> getBestMatching();
    public abstract String getApproachName();
    protected Record getBestMatchingByInputSentence(List<Record> records, String inputSentence) {
        return BestMatching.findBestMatchingParagraph(records, inputSentence);
    }
}
