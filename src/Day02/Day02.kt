package Day02

import readInput

enum class Color(val countGroupName: String, val inputName: String) {
    RED("redCount", "red"),
    BLUE("blueCount", "blue"),
    GREEN("greenCount", "green")
}

class GameConfig(
    val maxRed: Int,
    val maxBlue: Int,
    val maxGreen: Int
) {
    fun calculatePower() = maxRed * maxBlue * maxGreen
}

class Round(
    val redCount: Int,
    val blueCount: Int,
    val greenCount: Int
) {
    fun isRoundValid(gameConfig: GameConfig): Boolean {
        return (redCount <= gameConfig.maxRed && blueCount <= gameConfig.maxBlue && greenCount <= gameConfig.maxGreen)
    }
}

class Game(
    private val id: Int,
    private val rounds: List<Round>
) {

    fun isValidGame(gameConfig: GameConfig): Boolean {
        return rounds.all { round -> round.isRoundValid(gameConfig) }
    }

    fun getGameId() = id

    fun getMinimumRequiredGameConfig(): GameConfig {
        return GameConfig(
            maxBlue = rounds.maxOf { it.blueCount },
            maxRed = rounds.maxOf { it.redCount },
            maxGreen = rounds.maxOf { it.greenCount }
        )
    }

}

object Day02Solver {

    fun solvePart1(inputGames: List<String>): Int {
        val gameConfig = GameConfig(maxRed = 12, maxBlue = 14, maxGreen = 13)
        val games: List<Game> = parseGames(inputGames)
        val validGames = games.filter { game -> game.isValidGame(gameConfig) }
        return validGames.sumOf { it.getGameId() }
    }

    fun solvePart2(inputGames: List<String>): Int {
        val games: List<Game> = parseGames(inputGames)
        val minimumRequiredGameConfigs = games.map { it.getMinimumRequiredGameConfig() }
        return minimumRequiredGameConfigs.sumOf { it.calculatePower() }
    }

    private fun parseGames(inputGames: List<String>): List<Game> {
        return inputGames.map { game ->
            val gameId = parseGameId(game)
            val rounds: List<String> = game.split(":").last().split(";")
            Game(id=gameId, rounds=parseRounds(rounds))
        }
    }

    private fun parseRounds(rounds: List<String>): List<Round> {
        return rounds.map { round ->
            Round(
                redCount = parseColorCount(Color.RED, round),
                blueCount = parseColorCount(Color.BLUE, round),
                greenCount = parseColorCount(Color.GREEN, round),
            )
        }
    }

    private fun parseGameId(value: String): Int {
        return Regex("""(Game (?<gameId>\d*))""").find(value)!!.groups["gameId"]!!.value.toInt()
    }

    private fun parseColorCount(color: Color, value: String): Int {
        return getRegex(color).find(value)?.groups?.get(color.countGroupName)?.value?.toInt() ?: 0
    }

    private fun getRegex(color: Color): Regex {
        return Regex("""((?<${color.countGroupName}>(\d*)) ${color.inputName})""")
    }

}

fun main() {

    val day2ExampleInput = readInput("Day02_P1_Example")
    check(Day02Solver.solvePart1(day2ExampleInput) == 8)
    check(Day02Solver.solvePart2(day2ExampleInput) == 2286)

    val myPersonalInput = readInput("Day02")

    val part1Answer = Day02Solver.solvePart1(myPersonalInput)
    println("Part 1: $part1Answer")

    val part2Answer = Day02Solver.solvePart2(myPersonalInput)
    println("Part 2: $part2Answer")

}