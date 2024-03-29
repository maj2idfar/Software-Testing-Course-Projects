package controllers;

import exceptions.IncorrectPassword;
import exceptions.NotExistentUser;
import exceptions.UsernameAlreadyTaken;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationControllerTest {
    @Mock private Baloot baloot;
    @Mock private NotExistentUser notExistentUser;
    @Mock private IncorrectPassword incorrectPassword;
    @Mock private UsernameAlreadyTaken usernameAlreadyTaken;
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        baloot = mock(Baloot.class);

        authenticationController = new AuthenticationController();
        authenticationController.setBaloot(baloot);
    }

    @Test
    public void successfulLogin() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("password", "pass");

        doNothing().when(baloot).login(anyString(), anyString());

        ResponseEntity<String> response = authenticationController.login(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("login successfully!", response.getBody());

        verify(baloot, times(1)).login("user", "pass");
    }

    @Test
    public void loginWithIncorrectUsername() throws NotExistentUser, IncorrectPassword {
        notExistentUser = mock(NotExistentUser.class);

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("password", "pass");

        doThrow(notExistentUser).when(baloot).login(anyString(), anyString());
        when(notExistentUser.getMessage()).thenReturn("User not found!");

        ResponseEntity<String> response = authenticationController.login(input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(notExistentUser.getMessage(), response.getBody());

        verify(baloot, times(1)).login("user", "pass");
    }

    @Test
    public void loginWithIncorrectPassword() throws NotExistentUser, IncorrectPassword {
        incorrectPassword = mock(IncorrectPassword.class);

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("password", "pass");

        doThrow(incorrectPassword).when(baloot).login(anyString(), anyString());
        when(incorrectPassword.getMessage()).thenReturn("Password doesn't match!");

        ResponseEntity<String> response = authenticationController.login(input);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(incorrectPassword.getMessage(), response.getBody());

        verify(baloot, times(1)).login("user", "pass");
    }

    @Test
    public void successfulSignup() throws UsernameAlreadyTaken {
        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("password", "pass");
        input.put("email", "user@test.com");
        input.put("birthDate", "2000/1/1");
        input.put("address", "nowhere!");

        doNothing().when(baloot).addUser(isA(User.class));

        ResponseEntity<String> response = authenticationController.signup(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("signup successfully!", response.getBody());

        verify(baloot, times(1)).addUser(isA(User.class));
    }

    @Test
    public void signupWithExistedUsername() throws UsernameAlreadyTaken {
        usernameAlreadyTaken = mock(UsernameAlreadyTaken.class);

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("password", "pass");
        input.put("email", "user@test.com");
        input.put("birthDate", "2000/1/1");
        input.put("address", "nowhere!");

        doThrow(usernameAlreadyTaken).when(baloot).addUser(isA(User.class));
        when(usernameAlreadyTaken.getMessage()).thenReturn("Username already taken!");

        ResponseEntity<String> response = authenticationController.signup(input);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(usernameAlreadyTaken.getMessage(), response.getBody());

        verify(baloot, times(1)).addUser(isA(User.class));
    }
}
