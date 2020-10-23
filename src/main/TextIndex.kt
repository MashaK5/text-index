package program

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * This program works with files in .txt format, composes an index of the text file and responds to user requests.
 * @param {String} filename (file in .txt format)
 */

/**
 * This class indicates that an incorrect number of files are filed or in an incorrect format.
 */
class IncorrectArgsFormat(message: String): Exception(message)

/**
 * This class stores the type and input data of the request.
 */
data class Request(val typeOfRequest: Int, val inputData: String)


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
 * This function checks that user has given only one filename.
 */
fun isOneFile(args: Array<String>): String? {
    when (args.size) {
        0 -> IncorrectArgsFormat("No files are listed! Expect one file in .txt format.")
        1 -> { return args[0] }
        else -> IncorrectArgsFormat("Too many arguments! Expect one file in .txt format.")
    }
    return null
}

/**
 * This function checks that [filename] in .txt format.
 */
fun isTXTFile(filename: String) {
    if (filename.takeLast(4) != ".txt") {
        IncorrectArgsFormat("Incorrect file format! Expect one file in .txt format.")
    }
}

/**
 * This function shows how file with text index for [filename] should look.
 * @return path to index file
 */
fun nameOfIndexFile(filename: String): File {
    val indexDir = File("indices/")
    indexDir.mkdir()
    val indexFileName = "index_$filename"
    val indexFile = File(indexDir, indexFileName)
    return indexFile
}

/**
 * This function checks if [filename] has file with text index.
 */
fun haveIndexFile(filename: String): Boolean {
    return nameOfIndexFile(filename).exists()
}

/**
 * This function creates an index of the text for [filename], if it does not already exist,
 * and asks if the user wants to make a request.
 */
fun questionAboutTheDesireToRequest(filename: String) {
    if (haveIndexFile(filename)) {
        println("This file does not yet have a text index. The index will be compiled.")
        createIndexFile(filename)
        println("Index is compiled. Do you want to make a request? Enter \"yes\" or \"no\".")
    } else
        println(
            """This file already has a text index.
               Do you want to make a request? Enter "yes" or "no".""")
}

/**
 * This function processes the answer to the question if the user wants to make a request.
 */
fun processingAnswerAboutTheDesireToRequest(): Boolean {
    val wantToMakeRequest = readLine()
    when (wantToMakeRequest) {
        "yes" -> return true
        "no"  -> return false
        else  -> {
            println("Incorrect answer format! Enter \"yes\" or \"no\".")
            processingAnswerAboutTheDesireToRequest()
        }
    }
    return false
}

fun createIndexFile(filename: String) {
    val indexFile = nameOfIndexFile(filename)
    //дохрена всего
}

/**
 * This function offers to select the type of request.
 */
fun questionAboutTypeOfRequest() {
    println(
        """Please select the type of request you want (enter the number):
            1. Get a list of the given number of the most common words.
            2. Get full information about the use of a given word
            (number of occurrences, used word forms, page numbers).
            3. Get full information (see item 2) about the use of words from a given group 
            (for example, furniture items, verbs of movement, etc.).
            4. Output all lines containing a given word (in any of the word forms).""")

}

/**
 * This function processes the answer to the question about type of request.
 */
fun processingAnswerAboutTypeOfRequest(): Int {
    val typeOfRequest = readLine()
    when (typeOfRequest!!.toIntOrNull()) {
        1 -> return 1
        2 -> return 2
        3 -> return 3
        4 -> return 4
        else -> {
            println("Incorrect answer format! Enter a number from 1 to 4.")
            processingAnswerAboutTypeOfRequest()
        }
    }
    return -1
}

/**
 * This function prompts you to enter data for the request.
 */
fun offerToEnterDataForRequest(typeOfRequest: Int): Request {
    when (typeOfRequest) {
        1 -> println("Enter one natural number.")
        2 -> println("Enter one word in Russian.")
        3 -> println(
                """Choose one group from the proposed ones (copy and paste into the input line) or enter your own
                    (expect name of group, :, list of words with spaces):
                    Части тела:рука нога нос уши глаза губы колено плечо шея
                    Птицы:голубь снегирь воробей иволга
                    Звери:лошадь лиса осел тигр кошка заяц чучундра""")
        4 -> println("Enter one word in Russian.")
    }

    val inputData = readLine()
    when (typeOfRequest) {
        1 -> if (inputValidationForFirstType(inputData!!))
            return Request(1, inputData)

        2 -> if (inputData!!.matches(Regex("""([а-яА-Я-])""")))
            return Request(2, inputData)

        3 -> if (inputValidationForThirdType(inputData!!))
            return Request(3, inputData)

        4 -> if (inputData!!.matches(Regex("""([а-яА-Я-])""")))
            return Request(4, inputData)
    }
    println("Incorrect answer format!")
    offerToEnterDataForRequest(typeOfRequest)
    return Request(-1, "-1")
}

/**
 * This function checks the correctness of the input data for the first type of request.
 * @format one natural number
 */
fun inputValidationForFirstType(inputData: String): Boolean {
    if (inputData.toIntOrNull() != null)
        if (inputData.toInt() > 0)
            return true
    return false
}

/**
 * This function checks the correctness of the input data for the third type of request.
 * @format <name>:<word> <word> ...
 */
fun inputValidationForThirdType(inputData: String): Boolean {
    val dataOfGroup = inputData.split(":")

    if (dataOfGroup.size == 2) {
        val name = dataOfGroup[0]
        val words = dataOfGroup[1].split(" ")

        /**
         * This block checks if the input data are Russian words.
         * Spaces are allowed in the group name.
         */
        val isCorrectName = name.matches(Regex("""([а-яА-Я- ])"""))
        val isCorrectListOfWords = words.filterNot { it.matches(Regex("""([а-яА-Я-])""")) }.isEmpty()
        if (isCorrectName && isCorrectListOfWords)
            return true
    }
    return false
}



//индекс должен где-то храниться
//файлик не изменится (если ловим исключение, говорим, что надо перестроить индекс)

fun main(args: Array<String>) {
    val logFile = createLogFile()

    try {
        val filename = isOneFile(args)
        isTXTFile(filename!!)                          // проверить, что файл состоит из русского связного текста

        questionAboutTheDesireToRequest(filename)
        if (processingAnswerAboutTheDesireToRequest()) {
            questionAboutTypeOfRequest()
            val typeOfRequest = processingAnswerAboutTypeOfRequest()
            val request = offerToEnterDataForRequest(typeOfRequest)
        }
    }
    catch (e: Exception) {
        /**
         * This block prints an error message to the screen and writes a description of the error to log.
         */
        logFile.appendText("$e\n")
    }
}