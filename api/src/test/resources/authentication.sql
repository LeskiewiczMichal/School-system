INSERT INTO faculty (id, name)
VALUES (1, 'Informatics');

INSERT INTO degree (id, title, field_of_study, faculty, description , length_of_study, tuition_fee_per_year)
VALUES (1, 'BACHELOR_OF_SCIENCE', 'Computer Science', 1, 'This is test description', 3.0, 15000.00);

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('John', 'Doe', 'johndoe@example.com', '12345', 1, 1, 'ROLE_STUDENT');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (1, 'Introduction to Programming', 40, 1, 1);

INSERT INTO course_student (course_id, student_id)
VALUES (1, 1);

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Alice', 'Smith', 'alicesmith@example.com', '54321', 1, 1, 'ROLE_STUDENT');

INSERT INTO course_student (course_id, student_id)
VALUES (1, 2);

INSERT INTO users (first_name, last_name, email, password, faculty, role)
VALUES ('Bob', 'Johnson', 'bobjohnson@example.com', 'abcde', 1, 'ROLE_TEACHER');

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (2, 'MASTER', 'Software Engineering', 1, 'This is test description', 3.0, 15000.00);

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Emily', 'Wilson', 'emilywilson@example.com', 'qwerty', 1, 2, 'ROLE_STUDENT');

INSERT INTO degree_course (degree_id, course_id)
VALUES (2, 1);
