package org.project.pictureservice.repository;

import org.project.pictureservice.domain.picture.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    @Query(value = """
        SELECT * FROM pictures p
        JOIN users_pictures up ON up.picture_id = p.id
        WHERE up.user_id = :userId
    """, nativeQuery = true)
    List<Picture> findAllByUserId(
            @Param("userId") Long userId
    );

    @Modifying
    @Query(value = """
            INSERT INTO users_pictures (user_id, picture_id)
            VALUES (:userId, :pictureId)
            """, nativeQuery = true)
    void assignPicture(
            @Param("userId") Long userId,
            @Param("pictureId") Long pictureId
    );

    @Modifying
    @Query(value = """
            INSERT INTO pictures_images (picture_id, image)
            VALUES (:id, :fileName)
            """, nativeQuery = true)
    void addImage(
            @Param("id") Long id,
            @Param("fileName") String fileName
    );
}
