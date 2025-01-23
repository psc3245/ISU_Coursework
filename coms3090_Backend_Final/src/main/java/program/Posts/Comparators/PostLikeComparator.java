package program.Posts.Comparators;

import program.Posts.Post;

import java.util.Comparator;

public class PostLikeComparator implements Comparator<Post> {
    @Override
    public int compare(Post post1, Post post2) {
        return Integer.compare(post2.getLikedBy().size(), post1.getLikedBy().size());
    }
}
