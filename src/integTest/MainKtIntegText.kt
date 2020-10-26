package integTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import program.*
import java.io.File

internal class MainKtIntegText {

    @Test
    fun `first request`() {
        for (i in 1..3) {
            main(arrayOf("data/$i.txt", "1"))
            val expected = File("data/resultsForIntegTest/firstRequest", "$i.txt").readLines()
            val actual = File("output.txt").readLines()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `second request with number`() {
        for (i in 1..3) {
            main(arrayOf("data/$i.txt", "2", "4"))
            val expected = File("data/resultsForIntegTest/secondRequestWithNumber", "$i.txt").readLines()
            val actual = File("output.txt").readLines()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `second request with word`() {
        for (i in 1..3) {
            main(arrayOf("data/$i.txt", "2", "пони"))
            val expected = File("data/resultsForIntegTest/secondRequestWithWord", "$i.txt").readLines()
            val actual = File("output.txt").readLines()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `second request with group`() {
        for (i in 1..3) {
            main(arrayOf("data/$i.txt", "2", "пони", "строка"))
            val expected = File("data/resultsForIntegTest/secondRequestWithGroup", "$i.txt").readLines()
            val actual = File("output.txt").readLines()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `third request`() {
        for (i in 1..3) {
            main(arrayOf("data/$i.txt", "3", "пони"))
            val expected = File("data/resultsForIntegTest/thirdRequest", "$i.txt").readLines()
            val actual = File("output.txt").readLines()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `few args`() {
        main(arrayOf("data"))
        val expected = File("data/resultsForIntegTest/fewArgs.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `incor filename`() {
        main(arrayOf("data", "f"))
        val expected = File("data/resultsForIntegTest/incorrectFilename.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `incor type`() {
        main(arrayOf("data/3.txt", "5"))
        val expected = File("data/resultsForIntegTest/incorrectType.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }

    @Test
    fun `incor data`() {
        main(arrayOf("data/3.txt", "3", "2", "hg"))
        val expected = File("data/resultsForIntegTest/incorrectData.txt").readLines()
        val actual = File("output.txt").readLines()
        assertEquals(expected, actual)
    }
}