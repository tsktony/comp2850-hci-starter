package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter
import utils.Logger

/**
 * NOTE FOR NON-INTELLIJ IDEs (VSCode, Eclipse, etc.):
 * IntelliJ IDEA automatically adds imports as you type. If using a different IDE,
 * you may need to manually add imports. The commented imports below show what you'll need
 * for future weeks. Uncomment them as needed when following the lab instructions.
 *
 * When using IntelliJ: You can ignore the commented imports below - your IDE will handle them.
 */

// Week 7+ imports (inline edit, toggle completion):
// import model.Task               // When Task becomes separate model class
// import model.ValidationResult   // For validation errors
// import renderTemplate           // Extension function from Main.kt
// import isHtmxRequest            // Extension function from Main.kt

// Week 8+ imports (pagination, search, URL encoding):
// import io.ktor.http.encodeURLParameter  // For query parameter encoding
// import utils.Page                       // Pagination helper class

// Week 9+ imports (metrics logging, instrumentation):
// import utils.jsMode              // Detect JS mode (htmx/nojs)
// import utils.logValidationError  // Log validation failures
// import utils.timed               // Measure request timing

// Note: Solution repo uses storage.TaskStore instead of data.TaskRepository
// You may refactor to this in Week 10 for production readiness

/**
 * Task routes with HTMX progressive enhancement, inline edit,
 * and Week 8: search + basic pagination.
 */
fun Route.taskRoutes() {
    val pebble =
        PebbleEngine
            .Builder()
            .loader(
                io.pebbletemplates.pebble.loader.ClasspathLoader().apply {
                    prefix = "templates/"
                },
            ).build()

    fun ApplicationCall.isHtmx(): Boolean =
        request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true

    /**
     * GET /tasks - Full page render
     *
     * - query parameter ?q=  : simple title filter
     * - query parameter ?page=: 1-based page number (defaults to 1)
     * - uses TaskRepository.search() to get a Page<Task>
     */
    get("/tasks") {
        val query = call.request.queryParameters["q"].orEmpty()
        val pageParam = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val pageNumber = pageParam.coerceAtLeast(1)

        val error = call.request.queryParameters["error"]
        val msg = call.request.queryParameters["msg"]

        val pageSize = 10

        val resultPage =
            TaskRepository.search(
                query = query,
                page = pageNumber,
                size = pageSize,
            )

        val model =
            mapOf(
                "title" to "Tasks",
                "tasks" to resultPage.items,
                "query" to query,
                "page" to resultPage,
                "error" to error,
                "msg" to msg,
            )

        val template = pebble.getTemplate("tasks/index.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)
        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    /**
     * GET /tasks/fragment - HTMX fragment route
     */
    get("/tasks/fragment") {
        val query = call.request.queryParameters["q"].orEmpty()
        val pageParam = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val pageNumber = pageParam.coerceAtLeast(1)
        val pageSize = 10

        val resultPage =
            TaskRepository.search(
                query = query,
                page = pageNumber,
                size = pageSize,
            )

        val listTemplate = pebble.getTemplate("tasks/_list.peb")
        val listModel = mapOf(
            "tasks" to resultPage.items,
            "editingId" to null,
            "error" to null,
        )
        val listWriter = StringWriter()
        listTemplate.evaluate(listWriter, listModel)
        val listHtml = listWriter.toString()

        val pagerTemplate = pebble.getTemplate("tasks/_pager.peb")
        val pagerModel = mapOf(
            "page" to resultPage,
            "query" to query,
        )
        val pagerWriter = StringWriter()
        pagerTemplate.evaluate(pagerWriter, pagerModel)
        val pagerHtml = pagerWriter.toString()

        val statusHtml =
            """<div id="status" hx-swap-oob="true">
                Found ${resultPage.totalItems} tasks.
               </div>""".trimIndent()

        call.respondText(
            listHtml + pagerHtml + statusHtml,
            ContentType.Text.Html,
        )
    }

    /**
     * POST /tasks - Add new task
     * Dual-mode: HTMX fragment or PRG redirect
     */
    post("/tasks") {
        val start = System.currentTimeMillis()
        val sessionId = call.request.cookies["sid"] ?: "anon"
        val jsMode = if (call.isHtmx()) "on" else "off"

        val title = call.receiveParameters()["title"].orEmpty().trim()

        if (title.isBlank()) {
            val duration = System.currentTimeMillis() - start
            Logger.log(
                sessionId = sessionId,
                taskCode = "T3_add",
                step = "validation_error",
                outcome = "blank_title",
                durationMs = duration,
                httpStatus = HttpStatusCode.BadRequest.value,
                jsMode = jsMode,
            )

            if (call.isHtmx()) {
                val error =
                    """<div id="status" hx-swap-oob="true" role="alert" aria-live="assertive">
                        Title is required. Please enter at least one character.
                       </div>""".trimIndent()
                return@post call.respondText(
                    error,
                    ContentType.Text.Html,
                    HttpStatusCode.BadRequest,
                )
            } else {
                call.response.headers.append("Location", "/tasks?error=title&msg=blank")
                return@post call.respond(HttpStatusCode.SeeOther)
            }
        }

        val task = TaskRepository.add(title)

        if (call.isHtmx()) {
            val query = call.request.queryParameters["q"].orEmpty()
            val pageNumber = 1
            val pageSize = 10

            val resultPage =
                TaskRepository.search(
                    query = query,
                    page = pageNumber,
                    size = pageSize,
                )

            val listTemplate = pebble.getTemplate("tasks/_list.peb")
            val listModel = mapOf(
                "tasks" to resultPage.items,
                "editingId" to null,
                "error" to null,
            )
            val listWriter = StringWriter()
            listTemplate.evaluate(listWriter, listModel)
            val listHtml = listWriter.toString()

            val pagerTemplate = pebble.getTemplate("tasks/_pager.peb")
            val pagerModel = mapOf(
                "page" to resultPage,
                "query" to query,
            )
            val pagerWriter = StringWriter()
            pagerTemplate.evaluate(pagerWriter, pagerModel)
            val pagerHtml = pagerWriter.toString()

            val status =
                """<div id="status" hx-swap-oob="true">
                    Task "${task.title}" added successfully.
                   </div>""".trimIndent()

            val duration = System.currentTimeMillis() - start
            Logger.log(
                sessionId = sessionId,
                taskCode = "T3_add",
                step = "success",
                outcome = "",
                durationMs = duration,
                httpStatus = HttpStatusCode.Created.value,
                jsMode = jsMode,
            )

            return@post call.respondText(
                listHtml + pagerHtml + status,
                ContentType.Text.Html,
                HttpStatusCode.Created,
            )
        }

        val duration = System.currentTimeMillis() - start
        Logger.log(
            sessionId = sessionId,
            taskCode = "T3_add",
            step = "success",
            outcome = "",
            durationMs = duration,
            httpStatus = HttpStatusCode.SeeOther.value,
            jsMode = jsMode,
        )

        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    /**
     * POST /tasks/{id}/delete - Delete task
     * Dual-mode: HTMX status + removal or PRG redirect
     */
    post("/tasks/{id}/delete") {
        val start = System.currentTimeMillis()
        val sessionId = call.request.cookies["sid"] ?: "anon"
        val jsMode = if (call.isHtmx()) "on" else "off"

        val id = call.parameters["id"]?.toIntOrNull()
        val removed = id?.let { TaskRepository.delete(it) } ?: false

        val duration = System.currentTimeMillis() - start
        val step = if (removed) "success" else "fail"
        val outcome = if (removed) "" else "not_found"

        if (call.isHtmx()) {
            val message = if (removed) "Task deleted." else "Could not delete task."
            val status = """<div id="status" hx-swap-oob="true">$message</div>"""

            Logger.log(
                sessionId = sessionId,
                taskCode = "T4_delete",
                step = step,
                outcome = outcome,
                durationMs = duration,
                httpStatus = HttpStatusCode.OK.value,
                jsMode = jsMode,
            )

            return@post call.respondText(status, ContentType.Text.Html)
        }

        Logger.log(
            sessionId = sessionId,
            taskCode = "T4_delete",
            step = step,
            outcome = outcome,
            durationMs = duration,
            httpStatus = HttpStatusCode.SeeOther.value,
            jsMode = jsMode,
        )

        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    // --- Week 7: inline edit routes (unchanged, 这里也加上日志) ---

    /**
     * GET /tasks/{id}/edit
     */
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val task = TaskRepository.find(id)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val errorParam = call.request.queryParameters["error"]
        val errorMessage =
            when (errorParam) {
                "blank" -> "Title is required. Please enter at least one character."
                else -> null
            }

        if (call.isHtmx()) {
            val template = pebble.getTemplate("tasks/_edit.peb")
            val model = mapOf(
                "task" to task,
                "error" to errorMessage,
            )
            val writer = StringWriter()
            template.evaluate(writer, model)
            return@get call.respondText(writer.toString(), ContentType.Text.Html)
        } else {
            val model =
                mapOf(
                    "title" to "Tasks",
                    "tasks" to TaskRepository.all(),
                    "editingId" to id,
                    "error" to errorMessage,
                )
            val template = pebble.getTemplate("tasks/index.peb")
            val writer = StringWriter()
            template.evaluate(writer, model)
            return@get call.respondText(writer.toString(), ContentType.Text.Html)
        }
    }

    /**
     * POST /tasks/{id}/edit
     */
    post("/tasks/{id}/edit") {
        val start = System.currentTimeMillis()
        val sessionId = call.request.cookies["sid"] ?: "anon"
        val jsMode = if (call.isHtmx()) "on" else "off"

        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val existing = TaskRepository.find(id)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val newTitle = call.receiveParameters()["title"].orEmpty().trim()

        if (newTitle.isBlank()) {
            val duration = System.currentTimeMillis() - start
            Logger.log(
                sessionId = sessionId,
                taskCode = "T2_edit",
                step = "validation_error",
                outcome = "blank_title",
                durationMs = duration,
                httpStatus = HttpStatusCode.BadRequest.value,
                jsMode = jsMode,
            )

            if (call.isHtmx()) {
                val template = pebble.getTemplate("tasks/_edit.peb")
                val model = mapOf(
                    "task" to existing,
                    "error" to "Title is required. Please enter at least one character.",
                )
                val writer = StringWriter()
                template.evaluate(writer, model)
                return@post call.respondText(
                    writer.toString(),
                    ContentType.Text.Html,
                    HttpStatusCode.BadRequest,
                )
            } else {
                call.response.headers.append("Location", "/tasks/$id/edit?error=blank")
                return@post call.respond(HttpStatusCode.SeeOther)
            }
        }

        val updated = existing.copy(title = newTitle)
        TaskRepository.update(updated)

        val duration = System.currentTimeMillis() - start
        Logger.log(
            sessionId = sessionId,
            taskCode = "T2_edit",
            step = "success",
            outcome = "",
            durationMs = duration,
            httpStatus = HttpStatusCode.OK.value,
            jsMode = jsMode,
        )

        if (call.isHtmx()) {
            val viewTemplate = pebble.getTemplate("tasks/_item.peb")
            val viewWriter = StringWriter()
            viewTemplate.evaluate(viewWriter, mapOf("task" to updated))

            val status =
                """<div id="status" hx-swap-oob="true">
                    Task "${updated.title}" updated successfully.
                   </div>""".trimIndent()

            return@post call.respondText(viewWriter.toString() + status, ContentType.Text.Html)
        } else {
            call.response.headers.append("Location", "/tasks")
            return@post call.respond(HttpStatusCode.SeeOther)
        }
    }

    /**
     * GET /tasks/{id}/view
     */
    get("/tasks/{id}/view") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val task = TaskRepository.find(id)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val template = pebble.getTemplate("tasks/_item.peb")
        val model = mapOf("task" to task)
        val writer = StringWriter()
        template.evaluate(writer, model)
        return@get call.respondText(writer.toString(), ContentType.Text.Html)
    }
}
