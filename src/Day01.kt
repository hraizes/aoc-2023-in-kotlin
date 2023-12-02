import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

val SPELLED_DIGIT_TO_INT_VALUE = mapOf(
        "zero" to "0",
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
)
const val SPELLED_DIGITS = "zero|one|two|three|four|five|six|seven|eight|nine"
const val LITERAL_OR_SPELLED_DIGITS = """.*?($SPELLED_DIGITS|\d)."""
val LITERAL_OR_SPELLED_REVERSED = """.*?(${SPELLED_DIGITS.reversed()}|\d)."""
object Day01 {

    private val literalOrSpelledDigitRegex = Regex(LITERAL_OR_SPELLED_DIGITS)
    private val literalOrSpelledDigitRegexReverse = Regex(LITERAL_OR_SPELLED_REVERSED)
    fun solvePart1Serial(input: List<String>): Int {
        return input.sumOf { findTwoDigitNumberPart1(it) }
    }

    // TODO Come back to this, its slower than serial
    fun solvePart1Coroutines(input: List<String>): Int {
        return runBlocking {
            input.parallelMap { findTwoDigitNumberPart1(it) }.sum()
        }
    }

    private fun findTwoDigitNumberPart1(value: String): Int {
        val firstDigit = value.first { c -> c.isDigit() }
        val lastDigit = value.last { c -> c.isDigit() }
        return "$firstDigit$lastDigit".toInt()
    }

    fun solvePart2(input: List<String>): Int {
        return input.sumOf { findTwoDigitNumberPart2(it) }
    }

    private fun findTwoDigitNumberPart2(value: String): Int {
        val firstDigit = getFirstDigitPart2(value)
        val lastDigit = getSecondDigitPart2(value)
        return "$firstDigit$lastDigit".toInt()
    }

    private fun getFirstDigitPart2(value: String): String {
        val firstDigit = literalOrSpelledDigitRegex.find(value)?.groupValues?.get(1)?: "0"
        return firstDigit.toLiteralDigitPart2()
    }

    private fun getSecondDigitPart2(value: String): String {
        val lastDigit = literalOrSpelledDigitRegexReverse.find(value.reversed())?.groupValues?.get(1)?.reversed() ?: "0"
        return lastDigit.toLiteralDigitPart2()
    }

    private fun String.toLiteralDigitPart2(): String {
        return SPELLED_DIGIT_TO_INT_VALUE.getOrDefault(this, this)
    }

}

fun main() {

    // test if implementation meets criteria from the description, like:
    val day1ExampleInput = readInput("Day01_P1_Example")
    check(Day01.solvePart1Coroutines(day1ExampleInput) == 142)
    check(Day01.solvePart1Serial(day1ExampleInput) == 142)

    val myPersonalInput = readInput("Day01")

    val part1SerialMillis = measureTimeMillis {
        Day01.solvePart1Serial(myPersonalInput).println()
    }
    val part1ParallelMillis = measureTimeMillis {
        Day01.solvePart1Coroutines(myPersonalInput).println()
    }

    println("Serial method took $part1SerialMillis millis")
    println("Coroutines method took $part1ParallelMillis millis")

    val day2ExampleInput = readInput("Day01_P2_Example")
    check(Day01.solvePart2(day2ExampleInput) == 281)
    // TODO : Not correct, 54610 is too low
    Day01.solvePart2(myPersonalInput).println()

}
