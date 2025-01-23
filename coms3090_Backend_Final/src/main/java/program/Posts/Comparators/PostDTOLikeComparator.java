package program.Posts.Comparators;

import program.Posts.DTOs.PostDTOs.PostDTO;
import program.Posts.Post;

import java.util.Comparator;

public class PostDTOLikeComparator implements Comparator<PostDTO> {
    @Override
    public int compare(PostDTO post1, PostDTO post2) {
        return Integer.compare(post2.getLikes(), post1.getLikes());
    }
}
