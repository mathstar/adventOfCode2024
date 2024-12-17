package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.Direction
import com.staricka.adventofcode2024.util.Grid
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid
import java.util.PriorityQueue

class Day16: Day {
    enum class Cell(override val symbol: Char): GridCell {
        WALL('#'), START('S'), END('E')
    }

    private fun traversePathIterative(grid: Grid<Cell>): Int {
        val (sx, sy) = grid.cells().first { (_,_,c) -> c == Cell.START }
        val (ex, ey) = grid.cells().first { (_,_,c) -> c == Cell.END }

        val visited = HashSet<Triple<Int, Int, Direction>>()
        val priorityQueue = PriorityQueue<Pair<Triple<Int, Int, Direction>, Int>>(Comparator.comparingInt { it.second })
        priorityQueue.add(Triple(sx, sy, Direction.RIGHT) to 0)

        while (priorityQueue.isNotEmpty()) {
            val (t, costSoFar) = priorityQueue.poll()
            val (x, y, heading) = t

            if (x == ex && y == ey) return costSoFar
            if (!visited.add(t)) continue

            val (vx, vy) = heading.vector
            if (grid[x + vx, y + vy] != Cell.WALL) {
                priorityQueue.add(Triple(x + vx, y + vy, heading) to costSoFar + 1)
            }
            val clockwiseHeading = heading.clockwise()
            val (cwx, cwy) = clockwiseHeading.vector
            if (grid[x + cwx, y + cwy] != Cell.WALL) {
                priorityQueue.add(Triple(x + cwx, y + cwy, clockwiseHeading) to costSoFar + 1001)
            }
            val counterclockwiseHeading = heading.counterClockwise()
            val (ccwx, ccwy) = counterclockwiseHeading.vector
            if (grid[x + ccwx, y + ccwy] != Cell.WALL) {
                priorityQueue.add(Triple(x + ccwx, y + ccwy, counterclockwiseHeading) to costSoFar + 1001)
            }
        }
        return -1
    }

    private fun traversePathIterativeForViewing(grid: Grid<Cell>): Set<Pair<Int, Int>> {
        val (sx, sy) = grid.cells().first { (_,_,c) -> c == Cell.START }
        val (ex, ey) = grid.cells().first { (_,_,c) -> c == Cell.END }

        val visited = HashMap<Triple<Int, Int, Direction>, Int>()
        val priorityQueue = PriorityQueue<Pair<Triple<Int, Int, Direction>, Pair<Int, List<Pair<Int, Int>>>>>(Comparator.comparingInt { it.second.first })
        val viewingSpots = HashSet<Pair<Int, Int>>()
        viewingSpots.add(ex to ey)
        priorityQueue.add(Triple(sx, sy, Direction.RIGHT) to Pair(0, emptyList()))

        // track first (lowest) cost at end - could also just check visited for all directions, but this seems cleaner
        var endCost: Int? = null
        while (priorityQueue.isNotEmpty()) {
            val (t, costSoFar) = priorityQueue.poll()
            val (x, y, heading) = t

            if ((visited[t] ?: Int.MAX_VALUE) < costSoFar.first) continue
            visited[t] = costSoFar.first

            if (x == ex && y == ey) {
                if (endCost == null) endCost = costSoFar.first
                if (costSoFar.first != endCost) continue
                viewingSpots.addAll(costSoFar.second)
                continue
            }

            val path = costSoFar.second + (x to y)
            val (vx, vy) = heading.vector
            if (grid[x + vx, y + vy] != Cell.WALL) {
                priorityQueue.add(Triple(x + vx, y + vy, heading) to (costSoFar.first + 1 to path))
            }
            val clockwiseHeading = heading.clockwise()
            val (cwx, cwy) = clockwiseHeading.vector
            if (grid[x + cwx, y + cwy] != Cell.WALL) {
                priorityQueue.add(Triple(x + cwx, y + cwy, clockwiseHeading) to (costSoFar.first + 1001 to path))
            }
            val counterclockwiseHeading = heading.counterClockwise()
            val (ccwx, ccwy) = counterclockwiseHeading.vector
            if (grid[x + ccwx, y + ccwy] != Cell.WALL) {
                priorityQueue.add(Triple(x + ccwx, y + ccwy, counterclockwiseHeading) to (costSoFar.first + 1001 to path))
            }
        }
        return viewingSpots
    }

    override fun part1(input: String): Int {
        val grid = StandardGrid.build(input.trim()){when (it) {
            '#' -> Cell.WALL
            'S' -> Cell.START
            'E' -> Cell.END
            else -> null
        } }
        return traversePathIterative(grid)
    }

    override fun part2(input: String): Int {
        val grid = StandardGrid.build(input.trim()){when (it) {
            '#' -> Cell.WALL
            'S' -> Cell.START
            'E' -> Cell.END
            else -> null
        } }
        return traversePathIterativeForViewing(grid).size
    }
}