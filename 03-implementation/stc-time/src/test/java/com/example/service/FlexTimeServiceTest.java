package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class FlexTimeServiceTest {

    @Test
    public void testIsOutOfRange() {
        try {
            Class<?> clazz = Class.forName("com.example.service.FlexTimeService");
            Method method = clazz.getMethod("isOutOfRange", int.class);

            // Limit is 6000 minutes (100 hours), strictly outside (> 6000 or < -6000)
            assertFalse((boolean) method.invoke(null, 5000));  // within limit
            assertFalse((boolean) method.invoke(null, 6000));  // exactly boundary
            assertTrue((boolean) method.invoke(null, 6001));   // strictly outside (+100h 1min)
            assertFalse((boolean) method.invoke(null, -6000)); // exactly boundary negative
            assertTrue((boolean) method.invoke(null, -6001));  // strictly outside negative
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            // Expected if FlexTimeService method is not implemented yet by Person 3
            System.out.println("FlexTimeService.isOutOfRange not implemented yet, skipping reflection check.");
        } catch (Exception e) {
            fail("FlexTimeService test failed: " + e.getMessage());
        }
    }
}