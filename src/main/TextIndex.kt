@file:Suppress("UNREACHABLE_CODE")

package program

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This program works with files in .txt format, composes an index of the text file and responds to user requests.
 */

/**
 * This class indicates incorrect number of arguments.
 * There must be at least 2 of them.
 */
class IncorrectNumberOfArgs(message: String): Exception(message)

/**
 * This class indicates that file an incorrect format.
 */
class IncorrectFilenameFormat(message: String): Exception(message)

/**
 * This class indicates that type of request is not number from 1 to 3.
 */
class IncorrectTypeOfRequest(message: String): Exception(message)

/**
 * This class indicates that input data for request an incorrect format (see correct input data format in README.md).
 */
class IncorrectInputDataForRequest(message: String): Exception(message)

/**
 * This class stores filename, the type of the request and input data for request.
 */
data class Request(val filename: String, val typeOfRequest: TypeOfRequest, val dataOfRequest: DataOfRequest?)

/**
 * This class stores all possible types of valid requests.
 */
enum class TypeOfRequest {
    FIRST, SECOND, THIRD
}

/**
 * This class stores data for request as a string and [formatData] for quick decryption of this line.
 * @formatData {String} "word", "number" or "group".
 */
data class DataOfRequest(val formatData: Format, val data: String)

/**
 * This class stores three types of input data format for a request.
 */
enum class Format {
    NUMBER, WORD, GROUP
}

/**
 * This class stores all the necessary information about the word.
 */
data class InformationAboutWord(
    val numberOfOccurrences: Int,
    val usedWordForms:MutableList<String>,
    val pageNumbers: MutableList<Int>,
    val linesNumbers: MutableList<Int>
)

/**
 * This is the type of word in the index.
 */
typealias Word = String

/**
 * This is the type of index.
 */
typealias Index = HashMap<Word, InformationAboutWord>

/**
 * This class will store the vocabulary.
 * @key word form
 * @value main word form
 */
typealias Vocabulary = HashMap<Word, Word>


/**
 * This function generates a log file with the exact time in the name in the folder "logs".
 * @return log file
 * The log file will contain explanations of the type of error.
 */
fun createLogFile(): File {
    val logsDir = File("logs/")
    logsDir.mkdir()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.now().format(formatter)
    val logName = "$dateTime.log"
    val logFile = File(logsDir, logName)
    return logFile
}

/**
 * This function checks that there are at least two arguments: file name and request type.
 */
fun isCorrectNumberOfArgs(args: Array<String>) {
    if (args.size < 2) {
        throw IncorrectNumberOfArgs("Too few arguments! Expect at least two arguments: filename and type of request.")
    }
}

/**
 * This function checks that [filename] in .txt format.
 */
fun isCorrectFilename(filename: String): String {
    if (filename.takeLast(4) != ".txt") {
       throw IncorrectFilenameFormat("Incorrect filename format! Expect one file in .txt format.")
    }
    return filename
}

/**
 * This function checks that [typeOfRequest] is number from 1 to 3.
 */
fun isCorrectTypeOfRequest(typeOfRequest: String): TypeOfRequest {
    return when (typeOfRequest.toIntOrNull()) {
        1 -> TypeOfRequest.FIRST
        2 -> TypeOfRequest.SECOND
        3 -> TypeOfRequest.THIRD
        else -> throw IncorrectTypeOfRequest("Incorrect type of request! Expect one number from 1 to 3.")
    }
}

/**
 * This function checks if the input in [args] is correct for the specified request type.
 */
fun isCorrectDataForRequest(typeOfRequest: TypeOfRequest, args: Array<String>): DataOfRequest?  {
    return when (typeOfRequest) {
        TypeOfRequest.FIRST -> isCorrectDataForFirstRequest(args)
        TypeOfRequest.SECOND -> isCorrectDataForSecondRequest(args)
        TypeOfRequest.THIRD -> isCorrectDataForThirdRequest(args)
    }
}

/**
 * This function checks if the input in [args] is correct for request type 1.
 */
fun isCorrectDataForFirstRequest(args: Array<String>): DataOfRequest? {
    if (args.size != 2) {
        throw IncorrectInputDataForRequest("Too many arguments after filename and type of request for request type 1!")
    }
    return null
}

/**
 * This function checks if the input in [args] is correct for request type 1.
 * It must be one word in Russian, or one natural number, or a list of words in Russian.
 */
fun isCorrectDataForSecondRequest(args: Array<String>): DataOfRequest {
    return when (args.size) {
        2 -> throw IncorrectInputDataForRequest("Too few arguments after filename and type of request for request type 2!")
        3 -> numberOrWord(args[2])
        else -> correctListOfWord(args)
    }
}

/**
 * This function checks if [inputData] is a valid number or a word.
 */
fun numberOrWord(inputData: String): DataOfRequest {
    /**
     * Checking that this is a valid number.
     */
    return if (inputData.toIntOrNull() != null) {
        if (inputData.toIntOrNull()!! <= 0) {
            throw IncorrectInputDataForRequest("Incorrect number for request type 2! Expect a natural number.")
        }
        DataOfRequest(Format.NUMBER, inputData)
    }
    /**
     * Checking that this is a correct word.
     */
    else {
        if (!inputData.matches(Regex("""[а-яА-Я]([а-я-])[а-я]"""))) {
            throw IncorrectInputDataForRequest("Incorrect word for request type 2! Expect a word in Russian.")
        }
        DataOfRequest(Format.WORD, inputData)
    }
}

/**
 * This function checks if the input in [args] is correct group of words for request type 2,.
 * It should be words in Russian.
 * And concatenates all words into one line, separating them with spaces.
 */
fun correctListOfWord(args: Array<String>): DataOfRequest {
    val words = args.toList().drop(2)
    val isCorrectListOfWords = words.filterNot { it.matches(Regex("""[а-яА-Я]([а-я-])[а-я]""")) }.isEmpty()
    if (!isCorrectListOfWords) {
        throw IncorrectInputDataForRequest("Incorrect group of words for request type 2! Expect words in Russian.")
    }
    val wordsInString = words.joinToString(" ")
    return DataOfRequest(Format.GROUP, wordsInString)
}

/**
 * This function checks if the input in [args] is correct for request type 3.
 * It should be one word in Russian.
 */
fun isCorrectDataForThirdRequest(args: Array<String>): DataOfRequest {
    if (args.size != 3) {
        throw IncorrectInputDataForRequest("Wrong amount of arguments after filename and type of request for request type 3!")
    }
    val data = args[2]
    if (!data.matches(Regex("""([а-яА-Я-])"""))) {
        throw IncorrectInputDataForRequest("Incorrect word for request type 3! Expect a word in Russian.")
    }
    return DataOfRequest(Format.WORD, data)
}

/**
 * This function combines the results of all checks and returns the correct request.
 */
fun createCorrectRequest(args: Array<String>): Request {
    isCorrectNumberOfArgs(args)
    val filename = isCorrectFilename(args[0])
    val typeOfRequest = isCorrectTypeOfRequest(args[1])
    val inputData = isCorrectDataForRequest(typeOfRequest, args)
    val request = Request(filename, typeOfRequest, inputData)
    return request
}


/**
 * This program handles the correct request.
 * @return {String} result of program work
 */
fun processingRequest(request: Request): String {
    val (textFileName, typeOfRequest, dataOfRequest) = request
    val numberedTextFile = numberingTextFile(textFileName)
    val vocabulary = createVocabulary()

    var index = Index()
    val indexFile = createIndexFile(textFileName)

    /**
     * This block checks for the existence of an index for this file,
     * collects it by the index file,
     * or creates a new one and writes to index file.
     */
    if (!haveIndexFile(indexFile)) {
        index = createIndex(numberedTextFile, vocabulary)
        makeIndexFileByIndex(index, indexFile)
    }
    else
        index = makeIndexByIndexFile(indexFile)

    /**
     * This block processes a request for a file that already has a file index.
     */
    return when (typeOfRequest) {
        TypeOfRequest.FIRST -> "The index is built."
        TypeOfRequest.SECOND -> processingRequestTypeSecond(index, dataOfRequest!!)
        TypeOfRequest.THIRD -> processingRequestTypeThird(index, dataOfRequest!!.data, numberedTextFile)
    }
    numberedTextFile.delete()
}

/**
 * This function indexes the lines of a text file, excluding empty ones,
 * and writes them to a new file, adding its number and page number before the line.
 * @return text file without empty lines and with (index,page) before every line.
 */
fun numberingTextFile(textFileName: String): File {
    val textFileWithNumberingName = textFileName.dropLast(4) + "_num.txt"
    val textFileWithNumbering = File(textFileWithNumberingName)

    val textFile = File(textFileName)
    textFile.useLines { lines -> lines.filterNot { it.isEmpty() }.forEachIndexed {
            index, line ->
        run {
            val page = (index - index % 45) / 45
            textFileWithNumbering.appendText("($index,$page) $line")
        }
    }
    }
    return textFileWithNumbering
}

/**
 * This function processes odict.csv and writes data from it to the vocabulary.
 * @keys word forms
 * @values main word forms (word)
 */
fun createVocabulary(): Vocabulary {
    val vocabulary = Vocabulary()
    val odictCSVPath = "odict.csv"

    Files.newBufferedReader(Paths.get(odictCSVPath), Charset.forName("Windows-1251")).use {
            line -> CSVParser(line, CSVFormat.DEFAULT).use {
                val oneWordForms = mutableListOf<Word>()
                it.forEach { oneWordForms.add(it.toString()) }

                /**
                 * [oneWordForms[0]] is word
                 * [oneWordForms[1]] is type of word
                 * the rest of the list elements are word forms
                 */
                if (isNotServiceWord(oneWordForms[1])) {
                    oneWordForms.forEach { vocabulary[it] = oneWordForms[0] }
                }
            }
    }
    return vocabulary
}

/**
 * This function checks that a word is not a preposition, union, particle, or interjection.
 */
fun isNotServiceWord(word: String): Boolean {
    when (word) {
        "част." -> return false
        "межд." -> return false
        "союз" -> return false
        "предл." -> return false
        "с" -> if (word.length == 1) return false
    }
    return true
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
fun createIndex(numberedTextFile: File, vocabulary: Vocabulary): Index {
    val index = Index()

    numberedTextFile.useLines { lines -> lines.forEach {
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

/**
 * This function specifies which request for the second type of request should be processed.
 * @return the result of processing the request as a string
 */
fun processingRequestTypeSecond(index: Index, dataOfRequest: DataOfRequest): String {
    val (format, data) = dataOfRequest
    return when (format) {
        Format.NUMBER -> resultOfNumberRequest(index, data)
        Format.WORD -> resultOfWordRequest(index, data)
        Format.GROUP -> resultOfGroupRequest(index, data)
    }
}

/**
 * This function processes a request for a list of the specified number of most frequent words.
 * @return the result of processing the request as a string
 */
fun resultOfNumberRequest(index: Index, data: String): String {
    var number = data.toIntOrNull()!!
    if (number < index.size) {
        number = index.size
    }
    val commonWords = index.toList().sortedByDescending { it.second.numberOfOccurrences }.take(number)
    val result = commonWords.joinToString(", ") { "$it.first" }
    return result
}

/**
 * This function processes a request for information about a given word.
 * @return the result of processing the request as a string
 */
fun resultOfWordRequest(index: Index, word: String): String {
    val searchedWordInfo = index[word]
    return if (searchedWordInfo != null) {
        val result = """Word: $word
            |Number of occurrences: ${searchedWordInfo.numberOfOccurrences}
            |Used word forms: ${searchedWordInfo.usedWordForms.joinToString(" ")}
            |Page numbers: ${searchedWordInfo.pageNumbers.joinToString(" ")}
        """.trimMargin()
        result
    }
    else "Word $word was not found."
}

/**
 * This function processes a request for information about each word of a given group of words.
 * @return the result of processing the request as a string
 */
fun resultOfGroupRequest(index: Index, data: String): String {
    val words = data.split(" ")
    val result = words.joinToString("/n/n/n") { resultOfWordRequest(index, it) }
    return result
}

/**
 * This function processes a request of the third type to display all lines where a word occurs.
 * @return the result of processing the request as a string
 */
fun processingRequestTypeThird(index: Index, word: String, numberedTextFile: File): String {
    val searchedWordInfo = index[word]
    return if (searchedWordInfo != null) {
        val linesNumbers = searchedWordInfo.linesNumbers
        val result = linesNumbers.joinToString("/n/n") { lineByNumber(it, numberedTextFile) }
        result
    }
    else {
        val result = "Word $word was not found."
        result
    }
}

/**
 * This function finds a string by its number in a text file and returns it.
 */
fun lineByNumber(lineNumber: Int, numberedTextFile: File): String {
    numberedTextFile.useLines { lines -> lines.forEach {
        val thisLineNumber = it.drop(1).substringBefore(",").toInt()
        if (lineNumber == thisLineNumber) {
            val line = it.substringAfter(") ")
            return line
        }
    }
    }
    return ""
}

/**
 * This function displays the result of program's work and writes it in output.txt
 */
fun workWithOutput(output: MutableList<String>) {
    for (line in output) {
        println(line)
        File("output.txt").appendText("$line\n")
    }
}

fun main(args: Array<String>) {
    val output = mutableListOf<String>()
    File("output.txt").delete()
    val logFile = createLogFile()

    try {
        val request = createCorrectRequest(args)
        val result = processingRequest(request)
        output.add(result)
    }
    catch (e: Exception) {
        /**
         * This block prints an error message to the screen and writes a description of the error to log.
         */
        logFile.appendText("$e\n")
        output.add("$e")
    }
    workWithOutput(output)
}





