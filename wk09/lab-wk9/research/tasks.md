# Evaluation Tasks — Week 9

This document defines the tasks used in the Week 9 peer pilots.  
Task codes match the starter repo shorthand:

- `T1_filter`: filter by `"meet"`, report count
- `T2_edit`: rename `"draft report"` → `"submit report"`
- `T3_delete`: delete `"tmp"`

---

## Task T1_filter – Filter by “meet”

**Scenario**

> “Before a meeting, you want to quickly check all tasks that contain the word **‘meet’** in the title, so you can see everything related to meetings in one place.”

**Setup**

- Seed the database with at least 6–8 tasks.
- At least **2 tasks** must contain `meet` in their title, for example:
  - `meet project partner`
  - `plan team meet-up`
- Other tasks **must not** contain `meet` in the title.

**Success criteria**

The participant should:

1. Find and use the **Filter tasks** input;
2. Type `meet` (case-insensitive);
3. See that only tasks whose titles contain `meet` remain in the list;
4. State the correct number of remaining tasks (e.g. “2 tasks”);
5. Finish within **2 minutes**;
6. Not hit any obvious validation errors or navigation dead ends.

**Metrics**

- Time from task start to when they state the final count (ms);
- Task completion (per attempt):
  - `1`   = filtered correctly **and** reported correct count;
  - `0.5` = used the filter but reported wrong count;
  - `0`   = did not complete or gave up within the time limit;
- Number and type of validation errors (if any);
- Confidence rating (1–5):  
  > “How confident are you that you found all tasks containing ‘meet’?”

**Accessibility checks**

- Does the result summary / count get announced by a screen reader?
- Can the whole task be completed **using keyboard only**?
- With JavaScript disabled (no-JS path), does filtering still work via full-page reload and query parameters (`?q=meet&page=1`)?

---

## Task T2_edit – Rename “draft report” to “submit report”

**Scenario**

> “Your task list contains an item called **‘draft report’**, but the work has moved on. You want to change it to **‘submit report’** so the list stays accurate.”

**Setup**

- Seed data must contain a task titled exactly `"draft report"`;
- This task should be visible on the current page.

**Success criteria**

The participant should:

1. Locate the `"draft report"` task in the list;
2. Use the **Edit** control to enter inline edit mode  
   (or the full-page `/tasks/{id}/edit` view in the no-JS path);
3. Change the title to **`submit report`** (case-insensitive match is fine);
4. After saving, see `"submit report"` instead of `"draft report"` in the list;
5. Finish within **90 seconds**;
6. If they accidentally submit a blank title, see the error message and still recover to complete the task.

**Metrics**

- Time from first clicking **Edit** on `"draft report"` to seeing `"submit report"` in the list (ms);
- Task completion (1 / 0.5 / 0, same rules as above);
- Number and type of validation errors (e.g. `blank_title`);
- Confidence rating (1–5):  
  > “How confident are you that you successfully renamed this task?”

**Accessibility checks**

- When the update succeeds, is a status message like  
  `"Task 'submit report' updated successfully."`  
  shown in the live region and announced?
- After saving, does focus stay on or near the updated task, instead of jumping to the top of the page?
- Can inline edit be completed using keyboard only (Tab / Enter / Escape)?
- With JavaScript disabled, can the edit still be done via the full-page `/tasks/{id}/edit` flow?

---

## Task T3_delete – Delete “tmp”

**Scenario**

> “Your list contains a temporary task called **‘tmp’**. It’s no longer needed and just adds noise. Remove it from the list.”

**Setup**

- Seed data must contain a task titled `"tmp"`;
- This task must be visible on the current page.

**Success criteria**

The participant should:

1. Locate the `"tmp"` task in the list;
2. Press the **Delete** control on that task;
3. With JavaScript enabled:
   - See the HTMX confirmation dialog (e.g. `Delete the task 'tmp'?`);
   - Confirm the deletion;
4. After the operation, `"tmp"` should no longer appear in the list;
5. Finish within **45 seconds**.

**Metrics**

- Time from first pressing **Delete** on `"tmp"` to the moment `"tmp"` disappears from the list (ms);
- Task completion (1 / 0.5 / 0):
  - `1`   = `"tmp"` removed successfully;
  - `0.5` = started deletion but cancelled or got stuck;
  - `0`   = task still present after the time limit;
- For the HTMX path, whether the confirmation dialog appeared and was accepted;
- Confidence rating (1–5):  
  > “How confident are you that ‘tmp’ has been deleted?”

**Accessibility checks**

- Does the Delete button have a meaningful accessible name, e.g.  
  `"Delete task: tmp"` (instead of several indistinguishable “Delete”s)?
- In the HTMX path, is the status message (e.g. `Deleted "tmp".`) announced via the live region?
- Can the delete be completed using keyboard only?
- With JavaScript disabled:
  - Does the simple POST form still delete the task correctly?
  - Is the lack of a confirmation dialog clearly documented as a known trade-off?

---

## Task Order

**Recommended pilot order**

1. **Warm-up** (not timed)  
   > “Spend a minute browsing the task list. Scroll, click, and get familiar with the interface. Tell me when you’re ready to start the timed tasks.”

2. **T1_filter** – Filter by `"meet"`  
3. **T2_edit** – Rename `"draft report"` → `"submit report"`  
4. **T3_delete** – Delete `"tmp"`  
5. **Debrief** – Short discussion and feedback

If you test multiple peers, you can occasionally swap the order of T1_filter and T2_edit to reduce learning effects, but it’s still helpful to always start with a simple warm-up.

---

## Notes for the Facilitator

- Do **not** teach the interface in advance — just read the scenarios and let participants discover the controls.
- Only step in if they are stuck for about **3 minutes**; note any intervention in your pilot notes.
- Pay attention to:
  - Long hesitations before using Filter or Edit;
  - Whether participants notice or ignore status messages;
  - Any keyboard or screen reader barriers.
- After each task, remember to collect the **confidence rating (1–5)** and record it next to the task code and session ID.