package data

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

/**
 * NOTE FOR NON-INTELLIJ IDEs (VSCode, Eclipse, etc.):
 * IntelliJ IDEA automatically adds imports as you type. If using a different IDE,
 * you may need to manually add imports. The commented imports below show what you'll need
 * for future weeks. Uncomment them as needed when following the lab instructions.
 *
 * When using IntelliJ: You can ignore the commented imports below - your IDE will handle them.
 */

// Week 7+ imports (no new imports needed for find/update methods)

// Week 8+ imports (search functionality added, no new imports needed)

// Week 10+ evolution note:
// In the solution repo, this file is split into two separate files:
//
// 1. model/Task.kt (data class with validation):
//    import java.time.LocalDateTime
//    import java.time.format.DateTimeFormatter
//    import java.util.UUID
//
// 2. storage/TaskStore.kt (CSV persistence using Apache Commons CSV):
//    import model.Task
//    import org.apache.commons.csv.CSVFormat
//    import org.apache.commons.csv.CSVParser
//    import org.apache.commons.csv.CSVPrinter
//    import java.io.FileReader
//    import java.io.FileWriter
//    import java.time.format.DateTimeParseException

/**
 * Simple task data model for Week 6/7.
 */
data class Task(
    val id: Int,
    var title: String,
)

/**
 * Generic page wrapper for pagination (Week 8).
 */
data class Page<T>(
    val items: List<T>,
    val currentPage: Int,
    val pageSize: Int,
    val totalItems: Int,
) {
    // Derived properties used by templates
    val totalPages: Int =
        if (totalItems == 0) 1 else ((totalItems + pageSize - 1) / pageSize)

    val hasPrevious: Boolean = currentPage > 1
    val hasNext: Boolean = currentPage < totalPages
    val previousPage: Int = if (hasPrevious) currentPage - 1 else 1
    val nextPage: Int = if (hasNext) currentPage + 1 else totalPages
}

/**
 * In-memory repository with CSV persistence.
 *
 * Simple singleton for the labs.
 */
object TaskRepository {
    private val file = File("data/tasks.csv")
    private val tasks = mutableListOf<Task>()
    private val idCounter = AtomicInteger(1)

    init {
        file.parentFile?.mkdirs()
        if (!file.exists()) {
            file.writeText("id,title\n")
        } else {
            file.readLines().drop(1).forEach { line ->
                val parts = line.split(",", limit = 2)
                if (parts.size == 2) {
                    val id = parts[0].toIntOrNull() ?: return@forEach
                    tasks.add(Task(id, parts[1]))
                    idCounter.set(maxOf(idCounter.get(), id + 1))
                }
            }
        }
    }

    fun all(): List<Task> = tasks.toList()

    fun add(title: String): Task {
        val task = Task(idCounter.getAndIncrement(), title)
        tasks.add(task)
        persist()
        return task
    }

    fun delete(id: Int): Boolean {
        val removed = tasks.removeIf { it.id == id }
        if (removed) persist()
        return removed
    }

    // Week 7 Lab 1: helper methods for inline edit
    // Find a single task by id (or null if not found)
    fun find(id: Int): Task? =
        tasks.find { it.id == id }

    // Update an existing task and keep the CSV in sync
    fun update(task: Task) {
        tasks.find { it.id == task.id }?.let { existing ->
            existing.title = task.title
        }
        persist()
    }

    /**
     * Week 8: search + pagination helper.
     *
     * @param query Free text to filter by title (case-insensitive).
     * @param page  1-based page index requested by the caller.
     * @param size  Page size (will be coerced to at least 1).
     */
    fun search(
        query: String,
        page: Int,
        size: Int,
    ): Page<Task> {
        val normalizedQuery = query.trim().lowercase()
        val pageSize = size.coerceAtLeast(1)

        // Work on a copy to avoid exposing internal mutable list
        val source = tasks.toList()

        val filtered =
            if (normalizedQuery.isEmpty()) {
                source
            } else {
                source.filter { task ->
                    task.title.lowercase().contains(normalizedQuery)
                }
            }

        val totalItems = filtered.size
        val totalPages =
            if (totalItems == 0) 1 else ((totalItems + pageSize - 1) / pageSize)

        // Clamp requested page into valid range
        val currentPage = page.coerceIn(1, totalPages)

        val fromIndex = (currentPage - 1) * pageSize
        val toIndex = minOf(fromIndex + pageSize, totalItems)

        val pageItems =
            if (totalItems == 0) {
                emptyList()
            } else {
                filtered.subList(fromIndex, toIndex)
            }

        return Page(
            items = pageItems,
            currentPage = currentPage,
            pageSize = pageSize,
            totalItems = totalItems,
        )
    }

    private fun persist() {
        file.writeText(
            "id,title\n" +
                tasks.joinToString("\n") { "${it.id},${it.title}" },
        )
    }
}
