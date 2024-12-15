package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.*

class Day15: Day {
    enum class Cell(override val symbol: Char): GridCell {
        WALL('#'),
        BOX('O'),
        ROBOT('@')
    }

    enum class ExtendedCell(override val symbol: Char): GridCell {
        WALL('#'),
        BOX_LEFT('['),
        BOX_RIGHT(']'),
        ROBOT('@')
    }

    fun parseInput(input: String): Pair<StandardGrid<Cell>, List<Direction>> {
        val parts = input.trim().splitByBlankLines()
        val grid = StandardGrid.build(parts[0]) {when(it) {
            '#' -> Cell.WALL
            'O' -> Cell.BOX
            '@' -> Cell.ROBOT
            else -> null
        }}
        val directions = parts[1].trim().map { when(it) {
            '<' -> Direction.LEFT
            '>' -> Direction.RIGHT
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            else -> null
        } }.filterNotNull()
        return grid to directions
    }

    override fun part1(input: String): Int {
        val (grid, directions) = parseInput(input)
        var (rx, ry) = grid.cells().first { (_, _, c) -> c == Cell.ROBOT }

        for (direction in directions) {
            val (vx, vy)  = when (direction) {
                Direction.LEFT -> (0 to -1)
                Direction.RIGHT -> (0 to 1)
                Direction.UP -> (-1 to 0)
                Direction.DOWN -> (1 to 0)
            }

            var boxesToMove = 0
            while (grid[rx + vx * (boxesToMove + 1),ry + vy * (boxesToMove + 1)] == Cell.BOX) {
                boxesToMove++
            }
            val canMove = grid[rx + vx * (boxesToMove + 1),ry + vy * (boxesToMove + 1)] != Cell.WALL

            if (canMove) {
                for (i in (2..(boxesToMove+1)).reversed()) {
                    grid[rx + vx * i, ry + vy * i] = Cell.BOX
                }
                grid[rx + vx, ry + vy] = Cell.ROBOT
                grid[rx,ry] = null
                rx += vx
                ry += vy
            }
        }

        return grid.cells().filter { (_,_,c) -> c == Cell.BOX }.sumOf { (x,y) -> x*100 + y }
    }

    private fun extendMap(grid: Grid<Cell>): Grid<ExtendedCell> {
        val extended = StandardGrid<ExtendedCell>()
        for ((x,y,c) in grid.cells()) {
            when (c) {
                Cell.WALL -> {
                    extended[x,y*2] = ExtendedCell.WALL
                    extended[x,y*2+1] = ExtendedCell.WALL
                }
                Cell.BOX -> {
                    extended[x,y*2] = ExtendedCell.BOX_LEFT
                    extended[x,y*2+1] = ExtendedCell.BOX_RIGHT
                }
                Cell.ROBOT -> {
                    extended[x,y*2] = ExtendedCell.ROBOT
                }
            }
        }
        return extended
    }

    override fun part2(input: String): Int {
        val (ogrid, directions) = parseInput(input)
        val grid = extendMap(ogrid)
        var (rx, ry) = grid.cells().first { (_, _, c) -> c == ExtendedCell.ROBOT }

        for (direction in directions) {
            val (vx, vy)  = when (direction) {
                Direction.LEFT -> (0 to -1)
                Direction.RIGHT -> (0 to 1)
                Direction.UP -> (-1 to 0)
                Direction.DOWN -> (1 to 0)
            }

            val objectsToMove = ArrayList<Triple<Int, Int, ExtendedCell>>()
            val otmSet = HashSet<Pair<Int, Int>>()
            objectsToMove.add(Triple(rx, ry, ExtendedCell.ROBOT))
            otmSet.add(Pair(rx, ry))
            var nextToEval = 0
            var canMove = true
            while (nextToEval < objectsToMove.size) {
                val (ex, ey, _) = objectsToMove[nextToEval++]
                when (grid[ex + vx, ey + vy]) {
                    ExtendedCell.WALL -> {
                        canMove = false
                        break
                    }
                    ExtendedCell.BOX_LEFT -> {
                        if (otmSet.add(Pair(ex + vx, ey + vy))) {
                            objectsToMove.add(Triple(ex + vx, ey + vy, ExtendedCell.BOX_LEFT))
                        }
                        if (otmSet.add(Pair(ex + vx, ey + vy + 1))) {
                            objectsToMove.add(Triple(ex + vx, ey + vy + 1, ExtendedCell.BOX_RIGHT))
                        }
                    }
                    ExtendedCell.BOX_RIGHT -> {
                        if (otmSet.add(Pair(ex + vx, ey + vy))) {
                            objectsToMove.add(Triple(ex + vx, ey + vy, ExtendedCell.BOX_RIGHT))
                        }
                        if (otmSet.add(Pair(ex + vx, ey + vy - 1))) {
                            objectsToMove.add(Triple(ex + vx, ey + vy - 1, ExtendedCell.BOX_LEFT))
                        }
                    }
                    else -> {}
                }
            }

            if (canMove) {
                for ((x,y,c) in objectsToMove) {
                    grid[x+vx, y+vy] = c
                    if (!otmSet.contains(x-vx to y-vy)) {
                        grid[x,y] = null
                    }
                }
                grid[rx, ry] = null
                rx += vx
                ry += vy
            }
        }

        return grid.cells().filter { (_,_,c) -> c == ExtendedCell.BOX_LEFT }.sumOf { (x,y) -> x*100 + y }
    }
}