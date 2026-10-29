# Activity Diagrams: Main Processes of the STC Time Management System

## Overview
These UML Activity Diagrams answer the design question **"What are the main processes of the system and which roles are involved?"**
Each diagram uses swimlanes to show which role performs which action. The editable source for all three diagrams is `activity-diagrams.drawio` (one page per diagram).

| # | Process | Roles (swimlanes) | Files |
|---|---------|-------------------|-------|
| 1 | Record, submit and approve the monthly time sheet | Employee, System, Supervisor | `01-monthly-time-sheet.pdf` / `.png` |
| 2 | Request, approve and cancel vacation | Employee, System, Supervisor | `02-vacation-request.pdf` / `.png` |
| 3 | Register sickness | Employee, HR, System, Supervisor | `03-sickness.pdf` / `.png` |

## 1. Monthly Time Sheet
* The employee records begin, end and break time for each working day and books the time on a customer project.
* The system validates the break rules (more than 6 h: 30 min, more than 9 h: 60 min). If the rules are violated, the employee corrects the entries.
* If an employee works **more than 10 hours** on one day, the system notifies the supervisor by email (HR is not notified).
* At the end of the month, the employee submits the time sheet. The system locks it (read-only) and notifies the supervisor.
* The supervisor reviews the time sheet:
  * **[errors found]** – rejects it with a reason; the system unlocks the sheet and informs the employee, who corrects the times and submits again.
  * **[correct]** – approves it; only then the system updates the **flex time account**.
* If the flex time balance is outside the range of −100 … +100 hours, the system shows a warning (soft rule, no blocking).
* The updated flex time is shown to the employee, the supervisor and HR.

## 2. Vacation Request
* The employee enters a vacation request with a start and end date.
* The system checks that the request is made at least 1 day in advance and that enough open vacation days are left (30 days per year). If not, the employee adjusts the request.
* A valid request is saved with the status `SUBMITTED` and the supervisor is notified.
* The supervisor reviews the request:
  * **[rejected]** – rejects it with a reason; status `REJECTED`, the employee and HR are informed.
  * **[approved]** – status `APPROVED`, the days are marked as *planned*, the employee and HR are informed.
* If plans change, the employee can delete the planned vacation without a new approval. The system sets the status `CANCELED`, releases the days and notifies the supervisor.
* Otherwise, after the vacation period the days are marked as *taken*.

## 3. Sickness
* The employee informs HR about the sickness by phone or email.
* HR logs in (HR access is only possible on the company premises) and registers the sick days for the employee.
* The system saves the sick days – **no approval is needed** – and notifies the supervisor by email.

## Traceability to Requirements
* **Break rules and 10-hour limit:** validated while recording work time (Diagram 1).
* **Supervisor approval of time sheets and vacation:** only the Supervisor lane contains approve/reject actions (Diagrams 1 and 2).
* **Flex time update only after approval:** "Update flex time account" follows "Approve time sheet" (Diagram 1); consistent with the TimeSheet state machine (`Draft → Submitted → Approved`).
* **Vacation status values:** `SUBMITTED`, `APPROVED`, `REJECTED`, `CANCELED` match the `VacationRequestStatus` enumeration in the class diagram.
* **Sickness without approval and HR access on company premises:** Diagram 3.
