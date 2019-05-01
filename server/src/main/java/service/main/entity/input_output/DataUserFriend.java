package service.main.entity.input_output;

import java.io.Serializable;
import java.util.List;

public class DataUserFriend implements Serializable {

    private List<String> friends; // UsersEmails whom this user is friend
    private List<String> friendRequests; // UserEmails whom wants to be friend with this user.


    public List<String> getFriends() { return friends; }
    public List<String> getFriendRequests() { return friendRequests; }

    public void setFriends(List<String> friends) { this.friends = friends; }
    public void setFriendRequests(List<String> friendRequests) { this.friendRequests = friendRequests; }
}
