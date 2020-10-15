package Functions;

import Util.FileWriter;
import com.github.twitch4j.pubsub.events.ChannelBitsEvent;
import com.jcog.utils.database.DbManager;
import com.jcog.utils.database.misc.BitWarDb;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BitWarUpdater {
    private static final String OUTPUT_FILE_LOCATION = "output/";
    private static final String SAVE_YOSHI_FILENAME = "save_yoshi.txt";
    private static final String KILL_YOSHI_FILENAME = "kill_yoshi.txt";
    private static final String BIT_WAR_NAME = "save_kill_yoshi";
    private static final String TEAM_KILL = "team_kill";
    private static final String TEAM_SAVE = "team_save";
    private static final String SAVE_KEYWORD = "save";
    private static final String KILL_KEYWORD = "kill";
    private static final int INTERVAL = 30; // seconds
    
    private final ScheduledExecutorService scheduler;
    private final BitWarDb bitWarDb;
    
    private ScheduledFuture<?> scheduledFuture;
    private int savePoints;
    private int killPoints;
    
    public BitWarUpdater(ScheduledExecutorService scheduler, DbManager dbManager) {
        this.scheduler = scheduler;
        bitWarDb = dbManager.getBitWarDb();
        savePoints = bitWarDb.getCurrentAmount(BIT_WAR_NAME, TEAM_SAVE);
        killPoints = bitWarDb.getCurrentAmount(BIT_WAR_NAME, TEAM_KILL);
        outputSaveYoshiFile();
        outputKillYoshiFile();
    }
    
    public void parseBitEvent(ChannelBitsEvent bitsEvent) {
        String message = bitsEvent.getData().getChatMessage().toLowerCase();
        int amount = bitsEvent.getData().getBitsUsed();
        if (message.contains(SAVE_KEYWORD)) {
            savePoints += amount;
            outputSaveYoshiFile();
        }
        else if (message.contains(KILL_KEYWORD)) {
            killPoints += amount;
            outputKillYoshiFile();
        }
    }
    
    public void startDbSync() {
        scheduledFuture = scheduler.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int newSavePoints = bitWarDb.getCurrentAmount(BIT_WAR_NAME, TEAM_SAVE);
                int newKillPoints = bitWarDb.getCurrentAmount(BIT_WAR_NAME, TEAM_KILL);
                
                if (newSavePoints != savePoints) {
                    savePoints = newSavePoints;
                    outputSaveYoshiFile();
                }
    
                if (newKillPoints != killPoints) {
                    killPoints = newKillPoints;
                    outputKillYoshiFile();
                }
            }
        }, INTERVAL, INTERVAL, TimeUnit.SECONDS);
    }
    
    private void outputSaveYoshiFile() {
        FileWriter.writeToFile(
                OUTPUT_FILE_LOCATION,
                SAVE_YOSHI_FILENAME,
                String.valueOf(savePoints)
        );
    }
    
    private void outputKillYoshiFile() {
        FileWriter.writeToFile(
                OUTPUT_FILE_LOCATION,
                KILL_YOSHI_FILENAME,
                String.valueOf(killPoints)
        );
    }
}
