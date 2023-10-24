package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommentTest {

    private Comment comment;

    @BeforeEach
    public void setUp() {
        comment = new Comment(1, "mailto:user@example.com", "user1", 1001, "This is a test comment");
    }

    @Test
    public void OrdinaryAddUserVoteTest() {
        assertEquals(0, comment.getLike());
        assertEquals(0, comment.getDislike());

        comment.addUserVote("user2", "like");

        assertEquals(1, comment.getLike());
        assertEquals(0, comment.getDislike());

        comment.addUserVote("user3", "dislike");

        assertEquals(1, comment.getLike());
        assertEquals(1, comment.getDislike());

        comment.addUserVote("user4", "like");

        assertEquals(2, comment.getLike());
        assertEquals(1, comment.getDislike());

        comment.addUserVote("user1", "like");

        assertEquals(3, comment.getLike());
        assertEquals(1, comment.getDislike());

        comment.addUserVote("user5", "dislike");

        assertEquals(3, comment.getLike());
        assertEquals(2, comment.getDislike());
    }

    @Test
    public void SomeoneChangesTheirVoteAddUserVoteTest() {
        assertEquals(0, comment.getLike());
        assertEquals(0, comment.getDislike());

        comment.addUserVote("user2", "like");

        assertEquals(1, comment.getLike());
        assertEquals(0, comment.getDislike());

        comment.addUserVote("user3", "dislike");

        assertEquals(1, comment.getLike());
        assertEquals(1, comment.getDislike());

        comment.addUserVote("user4", "like");

        assertEquals(2, comment.getLike());
        assertEquals(1, comment.getDislike());

        comment.addUserVote("user2", "dislike");

        assertEquals(1, comment.getLike());
        assertEquals(2, comment.getDislike());

        comment.addUserVote("user3", "like");

        assertEquals(2, comment.getLike());
        assertEquals(1, comment.getDislike());

        comment.addUserVote("user1", "like");

        assertEquals(3, comment.getLike());
        assertEquals(1, comment.getDislike());
    }
}
