package org.project.pictureservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.project.pictureservice.domain.picture.Picture;
import org.project.pictureservice.domain.user.User;
import org.project.pictureservice.service.PictureService;
import org.project.pictureservice.service.UserService;
import org.project.pictureservice.web.dto.picture.PictureDto;
import org.project.pictureservice.web.dto.user.UserDto;
import org.project.pictureservice.web.dto.validation.OnCreate;
import org.project.pictureservice.web.mappers.PictureMapper;
import org.project.pictureservice.web.mappers.UserMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "User Controller",
        description = "User API"
)
public class UserController {
    private final UserService userService;
    private final PictureService pictureService;

    private final UserMapper userMapper;
    private final PictureMapper pictureMapper;

    private KafkaTemplate<String, String> kafkaTemplate;

    @PutMapping
    @MutationMapping(name = "updateUser")
    @Operation(summary = "Update user")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#dto.id)")
    public UserDto update(
            @Validated(OnCreate.class)
            @RequestBody @Argument final UserDto dto
    ) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{id}")
    @QueryMapping(name = "userById")
    @Operation(summary = "Get UserDto by id")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public UserDto getById(@PathVariable @Argument final Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @MutationMapping(name = "deleteUserById")
    @Operation(summary = "Delete user by id")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void deleteById(@PathVariable @Argument final Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/pictures")
    @QueryMapping(name = "picturesByUserId")
    @Operation(summary = "Get all user pictures")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public List<PictureDto> getPicturesByUserId(@PathVariable @Argument final Long id) {
        List<Picture> pictures = pictureService.getAllByUserId(id);
        return pictureMapper.toDto(pictures);
    }

    @PostMapping("/{id}/pictures")
    @MutationMapping(name = "createPicture")
    @Operation(summary = "Add task to user")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public PictureDto createPicture(
            @PathVariable @Argument final Long id,
            @Validated(OnCreate.class)
            @RequestBody @Argument final PictureDto dto
    ) {
        Picture picture = pictureMapper.toEntity(dto);
        Picture createdPicture = pictureService.create(picture, id);
        return pictureMapper.toDto(createdPicture);
    }

    @PostMapping
    @Operation(summary = "Edit picture in current user")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void edit(
            @RequestBody final Long id,
            @RequestBody final PictureDto dto
    ) {
        kafkaTemplate.send("picture-service-topic", id.toString());
    }
}
