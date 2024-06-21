package org.project.pictureservice.web.dto.picture;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.project.pictureservice.web.dto.validation.OnCreate;
import org.project.pictureservice.web.dto.validation.OnUpdate;

import java.util.List;

@Data
@Schema(description = "Picture Dto")
public class PictureDto {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "Title", example = "Vangog")
    @NotNull(message = "Title must be not null",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Title length must be smaller than 255 sym",
            groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Schema(description = "Description", example = "Beautiful")
    @Length(max = 255,
            message = "Description length must be smaller than 255 sym",
            groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Schema(description = "Store images")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images;
}
