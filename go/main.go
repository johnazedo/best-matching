package main

import "fmt"

func main() {
	
	var data []Record
	err := ReadBooksCSV(&data)

	if err != nil {
		fmt.Println("Unable to read CSV file: ", err)
		return 
	}

	sentences := []string{
		"Machine learning basics and algorithms",
        "Understanding natural language processing techniques for sentiment analysis",
        "Downsizing in response to increasing competition in the food industry often has pushed the responsibility for ingredient quality to the suppliers",
        "There is an important opportunity here, and some might say an integral one, to include participants in the research process",
	}
	
	

}

