import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BestMatching {

    private static Map<String, Integer> getWordFrequency(String text) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : text.toLowerCase().split("\s+")) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }
        return frequencyMap;
    }

    private static double cosineSimilarity(Map<String, Integer> freq1, Map<String, Integer> freq2) {
        Set<String> allWords = new HashSet<>(freq1.keySet());
        allWords.addAll(freq2.keySet());

        int dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (String word : allWords) {
            int count1 = freq1.getOrDefault(word, 0);
            int count2 = freq2.getOrDefault(word, 0);

            dotProduct += count1 * count2;
            norm1 += Math.pow(count1, 2);
            norm2 += Math.pow(count2, 2);
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public static Record findBestMatchingParagraph(List<Record> records, String inputSentence) {
        Record bestMatch = null;
        double highestSimilarity = 0;
        Map<String, Integer> inputFreqMap = getWordFrequency(inputSentence);

        for (Record record : records) {
            Map<String, Integer> paragraphFreqMap = getWordFrequency(record.paragraphText);
            double similarity = cosineSimilarity(inputFreqMap, paragraphFreqMap);
            record.similarity = similarity;

            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = record;
            }
        }

        return bestMatch;
    }
}
