DROP TABLE IF EXISTS course_student;
DROP TABLE IF EXISTS degree_course;
DROP TABLE IF EXISTS course_file;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS faculty_teacher;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS teacher_details;
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS degree;
DROP TABLE IF EXISTS faculty;
DROP TABLE IF EXISTS file;



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
                        description TEXT NOT NULL,
                        length_of_study DOUBLE NOT NULL,
                        tuition_fee_per_year DOUBLE NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (faculty) REFERENCES faculty(id)
);

CREATE TABLE languages_table (
                                 degree_id BIGINT,
                                 language VARCHAR(45),
                                 PRIMARY KEY (degree_id, language),
                                 FOREIGN KEY (degree_id) REFERENCES degree (id)
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

CREATE TABLE teacher_details (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         teacher_id BIGINT NOT NULL,
                         degree_field VARCHAR(100) NOT NULL,
                         title VARCHAR(100) NOT NULL,
                         bio VARCHAR(1200) DEFAULT '' NOT NULL,
                         tutorship VARCHAR(800) DEFAULT '' NOT NULL,
                         PRIMARY KEY (id),
                         FOREIGN KEY (teacher_id) REFERENCES users (id)
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

CREATE TABLE file (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      file_name VARCHAR(255) NOT NULL,
                      file_type VARCHAR(40) NOT NULL,
                      uploaded_by VARCHAR(120) NOT NULL,
                      file_data LONGBLOB NOT NULL
);

CREATE TABLE course_file (
                             course_id BIGINT NOT NULL,
                             file_id BIGINT NOT NULL,
                             PRIMARY KEY (course_id, file_id),
                             FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE,
                             FOREIGN KEY (file_id) REFERENCES file (id) ON DELETE CASCADE
);

CREATE TABLE article(
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        title VARCHAR(300) NOT NULL,
                        preview VARCHAR(600) NOT NULL,
                        content LONGTEXT NOT NULL,
                        author_id BIGINT,
                        category VARCHAR(50) NOT NULL,
                        faculty_id BIGINT,
                        image_name VARCHAR(120),
                        FOREIGN KEY (author_id) REFERENCES users(id),
                        FOREIGN KEY (faculty_id) REFERENCES faculty(id),
                        PRIMARY KEY (id)
);

