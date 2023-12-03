package Day03

import readInput

data class MatrixIndex(
    val i: Int,
    val j: Int
) {
    override fun equals(other: Any?): Boolean {
        return other is MatrixIndex && this.i == other.i && this.j == other.j
    }

    override fun hashCode() = 31 * i + j

}

class Gear(
    val index: MatrixIndex,
    private var adjacentPartNumbers: MutableList<PartNumber> = mutableListOf()
) {

    fun getAdjacentPartNumbers() = adjacentPartNumbers

    fun addAdjacentPartNumber(number: PartNumber) {
        adjacentPartNumbers.add(number)
    }

    fun getGearRatio(): Int {
        return adjacentPartNumbers.map{ it.getValue() }.reduce { total, number ->  total * number }
    }
}

class PartNumber(
    private val value: Int,
    private val adjacentIndices: MutableList<MatrixIndex> = mutableListOf()
) {

    fun getValue() = value

    fun getAdjacentIndices() = adjacentIndices

    fun addAdjacentIndices(newIndices: MutableList<MatrixIndex>) {
        // remove duplicates
        newIndices.removeIf { index -> adjacentIndices.contains(index) }
        adjacentIndices.addAll(newIndices)
    }
}

object Day03Solver {

    fun solvePart1(schematic: List<String>): Int {
        val charMatrix = parseSchematicInput(schematic)
        val partNumbers = findValidSchematicValues(charMatrix)
        return partNumbers.sumOf { it.getValue() }
    }

    fun solvePart2(schematic: List<String>): Int {
        val charMatrix = parseSchematicInput(schematic)
        val partNumbers = findValidSchematicValues(charMatrix)
        val potentialGears = parseGears(charMatrix)
        potentialGears.forEach { gear ->
            partNumbers.forEach { partNumber ->
                if (partNumber.getAdjacentIndices().contains(gear.index)) {
                    gear.addAdjacentPartNumber(partNumber)
                }
            }
        }
        val validGears = potentialGears.filter { it.getAdjacentPartNumbers().size == 2 }
        return validGears.sumOf { it.getGearRatio() }
    }

    private fun findValidSchematicValues(charMatrix: Array<CharArray>): List<PartNumber> {
        val validPartNumbers = mutableListOf<PartNumber>()
        val adjacentIndices = mutableListOf<MatrixIndex>()
        var potentialNumber = ""
        charMatrix.forEachIndexed { i, row ->
            row.forEachIndexed { j, value ->
                if (value.isDigit()) {
                    potentialNumber += value
                    adjacentIndices.addAll(getAdjacentIndices(i, j))
                } else {
                    if (isSymbolAdjacent(adjacentIndices, charMatrix)) {
                        val partNumber = PartNumber(potentialNumber.toInt())
                        partNumber.addAdjacentIndices(adjacentIndices)
                        validPartNumbers.add(partNumber)
                    }
                    potentialNumber = ""
                    adjacentIndices.clear()
                }
            }
        }
        return validPartNumbers
    }

    private fun parseGears(charMatrix: Array<CharArray>): List<Gear> {
        val gears: MutableList<Gear> = mutableListOf()
        charMatrix.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (char.isGear()) {
                    gears.add(Gear(MatrixIndex(i, j)))
                }
            }
        }
        return gears
    }

    private fun Char.isGear() = this == '*'

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
    check(Day03Solver.solvePart2(day3ExampleInput) == 467835)

    val myPersonalInput = readInput("Day03")

    val part1Answer = Day03Solver.solvePart1(myPersonalInput)
    println("Part 1: $part1Answer")

    val part2Answer = Day03Solver.solvePart2(myPersonalInput)
    println("Part 2: $part2Answer")

}