package main

import (
	"fmt"
	"math"
	"sync"
)

// Strategy interface representing a base strategy
type Strategy interface {
	RunBestMatching(records []Record, inputSentences []string)
	GetApproachName() string
}

// ConcurrencyStrategy represents an abstract concurrency strategy
type ConcurrencyStrategy struct {
	NumOfThreads int // Number of threads to use
}

// NewConcurrencyStrategy initializes a new ConcurrencyStrategy with default threads
func NewConcurrencyStrategy() *ConcurrencyStrategy {
	return &ConcurrencyStrategy{NumOfThreads: 8}
}

// DivideListIntoChunks divides a slice of records into chunks
func (cs *ConcurrencyStrategy) DivideListIntoChunks(records []Record) [][]Record {
	chunkSize := cs.GetChunkSizeByNumberOfThreads(len(records))
	var chunks [][]Record

	for i := 0; i < len(records); i += chunkSize {
		end := i + chunkSize
		if end > len(records) {
			end = len(records)
		}
		chunks = append(chunks, records[i:end])
	}

	return chunks
}

// GetChunkSizeByNumberOfThreads calculates the chunk size based on total size and number of threads
func (cs *ConcurrencyStrategy) GetChunkSizeByNumberOfThreads(totalSize int) int {
	return int(math.Ceil(float64(totalSize) / float64(cs.NumOfThreads)))
}

// Run is an abstract method to be implemented by subclasses
func (cs *ConcurrencyStrategy) Run(records []Record, inputSentences []string, results *sync.Map) {
	
}

// RunBestMatching executes the strategy using concurrency
func (cs *ConcurrencyStrategy) RunBestMatching(records []Record, inputSentences []string) {
	fmt.Printf("Run %s approach\n\n", cs.GetApproachName())

	chunks := cs.DivideListIntoChunks(records)
	var wg sync.WaitGroup

	results := &sync.Map{}

	for _, chunk := range chunks {
		wg.Add(1)
		go func(chunk []Record) {
			defer wg.Done()
			cs.Run(chunk, inputSentences, results)
		}(chunk)
	}

	wg.Wait() // Wait for all goroutines to finish
}

// GetApproachName is a placeholder method for subclasses
func (cs *ConcurrencyStrategy) GetApproachName() string {
	return "Concurrency Strategy"
}