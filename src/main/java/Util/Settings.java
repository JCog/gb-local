package Util;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Settings {
    private static final String INI_FILENAME = "settings.ini";
    private static final int REFRESH_RATE = 5; //minutes
    
    private static final String GENERAL_CAT_TAG = "general";
    private static final String DB_CAT_TAG = "database";
    private static final String TWITCH_CAT_TAG = "twitch";
    private static final String SUB_COUNT_CAT_TAG = "subPointUpdater";
    
    private final Wini ini = getIni();
    
    private LocalDateTime lastRefreshed = LocalDateTime.now();
    
    public boolean hasWritePermission() {
        return get(GENERAL_CAT_TAG, "writePermission", boolean.class);
    }
    
    public String getDbHost() {
        return get(DB_CAT_TAG, "host");
    }
    
    public int getDbPort() {
        return get(DB_CAT_TAG, "port", int.class);
    }
    
    public String getDbUser() {
        return get(DB_CAT_TAG, "user");
    }
    
    public String getDbPassword() {
        return get(DB_CAT_TAG, "password");
    }
    
    //streamer username in all lowercase
    public String getTwitchStream() {
        return get(TWITCH_CAT_TAG, "stream");
    }
    
    public String getTwitchChannelAuthToken() {
        return get(TWITCH_CAT_TAG, "channelAuthToken");
    }
    
    public String getTwitchChannelClientId() {
        return get(TWITCH_CAT_TAG, "channelClientId");
    }
    
    public String getSubCountFormat() {
        return get(SUB_COUNT_CAT_TAG, "format");
    }
    
    public int getSubCountOffset() {
        return get(SUB_COUNT_CAT_TAG, "offset", int.class);
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
    
    private void refreshIni() {
        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.MINUTES.between(lastRefreshed, now) >= REFRESH_RATE) {
            try {
                if (ini != null) {
                    ini.load();
                    lastRefreshed = now;
                }
            } catch (IOException e) {
                System.out.println("ERROR: unable to refresh ini");
            }
        }
    }
    
    private String get(Object sectionName, Object optionName) {
        refreshIni();
        return ini.get(sectionName, optionName);
    }
    
    private <T> T get(Object sectionName, Object optionName, Class<T> clazz) {
        refreshIni();
        return ini.get(sectionName, optionName, clazz);
    }
}
