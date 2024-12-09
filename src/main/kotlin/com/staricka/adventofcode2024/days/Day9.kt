package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import java.util.TreeMap

class Day9: Day {
    /**
     * @param start Start index (inclusive) of the block
     * @param end End index (exclusive) of the block
     */
    sealed class Block(val start: Int, val end: Int)
    class Free(start: Int, end: Int): Block(start, end)
    class File(start: Int, end: Int, val fileId: Int): Block(start, end)

    private fun checksum(files: Collection<File>): Long = files.sumOf {
        it.fileId.toLong() * (it.start.toLong() until it.end.toLong()).sum()
    }

    override fun part1(input: String): Long {
        /*
        Original solution works for example but not real input. Ended up just switching to a "dumb" solution of
        representing the space as an array of Int? where each value represents a block and is either the fileId or null.
        End of the day, it is plenty fast that way, but ended up moving back to representing chunks of space for part 2.

        var i = 0
        var file = 0
        var nextIsFile = true
        val blocks = input.trim().map {
            val block: Block
            if (nextIsFile) {
                block = Used(i, i + (it - '0'), file)
                file ++
            } else {
                block = Free(i, i + (it - '0'))
            }
            i += it - '0'
            nextIsFile = !nextIsFile

            block
        }

        val frees = blocks.filter { it is Free }.associate { it.start to it as Free }.toMap(TreeMap())
        val files = blocks.filter { it is Used }.associate { it.start to it as Used }.toMap(TreeMap())

        while (frees.firstKey() < files.lastKey()) {
            val free = frees.firstEntry().value
            val file = files.lastEntry().value

            val freeLength = free.end - free.start
            val fileLength = file.end - file.start
            val newFile = Used(free.start, free.start + min(freeLength, fileLength), file.fileId)

            files.remove(file.start)
            frees.remove(free.start)
            if (freeLength > fileLength) {
                frees[free.start + fileLength] = Free(free.start + fileLength, free.end)
            } else {
                files[file.start] = Used(file.start, file.end - freeLength, file.fileId)
            }
            files[newFile.start] = newFile
        }

        return checksum(files.values)
        */
        return part1Dumb(input)
    }

    private fun part1Dumb(input: String): Long {
        var i = 0
        var nextFileId = 0
        var nextIsFile = true
        val blocks = input.trim().flatMap {
            val block: List<Int?>
            if (nextIsFile) {
                block = (i until i + (it - '0')).map {nextFileId}
                nextFileId++
            } else {
                block = (i until i + (it - '0')).map {null}
            }
            i += it - '0'
            nextIsFile = !nextIsFile

            block
        }.toMutableList()

        var nextEmpty = blocks.indexOfFirst { it == null }
        var lastFile = blocks.indexOfLast { it != null }
        while (nextEmpty < lastFile) {
            blocks[nextEmpty] = blocks[lastFile]
            blocks[lastFile] = null

            while (blocks[nextEmpty] != null) nextEmpty++
            while (blocks[lastFile] == null) lastFile--
        }

        return blocks.withIndex().sumOf { (i,v) -> if (v == null) 0L else i.toLong() * v.toLong() }
    }

    override fun part2(input: String): Long {
        var i = 0
        var nextFileId = 0
        var nextIsFile = true
        val blocks = input.trim().map {
            val block: Block
            if (nextIsFile) {
                block = File(i, i + (it - '0'), nextFileId)
                nextFileId++
            } else {
                block = Free(i, i + (it - '0'))
            }
            i += it - '0'
            nextIsFile = !nextIsFile

            block
        }

        val frees = blocks.filter { it is Free }.associate { it.start to it as Free }.toMap(TreeMap())
        val files = blocks.filter { it is File }.map{ it as File }.associate { it.fileId to it }.toMap(TreeMap())

        for (fileId in (0 until nextFileId).reversed()) {
            val file = files[fileId]!!
            val length = file.end - file.start
            val free = frees.values.firstOrNull { length <= (it.end - it.start) }
            if (free != null && free.start < file.start) {
                frees.remove(free.start)
                if (free.start + length < free.end) {
                    // allocate remaining free space
                    frees[free.start + length] = Free(free.start + length, free.end)
                }
                files[fileId] = File(free.start, free.start + length, fileId)
            }
        }
        return checksum(files.values)
    }
}