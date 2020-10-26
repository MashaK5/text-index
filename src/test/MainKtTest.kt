package unitTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import program.*
import java.io.File

internal class NumberingTextFile {

    @Test
    fun `empty file`() {
        val textFileName = "data/HaveIndexFile.txt"
        val expected = listOf<String>()
        val actual = numberingTextFile(textFileName)
        assertEquals(expected, actual)
    }

    @Test
    fun `file with empty line`() {
        val textFileName = "data/FileWithEmptyLines.txt"
        val expected = listOf("(1,1) О, о", "(2,1) Моя оборона!")
        val actual = numberingTextFile(textFileName)
        assertEquals(expected, actual)
    }

    @Test
    fun `file without empty line`() {
        val textFileName = "data/MyText.txt"
        val expected = listOf("(1,1) Я надеялась поспать, но проект и дз победили.",
            "(2,1) Пожалуйста, не ставьте дедлайны на один день...",
            "(3,1) Хочется плакать.",
            "(4,1) И спать.")
        val actual = numberingTextFile(textFileName)
        assertEquals(expected, actual)
    }
}

internal class IsNotServiceWord {

    @Test
    fun `preposition`() {
        val type = "предл."
        val word = "хохо"
        val expected = false
        val actual = isNotServiceWord(type, word)
        assertEquals(expected, actual)
    }

    @Test
    fun `union`() {
        val type = "союз"
        val word = "хохо"
        val expected = false
        val actual = isNotServiceWord(type, word)
        assertEquals(expected, actual)
    }

    @Test
    fun `particle`() {
        val type = "част."
        val word = "хохо"
        val expected = false
        val actual = isNotServiceWord(type, word)
        assertEquals(expected, actual)
    }

    @Test
    fun `interjection`() {
        val type = "межд."
        val word = "хохо"
        val expected = false
        val actual = isNotServiceWord(type, word)
        assertEquals(expected, actual)
    }

    @Test
    fun `small c`() {
        val type = "с"
        val word = "а"
        val expected = false
        val actual = isNotServiceWord(type, word)
        assertEquals(expected, actual)
    }

    @Test
    fun `big c`() {
        val type = "с"
        val word = "хохо"
        val expected = true
        val actual = isNotServiceWord(type, word)
        assertEquals(expected, actual)
    }

    @Test
    fun `word`() {
        val type = "м"
        val word = "хохо"
        val expected = true
        val actual = isNotServiceWord(type, word)
        assertEquals(expected, actual)
    }
}
