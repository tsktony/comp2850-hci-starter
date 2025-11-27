package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter

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
// import renderTemplate            // Extension function from Main.kt
// import isHtmxRequest             // Extension function from Main.kt

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
 * Week 6/7 Labs: Task routes with HTMX progressive enhancement and inline edit.
 *
 * - Week 6: Basic add/delete with Int IDs
 * - Week 7: Add inline edit (HTMX + no-JS)
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

    /**
     * Helper: Check if request is from HTMX
     */
    fun ApplicationCall.isHtmx(): Boolean =
        request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true

    /**
     * GET /tasks - List all tasks
     * Returns full page (HTMX and no-JS both use this).
     */
    get("/tasks") {
        val model =
            mapOf(
                "title" to "Tasks",
                "tasks" to TaskRepository.all(),
            )
        val template = pebble.getTemplate("tasks/index.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)
        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    /**
     * POST /tasks - Add new task
     * Dual-mode: HTMX fragment or PRG redirect
     */
    post("/tasks") {
        val title = call.receiveParameters()["title"].orEmpty().trim()

        if (title.isBlank()) {
            // Validation error handling
            if (call.isHtmx()) {
                val error =
                    """<div id="status" hx-swap-oob="true" role="alert" aria-live="assertive">
                        Title is required. Please enter at least one character.
                       </div>""".trimIndent()
                return@post call.respondText(error, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                // No-JS: redirect back (could add error query param later)
                call.response.headers.append("Location", "/tasks")
                return@post call.respond(HttpStatusCode.SeeOther)
            }
        }

        val task = TaskRepository.add(title)

        if (call.isHtmx()) {
            // HTMX: render the new task using the same partial as the list
            val template = pebble.getTemplate("tasks/_item.peb")
            val model = mapOf("task" to task)
            val writer = StringWriter()
            template.evaluate(writer, model)
            val fragment = writer.toString()

            val status =
                """<div id="status" hx-swap-oob="true">
                    Task "${task.title}" added successfully.
                   </div>""".trimIndent()

            return@post call.respondText(fragment + status, ContentType.Text.Html, HttpStatusCode.Created)
        }

        // No-JS: POST-Redirect-GET pattern (303 See Other)
        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    /**
     * POST /tasks/{id}/delete - Delete task
     * Dual-mode: HTMX status + removal or PRG redirect
     */
    post("/tasks/{id}/delete") {
        val id = call.parameters["id"]?.toIntOrNull()
        val removed = id?.let { TaskRepository.delete(it) } ?: false

        if (call.isHtmx()) {
            val message = if (removed) "Task deleted." else "Could not delete task."
            val status = """<div id="status" hx-swap-oob="true">$message</div>"""
            // Returning only the status is enough; HTMX already swaps the <li> via outerHTML
            return@post call.respondText(status, ContentType.Text.Html)
        }

        // No-JS: POST-Redirect-GET pattern (303 See Other)
        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    // --- Week 7: inline edit routes ---

    /**
     * GET /tasks/{id}/edit
     * HTMX: return only the <li> edit fragment
     * no-JS: return full page with one task in edit mode
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
            // HTMX path: send only the edit <li>
            val template = pebble.getTemplate("tasks/_edit.peb")
            val model = mapOf(
                "task" to task,
                "error" to errorMessage,
            )
            val writer = StringWriter()
            template.evaluate(writer, model)
            return@get call.respondText(writer.toString(), ContentType.Text.Html)
        } else {
            // no-JS: render full page, mark which task is in edit mode
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
     * HTMX: validate and return view/edit fragment
     * no-JS: validate and redirect (PRG)
     */
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val existing = TaskRepository.find(id)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val newTitle = call.receiveParameters()["title"].orEmpty().trim()

        // Validation: title must not be blank
        if (newTitle.isBlank()) {
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
                // no-JS: redirect back with error flag
                call.response.headers.append("Location", "/tasks/$id/edit?error=blank")
                return@post call.respond(HttpStatusCode.SeeOther)
            }
        }

        // Update and persist
        val updated = existing.copy(title = newTitle)
        TaskRepository.update(updated)

        if (call.isHtmx()) {
            // HTMX: send view fragment + status message (OOB)
            val viewTemplate = pebble.getTemplate("tasks/_item.peb")
            val viewWriter = StringWriter()
            viewTemplate.evaluate(viewWriter, mapOf("task" to updated))

            val status =
                """<div id="status" hx-swap-oob="true">
                    Task "${updated.title}" updated successfully.
                   </div>""".trimIndent()

            return@post call.respondText(viewWriter.toString() + status, ContentType.Text.Html)
        } else {
            // no-JS: redirect back to list (PRG)
            call.response.headers.append("Location", "/tasks")
            return@post call.respond(HttpStatusCode.SeeOther)
        }
    }

    /**
     * GET /tasks/{id}/view
     * Used by the "Cancel" link in HTMX mode to go back to view state.
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
