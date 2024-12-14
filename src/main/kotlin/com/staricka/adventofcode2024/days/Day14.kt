package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import java.io.BufferedWriter
import java.util.regex.Pattern

class Day14(val xSize: Int = 101, val ySize: Int = 103): Day {
    inner class Bot(
        var x: Int,
        var y: Int,
        private var dx: Int,
        private var dy: Int
    ) {
        fun step(count: Int = 1) {
            x = (x + dx * count) % xSize
            while (x < 0) x += xSize
            y = (y + dy * count) % ySize
            while (y < 0) y += ySize
        }

        fun quadrant() = if (x == xSize / 2 || y == ySize / 2) {
            Quadrant.NONE
        } else if (x > xSize / 2) {
            if (y > ySize / 2) Quadrant.D else Quadrant.B
        } else {
            if (y > ySize / 2) Quadrant.C else Quadrant.A
        }

        override fun toString() = "Bot($x,$y,$dx,$dy)"
    }

    enum class Quadrant {A,B,C,D,NONE}

    private val pattern: Pattern = Pattern.compile("""p=(-?[0-9]+),(-?[0-9]+) v=(-?[0-9]+),(-?[0-9]+)""")
    private fun parseBot(input: String): Bot {
        val matcher = pattern.matcher(input)
        matcher.find()
        return Bot(matcher.group(1).toInt(), matcher.group(2).toInt(), matcher.group(3).toInt(), matcher.group(4).toInt())
    }

    /**
     * Renders bot positions to stdout or provided writer.
     */
    @Suppress("unused")
    private fun renderBots(bots: List<Bot>, writer: BufferedWriter? = null) {
        val field = Array(ySize){IntArray(xSize)}
        for (bot in bots) {
            field[bot.y][bot.x] += 1
        }
        for (y in field.indices) {
            for (x in field[y].indices) {
                if (writer == null) {
                    print(if (field[y][x] == 0) '.' else field[y][x])
                } else {
                    writer.write(if (field[y][x] == 0) "." else field[y][x].toString())
                }
            }
            if (writer == null) println() else writer.write("\n")
        }
        if (writer == null) println() else writer.write("\n")
    }

    /**
     * Heuristic for finding the easter egg. Look for iteration with least entropy in x axis.
     */
    private fun xNoise(bots: List<Bot>): Int {
        val rows = Array(ySize) {ArrayList<Bot>()}
        for (bot in bots) {
            rows[bot.y].add(bot)
        }
        return rows.sumOf { if (it.size == 0) 0 else it.maxOf { bot -> bot.x } - it.minOf { bot -> bot.x } }
    }

    override fun part1(input: String): Int {
        val bots = input.lines().filter { it.isNotBlank() }.map { parseBot(it) }
        bots.forEach { it.step(100)}
        return bots.groupingBy { it.quadrant() }
            .eachCount()
            .filter { (k,_) ->k != Quadrant.NONE }
            .values
            .reduce { a, b -> a * b }
    }

    override fun part2(input: String): Int {
        val bots = input.lines().filter { it.isNotBlank() }.map { parseBot(it) }
        var min = Int.MAX_VALUE
        var minStep = 0
        for (i in 1..10000) {
            bots.forEach { it.step() }
            val xNoise = xNoise(bots)
            if (xNoise < min) {
                min = xNoise
                minStep = i
            }
        }
        return minStep
    }
}