package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import java.math.BigInteger
import kotlin.math.pow
import kotlin.math.sqrt

fun divideByPowerOf2(numerator: Long, power: Long): Long {
    var result = numerator
    for (i in 1..power) {
        result /= 2
    }
    return result
}

class Day17: Day {
    enum class OpCode {
        ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV
    }

    data class State (
        val pc: Int,
        val regA: Long,
        val regB: Long,
        val regC: Long,
        val out: List<Long>
    ) {
        fun nextState(program: IntArray): State {
            var regA = regA
            var regB = regB
            var regC = regC
            when (OpCode.entries[program[pc]]) {
                OpCode.ADV -> regA = divideByPowerOf2(regA, comboOperand(program))
                OpCode.BXL -> regB = regB xor program[pc+1].toLong()
                OpCode.BST -> regB = comboOperand(program) % 8
                OpCode.JNZ -> if (regA != 0L) return State(program[pc+1], regA, regB, regC, out)
                OpCode.BXC -> regB = regB xor regC
                OpCode.OUT -> return State(pc + 2, regA, regB, regC, out + comboOperand(program) % 8)
                OpCode.BDV -> regB = divideByPowerOf2(regA, comboOperand(program))
                OpCode.CDV -> regC = divideByPowerOf2(regA, comboOperand(program))
            }
            return State(pc+2, regA, regB, regC, out)
        }

        fun comboOperand(program: IntArray) = when (program[pc+1]) {
            0 -> 0
            1 -> 1
            2 -> 2
            3 -> 3
            4 -> regA
            5 -> regB
            6 -> regC
            else -> throw Exception("Invalid operand")
        }
    }

    fun parseInput(input: String) =
        State(
            0,
            input.lines()[0].split(Regex(": "))[1].toLong(),
            input.lines()[1].split(Regex(": "))[1].toLong(),
            input.lines()[2].split(Regex(": "))[1].toLong(),
            emptyList()
        ) to input.lines()[4].split(Regex(": "))[1].split(",").map { it.toInt() }.toIntArray()

    fun runProgram(state: State, program: IntArray) = generateSequence(state) { it.nextState(program) }
        .dropWhile { it.pc < program.size }
        .first()
        .out
        .joinToString(",")

    override fun part1(input: String): Any? {
        val (state, program) = parseInput(input)
        return runProgram(state, program)
    }

    override fun part2(input: String): Any? {
        val (state, program) = parseInput(input)
        var i = 1L
        val stringProgram = program.joinToString(",")
        val programOnes = consecutiveOnes(stringProgram)
        while (true) {
            val output = runProgram(State(state.pc, i, state.regB, state.regC, state.out), program)
            if (output == stringProgram) return i
            if (output.length < stringProgram.length) {
                i *= 2
            } else if (consecutiveOnes(output) - 3 >= programOnes) {
                i += sqrt(i.toDouble()).toLong()
            } else {
                i++
            }
        }
    }

    fun consecutiveOnes(n: String): Int {
        return Regex("[1,?]+").findAll(n).map { it.value.length }.maxOrNull() ?: 0
    }
}