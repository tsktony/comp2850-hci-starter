# COMP2850 HCI Assessment: Evaluation & Redesign Portfolio

> **📥 Download this template**: [COMP2850-submission-template.md](/downloads/COMP2850-submission-template.md)
> Right-click the link above and select "Save link as..." to download the template file.

**Student**: Chenguo Wan 201826896
**Submission date**: 02/12/2025
**Academic Year**: 2025-26

---

## Privacy & Ethics Statement

- [x] I confirm all participant data is anonymous (session IDs use P1_xxxx format, not real names)
- [x] I confirm all screenshots are cropped/blurred to remove PII (no names, emails, student IDs visible)
- [x] I confirm all participants gave informed consent
- [x] I confirm this work is my own (AI tools used for code assistance are cited below)

**AI tools used** (if any): [e.g., "Copilot for route handler boilerplate (lines 45-67 in diffs)"]

---

## 1. Protocol & Tasks

### Link to Needs-Finding (LO2)

**Week 6 Job Story #1 — Fragmented task sources (S2)**:  
> When different teachers put tasks in different places (assignment area, announcements, group chat, or only spoken at the end of class),  
> I want a single place where I can see all my tasks in a clear list,  
> so I don’t have to act like a detective, copy-pasting from many channels and worrying that I have missed one,  
> because if I miss even one spoken instruction or hidden announcement, I can lose participation marks or damage my grade.
**How Task 1 tests this**:  
Task 1 asks participants to filter and scan a unified task list by keyword and then check how many matching tasks there are, which directly tests whether my interface works as a single, clear place to view and manage tasks instead of hunting across multiple channels.
---

### Evaluation Tasks (4-5 tasks)

#### Task 1 (T1): Filter tasks by keyword

- **Scenario**: You are worried you might miss a coursework deadline and want to quickly see only the tasks related to one module.
- **Action**: "Use the filter box to show only tasks that contain the word `COMP2850` in the title."
- **Success**: The list updates and only shows tasks whose titles contain `COMP2850`. The number of tasks matches what the participant expects.
- **Target time**: < 30 seconds
- **Linked to**: Week 6 Job Story S2 (Fragmented task sources / centralised list)

#### Task 2 (T2): Edit a task title

- **Scenario**: You notice that a task title is too vague and want to make it more specific.
- **Action**: "Edit the task called `Submit invoices` so that the title becomes `Submit invoices by Friday`."
- **Success**: The task appears in the list with the updated title and there are no duplicate tasks.
- **Target time**: < 40 seconds (before redesign) / < 5 seconds (after redesign)
- **Linked to**: Week 6 Job Story S6 (Clear confirmation and error feedback)

#### Task 3 (T3): Add a new task

- **Scenario**: You remember a new small task you need to do this week and want to add it quickly.
- **Action**: "Add a new task called `Email group about meeting` to the list."
- **Success**: The new task appears in the list once, with the correct title.
- **Target time**: < 30 seconds
- **Linked to**: Week 6 Job Story S2 (Centralised list of tasks)

#### Task 4 (T4): Delete a completed task

- **Scenario**: You have finished a task and want to remove it so that the list feels cleaner.
- **Action**: "Delete the task called `Buy printer paper` from the list."
- **Success**: The task disappears from the list and does not come back after refreshing the page.
- **Target time**: < 20 seconds
- **Linked to**: Week 6 Job Story S5 (Managing stress instead of demanding perfection)
---

### Consent Script (They Read Verbatim)

**Introduction**:
"Thank you for participating in my HCI evaluation. This will take about 15 minutes. I'm testing my task management interface, not you. There are no right or wrong answers."

**Rights**:
- [x] "Your participation is voluntary. You can stop at any time without giving a reason."
- [x] "Your data will be anonymous. I'll use a code (like P1) instead of your name."
- [x] "I may take screenshots and notes. I'll remove any identifying information."
- [x] "Do you consent to participate?" [Wait for verbal yes]

**Recorded consent timestamp**: **Recorded consent timestamps**  

For each participant I recorded verbal consent before starting Task 1:

- **P1_STD** – consented on **27/11/2025 at 09:10** (standard mouse + HTMX, full task flow T1–T4)  
- **P2_TYPO** – consented on **27/11/2025 at 09:30** (typical “typo-prone” user, HTMX)  
- **P3_KB** – consented on **27/11/2025 at 09:50** (keyboard-first user, HTMX)  
- **P4_NOJS** – consented on **27/11/2025 at 10:15** (no-JavaScript variant, full page reloads)  
- **P5_READ** – consented on **27/11/2025 at 10:40** (careful “check-everything” reader, HTMX)  

For the Week 10 verification pilots (post-redesign) I also obtained verbal consent before re-running Task 2 (edit task):

- **P6_POST** – consented on **01/12/2025 at 09:05** (standard HTMX verification of T2)  
- **P7_POST** – consented on **01/12/2025 at 09:32** (keyboard-first verification of T2, including intentional validation error)  
- **P8_POST** – consented on **01/12/2025 at 10:11** (no-JavaScript verification of T2)  

All participants were reminded that they could stop at any time and that their data would be stored using anonymous session IDs only (as shown in `metrics.csv`).
---

## 2. Findings Table

**Instructions**: Filled with 3 key findings from Week 9 + Week 10 pilots. Data sources mainly `metrics.csv` + my pilot notes.

| Finding | Data Source | Observation (Quote/Timestamp) | WCAG | Impact (1-5) | Inclusion (1-5) | Effort (1-5) | Priority |
|---------|-------------|------------------------------|------|--------------|-----------------|--------------|----------|
| F1 — T2 edit flow is very slow and hesitant before redesign | `metrics.csv` rows r002, r007, r011, r015, r019 (Week 9 T2_edit successes) | Week 9 T2_edit success times were 25 000–45 000 ms (median ≈ 40 000 ms, MAD ≈ 5 000 ms). Participants needed extra time to check whether the edit had really saved. | 4.1.3 Status Messages (AA) | 4 | 4 | 3 | (4+4)-3 = **5** |
| F2 — Validation error for blank title is not clearly linked to the input field | `metrics.csv` row r006 (`P2_TYPO`, T2_edit, validation_error, blank_title) + pilot note for P2 | P2 cleared the title and got a blank-title error; they hesitated before recovering because the error message was not clearly tied to the text field. In the original code, the status div had no stronger text and was not structurally linked to the input. | 3.3.1 Error Identification (A), 4.1.3 Status Messages (AA) | 5 | 5 | 3 | (5+5)-3 = **7** |
| F3 — No-JS users can complete T2 but with much higher time and cognitive load | `metrics.csv` row r015 (`P4_NOJS`, T2_edit, success, 45 000 ms) + no-JS pilot notes | The no-JS participant completed T2_edit, but took about 45 000 ms and needed more scrolling/checking. The flow was technically possible, but the feedback was weaker and slower to interpret compared to HTMX. | 2.1.1 Keyboard (A), 4.1.3 Status Messages (AA) | 4 | 4 | 2 | (4+4)-2 = **6** |

**Priority formula**: (Impact + Inclusion) - Effort

**Top 3 priorities for redesign** (used to drive Week 10 Lab 2):

1. **F2 — Validation error for blank title not clearly linked to input** — Priority **7**  
2. **F3 — No-JS users much slower on T2_edit** — Priority **6**  
3. **F1 — T2 edit flow generally slow and hesitant** — Priority **5**  
---

## 3. Pilot Metrics (metrics.csv)

**Instructions**: Paste your raw CSV data here OR attach metrics.csv file

```csv
ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode
2025-11-22T14:18:23.456Z,P1_a7f3,req_001,T1_add,success,,890,200,on
[Your metrics data here - all rows from Logger.kt output]
```

**Participant summary**:
- **P1**: [Variant - e.g., "Standard mouse + HTMX"]
- **P2**: [Variant - e.g., "Keyboard-only, HTMX-on"]
- **P3** (if applicable): [Variant]
- **P4** (if applicable): [Variant]

**Total participants**: [n=2, 3, or 4]

---

## 4. Implementation Diffs

**Instructions**: Show before/after code for 1-3 fixes. Link each to findings table.

### Fix 1: [Fix Name]

**Addresses finding**: [Finding #X from table above]

**Before** ([file path:line number]):
```kotlin
// ❌ Problem code
[Paste your original code here]
```

**After** ([file path:line number]):
```kotlin
// ✅ Fixed code
[Paste your improved code here]
```

**What changed**: [1 sentence - what you added/removed/modified]

**Why**: [1 sentence - which WCAG criterion or usability issue this fixes]

**Impact**: [1-2 sentences - how this improves UX, who benefits]

---

### Fix 2: [Fix Name]

**Addresses finding**: [Finding #X]

**Before**:
```kotlin
[Original code]
```

**After**:
```kotlin
[Fixed code]
```

**What changed**:

**Why**:

**Impact**:

---

[Add Fix 3 if applicable]

---

## 5. Verification Results

### Part A: Regression Checklist (20 checks)

**Instructions**: Test all 20 criteria. Mark pass/fail/n/a + add notes.

| Check | Criterion | Level | Result | Notes |
|-------|-----------|-------|--------|-------|
| **Keyboard (5)** | | | | |
| K1 | 2.1.1 All actions keyboard accessible | A | [pass/fail] | [e.g., "Tested Tab/Enter on all buttons"] |
| K2 | 2.4.7 Focus visible | AA | [pass/fail] | [e.g., "2px blue outline on all interactive elements"] |
| K3 | No keyboard traps | A | [pass/fail] | [e.g., "Can Tab through filter, edit, delete without traps"] |
| K4 | Logical tab order | A | [pass/fail] | [e.g., "Top to bottom, left to right"] |
| K5 | Skip links present | AA | [pass/fail/n/a] | [e.g., "Skip to main content works"] |
| **Forms (3)** | | | | |
| F1 | 3.3.2 Labels present | A | [pass/fail] | [e.g., "All inputs have <label> or aria-label"] |
| F2 | 3.3.1 Errors identified | A | [pass/fail] | [e.g., "Errors have role=alert (FIXED in Fix #1)"] |
| F3 | 4.1.2 Name/role/value | A | [pass/fail] | [e.g., "All form controls have accessible names"] |
| **Dynamic (3)** | | | | |
| D1 | 4.1.3 Status messages | AA | [pass/fail] | [e.g., "Status div has role=status"] |
| D2 | Live regions work | AA | [pass/fail] | [e.g., "Tested with NVDA, announces 'Task added'"] |
| D3 | Focus management | A | [pass/fail] | [e.g., "Focus moves to error summary after submit"] |
| **No-JS (3)** | | | | |
| N1 | Full feature parity | — | [pass/fail] | [e.g., "All CRUD ops work without JS"] |
| N2 | POST-Redirect-GET | — | [pass/fail] | [e.g., "No double-submit on refresh"] |
| N3 | Errors visible | A | [pass/fail] | [e.g., "Error summary shown in no-JS mode"] |
| **Visual (3)** | | | | |
| V1 | 1.4.3 Contrast minimum | AA | [pass/fail] | [e.g., "All text 7.1:1 (AAA) via CCA"] |
| V2 | 1.4.4 Resize text | AA | [pass/fail] | [e.g., "200% zoom, no content loss"] |
| V3 | 1.4.11 Non-text contrast | AA | [pass/fail] | [e.g., "Focus indicator 4.5:1"] |
| **Semantic (3)** | | | | |
| S1 | 1.3.1 Headings hierarchy | A | [pass/fail] | [e.g., "h1 → h2 → h3, no skips"] |
| S2 | 2.4.1 Bypass blocks | A | [pass/fail] | [e.g., "<main> landmark, <nav> for filter"] |
| S3 | 1.1.1 Alt text | A | [pass/fail] | [e.g., "No images in interface OR all have alt"] |

**Summary**: [X/20 pass], [Y/20 fail]
**Critical failures** (if any): [List any Level A fails]

---

### Part B: Before/After Comparison

**Instructions**: Compare Week 9 baseline (pre-fix) to Week 10 (post-fix). Show improvement.

| Metric | Before (Week 9, n=X) | After (Week 10, n=Y) | Change | Target Met? |
|--------|----------------------|----------------------|--------|-------------|
| SR error detection | [e.g., 0/2 (0%)] | [e.g., 2/2 (100%)] | [e.g., +100%] | [✅/❌] |
| Validation error rate | [e.g., 33%] | [e.g., 0%] | [e.g., -33%] | [✅/❌] |
| Median time [Task X] | [e.g., 1400ms] | [e.g., 1150ms] | [e.g., -250ms] | [✅/❌] |
| WCAG [criterion] pass | [fail] | [pass] | [— ] | [✅/❌] |

**Re-pilot details**:
- **P5** (post-fix): [Variant - e.g., "Screen reader user, NVDA + keyboard"] - [Date piloted]
- **P6** (if applicable): [Variant] - [Date]

**Limitations / Honest reporting**:
[If metrics didn't improve or only modestly: explain why. Small sample size? Wrong fix? Needs more iteration? Be honest - valued over perfect results.]

---

## 6. Evidence Folder Contents

**Instructions**: List all files in your evidence/ folder. Provide context.

### Screenshots

| Filename | What it shows | Context/Link to finding |
|----------|---------------|-------------------------|
| before-sr-error.png | Error message without role="alert" | Finding #1 - SR errors not announced |
| after-sr-error.png | Error message WITH role="alert" added | Fix #1 verification |
| regression-axe-report.png | axe DevTools showing 0 violations | Verification Part A |
| [your-screenshot-3.png] | [Description] | [Which finding/fix this supports] |

**PII check**:
- [ ] All screenshots cropped to show only relevant UI
- [ ] Browser bookmarks/tabs not visible
- [ ] Participant names/emails blurred or not visible

---

### Pilot Notes

**Instructions**: Attach pilot notes as separate files (P1-notes.md, P2-notes.md, etc.). Summarize key observations here.

**P1** ([ Variant - e.g., "Standard mouse + HTMX"]):
- **Key observation 1**: [Quote + timestamp - e.g., "Struggled with filter button (09:47)"]
- **Key observation 2**: [Quote + timestamp]

**P2** ([Variant]):
- **Key observation 1**: [Quote + timestamp]
- **Key observation 2**: [Quote + timestamp]

[Repeat for P3, P4 if applicable]

---

## Evidence Chain Example (Full Trace)

**Instructions**: Pick ONE finding and show complete evidence trail from data → fix → verification.

**Finding selected**: [e.g., "Finding #1 - SR errors not announced"]

**Evidence trail**:
1. **Data**: metrics.csv lines 47-49 show P2 (SR user) triggered validation_error 3 times
2. **Observation**: P2 pilot notes timestamp 14:23 - Quote: "I don't know if it worked, didn't hear anything"
3. **Screenshot**: before-sr-error.png shows error message has no role="alert" or aria-live
4. **WCAG**: 3.3.1 Error Identification (Level A) violation - errors not programmatically announced
5. **Prioritisation**: findings-table.csv row 1 - Priority score 7 (Impact 5 + Inclusion 5 - Effort 3)
6. **Fix**: implementation-diffs.md Fix #1 - Added role="alert" + aria-live="assertive" to error span
7. **Verification**: verification.csv Part A row F2 - 3.3.1 now PASS (tested with NVDA)
8. **Before/after**: verification.csv Part B - SR error detection improved from 0% to 100%
9. **Re-pilot**: P5 (SR user) pilot notes - "Heard error announcement immediately, corrected and succeeded"

**Complete chain**: Data → Observation → Interpretation → Fix → Verification ✅

---

## Submission Checklist

Before submitting, verify:

**Files**:
- [ ] This completed template (submission-template.md)
- [ ] metrics.csv (or pasted into Section 3)
- [ ] Pilot notes (P1-notes.md, P2-notes.md, etc. OR summarised in Section 6)
- [ ] Screenshots folder (all images referenced above)
- [ ] Privacy statement signed (top of document)

**Evidence chains**:
- [ ] findings-table.csv links to metrics.csv lines AND/OR pilot notes timestamps
- [ ] implementation-diffs.md references findings from table
- [ ] verification.csv Part B shows before/after for fixes

**Quality**:
- [ ] No PII in screenshots (checked twice!)
- [ ] Session IDs anonymous (P1_xxxx format, not real names)
- [ ] Honest reporting (limitations acknowledged if metrics didn't improve)
- [ ] WCAG criteria cited specifically (e.g., "3.3.1" not just "accessibility")

**Pass criteria met**:
- [ ] n=2+ participants (metrics.csv has 2+ session IDs)
- [ ] 3+ findings with evidence (findings-table.csv complete)
- [ ] 1-3 fixes implemented (implementation-diffs.md shows code)
- [ ] Regression complete (verification.csv has 20 checks)
- [ ] Before/after shown (verification.csv Part B has data)

---

**Ready to submit?** Upload this file + evidence folder to Gradescope by end of Week 10.

**Estimated completion time**: 12-15 hours across Weeks 9-10

**Good luck!** 🎯
