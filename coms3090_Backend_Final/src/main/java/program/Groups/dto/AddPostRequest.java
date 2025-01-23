package program.Groups.dto;

public class AddPostRequest {
    private int groupId;
    private int postId;

    public AddPostRequest(){}
    public int getGroupId() {
        return this.groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
}
