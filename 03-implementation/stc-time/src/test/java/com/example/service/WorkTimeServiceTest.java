package com.example.service; 
 
import static org.junit.jupiter.api.Assertions.*; 
import org.junit.jupiter.api.Test; 
 
class WorkTimeServiceTest { 
    @Test void noBreakUpToSixHours() { assertEquals(0, 
WorkTimeService.requiredBreakMinutes(360)); } 
    @Test void thirtyMinutesOverSixHours() { 
        assertEquals(30, WorkTimeService.requiredBreakMinutes(361)); 
        assertEquals(30, WorkTimeService.requiredBreakMinutes(540)); 
    } 
    @Test void sixtyMinutesOverNineHours() { assertEquals(60, 
WorkTimeService.requiredBreakMinutes(541)); } 
    @Test void breakValidity() { 
        assertTrue(WorkTimeService.isBreakValid(420, 30)); 
        assertFalse(WorkTimeService.isBreakValid(420, 15)); 
        assertFalse(WorkTimeService.isBreakValid(600, 30)); 
    } 
}