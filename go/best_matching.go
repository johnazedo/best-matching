package main

import (
	"math"
	"strings"
)


// getWordFrequency calculates the word frequency map for a given text
func getWordFrequency(text string) map[string]int {
	frequencyMap := make(map[string]int)
	words := strings.Fields(strings.ToLower(text)) // Split text into words and convert to lowercase
	for _, word := range words {
		frequencyMap[word]++
	}
	return frequencyMap
}

// cosineSimilarity calculates the cosine similarity between two word frequency maps
func cosineSimilarity(freq1, freq2 map[string]int) float64 {
	allWords := make(map[string]bool)
	for word := range freq1 {
		allWords[word] = true
	}
	for word := range freq2 {
		allWords[word] = true
	}

	dotProduct := 0
	norm1, norm2 := 0.0, 0.0

	for word := range allWords {
		count1 := freq1[word]
		count2 := freq2[word]

		dotProduct += count1 * count2
		norm1 += float64(count1 * count1)
		norm2 += float64(count2 * count2)
	}

	if norm1 == 0 || norm2 == 0 {
		return 0 // Avoid division by zero
	}
	return float64(dotProduct) / (math.Sqrt(norm1) * math.Sqrt(norm2))
}

// FindBestMatchingParagraph finds the record with the highest similarity to the input sentence
func FindBestMatchingParagraph(records []Record, inputSentence string) *Record {
	var bestMatch *Record
	highestSimilarity := 0.0

	inputFreqMap := getWordFrequency(inputSentence)

	for i := range records {
		paragraphFreqMap := getWordFrequency(records[i].ParagraphText)
		similarity := cosineSimilarity(inputFreqMap, paragraphFreqMap)
		records[i].Similarity = similarity

		if similarity > highestSimilarity {
			highestSimilarity = similarity
			bestMatch = &records[i]
		}
	}

	return bestMatch
}