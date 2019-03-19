package com.aldebaran.robotservice;


interface IRobotService {
    String getPrivateToken();
    String getPrivateEndpoint();
    String getPublicToken();
    String getPublicEndpoint();

    // ROBOT_STATE_ABSENT = -1
    // ROBOT_STATE_DISCONNECTED = 0
    // ROBOT_STATE_CONNECTED = 1
    int getRobotState();


    boolean isFocusPreempted(String activityName);

    boolean isFocusPreemptedWithID(String activityName, String activityID);
}
