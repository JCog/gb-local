package Functions;

import Util.FileWriter;
import Util.Settings;
import com.github.twitch4j.helix.domain.Subscription;
import com.github.twitch4j.helix.domain.User;
import com.jcog.utils.TwitchApi;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.out;

public class SubPointUpdater {
    private static final String LOCAL_SUB_POINTS_FILE_LOCATION = "output/";
    private static final String LOCAL_SUB_POINTS_FILENAME = "sub_points.txt";
    private static final int INTERVAL = 60 * 1000;
    private static final int TIER_2_MULTIPLIER = 2;
    private static final int TIER_3_MULTIPLIER = 6;
    
    private final Timer timer = new Timer();
    private final String displayFormat = Settings.getSubCountFormat();
    private final int offset = Settings.getSubCountOffset();
    private final User streamerUser;
    private final TwitchApi twitchApi;
    
    private int subPoints;
    
    //TBH this whole class is almost never exactly right, but that's only because Twitch's sub API sucks :/
    public SubPointUpdater(TwitchApi twitchApi, User streamerUser) {
        this.twitchApi = twitchApi;
        this.streamerUser = streamerUser;
        subPoints = 0;
    }
    
    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateSubTierCounts();
                outputSubPointsFile();
            }
        }, 0, INTERVAL);
    }
    
    public void stop() {
        timer.cancel();
    }
    
    private void updateSubTierCounts() {
        int tier1 = 0;
        int tier2 = 0;
        int tier3 = 0;
    
        List<Subscription> subList;
        try {
            subList = twitchApi.getSubList(streamerUser.getId());
        } catch (HystrixRuntimeException e) {
            e.printStackTrace();
            out.println("Failed to get sub list. Will try again at next interval");
            return;
        }
    
        for (Subscription sub : subList) {
            //idk wtf is up with twitch's api, but sometimes the number is just wrong ¯\_(ツ)_/¯
            if (!sub.getUserId().equals(streamerUser.getId())) {
                switch (sub.getTier()) {
                    case "1000":
                        tier1++;
                        break;
                    case "2000":
                        tier2++;
                        break;
                    case "3000":
                        tier3++;
                        break;
                }
            }
        }
        
        subPoints = tier1 + (tier2 * TIER_2_MULTIPLIER) + (tier3 * TIER_3_MULTIPLIER) + offset;
    }
    
    private void outputSubPointsFile() {
        FileWriter.writeToFile(
                LOCAL_SUB_POINTS_FILE_LOCATION,
                LOCAL_SUB_POINTS_FILENAME,
                String.format(displayFormat, subPoints)
        );
    }
}
