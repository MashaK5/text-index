## Project "Text index"

This program works with files in .txt format, composes an index of the text file and responds to user requests.  

Text files consist of lines, lines are collected into pages 
(each page contains exactly 45 lines, blank lines are not counted).

#### Description of correct format of input data:

1. **Filename**
    * one file in .txt format
    * the file contains coherent text in Russian
2. **Type of request**
    * one number from 1 to 3
3. **Request**
    * correct input data (see **Correct input data** for selected request)
    
- There must be separators (one space) between 1, 2 and 3 inputs.

                                                                                                                                                                                                                                                                                              
#### Types of requests:

1. Compilation of an index for a given file in txt format.  
    **Correct input data**: nothing  
    
2. Displaying information for a given file with a text index:
    * a list of a given number of the most common words;  
        **Correct input data**: one natural number  
        
    * complete information about the use of a given word
    (number of occurrences, used word forms, page numbers)  
        **Correct input data**: one word in Russian in initial form
        
    * data on the use of words from a given group
    (for example, pieces of furniture, verbs of movement, etc.).  
        **Correct input data**: several words in Russian in initial form, separated by spaces   
         
3. Displays all lines containing the specified word (in any of the word forms).  
    **Correct input data**: one word in Russian in initial form

#### Output (displayed on the screen)

1. Compilation of an index for a given file in txt format.  
    **Output**:
    > The index is built. 
    
2. Displaying information for a given file with a text index:
    * a list of a given number of the most common words;  
        **Output**: a list of words separated by commas with a space.  
        If the number of different words in the text is less than the specified number, all words will be displayed. 
        
    * complete information about the use of a given word
    (number of occurrences, used word forms, page numbers)   
    **Word in initial form!**  
 
        **Output**: 
        > Word:  
        Number of occurrences:  
        Used word forms:   
        Page numbers:
        
        > Word ... was not found.
        
    * data on the use of words from a given group
    (for example, pieces of furniture, verbs of movement, etc.).  
        **Output**: information output for each word in blocks from the previous paragraph.
        Blocks are separated by blank lines.
                 
3. Displays all lines containing the specified word (in any of the word forms).  
**Word in initial form!**  
    **Output**: lines containing the specified word, separated by blank lines.

   
#### Example
````bash
$ ./gradlew run --args="data/MyText.txt 2 плакать"

Word: плакать
Number of occurrences: 1
Used word forms: плакать
Page numbers: 1

````

