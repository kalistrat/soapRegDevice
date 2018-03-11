
package com;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

@WebService(endpointInterface = "com.soapDevIntegrations")
public class soapDevInteg implements soapDevIntegrations {
    @Resource
    WebServiceContext webServiceContext;


    public String linkUserDevice(String UID_CHAIN) {
        System.out.println("UID_CHAIN: " + UID_CHAIN);
        String resValue;
        MessageContext messageContext = webServiceContext.getMessageContext();

        Map<?,?> requestHeaders = (Map<?,?>) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        List<?> usernameList = (List<?>) requestHeaders.get("username");
        List<?> passwordList = (List<?>) requestHeaders.get("password");

        String username = "";
        String password = "";

        if (usernameList != null) {
            username = usernameList.get(0).toString();
        }

        if (passwordList != null) {
            password = passwordList.get(0).toString();
        }

        System.out.println("username : " + username);
        System.out.println("password : " + password);

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

