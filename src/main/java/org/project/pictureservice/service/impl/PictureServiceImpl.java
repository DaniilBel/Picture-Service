package org.project.pictureservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.pictureservice.domain.exception.ResourceNotFoundException;
import org.project.pictureservice.domain.picture.Picture;
import org.project.pictureservice.domain.picture.PictureImage;
import org.project.pictureservice.repository.PictureRepository;
import org.project.pictureservice.service.ImageService;
import org.project.pictureservice.service.PictureService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final ImageService imageService;

    @Override
    @Cacheable(
            value = "PictureService::getById",
            key = "#id"
    )
    public Picture getById(final Long id) {
        return pictureRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Picture not found with id " + id));
    }

    @Override
    public List<Picture> getAllByUserId(final Long id) {
        return pictureRepository.findAllByUserId(id);
    }

    @Override
    @Transactional
    @CachePut(
            value = "PictureService::getByID",
            key = "#picture.id"
    )
    public Picture update(final Picture picture) {
        Picture existing = getById(picture.getId());
        existing.setTitle(picture.getTitle());
        existing.setDescription(picture.getDescription());
        pictureRepository.save(picture);
        return picture;
    }

    @Override
    @Transactional
    @Cacheable(
            value = "PictureService::getById",
            condition = "#picture.id!=null",
            key = "#picture.id"
    )
    public Picture create(
            final Picture picture,
            final Long userId
    ) {
        pictureRepository.save(picture);
        pictureRepository.assignPicture(userId, picture.getId());
        return picture;
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "PictureService::getByID",
            key = "#id"
    )
    public void delete(final Long id) {
        pictureRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "PictureService::getByID",
            key = "#id"
    )
    public void uploadImage(
            final Long id,
            final PictureImage image
    ) {
        String filename = imageService.upload(image);
        pictureRepository.addImage(id, filename);
    }
}
