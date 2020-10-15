package Functions;

import com.github.twitch4j.pubsub.events.ChannelBitsEvent;
import com.github.twitch4j.pubsub.events.ChannelSubGiftEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.jcog.utils.TwitchPubSubClient;

public class PubSub extends TwitchPubSubClient {
    
    private final BitWarUpdater bitWarUpdater;
    public PubSub(BitWarUpdater bitWarUpdater, String streamerId, String authToken) {
        super(streamerId, authToken);
        this.bitWarUpdater = bitWarUpdater;
    }
    
    @Override
    public void onBitsEvent(ChannelBitsEvent event) {
        bitWarUpdater.parseBitEvent(event);
    }
    
    @Override
    public void onChannelPointsEvent(RewardRedeemedEvent event) {
        System.out.println(event.toString());
        System.out.println();
    }
    
    @Override
    public void onSubGiftsEvent(ChannelSubGiftEvent event) {
        System.out.println(event.toString());
        System.out.println();
    }
}
