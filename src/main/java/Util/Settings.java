package Util;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class Settings {
    private static final String INI_FILENAME = "settings.ini";
    
    private static final String GENERAL_CAT_TAG = "general";
    private static final String DB_CAT_TAG = "database";
    private static final String TWITCH_CAT_TAG = "twitch";
    private static final String SUB_COUNT_CAT_TAG = "subPointUpdater";
    
    private final Wini ini = getIni();
    
    public boolean hasWritePermission() {
        return ini.get(GENERAL_CAT_TAG, "writePermission", boolean.class);
    }
    
    public String getDbHost() {
        return ini.get(DB_CAT_TAG, "host");
    }
    
    public int getDbPort() {
        return ini.get(DB_CAT_TAG, "port", int.class);
    }
    
    public String getDbUser() {
        return ini.get(DB_CAT_TAG, "user");
    }
    
    public String getDbPassword() {
        return ini.get(DB_CAT_TAG, "password");
    }
    
    //streamer username in all lowercase
    public String getTwitchStream() {
        return ini.get(TWITCH_CAT_TAG, "stream");
    }
    
    public String getTwitchChannelAuthToken() {
        return ini.get(TWITCH_CAT_TAG, "channelAuthToken");
    }
    
    public String getTwitchChannelClientId() {
        return ini.get(TWITCH_CAT_TAG, "channelClientId");
    }
    
    public String getSubCountFormat() {
        return ini.get(SUB_COUNT_CAT_TAG, "format");
    }
    
    public int getSubCountOffset() {
        return ini.get(SUB_COUNT_CAT_TAG, "offset", int.class);
    }
    
    private Wini getIni() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Wini ini;
        try {
            ini = new Wini(new File(INI_FILENAME));
        }
        catch (IOException e) {
            System.out.println("IOException reading ini");
            return null;
        }
        return ini;
    }
}
