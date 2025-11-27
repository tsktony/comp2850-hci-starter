# Data Boundaries — Week 7

**Module**: COMP2850 Human-Computer Interaction  
**Week**: 7  
**Student**: Chenguo Wan  


## What I Will Collect (OK)

In this project I’m fine to log:

- Pseudonymised session IDs, for example `P1_a3f7`.  
- Simple task metadata in the demo app: id, title, created time, completed flag.  
- Technical interaction data: which route was called, rough response time, HTTP status code, JS mode (HTMX or no-JS).  
- Accessibility testing notes, for example “NVDA read the error twice” or “focus moved to the Save button instead of back to the list”.

These are enough for me to answer my HCI questions (error rate, time-on-task, etc.) without needing to know who the person actually is.

---

## What I Will NOT Collect (Not OK)

I do **not** plan to log or store:

- Real names of participants – I’ll always use labels like “Participant A” or “P1”.  
- Student ID numbers or email addresses.  
- IP addresses or any kind of browser fingerprint.  
- Real personal to-do items from people’s private lives – I will stick to demo tasks like “Buy milk”, “Finish COMP2850 lab”, etc.

If someone accidentally types something very private into a task title (for example “book doctor appointment for X”), I will remove that entry from the demo data and from any logs as soon as I notice it.

---

## Storage and Retention

- All data for this lab stays on **my own laptop** in this project folder  
  (CSV files like `data/tasks.csv` and `data/metrics.csv`).  
- The GitHub repo for this module is **private**. I will **not** push raw logs or interview notes to a public repository.  
- I will keep the data until the end of Semester 1 for marking. After that I will either delete it,  
  or anonymise it even further if I want to reuse screenshots in a portfolio (for example by blurring names).

---

## Privacy by Design (How I Apply Week 6 Ideas)

I’m trying to apply the Week 6 privacy-by-design ideas in a simple way:

1. **Data minimisation** – only log what I really need for analysis (no extra PII “just in case”).  
2. **Purpose limitation** – the logs are only for COMP2850 coursework, not for anything commercial.  
3. **Storage limitation** – keep the data only until assessment is finished, not forever.  
4. **Integrity & confidentiality** – keep the data on my own machine, not on random cloud services; keep the repo private.

---

## Possible Risks and How I’ll Reduce Them

| Risk | Mitigation |
|------|-----------|
| Someone types sensitive content into a task title | I use neutral example tasks in demos, and I will manually delete any sensitive ones if I spot them |
| Session IDs could be linked back to a real person | I will not keep any “mapping table” from P1/P2 to real names; the IDs are just labels in my notes |
| I accidentally push logs or notes to GitHub | I add the `data` and `research`/`notes` folders to `.gitignore` so they are not committed |
| Screenshots contain names or emails | I crop screenshots to just the task area or blur personal information before saving/using them |

This document builds on the informed consent protocol I wrote in Week 6 and is meant to guide what I log in Week 7 and later labs.
