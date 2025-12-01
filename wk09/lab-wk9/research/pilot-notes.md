# Pilot Data — Week 9

## Participant P1 — “Standard, fluent user”
**Session ID**: sid=SIM_P1_STD  
**Mode**: JS on (HTMX)  
**Profile**: Typical student who understands the interface, operates smoothly, with only slight hesitation.

### T1_filter ("meet")
- Time: 18s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Immediately noticed the search box and used it. When the list updated, nodded and said “OK”.

### T2_edit
- Time: 25s
- Success: 1
- Errors: 0
- Confidence: 4/5
- Notes: After clicking **Edit**, needed a moment to locate the **Save** button (button styling may not be prominent enough). Paused ~2 seconds, then saved.

### T3_add ("New Task")
- Time: 15s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Very straightforward, no visible issues.

### T4_delete
- Time: 10s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Saw the confirmation dialog and confirmed quickly.

---

## Participant P2 — “Fast but typo-prone user”
**Session ID**: sid=SIM_P2_TYPO  
**Mode**: JS on (HTMX)  
**Profile**: Works quickly but sometimes makes mistakes and triggers validation, then corrects them.

### T1_filter
- Time: 20s
- Success: 1
- Errors: 0
- Confidence: 4/5
- Notes: Typed the wrong keyword at first, saw no results, then deleted and re-typed correctly.

### T2_edit
- Time: 40s
- Success: 1
- Errors: 1 (validation)
- Confidence: 3/5
- Notes: Tried to clear the title completely (accidental full delete). On save, saw the red warning “Title cannot be empty”. Said: “Ah, I can’t leave it blank?”, then re-entered the title and saved.
- Interpretation: This tests whether the error message is clear and how quickly users can recover.

### T3_add
- Time: 22s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Normal, no issues.

### T4_delete
- Time: 12s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Normal.

---

## Participant P3 — “Keyboard-only user”
**Session ID**: sid=SIM_P3_KB  
**Mode**: JS on (HTMX)  
**Profile**: Programmer-type user who prefers keyboard, uses Tab to move focus and avoids the mouse.

### T1_filter
- Time: 30s
- Success: 1
- Errors: 0
- Confidence: 4/5
- Notes: Needed several Tab presses to reach the search box.

### T2_edit
- Time: 35s
- Success: 1
- Errors: 0
- Confidence: 4/5
- Notes: Completed entirely with keyboard. Discovered that pressing **Enter** after editing saves the task and commented “Nice, this shortcut is convenient.”

### T3_add
- Time: 20s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Normal.

### T4_delete
- Time: 25s
- Success: 1
- Errors: 0
- Confidence: 4/5
- Notes: This was the slowest step because the **Delete** button is at the end of each row; needed several Tab presses to reach it.

---

## Participant P4 — “No-JS environment tester”
**Session ID**: sid=SIM_P4_NOJS  
**Mode**: JS off (No-JS)  
**Profile**: Asked to disable JavaScript to test the baseline server-first path. Knows what they are doing, but page reloads interrupt their rhythm.

### T1_filter
- Time: 35s
- Success: 1
- Errors: 0
- Confidence: 4/5
- Notes: After typing the keyword, waited briefly expecting auto-update. When nothing changed, pressed Enter and saw a full page reload.
- Interpretation: Typical behaviour of users used to “instant search” UIs.

### T2_edit
- Time: 45s
- Success: 1
- Errors: 0
- Confidence: 4/5
- Notes: Flow was Edit → full page reload → change title → Save → full page reload. Commented: “It’s a bit slow, but still usable.”

### T3_add
- Time: 25s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Classic POST form and redirect; very familiar pattern.

### T4_delete
- Time: 15s
- Success: 1
- Errors: 0
- Confidence: 3/5
- Notes: Clicked Delete → page reloaded and the task disappeared. Without the JS confirm dialog, asked: “Is that it? It’s just gone?”

---

## Participant P5 — “Careful, reading-oriented user”
**Session ID**: sid=SIM_P5_READ  
**Mode**: JS on (HTMX)  
**Profile**: Very careful; reads most of the text before acting. Slower but makes few mistakes.

### T1_filter
- Time: 30s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Typed slowly and double-checked the result count.

### T2_edit
- Time: 40s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Read the original title before editing, then checked again after saving to confirm the change.

### T3_add
- Time: 25s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: Normal.

### T4_delete
- Time: 20s
- Success: 1
- Errors: 0
- Confidence: 5/5
- Notes: When the confirm dialog appeared, carefully read the text “Delete task X?” before clicking confirm.