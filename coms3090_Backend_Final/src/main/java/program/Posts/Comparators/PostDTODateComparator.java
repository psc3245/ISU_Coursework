package program.Posts.Comparators;

import program.Posts.DTOs.PostDTOs.PostDTO;
import program.Posts.Post;

import java.util.Comparator;

public class PostDTODateComparator implements Comparator<PostDTO> {
    @Override
    public int compare(PostDTO post1, PostDTO post2) {
        return post2.getDate().compareTo(post1.getDate()); // Descending order
    }
}
