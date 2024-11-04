import java.util.List;

class Main {
    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader("./dataset/books.csv");
        List<Record> records = csvReader.read();
        
        List<String> inputSentences = List.of(
            "Machine learning basics and algorithms",
            "Understanding natural language processing techniques for sentiment analysis",
            "Downsizing in response to increasing competition in the food industry often has pushed the responsibility for ingredient quality to the suppliers",
            "There is an important opportunity here, and some might say an integral one, to include participants in the research process"
        );
        
        Strategy approach = new ReentrantLockVTApproach();
        approach.runBestMatching(records, inputSentences);

        approach.getBestMatching().forEach((input, record) -> {
            System.out.println("Sentence: " + input);
            if(record.similarity > 0.0) {
                System.out.println("Book: " + record.bookName);
                System.out.println("Similarity: " + record.similarity);   
            } else {
                System.out.println("Book not found!");
            }
            System.err.println("");
        });
    }
}