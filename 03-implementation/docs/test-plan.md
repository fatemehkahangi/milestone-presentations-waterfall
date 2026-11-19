# STC Time Management System - Test Plan

1. **TC-01: Login as Employee**
   - Setup: Run app, open `http://localhost:8080/`.
   - Input: Username `alex@stc.de`, Password `test123`.
   - Expected Output: Successful Dashboard view.

2. **TC-02: Break Rule Violation Check**
   - Setup: Log in as Alex, open Timesheet view.
   - Input: Add entry with worked time > 6 hours (e.g. 08:00 AM to 3:00 PM) with 0 minutes break.
   - Expected Output: Break too short: at least 30 min required for 7h 0min of work.

3. **TC-03: Overtime > 10 Hours Notification**
   - Setup: Log with Username `malte@stc.de`, Password `test123`.
   - Input: Add entry from 08:00AM to 7:30PM (10h 30min worked). Log out and login in with `ingrid@stc.de`, Password `test123`
   - Expected Output: Malte Jansen worked 10h 30min on **current Date**.

4. **TC-04: Timesheet Submission and Locking**
   - Setup: Fill valid entries for Alex's timesheet.
   - Input: Click "Submit time sheet".
   - Expected Output: Timesheet editing is disabled.

5. **TC-05: Supervisor Review, Approval & Flex-Time Update**
   - Setup: Log in with `ingrid@stc.de`, Password `test123` and go to Approvals view.
   - Input: Review Alex's submitted sheet and click "Approve". Log out and log in with `alex@stc.de`, Password `test123`
   - Expected Output: Sheet status becomes `APPROVED`. Time-flex balance  will be updated.

6. **TC-06: Supervisor Rejection with Reason**
   - Pre-Condition: At least one Time Sheet submitted.
   - Setup: Log in as `ingrid@stc.de`, Password `test123` in Approvals view.
   - Input: Click "Reject", provide a non-empty rejection reason, and confirm. Log out and log in as employee, whos time sheet was rejected
   - Expected Output: Sheet status becomes `REJECTED`, rejection reason is saved, employee can edit again.

7. **TC-07: Security / Role Access Restriction**
   - Setup: Log in as regular employee `alex@stc.de`.
   - Input: Manually navigate to `/approvals`.
   - Expected Output: User stays in the dashboard view.