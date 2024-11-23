package com.staricka.adventofcode2024.framework

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import kotlin.io.path.Path

class InputDownloader: InputProvider {
    override fun getInput(day: Int): String {
        getFromCache(day)
        return getFromCache(day) ?: downloadInput(day).also { cacheInput(day, it) }
    }

    fun getFromCache(day: Int): String? =
        try {
            File("cache/day$day.txt").readText()
        } catch (_: Exception) {
            null
        }

    fun cacheInput(day: Int, input: String) {
        try {
            Files.createDirectory(Path("cache"))
        } catch (_: Exception) {}
        try {
            File("cache/day$day.txt").writeText(input)
        } catch (_: Exception) {}
    }

    fun downloadInput(day: Int): String {
        val response = HttpClient.newHttpClient().send(HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("https://adventofcode.com/2024/day/$day/input"))
            .header("UserAgent", "https://github.com/mathstar/adventOfCode2024 by mstaricka@gmail.com")
            .header("Cookie", "session=${System.getenv("SESSION")}")
            .build(), HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}