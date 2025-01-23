package program.Groups.dto;

public class UpdateGroupsRequest {
    private String groupName;
    private String groupDescription;
    private String groupType;
    private Integer groupId;
    public UpdateGroupsRequest(String groupName, String groupDescription, String groupType, Integer groupId) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupType = groupType;
        this.groupId = groupId;
    }
    public UpdateGroupsRequest() {
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
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
