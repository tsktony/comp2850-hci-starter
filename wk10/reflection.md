# Week 10 – Reflection

## 1. Data interpretation – what surprised me?

The numbers for T1, T3, and T4 were mostly in line with what I expected: 100% completion and no validation errors. The main surprise was that **T2_edit is clearly slower and slightly more fragile than the others**, even though the interaction looks simple on paper. The JS-on median for T2 is around 37.5s, which is noticeably higher than T3_add (~21–22s), and overall completion for T2 is only about 5/6 (≈83%). This matches my notes that people hesitated when they were in the inline edit state and took extra time to find or trust the Save action.

Another small surprise was that the **no-JS times are consistently slower but still “usable”**. For example, T1_filter and T3_add with JS-off are slower than JS-on, but participants still finished successfully without complaining too much. This makes me more confident that the baseline server-first design is working, even if the enhanced path feels smoother.

---

## 2. Inclusion lens – how it changed my priorities

If I only looked at “where is it slow / where are there errors?”, I probably would have focused on general UX polish, like “make everything a bit faster” or “reduce small confusions in the filter.” Adding an explicit **Inclusion** dimension forced me to ask “who is actually being slowed down or excluded?”.

With that lens, the most important issues are the ones that **disproportionately hurt keyboard-first users or future screen reader users**, even if they only showed up in one task. That is why the edit flow (T2) became my Priority 1 target: it is a core task, and the current design already shows extra friction for keyboard users and more cautious users. The deletion flow, on the other hand, looks a bit inconsistent in no-JS mode but does not block anyone, so it can be safely deprioritised for now.

---

## 3. Trade-offs – what I chose to deprioritise

The main things I am explicitly deprioritising for this iteration are:

- Adding a separate **no-JS delete confirmation page**  
- Reworking the filter behaviour (for example, adding an explicit “Apply” button instead of auto-search)

Both of these have some UX value, but compared with the T2_edit issues they have lower impact and lower inclusion risk. No-JS delete without a confirm dialog is slightly scary, but people can still recover by re-adding a task. The filter auto-update caused a bit of confusion for one or two people, but it did not stop anyone from completing T1.

Given limited time in Week 10, I am more comfortable focusing on the **edit flow accessibility and clarity** first, and treating these other points as “nice-to-fix later” items that can go into a longer-term backlog.

---

## 4. Redesign confidence – how sure am I it will help?

I am reasonably confident that the planned changes for T2_edit will move the metrics in the right direction:

- Making the **Save** action visually clearer should reduce hesitation and lower the median time.
- Stronger validation feedback (field-level error text + `aria-invalid` + `aria-describedby`) should reduce repeat errors.
- More consistent status messages should increase confidence scores, because users can see immediately that their change was saved.

What I am less sure about is **how big** the improvement will be with only a small number of pilots in Week 10. With n=3–4 retest participants, the numbers may still be quite noisy. So I treat the new targets (e.g., T2_edit median ≤ 30s, error_rate ≤ 0.10, completion ≥ 90%) as something to aim for, but I also plan to explain the small sample size in the assessment if the results are close but not perfect.

---

## 5. Evidence chains – are they actually joined up?

For the T2_edit problem, I can now trace a fairly clear evidence chain:

- **Raw data**: `data/metrics.csv` and `analysis/analysis.csv` show T2 with higher time and lower completion than T1/T3/T4.
- **Analysis summary**: `wk10/analysis/summary.md` explains that T2 is slower, with more hesitation and at least one validation error.
- **Prioritisation**: `analysis/prioritisation.csv` gives T2-related issues a high score, because they affect core functionality and have inclusion impact.
- **Redesign brief**: `wk10/lab-wk10/docs/redesign-brief.md` turns that into a concrete change plan with goals and acceptance criteria.

The only thing that still needs attention is making sure I explicitly link **pilot notes (qualitative quotes)** into the chain as supporting evidence, not just the numeric metrics. I will reference specific participants in the final assessment pack (e.g. “P2 hesitated to find Save”, “P3 took longer using keyboard only”) so the chain does not look purely statistical.

---

## 6. Week 10 Lab 2 readiness – what could go wrong?

The main risks for Week 10 Lab 2 are:

- **Implementation taking longer than expected**: even small template changes can be fiddly. To reduce the risk, I have kept the redesign tightly scoped to the edit flow and reused the existing status region pattern instead of adding a new component.
- **Breaking the no-JS path by accident**: it is easy to test only the HTMX version and forget the redirect version. I will deliberately re-run the T2 scenario with JS disabled after every change.
- **Too few verification participants or very noisy timings**: if I only manage to retest with 1–2 people, the numbers may not move very clearly. In that case, I will still report the qualitative changes (e.g. fewer questions, less hesitation) and be explicit about the small n in the reflection.

Overall, I feel ready for Week 10 Lab 2: the redesign brief is focused, the metrics are defined, and I have a clear idea of how to check that the edit flow is genuinely better for both “standard” users and more inclusive use cases like keyboard-first access.