package program

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
    val usedWordForms:MutableSet<String>,
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
 * This function indexes the lines of a text file, excluding empty ones,
 * and writes them to list, adding its number and page number before the line.
 * @return text file without empty lines and with (index,page) before every line.
 */
fun numberingTextFile(textFileName: String): List<String> {
    val numberedText = mutableListOf<String>()
    val textFile = File(textFileName)
    textFile.useLines { lines -> lines.filterNot { it.isEmpty() }.forEachIndexed {
            index, line ->
        run {
            val lineNum = index + 1
            val pageNum = (index - index % 45) / 45 + 1
            numberedText.add("($lineNum,$pageNum) $line")
        }
    }
    }
    return numberedText.toList()
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
        it.forEachLine {
            val line = it.split(",")

            val oneWordForms = mutableListOf<Word>()
            line.forEach { oneWordForms.add(it) }
            /**
             * [oneWordForms[0]] is word
             * [oneWordForms[1]] is type of word
             * the rest of the list elements are word forms
             */
            if (isNotServiceWord(oneWordForms[1], oneWordForms[0])) {
                oneWordForms.forEach { vocabulary[it] = oneWordForms[0] }

            }
        }
    }
    return vocabulary
}

/**
 * This function checks that a word is not a preposition, union, particle, or interjection.
 */
fun isNotServiceWord(type: String, word: String): Boolean {
    when (type) {
        "част." -> return false
        "межд." -> return false
        "союз" -> return false
        "предл." -> return false
        "с" -> if (word.length == 1) return false
    }
    return true
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
        val textFileName = request.filename

        /**
         * This block prepares a text file and vocabulary.
         */
        val numberedText = numberingTextFile(textFileName)
        val vocabulary = createVocabulary()

        val index = makeIndex(numberedText, vocabulary, textFileName)
        val result = processingRequest(index, numberedText, request)
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





