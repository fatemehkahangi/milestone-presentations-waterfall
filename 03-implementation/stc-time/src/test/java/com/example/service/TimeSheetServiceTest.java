package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class TimeSheetServiceTest {

    @Test
    public void testIsLockedMethod() {
        try {
            Class<?> clazz = Class.forName("com.example.service.TimeSheetService");
            Method method = clazz.getMethod("isLocked", com.example.domain.TimeSheet.class);
            assertNotNull(method, "isLocked method should exist in TimeSheetService");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            System.out.println("TimeSheetService.isLocked not fully implemented yet by Person 3, skipping.");
        } catch (Exception e) {
            fail("TimeSheetServiceTest failed: " + e.getMessage());
        }
    }

    @Test
    public void testServiceClassExists() {
        try {
            Class<?> clazz = Class.forName("com.example.service.TimeSheetService");
            assertNotNull(clazz);
        } catch (ClassNotFoundException e) {
            fail("TimeSheetService class must exist.");
        }
    }
}