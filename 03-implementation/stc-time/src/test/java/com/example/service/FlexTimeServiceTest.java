package com.example.service; 
 
import static org.junit.jupiter.api.Assertions.*; 
import org.junit.jupiter.api.Test; 
 
class FlexTimeServiceTest { 
    @Test void insideRange() { 
        assertFalse(FlexTimeService.isOutOfRange(0)); 
        assertFalse(FlexTimeService.isOutOfRange(6000)); 
        assertFalse(FlexTimeService.isOutOfRange(-6000)); 
    } 
    @Test void outsideRange() { 
        assertTrue(FlexTimeService.isOutOfRange(6001)); 
        assertTrue(FlexTimeService.isOutOfRange(-6001)); 
    } 
}