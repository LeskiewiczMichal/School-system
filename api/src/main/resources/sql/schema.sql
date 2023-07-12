DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       faculty VARCHAR(100) NOT NULL,
                       role VARCHAR(100) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE authorities (
                             id INT NOT NULL AUTO_INCREMENT,
                             user_id BIGINT NOT NULL,
                             name VARCHAR(50) NOT NULL,
                             PRIMARY KEY (id),
                             FOREIGN KEY (user_id) REFERENCES users (id)
);