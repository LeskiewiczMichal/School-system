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
   student_faculty BIGINT,
   degree BIGINT,
   role VARCHAR(100) NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (student_faculty) REFERENCES faculty(id),
   FOREIGN KEY (degree) REFERENCES degree(id)
);

CREATE TABLE authorities (
    id INT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE faculty_teacher (
     faculty_id BIGINT,
     teacher_id BIGINT,
     FOREIGN KEY (faculty_id) REFERENCES faculty(id),
     FOREIGN KEY (teacher_id) REFERENCES users(id)
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

INSERT INTO faculty (id, name)
VALUES (1, 'Informatics');

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (1, 'BACHELOR_OF_SCIENCE', 'Computer Science', 1);

INSERT INTO users (first_name, last_name, email, password, student_faculty, degree, role)
VALUES ('John', 'Doe', 'johndoe@example.com', '12345', 1, 1, 'ROLE_STUDENT');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (1, 'Introduction to Programming', 40, 1, 1);

INSERT INTO course_student (course_id, student_id)
VALUES (1, 1);

INSERT INTO users (first_name, last_name, email, password, student_faculty, degree, role)
VALUES ('Alice', 'Smith', 'alicesmith@example.com', '54321', 1, 1, 'ROLE_STUDENT');

INSERT INTO course_student (course_id, student_id)
VALUES (1, 2);

INSERT INTO users (first_name, last_name, email, password, student_faculty, role)
VALUES ('Bob', 'Johnson', 'bobjohnson@example.com', 'abcde', NULL, 'ROLE_TEACHER');

INSERT INTO faculty_teacher (faculty_id, teacher_id)
VALUES (1, 3);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (2, 'Master of Science', 'Software Engineering', 1);

INSERT INTO users (first_name, last_name, email, password, student_faculty, degree, role)
VALUES ('Emily', 'Wilson', 'emilywilson@example.com', 'qwerty', 1, 2, 'ROLE_STUDENT');

INSERT INTO degree_course (degree_id, course_id)
VALUES (2, 1);
