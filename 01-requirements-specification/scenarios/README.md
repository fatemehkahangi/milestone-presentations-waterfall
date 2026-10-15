# Scenarios

Scenarios describe how different users interact with the time management system to achieve their goals.

---

## Quick Navigation

* [Scenario 1: Submitting Monthly Work Time & Project Hours](#scenario-1-submitting-monthly-work-time--project-hours)
* [Scenario 2: Supervisor Approval and Flex Time Update](#scenario-2-supervisor-approval-and-flex-time-update)
* [Scenario 3: Requesting and Canceling Vacation Time](#scenario-3-requesting-and-canceling-vacation-time)
* [Scenario 4: Overtime Monitoring and Exceeding Daily Limits](#scenario-4-overtime-monitoring-and-exceeding-daily-limits)
* [Scenario 5: HR Registration of Sick Leave and Location-Based Access](#scenario-5-hr-registration-of-sick-leave-and-location-based-access)
* [Scenario 6: Year-End Vacation Expiration Notification](#scenario-6-year-end-vacation-expiration-notification)

---

## Scenario 1: Submitting Monthly Work Time & Project Hours

**User:** Alex (Mechanical Engineer at STC)

1. Throughout the month, Alex logs his daily working hours, break durations (e.g., entering at least 30 minutes for days exceeding 6 working hours), and assigns his time to a unique customer project created in the system.
2. At the end of the month, Alex reviews his total logged time and clicks **"Transmit Time Sheet."**
3. Once submitted:
   * The time sheet becomes **read-only** for Alex.
   * An automated notification is sent to his supervisor, **Maria**.

---

## Scenario 2: Supervisor Approval and Flex Time Update

**User:** Ingrid Halvorsen (Supervisor)

1. At the beginning of the new month, Maria logs into the central system to review her team's submitted time sheets.
2. She opens Alex's monthly record, checks project time allocations, and verifies compliance with company break policies.
3. **Outcome Options:**
   * **Approval:** Maria approves the time sheet. The system automatically recalculates and updates Alex's **Flex Time account balance**.
   * **Rejection:** If an issue is identified, Maria rejects the sheet with a reason, unlocking it for Alex to correct and resubmit.

---

## Scenario 3: Requesting and Canceling Vacation Time

**User:** Malte Jansen (Employee)

1. **Submission:** David submits a 5-day vacation request at least 1 day prior to the planned start date.
2. **Approval:** His supervisor approves the request, reserving 5 days from David’s annual 30-day allowance.
3. **Cancellation:**
   * Two weeks later, due to project schedule changes, David deletes the planned vacation directly in the software.
   * **Workflow:** No re-approval workflow is required.
   * **Notification:** The system automatically sends a notification to David’s supervisor **2 days before** the original start date of the canceled vacation.

---

## Scenario 4: Overtime Monitoring and Exceeding Daily Limits

**User:** Elena (Employee)

1. During a critical machine setup phase, Elena inputs **10.5 hours** for a Tuesday.
2. The system accepts the entry but flags a daily limit breach (> 10 hours).
3. **Notification Logic:**
   * Elena and the HR department do **not** receive a warning.
   * An automated overtime notification is generated specifically for her supervisor's dashboard and sent via email.

---

## Scenario 5: HR Registration of Sick Leave and Location-Based Access

**User:** Sarah (HR Manager)

1. **Access Control:** HR access is restricted to company grounds for security and compliance reasons. Sarah logs into the portal from her desktop connected to the office network.
2. **Registration:** Sarah registers sick leave on behalf of an employee who called in sick.
3. **Workflow:**
   * No approval is required for sick days.
   * The system automatically emails the employee's direct supervisor to maintain team awareness.

---

## Scenario 6: Year-End Vacation Expiration Notification

**User:** Thomas (Employee) / Automated System Audit

1. At the end of the calendar year, the system runs an automated audit of remaining vacation balances.
2. It detects that Thomas has **5 unused vacation days** out of his annual 30-day allowance.
3. The system sends an automated notification to Thomas:  
   > *"You have 5 remaining vacation days. Please take them within the designated carry-over period."*
