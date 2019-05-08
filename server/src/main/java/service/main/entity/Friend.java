package service.main.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friend implements Serializable {

    private List<String> friends; // UsersEmails whom this user is friend
    private List<String> friendRequests; // UserEmails whom wants to be friend with this user.

    private List<String> rejectedUsersSuggested; // Users that got rejected by *this* user, when they were suggested.



    public Friend() {
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
        setRejectedUsersSuggested(new ArrayList<>());

    }

    public Friend(List<String> friends, List<String> friendRequests)
    {
        setFriends(friends);
        setFriendRequests(friendRequests);
    }

    public Friend(List<String> friends, List<String> friendRequests, List<String> rejectedUsersSuggested)
    {
        setFriends(friends);
        setFriendRequests(friendRequests);
        setRejectedUsersSuggested(rejectedUsersSuggested);
    }


    public List<String> getFriends() { return friends; }
    public List<String> getFriendRequests() { return friendRequests; }
    public List<String> getRejectedUsersSuggested() { return rejectedUsersSuggested; }


    public void setFriends(List<String> friends) { this.friends = friends; }
    public void setFriendRequests(List<String> friendRequests) { this.friendRequests = friendRequests; }
    public void setRejectedUsersSuggested(List<String> rejectedUsersSuggested) { this.rejectedUsersSuggested = rejectedUsersSuggested; }




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

    /*
    Friends Suggestion
     */

    public void addRejectedUserSuggestion(String userEmail) {
        rejectedUsersSuggested.add(userEmail);
    }

    public void removeRejectedUserSuggestion(String userEmail) {
        rejectedUsersSuggested.remove(userEmail);
    }

    public boolean rejectedFriendSuggestionOf(String userEmail)  {
        return rejectedUsersSuggested.contains(userEmail);
    }

}
