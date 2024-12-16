package com.staricka.adventofcode2024.util

fun String.splitByBlankLines(): List<String> {
    val result = ArrayList<String>()
    val sb = StringBuilder()
    for (line in this.lines()) {
        if (line.isNotBlank()) {
            sb.appendLine(line)
        } else {
            result.add(sb.toString())
            sb.clear()
        }
    }
    if (sb.isNotBlank()) result.add(sb.toString())
    return result
}

enum class Direction(val vector: Pair<Int, Int>) {
    UP(-1 to 0), LEFT(0 to -1), DOWN(1 to 0), RIGHT(0 to 1);

    fun clockwise() = when(this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    fun counterClockwise() = when(this) {
        UP -> LEFT
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP
    }
}