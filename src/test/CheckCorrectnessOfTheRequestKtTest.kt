package unitTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import program.*

internal class CreateCorrectRequest {

    @Test
    fun `just correct args`() {
        val args = arrayOf("data/Read.txt", "2", "слово", "второе")
        val inputData = DataOfRequest(Format.GROUP, "слово второе")
        val expected = Request("data/Read.txt", TypeOfRequest.SECOND, inputData)
        val actual = createCorrectRequest(args)
        assertEquals(expected, actual)
    }
}

internal class IsCorrectNumberOfArgs {

    @Test
    fun `empty array`() {
        val args = emptyArray<String>()
        val expected = "Too few arguments! Expect at least two arguments: filename and type of request."
        val actual = assertThrows(IncorrectNumberOfArgs::class.java) {
            isCorrectNumberOfArgs(args)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `one arg`() {
        val args = arrayOf("file.txt")
        val expected = "Too few arguments! Expect at least two arguments: filename and type of request."
        val actual = assertThrows(IncorrectNumberOfArgs::class.java) {
            isCorrectNumberOfArgs(args)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `two arg`() {
        val args = arrayOf("file.txt", "rrrrr")
        val actual = assertDoesNotThrow {
            isCorrectNumberOfArgs(args)
        }
    }
}

internal class IsCorrectFilename {

    @Test
    fun `small filename`() {
        val filename = ".txt"
        val expected = "Incorrect filename format! Too small string."
        val actual = assertThrows(IncorrectFilenameFormat::class.java) {
            isCorrectFilename(filename)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `incorrect filename`() {
        val filename = "dddddd.p"
        val expected = "Incorrect filename format! Expect one file in .txt format."
        val actual = assertThrows(IncorrectFilenameFormat::class.java) {
            isCorrectFilename(filename)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `correct filename`() {
        val filename = "data/Read.txt"
        val expected = "data/Read.txt"
        val actual = isCorrectFilename(filename)
        assertEquals(expected, actual)
    }
}

internal class IsCorrectTypeOfRequest {

    @Test
    fun `not number`() {
        val type = "jhsdj"
        val expected = "Incorrect type of request! Expect one number from 1 to 3."
        val actual = assertThrows(IncorrectTypeOfRequest::class.java) {
            isCorrectTypeOfRequest(type)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `incorrect number`() {
        val type = "5"
        val expected = "Incorrect type of request! Expect one number from 1 to 3."
        val actual = assertThrows(IncorrectTypeOfRequest::class.java) {
            isCorrectTypeOfRequest(type)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `first`() {
        val type = "1"
        val expected = TypeOfRequest.FIRST
        val actual = isCorrectTypeOfRequest(type)
        assertEquals(expected, actual)
    }

    @Test
    fun `second`() {
        val type = "2"
        val expected = TypeOfRequest.SECOND
        val actual = isCorrectTypeOfRequest(type)
        assertEquals(expected, actual)
    }

    @Test
    fun `third`() {
        val type = "3"
        val expected = TypeOfRequest.THIRD
        val actual = isCorrectTypeOfRequest(type)
        assertEquals(expected, actual)
    }
}

internal class IsCorrectDataForRequest {

    @Test
    fun `first`() {
        val type = TypeOfRequest.FIRST
        val args = arrayOf("data/Read.txt", "1")
        val expected = isCorrectDataForFirstRequest(args)
        val actual = isCorrectDataForRequest(type, args)
        assertEquals(expected, actual)
    }

    @Test
    fun `second`() {
        val type = TypeOfRequest.SECOND
        val args = arrayOf("data/Read.txt", "2", "слово")
        val expected = isCorrectDataForSecondRequest(args)
        val actual = isCorrectDataForRequest(type, args)
        assertEquals(expected, actual)
    }

    @Test
    fun `third`() {
        val type = TypeOfRequest.THIRD
        val args = arrayOf("data/Read.txt", "3", "пих")
        val expected = isCorrectDataForThirdRequest(args)
        val actual = isCorrectDataForRequest(type, args)
        assertEquals(expected, actual)
    }
}

internal class IsCorrectDataForFirstRequest {

    @Test
    fun `not two args`() {
        val args = arrayOf("data/Read.txt", "1", "yy")
        val expected = "Too many arguments after filename and type of request for request type 1!"
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            isCorrectDataForFirstRequest(args)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `two args`() {
        val args = arrayOf("data/Read.txt", "1")
        val expected = null
        val actual = isCorrectDataForFirstRequest(args)
        assertEquals(expected, actual)
    }
}

internal class IsCorrectDataForSecondRequest {

    @Test
    fun `two args`() {
        val args = arrayOf("data/Read.txt", "2")
        val expected = "Too few arguments after filename and type of request for request type 2!"
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            isCorrectDataForSecondRequest(args)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `three args`() {
        val args = arrayOf("data/Read.txt", "2", "слово")
        val expected = numberOrWord(args[2])
        val actual = isCorrectDataForSecondRequest(args)
        assertEquals(expected, actual)
    }

    @Test
    fun `more args`() {
        val args = arrayOf("data/Read.txt", "2", "yyj", "8")
        val expected = assertThrows(IncorrectInputDataForRequest::class.java) {
            correctListOfWord(args)
        }
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            isCorrectDataForSecondRequest(args)
        }
        assertEquals(expected.message, actual.message)
    }
}

internal class NumberOrWord {

    @Test
    fun `not natural number`() {
        val data = "-3"
        val expected = "Incorrect number for request type 2! Expect a natural number."
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            numberOrWord(data)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `natural number`() {
        val data = "3"
        val expected = DataOfRequest(Format.NUMBER, data)
        val actual = numberOrWord(data)
        assertEquals(expected, actual)
    }

    @Test
    fun `not correct word`() {
        val data = "печаль("
        val expected = "Incorrect word for request type 2! Expect a word in Russian."
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            numberOrWord(data)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `not word`() {
        val data = "43mlkmslk"
        val expected = "Incorrect word for request type 2! Expect a word in Russian."
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            numberOrWord(data)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `correct word`() {
        val data = "работа"
        val expected = DataOfRequest(Format.WORD, data)
        val actual = numberOrWord(data)
        assertEquals(expected, actual)
    }
}

internal class IsRussianWord {

    @Test
    fun `not correct word`() {
        val data = "-печаль"
        val expected = false
        val actual = isRussianWord(data)
        assertEquals(expected, actual)
    }

    @Test
    fun `not word`() {
        val data = "43mlkmslk"
        val expected = false
        val actual = isRussianWord(data)
        assertEquals(expected, actual)
    }

    @Test
    fun `correct word`() {
        val data = "чуть-чуть"
        val expected = true
        val actual = isRussianWord(data)
        assertEquals(expected, actual)
    }
}

internal class CorrectListOfWord {

    @Test
    fun `correct list`() {
        val args = arrayOf("data/Read.txt", "2", "обложка", "картошка", "скатерть-самобранка")
        val expected = DataOfRequest(Format.GROUP,"обложка картошка скатерть-самобранка")
        val actual = correctListOfWord(args)
        assertEquals(expected, actual)
    }

    @Test
    fun `not correct list`() {
        val args = arrayOf("data/Read.txt", "2", "обложка", "картошка", "shajbskj3")
        val expected = "Incorrect group of words for request type 2! Expect words in Russian."
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            correctListOfWord(args)
        }
        assertEquals(expected, actual.message)
    }
}

internal class IsCorrectDataForThirdRequest {

    @Test
    fun `correct word`() {
        val args = arrayOf("data/Read.txt", "2", "лиса")
        val expected = DataOfRequest(Format.WORD, args[2])
        val actual = isCorrectDataForThirdRequest(args)
        assertEquals(expected, actual)
    }

    @Test
    fun `not correct word`() {
        val args = arrayOf("data/Read.txt", "2", "shajbskj3")
        val expected = "Incorrect word for request type 3! Expect a word in Russian."
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            isCorrectDataForThirdRequest(args)
        }
        assertEquals(expected, actual.message)
    }

    @Test
    fun `too much args`() {
        val args = arrayOf("data/Read.txt", "2", "обложка", "картошка", "shajbskj3")
        val expected = "Wrong amount of arguments after filename and type of request for request type 3!"
        val actual = assertThrows(IncorrectInputDataForRequest::class.java) {
            isCorrectDataForThirdRequest(args)
        }
        assertEquals(expected, actual.message)
    }
}

