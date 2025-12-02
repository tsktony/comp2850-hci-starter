# Before/After Summary — Task T2 (Edit Task)

## 1. Problem Addressed

In the Week 9 pilots, Task T2 (inline edit) was **functionally successful** (all participants eventually completed the edit), but the **experience was slow and hesitant**:

- Success times for T2_edit were between **25 000–45 000 ms**, with a **median of 40 000 ms** and **MAD ≈ 5 000 ms**.
- One participant triggered a blank-title validation error and needed extra time to recover.
- Qualitative notes (and my own feeling when testing) suggested that people were **not fully confident** that their changes had been saved and spent time double-checking the list.

The redesign for Week 10 focused on:

- Making the **Save path clearer and more direct**.
- Strengthening **confirmation and error feedback**, for both HTMX and no-JS paths.
- Getting ready for better **assistive tech support** (screen readers, keyboard-only).

---

## 2. Quantitative Before/After Comparison (T2_edit)

Using `metrics.csv` (T2_edit rows only):

- **Before redesign (Week 9 pilots)**  
  Participants: P1_STD, P2_TYPO, P3_KB, P4_NOJS, P5_READ  
  Relevant rows: `r002, r006, r007, r011, r015, r019`

- **After redesign (Week 10 verification pilots)**  
  Participants: P6_POST, P7_POST, P8_POST  
  Relevant rows: `r102, r106, r107, r111`

### 2.1 Participant-level completion

At participant level we count a task as completed if the participant eventually performs a successful `T2_edit` (validation errors on the way are counted separately in the error rate).

| Metric                              | Before (Week 9)            | After (Week 10)           | Interpretation                                  |
|-------------------------------------|----------------------------|---------------------------|-------------------------------------------------|
| Participants with successful T2     | 5/5 (100%)                 | 3/3 (100%)                | All participants eventually completed the task. |
| Participants with **any** error     | 1/5 (20%)                  | 1/3 (~33%)                | One “typo” user before, one deliberate error after. |
| JS-on participants (success)        | 5/5 (100%)                 | 2/2 (100%)                | JS-on path always allowed completion.           |
| JS-off participants (success)       | 1/1 (100%)                 | 1/1 (100%)                | No-JS path worked before and after.             |

> In the **after** pilots, the P7_POST validation error was **intentional** to test the new error handling, so the higher “any error” percentage does **not** indicate a real regression.

### 2.2 Time-on-task (success attempts only)

Using the `ms` field for rows where `step=success` and `task_code=T2_edit`:

#### All participants (JS-on + JS-off)

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
- MAD also dropped (from 5 000 ms to 500 ms), meaning **experiences became more consistent**.
- This matches observations: with a clearer Save affordance and stronger feedback, participants spent less time hesitating or double-checking.

#### JS-on vs JS-off

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

- For **JS-on**, the typical edit time shrank from ~37.5 s to ~2.7 s. This is a large performance gain and suggests that the inline edit flow is now much more direct and less confusing.
- For **JS-off**, the single no-JS run went from **45 s** to **3.5 s**. Together with the qualitative impression, this supports the claim that the no-JS flow is clearer and requires less “scrolling and checking”.

### 2.3 Validation error rate

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

- Raw error rate increased slightly (17% → 25%), but this is **driven by the deliberate test** of the new error handling in Week 10.
- Importantly, the Week 10 validation error was recovered from **very quickly** (~200 ms) and the participant remained confident afterwards, which suggests that the new error message was clear and easy to act on.
- In normal usage (without deliberately forcing a blank submission), we would expect the effective error rate to be **lower** after the redesign, because:
  - The error message is now more visible and descriptive.
  - The input field is structurally linked to the error text (ready for `aria-invalid` / `aria-describedby`).

---

## 3. Interpretation for Inclusion & Accessibility

From an inclusion and accessibility perspective, the key improvements for T2_edit are:

- **Reduced cognitive load and stress**  
  The very large drop in time-on-task (from ~40 s to ~3–4 s) means that participants no longer have to “fight” the interface or spend a long time checking whether their edit worked. This is especially helpful for:
  - Students under time pressure,
  - People who are easily stressed by unclear status feedback,
  - Users who prefer keyboard and want quick, predictable flows.

- **Better support for no-JS and keyboard-first users**  
  The no-JS run for T2_edit now completes in about 3.5 seconds (vs 45 seconds before). The behaviour and feedback are much closer to the HTMX path, which supports:
  - Keyboard-only users,
  - Users in restricted environments (JS disabled, slow devices),
  - People who need predictable POST–Redirect–GET flows.

- **Improved foundation for accessible error handling**  
  Even though this small re-pilot did not include a dedicated screen-reader user, the redesigned flow is now structured so that:
  - Validation feedback is clearer and located near the field,
  - The status region and error messages can be read by assistive technologies (e.g. via `role="alert"` / status live regions),
  - Both HTMX and no-JS paths share the same message wording, which is important for WCAG **3.3.1 Error Identification** and **4.1.3 Status Messages**.

Overall, the **main measurable win** for T2_edit is **time-on-task** and **clarity of error recovery**, rather than raw “did they eventually finish”. All participants already completed the task before and after, but **after the redesign they did so much faster and with more obvious feedback**, which is especially important for cautious users and for people who rely on assistive technologies or keyboard-only workflows.

---

## 4. Check Against Redesign Goals

From the Week 10 redesign brief for T2_edit, the main targets were:

- **Median time (JS-on) ≤ 30 000 ms**  
  - Result: ≈ **2 725 ms** → ✅ **Met**

- **Overall completion rate (all modes) ≥ 90%**  
  - Result: 100% before and after → ✅ **Met** (but now with much better UX)

- **Error rate ≤ 0.10**  
  - Result: ≈ 0.25 in this tiny post-fix sample, but the one error was **intentional** for testing.  
    In realistic usage we would expect ≤ 0.10, but strictly from the numbers: ⚠️ **Partially met / needs more data**.

- **Mean confidence rating ≥ 4/5**  
  - I did not log numerical confidence ratings in `metrics.csv`, but observationally participants looked more relaxed and did not need to re-check the list as much. This is a **qualitative improvement**, but I cannot claim a precise numeric value → ⚠️ **Reported qualitatively only**.

In summary, the redesign clearly achieved the **performance** and **parity** goals (time-on-task and completion), and put in place a stronger structure for **accessible feedback**, while the error-rate and confidence goals would need a slightly larger sample and more formal confidence logging to be demonstrated quantitatively.