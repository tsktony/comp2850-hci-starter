package utils

import io.ktor.http.HttpStatusCode
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter

data class MetricsEvent(
    val sessionId: String,
    val requestId: String,
    val taskCode: String,
    val step: String,
    val outcome: String,
    val durationMs: Long,
    val statusCode: Int,
    val jsMode: String,
)

object Logger {

    private val file: File =
        File("data/metrics.csv").apply {
            parentFile?.mkdirs()
            if (!exists()) {
                writeText(
                    "ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode\n",
                )
            }
        }

    @Synchronized
    fun write(event: MetricsEvent) {
        val ts = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val line =
            buildString {
                append(ts)
                append(',')
                append(event.sessionId)
                append(',')
                append(event.requestId)
                append(',')
                append(event.taskCode)
                append(',')
                append(event.step)
                append(',')
                append(event.outcome)
                append(',')
                append(event.durationMs)
                append(',')
                append(event.statusCode)
                append(',')
                append(event.jsMode)
                append('\n')
            }
        file.appendText(line)
    }

    fun success(
        sessionId: String,
        requestId: String,
        taskCode: String,
        durationMs: Long,
        jsMode: String,
    ) {
        write(
            MetricsEvent(
                sessionId = sessionId,
                requestId = requestId,
                taskCode = taskCode,
                step = "success",
                outcome = "",
                durationMs = durationMs,
                statusCode = HttpStatusCode.OK.value,
                jsMode = jsMode,
            ),
        )
    }

    fun validationError(
        sessionId: String,
        requestId: String,
        taskCode: String,
        outcome: String,
        jsMode: String,
    ) {
        write(
            MetricsEvent(
                sessionId = sessionId,
                requestId = requestId,
                taskCode = taskCode,
                step = "validation_error",
                outcome = outcome,
                durationMs = 0L,
                statusCode = HttpStatusCode.BadRequest.value,
                jsMode = jsMode,
            ),
        )
    }
}
