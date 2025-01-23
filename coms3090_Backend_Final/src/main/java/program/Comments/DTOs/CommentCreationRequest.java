package program.Comments.DTOs;

public class CommentCreationRequest {
    private String content;
    private Integer postId;
    private Integer userId;
    public CommentCreationRequest(String content, Integer postId, Integer userId) {
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }
    public CommentCreationRequest() {
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getPostId() {
        return postId;
    }
    public void setPostId(Integer postId) {
        this.postId = postId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
