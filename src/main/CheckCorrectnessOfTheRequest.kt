package program

/**
 * This file validates the input data. Returns a valid request if correct.
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
    if (filename.length < 5) {
        throw IncorrectFilenameFormat("Incorrect filename format! Too small string.")
    }
    else {
        if ((filename.takeLast(4) != ".txt")&&(filename.length > 4)) {
            throw IncorrectFilenameFormat("Incorrect filename format! Expect one file in .txt format.")
        }
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
        if (!isRussianWord(inputData)) {
            throw IncorrectInputDataForRequest("Incorrect word for request type 2! Expect a word in Russian.")
        }
        DataOfRequest(Format.WORD, inputData)
    }
}

/**
 * This function checks if a word is Russian (primitive).
 */
fun isRussianWord(word: Word): Boolean {
    return when (word.length) {
        1 -> word.matches(Regex("""[а-яА-ЯёЁ]"""))
        2 -> word.matches(Regex("""[а-яА-ЯёЁ][а-яА-ЯёЁ]"""))
        else -> word.matches(Regex("""[а-яА-ЯёЁ][а-яА-Я-ёЁ]*[а-яА-ЯёЁ]"""))
    }
}

/**
 * This function checks if the input in [args] is correct group of words for request type 2,.
 * It should be words in Russian.
 * And concatenates all words into one line, separating them with spaces.
 */
fun correctListOfWord(args: Array<String>): DataOfRequest {
    val words = args.toList().drop(2)
    val isCorrectListOfWords = words.filterNot { isRussianWord(it) }.isEmpty()
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
    if (!isRussianWord(data)) {
        throw IncorrectInputDataForRequest("Incorrect word for request type 3! Expect a word in Russian.")
    }
    return DataOfRequest(Format.WORD, data)
}

