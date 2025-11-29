# Week 8 — Prototyping Constraints & Trade-offs (Tasks UI)

This note summarises the main design and implementation decisions I made in **Week 8 Lab 1** when adding **partials, filtering and pagination** to the task manager. It focuses on how the prototype scales while still keeping **server-first, HTMX as enhancement, and accessibility**.

---

## 1. Rendering splits

I now have **two main ways** of rendering the task list:

### 1.1 Full-page route

- **Route**: `GET /tasks`
- **Returns**: the full HTML page:
  - Layout from `_layout/base.peb`
  - Header + navigation + footer partials
  - Filter form
  - Add-task form
  - Task area: `tasks/_list.peb` + `tasks/_pager.peb`

**Who uses this:**

- Users with **JavaScript disabled**
- HTMX users on initial page load
- Browser back/forward when reloading a page directly

### 1.2 Fragment route

- **Route**: `GET /tasks/fragment`
- **Returns**: **fragments only**, not the whole page:
  - `tasks/_list.peb` (the `<ul>` with tasks + result count)
  - `tasks/_pager.peb` (the pagination links)
  - A status update with `hx-swap-oob="true"` for the live region (`#status`)

**Who uses this:**

- HTMX-enhanced filter form  
- HTMX pagination links

This split keeps the **source of truth for markup** in partials (`_list` and `_pager`), and I just decide whether to wrap them in the full layout or not.

---

## 2. State, URL & history

### 2.1 State encoded in the URL

The main state for the list is:

- `q` – text filter (search query)
- `page` – 1-based page number

Examples:

- `/tasks?q=exam` → filter titles containing “exam”, page 1
- `/tasks?q=exam&page=2` → same filter, page 2

By keeping state in the URL:

- Users can **bookmark** or **share** a filtered page.
- The **back/forward buttons** behave as expected.
- No hidden client-side state is needed.

### 2.2 HTMX and `hx-push-url`

I use `hx-push-url="true"` on:

- The **filter form**  
- The **pagination links**

This means:

- When HTMX loads `/tasks/fragment` with new `q` or `page` values, the **browser URL is also updated**.
- Pressing **Back** returns to the previous query / page, not just the previous DOM state.
- The server still has a clean `/tasks` full-page handler if someone reloads the URL.

---

## 3. Partials & reuse

### 3.1 `_list.peb`

`tasks/_list.peb` is responsible for:

- Rendering the `<ul id="task-list">` with tasks.
- Deciding between **view** and **edit** states using `editingId`.
- Showing a friendly message when there are no tasks.
- Announcing the result count for screen readers.

Key points:

- The same partial is used:
  - In the **full-page** render (`index.peb`)
  - In the **fragment** response (`/tasks/fragment`)
- This reduces **duplication** and keeps the inline edit logic in one place.

### 3.2 `_pager.peb`

`tasks/_pager.peb` renders:

- A `<nav aria-label="Pagination">` landmark
- A `<ul class="pagination">` with:
  - Optional **Previous** link
  - Current page text: “Page X of Y”
  - Optional **Next** link

Each link has:

- A normal `href="/tasks?q=...&page=..."` for no-JS / full-page loads
- HTMX attributes:
  - `hx-get="/tasks/fragment?q=...&page=..."`  
  - `hx-target="#task-area"`  
  - `hx-push-url="true"`

So the **same markup** works in both modes.

---

## 4. Accessibility hooks

I tried to keep the Week 7 accessibility work when adding complexity.

### 4.1 Live status region

- A `<div id="status" role="status" aria-live="polite" aria-atomic="true">` lives in the base layout header.
- HTMX responses from `/tasks/fragment` include an **out-of-band** update like:

  ```html
  <div id="status" hx-swap-oob="true">
    Found 7 tasks matching "exam".
  </div>
  ```
This gives:

- Visible feedback for sighted users

- Announced feedback for screen reader users (WCAG 4.1.3 Status Messages)

### 4.2 Result count tied to the list

In _list.peb I have:

A <p id="result-count"> with text like
“Showing 5 of 12 tasks matching comp”

The <ul id="task-list"> uses aria-describedby="result-count".

This means:

Screen reader users can understand how many items are shown and how many exist in total.

When the list is updated by filtering or paging, the result count changes too.

### 4.3 Keyboard and no-JS parity

Design goals:

Filter form:

Works fully with Enter + Tab even without JS.

Label and hint linked by for + aria-describedby.

Pagination:

Links are normal <a> elements.

No keyboard traps; focus stays predictable when changing pages.

I manually tested:

Keyboard-only navigation through filter, add form, task list and pager.

JS disabled: filter + pagination still work via full-page reloads.

## 5. Performance considerations

### 5.1 Page size choice

I use a fixed page size of 10 tasks.

This is a compromise between:

Fewer HTTP requests (bigger pages)

Lower cognitive load and less scrolling (smaller pages)

10 items is a reasonable default for:

Students scanning tasks

Screen reader users not wanting to navigate 50+ items at once

### 5.2 Full-page vs fragment cost

Full-page (/tasks):

Sends layout + header + footer + all scripts/styles.

Heavier, but needed for first load and no-JS.

Fragment (/tasks/fragment):

Only sends the changing part (_list + _pager + status).

Lighter for repeated operations (filtering, paging).

Trade-off:

Implementation is slightly more complex (two routes),
but network and rendering cost per interaction is smaller.

## 6. Implementation constraints & decisions

Server-first

All flows (filter, pagination, add, edit, delete) can be done with no JavaScript using full-page routes.

HTMX is used only as a progressive enhancement.

Single source of truth for markup

_list.peb and _pager.peb are the only places that know how to render the list and pager.

Both /tasks and /tasks/fragment call the same partials.

State lives in query parameters

No hidden session-only state for current page or q.

Easy to debug and test: everything is visible in the URL.

Repository search helper

TaskRepository.search(query, page, size) returns a Page<Task>.

This isolates paging logic from routes and keeps templates simple (page.items, page.totalPages, etc.).

## 7. Risks & mitigations

### 7.1 Template duplication drift

Risk:
If someone later edits the list or pager HTML in only one place, full-page and fragment outputs can diverge.

Mitigation:

Always edit _list.peb and _pager.peb, not hand-written HTML in routes.

Keep routes dumb: they just render templates, no inline HTML strings apart from status messages.

### 7.2 Focus behaviour after actions

Risk:
After delete or paging, focus might jump to the top of the page or disappear, which is confusing for keyboard and screen reader users.

Current state:

Basic flows work, but I have not yet added advanced focus management (e.g. returning focus to the next <li> or to the filter).

Future mitigation:

Use hx-swap strategies or JavaScript to move focus back to:

The filter input after search

The next/previous task after a delete

### 7.3 Out-of-range pages

Risk:
If the user manually requests /tasks?page=999, the result set is empty.

Current behaviour:

The search helper clamps currentPage to a valid range.

The pager shows correct “Page X of Y” even if the URL had an invalid number.

## 8. Quick verification log

After implementing filtering, pagination, and partials, I ran a quick check:

✅ Keyboard-only:

Tab through filter → add form → list → pager in a logical order.

Activate filter with Enter; use pager links with Enter.

✅ No-JS:

Disabled JS in the browser.

/tasks?q=...&page=... still works via full-page reload.

✅ HTMX-enhanced:

Typing in the filter with HTMX triggers fragment updates.

Pager links update only the task area and the URL (via hx-push-url).

✅ Screen reader sanity check (NVDA):

Result count is read because of aria-describedby="result-count".

Status messages like “Found X tasks” are announced via the live region.

These checks show that the prototype scales to larger lists while still following the server-first + progressive enhancement approach and maintaining basic accessibility guarantees.