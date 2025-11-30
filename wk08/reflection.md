# Week 8 – Reflection

## 1. Dual-path complexity (HTMX + no-JS)

At the start of Week 8 I thought “server-first + HTMX enhancement” just meant adding a few attributes.  
In reality it was more complex, because almost every route now has **two paths**:

- HTMX requests: return **fragments** and OOB status messages;
- No-JS requests: return **full pages** or PRG redirects.

The small helper `call.isHtmx()` kept the branching readable, but it also meant that whenever I changed a route, I had to think about both responses at the same time.  

Splitting templates into shared partials like `_list.peb`, `_pager.peb`, `_item.peb` helped a lot — there is only one place that knows how to render the task list and pager.  
The `lab-w8/scripts/nojs-check.md` script was also useful: after any change to routes or templates I could re-run it to see if the HTMX and no-JS paths were still in sync.

---

## 2. Error handling and accessibility

Before this lab, I mostly thought about validation as “don’t let bad data into the database”.  
After reading WCAG and the GOV.UK error patterns, I realised **how errors are presented to people** is equally important:

- The error must be in text, not just colour or an icon;
- The invalid input should have `aria-invalid` and be linked to its error text via `aria-describedby`;
- Screen reader users need a **live region or error summary** so they actually hear what went wrong.

For `/tasks` I ended up doing:

- When the title is blank the server redirects to `/tasks?error=title&msg=blank`;
- The page shows an error summary at the top with a link to the title field;
- The `title` input has a red border, `aria-invalid="true"`, and a specific error message below it that is referenced from `aria-describedby`.

This changed my mental model: validation is not only a backend-security task, it is also **interaction design + accessibility** work.

---

## 3. Trade-offs, especially delete confirmation (and a debugging story)

The most obvious trade-off this week is **delete confirmation**:

- In the HTMX path I use  
  `hx-confirm="Delete the task '{{ task.title }}'?"`,  
  so people with JavaScript get a browser confirm dialog before the request is sent.
- In the no-JS path the form simply posts to `POST /tasks/{id}/delete` with **no intermediate confirmation page**.

For a lab prototype this inconsistency is acceptable, but in a real product it could definitely surprise people. With more time I would probably:

- Add a dedicated confirmation page for no-JS, e.g. `GET /tasks/{id}/delete/confirm`, or
- Implement an “undo / soft delete” instead of hard delete.

### Debugging: why the confirm dialog never appeared

At one point the delete button was even more confusing:  
**there was no confirmation dialog at all, even with JavaScript enabled — it just deleted immediately.**

My first guess was that I had written the HTMX attributes incorrectly, but the markup looked fine.  
The real clue came from DevTools: the console showed an HTMX error like:

> `querySelectorAll` on ‘Document’: `'closest(form)'` is not a valid selector

This was caused by my filter form, where I had copied an `hx-trigger` value incorrectly:

```html
hx-trigger="keyup changed delay:300ms, submit from:closest(form)"
```

Because that selector was invalid, HTMX crashed during initialisation and stopped processing the rest of the attributes, including hx-confirm on the delete button. From the UI it just looked like “HTMX is broken” and delete had no confirmation.

The fix was to simplify the trigger to a valid value, for example:
hx-trigger="keyup changed delay:300ms, submit"
and then keep the HTMX attributes on the correct element:

<form action="/tasks/{{ task.id }}/delete"
      method="post"
      style="display:inline;">
  <button
    type="submit"
    hx-post="/tasks/{{ task.id }}/delete"
    hx-target="#task-{{ task.id }}"
    hx-swap="outerHTML"
    hx-confirm="Delete the task '{{ task.title }}'?"
    aria-label="Delete task: {{ task.title }}"
  >
    Delete
  </button>
</form>

After fixing the invalid trigger, the HTMX confirm dialog started working as expected.

This little debugging session reminded me of two things:

- Always check the JavaScript console when HTMX behaves strangely;

- A single broken attribute can disable all the “progressive enhancement” on the page.

## 4. Testing and the value of scripts

Writing lab-w8/scripts/nojs-check.md felt slightly over-formal at first, but it turned out to be very useful:

It forced me to list all the important flows: create, validation error, filter, pagination, edit, delete, history, keyboard navigation;

After refactoring routes or templates, I had a repeatable checklist instead of random clicking;

Anyone else (teammate or tutor) could run through the steps without me standing next to them explaining the UI.

I think I will reuse this style of “semi-formal test script” for future progressive-enhancement projects.
It sits nicely between “just click around a bit” and fully automated tests.

## 5. Inclusion impact of the dual-path architecture

The people who actually benefit from the dual-path design are quite specific:

People on slow or unstable networks: if JS bundles fail to load, the HTML-only path still works;

People on locked-down enterprise browsers or older devices where JS might be restricted or disabled;

Screen reader and keyboard users: our baseline is full HTML forms and links, not a JS-heavy SPA;

People who rely heavily on the Back button to recover from mistakes: PRG and clean URLs make history more predictable.

Designing the no-JS path first, then layering HTMX on top, makes the prototype feel much more robust.
HTMX becomes true progressive enhancement instead of a hard dependency.

## 6. Next steps before Week 9 instrumentation

Before adding Week 9 instrumentation and evaluation, I would like to:

Revisit the delete flow and decide whether to:

add a no-JS confirmation page, or

experiment with undo / soft delete;

Clean up the template structure to ensure _list.peb, _pager.peb, and _item.peb really are the single source of truth for the list UI;

Improve focus management after actions such as:

returning focus to the filter input after a search,

moving focus to the next task (or the list container) after a delete;

Re-run nojs-check.md whenever I touch routes or templates, so the evaluation in Week 9 starts from a solid baseline.

Overall, Week 8 made me take “server-first + progressive enhancement + accessibility” more seriously as a combined design approach.
I now treat “equivalent behaviour in HTMX and no-JS paths” as a design requirement, not just a technical nice-to-have.