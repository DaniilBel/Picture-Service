CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL unique,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS pictures
(
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS users_pictures
(
    user_id BIGINT NOT NULL,
    picture_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, picture_id),
    CONSTRAINT fk_users_pictures_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT fk_users_pictures_pictures FOREIGN KEY (picture_id) REFERENCES pictures (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id BIGINT       NOT NULL,
    role    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_users_roles_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS pictures_images
(
    picture_id BIGINT       NOT NULL,
    image   VARCHAR(255) NOT NULL,
    CONSTRAINT fk_pictures_images_pictures FOREIGN KEY (picture_id) REFERENCES pictures (id) ON DELETE CASCADE ON UPDATE NO ACTION
);