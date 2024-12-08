package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day

class Day7: Day {
    data class Equation(val result: Long, val operands: List<Long>)

    override fun part1(input: String): Long {
        val equations = parseInput(input)
        return sumMatches(equations, listOf({ a, b -> a + b}, { a, b -> a * b}))
    }

    override fun part2(input: String): Long {
        val equations = parseInput(input)
        return sumMatches(equations, listOf({ a, b -> a + b}, { a, b -> a * b}, { a, b -> (a.toString() + b.toString()).toLong() }))
    }

    private fun parseInput(input: String) = input.lines().filter { it.isNotEmpty() }
        .map { it.split(":") }
        .map { split -> Equation(split[0].toLong(), split[1].trim().split(" ").map { it.toLong() }) }

    private fun sumMatches(equations: List<Equation>, operators: List<(Long, Long) -> Long>) =
        equations.filter { eq ->
            var acc = setOf(eq.operands[0])
            for (operand in eq.operands.drop(1)) {
                acc = acc.flatMap { a -> operators.map { operator -> operator(a, operand) }.filter { it <= eq.result } }.toSet()
            }
            acc.contains(eq.result)
        }.sumOf { it.result }
}