package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    private Comment comment;

    @BeforeEach
    public void setUp() {
        comment = new Comment(1, "mailto:user@example.com", "user1", 1001, "This is a test comment");
    }

    @Test
    public void testAddUserVote() {
        // Initially, like and dislike should be 0
        assertEquals(0, comment.getLike());
        assertEquals(0, comment.getDislike());

        // Add a 'like' vote
        comment.addUserVote("user2", "like");

        // Verify that 'like' count is updated to 1
        assertEquals(1, comment.getLike());
        assertEquals(0, comment.getDislike());

        // Add a 'dislike' vote
        comment.addUserVote("user3", "dislike");

        // Verify that 'dislike' count is updated to 1
        assertEquals(1, comment.getLike());
        assertEquals(1, comment.getDislike());

        // Add another 'like' vote
        comment.addUserVote("user4", "like");

        // Verify that 'like' count is updated to 2
        assertEquals(2, comment.getLike());
        assertEquals(1, comment.getDislike());

        // Someone (user2) changes their vote (like to dislike)
        comment.addUserVote("user2", "dislike");

        // Verify that 'like' count is updated to 2
        assertEquals(1, comment.getLike());
        assertEquals(2, comment.getDislike());

        // Someone (user3) changes their vote (dislike to like)
        comment.addUserVote("user3", "like");

        // Verify that 'like' count is updated to 2
        assertEquals(2, comment.getLike());
        assertEquals(1, comment.getDislike());

        // Add another 'like' vote
        comment.addUserVote("user1", "like");

        // Verify that 'like' count is updated to 2
        assertEquals(3, comment.getLike());
        assertEquals(1, comment.getDislike());
    }
}
