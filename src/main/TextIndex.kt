package program

import java.io.File
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
data class Request(val filename: String, val typeOfRequest: Int, val dataOfRequest: DataOfRequest?)

/**
 * This class stores data for request as a string and [formatData] for quick decryption of this line.
 * @formatData {String} "word", "number" or "list".
 */
data class DataOfRequest(val formatData: String, val data: String)

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
 * This function displays the result of program's work and writes it in output.txt
 */
fun workWithOutput(output: MutableList<String>) {
    for (line in output) {
        println(line)
        File("output.txt").appendText("$line\n")
    }
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
fun isCorrectTypeOfRequest(typeOfRequest: String): Int {
    return when (typeOfRequest.toIntOrNull()) {
        1 -> 1
        2 -> 2
        3 -> 3
        else -> throw IncorrectTypeOfRequest("Incorrect type of request! Expect one number from 1 to 3.")
    }
}

/**
 * This function checks if the input in [args] is correct for the specified request type.
 */
fun isCorrectDataForRequest(typeOfRequest: Int, args: Array<String>): DataOfRequest?  {
    when (typeOfRequest) {
        1 -> isCorrectDataForFirstRequest(args)
        2 -> return isCorrectDataForSecondRequest(args)
        3 -> return isCorrectDataForThirdRequest(args)
    }
    return null
}

/**
 * This function checks if the input in [args] is correct for request type 1.
 */
fun isCorrectDataForFirstRequest(args: Array<String>) {
    if (args.size != 2) {
        throw IncorrectInputDataForRequest("Too many arguments after filename and type of request for request type 1!")
    }
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
    return if (inputData.toIntOrNull() != null) {
        if (inputData.toInt() <= 0) {
            throw IncorrectInputDataForRequest("Incorrect number for request type 2! Expect a natural number.")
        }
        DataOfRequest("number", inputData)
    }
    else {
        if (!inputData.matches(Regex("""([а-яА-Я-])"""))) {
            throw IncorrectInputDataForRequest("Incorrect word for request type 2! Expect a word in Russian.")
        }
        DataOfRequest("number", inputData)
    }
}

/**
 * This function checks if the input in [args] is correct group of words for request type 2,.
 * It should be words in Russian.
 * And concatenates all words into one line, separating them with spaces.
 */
fun correctListOfWord(args: Array<String>): DataOfRequest {
    val words = args.toList().drop(2)
    val isCorrectListOfWords = words.filterNot { it.matches(Regex("""([а-яА-Я-])""")) }.isEmpty()
    if (!isCorrectListOfWords) {
        throw IncorrectInputDataForRequest("Incorrect group of words for request type 2! Expect words in Russian.")
    }
    val wordsInString = words.reduce { current, next -> current + "" + next}
    return DataOfRequest("list", wordsInString)
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
    return DataOfRequest("word", data)
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


fun main(args: Array<String>) {
    val output = mutableListOf<String>()
    File("output.txt").delete()
    val logFile = createLogFile()

    try {
        val request = createCorrectRequest(args)
        //print(request)
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



///**
// * This function shows how file with text index for [filename] should look.
// * @return path to index file
// */
//fun nameOfIndexFile(filename: String): File {
//    val indexDir = File("indices/")
//    indexDir.mkdir()
//    val indexFileName = "index_$filename"
//    val indexFile = File(indexDir, indexFileName)
//    return indexFile
//}
//
///**
// * This function checks if [filename] has file with text index.
// */
//fun haveIndexFile(filename: String): Boolean {
//    return nameOfIndexFile(filename).exists()
//}
//

