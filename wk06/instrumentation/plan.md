# Instrumentation Plan — Week 6

**Module**: COMP2850 Human-Computer Interaction  
**Context**: Simple task manager (server-first + HTMX)  

In this module I am building a small task manager.  
In later weeks (especially Week 9 and 10) I want to log some basic, anonymous usage data so that I can check whether my design changes actually improve usability and accessibility.

---

## 1. Goals

I do **not** want a heavy analytics system. Instead, I want a few simple metrics that can help me answer questions like:

- How long does it roughly take to add or delete a task in HTMX mode (JavaScript on) vs. no-JS mode?
- How often do people trigger validation errors (for example, submitting an empty title)?
- After I change the UI (error messages, keyboard access, feedback), do these errors and confusions go down?

I will only log anonymous data. I will **not** log any personally identifiable information (PII).

---

## 2. Events to Log

Here are the events I plan to log in later weeks.

### 2.1 `task_created`

**Trigger**  
This event happens when a task is successfully created via `POST /tasks`.

**Fields**

- `ts_iso`: ISO 8601 timestamp (for example `2025-11-06T14:23:45Z`)
- `session_id`: Anonymous session ID (for example `P1_a3f7`)
- `request_id`: Unique ID for this HTTP request (for example `req_001`)
- `task_code`: Code for the pilot task, for example `T_add`
- `step`: For now I will just use `submit`
- `outcome`: `success`
- `ms`: Server-side processing time in milliseconds
- `http_status`: Expected `201` (HTMX) or `303` (no-JS PRG redirect)
- `js_mode`: `js-on` (HTMX) or `js-off` (no JavaScript)

**Why log this?**

- I want to compare time-on-task and success rate between HTMX and no-JS flows.
- I also want to check that progressive enhancement does **not** make the no-JS path slower or more fragile.

---

### 2.2 `validation_error`

**Trigger**  
This event happens when there is a validation error, for example the title is blank in `POST /tasks`.

**Fields**

The schema is the same as `task_created`, but with a few differences:

- `outcome`: `validation_error`
- `http_status`: Usually `400` (HTMX) or a redirect with an error flag in no-JS mode
- `step`: Still `submit`

**Why log this?**

- If there are many validation errors, it might mean that the form design or instructions are unclear.
- Later I want to see whether better error messages (and better ARIA attributes) reduce these errors.

---

### 2.3 `task_deleted`

**Trigger**  
This event happens when a task is deleted via `POST /tasks/{id}/delete`.

**Fields**

- `ts_iso`
- `session_id`
- `request_id`
- `task_code`: For example `T_delete`
- `step`: `submit`
- `outcome`: `success` or `not_found`
- `ms`
- `http_status`: For example `200` (HTMX) or `303` (no-JS)
- `js_mode`

**Why log this?**

- I want to compare how fast and reliable the delete action is in HTMX vs. no-JS.
- I also want to make sure delete stays fast and predictable even after I add more features later.

---

### 2.4 (Optional) `filter_applied` / `view_changed`

This event is optional. I will only use it if I later implement filters or different views (for example “only today’s tasks”).

**Trigger**  
The user applies a filter or changes the view.

**Fields (example)**

- `ts_iso`
- `session_id`
- `request_id`
- `task_code`: For example `T_filter`
- `step`: `apply_filter`
- `filter_value`: For example `today`, `COMP2850`
- `result_count`: Number of tasks shown after filtering
- `js_mode`

**Why log this?**

- To see which filters or views are actually used.
- To check whether the feature supports the “deadline reassurance” job story (S1).

---

## 3. Data Format and Storage

I will use a simple CSV file for the metrics so that I can open it easily in Excel or Google Sheets.

**Planned file path**

- `data/metrics.csv` (I will create this in a later week, not in Week 6)

**Header schema**

```csv
ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode
