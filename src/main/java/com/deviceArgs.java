package com;

/**
 * Created by kalistrat on 16.03.2018.
 */
public class deviceArgs {
    String devStatus;
    String devWsURL;
    String userLog;
    String userPassSha;

    public deviceArgs(
            String Status
            ,String WsURL
            ,String Log
            ,String PassSha
    ){
        devStatus = Status;
        devWsURL = WsURL;
        userLog = Log;
        userPassSha = PassSha;
    }
}
