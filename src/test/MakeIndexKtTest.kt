package unitTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import program.*
import java.io.File


internal class CreateIndexFile {

    @Test
    fun `index file`() {
        val textFileName = "data/MyText.txt"
        val expected = File("indices/MyText.txt")
        val actual = createIndexFile(textFileName)
        assertEquals(expected, actual)
    }
}

internal class HaveIndexFile {

    @Test
    fun `have`() {
        val textFileName = File("indices/HaveIndexFile.txt")
        val expected = true
        val actual = haveIndexFile(textFileName)
        assertEquals(expected, actual)
    }

    @Test
    fun `not have`() {
        val textFileName = File("indices/NotHaveIndexFile.txt")
        val expected = false
        val actual = haveIndexFile(textFileName)
        assertEquals(expected, actual)
    }
}

internal class CreateIndex {

    @Test
    fun `clouds`() {
        val expected = index
        val actual = createIndex(numberedText, vocabulary)
        assertEquals(expected, actual)
    }

}

internal class OnlyWord {

    @Test
    fun `word without punctuation marks`() {
        val word = "Конёк-Горбунок"
        val expected = "Конёк-Горбунок"
        val actual = onlyWord(word)
        assertEquals(expected, actual)
    }

    @Test
    fun `word with punctuation marks after`() {
        val word = "Конёк-Горбунок,"
        val expected = "Конёк-Горбунок"
        val actual = onlyWord(word)
        assertEquals(expected, actual)
    }

    @Test
    fun `word with punctuation marks before`() {
        val word = "(((Конёк-Горбунок"
        val expected = "Конёк-Горбунок"
        val actual = onlyWord(word)
        assertEquals(expected, actual)
    }

    @Test
    fun `word with punctuation marks after and before`() {
        val word = "(Конёк-Горбунок!.."
        val expected = "Конёк-Горбунок"
        val actual = onlyWord(word)
        assertEquals(expected, actual)
    }
}

internal class AddWordToIndex {

    @Test
    fun `new word`() {
        val indexWithoutRecord = hashMapOf<Word, InformationAboutWord>()
        indexWithoutRecord.putAll(index)
        indexWithoutRecord.remove("яблоко")
        val expected = index
        val actual = addWordToIndex(indexWithoutRecord, "яблока", vocabulary, 2, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `not word`() {
        val expected = index
        val actual = addWordToIndex(index, "яблокааааа", vocabulary, 3, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `old word`() {
        val indexWithoutRecord = hashMapOf<Word, InformationAboutWord>()
        indexWithoutRecord.putAll(index)
        indexWithoutRecord.remove("облако")
        indexWithoutRecord["облако"] = InformationAboutWord(
            1,
            mutableSetOf("облака"),
            mutableListOf(1),
            mutableListOf(1)
        )
        val expected = index
        val actual = addWordToIndex(indexWithoutRecord, "облаке", vocabulary, 3, 1)
        println(expected)
        println(actual)
        assertEquals(expected, actual)
    }
}

internal class MainForm {

    @Test
    fun `wordLow`() {
        val expected = Pair("облаке", "облако")
        val actual = mainForm("облаке", vocabulary)
        assertEquals(expected, actual)
    }

    @Test
    fun `wordUp`() {
        val expected = Pair("облака", "облако")
        val actual = mainForm("Облака", vocabulary)
        assertEquals(expected, actual)
    }

    @Test
    fun `not word`() {
        val expected = Pair("Пппп", "")
        val actual = mainForm("Пппп", vocabulary)
        assertEquals(expected, actual)
    }
}

internal class UpdateInfoAboutWord {

    @Test
    fun `small example`() {
        val indexWithoutRecord = hashMapOf<Word, InformationAboutWord>()
        indexWithoutRecord.putAll(index)
        indexWithoutRecord.remove("облако")
        indexWithoutRecord["облако"] = InformationAboutWord(
            1,
            mutableSetOf("облака"),
            mutableListOf(1),
            mutableListOf(1)
        )
        val expected = index
        val actual = updateInfoAboutWord(indexWithoutRecord, "облаке", "облако", 1, 3)
        assertEquals(expected, actual)
    }
}
