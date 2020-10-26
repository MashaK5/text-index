package program

/**
 * This function processes a request for a file that already has a file index.
 */
fun processingRequest(index: Index, numberedText: List<String>, request: Request): String {
    val (_, typeOfRequest, dataOfRequest) = request
    return when (typeOfRequest) {
        TypeOfRequest.FIRST -> "The index is built."
        TypeOfRequest.SECOND -> processingRequestTypeSecond(index, dataOfRequest!!)
        TypeOfRequest.THIRD -> processingRequestTypeThird(index, dataOfRequest!!.data, numberedText)
    }
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
    if (number > index.size) {
        number = index.size
    }
    val sortedIndex = index.toList().sortedByDescending { it.second.numberOfOccurrences }
    val result = sortedIndex.take(number).joinToString(", ") { it.first }
    return result
}

/**
 * This function processes a request for information about a given word.
 * @return the result of processing the request as a string
 */
fun resultOfWordRequest(index: Index, word: String): String {
    val searchedWordInfo = index[word]
    return if (searchedWordInfo != null) {
        val result = """
            |Word: $word
            |Number of occurrences: ${searchedWordInfo.numberOfOccurrences}
            |Used word forms: ${searchedWordInfo.usedWordForms.joinToString(", ")}
            |Page numbers: ${searchedWordInfo.pageNumbers.joinToString(", ")}
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
    val result = words.joinToString("\n\n\n") { resultOfWordRequest(index, it) }
    return result
}

/**
 * This function processes a request of the third type to display all lines where a word occurs.
 * @return the result of processing the request as a string
 */
fun processingRequestTypeThird(index: Index, word: String, numberedText: List<String>): String {
    val searchedWordInfo = index[word]
    return if (searchedWordInfo != null) {
        val linesNumbers = searchedWordInfo.linesNumbers
        val result = linesNumbers.joinToString("\n\n") { lineByNumber(it, numberedText) }
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
fun lineByNumber(lineNumber: Int, numberedText: List<String>): String {
    numberedText.forEach {
        val thisLineNumber = it.drop(1).substringBefore(",").toInt()
        if (lineNumber == thisLineNumber) {
            val line = it.substringAfter(") ")
            return line
        }
    }
    return ""
}