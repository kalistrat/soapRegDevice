package com;

import javax.jws.WebService;

@WebService
public interface soapDevIntegrations {
    String linkUserDevice(String UID_CHAIN);
    String setUserDevice(String UID, String USER_DB_HOST);
}

