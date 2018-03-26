
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
                    if (requestExecutionMethods.getUIDStatus(iUID).equals("")){
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
                        //System.out.println("linkResultValue from userWs: " + linkResultValue);
                        if (!linkResultValue.contains("ERROR")) {
                            //System.out.println("go to connected");
                            requestExecutionMethods.updateSoldDeviceStatus(bindingDeviceUID, dArgs.userLog, "CONNECTED");
                        }
                    } else if (dArgs.devStatus.equals("CONNECTED")){
                        linkResultValue = "ERROR_DEVICE_"+bindingDeviceUID+"_IS_ALREADY_IN_USE";
                    } else {
                        linkResultValue = "ERROR_DEVICE_"+bindingDeviceUID+"_NOT_IN_PERSONAL_ACCOUNT";
                    }

                } else {
                    linkResultValue = "ERROR_NOT_FOUND_DEVICES: " + notFoundDevices;
                }
            } else {
                linkResultValue = "ERROR_CANT_PARSE_CHAIN";
            }

            return linkResultValue;
        } catch (Exception e){
            e.printStackTrace();
            return "ERROR_EXECUTION_FAILED";
        }

    }

    public String setUserDevice(String UID, String userLogin, String userPasswordSha, String reqStatus) {
        try {

            String setResultValue;
            //String userPasswordSha = requestExecutionMethods.sha256(userPassword);
            String dbPasswordSha = requestExecutionMethods.fGetUserPassSha(userLogin);

            if (dbPasswordSha != null) {
                if (dbPasswordSha.equals(userPasswordSha)) {
                    // check login and device owner's login
                    String uidStatus = requestExecutionMethods.getUIDStatus(UID);
                    if (uidStatus.equals("OUTSIDE") && reqStatus.equals("AWAINTING")) {

                        requestExecutionMethods.updateSoldDeviceStatus(UID, userLogin, "AWAINTING");
                        setResultValue = "DEVICE_TRANSFERRED_TO_AWAITING_STATUS";

                    } else if(uidStatus.equals("CONNECTED")&& reqStatus.equals("OUTSIDE")) {

                        requestExecutionMethods.updateSoldDeviceStatus(UID, userLogin, "OUTSIDE");
                        setResultValue = "DEVICE_TRANSFERRED_TO_OUTSIDE_STATUS";

                    } else if(uidStatus.equals("AWAINTING")&& reqStatus.equals("OUTSIDE")) {

                        requestExecutionMethods.updateSoldDeviceStatus(UID, userLogin, "OUTSIDE");
                        setResultValue = "DEVICE_TRANSFERRED_TO_OUTSIDE_STATUS";

                    } else {
                        setResultValue = "WRONG_DEVICE_TRANSITION";
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
                    String uidStatus = requestExecutionMethods.getUIDStatus(UID);
                    if (uidStatus.equals("OUTSIDE")) {
                        setResultValue = "DEVICE_EXISTS_AND_OUTSIDE";
                    } else if(uidStatus.equals("AWAINTING")) {
                        setResultValue = "DEVICE_EXISTS_AND_WAITING";
                    } else if(uidStatus.equals("CONNECTED")) {
                        setResultValue = "DEVICE_EXISTS_AND_CONNECTED";
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

