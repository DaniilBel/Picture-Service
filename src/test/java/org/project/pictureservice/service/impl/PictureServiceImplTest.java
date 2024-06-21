package org.project.pictureservice.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.project.pictureservice.config.TestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.pictureservice.domain.exception.ResourceNotFoundException;
import org.project.pictureservice.domain.picture.Picture;
import org.project.pictureservice.domain.picture.PictureImage;
import org.project.pictureservice.repository.PictureRepository;
import org.project.pictureservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class PictureServiceImplTest {

    @MockBean
    private PictureRepository pictureRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private PictureServiceImpl pictureService;

    @Test
    void getById() {
        Long id = 1L;
        Picture picture = new Picture();
        picture.setId(id);
        Mockito.when(pictureRepository.findById(id))
                .thenReturn(Optional.of(picture));
        Picture testPicture = pictureService.getById(id);
        Mockito.verify(pictureRepository).findById(id);
        Assertions.assertEquals(picture, testPicture);
    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        Mockito.when(pictureRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pictureService.getById(id));
        Mockito.verify(pictureRepository).findById(id);
    }

    @Test
    void getAllByUserId() {
        Long userId = 1L;
        List<Picture> pictures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pictures.add(new Picture());
        }
        Mockito.when(pictureRepository.findAllByUserId(userId))
                .thenReturn(pictures);
        List<Picture> testTasks = pictureService.getAllByUserId(userId);
        Mockito.verify(pictureRepository).findAllByUserId(userId);
        Assertions.assertEquals(pictures, testTasks);
    }

    @Test
    void update() {
        Long id = 1L;
        Picture picture = new Picture();
        picture.setId(id);
        Mockito.when(pictureRepository.findById(picture.getId()))
                .thenReturn(Optional.of(picture));
        Picture testPicture = pictureService.update(picture);
        Mockito.verify(pictureRepository).save(picture);
        Assertions.assertEquals(picture, testPicture);
        Assertions.assertEquals(picture.getTitle(), testPicture.getTitle());
        Assertions.assertEquals(
                picture.getDescription(),
                testPicture.getDescription()
        );
    }

    @Test
    void create() {
        Long userId = 1L;
        Long pictureId = 1L;
        Picture picture = new Picture();
        Mockito.doAnswer(invocation -> {
                    Picture savedTask = invocation.getArgument(0);
                    savedTask.setId(pictureId);
                    return savedTask;
                })
                .when(pictureRepository).save(picture);
        Picture testPicture = pictureService.create(picture, userId);
        Mockito.verify(pictureRepository).save(picture);
        Assertions.assertNotNull(testPicture.getId());
        Mockito.verify(pictureRepository).assignPicture(userId, picture.getId());
    }

    @Test
    void delete() {
        Long id = 1L;
        pictureService.delete(id);
        Mockito.verify(pictureRepository).deleteById(id);
    }

    @Test
    void uploadImage() {
        Long id = 1L;
        String imageName = "imageName";
        PictureImage pictureImage = new PictureImage();
        Mockito.when(imageService.upload(pictureImage))
                .thenReturn(imageName);
        pictureService.uploadImage(id, pictureImage);
        Mockito.verify(pictureRepository).addImage(id, imageName);
    }
}
