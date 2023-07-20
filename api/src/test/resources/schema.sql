DROP TABLE IF EXISTS course_student;
DROP TABLE IF EXISTS degree_course;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS faculty_teacher;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS degree;
DROP TABLE IF EXISTS faculty;


CREATE TABLE faculty (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE degree (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        title VARCHAR(100) NOT NULL,
                        field_of_study VARCHAR(100) NOT NULL,
                        faculty BIGINT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (faculty) REFERENCES faculty(id)
);

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       faculty BIGINT,
                       degree BIGINT,
                       role VARCHAR(100) NOT NULL,
                       PRIMARY KEY (id),
                       FOREIGN KEY (faculty) REFERENCES faculty(id),
                       FOREIGN KEY (degree) REFERENCES degree(id)
);

CREATE TABLE authorities (
                             id INT NOT NULL AUTO_INCREMENT,
                             user_id BIGINT NOT NULL,
                             name VARCHAR(50) NOT NULL,
                             PRIMARY KEY (id),
                             FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE course (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        title VARCHAR(100) NOT NULL,
                        duration_in_hours INT NOT NULL,
                        teacher BIGINT NOT NULL,
                        faculty BIGINT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (teacher) REFERENCES users(id),
                        FOREIGN KEY (faculty) REFERENCES faculty(id)
);

CREATE TABLE course_student (
                                course_id BIGINT,
                                student_id BIGINT,
                                FOREIGN KEY (course_id) REFERENCES course(id),
                                FOREIGN KEY (student_id) REFERENCES users(id)
);

CREATE TABLE degree_course (
                               degree_id BIGINT,
                               course_id BIGINT,
                               FOREIGN KEY (degree_id) REFERENCES degree(id),
                               FOREIGN KEY (course_id) REFERENCES course(id)
);