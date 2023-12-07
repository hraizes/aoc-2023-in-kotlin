package Day05

import Day04.Day04Solver
import readInput

object Day05Solver {
    fun solvePart1(input: List<String>): Int {
        return 0
    }

    fun solvePart2(input: List<String>): Int {
        return 0
    }

}

fun main() {

    val day5ExampleInput = readInput("Day05_Example")
    check(Day05Solver.solvePart1(day5ExampleInput) == 13)
    check(Day05Solver.solvePart2(day5ExampleInput) == 30)

    val myPersonalInput = readInput("Day05")

    val part1Answer = Day04Solver.solvePart1(myPersonalInput)
    println("Part 1: $part1Answer")
//
//    val part2Answer = Day04Solver.solvePart2(myPersonalInput)
//    println("Part 2: $part2Answer")

}