package org.project.pictureservice.service;

import org.project.pictureservice.domain.user.User;

public interface UserService {

    User getById(
            Long id
    );

    User getByUsername(
            String username
    );

    User update(
            User user
    );

    User create(
            User user
    );

    boolean isPictureOwner(
            Long userId,
            Long pictureId
    );

    void delete(
            Long id
    );
}
