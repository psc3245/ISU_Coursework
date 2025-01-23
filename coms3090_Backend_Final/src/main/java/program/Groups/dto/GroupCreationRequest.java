package program.Groups.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GroupCreationRequest {
    @NotBlank(message = "creatorID is required")
    private int creatorId;
    @NotBlank(message = "Group Name is required to exist")
    @Size(min = 3,max=40,message="Group name must be between 3 and 40 characters long")
    private String groupName;
    
    private String groupDescription;
    private String groupType;
    
    public GroupCreationRequest() {}
    public int getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
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
    
}
