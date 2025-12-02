# Before/After Metrics — Task T2 (Edit Task)

## 1. Data Used

- **Before redesign (Week 9 pilots)**  
  Participants: P1_STD, P2_TYPO, P3_KB, P4_NOJS, P5_READ  
  Relevant rows in `metrics.csv`:  
  `r002, r006, r007, r011, r015, r019`

- **After redesign (Week 10 verification pilots)**  
  Participants: P6_POST, P7_POST, P8_POST  
  Relevant rows in `metrics.csv`:  
  `r102, r106, r107, r111`

We focus on **T2_edit** and compare:

- Completion (per participant)
- Validation error rate
- Time-on-task (median + MAD)
- JS-on vs JS-off

---

## 2. Summary Table (Participant-level Completion)

At participant level we count a task as completed if the participant eventually performs a successful `T2_edit` (validation errors on the way are counted separately in the error rate).

| Metric                              | Before (Week 9)            | After (Week 10)           | Interpretation                                  |
|-------------------------------------|----------------------------|---------------------------|-------------------------------------------------|
| Participants with successful T2     | 5/5 (100%)                 | 3/3 (100%)                | All participants eventually completed the task. |
| Participants with **any** error     | 1/5 (20%)                  | 1/3 (~33%)                | One “typo” user before, one deliberate error after. |
| JS-on participants (success)        | 5/5 (100%)                 | 2/2 (100%)                | JS-on path always allowed completion.           |
| JS-off participants (success)       | 1/1 (100%)                 | 1/1 (100%)                | No-JS path works both before and after, but feedback changed. |

> Note: In the **after** pilots, the P7_POST validation error was **intentional** to test the new error handling. So the higher “participants with any error” percentage does not indicate a regression.

---

## 3. Time-on-Task (Success Attempts Only)

Using the `ms` field for rows where `step=success` and `task_code=T2_edit`:

### 3.1 All participants (JS-on + JS-off)

- **Before** (P1, P2, P3, P4, P5)

  Success times: 25 000, 40 000, 35 000, 45 000, 40 000 ms  
  - Median = **40 000 ms**  
  - MAD (Median Absolute Deviation) = **5 000 ms**

- **After** (P6, P7, P8)

  Success times: 1 450, 4 000, 3 500 ms  
  - Median = **3 500 ms**  
  - MAD = **500 ms**

**Interpretation**:

- Median time for T2_edit dropped from about **40 seconds** to about **3.5 seconds**.
- MAD also dropped (from 5 000ms to 500ms), meaning **experiences became more consistent**.
- This matches observations: with clearer Save affordance and stronger feedback, participants spent less time hesitating or double-checking.

---

### 3.2 JS-on vs JS-off

**JS-on only** (HTMX, `js_mode=on`):

- Before (P1, P2, P3, P5): 25 000, 40 000, 35 000, 40 000 ms  
  - Median ≈ **37 500 ms**  
  - MAD ≈ **2 500 ms**

- After (P6, P7): 1 450, 4 000 ms  
  - Median ≈ **2 725 ms**  
  - MAD ≈ **1 275 ms**

**JS-off only** (no-JS, `js_mode=off`):

- Before (P4): 45 000 ms  
  - Only one success; no variability.

- After (P8): 3 500 ms  
  - Only one success; no variability.

**Interpretation**:

- For **JS-on**, the typical edit time shrank from ~37.5s to ~2.7s. This is a large performance gain and suggests that the inline edit flow is now much more direct and less confusing.
- For **JS-off**, our sample is small, but the single no-JS run went from **45s** to **3.5s**. Combined with qualitative notes, this supports the claim that the no-JS flow is clearer and requires less “scrolling and checking”.

---

## 4. Error Rate (Validation Errors)

Here we look at rows where `step="validation_error"` and `task_code="T2_edit"`.

- **Before**:
  - 1 validation error (P2_TYPO, `blank_title`)
  - 5 success rows
  - Error rate (per attempt) = **1 / (5 + 1) ≈ 17%**

- **After**:
  - 1 validation error (P7_POST, `blank_title`) — **intentional test**
  - 3 success rows
  - Error rate (per attempt) = **1 / (3 + 1) = 25%**

**Interpretation**:

- Raw error rate increased slightly (17% → 25%), but this is **driven by the deliberate test of the new error handling** in Week 10.
- Importantly, the Week 10 validation error was recovered from **very quickly** (200ms) and the participant rated their confidence as high afterwards, indicating that the new error message was clear and easy to act on.
- In real usage (without deliberately forcing a blank submission), we expect the effective error rate to be **lower** after the redesign, because:
  - The error message is now more visible and descriptive.
  - The input field is marked with `aria-invalid` and linked to the error text.

---

## 5. Inclusion & Accessibility Interpretation

From an inclusion perspective, the key improvements are:

- **Faster and more consistent experience** for both mouse and keyboard users:
  - Median time dropped dramatically for T2_edit in both JS-on and JS-off modes.
  - Lower MAD after the redesign means fewer “outlier” struggles.

- **Better support for no-JS and keyboard-first users**:
  - No-JS participant (P8_POST) could complete the edit in ~3.5 seconds with clear feedback.
  - The same error behaviour (blank title → explanation) is now available in both HTMX and no-JS paths.

- **Accessible error handling** (by design, even if we did not run a full screen reader test in this small verification):
  - Error messages are clearer and placed near the field.
  - The structure is ready for screen readers (`role="alert"`, `aria-invalid`, `aria-describedby`), reducing barriers for future SR users.

Overall, the **main measurable win** for T2_edit is **time-on-task** and **clarity of error recovery**, rather than raw “did they eventually finish”. All participants still completed the task before and after, but **after the redesign they did so much faster and with more obvious feedback**, which is especially important for cautious users and for people relying on assistive technologies.