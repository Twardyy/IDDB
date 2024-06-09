--liquibase formatted sql
--changeset pacion:1
CREATE TABLE role
(
    role_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (role_id)
);

ALTER TABLE role
    ADD CONSTRAINT uc_role_name UNIQUE (name);
