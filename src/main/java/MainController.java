import Functions.SubPointUpdater;
import Util.ConsoleCommandListener;
import Util.Settings;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.User;
import com.jcog.utils.TwitchApi;

import java.util.NoSuchElementException;

import static java.lang.System.out;

public class MainController {

    private final TwitchApi twitchApi = new TwitchApi(
            Settings.getTwitchStream(),
            Settings.getTwitchChannelAuthToken(),
            Settings.getTwitchChannelClientId()
    );
    private final User streamerUser = twitchApi.getUserByUsername(Settings.getTwitchStream());
    private final SubPointUpdater subPointUpdater = new SubPointUpdater(twitchApi, streamerUser);
    
    public synchronized void run() {
        subPointUpdater.start();
    
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
        twitchApi.close();
    }
}
