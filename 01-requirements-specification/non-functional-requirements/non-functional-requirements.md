#  non functional requirements

## 1. Accessibility

- Employees and supervisors shall be able to access the system remotely through the company's existing VPN connection.
- The customer already provides VPN access to the corporate network.
- After establishing a VPN connection, employees and supervisors shall be able to access the system from outside the company premises using an online connection, for example from a laptop.
- HR shall only be allowed to access employee data from within the company premises.
- If an HR user attempts to access employee data from outside the company premises, the system shall deny access.

## 2. Security

- Every user shall authenticate before accessing protected functions or employee data.
- The system shall use personal user credentials for authentication.
- After successful authentication, the system shall identify the user's role and grant access according to that role.
- The system shall distinguish between the following roles:
  - Employee
  - Supervisor
  - HR
  - Admin
- Employees and supervisors accessing the system remotely shall first connect to the company's VPN.
- HR access to employee data shall remain restricted to the company premises.
- The customer does not prescribe a specific authentication technology.



## 3. Data Storage

- Employee and time-management data shall be stored persistently on a central server.
- The centrally stored data shall include:
  - Work-time records
  - Break times
  - Vacation information
  - Sickness information
  - Flex-time balances
  - Project-related time bookings
- The stored data shall remain available after a user logs out or closes the application.
- The system shall not rely on Excel files stored locally on individual employee devices.
- All authorized users shall work with the same centrally stored data instead of maintaining separate local copies.
- The customer does not prescribe a specific cloud provider or storage technology.


## 4. Notifications

- The system shall monitor the daily working time recorded by employees.
- If an employee records more than 10 working hours in one day, the system shall automatically detect this condition.
- The responsible supervisor shall receive a notification about the excessive working time.
- HR shall not receive this notification.


## 5. Access Environment

- The system shall be accessible through an online environment.
- Employees and supervisors shall not be restricted to a workstation located inside the company premises.
- Employees and supervisors shall be able to use the system remotely from a laptop after connecting to the corporate network through VPN.
- HR access to employee data shall remain restricted to the company premises.
- Regardless of whether an authorized employee or supervisor accesses the system remotely or from the company premises, the same centrally stored data shall be used.

## 6. Technical Constraints

- The application shall operate within the customer's existing corporate network environment.
- Remote access shall use the VPN connection already provided by the customer.
- Employee and time-management data shall be stored persistently on a central server.
- Users shall authenticate before accessing protected system functions or employee data.
- The system shall support role-based access for Employee, Supervisor, HR, and Admin.
- Employees and supervisors shall be able to access the system remotely after establishing a VPN connection.
- HR access to employee data shall only be permitted from within the company premises.
- The selected technologies shall support:
  - Remote online access
  - VPN-based access to the corporate network
  - Persistent central data storage
  - Role-based access restrictions
