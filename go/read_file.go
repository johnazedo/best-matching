package main

import (
	"encoding/csv"
	"fmt"
	"os"
)



func ReadBooksCSV(data *[]Record)  error {

	file, err := os.Open("../dataset/books.csv")
	if err != nil {
		fmt.Println("Error opening file:", err)
		return err
	}
	defer file.Close()

	reader := csv.NewReader(file)

	records, err := reader.ReadAll()
	if err != nil {
		fmt.Println("Error reading csv", err)
		return err
	}

	for i, record := range records {
		if i == 0 {
			continue
		}

		*data = append(*data, Record{
			Bookname: record[0],
			Chapter: record[1],
			ParagraphID: record[2],
			ParagraphText: record[3],
		})
	}

	return nil
}