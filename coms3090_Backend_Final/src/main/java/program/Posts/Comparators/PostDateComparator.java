package program.Posts.Comparators;

import program.Posts.Post;

import java.util.Comparator;

public class PostDateComparator implements Comparator<Post> {
    @Override
    public int compare(Post post1, Post post2) {
        return post2.getDate().compareTo(post1.getDate()); // Descending order
    }
}
