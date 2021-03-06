// by: Martin O'Hanlon
// from: https://github.com/martinohanlon/BlueDot/blob/master/clients/android/app/src/main/java/com/stuffaboutcode/bluedot/Constants.java

package com.harlie.batbot.service;

/**
 * Defines several constants used by {@link BluetoothChatService}
 */
public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int IMAGE_READ = 6;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static final String DATA = "data";
    public static final String SIZE = "size";
    public static final String INITIALIZING = "initializing..";
    public static final String DISCONNECT = "disconnect";
    public static final String CONNECTION_FAILED = "connection failed";
    public static final String CONNECTION_LOST = "connection lost";

    public static final float BORDER_THICKNESS = (float)0.025;

    public static final String PROTOCOL_VERSION = "1";
    public static final String CLIENT_NAME = "BatBot Android app";
}
