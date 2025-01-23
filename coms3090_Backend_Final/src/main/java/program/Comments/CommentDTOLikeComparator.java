package program.Comments;

import program.Comments.DTOs.CommentDTO;

import java.util.Comparator;

public class CommentDTOLikeComparator implements Comparator<CommentDTO> {
    @Override
    public int compare(CommentDTO comment1, CommentDTO comment2) {
        return Integer.compare(comment2.getLikedByUserIds().size(), comment1.getLikedByUserIds().size());
    }
}
