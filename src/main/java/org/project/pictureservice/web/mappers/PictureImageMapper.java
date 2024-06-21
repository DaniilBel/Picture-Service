package org.project.pictureservice.web.mappers;

import org.mapstruct.Mapper;
import org.project.pictureservice.domain.picture.PictureImage;
import org.project.pictureservice.web.dto.picture.PictureImageDto;

@Mapper(componentModel = "spring")
public interface PictureImageMapper extends Mappable<PictureImage, PictureImageDto> {
}
