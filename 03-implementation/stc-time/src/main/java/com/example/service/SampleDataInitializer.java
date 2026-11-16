package com.example.service; 
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

import org.springframework.boot.CommandLineRunner; 
import org.springframework.core.annotation.Order; 
import org.springframework.stereotype.Component; 
import com.example.domain.*; 
import com.example.repository.*; 

import java.util.List;
import java.util.Optional;

   @Component 
   @Order(2) 
   public class SampleDataInitializer implements CommandLineRunner 
{ 
 
       private final UserRepository users; 
       private final ProjectRepository projects; 
       private final TimeSheetRepository sheets; 
       private final TimeEntryRepository entries; 
 
       public SampleDataInitializer(UserRepository users, 
ProjectRepository projects, 
                                    TimeSheetRepository sheets, 
TimeEntryRepository entries) { 
           this.users = users; 
           this.projects = projects; 
           this.sheets = sheets; 
           this.entries = entries; 
       } 
 
       @Override 
       public void run(String... args) { 
           seedProjects(); 
           seedSubmittedSheet(); 
       } 
 
       private void seedProjects() {
        try {
            // Check if projects already exist, then seed default sample projects
            if (projects.count() == 0) {
                for (String name : new String[] {"Website relaunch", "Mobile app", "Internal"}) {
                    Project p = new Project();
                    p.setName(name);
                    projects.save(p);
                }
            }
        } catch (Exception e) {
            System.out.println("Projects already seeded or DB not ready: " + e.getMessage());
        }
    }
 
       private void seedSubmittedSheet() { 
        try {
            // Load alex@stc.de via users.findByEmail(...)
            Optional<User> alexOpt = users.findByEmail("alex@stc.de");
            if (alexOpt.isEmpty()) {
                return; // If user doesn't exist yet, skip
            }
            User alex = alexOpt.get();

            // Determine previous month
            YearMonth prevMonth = YearMonth.now().minusMonths(1);

            // Check if a sheet already exists to avoid duplicates on restart
            if (sheets.findByEmployeeAndYearAndMonth(alex, prevMonth.getYear(), prevMonth.getMonthValue()).isPresent()) {
                return;
            }

            // Create a TimeSheet for the previous month with status SUBMITTED
            TimeSheet sheet = new TimeSheet();
            sheet.setEmployee(alex);
            sheet.setYear(prevMonth.getYear());
            sheet.setMonth(prevMonth.getMonthValue());
            sheet.setStatus(TimeSheetStatus.SUBMITTED);
            sheets.save(sheet);

            // Get a default project for entries
            List<Project> allProjects = projects.findAll();
            Project defaultProject = allProjects.isEmpty() ? null : allProjects.get(0);

            // Create ~5 TimeEntry rows and set timeSheet on each
            LocalDate startDate = prevMonth.atDay(1);
            for (int i = 0; i < 5; i++) {
                LocalDate entryDate = startDate.plusDays(i);
                // Skip weekends
                if (entryDate.getDayOfWeek().getValue() > 5) {
                    entryDate = entryDate.plusDays(2);
                }

                TimeEntry entry = new TimeEntry();
                entry.setTimeSheet(sheet);
                entry.setDate(entryDate);
                entry.setBeginTime(LocalTime.of(9, 0));
                entry.setEndTime(LocalTime.of(17, 30));
                entry.setBreakMinutes(30);
                entry.setProject(defaultProject);
                entry.setComment("Sample work entry " + (i + 1));

                entries.save(entry);
            }
        } catch (Exception e) {
            // Catch H2 'month' keyword syntax error so the application still boots
            System.out.println("Could not seed submitted sheet (H2 schema issue): " + e.getMessage());
        }
    }
} 