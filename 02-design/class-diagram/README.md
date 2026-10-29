# Class Diagram: Vacation Request Architecture (ECB Pattern)

## Overview
This UML Class Diagram depicts the structural design of the **Vacation Request Subsystem** using the **Entity-Control-Boundary (ECB)** architectural pattern. It encapsulates the data models, user interaction boundaries, and workflow controls required for managing employee vacation leave.

## Architectural Layers (ECB Breakdown)

### 1. Boundary Layer (`«boundary»`)
* **`VacationRequestForm`**: Handles user interface interaction.
  * `onNewRequestClicked()` / `onCancelRequestClicked()`: User actions for submitting or canceling vacation requests.
  * `displayVacationBalances()`: Displays real-time breakdown of open, planned, and taken vacation days.
  * `displayStatus()`: Renders feedback and status messages to the user.

### 2. Control Layer (`«control»`)
* **`VacationRequestControl`**: Manages the core business domain logic.
  * `requestVacation()`: Validates submission timing (must be submitted at least 1 day in advance).
  * `cancelRequest()`: Processes cancellation of future approved/planned leaves without requiring re-approval.
  * `validateBusinessRules()`: Enforces annual caps (30 days limit) and carry-over policy rules.
* **`ApproverWorkflowService`**: Handles asynchronous coordination.
  * `processWorkflowStepAsync()`: Asynchronously dispatches approval tasks, reminders, and supervisor notifications.

### 3. Entity & Enumeration Layer (`«entity»`, `«enumeration»`)
* **`VacationRequest`**: Core entity storing request metadata (`startDate`, `endDate`, `totalDays`, `status`).
* **`VacationRequestDay`**: Detailed itemized entity representing individual days within a request (`date`, `isWorkingDay`). Linked via 1-to-many Composition.
* **`VacationRequestStatus`**: Enumeration defining valid lifecycle states: `SUBMITTED`, `APPROVED`, `REJECTED`, `CANCELED`.

## Traceability to Business Requirements
* **30-Day Annual Vacation Limit:** Managed by `validateBusinessRules()` in `VacationRequestControl`.
* **Cancellation Policy:** `canBeCanceled()` and `cancelRequest()` enable direct employee cancellation of future leaves with automatic notification to supervisors 2 days prior.
