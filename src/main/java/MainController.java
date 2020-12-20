import Functions.BitWarUpdater;
import Functions.PubSub;
import Functions.SubPointUpdater;
import Util.ConsoleCommandListener;
import Util.Settings;
import com.github.twitch4j.helix.domain.User;
import com.jcog.utils.TwitchApi;
import com.jcog.utils.database.DbManager;

import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.System.out;

public class MainController {
    private static final String DB_NAME = "goombotio";
    private static final int TIMER_THREAD_SIZE = 2;
    
    private final Settings settings = new Settings();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(TIMER_THREAD_SIZE);
    private final DbManager dbManager = new DbManager(
            settings.getDbHost(),
            settings.getDbPort(),
            DB_NAME,
            settings.getDbUser(),
            settings.getDbPassword(),
            settings.hasWritePermission()
    );
    private final TwitchApi twitchApi = new TwitchApi(
            settings.getTwitchStream(),
            settings.getTwitchChannelAuthToken(),
            settings.getTwitchChannelClientId()
    );
    private final User streamerUser = twitchApi.getUserByUsername(settings.getTwitchStream());
    private final SubPointUpdater subPointUpdater = new SubPointUpdater(twitchApi, streamerUser, settings);
    private final BitWarUpdater bitWarUpdater = new BitWarUpdater(scheduler, dbManager);
    private final PubSub pubSub = (PubSub) new PubSub(bitWarUpdater, streamerUser.getId(), settings.getTwitchChannelAuthToken())
            .listenForBits()
            .listenForChannelPoints()
            .listenForSubGifts();
    
    public synchronized void run() {
        subPointUpdater.start();
        bitWarUpdater.startDbSync();
    
        out.println("gb-local is ready.");
    
        //main loop
        try {
            new ConsoleCommandListener().run();
        }
        catch (NoSuchElementException nsee) {
            out.println("No console detected. Process must be killed manually");
            try {
                this.wait();
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
    
    public void closeAll() {
        subPointUpdater.stop();
        pubSub.close();
    }
}
