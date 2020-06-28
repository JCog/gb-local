package Util;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class Settings {
    private static final String INI_FILENAME = "settings.ini";
    
    /////////////////////  TAGS  /////////////////////
    
    private static final String TWITCH_CAT_TAG = "twitch";
    private static final String TWITCH_STREAM_TAG = "stream";
    private static final String TWITCH_CHANNEL_AUTH_TOKEN_TAG = "channelAuthToken";
    private static final String TWITCH_CHANNEL_CLIENT_ID_TAG = "channelClientId";
    
    private static final String SUB_COUNT_CAT_TAG = "subPointUpdater";
    private static final String SUB_COUNT_FORMAT_TAG = "format";
    private static final String SUB_COUNT_OFFSET_TAG = "offset";
    
    /////////////////////  VARS  /////////////////////
    
    private static String TWITCH_STREAM;
    private static String TWITCH_CHANNEL_AUTH_TOKEN;
    private static String TWITCH_CHANNEL_CLIENT_ID;
    
    private static String SUB_COUNT_FORMAT;
    private static int SUB_COUNT_OFFSET;
    
    //////////////////////////////////////////////////
    public static void init() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Wini ini;
        try {
            ini = new Wini(new File(INI_FILENAME));
        }
        catch (IOException e) {
            System.out.println("IOException reading ini, exiting");
            System.exit(1);
            return;
        }
    
        TWITCH_STREAM = ini.get(TWITCH_CAT_TAG, TWITCH_STREAM_TAG);
        TWITCH_CHANNEL_AUTH_TOKEN = ini.get(TWITCH_CAT_TAG, TWITCH_CHANNEL_AUTH_TOKEN_TAG);
        TWITCH_CHANNEL_CLIENT_ID = ini.get(TWITCH_CAT_TAG, TWITCH_CHANNEL_CLIENT_ID_TAG);
    
        SUB_COUNT_FORMAT = ini.get(SUB_COUNT_CAT_TAG, SUB_COUNT_FORMAT_TAG);
        SUB_COUNT_OFFSET = ini.get(SUB_COUNT_CAT_TAG, SUB_COUNT_OFFSET_TAG, int.class);
    }
    
    //streamer username in all lowercase
    public static String getTwitchStream() {
        return TWITCH_STREAM;
    }
    
    public static String getTwitchChannelAuthToken() {
        return TWITCH_CHANNEL_AUTH_TOKEN;
    }
    
    public static String getTwitchChannelClientId() {
        return TWITCH_CHANNEL_CLIENT_ID;
    }
    
    public static String getSubCountFormat() {
        return SUB_COUNT_FORMAT;
    }
    
    public static int getSubCountOffset() {
        return SUB_COUNT_OFFSET;
    }
}