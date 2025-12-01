# Evidence Chains — Week 9

This document links pilot data to findings, backlog items, and redesign plans.
Each chain shows how real user evidence leads directly to design decisions for Week 10.

---

## Chain 1 – No-JS Delete Operation Feels “Uncertain”

### Raw Evidence
- **P4 (No-JS mode)**, Task T4_delete  
  - Time = 15 s, Success = 1, Confidence = 3/5  
  - Comment: “I clicked delete, the page refreshed and it was gone… not sure if that was right.”

- **Other participants (JS on)**  
  - Confidence ≈ 5/5  
  - Comments: “Got a pop-up, clicked OK.”  “Read ‘Delete task X?’ then confirmed.”

### Finding
Both JS-on and JS-off paths delete tasks correctly, but only the HTMX path gives visible confirmation.
Participants without JS felt **unsure whether deletion succeeded**.

### Risk
Low trust in destructive actions may cause hesitation or repeated checking.

### Backlog Reference
- **ID 2 – No clear confirmation after add/edit/delete**  
  - Severity: High Type: Accessibility / UX  
  - WCAG 3.3.1 Error Identification, 3.3.4 Error Prevention  
  - Candidate fix ✅

### Planned Week 10 Changes
- Add explicit confirmation page or success message for No-JS deletes.  
- Ensure status message appears in the live-region and is keyboard-focusable.

---

## Chain 2 – Edit / Save / Cancel Flow Works but Feels Clumsy

### Raw Evidence
- **P1 (T2_edit)**  25 s, Success 1, Confidence 4/5 — paused searching for Save button.  
- **P2 (T2_edit)**  40 s, 1 validation error (blank title) — said “oh, can’t empty it?” then recovered.  
- **P3 (Keyboard-only)**  35 s — used Enter key to save successfully.  
- **P5 (Careful reader)**  40 s, Confidence 5 — double-checked before saving.

### Finding
Editing is fully functional but **Save and Cancel are not visually or semantically clear**.
Users pause or mis-interpret “Cancel”.

### Risk
Minor friction adds cognitive load; Cancel ambiguity could cause user anxiety.

### Backlog Reference
- **ID 12 – Edit / Save / Cancel flow unclear**  
  - Severity: Medium Type: Usability / Interaction  
  - WCAG 3.2.4 Consistent Identification  
  - Candidate fix ✅

### Planned Week 10 Changes
- Make **Save** the primary button with clear label (“Save changes”).  
- Change **Cancel** to “Discard changes”.  
- Add status message “Task updated” in live region after save.

---

## Chain 3 – Keyboard Access Good but Delete Takes Longer

### Raw Evidence
- **P3 (Keyboard-only)**  
  - T1 = 30 s, T2 = 35 s, T3 = 20 s, T4 = 25 s; all success, no errors.  
  - Comment: “Tabbing to Delete takes a while.”

### Finding
All tasks are reachable by keyboard—no traps—but **Delete is inefficient** due to long tab sequence.

### Risk
Usable yet slower for keyboard-only users; scales poorly with larger lists.

### Backlog Reference
- **ID 3 – Keyboard focus and controls may skip important buttons**  
  - Severity: High Type: Accessibility  
  - WCAG 2.1.1 Keyboard Access  
  - Candidate fix ✅

### Planned Week 10 Changes
- Review tab order within task rows.  
- Optionally re-group Edit/Delete buttons to reduce tab steps.  
- After delete, move focus to next task or status message.

---

## Chain 4 – Add Task Flow Is a Clear Strength

### Raw Evidence
- **All participants (P1–P5)** Task T3_add  
  - Time ≈ 15–25 s, Success 100%, Confidence 5/5, Errors 0.  
  - Comments: “Smooth.”  “Instant feedback.”  “Exactly what I expected.”

### Finding
Adding tasks is fast, error-free and confident across both JS modes — a strong design area.

### Risk
Future feature creep could damage this simplicity.

### Backlog Reference
- **ID 4 – Maintain Add Task clarity (strength item)**  
  - Severity: Info Type: Strength  
  - Candidate fix ❌ (keep as reference)

### Planned Week 10 Action
- Keep core Add Task interaction unchanged.  
- When adding new fields, place them secondary to the main title input.  
- Use current form as a model for future flows (e.g., filters or bulk add).

---

## Summary

From the five pilot sessions, four major insights guide redesign:

1. **Improve No-JS deletion feedback** — add explicit confirmation message.  
2. **Clarify Edit/Save/Cancel interaction** — clearer labels and status messages.  
3. **Enhance keyboard efficiency** — especially Delete button position.  
4. **Preserve Add Task strength** — fast, confident, error-free interaction.

Together these chains form the complete evidence trail:
**Raw pilot data → Findings → Backlog items → Week 10 redesign plan.**