package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day

class Day4: Day {
    private val target = "XMAS"
    private fun searchGrid(grid: List<CharArray>, i: Int, j: Int, iDirection: Int, jDirection: Int, t: Int): Boolean {
        if (grid[i][j] == target[t]) {
            if (t == target.length - 1) return true
            val nextI = i + iDirection
            val nextJ = j + jDirection
            if (nextI >= 0 && nextI < grid.size && nextJ >= 0 && nextJ < grid[nextI].size) {
                return searchGrid(grid, nextI, nextJ, iDirection, jDirection, t + 1)
            }
        }
        return false
    }

    override fun part1(input: String): Int {
        val grid = input.lines().filter { it.isNotBlank() }.map { l -> l.toCharArray() }.toList()
        var count = 0
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == target[0]) {
                    if (searchGrid(grid, i, j, -1, -1, 0)) count++
                    if (searchGrid(grid, i, j, -1, 0, 0)) count++
                    if (searchGrid(grid, i, j, -1, 1, 0)) count++
                    if (searchGrid(grid, i, j, 0, -1, 0)) count++
                    if (searchGrid(grid, i, j, 0, 1, 0)) count++
                    if (searchGrid(grid, i, j, 1, -1, 0)) count++
                    if (searchGrid(grid, i, j, 1, 0, 0)) count++
                    if (searchGrid(grid, i, j, 1, 1, 0)) count++
                }
            }
        }
        return count
    }

    private fun checkX(grid: List<CharArray>, i: Int, j: Int): Boolean {
        if (grid[i][j] != 'A') return false
        if (i < 1 || j < 1 || i >= grid.size - 1 || j >= grid[0].size - 1) return false

        if (grid[i-1][j-1] == 'M') {
            if (grid[i+1][j+1] != 'S') return false
        } else if (grid[i-1][j-1] == 'S') {
            if (grid[i+1][j+1] != 'M') return false
        } else {
            return false
        }

        if (grid[i-1][j+1] == 'M') {
            if (grid[i+1][j-1] != 'S') return false
        } else if (grid[i-1][j+1] == 'S') {
            if (grid[i+1][j-1] != 'M') return false
        } else {
            return false
        }
        return true
    }

    override fun part2(input: String): Int {
        val grid = input.lines().filter { it.isNotBlank() }.map { l -> l.toCharArray() }.toList()
        var count = 0
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (checkX(grid, i, j)) count++
            }
        }
        return count
    }
}