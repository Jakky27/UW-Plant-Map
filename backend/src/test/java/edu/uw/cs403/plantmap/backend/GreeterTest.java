package edu.uw.cs403.plantmap.backend;

import org.junit.Test;

import static org.junit.Assert.*;

public class GreeterTest {
    @Test
    public void testGetGreeting() {
        Greeter greeter = new Greeter();

        assertEquals("Oops, I failed.", greeter.getGreeting());
    }
}
