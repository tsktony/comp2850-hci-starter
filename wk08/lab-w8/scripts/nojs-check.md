# No-JS Parity Verification Script — Week 8

**Purpose**: Verify that all main task flows still work correctly when **JavaScript is disabled**, using the server-first baseline.

---

## Setup

1. Open the task manager at `/tasks`.
2. Open DevTools → Settings → **Disable JavaScript**:
   - Chrome / Edge: DevTools ⚙ → Preferences → Debugger → uncheck “Enable JavaScript”.
3. Hard-refresh the page: `Ctrl+Shift+R` (Windows) or `Cmd+Shift+R` (macOS).
4. Check the footer and confirm it shows: `Mode: No-JS`.

---

## Test 1: Add task (happy path)

**Steps**

1. Go to `/tasks`.
2. In the **Title** input, type: `No-JS test task`.
3. Click **Add Task**.

**Expected**

- The browser does a **full page reload** (no HTMX/XHR requests).
- The URL is `/tasks` (PRG redirect).
- The new task “No-JS test task” appears in the **Current tasks** list.
- No error messages are shown.

**Result**

- [x] Pass  
- [ ] Fail  

---

## Test 2: Add task (validation error)

**Steps**

1. Make sure JavaScript is still disabled.
2. Open `/tasks`.
3. Leave the **Title** field empty.
4. Click **Add Task**.

**Expected**

- Full page reload.
- URL becomes `/tasks?error=title&msg=blank`.
- An error summary appears above the form:

  > There is a problem  
  > • Title is required. Please enter at least one character.

- The **Title** input:
  - Has a red error border.
  - Has `aria-invalid="true"`.
  - Has `aria-describedby="title-hint title-error"`.
- An inline error message appears under the input:  
  “Title is required. Please enter at least one character.”
- When you activate the link in the error summary, keyboard focus moves to the **Title** input.

**Result**

- [x] Pass  
- [ ] Fail  

---

## Test 3: Filter tasks

**Steps**

1. With JS still disabled, create three tasks: `Alpha`, `Bravo`, `Charlie`.
2. In **Filter tasks**, type: `ra`.
3. Press **Enter** or click **Apply**.

**Expected**

- Full page reload.
- URL becomes `/tasks?q=ra&page=1`.
- Only `Bravo` and `Charlie` remain in the list (they contain “ra”).
- If a result count is shown (e.g. “Showing 2 tasks”), it should show 2.

**Result**

- [x] Pass  
- [ ] Fail  

---

## Test 4: Pagination

(Assuming page size = 10.)

**Steps**

1. With JS disabled, add at least 15 simple tasks (e.g. `T1` … `T15`).
2. On `/tasks`, click the **Next** link in the pager.

**Expected**

- Full page reload.
- URL becomes `/tasks?page=2`  
  (or `/tasks?q=...&page=2` if a filter is active).
- The list shows only tasks 11–15.
- The **Previous** link is visible.
- On the last page, the **Next** link is hidden or disabled.

**Result**

- [x] Pass  
- [ ] Fail  

---

## Test 5: Edit task (no-JS path)

**Steps**

1. With JS disabled, on `/tasks` click **Edit** for one task.
2. The page reloads and an edit form appears in place of that task.
3. Change the title to: `Updated via no-JS`.
4. Submit the form.

**Expected**

- Full page reload.
- The updated title `Updated via no-JS` is visible in the list.
- No error messages are shown.

**Result**

- [x] Pass  
- [ ] Fail  

---

## Test 6: Delete task (no-JS fallback)

**Steps**

1. With JS disabled, on `/tasks` click **Delete** for one task.
2. Note: there is **no** browser confirmation dialog in this mode (this is an accepted trade-off).

**Expected**

- The request is sent to `POST /tasks/{id}/delete`.
- The browser is redirected back to `/tasks`.
- The deleted task is no longer in the list.
- No error messages are shown.

**Result**

- [x] Pass  
- [ ] Fail  

---

## Test 7: Keyboard navigation (no-JS)

**Steps**

1. With JS disabled, start at the top of `/tasks`.
2. Use **Tab / Shift+Tab** only:
   - Skip link (if present).
   - Filter form controls.
   - Add Task form controls.
   - Edit / Delete buttons in the task list.
   - Pagination links.
3. Use **Enter** to trigger **Add Task** and **Apply**.

**Expected**

- Focus order matches the visual order of the page.
- Every interactive element is reachable via keyboard.
- Focus has a clear visible indicator.
- Submitting forms with Enter works as expected.

**Result**

- [x] Pass  
- [ ] Fail  

---

## Test 8: Browser history (no-JS)

**Steps**

1. With JS disabled, go to `/tasks`.
2. Add a task → you are redirected back to `/tasks`.
3. Apply a filter, e.g. `test` → URL becomes `/tasks?q=test`.
4. Use pagination to go to page 2 → `/tasks?q=test&page=2`.
5. Press the browser **Back** button several times.

**Expected**

- Back #1 → `/tasks?q=test&page=1` (same filter, first page).
- Back #2 → `/tasks?q=test`.
- Back #3 → `/tasks`.
- At each step, the list of tasks matches the URL state.

**Result**

- [x] Pass  
- [ ] Fail  

---

## Summary

**Passed**: 8 / 8  
**Failed**: 0 / 8  

All no-JS flows currently behave as expected. If new issues are found later, they can be recorded in a separate backlog document.
