package org.project.pictureservice.web.mappers;

import org.mapstruct.Mapper;
import org.project.pictureservice.domain.picture.Picture;
import org.project.pictureservice.web.dto.picture.PictureDto;

@Mapper(componentModel = "spring")
public interface PictureMapper extends Mappable<Picture, PictureDto> {
}
