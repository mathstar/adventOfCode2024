package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.Direction
import com.staricka.adventofcode2024.util.Grid
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid

class Day6: Day {
    enum class Cell(override val symbol: Char): GridCell {WALL('#'), EMPTY('.'), GUARD('^'), TRAVERSED('X')}

    private fun Pair<Int, Int>.step(direction: Direction): Pair<Int, Int> = when (direction) {
        Direction.UP -> first - 1 to second
        Direction.DOWN -> first + 1 to second
        Direction.RIGHT -> first to second + 1
        Direction.LEFT -> first to second - 1
    }

    private fun Direction.turn() = when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
    }

    private fun findGuard(grid: Grid<Cell>) = grid.cells().first { (_,_,c) -> c == Cell.GUARD }.run{ first to second }

    override fun part1(input: String): Int {
        val grid = StandardGrid.build(input){when (it) {
            '#' -> Cell.WALL
            '^' -> Cell.GUARD
            else -> Cell.EMPTY
        } }

        var guard = findGuard(grid)
        var heading = Direction.UP

        while (guard.first in (grid.minX..grid.maxX) && guard.second in (grid.minY..grid.maxY)) {
            var next = guard.step(heading)
            while (grid.get(next) == Cell.WALL) {
                heading = heading.turn()
                next = guard.step(heading)
            }
            grid[guard.first, guard.second] = Cell.TRAVERSED
            guard = next
        }
        return grid.cells().count { (_, _, c) -> c == Cell.TRAVERSED }
    }

    override fun part2(input: String): Int {
        val originalGrid = StandardGrid.build(input){when (it) {
            '#' -> Cell.WALL
            '^' -> Cell.GUARD
            else -> Cell.EMPTY
        } }
        val originalGuard = findGuard(originalGrid)

        var result = 0
        for (x in (originalGrid.minX..originalGrid.maxX)) {
            y@for (y in (originalGrid.minY..originalGrid.maxY)) {
                val grid = originalGrid.clone()
                if (grid[x, y] in listOf(Cell.WALL, Cell.GUARD)) {
                    continue
                }
                grid[x, y] = Cell.WALL

                var guard = originalGuard.first to originalGuard.second
                var heading = Direction.UP

                val visited = HashMap<Pair<Int, Int>, MutableSet<Direction>>()

                while (guard.first in (grid.minX..grid.maxX) && guard.second in (grid.minY..grid.maxY)) {
                    var next = guard.step(heading)
                    var rotations = 0
                    while (grid.get(next) == Cell.WALL) {
                        rotations++
                        if (rotations == 4) {
                            result++
                            continue@y
                        }
                        heading = heading.turn()
                        next = guard.step(heading)
                    }
                    grid[guard.first, guard.second] = Cell.TRAVERSED
                    guard = next
                    if (!visited.getOrPut(guard){HashSet()}.add(heading)) {
                        result++
                        continue@y
                    }
                }
            }
        }
        return result
    }
}