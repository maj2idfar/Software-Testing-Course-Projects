package controllers;

import exceptions.NotExistentComment;
import model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommentControllerTest {
    @Mock private Baloot baloot;
    @Mock private Comment comment;
    @Mock private NotExistentComment notExistentCommentError;

    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        baloot = mock(Baloot.class);
        comment = mock(Comment.class);

        commentController = new CommentController();
        commentController.setBaloot(baloot);
    }

    @Test
    public void LikeValidCommentTest() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "user");

        when(baloot.getCommentById(anyInt())).thenReturn(comment);
        doNothing().when(comment).addUserVote(anyString(), eq("like"));

        ResponseEntity<String> response = commentController.likeComment("1", input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The comment was successfully liked!", response.getBody());

        verify(baloot, times(1)).getCommentById(1);
        verify(comment, times(1)).addUserVote("user", "like");
    }

    @Test
    public void LikeInvalidCommentTest() throws NotExistentComment {
        notExistentCommentError = mock(NotExistentComment.class);

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");

        when(baloot.getCommentById(anyInt())).thenThrow(notExistentCommentError);
        doNothing().when(comment).addUserVote(anyString(), eq("like"));
        when(notExistentCommentError.getMessage()).thenReturn("Comment not found!");

        ResponseEntity<String> response = commentController.likeComment("1", input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(notExistentCommentError.getMessage(), response.getBody());

        verify(baloot, times(1)).getCommentById(1);
        verify(comment, never()).addUserVote(anyString(), anyString());
    }

    @Test
    public void DislikeValidCommentTest() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "user");

        when(baloot.getCommentById(anyInt())).thenReturn(comment);
        doNothing().when(comment).addUserVote(anyString(), eq("dislike"));

        ResponseEntity<String> response = commentController.dislikeComment("1", input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The comment was successfully disliked!", response.getBody());

        verify(baloot, times(1)).getCommentById(1);
        verify(comment, times(1)).addUserVote("user", "dislike");
    }

    @Test
    public void DislikeInvalidCommentTest() throws NotExistentComment {
        notExistentCommentError = mock(NotExistentComment.class);

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");

        NotExistentComment error = new NotExistentComment();

        when(baloot.getCommentById(anyInt())).thenThrow(notExistentCommentError);
        doNothing().when(comment).addUserVote(anyString(), eq("dislike"));
        when(notExistentCommentError.getMessage()).thenReturn("Comment not found!");

        ResponseEntity<String> response = commentController.dislikeComment("1", input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(notExistentCommentError.getMessage(), response.getBody());

        verify(baloot, times(1)).getCommentById(1);
        verify(comment, never()).addUserVote(anyString(), anyString());
    }
}
