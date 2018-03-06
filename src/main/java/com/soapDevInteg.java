
package com;

import javax.jws.WebService;

@WebService(endpointInterface = "com.soapDevIntegrations")
public class soapDevInteg implements soapDevIntegrations {

    public String linkUserDevice(String UID_CHAIN) {
        System.out.println("UID_CHAIN: " + UID_CHAIN);
        String resValue;
        if (UID_CHAIN.equals("TEST_CHAIN")) {
            resValue = "mqttLogin:qwerty1;mqttPassword:hfey67fe;mqttTopic:/SEN-002;";
        } else {
            resValue = "DEVICE_NOT_FOUND;";
        }
        return resValue;
    }

    public String setUserDevice(String UID, String USER_DB_HOST) {
        System.out.println("UID: " + UID);
        System.out.println("USER_DB_HOST: " + USER_DB_HOST);
        return "The device with the UID: " + UID + " was successfully added to the buffer";
    }
}

