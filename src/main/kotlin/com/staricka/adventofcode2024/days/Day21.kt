package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import java.util.PriorityQueue

class Day21: Day {

    enum class NumPad(val x: Int, val y: Int) {
        SEVEN(0, 0),
        EIGHT(0, 1),
        NINE(0, 2),
        FOUR(1, 0),
        FIVE(1, 1),
        SIX(1, 2),
        ONE(2, 0),
        TWO(2, 1),
        THREE(2, 2),
        ZERO(3, 1),
        A(3, 2);

        fun navigate(destination: NumPad): List<DirectionPad> {
            val dx = destination.x
            val dy = destination.y

            val result = ArrayList<DirectionPad>()
            if (dy > y) {
                for (i in y until dy) {
                    result.add(DirectionPad.RIGHT)
                }
                if (dx > x) {
                    for (i in x until dx) {
                        result.add(DirectionPad.DOWN)
                    }
                } else {
                    for (i in dx until x) {
                        result.add(DirectionPad.UP)
                    }
                }
            } else {
                if (dx > x) {
                    for (i in x until dx) {
                        result.add(DirectionPad.DOWN)
                    }
                } else {
                    for (i in dx until x) {
                        result.add(DirectionPad.UP)
                    }
                }
                for (i in dy until y) {
                    result.add(DirectionPad.LEFT)
                }
            }
            return result
        }
    }

    enum class DirectionPad(val x: Int, val y: Int) {
        UP(0, 1),
        A(0, 2),
        LEFT(1, 0),
        DOWN(1, 1),
        RIGHT(1, 2);

        fun navigate(direction: DirectionPad): List<DirectionPad> {
            val dx = direction.x
            val dy = direction.y
            val result = ArrayList<DirectionPad>()

            if (dy > y) {
                for (i in y until dy) {
                    result.add(DirectionPad.RIGHT)
                }
                if (dx > x) {
                    for (i in x until dx) {
                        result.add(DirectionPad.DOWN)
                    }
                } else {
                    for (i in dx until x) {
                        result.add(DirectionPad.UP)
                    }
                }
            } else {
                if (dx > x) {
                    for (i in x until dx) {
                        result.add(DirectionPad.DOWN)
                    }
                } else {
                    for (i in dx until x) {
                        result.add(DirectionPad.UP)
                    }
                }
                for (i in dy until y) {
                    result.add(DirectionPad.LEFT)
                }
            }
            return result
        }
    }

    fun parseInput(input: String): List<Pair<Int, List<NumPad>>> {
        return input.lines()
            .filter { it.isNotBlank() }
            .map { line ->
                line.split("A")[0].toInt() to
                line.filter { !it.isWhitespace() }
                    .map {
                        when (it) {
                            '0' -> NumPad.ZERO
                            '1' -> NumPad.ONE
                            '2' -> NumPad.TWO
                            '3' -> NumPad.THREE
                            '4' -> NumPad.FOUR
                            '5' -> NumPad.FIVE
                            '6' -> NumPad.SIX
                            '7' -> NumPad.SEVEN
                            '8' -> NumPad.EIGHT
                            '9' -> NumPad.NINE
                            'A' -> NumPad.A
                            else -> throw Exception("Invalid input")
                        }
                    }
            }
    }

    fun instructNumPadBot(numPads: List<NumPad>): List<DirectionPad> {
        var numPadPosition = NumPad.A
        val directions = ArrayList<DirectionPad>()
        for (numPad in numPads) {
            directions += numPadPosition.navigate(numPad)
            directions += DirectionPad.A
            numPadPosition = numPad
        }
        return directions
    }

    fun prettyPrintDirections(directions: List<DirectionPad>): String {
        return directions.map { when (it) {
            DirectionPad.A -> 'A'
            DirectionPad.DOWN -> 'v'
            DirectionPad.UP -> '^'
            DirectionPad.LEFT -> '<'
            DirectionPad.RIGHT -> '>'
        } }.joinToString("")
    }

    fun instructDirectionPadBot(directionPads: List<DirectionPad>): List<DirectionPad> {
        var directionPadPosition = DirectionPad.A
        val directions = ArrayList<DirectionPad>()
        for (directionPad in directionPads) {
            directions += directionPadPosition.navigate(directionPad)
            directions += DirectionPad.A
            directionPadPosition = directionPad
        }
        return directions
    }

    /*
    data class State(val numPad: NumPad, val directionPad1: DirectionPad, val directionPad2: DirectionPad)
    fun pathfind(source: NumPad, destination: NumPad): List<DirectionPad> {
        val queue = PriorityQueue<Pair<State, List<DirectionPad>>>(Comparator.comparingInt { it.second.size })
        queue.add()
    }
     */

    override fun part1(input: String): Any? {
        val codes = parseInput(input)
        var result = 0
        for ((code, numPads) in codes) {
            val directionBot1Directions = instructNumPadBot(numPads)
            val directionBot2Directions = instructDirectionPadBot(directionBot1Directions)
            val humanDirections = instructDirectionPadBot(directionBot2Directions)
            result += code * humanDirections.size
        }
        return result
    }

    override fun part2(input: String): Any? {
        TODO("Not yet implemented")
    }
}