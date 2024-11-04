from tika import parser
import csv
import os
import re

# Function to extract book content using Tika
def extract_book_content(book_path):
    parsed = parser.from_file(book_path)
    content = parsed['content']
    
    # Remove all newline characters and non-printable characters
    content = re.sub(r'[^\x20-\x7E]', ' ', content)  # Keep only printable ASCII characters
    content = content.replace('\n', ' ')
    return content

# Function to split content into chapters
def split_into_chapters(content):
    # Regex for splitting chapters, adjust based on the book's structure
    chapters = re.split(r'(Chapter \d+)', content)
    return chapters

# Function to extract paragraphs from each chapter
def extract_paragraphs(chapter_text):
    # Split paragraphs based on typical punctuation patterns that signify paragraph boundaries
    paragraphs = re.split(r'(?<=\.)\s+(?=[A-Z])', chapter_text)  # Splits on end-of-sentence followed by space and capital letter
    return [p.strip() for p in paragraphs if p.strip()]

# Function to append book data to a single CSV file
def append_to_csv(book_name, chapters, output_csv):
    with open(output_csv, 'a', newline='') as csvfile:
        writer = csv.writer(csvfile)

        chapter_num = 0
        for i in range(0, len(chapters), 2):
            if i + 1 < len(chapters):
                chapter_num += 1
                chapter_title = chapters[i] + chapters[i + 1]  # Chapter title like "Chapter 1"
                paragraphs = extract_paragraphs(chapter_title)
                for para_id, paragraph in enumerate(paragraphs, start=1):
                    writer.writerow([book_name, chapter_num, para_id, paragraph])

# Function to process multiple books from a CSV
def process_books_from_csv(input_csv, output_csv):
    # Read the CSV that contains the list of books and their paths
    with open(input_csv, 'r') as csvfile:
        reader = csv.DictReader(csvfile)

        # Loop through each book entry
        for row in reader:
            book_name = row['Book Title']
            book_path = f"../dataset/books/{row['File_name']}"

            # Check if the book file exists
            if not os.path.exists(book_path):
                print(f"Book not found: {book_path}")
                continue

            print(f"Processing book: {book_name}")
            try:
                # Extract the book content
                content = extract_book_content(book_path)

                # Split into chapters
                chapters = split_into_chapters(content)

                # Append the processed content into the single CSV
                append_to_csv(book_name, chapters, output_csv)
                print(f"Finished processing: {book_name}")
            except Exception as e:
                print(f"Error processing {book_name}: {e}")

# Main function
if __name__ == "__main__":
    input_csv = "../dataset/booklist.csv"  # Path to CSV containing book names and paths
    output_csv = "../dataset/books.csv"  # Path to the single output CSV file

    # Create/overwrite the output CSV file with the header
    with open(output_csv, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(["Book Name", "Chapter", "Paragraph_ID", "Paragraph"])  # Write header

    # Process books listed in the input CSV and append the output to the same CSV
    for i in range(2):
        process_books_from_csv(input_csv, output_csv)
