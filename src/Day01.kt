import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

object Day01Solver {

    private val literalOrSpelledDigitRegex = Regex("""(?=(zero|one|two|three|four|five|six|seven|eight|nine|\d))""")
    private val spelledDigitToLiteralDigit = mapOf(
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
        val allDigits = literalOrSpelledDigitRegex.findAll(value).map { it.groupValues[1] }.toList()
        val firstDigit = allDigits.first().toLiteralDigitPart2()
        val lastDigit = allDigits.last().toLiteralDigitPart2()
        return "$firstDigit$lastDigit".toInt()
    }

    private fun String.toLiteralDigitPart2(): String {
        return spelledDigitToLiteralDigit.getOrDefault(this, this)
    }

}

fun main() {

    // test if implementation meets criteria from the description, like:
    val day1ExampleInput = readInput("Day01_P1_Example")
    check(Day01Solver.solvePart1Coroutines(day1ExampleInput) == 142)
    check(Day01Solver.solvePart1Serial(day1ExampleInput) == 142)

    val myPersonalInput = readInput("Day01")

    val part1SerialMillis = measureTimeMillis {
        Day01Solver.solvePart1Serial(myPersonalInput).println()
    }
    val part1ParallelMillis = measureTimeMillis {
        Day01Solver.solvePart1Coroutines(myPersonalInput).println()
    }

    println("Serial method took $part1SerialMillis millis")
    println("Coroutines method took $part1ParallelMillis millis")

    val day2ExampleInput = readInput("Day01_P2_Example")
    check(Day01Solver.solvePart2(day2ExampleInput) == 281)

    Day01Solver.solvePart2(myPersonalInput).println()

}
