package utils

import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter

data class LogEntry(
    val tsIso: String,
    val sessionId: String,
    val requestId: String,
    val taskCode: String,
    val step: String,
    val outcome: String,
    val durationMs: Long,
    val httpStatus: Int,
    val jsMode: String,
)

object Logger {

    private val file: File
    private var counter: Int = 0

    init {
        val dir = File("data")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        file = File(dir, "metrics.csv")

        if (!file.exists() || file.length() == 0L) {
            file.writeText(
                "ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode\n"
            )
        }
    }

    @Synchronized
    private fun nextRequestId(): String {
        counter += 1
        return "r%04d".format(counter)
    }

    @Synchronized
    fun log(
        sessionId: String,
        taskCode: String,
        step: String,
        outcome: String,
        durationMs: Long,
        httpStatus: Int,
        jsMode: String,
    ) {
        val ts = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val reqId = nextRequestId()

        val entry =
            LogEntry(
                tsIso = ts,
                sessionId = sessionId,
                requestId = reqId,
                taskCode = taskCode,
                step = step,
                outcome = outcome,
                durationMs = durationMs,
                httpStatus = httpStatus,
                jsMode = jsMode,
            )

        file.appendText(
            "${entry.tsIso}," +
                "${entry.sessionId}," +
                "${entry.requestId}," +
                "${entry.taskCode}," +
                "${entry.step}," +
                "${entry.outcome}," +
                "${entry.durationMs}," +
                "${entry.httpStatus}," +
                "${entry.jsMode}\n"
        )
    }
}
