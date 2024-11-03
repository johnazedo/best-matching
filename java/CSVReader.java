import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private String csvFilePath;

    public CSVReader(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    // Reads the CSV and returns a list of Record objects
    public List<Record> read() {
        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isHeader = true;
            int counter = 0;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",", 4);
                if (values.length == 4) {
                    String bookName = values[0].trim();
                    String chapter = values[1].trim();
                    String paragraphId = values[2].trim();
                    String paragraphText = values[3].trim();
                    records.add(new Record(bookName, chapter, paragraphId, paragraphText));
                    counter++;
                }
            }

            System.out.println("Number of lines read: " + counter + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
