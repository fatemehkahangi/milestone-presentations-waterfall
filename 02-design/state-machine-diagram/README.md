# State Machine Diagram: TimeSheet Lifecycle

## Overview
This State Machine Diagram represents the complete lifecycle and state transitions of a monthly **TimeSheet** entity within the STC Time Management System. It models how an employee's time tracking sheet transitions from initial creation to final approval.

## States & Key Transitions

### 1. Draft
* **Description:** Initial state after creation (`Create Monthly Sheet`). The employee logs daily work hours, mandatory break times, and project allocations.
* **Transition Out:** `transmitTimeSheet() [lock]`
  * Triggered when the employee submits the monthly time sheet.
  * **Action:** The system locks the document, setting it to **Read-Only** mode to prevent further edits during review.

### 2. Submitted
* **Description:** Pending state awaiting Supervisor review.
* **Transitions:**
  * **Approval:** `approve() [update Flex Time]`
    * Triggered by the Supervisor approving the submitted hours.
    * **Action:** Moves the sheet to the `Approved` state and automatically triggers the recalculation of the employee's **Flex Time account balance**.
  * **Rejection:** `reject(reason) [unlock & edit]`
    * Triggered by the Supervisor rejecting the submission due to errors or policy violations.
    * **Action:** Returns the sheet to the `Draft` state, unlocks it for editing, and notifies the employee with the provided reason.

### 3. Approved
* **Description:** Final terminal state. The time sheet is permanently archived and read-only. Flex time updates are committed.

## Traceability to Business Requirements
* **10-Hour Daily Limit & Mandatory Breaks:** Validated while in the `Draft` state prior to submission.
* **Supervisor Approval:** Only Supervisors can trigger transitions out of the `Submitted` state.
* **Flex Time Synchronization:** Automatically executed upon transition to `Approved`.
