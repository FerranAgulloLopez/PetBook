package service.main.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friend implements Serializable {

    private List<String> friends; // UsersEmails whom this user is friend
    private List<String> friendRequests; // UserEmails whom wants to be friend with this user.



    public Friend() {
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
    }

    public Friend(List<String> friends, List<String> friendRequests)
    {
        setFriends(friends);
        setFriendRequests(friendRequests);
    }


    public List<String> getFriends() { return friends; }
    public List<String> getFriendRequests() { return friendRequests; }

    public void setFriends(List<String> friends) { this.friends = friends; }
    public void setFriendRequests(List<String> friendRequests) { this.friendRequests = friendRequests; }


    /*
    Friends
     */

    public void addFriend(String friend) {
        friends.add(friend);
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
    }

    public boolean isFriend(String friend)  {
        return friends.contains(friend);
    }

    /*
    Friend Request
     */

    public void addFriendRequest(String friend) {
        friendRequests.add(friend);
    }

    public void removeFriendRequest(String friend) {
        friendRequests.remove(friend);
    }

    public boolean beenRequestedToBeFriendBy(String friend)  {
        return friendRequests.contains(friend);
    }

}
