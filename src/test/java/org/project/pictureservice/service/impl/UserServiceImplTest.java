package org.project.pictureservice.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.pictureservice.config.TestConfig;
import org.project.pictureservice.domain.exception.ResourceNotFoundException;
import org.project.pictureservice.domain.user.Role;
import org.project.pictureservice.domain.user.User;
import org.project.pictureservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        User testUser = userService.getById(id);
        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(id));
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void getByUsername() {
        String username = "username@gmail.com";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));
        User testUser = userService.getByUsername(username);
        Mockito.verify(userRepository).findByUsername(username);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingUsername() {
        String username = "username@gmail.com";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getByUsername(username));
        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void update() {
        Long id = 1L;
        String password = "password";
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        User updated = userService.update(user);
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(user.getUsername(), updated.getUsername());
        Assertions.assertEquals(user.getName(), updated.getName());
    }

    @Test
    void isPictureOwner() {
        Long userId = 1L;
        Long pictureId = 1L;
        Mockito.when(userRepository.isPictureOwner(userId, pictureId))
                .thenReturn(true);
        boolean isOwner = userService.isPictureOwner(userId, pictureId);
        Mockito.verify(userRepository).isPictureOwner(userId, pictureId);
        Assertions.assertTrue(isOwner);
    }

    @Test
    void create() {
        String username = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        User testUser = userService.create(user);
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(Set.of(Role.ROLE_USER), testUser.getRoles());
        Assertions.assertEquals("encodedPassword",
                testUser.getPassword());
    }

    @Test
    void createWithExistingUsername() {
        String username = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User()));
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void createWithDifferentPasswords() {
        String username = "username@gmail.com";
        String password = "password1";
        String passwordConfirmation = "password2";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void isTaskOwnerWithFalse() {
        Long userId = 1L;
        Long pictureId = 1L;
        Mockito.when(userRepository.isPictureOwner(userId, pictureId))
                .thenReturn(false);
        boolean isOwner = userService.isPictureOwner(userId, pictureId);
        Mockito.verify(userRepository).isPictureOwner(userId, pictureId);
        Assertions.assertFalse(isOwner);
    }

    @Test
    void getPictureAuthor() {
        Long userId = 1L;
        Long pictureId = 1L;
        User user = new User();
        user.setId(userId);
        Mockito.when(userRepository.findPictureAuthor(pictureId))
                .thenReturn(Optional.of(user));
        User author = userService.getPictureAuthor(pictureId);
        Mockito.verify(userRepository).findPictureAuthor(pictureId);
        Assertions.assertEquals(user, author);
    }

    @Test
    void getNotExistingPictureAuthor() {
        Long pictureId = 1L;
        Mockito.when(userRepository.findPictureAuthor(pictureId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                userService.getPictureAuthor(pictureId));
        Mockito.verify(userRepository).findPictureAuthor(pictureId);
    }

    @Test
    void delete() {
        Long id = 1L;
        userService.delete(id);
        Mockito.verify(userRepository).deleteById(id);
    }
}
