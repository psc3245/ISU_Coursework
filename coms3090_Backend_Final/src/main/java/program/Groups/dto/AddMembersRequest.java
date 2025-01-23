package program.Groups.dto;


public class AddMembersRequest{
    private int groupId;
    private int userId;

    public AddMembersRequest(){}
    public int getGroupId() {
        return this.groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}