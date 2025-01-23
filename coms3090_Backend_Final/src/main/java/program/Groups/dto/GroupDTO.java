package program.Groups.dto;

import java.util.List;

public class GroupDTO {
    private int groupId;
    private String groupName;
    private String groupDescription;
    private String groupType;
    private List<Integer> memberIds; // List of user IDs
    private List<Integer> postIds; // List of post IDs

    // Constructor
    public GroupDTO(int groupId, String groupName, String groupDescription, String groupType, List<Integer> memberIds, List<Integer> postIds) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupType = groupType;
        this.memberIds = memberIds;
        this.postIds = postIds;
    }

    // Getters and Setters
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public List<Integer> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<Integer> postIds) {
        this.postIds = postIds;
    }
}