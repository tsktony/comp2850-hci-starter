# Measures

## Completion (0/1)

- One value per task attempt:
  - `1` = task finished within the time limit **and** correct outcome
  - `0` = failed, abandoned, or over time limit
- Soft time limits per task:
  - `T1_filter`  – up to ~180 seconds
  - `T2_edit`    – up to ~120 seconds
  - `T3_delete`  – up to ~60 seconds
- Later we will just count how many attempts are `1` vs `0` for each task (and optionally split by JS-on / JS-off, keyboard-only, etc.).

---

## Time-on-task (ms)

- Source: server-side timing per request (from request start to response end).
- Only analyse successful attempts (where completion = 1).
- If logs are missing or broken, fall back to facilitator stopwatch times.
- When summarising, describe “typical” time per task (e.g. middle value) and note any obvious outliers.

---

## Errors

- We care mainly about:
  - **Validation errors** (e.g. blank title when adding or editing)
  - **Server-side errors** (non-200 responses, if any)
- Data source:
  - Rows in the metrics log where the event is recorded as a validation or server error.
- For each task we will:
  - Note how many attempts triggered at least one error.
  - List common error types (for example, `blank_title` appearing multiple times).

---

## Accessibility confirmations

For each pilot and each task we will make a quick note about:

- Keyboard-only completion:
  - Can the task be done using only keyboard (Tab / Shift+Tab / Enter / Space)?
- Status / feedback:
  - Is there a clear visual confirmation (e.g. “Task added”, “Task deleted”)?
  - For screen reader runs, does the status live region announce the message?
- Focus behaviour:
  - After errors, is focus still easy to recover (e.g. back to the input)?
  - After delete or filter, does the page remain easy to navigate?

These are recorded as short yes/no comments plus a few words of explanation, not as numbers.

---

## Subjective measures

### Confidence per task (1–5)

After each task, ask:

> “On a scale from 1 to 5, how confident are you that you completed that task correctly?”

- 1 = not confident at all  
- 5 = very confident  

Later we will compare:

- Average confidence for each task.
- Whether there are cases where participants completed the task but still reported low confidence (which suggests the feedback may be unclear).

### UMUX-Lite (2 questions, optional)

At the end of the whole session (if time allows), ask the two UMUX-Lite questions:

1. “This system’s capabilities meet my requirements.” (1–7)
2. “This system is easy to use.” (1–7)

These give a quick overall impression of perceived usability for the prototype.