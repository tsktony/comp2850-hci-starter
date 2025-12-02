# Key Code Changes — Task T2 (Edit) & T3 (Add)

This document explains the key Week 10 code changes I made to the task manager, focusing on:

- Enabling **automatic timing and logging** for add/edit flows so I can analyse metrics later, and
- Improving validation feedback while keeping a **server-first design** with a working no-JS path.

---

## 1. POST /tasks — Add Task (T3_add)

**File:**  
`src/main/kotlin/routes/TaskRoutes.kt` — the `post("/tasks")` route.

### What changed

1. **Added timing and logging**

   At the start of the handler I now:

   - Record the start time with `start = System.currentTimeMillis()`.
   - Read a session identifier from a cookie (`sid`), falling back to `"anon"` if it isn’t set:
     - `val sessionId = call.request.cookies["sid"] ?: "anon"`.
   - Derive `jsMode` from whether the request is HTMX:
     - `val jsMode = if (call.isHtmx()) "on" else "off"`.

   Before each return, I call `Logger.log(...)` to write one row into `metrics.csv`. For this route:

   - `taskCode = "T3_add"` (add task flow),
   - `step` is `"success"` on a successful add, or `"validation_error"` when the title is blank,
   - `outcome` is `""` on success, or `"blank_title"` for the validation error,
   - `durationMs` is the difference between the current time and `start`,
   - `httpStatus` is the status code used for that response (e.g. 201, 303, 400),
   - `jsMode` is `"on"` or `"off"` as detected above.

   This means every “Add task” attempt is now logged automatically, instead of relying on manual stopwatch notes. Later in Week 10 I can compute median time, MAD, completion rate and error rate directly from `metrics.csv`.

2. **Improved HTMX validation feedback**

   When the `title` is blank, the HTMX branch now returns a clearer error message into the status region:

   - The message text is:  
     **“Title is required. Please enter at least one character.”**
   - The `<div id="status">` used for the message is now marked with:
     - `role="alert"`
     - `aria-live="assertive"`
     - `hx-swap-oob="true"`

   This keeps the visual behaviour from earlier labs (OOB status update) but makes the error easier to detect for assistive technologies as well, which is important for future WCAG 4.1.3 (Status Messages) compliance.

3. **No-JS path surfaces errors on the full page**

   For the no-JS branch, if the title is blank:

   - The server responds with a **303 redirect** to  
     `/tasks?error=title&msg=blank`.

   In the `GET /tasks` handler, I now:

   - Read `error` and `msg` from the query string, and
   - Pass them into the template model as `"error"` and `"msg"`.

   The `tasks/index.peb` template can use these flags to render an error message on the full page in the no-JS path, so non-JS users also get meaningful validation feedback instead of a “silent” failure.

---

## 2. POST /tasks/{id}/edit — Edit Task (T2_edit)

**File:**  
`src/main/kotlin/routes/TaskRoutes.kt` — the `post("/tasks/{id}/edit")` route.

### What changed

1. **Added timing and logging**

   At the top of the edit handler, I added the same instrumentation pattern:

   - `start = System.currentTimeMillis()`,
   - `sessionId` from the `sid` cookie (default `"anon"`),
   - `jsMode` derived from `call.isHtmx()`.

   On a **validation error** (blank title):

   - I compute `duration = System.currentTimeMillis() - start`,
   - Log a row with:
     - `taskCode = "T2_edit"`,
     - `step = "validation_error"`,
     - `outcome = "blank_title"`,
     - `durationMs = duration`,
     - `httpStatus = 400`,
     - `jsMode` as above.

   On **successful update**:

   - After updating the task in `TaskRepository`, I compute `duration` again,
   - Log a row with:
     - `taskCode = "T2_edit"`,
     - `step = "success"`,
     - `outcome = ""` (empty string),
     - `durationMs = duration`,
     - `httpStatus = 200` (HTMX) or 303 (no-JS redirect).

   This gives me a consistent dataset for the T2_edit flow: I can see how long participants took, how often they hit validation errors, and whether they were using HTMX or no-JS.

2. **Clearer validation error message**

   When `newTitle` is blank:

   - Both HTMX and no-JS branches now use the same clear message text:  
     **“Title is required. Please enter at least one character.”**

   For the HTMX route:

   - I re-render the `_edit.peb` fragment, passing the `existing` task and an `error` string into the template model. This lets the inline edit UI show an in-place error near the field the user is editing.

   For the no-JS route:

   - I redirect to `/tasks/{id}/edit?error=blank`,
   - The corresponding `GET /tasks/{id}/edit` route reads this `error` query parameter and sets an `errorMessage` string,
   - That error message is then passed into `_edit.peb` or `tasks/index.peb` when rendering, so the full-page edit view also displays the same validation message.

3. **More consistent success feedback**

   On a successful edit via HTMX:

   - In addition to sending back the updated `<li>` from `_item.peb`, I now append a status block like:

     > `Task "…" updated successfully.`

   - This status HTML is wrapped in `<div id="status" hx-swap-oob="true">…</div>` so it updates the shared live-region at the top of the page, matching the pattern used for add/delete.

   This makes feedback for **add / edit / delete** more consistent: users always get a clear textual confirmation that the action succeeded.

---

## 3. GET /tasks — Full Page View Receives Error Flags

**File:**  
`src/main/kotlin/routes/TaskRoutes.kt` — the `get("/tasks")` route.

### What changed

Inside `GET /tasks` I now:

- Read two optional query parameters:
  - `error = call.request.queryParameters["error"]`
  - `msg   = call.request.queryParameters["msg"]`
- Pass them into the Pebble model:
  - `"error" to error`
  - `"msg"   to msg`

The `tasks/index.peb` template can then:

- Show an error message in the no-JS add path when `error="title"` and `msg="blank"`,
- Or interpret `msg` as a simple status flag for future messages (e.g. “task added”, “filter applied”), keeping full-page and HTMX feedback aligned.

---

## 4. Extra: Logging for Delete (T4_delete)

**File:**  
`src/main/kotlin/routes/TaskRoutes.kt` — the `post("/tasks/{id}/delete")` route.

### What changed

For completeness, I also instrumented the delete flow:

- At the start, I record `start`, `sessionId`, and `jsMode` in the same way.
- After attempting `TaskRepository.delete(id)`, I calculate:
  - `step = "success"` if the task was removed, otherwise `"fail"`,
  - `outcome = ""` on success, or `"not_found"` if the ID didn’t match an existing task.
- I then call `Logger.log(...)` with:
  - `taskCode = "T4_delete"`,
  - `step`, `outcome`, `durationMs`, `httpStatus`, `jsMode` as above.

This gives a complete picture of how long it takes participants to delete tasks and whether there were any failures (e.g. trying to delete a task that no longer exists).

---

## 5. Summary and Example Log Call

Overall, the Week 10 code changes around T2 and T3 focus on three goals:

1. **Instrumentation & automatic metrics**  
   - Both **Add Task (T3_add)** and **Edit Task (T2_edit)** now record time, outcome, and mode (JS on/off) via `Logger.log(...)`.
   - This turns real interactions into structured data in `metrics.csv`, enabling the Week 10 before/after analysis.

2. **Clearer validation feedback**  
   - Error messages for blank titles use a full sentence that explains the problem and what the user should do.
   - HTMX paths write feedback into a shared `#status` region (with ARIA attributes for add), while no-JS paths use redirects plus query parameters so the full page can show equivalent feedback.

3. **Server-first with no-JS parity**  
   - All core flows (add, edit, delete) still work with JavaScript disabled.
   - HTMX is only used as a progressive enhancement; the underlying routes remain REST-style and usable from full page loads.

**Typical log call example (edit success):**

```kotlin
Logger.log(
    sessionId = sessionId,
    taskCode = "T2_edit",
    step = "success",
    outcome = "",
    durationMs = duration,
    httpStatus = HttpStatusCode.OK.value,
    jsMode = jsMode,
)
```
This single line captures the key facts for one successful edit attempt: which flow (T2_edit), in which context (session + jsMode), with what result (success), and how long it took (durationMs). Together with the other calls, it forms the quantitative backbone of my Week 10 analysis and redesign evidence.