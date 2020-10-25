package program

import java.io.File

/**
 * This file checks if the given file has a ready index
 * and collects the index either by the text file or by the index file.
 */

/**
 * This function checks for the existence of an index for this file,
 * collects it by the index file,
 * or creates a new one and writes to index file.
 */
fun makeIndex(numberedText: List<String>, vocabulary: Vocabulary, textFileName: String): Index {
    val indexFile = createIndexFile(textFileName)
    var index = Index()
    if (haveIndexFile(indexFile)) {
        index = makeIndexByIndexFile(indexFile)
    }
    else {
        index = createIndex(numberedText, vocabulary)
        makeIndexFileByIndex(index, indexFile)
    }
    return index
}

/**
 * This function shows how file with text index for [textFileName] should look.
 * @return path to index file
 */
fun createIndexFile(textFileName: String): File {
    val indexDir = File("indices/")
    indexDir.mkdir()
    val indexFileName = "$textFileName(index)"
    val indexFile = File(indexDir, indexFileName)
    return indexFile
}

/**
 * This function checks if [indexFile] has file with text index.
 */
fun haveIndexFile(indexFile: File): Boolean {
    return indexFile.exists()
}

/**
 * This function builds the file index.
 * Each line is separated by spaces into an array of words that are added to the dictionary.
 */
fun createIndex(numberedText: List<String>, vocabulary: Vocabulary): Index {
    val index = Index()

    numberedText.forEach {
        val words = it.split(" ")

        /**
         * The line looks like: (line,page) ...
         */
        val lineNumber = words[0].drop(1).substringBefore(",").toInt()
        val pageNumber = words[0].dropLast(1).substringAfter(",").toInt()
        words.map { onlyWord(it) }.forEach {
                word -> addWordToIndex(index, word, vocabulary, lineNumber, pageNumber)
        }
    }
    return index
}

/**
 * This function clears a word from a string of punctuation marks
 * that may have remained around it after being separated by spaces.
 */
fun onlyWord(wordWithPunctuationMarks: String): Word {
    val notRussianLetter = Regex("""([^a-zA-Z])""")
    val word = wordWithPunctuationMarks
        .substringAfter("$notRussianLetter")
        .substringBeforeLast("$notRussianLetter")
    return word
}

/**
 * This function adds a word to the index or updates the data for it if it is already in the index.
 */
fun addWordToIndex(index: Index, wordFromText: Word, vocabulary: Vocabulary, line:Int, page: Int): Index {
    val mainWordFord = vocabulary[wordFromText]
    if (mainWordFord != null) {
        /**
         * If the word is in the vocabulary, see if its main form has been added to the index.
         */
        val info = index[mainWordFord]
        if (info == null) {
            index[mainWordFord] = InformationAboutWord(
                1,
                mutableListOf(wordFromText),
                mutableListOf(page),
                mutableListOf(line)
            )
        }
        else {
            val (number, wordForms, pages, lines) = info
            wordForms.add(wordFromText)
            pages.add(page)
            listOf(line)
            index[mainWordFord] = InformationAboutWord(
                number + 1,
                wordForms,
                pages,
                lines
            )
        }
    }
    return index
}

/**
 * This function writes the index to the index file.
 * Each line contains one index element (a word and information elements about it, separated by commas with a space).
 */
fun makeIndexFileByIndex(index: Index, indexFile: File) {
    index.forEach {
        val word = it.key
        val numberOfOccur = it.value.numberOfOccurrences.toString()
        val usedWordForms = it.value.usedWordForms.joinToString(" ")
        val pageNum = it.value.pageNumbers.joinToString(" ")
        val linesNum = it.value.linesNumbers.joinToString(" ")

        val line = "$word, $numberOfOccur, $usedWordForms, $pageNum, $linesNum"
        indexFile.appendText(line)
    }
}

/**
 * This function reads the index from the file where it was previously written.
 * A word and all information about it is read from each line.
 * @return {Index} index
 */
fun makeIndexByIndexFile(indexFile: File): Index {
    val index = Index()
    indexFile.forEachLine {
        val wordAndInfo = it.split(", ")
        val word = wordAndInfo[0]

        val numberOfOccurrences = wordAndInfo[1].toInt()
        val usedWordForms = wordAndInfo[2].split(" ").toMutableList()
        val pageNumbers = wordAndInfo[3].split(" ").map { it.toInt() }.toMutableList()
        val linesNumbers = wordAndInfo[4].split(" ").map { it.toInt() }.toMutableList()

        val info = InformationAboutWord(numberOfOccurrences, usedWordForms, pageNumbers, linesNumbers)
        index[word] = info
    }
    return index
}
