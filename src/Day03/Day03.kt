package Day03

import readInput

data class MatrixIndex(
    val i: Int,
    val j: Int
)

object Day03Solver {
    fun solvePart1(schematic: List<String>): Int {
        val charMatrix = parseSchematicInput(schematic)
        val partNumbers = findValidSchematicValues(charMatrix)
        return partNumbers.sumOf { it }
    }

    private fun findValidSchematicValues(charMatrix: Array<CharArray>): List<Int> {
        val validValues = mutableListOf<Int>()
        val adjacentIndices = mutableListOf<MatrixIndex>()
        var potentialNumber = ""
        charMatrix.forEachIndexed { i, row ->
            row.forEachIndexed { j, value ->
                if (value.isDigit()) {
                    potentialNumber += value
                    adjacentIndices.addAll(getAdjacentIndices(i, j))
                } else {
                    if (isSymbolAdjacent(adjacentIndices, charMatrix)) {
                        validValues.add(potentialNumber.toInt())
                    }
                    potentialNumber = ""
                    adjacentIndices.clear()
                }
            }
        }
        return validValues
    }

    private fun getAdjacentIndices(i: Int, j: Int): List<MatrixIndex> {
        return listOf(
            MatrixIndex(i=i-1, j=j),
            MatrixIndex(i=i, j=j-1),
            MatrixIndex(i=i+1, j=j),
            MatrixIndex(i=i, j=j+1),
            MatrixIndex(i=i-1, j=j-1),
            MatrixIndex(i=i+1, j=j-1),
            MatrixIndex(i=i+1, j=j+1),
            MatrixIndex(i=i-1, j=j+1)
        ).filter { it.i >= 0 && it.j >= 0 }
    }

    private fun isSymbolAdjacent(adjacentIndices: List<MatrixIndex>, charMatrix: Array<CharArray>): Boolean {
        return adjacentIndices.any {
            val char = charMatrix.getOrNull(it.i)?.getOrNull(it.j)
            char != null && char != '.'&& !char.isDigit()
        }
    }

    private fun parseSchematicInput(schematic: List<String>): Array<CharArray> {
        return schematic.map { row -> row.toCharArray() }.toTypedArray()
    }
}

fun main() {

    val day3ExampleInput = readInput("Day03_Example")
    check(Day03Solver.solvePart1(day3ExampleInput) == 4361)

    val myPersonalInput = readInput("Day03")

    val part1Answer = Day03Solver.solvePart1(myPersonalInput)
    println("Part 1: $part1Answer")

}