# Pilot Findings Analysis — Week 9

**Participants**: 5  
- P1 – “standard & confident”, JS on  
- P2 – “fast but typo-prone”, JS on  
- P3 – “keyboard-only”, JS on  
- P4 – “No-JS tester”, JS off  
- P5 – “careful reader”, JS on  

**Date range**: Week 9 pilot sessions (timed with stopwatch + notes)

---

## 1. Quantitative Summary

All four tasks were completed by all 5 participants  
→ **success rate = 100%** for every task.

Metrics below use the stopwatch times and confidence scores recorded during the pilots.

---

### Task T1_filter — Filter by “meet” and report count

**Goal**: Use the filter box to search for “meet” and report how many tasks remain.

| Metric              | HTMX (JS on, n=4) | No-JS (n=1) | Overall (n=5) |
|---------------------|-------------------|-------------|---------------|
| Mean time (s)       | 24.5              | 35.0        | 26.6          |
| Success rate        | 100%              | 100%        | 100%          |
| Mean errors (count) | 0.0               | 0.0         | 0.0           |
| Mean confidence     | 4.5 / 5           | 4.0 / 5     | 4.4 / 5       |

**Interpretation**

- Everyone completed the filter task successfully with **no validation errors**.
- HTMX users were faster on average (≈24.5s) than the No-JS participant (35s), which matches expectations because HTMX avoids full page reloads.
- Confidence is high overall (4.4/5).  
  - HTMX users: very confident (4.5/5) — they saw the list change instantly.  
  - No-JS participant: slightly less confident (4/5) and waited for a full reload.

**Notable behaviours**

- P1 found the search box immediately and reacted positively when the list changed.  
- P2 initially typed the wrong word, noticed no results, then corrected it — this shows the filter behaviour is understandable.  
- P3 (keyboard-only) needed a few extra Tab presses to reach the filter, but still completed within 30 seconds.

---

### Task T2_edit — Rename “draft report” → “submit report”

**Goal**: Enter edit mode, change the title, and save successfully.

| Metric              | HTMX (JS on, n=4) | No-JS (n=1) | Overall (n=5) |
|---------------------|-------------------|-------------|---------------|
| Mean time (s)       | 35.0              | 45.0        | 37.0          |
| Success rate        | 100%              | 100%        | 100%          |
| Mean errors (count) | 0.25              | 0.0         | 0.2           |
| Mean confidence     | 4.0 / 5           | 4.0 / 5     | 4.0 / 5       |

**Interpretation**

- Everyone managed to rename the task successfully.
- Editing is **slower** than filtering:
  - HTMX: ~35s on average  
  - No-JS: ~45s (page reload into edit mode, then reload after save)
- Error rate is low but not zero:
  - P2 tried to save a **blank title once**, triggered server-side validation, and then recovered.  
  - This confirms the validation message is visible and understandable.
- Confidence is moderate (4/5): people can complete the task, but there is some hesitation around the edit/save flow.

**Notable behaviours**

- P1 paused briefly after entering edit mode to locate the Save button (it is not very visually dominant).  
- P3 (keyboard-only) discovered that pressing **Enter** saves the edit and commented that this felt convenient.  
- P5 read the original title carefully, edited it, and checked again after saving — behaviour is slow but very controlled.  
- P2’s attempt to clear the field completely shows that users sometimes expect “delete title” to be valid; the validation rule and message prevented bad data.

---

### Task T3_add — Add a new task (“New Task”)

**Goal**: Use the add form to create a new task with a given title.

| Metric              | HTMX (JS on, n=4) | No-JS (n=1) | Overall (n=5) |
|---------------------|-------------------|-------------|---------------|
| Mean time (s)       | 20.5              | 25.0        | 21.4          |
| Success rate        | 100%              | 100%        | 100%          |
| Mean errors (count) | 0.0               | 0.0         | 0.0           |
| Mean confidence     | 5.0 / 5           | 5.0 / 5     | 5.0 / 5       |

**Interpretation**

- This is the **strongest task**:
  - Fastest completion times (≈21s overall).
  - No validation errors at all.
  - Confidence is **maximum** (everyone gave 5/5).
- The add-task form appears clear and behaves as users expect in both HTMX and No-JS modes.

**Notable behaviours**

- P1 and P2 completed smoothly in under 25 seconds.  
- P3 and P5 both had no issues using keyboard or careful reading.  
- P4 (No-JS) used a traditional POST + full page reload but was still very confident, probably because the new item was clearly visible in the refreshed list.

---

### Task T4_delete — Delete “tmp”

**Goal**: Delete a known task from the list.

| Metric              | HTMX (JS on, n=4) | No-JS (n=1) | Overall (n=5) |
|---------------------|-------------------|-------------|---------------|
| Mean time (s)       | 16.75             | 15.0        | 16.4          |
| Success rate        | 100%              | 100%        | 100%          |
| Mean errors (count) | 0.0               | 0.0         | 0.0           |
| Mean confidence     | 4.75 / 5          | 3.0 / 5     | 4.4 / 5       |

**Interpretation**

- Deletion is **quick and accurate** for everyone.
- HTMX users see a browser confirm dialog via `hx-confirm` and are very confident (4.75/5).
- The No-JS participant is noticeably less confident (3/5) because:
  - There is no confirmation dialog.
  - The page simply reloads and the task disappears, which feels a bit abrupt.
- Time is short in both modes (≈15–17s), but the interaction feels riskier in the No-JS path.

**Notable behaviours**

- P1 and P2 confirmed very quickly: confirm dialog → accept → task removed.  
- P3 (keyboard-only) took longer (25s) because Tab focus has to move across the row to the Delete button.  
- P4 explicitly asked “Is it just gone now?” after using the No-JS delete, highlighting the lack of confirmation.  
- P5 carefully read the confirmation dialog text before accepting.

---

## 2. Qualitative Themes

### Theme 1 — Confirmation and feedback are critical

**Evidence**

- Several participants commented on feedback:
  - P1 liked seeing the list update immediately after filtering and adding.  
  - P4 (No-JS) felt unsure after delete and verbally checked whether the item was really gone.  
- Confidence scores:
  - T3_add has perfect confidence (5/5).  
  - T4_delete drops to 3/5 for the No-JS participant.

**Interpretation**

- When feedback is explicit (status text, confirm dialog, visible new item in list), users feel in control.
- The No-JS delete flow has a **behavioural gap**: the task disappears without any dialog or extra message, which reduces trust even though the action is technically correct.

**Design implication**

- Keep strong feedback on HTMX paths (status messages + confirm dialogs).
- For No-JS, consider adding:
  - A dedicated delete confirmation page, or  
  - A clear success message after the PRG redirect (for example, “Task ‘tmp’ deleted.”).

---

### Theme 2 — Edit and Save flow is slightly harder to discover

**Evidence**

- P1 paused briefly after entering edit mode to locate the Save button.  
- P2 accidentally submitted a blank title and then recovered using the validation message.  
- P3 relied on keyboard and only discovered “Enter to save” by trying it.  
- Confidence on T2_edit is lower (4/5) than on T3_add (5/5).

**Interpretation**

- The edit flow works, but it is **less obvious** than the add-task form.
- Users are not completely sure:
  - Where the Save control is.
  - Whether Cancel will discard or keep changes.

**Design implication**

- Make the Save action more visually prominent (button styling, placement).  
- Clarify the Cancel label (for example, “Cancel (discard changes)”).  
- Optionally add a short status message after successful edit (“Task updated.”).

---

### Theme 3 — Keyboard and accessibility support are generally good, but a bit slower

**Evidence**

- P3 completed all tasks using keyboard only:
  - All tasks were successful with no errors.
  - Times were slightly slower but still reasonable (e.g. T1 = 30s, T4 = 25s).
- P3 explicitly mentioned that pressing Enter to save the edit felt good.
- Keyboard navigation cost is visible:
  - Reaching Delete takes multiple Tab presses across each row.

**Interpretation**

- The interface is **functionally keyboard-accessible**:
  - All interactive elements are reachable.
  - No obvious keyboard traps appeared during pilots.
- Efficiency for keyboard users is slightly lower, especially for actions at the end of a row (Delete).

**Design implication**

- Keep the current keyboard support; it is working well enough.  
- If time allows, consider:
  - Tweaking tab order or grouping controls.  
  - Providing small shortcuts or better focus management after actions.

---

### Theme 4 — No-JS mode works but feels slower and less “smart”

**Evidence**

- P4 (No-JS) completed all tasks with no errors, but:
  - Needed to press Enter on T1_filter because they expected auto-update.  
  - Described edit and delete as “a bit slow, but usable”.  
  - Confidence on delete (T4) was only 3/5.

**Interpretation**

- Functionally, No-JS parity is good: all four tasks are possible and completed successfully.
- Perception is different:
  - Extra full-page reloads make the system feel slower and less modern.  
  - Lack of a confirm dialog for delete creates uncertainty.

**Design implication**

- For this assignment, the No-JS implementation is acceptable (parity achieved).  
- In a real product, I would:
  - Add a No-JS confirmation step for delete, and  
  - Add a visible success message after redirects.

---

## 3. Accessibility Observations

Even though I did not run a full screen-reader session, the pilots still reveal some accessibility aspects:

- **Keyboard-only**:
  - All tasks are completable with keyboard only.
  - Time is slightly higher, but there are no hard blocks.
- **Visual clarity**:
  - The filter box and add form are easy to find.
  - The Save button in edit mode could be more visually prominent.
- **Status feedback**:
  - HTMX flows give clear confirmation (dialogs + visible updates).
  - No-JS delete does not provide equivalent feedback beyond the item disappearing.

Overall, accessibility for keyboard users looks good, but delete feedback in No-JS is a weakness.

---

## 4. Prioritised Issues for Redesign

Based on frequency and impact:

1. **High priority — No-JS delete feedback is weak**  
   - Symptom: P4 unsure whether delete really happened; confidence only 3/5.  
   - Impact: Users may not fully trust destructive actions, especially without JS.  
   - Plan: Add a clear success message after PRG redirect or a dedicated confirmation step.

2. **Medium priority — Edit/save flow not completely obvious**  
   - Symptom: Short pauses to find Save; one blank-title error from P2.  
   - Impact: Small but affects efficiency and confidence on T2_edit.  
   - Plan: Improve Save button prominence; clarify Cancel text; show a “Task updated” status.

3. **Low priority — Keyboard efficiency for delete**  
   - Symptom: P3 needed many Tab presses to reach Delete; T4 time was longest for the keyboard-only user.  
   - Impact: Only affects some users; still successful but slower.  
   - Plan: Optional refinement of tab order or consider an alternative placement for destructive actions.

---

## 5. Summary

- All four tasks achieved **100% completion** across 5 participants.  
- Add and filter flows are strong (fast, high confidence, no errors).  
- Edit works but could be smoother; delete is functional but has a No-JS feedback gap.  
- Keyboard accessibility is good; No-JS parity works but feels slower and less reassuring.

These findings will feed directly into my Week 10 redesign priorities:
1. Improve delete confirmation and feedback in No-JS mode.  
2. Clarify the edit/save/cancel interaction.  
3. Optionally refine keyboard efficiency for destructive actions.
