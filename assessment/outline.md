# Assessment Draft Outline — COMP2850 HCI

This outline maps my Week 9–10 work to the final assessment structure.
It is a planning document, not the final report.

---

## Section 1: Redesign Rationale (≈20%)

### 1.1 Priority Selection

- I focused my main redesign on **Task T2 (Edit Task)**, specifically the
  inline edit flow.
- Week 9 analysis showed:
  - T2_edit had the **slowest median time** and a relatively high spread (MAD),
    compared with the other tasks.
  - Qualitative notes recorded **hesitation** around where to click “Save”
    and whether the change had “really saved”.
- I selected T2_edit as the main target because:
  - It is a **core task** for maintaining the list.
  - Performance issues here affect almost every user segment.
  - It can be improved within the time constraints of Week 10.

### 1.2 Evidence from Week 9

I will summarise Week 9 evidence here using:

- **Quantitative data**:
  - From `analysis/analysis.csv` and `wk10/analysis/quantitative-summary.md`.
  - Show T2_edit median time, MAD, and completion rate compared to T1/T3/T4.
- **Qualitative observations**:
  - Quotes from pilot notes (e.g. P2 and P3) about:
    - Not being sure which button actually saves.
    - Double-checking the list after editing because feedback felt weak.
- **Accessibility angle**:
  - Even though all 6 pilots could eventually complete T2_edit, the
    variability and hesitation suggest potential inclusion issues for
    keyboard-first or future screen reader users.

---

## Section 2: Implementation (≈30%)

### 2.1 Fix 1: Make the Edit Flow Faster and Clearer (T2_edit)

- **Problem**:
  - T2_edit was slow and slightly error-prone.
  - Participants hesitated around “Save” and checked multiple times
    whether their change had been applied.
- **Changes** (high-level):
  - Made the primary action (“Save”/“Update”) visually stronger and consistent.
  - Tightened validation for blank titles and improved the error message.
  - Standardised success status messages so that add/edit/delete all give
    clear, short confirmations.
- **Code evidence**:
  - Before/after snippets from:
    - `src/main/kotlin/routes/TaskRoutes.kt` (POST `/tasks/{id}/edit`,
      POST `/tasks`, delete route).
    - View templates for the edit row if relevant.
  - These will appear in `wk10/assessment/03-key-diffs.md`.
- **WCAG / UX link**:
  - Primarily improves **feedback and error handling** (WCAG 3.3.x),
    and reduces cognitive load by making the primary path clearer.

### 2.2 Fix 2: Instrumentation & Metrics Logging

- **Problem**:
  - Week 9 used hand-timed durations; it was easy to lose precision.
- **Changes**:
  - Integrated a simple `Logger.log(...)` call into:
    - T2_edit
    - T3_add
    - T4_delete
  - Ensured that every action produces a row with:
    - `task_code`, `step`, `outcome`, `ms`, `js_mode`.
- **Code evidence**:
  - Before/after snippets from `TaskRoutes.kt` showing:
    - Manual timing with `System.currentTimeMillis()`.
    - Calls to `Logger.log(...)` on both success and validation error paths.
- **Impact**:
  - Makes Week 10 before/after metrics more reliable.
  - Supports the “evidence chain” requirement for the assessment.

### 2.3 Trade-Offs

- I will briefly discuss:
  - Why I focused on **one primary flow (T2_edit)** instead of many small fixes.
  - The fact that I did not fully implement all ARIA attributes for screen readers
    due to time; however, messages are written to be SR-friendly and the
    structure is ready for further enhancement.

---

## Section 3: Verification (≈30%)

### 3.1 Re-Testing After the Fix

- Describe how I re-tested using:
  - `P6_POST`, `P7_POST`, `P8_POST` verification runs.
  - Same basic tasks as in Week 9 to keep comparability.
- Summarise key metrics (from `wk10/analysis/before-after-T2.md`):
  - **Median time** for T2_edit before vs after (all / JS-on / JS-off).
  - **MAD** before vs after.
  - **Error rate** before vs after (with note that the Week 10 error
    was intentional to test validation).
- Explain in words what improved:
  - Much faster typical time.
  - Less variation between participants.
  - Clearer feedback (less hesitation).

### 3.2 Accessibility & Regression

- Use the narrative from `wk10/evidence/reverification.md`:
  - Keyboard behaviour (Tab order, focus visibility).
  - No-JS parity (all tasks still completable).
  - Status messages (clarity and consistency).
- Be honest about limitations:
  - Due to time, I did not run a full screen reader test in Week 10.
  - I will treat this as a limitation in the assessment and explain
    what I would do next (e.g. NVDA / VoiceOver tests).

---

## Section 4: Reflection & Future Work (≈20%)

### 4.1 Process Reflection

I will reflect on:

- What went well:
  - Using metrics from Week 9 to pick a concrete target (T2_edit).
  - Having a clear redesign brief before coding.
- What was hard:
  - Keeping parity between HTMX and no-JS paths.
  - Balancing small improvements with the time needed for verification.
- What I would change in my workflow for next time.

### 4.2 Deferred Improvements

I will list a few items that I considered but did not implement, for example:

- Stronger ARIA usage for validation errors (role="alert", aria-describedby).
- More visible focus styles that strictly meet WCAG contrast ratios.
- Additional tasks or scenarios (e.g. bulk operations, filters that persist across sessions).

These will be framed as **future work** rather than failures.

---

## Files to Re-Use for the Final Submission

This outline assumes I will reuse the following Week 9–10 artefacts:

- `wk10/analysis/quantitative-summary.md`  
- `wk10/analysis/before-after-T2.md`  
- `wk10/redesign/priorities.md`  
- `wk10/evidence/reverification.md`  
- `wk10/assessment/03-key-diffs.md` (code snippets to be created)  
- `data/metrics.csv` (P1–P8 combined)

Together, these documents will feed into the final
`submission-template.md` that I submit to Gradescope.