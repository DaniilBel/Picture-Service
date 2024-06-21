package org.project.pictureservice.service;

import org.project.pictureservice.domain.picture.PictureImage;

public interface ImageService {

    String upload(
            PictureImage image
    );
}
