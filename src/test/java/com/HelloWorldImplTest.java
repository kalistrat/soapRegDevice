
package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloWorldImplTest {

    @Test
    public void testSayHi() {
        soapDevInteg helloWorldImpl = new soapDevInteg();
        String response = helloWorldImpl.getDeviceInitialData("Sam");
        assertEquals("soapDevInteg not properly saying hi", "Hello Sam", response);
    }
}
