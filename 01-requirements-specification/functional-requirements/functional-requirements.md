# Functional requirements

## Work time

- The system shall allow an employee to record begin time, end time and break time for each working day.
- The system shall require no break for work times under 6 hours, a 30-minute break for more than 6 hours and a 1-hour break for more than 9 hours.
- The system shall allow an employee to take flex time off, either for a whole day or for fewer than 8 working hours on a day.
- The system shall allow an employee to book working time on customer projects.
- The system shall allow any employee to create a project; project names shall be unique.
- The system shall allow an employee to submit the time sheet of a month to the supervisor; after submission the times cannot be changed.
- The system shall inform the supervisor when a time sheet is submitted.
- The system shall allow the supervisor to approve a monthly time sheet or reject it with a reason.
- The system shall inform the employee of a rejection and allow the employee to change the times and submit again.
- The system shall update the flex time account only after the supervisor has approved the monthly time sheet.
- The system shall warn when the flex time account is outside the range of -100 to +100 hours (soft rule, no blocking).
- The system shall notify the supervisor by email if an employee works more than 10 hours on a day.

## Vacation

- The system shall allow an employee to request vacation with a start and end date; vacation cannot be added without a request.
- The system shall allow the supervisor to approve a vacation request or reject it with a reason, and shall inform the employee and HR.
- The system shall manage a vacation account of 30 days per year per employee and show the days as open, planned and taken.
- The system shall allow an employee to delete a planned vacation (an open request or an approved future vacation) and shall notify the supervisor.
- The system shall remind an employee of unused vacation days that can be taken in the first 3 months of the next year.

## Sickness

- The system shall allow HR to register sickness for an employee; no approval is needed.
- The system shall notify the supervisor of a sick employee by email.

## Statistics and overview

- The system shall show an employee the flex time account, the vacation days of the year (open, planned, taken) and the working time of the current month.
- The system shall allow an employee to flip through the months and view the work time details.
- The system shall show a supervisor the flex time state and the vacation days of each of his or her employees.
- The system shall show HR the flex time state and the vacation days of every employee.
- The system shall show HR which employees work too much (between 8 and 10 hours, more than 10 hours on a day). (To be confirmed, see conflict above)

## Users

- The system shall provide an admin role to manage users.
