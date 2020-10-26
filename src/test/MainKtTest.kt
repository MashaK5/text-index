package unitTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import program.*
import java.io.File

val vocabulary = hashMapOf<Word, Word>(
    "облака" to "облако", "облаке" to "облако", "облаку" to "облако",
    "мимо" to "мимо", "белого" to "белый", "яблока" to "яблоко",
    "неведомой" to "неведомый", "страны" to "страна", "лететь" to "летать",
    "белые" to "белый", "яблоки" to "яблоко", "летать" to "летать",
)

val numberedText = listOf(
    "(1,1) Белого облака!",
    "(2,1) белого яблока",
    "(3,1) лететь, Летать на облаке",
)

val index = hashMapOf(
        "белый" to InformationAboutWord(
            2,
            mutableSetOf("белого"),
            mutableListOf(1),
            mutableListOf(1, 2)
        ),
        "облако" to InformationAboutWord(
            2,
            mutableSetOf("облака", "облаке"),
            mutableListOf(1),
            mutableListOf(1, 3)
        ),
        "яблоко" to InformationAboutWord(
            1,
            mutableSetOf("яблока"),
            mutableListOf(1),
            mutableListOf(2)
        ),
        "летать" to InformationAboutWord(
            2,
            mutableSetOf("лететь", "летать"),
            mutableListOf(1),
            mutableListOf(3)
        )
)

val indexWithWordsWithDiffOccurs = hashMapOf(
        "белый" to InformationAboutWord(
                3,
                mutableSetOf("белого"),
                mutableListOf(1),
                mutableListOf(1, 2)
        ),
        "облако" to InformationAboutWord(
                2,
                mutableSetOf("облака", "облаке"),
                mutableListOf(1),
                mutableListOf(1, 3)
        ),
        "яблоко" to InformationAboutWord(
                5,
                mutableSetOf("яблока"),
                mutableListOf(1),
                mutableListOf(2)
        ),
        "летать" to InformationAboutWord(
                1,
                mutableSetOf("лететь", "летать"),
                mutableListOf(1),
                mutableListOf(3)
        )
)


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
