# STC Time Management System - Test Plan

1. **TC-01: Login as Employee**
   - Setup: Run app, open `http://localhost:8080/`.
   - Input: Username `alex@stc.de`, Password `test123`.
   - Expected Output: Successful login and redirect to employee dashboard/timesheet view.

2. **TC-02: Break Rule Violation Check**
   - Setup: Log in as Alex, open Timesheet view.
   - Input: Add entry with worked time > 6 hours (e.g. 08:00 to 15:00) with 0 minutes break.
   - Expected Output: System rejects entry with `IllegalArgumentException` (break required).

3. **TC-03: Overtime > 10 Hours Notification**
   - Setup: Log in as Malte.
   - Input: Add entry from 08:00 to 19:30 (10h 30min worked).
   - Expected Output: Entry saved, and notification is triggered for supervisor (Ingrid).

4. **TC-04: Timesheet Submission and Locking**
   - Setup: Fill valid entries for Alex's timesheet.
   - Input: Click "Submit time sheet".
   - Expected Output: Status changes to `SUBMITTED`, sheet becomes locked (`isLocked = true`), form disables editing.

5. **TC-05: Supervisor Review, Approval & Flex-Time Update**
   - Setup: Log in as supervisor `ingrid@stc.de` and go to Approvals view.
   - Input: Review Alex's submitted sheet and click "Approve".
   - Expected Output: Sheet status becomes `APPROVED`, employee flex-time account is recalculated.

6. **TC-06: Supervisor Rejection with Reason**
   - Setup: Log in as `ingrid@stc.de` in Approvals view.
   - Input: Click "Reject", provide a non-empty rejection reason, and confirm.
   - Expected Output: Sheet status becomes `REJECTED`, rejection reason is saved, employee can edit again.

7. **TC-07: Security / Role Access Restriction**
   - Setup: Log in as regular employee `alex@stc.de`.
   - Input: Manually navigate to `/approvals`.
   - Expected Output: Access denied / RouteGuard blocks access.