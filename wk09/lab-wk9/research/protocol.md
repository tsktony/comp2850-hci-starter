# Peer Pilot Protocol — Week 9 (No PII)

## 1. Purpose and Scope

This is a **low-risk peer pilot** for COMP2850 Week 9.

- Goal: check how usable the **task list prototype** is for three core flows:
  - T1_filter – filter tasks by a keyword
  - T2_edit – rename an existing task
  - T3_delete – delete an existing task
- No audio / video recording.
- No names, emails, student IDs or other personally identifiable information (PII) are collected.

---

## 2. Participants and Ethics

- Participants: classmates from COMP2850 (peer-to-peer testing).
- Before starting, I will give a short verbal explanation:

  > “This is a short usability test, about 15 minutes.  
  > I’m testing the interface, not you.  
  > I will note whether you can complete each task, roughly how long it takes,  
  > and how confident you feel afterwards.  
  > I will **not** record your name, email, or student ID.  
  > You can stop at any time and it will not affect your grade.”

- In my notes I only use simple **participant codes** like `P1`, `P2`, etc.
- If someone later asks to have their data removed, I will delete all notes linked to that code.

---

## 3. Environment and Setup

- Environment: lab PC or laptop with a browser open on my COMP2850 task list prototype.
- Start page: `/tasks`, with a small set of pre-seeded example tasks.
- JavaScript:
  - Most of the session runs with **JS enabled** (HTMX enhancement).
  - At the end I repeat **one task with JS disabled** to check the no-JS path.

I also keep a simple note file open (e.g. `pilot-notes.md`) where I track:

- Participant code (P1, P2, …)
- Which tasks they attempted
- Completion, confidence ratings, and a few short observations

---

## 4. Task Order and Timing

Total time per participant: roughly **15–20 minutes**.

**Order:**

1. Warm-up (not timed)
2. **T1_filter** — filter by “meet”
3. UMUX-Lite Q1 (capabilities / meets needs)
4. **T2_edit** — rename “draft report” → “submit report”
5. UMUX-Lite Q2 (ease of use)
6. **T3_delete** — delete task called “tmp”
7. Repeat **one task with JS off** (usually T1_filter or T3_delete)
8. Short debrief

**Time cap:** about **3 minutes per task**.  
If someone is stuck for more than 3 minutes, I will offer to move on and mark that task as not completed.

---

## 5. What I Say and Do in Each Phase

### 5.1 Warm-up (not timed)

I say something like:

> “First, just have a quick look around the page.  
> Scroll, click things if you want, and get a feel for it.  
> When you’re ready, tell me and we’ll start the actual tasks.”

No metrics are recorded here; it is just to help them relax and get familiar with the layout.

---

### 5.2 Main Tasks (T1 / T2 / T3)

For each task:

1. I read the **scenario** from `tasks.md` (T1_filter, T2_edit, T3_delete).
2. I ask them to tell me when they feel they are finished (e.g. “done” / “finished”).
3. I watch what they do, but I **avoid helping** unless they are really stuck.

After each task I ask:

> “On a scale from 1 to 5, how confident are you that you completed that task correctly?  
> 1 = not confident at all, 5 = very confident.”

UMUX-Lite questions:

- After **T1_filter**, I ask **UMUX-Lite Q1** about system capabilities / meeting needs.
- After **T2_edit**, I ask **UMUX-Lite Q2** about ease of use.

In my notes I write, for example:

- `P1 – T1_filter – confidence = 4`
- `P1 – UMUX Q1 = 5`
- `P1 – T2_edit – confidence = 3`
- `P1 – UMUX Q2 = 4`
- `P1 – T3_delete – confidence = 5`

---

### 5.3 One Task with JS Disabled

Near the end, I turn **JavaScript off** in the browser settings and reload `/tasks`.  
Then I repeat **one of the tasks**, usually:

- **T1_filter** (filter by “meet”), or  
- **T3_delete** (delete “tmp”).

I check:

- Can they still complete the task without JS?
- Does the behaviour match the JS-on version (same result, but maybe slower)?
- Are any error messages or confirmations missing in the no-JS path?

I note whether this no-JS attempt was successful and how it felt compared to JS-on
(e.g. “slower because of full page reloads”, “less obvious feedback”, etc.).

---

### 5.4 Debrief

At the end I ask a few short questions, for example:

1. “Which task felt the most difficult? Why?”
2. “Was there any moment where you weren’t sure if something had actually worked?”
3. “If you could change one thing about this interface first, what would it be?”

I note a few keywords or direct quotes with the participant code.

---

## 6. Prompting Rules (Avoid Leading the Participant)

- I **do not** explain where buttons are or how features work before they try the task.
- During the task I **do not** say things like “try clicking this button”.
- If they ask “Is this the right way?”, I answer:

  > “Use it as you normally would use a website.  
  > I’m interested in how you naturally try to solve it.”

- If they are stuck for more than ~3 minutes, I ask:

  > “Would you like to keep trying, or shall we move on to the next task?”

If we move on, I mark that task as “not completed” and briefly note the reason.

---

## 7. What Data I Actually Record

For each participant code (P1, P2, …) I record:

- **Task completion**:  
  - `1` = completed  
  - `0` = not completed  
  For T1_filter, T2_edit, T3_delete, and the one repeated no-JS task.
- Rough **time-on-task** (if I’m timing): from when they start trying to when they say “done”.
- Obvious **errors / validation problems**:
  - e.g. submitting a blank field, editing the wrong task, deleting the wrong item.
- **Confidence rating** (1–5) after each task.
- UMUX-Lite Q1 and Q2 scores (1–7).
- A few **short qualitative observations**, for example:
  - “Hesitated several seconds before deleting”
  - “Did not notice the status message at the top the first time”

All notes stay in my private repo / files and only use participant codes, never real identities.

---

## 8. Accessibility-Specific Notes

If a participant:

- Uses **keyboard only**, I note:
  - Whether they can complete each task,
  - Any issues with tab order, focus getting lost, or unreachable controls.
- Uses a **screen reader**, I note:
  - Whether status messages and counts are announced correctly,
  - Any unclear labels or missing announcements.

Later (Week 10), I will add these to the backlog as higher-priority items if they look like accessibility barriers.

---