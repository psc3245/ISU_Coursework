package program.Users.listener;

import jakarta.persistence.PreRemove;
import program.Groups.Group;
import program.Users.User;

public class UserEntityListener {
    @PreRemove
    public void preRemove(User user) {
        for (Group group : user.getMemberGroups()) {
            group.getMembers().remove(user);
        }
    }
}