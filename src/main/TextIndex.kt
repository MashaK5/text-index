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
 * This class indicates that it is incorrect answer.
 * The user is informed about the rules for writing a answer.
 */
class IncorrectAnswerFormat(message: String): Exception(message)

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
fun doYouWantMakeRequest(filename: String) {
    if (haveIndexFile(filename)) {
        println("This file does not yet have a text index. The index will be compiled.")
        createIndexFile(filename)
        println("Index is compiled. Do you want to make a request? Enter \"yes\" or \"no\".")
    } else
        println(
            """This file already has a text index.
               Do you want to make a request? Enter "yes" or "no"."""
        )
}

/**
 * This function processes the response to the question if the user wants to make a request.
 */
fun processingFirstAnswer(): Boolean? {
    val wantToMakeRequest = readLine()
    when (wantToMakeRequest) {
        "yes" -> return true
        "no"  -> return false
        else  -> {
            IncorrectAnswerFormat("Incorrect answer format! Enter \"yes\" or \"no\"")
            processingFirstAnswer()
        }
    }
    return null
}

fun createIndexFile(filename: String) {
    val indexFile = nameOfIndexFile(filename)
    //дохрена всего
}



//индекс должен где-то храниться
//пункт с группами: есть уже какой-то список, который пользователь может пополнять
//файлик не изменится (если ловим исключение, говорим, что надо перестроить индекс)

fun main(args: Array<String>) {
    val logFile = createLogFile()

    try {
        val filename = isOneFile(args)
        isTXTFile(filename!!)// проверить, что файл состоит из русского связного текста
        doYouWantMakeRequest(filename)


    }
    catch (e: Exception) {
        /**
         * This block prints an error message to the screen and writes a description of the error to log.
         */
        logFile.appendText("$e\n")
    }
}