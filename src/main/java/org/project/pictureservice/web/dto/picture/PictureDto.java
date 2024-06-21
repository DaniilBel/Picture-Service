package org.project.pictureservice.web.dto.picture;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.project.pictureservice.web.dto.validation.OnCreate;
import org.project.pictureservice.web.dto.validation.OnUpdate;

import java.util.List;

@Data
public class PictureDto {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Title must be not null",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Title length must be smaller than 255 sym",
            groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Length(max = 255,
            message = "Description length must be smaller than 255 sym",
            groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images;
}
