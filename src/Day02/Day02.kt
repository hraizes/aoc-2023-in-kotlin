package Day02

import println
import readInput

data class GameConfig(
    val maxRed: Int,
    val maxBlue: Int,
    val maxGreen: Int
)

class Round(
    private val redCount: Int,
    private val blueCount: Int,
    private val greenCount: Int
) {
    fun isRoundValid(gameConfig: GameConfig): Boolean {
        return (redCount <= gameConfig.maxRed && blueCount <= gameConfig.maxBlue && greenCount <= gameConfig.maxGreen)
    }
}

class Game(
    private val id: Int,
    private val gameConfig: GameConfig,
    private val rounds: List<Round>
) {

    fun isValidGame(): Boolean {
        return rounds.all { round -> round.isRoundValid(gameConfig) }
    }

    fun getGameId() = id
}

object Day02Solver {

    enum class Color(val countGroupName: String, val inputName: String) {
        RED("redCount", "red"),
        BLUE("blueCount", "blue"),
        GREEN("greenCount", "green")
    }

    private val gameIdRegex = Regex("""(Game (?<gameId>\d*))""")
    fun solvePart1(gameConfig: GameConfig, inputGames: List<String>): Int {
        val games: List<Game> = parseGames(gameConfig, inputGames)
        return games.filter { game -> game.isValidGame() }.sumOf { it.getGameId() }
    }

    private fun parseGames(gameConfig: GameConfig, inputGames: List<String>): List<Game> {
        return inputGames.map { gameData ->
            val gameId = gameIdRegex.find(gameData)!!.groups["gameId"]!!.value.toInt()
            val rounds: List<String> = gameData.split(":").last().split(";")
            Game(
                id=gameId,
                gameConfig=gameConfig,
                rounds=parseRounds(rounds)
            )
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

    private fun parseColorCount(color: Color, value: String): Int {
        return getRegex(color).find(value)?.groups?.get(color.countGroupName)?.value?.toInt() ?: 0
    }

    private fun getRegex(color: Color): Regex {
        return Regex("""((?<${color.countGroupName}>(\d*)) ${color.inputName})""")
    }

}

fun main() {

    val gameConfig = GameConfig(maxRed = 12, maxBlue = 14, maxGreen = 13)

    val day2ExampleInput = readInput("Day02_P1_Example")
    check(Day02Solver.solvePart1(gameConfig, day2ExampleInput) == 8)

    val myPersonalInput = readInput("Day02")
    Day02Solver.solvePart1(gameConfig, myPersonalInput).println()

}