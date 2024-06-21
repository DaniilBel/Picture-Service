package org.project.pictureservice.web.dto.picture;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "Picture Image Dto")
public class PictureImageDto {

    @NotNull(message = "Image must be not null")
    private MultipartFile file;
}
