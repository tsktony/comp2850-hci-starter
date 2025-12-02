# Redesign Brief — Edit Task Flow (T2_edit)

## Problem
Pilot data shows that **T2_edit** is noticeably slower and slightly more error-prone than the other flows.  
From `analysis/analysis.csv`, the median time for JS-on is about **37.5s** (MAD ≈ 2.5s), and overall completion is only **5/6 ≈ 83%**.  
Qualitative notes say participants hesitated to find the **Save** action, and one user briefly cleared the title and hit the validation error before recovering.

## Goal
Make the edit flow faster and clearer, with fewer accidental errors.

- Target median time (JS-on) for **T2_edit**: **≤ 30 000ms**
- Target completion rate (all modes combined): **≥ 90%**
- Target error_rate for T2_edit: **≤ 0.10**
- Target mean confidence rating (T2): **≥ 4/5**

## Inclusion Impact
The edit flow is a core feature for anyone who regularly maintains their task list.  
The current design especially affects:

- **Keyboard-first users**: they tab through multiple controls in the inline row, so any hesitation around “where is Save?” costs extra time.
- **Careful / cautious readers**: they spend longer checking if their change really saved because the feedback is not very strong.
- In future, **screen reader users** could also be affected if the error / success messages are not clearly linked to the input.

Improving this flow should benefit both “power” users (keyboard-heavy) and more cautious users who worry about losing data.

## Changes (server-first + no-JS parity)
Planned changes are all small, server-first tweaks that keep the no-JS path working:

- **Clarify the primary action**
  - Make the **Save** button visually stronger than Cancel (primary style vs. secondary).
  - Keep the label consistent (e.g. “Save” or “Update task”) so users always know which control finishes the edit.
  - Keep Save directly after the text input in the template, so the scan order is “edit → Save”.

- **Tighten validation feedback for blank titles**
  - When the title is blank, show a clear error message **next to the input**, not only in a global bar.
  - Add `aria-invalid="true"` to the title field when there is an error.
  - Link the field and error message using `aria-describedby="title-error-{{ task.id }}"` in the template.
  - Keep behaviour identical in JS-on (HTMX) and JS-off (redirect) paths.

- **Make success feedback more consistent**
  - After a successful edit, send a clear status message such as  
    `Task "X" updated successfully.`  
    into the existing `#status` region, so add / edit / delete all use the same feedback pattern.

## Acceptance Tests
The redesign is “done” when:

- **Keyboard-only path PASS**
  - User can tab into the edit field, reach the Save button easily, and submit without needing the mouse.
  - If they trigger the blank-title error, focus returns to the field and the error text is visible and understandable.

- **SR / future accessibility PASS (design-ready)**
  - The title input has `aria-invalid` and an `aria-describedby` link to the error text when validation fails.
  - Error and success messages are written in plain, descriptive language that will make sense when read out by a screen reader.

- **JS-off parity PASS**
  - Editing a task works in the no-JS path via POST–Redirect–GET.
  - Blank-title errors are also shown next to the field on the GET page, with the same wording as the HTMX path.
  - No-JS users can still complete T2 without extra steps or hidden states.

## Measurement Plan
After implementing the changes in Week 10 Lab 2:

- **Re-run T2_edit** with 3–4 peers using the same scenario as Week 9:
  - At least one “standard” mouse user.
  - At least one keyboard-first user if possible.
- For each participant, record:
  - Start/end time for T2_edit (stopwatch in seconds → convert to ms).
  - Success (0/1), number of validation errors during T2.
  - Confidence rating for T2 (1–5).

Then:

- Update `analysis/analysis.csv` with the new T2_edit timings and completion.
- Check that:
  - Median T2_edit (JS-on) ≤ 30 000ms,
  - Overall completion for T2_edit ≥ 90%,
  - Error_rate for T2_edit ≤ 0.10.
- Add a short “before vs after” paragraph for T2 in `wk10/analysis/summary.md`, explaining how the metrics changed and whether the redesign goal was met.