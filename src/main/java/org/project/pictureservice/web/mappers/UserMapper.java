package org.project.pictureservice.web.mappers;

import org.mapstruct.Mapper;
import org.project.pictureservice.domain.user.User;
import org.project.pictureservice.web.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
