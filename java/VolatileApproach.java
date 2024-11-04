import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolatileApproach extends ConcurrencyStrategy {
    private volatile Map<String, Record> results = new HashMap<>();
    
    @Override
    protected void run(List<Record> records, List<String> inputSentences) {
        throw new UnsupportedOperationException("Cannot use volatile approach because change result depends of itself");
    }

    @Override
    public Map<String, Record> getBestMatching() {
        throw new UnsupportedOperationException("Cannot use volatile approach because change result depends of itself");
    }

    @Override
    public String getApproachName() {
        return "Volatile";
    }
}
