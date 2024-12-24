package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.splitByBlankLines

class Day24: Day {
    enum class Op {
        AND, OR, XOR;

        fun apply(inputs: List<Boolean>): Boolean {
            return when (this) {
                AND -> inputs.all { it }
                OR -> inputs.any { it }
                XOR -> inputs.size == 2 && inputs[0] != inputs[1]
            }
        }
    }
    data class LogicGate(val inputs: List<String>, val op: Op, val output: String) {
        fun swapOutputs(other: LogicGate): Pair<LogicGate, LogicGate> {
            return LogicGate(inputs, op, other.output) to LogicGate(other.inputs, other.op, output)
        }
    }
    data class Input(val gates: Collection<String>, val initialValues: Map<String, Boolean>, val logicGates: List<LogicGate>)
    private fun parseInput(input: String): Input {
        val split = input.splitByBlankLines()
        val initialValues = split[0].lines()
            .filter { it.isNotBlank() }
            .map { it.trim().split(": ") }
            .associate { it[0] to (it[1] == "1") }
        val logicGates = split[1].lines()
            .filter { it.isNotBlank() }
            .map { it.trim().split(" ") }
            .map { LogicGate(listOf(it[0], it[2]), Op.valueOf(it[1]), it[4]) }
        return Input(
            initialValues.keys.toSet() + logicGates.flatMap { it.inputs + it.output },
            initialValues,
            logicGates
        )
    }

    private fun getZGates(input: Input): Map<String, Boolean> {
        val values = input.initialValues.toMutableMap()
        val zGates = input.gates.filter { it.startsWith('z') }
        while (zGates.any { values[it] == null }) {
            val gate = input.logicGates.first { values[it.output] == null && it.inputs.all { o -> values[o] != null } }
            values[gate.output] = gate.op.apply(gate.inputs.map { values[it]!! })
        }
        return values.filterKeys { it.startsWith('z') }
    }

    private fun getZValue(input: Input): Long {
        return getZGates(input)
            .toSortedMap()
            .values.map { if (it) "1" else "0" }
            .reversed()
            .joinToString("")
            .toLong(2)
    }

    override fun part1(input: String): Any? {
        return getZGates(parseInput(input))
            .toSortedMap()
            .values.map { if (it) "1" else "0" }
            .reversed()
            .joinToString("")
            .toLong(2)
    }

    private fun getValue(input: Input, prefix: Char): Long {
        return input.initialValues
            .filter { it.key.startsWith(prefix) }
            .toSortedMap()
            .values.map { if (it) "1" else "0" }
            .reversed()
            .joinToString("")
            .toLong(2)
    }

    private fun gatesToConsiderSwapping(input: Input, actual: Long, expected: Long): Map<String, List<LogicGate>> {
        val actualString = actual.toString(2).reversed()
        val expectedString = expected.toString(2).reversed()
        val result = mutableMapOf<String, MutableList<String>>()
        for (i in 0 until expectedString.length) {
            if (expectedString[i] != actualString[i]) {
                val zGate = String.format("z%02d", i)
                result[zGate] = mutableListOf(zGate)
            }
        }

        val inputs = input.logicGates.associate { it.output to it.inputs }
        for ((_, gates) in result) {
            var i = 0
            while (i < gates.size) {
                gates.addAll(inputs[gates[i]] ?: emptyList())
                i++
            }
        }
        return result.mapValues { (_, gates) -> gates.filter { !it.startsWith('x') && !it.startsWith('y') }
            .toSet().map {out -> input.logicGates.first { lg -> lg.output == out } }}
    }

    override fun part2(input: String): Any? {
        val initialInput = parseInput(input)
        val x = getValue(initialInput, 'x')
        val y = getValue(initialInput, 'y')
        var z = getZValue(initialInput)

        val gatesToSwap = gatesToConsiderSwapping(initialInput, z, x + y).values.toList().reversed()
        val swapIndices = gatesToSwap.map { 0 }.toMutableList()
        val possibleSwaps = mutableSetOf<Set<LogicGate>>()
        while (true) {
            val thisRoundGates = gatesToSwap.withIndex().map { (i, gates) -> gates[swapIndices[i]] }.toSet()
            if (thisRoundGates.size != 8) {
                var incIndex = 0
                var carry = true
                while (carry) {
                    carry = false
                    if (incIndex >= gatesToSwap.size) return ""
                    swapIndices[incIndex]++
                    if (swapIndices[incIndex] >= gatesToSwap[incIndex].size) {
                        swapIndices[incIndex] = 0
                        incIndex++
                        carry = true
                    }
                }
                continue
            }

            possibleSwaps.add(thisRoundGates)
        }

        return ""
    }
}