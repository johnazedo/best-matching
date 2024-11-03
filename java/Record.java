public class Record {
    public String bookName;
    public String chapter;
    public String paragraphId;
    public String paragraphText;
    public Double similarity = 0.0;

    public Record(String bookName, String chapter, String paragraphId, String paragraphText) {
        this.bookName = bookName;
        this.chapter = chapter;
        this.paragraphId = paragraphId;
        this.paragraphText = paragraphText;
    }
}
