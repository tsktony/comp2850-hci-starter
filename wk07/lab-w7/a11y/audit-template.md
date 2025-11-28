# Accessibility Audit — Week 7

## Scope

**Pages checked**

- `/tasks` index page (task list + add form)
- Inline edit form (HTMX: “Edit” → input inside `<li>`)
- Edit flow in **no-JS** mode (`/tasks/{id}/edit`)
- Create flow with **JS on** and **JS off**

**Environment**

- Browser: Microsoft Edge (Windows 11)
- Screen reader: NVDA (Windows 11)
- Zoom: 100% and 200%
- JavaScript: tested both enabled and disabled

---

## Keyboard

- [x] All actions possible with keyboard only  
  - Tested: add task, edit task title, save, cancel, delete task.  
  - Used **Tab / Shift+Tab / Enter / Space** in Microsoft Edge.  
  - With JS **off**, the Edit button now works by going to `/tasks/{id}/edit` (full page).

- [x] Focus order logical; no traps; visible focus  
  - Order: skip link → page title → “Add a new task” form → task list items (Edit / Delete).  
  - No keyboard traps found; I can always Tab forward and Shift+Tab backward.  
  - Pico’s default outline is visible, but a bit faint on my monitor (see Finding A3).

---

## Structure & Semantics

- [x] One H1, meaningful H2/H3  
  - `<h1>Tasks</h1>` for page title, `<h2>` for “Add a new task” and “Current tasks”.  
  - No extra H1s.

- [x] Landmarks present (main, nav if any)  
  - Layout uses semantic `<main>` for the central content area.  
  - Skip link jumps to `#main` so keyboard users can bypass repeated content.

- [x] Lists and buttons/links used correctly  
  - Task list is a real `<ul>` with each task in an `<li>`.  
  - Actions are `<button>`s inside `<form>`s, not click-only `<div>`s.  
  - “Cancel” in inline edit is implemented as a link with `role="button"` in the partial.

---

## Forms

- [x] Labels bound to inputs  
  - Main title field: `<label for="title">Title</label>` correctly connected.  
  - Inline edit fields use `<label for="title-{{ task.id }}">` so each input has a unique label.  
  - **Exception (A1)**: the optional `priority` field only has a placeholder, no visible label.

- [x] Errors linked via `aria-describedby`  
  - Edit form: `aria-describedby="hint-{{ task.id }} error-{{ task.id }}"`.  
  - Error message uses a predictable ID so screen readers read label + hint + error together.

- [x] Instructions/hints near controls  
  - Hint text “Keep it short and specific.” is placed just under the title input.  
  - Error messages appear directly below the field that caused the error.

---

## Dynamic Updates

- [x] Status messages announced (`role="status"`)  
  - Base template has a status region updated via HTMX OOB swap.  
  - NVDA announces messages like `Task "Buy milk" added successfully.` without moving focus.

- [x] No unexpected focus shifts  
  - In HTMX path, focus stays on the interactive controls (Edit → input → Save/Cancel).  
  - When JS is disabled, full-page reload is expected behaviour; focus returns to top of page.

- [x] OOB updates don’t hide essential content  
  - Only the `#status` live region is updated OOB; task items themselves are swapped in place.  
  - No important content disappears unexpectedly for keyboard or screen reader users.

---

## Contrast & Zoom

- [x] AA contrast met (informal check)  
  - Text on the default Pico theme looks readable on my screen at normal brightness.  
  - Buttons and body text are sufficiently dark on a white background for typical use.  
  - I have not yet run a formal contrast checker on all colours, so this is a **provisional pass**.

- [x] 200% zoom still works  
  - At 200% zoom in Edge, the layout is still usable:  
    - Add form and task list stack vertically.  
    - No horizontal scrolling needed for the main content.  
    - Buttons and text remain readable and clickable.

---

## Findings

| ID | Location                    | Issue                                      | Impact | Inclusion risk                  | Evidence                                   | Fix idea                                                                                     |
|----|-----------------------------|--------------------------------------------|--------|---------------------------------|--------------------------------------------|----------------------------------------------------------------------------------------------|
| A1 | Add form (`priority` input) | Optional `priority` field has no label.    | Medium | Screen reader, Cognitive        | Input only has placeholder text, no label. | Add `<label for="priority">Priority (optional)</label>` and give the input `id="priority"`. |
| A2 | Task list icon image        | Icon image has no `alt` attribute.         | Low    | Screen reader                   | `<img src="/static/img/icon.png">`         | If decorative, set `alt=""`; if meaningful, add a short descriptive `alt` text.             |
| A3 | Buttons / links focus       | Focus outline is visible but quite subtle. | Medium | Keyboard, Low vision, SR users | Manual keyboard + 200% zoom test.          | Add stronger `:focus` styles in CSS (e.g. 2–3px solid outline with a high-contrast colour). |

---

_Note_: During this audit I also confirmed that the **Edit** button now works with JavaScript disabled (full-page edit form at `/tasks/{id}/edit`), and that NVDA can successfully announce labels, hints, error messages, and status updates for the inline edit flow.
