
package com;

import javax.jws.WebService;

@WebService(endpointInterface = "com.soapDevIntegrations")
public class soapDevInteg implements soapDevIntegrations {

    public String getDeviceInitialData(String text) {
        System.out.println("Я изменил обработчик " + text);
        return "Hello " + text;
    }

    public String setDeviceInitialData(String text) {
        System.out.println("Я установил обработчик " + text);
        return "Hello " + text;
    }
}

