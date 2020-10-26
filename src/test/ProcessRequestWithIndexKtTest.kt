package unitTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import program.*


internal class ProcessingRequest {

    @Test
    fun `first`() {
        val request = Request("data/Cloud.txt", TypeOfRequest.FIRST, null)
        val (_, typeOfRequest, dataOfRequest) = request
        val expected = "The index is built."
        val actual = processingRequest(index, numberedText, request )
        assertEquals(expected, actual)
    }

    @Test
    fun `second`() {
        val data = DataOfRequest(Format.NUMBER, "2")
        val request = Request("data/Cloud.txt", TypeOfRequest.SECOND, data)
        val (_, typeOfRequest, dataOfRequest) = request
        val expected = processingRequestTypeSecond(index, dataOfRequest!!)
        val actual = processingRequest(index, numberedText, request )
        assertEquals(expected, actual)
    }

    @Test
    fun `third`() {
        val data = DataOfRequest(Format.WORD, "облако")
        val request = Request("data/Cloud.txt", TypeOfRequest.THIRD, data)
        val (_, typeOfRequest, dataOfRequest) = request
        val expected = processingRequestTypeThird(index, dataOfRequest!!.data, numberedText)
        val actual = processingRequest(index, numberedText, request )
        assertEquals(expected, actual)
    }
}

internal class ProcessingRequestTypeSecond {

    @Test
    fun `number`() {
        val dataOfRequest = DataOfRequest(Format.NUMBER, "2")
        val expected = resultOfNumberRequest(index, "2")
        val actual = processingRequestTypeSecond(index, dataOfRequest)
        assertEquals(expected, actual)
    }

    @Test
    fun `word`() {
        val dataOfRequest = DataOfRequest(Format.WORD, "облаке")
        val expected = resultOfWordRequest(index, "облаке")
        val actual = processingRequestTypeSecond(index, dataOfRequest)
        assertEquals(expected, actual)
    }

    @Test
    fun `group`() {
        val dataOfRequest = DataOfRequest(Format.GROUP, "облаке зверь")
        val expected = resultOfGroupRequest(index, "облаке зверь")
        val actual = processingRequestTypeSecond(index, dataOfRequest)
        assertEquals(expected, actual)
    }
}

internal class ResultOfNumberRequest {

    @Test
    fun `small number`() {
        val expected = "яблоко, белый"
        val actual = resultOfNumberRequest(indexWithWordsWithDiffOccurs, "2")
        assertEquals(expected, actual)
    }

    @Test
    fun `big number`() {
        val expected = "яблоко, белый, облако, летать"
        val actual = resultOfNumberRequest(indexWithWordsWithDiffOccurs, "7")
        assertEquals(expected, actual)
    }

}

internal class ResultOfWordRequest {

    @Test
    fun `found`() {
        val expected = """
        |Word: яблоко
        |Number of occurrences: 1
        |Used word forms: яблока
        |Page numbers: 1
        """.trimMargin()
        val actual = resultOfWordRequest(index, "яблоко")
        assertEquals(expected, actual)
    }

    @Test
    fun `not found`() {
        val expected = "Word яблоки was not found."
        val actual = resultOfWordRequest(index, "яблоки")
        assertEquals(expected, actual)
    }

}

internal class ResultOfGroupRequest {

    @Test
    fun `test`() {
        val expected = """
        |Word: яблоко
        |Number of occurrences: 1
        |Used word forms: яблока
        |Page numbers: 1
        |
        |
        |Word яблоки was not found.
        """.trimMargin()
        val actual = resultOfGroupRequest(index, "яблоко яблоки")
        assertEquals(expected, actual)
    }

}

internal class ProcessingRequestTypeThird {

    @Test
    fun `found`() {
        val expected = """
            |Белого облака!
            |
            |лететь, Летать на облаке
        """.trimMargin()
        val actual = processingRequestTypeThird(index, "облако", numberedText)
        assertEquals(expected, actual)
    }

    @Test
    fun `not found`() {
        val expected = "Word зверь was not found."
        val actual = processingRequestTypeThird(index, "зверь", numberedText)
        assertEquals(expected, actual)
    }
}

internal class LineByNumber {

    @Test
    fun `line`() {
        val expected = "Белого облака!"
        val actual = lineByNumber(1, numberedText)
        assertEquals(expected, actual)
    }

    @Test
    fun `other line`() {
        val expected = "лететь, Летать на облаке"
        val actual = lineByNumber(3, numberedText)
        assertEquals(expected, actual)
    }
}

