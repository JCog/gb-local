package Util;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.*;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import java.util.*;

public class TwitchApi {
    private final TwitchClient twitchClient;
    private final String streamer;
    
    public TwitchApi(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
        streamer = Settings.getTwitchStream();
    }
    
    public Clip getClipById(String id) throws HystrixRuntimeException {
        ClipList clipList = twitchClient.getHelix().getClips(
                Settings.getTwitchChannelAuthToken(),
                null,
                null,
                id,
                null,
                null,
                1,
                null,
                null
        ).execute();
        if (clipList.getData().isEmpty()) {
            return null;
        }
        return clipList.getData().get(0);
    }
    
    public Follow getFollow(String fromId, String toId) throws HystrixRuntimeException {
        FollowList followList = twitchClient.getHelix().getFollowers(
                Settings.getTwitchChannelAuthToken(),
                fromId,
                toId,
                null,
                1
        ).execute();
        if (followList.getFollows().isEmpty()) {
            return null;
        }
        return followList.getFollows().get(0);
    }
    
    public int getFollowerCount(String userId) throws HystrixRuntimeException {
        FollowList followList = twitchClient.getHelix().getFollowers(
                Settings.getTwitchChannelAuthToken(),
                null,
                userId,
                null,
                null
        ).execute();
        return followList.getTotal();
    }
    
    public List<Follow> getFollowers(String userId) throws HystrixRuntimeException {
        FollowList followList = twitchClient.getHelix().getFollowers(
                Settings.getTwitchChannelAuthToken(),
                null,
                userId,
                null,
                100
        ).execute();
        
        List<Follow> followsOutput = new ArrayList<>(followList.getFollows());
        
        while (!followList.getFollows().isEmpty()) {
            followList = twitchClient.getHelix().getFollowers(
                    Settings.getTwitchChannelAuthToken(),
                    null,
                    userId,
                    followList.getPagination().getCursor(),
                    100
            ).execute();
            followsOutput.addAll(followList.getFollows());
        }
        return followsOutput;
    }
    
    public Game getGameById(String gameId) throws HystrixRuntimeException {
        GameList gameList = twitchClient.getHelix().getGames(
                Settings.getTwitchChannelAuthToken(),
                Collections.singletonList(gameId),
                null
        ).execute();
        if (gameList.getGames().isEmpty()) {
            return null;
        }
        return gameList.getGames().get(0);
    }
    
    public Stream getStream() throws HystrixRuntimeException {
        StreamList streamList = twitchClient.getHelix().getStreams(
                Settings.getTwitchChannelAuthToken(),
                "",
                "",
                1,
                null,
                null,
                null,
                null,
                Collections.singletonList(streamer)).execute();
        if (streamList.getStreams().isEmpty()) {
            return null;
        }
        return streamList.getStreams().get(0);
    }
    
    public List<Subscription> getSubList(String userId) throws HystrixRuntimeException {
        SubscriptionList subscriptionList = twitchClient.getHelix().getSubscriptions(
                Settings.getTwitchChannelAuthToken(),
                userId,
                null,
                null,
                100
        ).execute();
    
        List<Subscription> subscriptionsOutput = new ArrayList<>(subscriptionList.getSubscriptions());
    
        while (!subscriptionList.getSubscriptions().isEmpty()) {
            subscriptionList = twitchClient.getHelix().getSubscriptions(
                    Settings.getTwitchChannelAuthToken(),
                    userId,
                    subscriptionList.getPagination().getCursor(),
                    null,
                    100
            ).execute();
            subscriptionsOutput.addAll(subscriptionList.getSubscriptions());
        }
        return subscriptionsOutput;
    }
    
    public User getUserById(String userId) throws HystrixRuntimeException {
        UserList userList = twitchClient.getHelix().getUsers(
                Settings.getTwitchChannelAuthToken(),
                Collections.singletonList(userId),
                null
        ).execute();
        if (userList.getUsers().isEmpty()) {
            return null;
        }
        return userList.getUsers().get(0);
    }
    
    public User getUserByUsername(String username) throws HystrixRuntimeException {
        UserList userList = twitchClient.getHelix().getUsers(
                Settings.getTwitchChannelAuthToken(),
                null,
                Collections.singletonList(username)
        ).execute();
        if (userList.getUsers().isEmpty()) {
            return null;
        }
        return userList.getUsers().get(0);
    }
    
    public List<User> getUserListByIds(Collection<String> userIdList) throws HystrixRuntimeException {
        Iterator<String> iterator = userIdList.iterator();
        List<String> usersHundred = new ArrayList<>();
        List<User> output = new ArrayList<>();
        while (iterator.hasNext()) {
            while (usersHundred.size() < 100 && iterator.hasNext()) {
                usersHundred.add(iterator.next());
            }
            UserList resultList = twitchClient.getHelix().getUsers(
                    Settings.getTwitchChannelAuthToken(),
                    usersHundred,
                    null
            ).execute();
            output.addAll(resultList.getUsers());
            usersHundred.clear();
        }
        return output;
    }
    
    public List<User> getUserListByUsernames(Collection<String> usernameList) throws HystrixRuntimeException {
        Iterator<String> iterator = usernameList.iterator();
        List<String> usersHundred = new ArrayList<>();
        List<User> output = new ArrayList<>();
        while (iterator.hasNext()) {
            while (usersHundred.size() < 100 && iterator.hasNext()) {
                usersHundred.add(iterator.next());
            }
            UserList resultList = twitchClient.getHelix().getUsers(
                    Settings.getTwitchChannelAuthToken(),
                    null,
                    usersHundred
            ).execute();
            output.addAll(resultList.getUsers());
            usersHundred.clear();
        }
        return output;
    }
    
    public Video getVideoById(String videoId) throws HystrixRuntimeException {
        VideoList videoList = twitchClient.getHelix().getVideos(
                Settings.getTwitchChannelAuthToken(),
                videoId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                1
        ).execute();
        if (videoList.getVideos().isEmpty()) {
            return null;
        }
        return videoList.getVideos().get(0);
    }
}