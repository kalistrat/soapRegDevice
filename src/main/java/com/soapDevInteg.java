
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
        try {

            String linkResultValue;

            List<String> uidLilst = requestExecutionMethods.GetListFromString(UID_CHAIN,"|");

            if (uidLilst.size()!=0) {
                String notFoundDevices = "";
                for (String iUID : uidLilst){
                    if (requestExecutionMethods.fisUIDExists(iUID) == 0){
                        notFoundDevices = notFoundDevices + iUID + ";";
                    }
                }
                if (notFoundDevices.equals("")) {

                    String bindingDeviceUID = uidLilst.get(uidLilst.size()-1);
                    deviceArgs dArgs = requestExecutionMethods.getDeviceArgs(bindingDeviceUID);

                    if (dArgs.devStatus.equals("AWAINTING")
                            && dArgs.devWsURL != null
                            && dArgs.userLog != null
                            && dArgs.userPassSha != null
                            ){
                        linkResultValue = requestExecutionMethods.userWsLinkDevice(UID_CHAIN,dArgs);
                    } else {
                        linkResultValue = "DEVICE_"+bindingDeviceUID+"_IS_NOT_ADDED_TO_THE_PERSONAL_ACCOUNT";
                    }

                } else {
                    linkResultValue = "NOT_FOUND_DEVICES: " + notFoundDevices;
                }
            } else {
                linkResultValue = "CANT_PARSE_CHAIN";
            }

            return linkResultValue;
        } catch (Exception e){
            e.printStackTrace();
            return "EXECUTION_ERROR";
        }

    }

    public String setUserDevice(String UID, String userLogin, String userPasswordSha) {
        try {

            String setResultValue;
            //String userPasswordSha = requestExecutionMethods.sha256(userPassword);
            String dbPasswordSha = requestExecutionMethods.fGetUserPassSha(userLogin);

            if (dbPasswordSha != null) {
                if (dbPasswordSha.equals(userPasswordSha)) {
                    if (requestExecutionMethods.fisUIDExists(UID) == 1) {
                        requestExecutionMethods.updateSoldDeviceStatus(UID, userLogin, "AWAINTING");
                        setResultValue = "DEVICE_" + UID + "_TRANSFERRED_TO_AWAINTING_STATE";
                    } else {
                        setResultValue = "DEVICE_NOT_FOUND";
                    }
                } else {
                    setResultValue = "WRONG_LOGIN_PASSWORD";
                }
            } else {
                setResultValue = "WRONG_LOGIN_PASSWORD";
            }

            return setResultValue;
        } catch (Exception e){
            e.printStackTrace();
            return "EXECUTION_ERROR";
        }
    }

    public String checkUserDevice(String UID, String userLogin, String userPasswordSha) {

        try {

            String setResultValue;
            String dbPasswordSha = requestExecutionMethods.fGetUserPassSha(userLogin);


            if (dbPasswordSha != null) {
                if (dbPasswordSha.equals(userPasswordSha)) {
                    if (requestExecutionMethods.fisUIDExists(UID) == 1) {
                        setResultValue = "DEVICE_EXISTS";
                    } else {
                        setResultValue = "DEVICE_NOT_FOUND";
                    }
                } else {
                    setResultValue = "WRONG_LOGIN_PASSWORD";
                }
            } else {
                setResultValue = "WRONG_LOGIN_PASSWORD";
            }

            return setResultValue;
        } catch (Exception e){
            e.printStackTrace();
            return "EXECUTION_ERROR";
        }
    }
}

