package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class WorkTimeServiceTest {

    @Test
    public void testRequiredBreakMinutes() {
        try {
            Class<?> clazz = Class.forName("com.example.service.WorkTimeService");
            Method method = clazz.getMethod("requiredBreakMinutes", int.class);

            assertEquals(0, (int) method.invoke(null, 300));
            assertEquals(0, (int) method.invoke(null, 360));
            assertEquals(30, (int) method.invoke(null, 361));
            assertEquals(30, (int) method.invoke(null, 540));
            assertEquals(60, (int) method.invoke(null, 541));
        } catch (Exception e) {
            fail("WorkTimeService method not found or failed: " + e.getMessage());
        }
    }
}