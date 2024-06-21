package org.project.pictureservice.repository;

import org.project.pictureservice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(
            String username
    );

    @Query(value = """
            SELECT exists(
                               SELECT 1
                               FROM users_pictures
                               WHERE user_id = :userId
                                 AND picture_id = :pictureId)
                """, nativeQuery = true)
    boolean isPictureOwner(
            @Param("userId") Long userId,
            @Param("pictureId") Long pictureId
    );
}
