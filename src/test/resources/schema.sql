CREATE TABLE users
(
    id                bigint       NOT NULL AUTO_INCREMENT,
    age               int          DEFAULT NULL,
    email             varchar(255) NOT NULL,
    first_name        varchar(255) DEFAULT NULL,
    last_name         varchar(255) DEFAULT NULL,
    password          varchar(255) NOT NULL,
    picture_public_id varchar(255) DEFAULT NULL,
    picture_url       varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id   bigint NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);