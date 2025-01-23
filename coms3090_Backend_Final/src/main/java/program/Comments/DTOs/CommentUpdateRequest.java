package program.Comments.DTOs;

public class CommentUpdateRequest {
    private String content;
    private Integer commentId;
    public CommentUpdateRequest() {
    }
    public CommentUpdateRequest(String content, Integer commentId) {
        this.content = content;
        this.commentId = commentId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getCommentId() {
        return commentId;
    }
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
}
