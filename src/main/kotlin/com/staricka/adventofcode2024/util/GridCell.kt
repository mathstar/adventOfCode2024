package com.staricka.adventofcode2024.util

interface GridCell {
    val symbol: Char
}

data class BasicCell(override val symbol: Char): GridCell