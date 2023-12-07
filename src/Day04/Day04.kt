package Day04

import readInput
import kotlin.math.pow


// TODO Have not finished part 2

class ScratchCard(
    val cardId: Int,
    private val winningNumbers: Set<Int>,
    private val cardNumbers: Set<Int>,
    private val winners: Set<Int> = winningNumbers.intersect(cardNumbers)
) {
    fun calculateTotalPoints(): Int {
        val winningNumberCount = getWinners().size
        if(winningNumberCount < 2) { return winningNumberCount }
        return "2".toDouble().pow(winningNumberCount-1).toInt()
    }

    private fun getWinners() = winners

    fun getCopies(maxCopyId: Int): MutableList<Int> {
        val endRange = cardId + winners.size
        if(endRange > maxCopyId) {
            return mutableListOf()
        }
        return (cardId + 1 ..endRange).toMutableList()
    }

}



// Card 1 -> 4 winning numbers -> copies = 2, 3, 4, 5
// Copies 2, 3, 4, 5
    // Copy 2 -> 2 winning numbers -> copies 3, 4

object Day04Solver {

    fun solvePart1(input: List<String>): Int {
        return input.map { parseCard(it) }.sumOf { it.calculateTotalPoints() }
    }

    fun solvePart2(input: List<String>): Int {
        val scratchCards = input.map { parseCard(it) }
        val maxCardId = scratchCards.last().cardId
        val copiesById = scratchCards.associate {it.cardId to it.getCopies(maxCardId)}
        println(copiesById)
        scratchCards.forEach { originalCard ->
            val cardCopies = originalCard.getCopies(maxCardId)
            cardCopies.forEach {
                copiesById[it]?.addAll(copiesById[it] ?: emptyList())
            }
        }
        return copiesById.values.sumOf { it.size } + scratchCards.size
    }


    private fun parseCard(value: String): ScratchCard {
        val allNumbers = value.split(":").last().split("|")
        return ScratchCard(
            cardId = getCardId(value),
            winningNumbers = parseNumberSet(allNumbers.first()),
            cardNumbers = parseNumberSet(allNumbers.last())
        )
    }

    private fun parseNumberSet(numbers: String): Set<Int> {
        return numbers.split(" ").filter { it != "" }.map { it.toInt() }.toSet()
    }

    private fun getCardId(value: String): Int {
        return Regex("""(Card *(?<cardId>\d*)):""").find(value)!!.groups["cardId"]!!.value.toInt()
    }

}

fun main() {

    val day4ExampleInput = readInput("Day04_Example")
    check(Day04Solver.solvePart1(day4ExampleInput) == 13)
    //check(Day04Solver.solvePart2(day4ExampleInput) == 30)

    val myPersonalInput = readInput("Day04")

    val part1Answer = Day04Solver.solvePart1(myPersonalInput)
    println("Part 1: $part1Answer")

    val part2Answer = Day04Solver.solvePart2(myPersonalInput)
    println("Part 2: $part2Answer")

    // your answer is too low - 57854, 29908

}