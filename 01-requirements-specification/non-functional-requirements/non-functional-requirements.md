#  non functional requirements

## 1. Accessibility

- Employees shall be able to access the system from outside the company premises.
- Supervisors shall be able to access the system from outside the company premises.
- HR shall only be able to access employee data from the company premises.
- The system shall be accessible online, for example from a laptop.

## 2. Security

- The system shall require user authentication before access is granted.
- Access permissions shall depend on the user's role: Employee, Supervisor, or HR.
- The specific authentication method is not prescribed by the customer and may be selected by the development team.

## 3. Data Storage

- Employee and time-management data shall be stored persistently on a central server.
- The specific cloud or storage technology is not prescribed by the customer and may be selected by the development team.

## 4. Notification

- If an employee works more than 10 hours in one day, the supervisor shall receive a notification.
- HR shall not receive a notification for this event.
- The notification may be provided by email or through the system interface.

## 5. Portability / Access Environment

- The system shall not be restricted to a single physical workplace for employees and supervisors.
- Employees and supervisors shall be able to use the system through an online environment.

## 6. Technical Constraints

- The exact cloud provider or storage technology is not specified by the customer.
- The exact authentication technology is not specified by the customer.
- These technology decisions shall be made by the development team.
