# Data Schema

Evaluation metrics are stored in a local CSV file (`data/metrics.csv`)
that is not shared publicly. Each row represents one logged event.

Columns (in order):

- **ts_iso**  
  ISO 8601 timestamp for the event, e.g. `2025-11-30T13:45:12.345Z`.

- **session_id**  
  Anonymous session identifier (6–8 characters) used to group events
  from the same participant. Contains no name, email, or student ID.

- **request_id**  
  Request identifier within the session, e.g. `r001`, `r002`, so a
  single attempt can be traced across logs.

- **task_code**  
  Short code for the evaluation task, matching `tasks.md`, e.g.
  `T1_filter`, `T2_edit`, `T3_add`, `T4_delete`.

- **step**  
  High-level outcome for this event, e.g.:
  - `success`
  - `validation_error`
  - `fail`
  - `server_error`

- **outcome**  
  Short description for error cases, e.g. `blank_title`, `too_long`.
  Left empty for successful events.

- **ms**  
  Duration of the attempt in milliseconds, measured on the server
  (from request start to response end).

- **http_status**  
  HTTP status code returned for this request, e.g. `200`, `400`, `500`.

- **js_mode**  
  JavaScript mode inferred from the request:
  - `on`  = HTMX / JS-enhanced path  
  - `off` = no-JS, full-page refresh path

This schema is only used for Week 9 evaluation; all data is anonymous
and remains inside the local project.