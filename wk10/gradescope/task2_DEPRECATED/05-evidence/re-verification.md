# Re-Verification — Week 10 Lab 2

## Scope

Fix implemented in Week 10 Lab 2:

- Focus task: **T2_edit (Edit Task)** inline flow
- Small supporting changes around:
  - Status messages for add/edit/delete
  - Server-side logging (metrics) for all four tasks

The goal was to make the edit flow faster and clearer while keeping the
server-first + no-JS parity design.

## Variants Tested

I ran three quick post-fix verification runs (logged as `P6_POST`, `P7_POST`, `P8_POST` in `metrics.csv`):

- **P6_POST** — standard mouse + HTMX (JS-on, typical use)
- **P7_POST** — keyboard-first, JS-on, with one **intentional blank-title error** to test validation feedback
- **P8_POST** — **no-JS** fallback (JavaScript disabled in the browser)

Each participant performed the same four tasks as in Week 9:

1. T1_filter — filter the list
2. T2_edit — edit an existing task
3. T3_add — add a new task
4. T4_delete — delete a task

---

## Keyboard & Focus Behaviour

- All interactive controls on `/tasks` can be reached using **Tab / Shift+Tab**:
  - Filter field
  - Add form
  - Edit / Delete controls
  - Pagination links (if present)
- Focus order is top-to-bottom and matches the visual layout.
- Focus indicators are clearly visible on:
  - Links, buttons
  - Text inputs
- No keyboard traps observed:
  - It is always possible to Tab into, through, and out of the task list.

**Result**: Keyboard-only use is still fully supported after the fix (no regressions).

---

## No-JS Parity

With JavaScript disabled and the page hard-refreshed:

- **T1_filter** still works with full-page reload.
- **T2_edit** works end-to-end:
  - Edit link triggers a full-page reload with the row in edit mode.
  - Submitting a valid title updates the task and redirects back to the list.
  - Submitting a **blank** title triggers a validation error and redirects back with an error flag (handled in the template).
- **T3_add** and **T4_delete** still use POST–Redirect–GET:
  - Refreshing the page does **not** duplicate submissions.

**Result**: All four tasks remain completable in **JS-off** mode. The fix did not break the no-JS path.

---

## Status & Feedback Messages

- After **adding** a task (JS-on):
  - A clear status message is shown in the existing status area (e.g. `Task "X" added successfully.`).
- After **editing** a task (JS-on):
  - A similar status message confirms the update (e.g. `Task "X" updated successfully.`).
- After **deleting** a task:
  - A short confirmation message (“Task deleted.”) is shown.
- When a **blank title** is submitted:
  - The user immediately sees a short, direct error message:
    - “Title is required. Please enter at least one character.”

These messages are consistent with the Week 10 redesign brief: they make it easier
for cautious users to see whether their change “really went through”.

---

## Metrics Cross-Check (Summary)

The post-fix runs (`P6_POST`, `P7_POST`, `P8_POST`) are included in `data/metrics.csv`.

For **T2_edit**:

- Success attempts (after the fix):
  - P6_POST: `r102` — success
  - P7_POST: `r107` — success (after one deliberate validation error `r106`)
  - P8_POST: `r111` — success (no-JS)
- Validations:
  - One **intentional** blank-title validation error (P7_POST, `r106`).
- Overall:
  - All three post-fix participants completed T2_edit successfully.
  - The logged durations are much lower than in Week 9 (see `wk10/analysis/before-after-T2.md`).

**Result**: Quantitative data confirms that the T2_edit flow is now faster and more
consistent, without breaking completion for any variant.

---

## Regression Summary

- ✅ Keyboard navigation: unchanged or slightly improved (no traps, clear focus)
- ✅ No-JS parity: all four tasks (T1–T4) remain completable with JavaScript disabled
- ✅ Status messages: now more consistent and clearer after add/edit/delete
- ⚠ Screen reader testing: **not fully re-run** in Week 10 (time-limited);
  however, error messages and status text are written to be SR-friendly
  and the structure is ready for future ARIA improvements.

Overall, the Week 10 changes **improve clarity and speed for T2_edit** and do not
introduce regressions for the other tasks.