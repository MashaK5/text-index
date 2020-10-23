## Project "Text index"
This program works with files in .txt format, composes an index of the text file and responds to user requests.

**Description of correct format of input data:**
* one file in .txt format
* the file contains coherent text in Russian

#### Program operation mode
* Compiling a text index for a file that does not already have one.
    >This file does not yet have a text index. The index will be compiled.  
    ...  
    Index compiled. Do you want to make a request? Enter "yes" or "no".

    >This file already has a text index.  
    Do you want to make a request? Enter "yes" or "no".
   
* Displays a response to a user request for a file that already has a text index.
    >Please select the type of request you want (enter the number):...                                                                                                                                                                                                                                                                                                                

#### Types of requests:
1. Get a list of the given number of the most common words.  
* **Input data:** one natural number
* **Output data:**
    
2. Get full information about the use of a given word 
    (number of occurrences, used word forms, page numbers).
* **Input data:** one word in Russian
* **Output data:**
        
3. Get full information (see item 2) about the use of words from a given group 
    (for example, furniture items, verbs of movement, etc.).
* **Input data:** nothing
* A list of existing groups will be displayed.
  Select one of the suggested groups (copy the line you want and paste into the input line) 
  or create a new one:
    - 
* **Output data:**
        
4. Output all lines containing a given word (in any of the word forms).
* **Input data:** one word in Russian
* **Output data:**
    
    


#### Example
````bash

````

