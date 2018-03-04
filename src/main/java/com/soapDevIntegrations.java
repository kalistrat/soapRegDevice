package com;

import javax.jws.WebService;

@WebService
public interface soapDevIntegrations {
    String getDeviceInitialData(String text);
    String setDeviceInitialData(String text);
}

