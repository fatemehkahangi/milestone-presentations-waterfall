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
           for (String name : new String[] {"Website relaunch", 
"Mobile app", "Internal"}) { 
               Project p = new Project(); 
               p.setName(name); 
               projects.save(p); 
           } 
       } 
 
       private void seedSubmittedSheet() { 
        User alex = users.findByEmail("alex@stc.de").orElse(null);
        if (alex == null) {
            return;
        }

        YearMonth prevMonth = YearMonth.now().minusMonths(1);

        TimeSheet sheet = new TimeSheet();
        sheet.setEmployee(alex);
        sheet.setYear(prevMonth.getYear());
        sheet.setMonth(prevMonth.getMonthValue());
        sheet.setStatus(TimeSheetStatus.SUBMITTED);
        sheets.save(sheet);

        List<Project> allProjects = projects.findAll();
        Project project = allProjects.isEmpty() ? null : allProjects.get(0);

        LocalDate startDate = prevMonth.atDay(1);
        for (int i = 0; i < 5; i++) {
            TimeEntry entry = new TimeEntry();
            entry.setTimeSheet(sheet);
            entry.setDate(startDate.plusDays(i));
            entry.setBeginTime(LocalTime.of(9, 0));
            entry.setEndTime(LocalTime.of(17, 30));
            entry.setBreakMinutes(30);
            entry.setProject(project);
            entry.setComment("Sample entry " + (i + 1));
            entries.save(entry);
        }
} 
} 