# Evidence (Week 7)
# Testing Notes — Week 7 Lab 1

## Environment

- Date: 2025-11-27
- OS: Windows 10
- Browser: Microsoft Edge (latest stable)
- JavaScript: Enabled (HTMX path) / Disabled (no-JS path)
- Screen reader: NVDA 2024 (Windows)
- App: COMP2850 Task Manager (`/tasks`)

---

## 1. HTMX path (JavaScript ON)

### 1.1 Inline edit activation

- Action:  
  - Opened `/tasks` with JavaScript enabled.  
  - Added a demo task, e.g. **"12313"**.  
  - Pressed **Tab** to move focus to the **Edit** button for the first task.  
  - Pressed **Enter** on **Edit**.
- Expected:  
  - The task’s `<li>` is replaced by an inline edit form (no full page reload).
- Result:  
  - ✅ The row switched to **edit mode** in place (input + Save + Cancel).  
  - ✅ Network panel shows **GET `/tasks/{id}/edit`** as an XHR/HTMX request.  
  - ✅ Focus moved into the title input so I could start typing immediately.

### 1.2 Successful save (HTMX)

- Action:  
  - In the inline form, changed the title to **"Updated title (HTMX)"**.  
  - Pressed **Tab** to reach **Save**, then pressed **Enter**.
- Expected:  
  - Row switches back to view mode with the new title.  
  - Status message announces success.
- Result:  
  - ✅ The inline form was replaced by the normal view row showing the new title.  
  - ✅ DevTools Elements panel shows a `<div id="status" hx-swap-oob="true">Task "Updated title (HTMX)" updated successfully.</div>`.  
  - ✅ Visually, the status bar at the top of the page updated with the success message.

### 1.3 Validation error (HTMX)

- Action:  
  - Clicked **Edit** on an existing task.  
  - Deleted all text in the title input so it became empty.  
  - Pressed **Save**.
- Expected:  
  - The form stays in edit mode and an error appears under the input.  
  - Screen reader should announce the error via `role="alert"`.
- Result:  
  - ✅ Error message displayed:  
    > *Title is required. Please enter at least one character.*  
  - ✅ HTML confirmed in DevTools:

    ```html
    <p id="title-error-{{ id }}" class="error"
       role="alert" aria-live="assertive">
      Title is required. Please enter at least one character.
    </p>
    ```

  - ✅ NVDA read the error immediately after pressing Save (see screen reader notes below).

---

## 2. No-JS path (JavaScript OFF)

> JavaScript disabled via Edge DevTools (“Disable JavaScript”), then `/tasks` reloaded.

### 2.1 Edit flow (no-JS)

- Action:  
  - With JS disabled, opened `/tasks`.  
  - Clicked **Edit** on a task.
- Expected:  
  - Full page reload to `/tasks/{id}/edit`, showing the edit form in place of that row.
- Result **after fix**:  
  - ✅ Browser URL changed to `/tasks/{id}/edit`.  
  - ✅ The selected task row is shown in **edit mode** (input + Save + Cancel).  
  - ✅ Other tasks remain in normal view mode.

### 2.2 Validation error (no-JS)

- Action:  
  - On `/tasks/{id}/edit`, cleared the title input.  
  - Clicked **Save**.
- Expected:  
  - Redirect back to `/tasks/{id}/edit?error=blank`.  
  - Error message is visible next to the input.
- Result:  
  - ✅ URL became `/tasks/{id}/edit?error=blank`.  
  - ✅ The same page reloaded with the error message under the input.  
  - ✅ Error text matched the HTMX path:
    > *Title is required. Please enter at least one character.*

### 2.3 Successful save (no-JS)

- Action:  
  - Entered a valid title, e.g. **"Updated title (no-JS)"**.  
  - Clicked **Save**.
- Expected:  
  - Redirect back to `/tasks` with updated title in the list.
- Result:  
  - ✅ Browser redirected to `/tasks`.  
  - ✅ The edited task showed the new title in the list in view mode.

---

## 3. Keyboard-only testing

(HTMX path, JavaScript ON)

- Input: hardware keyboard, no mouse.
- Tests:

1. **Tab order**  
   - ✅ Tab moves through:  
     **Title input → Priority input → Add Task → first task Edit → first task Delete → next task Edit → …**  
   - ✅ The order matches the visual layout and is predictable.

2. **Activating controls**  
   - ✅ Pressing **Enter** on **Add Task** added a task.  
   - ✅ Pressing **Enter** on **Edit** opened inline edit.  
   - ✅ Pressing **Enter** on **Save** submitted the edit.  
   - ✅ Pressing **Enter** on **Cancel** (linked button) returned to view mode.

3. **Focus visibility**  
   - ✅ Buttons and inputs have visible focus outlines in the dark theme.  
   - ✅ After an HTMX swap, focus moved to the title input in edit mode as expected.

---

## 4. Screen reader (NVDA) testing

- Tool: **NVDA 2024**, Windows 10, with Microsoft Edge.
- Scenario: HTMX path (JavaScript ON).

### 4.1 Edit button announcement

- Action:  
  - Used **Tab** to move to **Edit** for a task called “Buy milk”.
- Result:  
  - ✅ NVDA announced something like:  
    > “Edit task: Buy milk, button”

### 4.2 Input + hint + value

- Action:  
  - Pressed **Enter** on Edit to open inline edit form.  
  - NVDA moved focus into the title input.
- Result:  
  - ✅ NVDA announced label + hint + current value, e.g.:  
    > “Title, edit, Keep the title short and clear, Buy milk”

### 4.3 Validation error announcement

- Action:  
  - Deleted the whole title and pressed **Save**.
- Result:  
  - ✅ NVDA immediately announced:  
    > “Title is required. Please enter at least one character, alert”  
  - Confirms `role="alert"` + `aria-live="assertive"` are working.

### 4.4 Status message announcement (success)

- Action:  
  - Entered a valid title and pressed **Save**.
- Result:  
  - ✅ NVDA announced the status message from the OOB live region, e.g.:  
    > “Task ‘Updated title (HTMX)’ updated successfully”  
  - Focus stayed on the task area; status did **not** steal focus.

---

## 5. Bug: Edit did nothing in no-JS mode (and how it was fixed)

### 5.1 Symptom

- With JavaScript disabled, clicking **Edit** on a task originally did **nothing useful**:
  - The URL stayed on `/tasks` or only briefly showed `/tasks/{id}/edit`.
  - The task list stayed in view mode (no edit form shown).
- This meant the **no-JS edit flow was broken**.

### 5.2 Cause

- `GET /tasks/{id}/edit` and `POST /tasks/{id}/edit` were only partially implemented for HTMX.  
- There was no proper **no-JS branch**:
  - The route did not return a full page with `editingId` set.  
  - `tasks/index.peb` did not know which task should be in edit mode.

### 5.3 Fix

1. **TaskRepository**  
   - Added helper methods:

     ```kotlin
     fun find(id: Int): Task? =
         tasks.find { it.id == id }

     fun update(task: Task) {
         tasks.find { it.id == task.id }?.let { existing ->
             existing.title = task.title
         }
         persist()
     }
     ```

2. **Templates (`tasks/index.peb`, `_item.peb`, `_edit.peb`)**  
   - `index.peb` now passes `editingId` to decide per row:

     ```twig
     {% for task in tasks %}
       {% if editingId and editingId == task.id %}
         {% include "tasks/_edit.peb" with {"task": task, "error": error} %}
       {% else %}
         {% include "tasks/_item.peb" with {"task": task} %}
       {% endif %}
     {% endfor %}
     ```

3. **Routes (TaskRoutes.kt)**  
   - Implemented **dual-path logic** for `GET /tasks/{id}/edit`:

     - HTMX: return only the `<li>` edit fragment.  
     - No-JS: return full page with `editingId = id` and optional `error`.

   - Implemented `POST /tasks/{id}/edit`:

     - On validation error:  
       - HTMX → return edit fragment with error and `400`.  
       - No-JS → redirect to `/tasks/{id}/edit?error=blank`.  
     - On success:  
       - HTMX → return view fragment + OOB status message.  
       - No-JS → redirect to `/tasks`.

4. **Retest**  
   - Repeated the no-JS tests in section 2.  
   - ✅ After the changes, editing works correctly with JavaScript disabled.

---

## 6. WCAG check summary

| Criterion                 | Status | Evidence                                      |
|--------------------------|--------|-----------------------------------------------|
| 2.1.1 Keyboard (Level A) | ✅ Pass | All actions (add/edit/delete) via keyboard    |
| 3.3.1 Error Identification (A) | ✅ Pass | Field-level error with `role="alert"`        |
| 4.1.3 Status Messages (AA)     | ✅ Pass | Live region announces success without focus change |

---

---

## Screenshots

![View mode with Edit/Delete buttons](01-view-mode-htmx.png)

![Inline edit mode with Save/Cancel](02-edit-mode-htmx.png)

![HTMX validation error under the input](03-validation-error-htmx.png)

![HTMX status message in live region](04-status-message-htmx.png)

![No-JS full-page edit view](05-edit-mode-nojs.png)

![No-JS validation error with query parameter](06-validation-error-nojs.png)


