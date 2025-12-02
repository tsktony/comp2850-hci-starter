# Pilot Data Analysis Summary — Week 10

**Study**: Week 9 peer pilots (n = 5)  
**Tasks**:  
- **T1_filter** — Filter the task list using keyword `"meet"`  
- **T2_edit** — Rename `"draft report"` → `"submit report"`  
- **T3_add** — Add a new task  
- **T4_delete** — Delete the `"tmp"` task  

Participant profiles (from Week 9):

- **P1_STD** — “Standard” student, reasonably confident, JS on  
- **P2_TYPO** — Very fast, tends to make typos / trigger validation, JS on  
- **P3_KB** — Keyboard-first user, prefers Tab/Enter over mouse, JS on  
- **P4_NOJS** — No-JS tester (DevTools JS disabled), understands what they are doing  
- **P5_READ** — Careful reader, reads text before acting, JS on  

---

## 1. Summary Statistics

### 1.1 Task Completion Times (Median ± MAD)

> Units: milliseconds (approximate seconds in brackets)  

#### T1_filter

| Task       | js_mode | n_success | median_ms (≈s) | mad_ms (≈s) | Notes |
|-----------|---------|----------:|----------------|------------|-------|
| T1_filter | on      | 4         | 25000 (~25 s)  | 5000 (~5 s) | Filtering is relatively quick with JS enabled |
| T1_filter | off     | 1         | 35000 (~35 s)  | 0          | No-JS requires Enter + full page reload, slightly slower |
| T1_filter | all     | 5         | 30000 (~30 s)  | 5000 (~5 s) | Overall median 30 s, moderate spread |

#### T2_edit

| Task       | js_mode | n_success | median_ms (≈s) | mad_ms (≈s) | Notes |
|-----------|---------|----------:|----------------|------------|-------|
| T2_edit   | on      | 4         | 37500 (~37.5 s) | 2500 (~2.5 s) | Edit flow takes noticeably longer |
| T2_edit   | off     | 1         | 45000 (~45 s)  | 0            | No-JS edit is slower due to page transitions |
| T2_edit   | all     | 5         | 40000 (~40 s)  | 5000 (~5 s)   | Longest median time among all four tasks |

#### T3_add

| Task       | js_mode | n_success | median_ms (≈s) | mad_ms (≈s) | Notes |
|-----------|---------|----------:|----------------|------------|-------|
| T3_add    | on      | 4         | 21000 (~21 s)  | 2500 (~2.5 s) | Add flow is smooth for JS-on participants |
| T3_add    | off     | 1         | 25000 (~25 s)  | 0            | No-JS slightly slower but still reasonable |
| T3_add    | all     | 5         | 22000 (~22 s)  | 3000 (~3 s)   | Stable performance overall |

#### T4_delete

| Task       | js_mode | n_success | median_ms (≈s) | mad_ms (≈s) | Notes |
|-----------|---------|----------:|----------------|------------|-------|
| T4_delete | on      | 4         | 16000 (~16 s)  | 5000 (~5 s) | Fastest task in JS-on mode |
| T4_delete | off     | 1         | 15000 (~15 s)  | 0           | No-JS delete is roughly the same speed |
| T4_delete | all     | 5         | 15000 (~15 s)  | 5000 (~5 s) | Overall the quickest of the four tasks |

---

### 1.2 Completion & Error Rates

#### T1_filter

| Task       | js_mode | n_success | n_attempts | completion_rate | error_rate | Notes |
|-----------|---------|----------:|-----------:|----------------:|-----------:|-------|
| T1_filter | on      | 4         | 4          | 1.00            | 0.00       | All JS-on participants completed successfully |
| T1_filter | off     | 1         | 1          | 1.00            | 0.00       | No-JS participant also completed successfully |
| T1_filter | all     | 5         | 5          | 1.00            | 0.00       | No failures or validation errors |

#### T2_edit

| Task       | js_mode | n_success | n_attempts | completion_rate | error_rate | Notes |
|-----------|---------|----------:|-----------:|----------------:|-----------:|-------|
| T2_edit   | on      | 4         | 5          | 0.80            | 0.20       | One JS-on attempt triggered a validation error |
| T2_edit   | off     | 1         | 1          | 1.00            | 0.00       | No-JS participant eventually completed |
| T2_edit   | all     | 5         | 6          | 0.83            | 0.17       | Only task with completion < 1.0 |

#### T3_add

| Task       | js_mode | n_success | n_attempts | completion_rate | error_rate | Notes |
|-----------|---------|----------:|-----------:|----------------:|-----------:|-------|
| T3_add    | on      | 4         | 4          | 1.00            | 0.00       | All JS-on add attempts succeeded |
| T3_add    | off     | 1         | 1          | 1.00            | 0.00       | No-JS add is also successful |
| T3_add    | all     | 5         | 5          | 1.00            | 0.00       | Best-performing task overall |

#### T4_delete

| Task       | js_mode | n_success | n_attempts | completion_rate | error_rate | Notes |
|-----------|---------|----------:|-----------:|----------------:|-----------:|-------|
| T4_delete | on      | 4         | 4          | 1.00            | 0.00       | All JS-on delete attempts succeeded |
| T4_delete | off     | 1         | 1          | 1.00            | 0.00       | No-JS delete is also successful |
| T4_delete | all     | 5         | 5          | 1.00            | 0.00       | No failures, no validation errors |

---

## 2. Task-by-Task Interpretation (with Inclusion Lens)

### 2.1 T1_filter — Filter task list

- **Efficiency**: Overall median time is ~30 s; JS-on median is 25 s, no-JS is 35 s. The no-JS path is slower, mainly because the user has to press Enter and wait for a full page reload, but still within a reasonable range.
- **Stability**: MAD ≈ 5 s, so differences between participants are not huge. The keyboard user (P3) and careful reader (P5) are slightly slower, which matches their interaction style.
- **Effectiveness**: Completion rate is 100% with 0% error rate. This suggests the filter control is discoverable and understandable.
- **Inclusion**: Both JS-on and JS-off participants can complete the task, so functional parity is in place. If time allows later, this is a good candidate for extra checks with screen readers (e.g., announcing “Found X tasks.” in the live region).

### 2.2 T2_edit — Edit task title (current weak spot)

- **Efficiency**: T2 has the highest median time (~40 s) of all tasks. No-JS participant needs ~45 s, which is clearly slower than add/delete.
- **Effectiveness**: JS-on completion rate is 0.80 (4/5), and “all” is 0.83 (5/6). One attempt triggered a validation error when the user cleared the title.
- **Error rate**: Overall error rate is 0.17, which is noticeably higher than the other tasks (all 0). This flags the edit flow as less forgiving.
- **Qualitative link**:
  - P2 (Typo user) cleared the title and only then realised the form does not allow blank titles. They understood the error message content, but still hit the error once.
  - P3 (keyboard user) completed the edit entirely via keyboard. Time was slightly longer but still acceptable.
- **Inclusion impact**:
  - With this small sample, both keyboard and no-JS users can eventually complete the task, but T2 clearly has higher cognitive and interaction load than the others.
  - If future testing includes screen readers, T2 is likely to surface accessibility issues first (e.g. how validation errors are announced and how focus is managed). It should therefore be treated as a Priority-1 area for inclusive redesign.

### 2.3 T3_add — Add new task

- **Efficiency**: Overall median is ~22 s. JS-on is slightly faster (~21 s) and no-JS is ~25 s. The gap is small and mainly reflects full page reloads.
- **Completion & errors**: Completion rate is 100% with 0% error rate. The hypothesised “blank title” error did not occur in these pilots.
- **Experience**:
  - P1 and P2 both completed the add flow in one go, which suggests the form structure and button labels are clear.
  - P3 (keyboard user) can submit via input + Enter, which matches common expectations and supports power users.
- **Inclusion**: Keyboard and no-JS paths both work well. At this stage, add-task behaviour already meets basic inclusion expectations and does not need major rework.

### 2.4 T4_delete — Delete task

- **Efficiency**: Overall median is ~15 s, making this the lightest task. JS-on and no-JS timings are almost identical.
- **Completion**: 100% completion, 0% error rate.
- **Experience differences**:
  - JS-on path shows an HTMX confirmation dialog plus list update, giving clear feedback.
  - No-JS path uses classic POST-Redirect-GET. In the pilots, P4 (no-JS) hesitated and asked “Is it really gone?” which hints that feedback could be more explicit.
- **Inclusion**:
  - Keyboard users can reach the delete controls, though the button is at the far right of each row, so it takes several Tab presses to reach. In this small sample, this did not cause failures.
  - Overall, delete is fast and successful for all participants; only minor polish is needed for no-JS feedback and keyboard tabbing effort.

---

## 3. Overall Conclusions & Next-Step Focus

1. **General usability**  
   - Across all four tasks, completion rates are between 0.83 and 1.00. T1, T3 and T4 all reach 100% completion with 0% error, which indicates core flows are usable.
   - No-JS mode is consistently slower than JS-on, but all tasks remain completable. This meets the basic goal of functional parity between JS-on and JS-off.

2. **Main weak point: T2 (Edit task)**  
   - T2 has the **longest median time** (~40 s), lower completion rate (0.83 overall), and the **highest error rate** (0.17).  
   - Combined with pilot notes, this suggests the edit flow still needs work around error handling, the clarity of the edit state, and the discoverability / emphasis of Save and Cancel buttons.
   - In Week 10 prioritisation, issues around **T2’s validation and accessibility** should be treated as **Priority 1**.

3. **Inclusion lens**  
   - Keyboard (P3) and no-JS (P4) participants can complete all tasks, but they are noticeably slower on T1 and T2. This shows that more complex flows become less friendly for non-typical usage patterns.
   - Current pilot set does not yet include actual screen-reader users. If more time is available, T2’s validation messages and focus behaviour are the first place to run deeper accessibility checks (`role="alert"`, `aria-describedby`, etc.).

4. **Preparing for Week 10 Lab 2**  
   - Based on these numbers, the backlog item “T2 edit validation / accessibility” can be scored highly in `(Impact + Inclusion) – Effort` and selected as a **candidate fix** for Week 10 Lab 2.
   - Other issues (e.g. stronger feedback for no-JS delete, clearer result count announcement for filter) can be treated as medium-priority and tackled if time allows, but do not block basic inclusive use.

---
