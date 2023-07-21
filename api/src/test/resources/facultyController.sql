-- Faculties

INSERT INTO faculty (id, name)
VALUES (1, 'Informatics');

INSERT INTO faculty (id, name)
VALUES (2, 'Biology');

INSERT INTO faculty (id, name)
VALUES (3, 'Electronics');

INSERT INTO faculty (id, name)
VALUES (4, 'Chemistry');

INSERT INTO faculty (id, name)
VALUES (5, 'Physics');

INSERT INTO faculty (id, name)
VALUES (6, 'Mathematics');

INSERT INTO faculty (id, name)
VALUES (7, 'Geography');

INSERT INTO faculty (id, name)
VALUES (8, 'History');

INSERT INTO faculty (id, name)
VALUES (9, 'Philosophy');

INSERT INTO faculty (id, name)
VALUES (10, 'Psychology');

INSERT INTO faculty (id, name)
VALUES (11, 'Sociology');

INSERT INTO faculty (id, name)
VALUES (12, 'Law');

INSERT INTO faculty (id, name)
VALUES (13, 'Economics');

-- Degrees

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (1, 'BACHELOR_OF_SCIENCE', 'Computer Science', 1);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (2, 'MASTER', 'Software Engineering', 1);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (3, 'BACHELOR', 'Nano', 2);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (4, 'BACHELOR_OF_SCIENCE', 'Math', 2);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (5, 'PROFESSOR', 'Computer Science', 3);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (6, 'DOCTOR', 'Physics', 3);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (7, 'BACHELOR', 'Nano', 4);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (8, 'DOCTOR', 'Computer Science', 4);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (9, 'BACHELOR_OF_SCIENCE', 'Math', 5);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (10, 'MASTER', 'Electronics', 5);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (11, 'DOCTOR', 'Nano', 6);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (12, 'BACHELOR_OF_SCIENCE', 'Physics', 6);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (13, 'DOCTOR', 'Math', 7);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (14, 'MASTER', 'Software Engineering', 7);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (15, 'BACHELOR', 'Electronics', 8);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (16, 'BACHELOR', 'Math', 8);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (17, 'DOCTOR', 'Nano', 9);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (18, 'PROFESSOR', 'Electronics', 10);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (19, 'BACHELOR', 'Physics', 11);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (20, 'DOCTOR', 'Math', 12);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (21, 'BACHELOR', 'Physics', 12);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (22, 'PROFESSOR', 'Nano', 13);

-- Users

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('John', 'Doe', 'johndoe@example.com', '12345', 1, 1, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Jane', 'Smith', 'janesmith@example.com', 'password123', 2, 3, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Michael', 'Johnson', 'michaeljohnson@example.com', 'test123', 3, 5, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Emily', 'Anderson', 'emily.anderson@example.com', 'password123', 4, 7, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Daniel', 'Wilson', 'daniel.wilson@example.com', 'test123', 5, 10, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Olivia', 'Martinez', 'olivia.martinez@example.com', 'securepass', 6, 11, 'ROLE_TEACHER');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('James', 'Garcia', 'james.garcia@example.com', 'mypassword', 7, 13, 'ROLE_TEACHER');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Ava', 'Thomas', 'ava.thomas@example.com', 'pass1234', 8, 15, 'ROLE_TEACHER');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('William', 'Robinson', 'william.robinson@example.com', 'myp@ssw0rd', 9, 17, 'ROLE_TEACHER');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Sophia', 'Walker', 'sophia.walker@example.com', 'password123!', 10, 18, 'ROLE_TEACHER');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Benjamin', 'Young', 'benjamin.young@example.com', 'testpass', 11, 19, 'ROLE_ADMIN');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Isabella', 'Harris', 'isabella.harris@example.com', 'myp@ssword', 12, 20, 'ROLE_ADMIN');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Elijah', 'Lee', 'elijah.lee@example.com', 'mypass123', 13, 22, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Mia', 'Wright', 'mia.wright@example.com', 'password1234', 1, 2, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Oliver', 'Lopez', 'oliver.lopez@example.com', 'myp@ssword123', 2, 4, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Amelia', 'Hall', 'amelia.hall@example.com', 'testpassword', 3, 6, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Lucas', 'Scott', 'lucas.scott@example.com', 'mypassword123', 4, 8, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Aria', 'Perez', 'aria.perez@example.com', 'myp@ss123', 5, 10, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Alice', 'Smith', 'alicesmith@example.com', '54321', 1, 1, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, role)
VALUES ('Bob', 'Johnson', 'bobjohnson@example.com', 'abcde', 1, 'ROLE_TEACHER');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Emily', 'Wilson', 'emilywilson@example.com', 'qwerty', 1, 2, 'ROLE_STUDENT');


-- Courses

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (1, 'Introduction to Programming', 40, 6, 1);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (2, 'Data Structures and Algorithms', 60, 7, 1);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (3, 'Software Engineering Principles', 50, 8, 1);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (4, 'Cell Biology', 45, 9, 2);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (5, 'Genetics', 50, 10, 2);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (6, 'Electromagnetics', 55, 6, 3);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (7, 'Quantum Mechanics', 60, 7, 3);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (8, 'Organic Chemistry', 45, 8, 4);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (9, 'Inorganic Chemistry', 40, 9, 4);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (10, 'Classical Mechanics', 55, 10, 5);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (11, 'Introduction to Electronics', 50, 6, 5);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (12, 'Number Theory', 40, 7, 6);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (13, 'Calculus', 60, 8, 6);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (14, 'Geography of World', 45, 9, 7);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (15, 'Historical Events', 50, 10, 8);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (16, 'Philosophy and Ethics', 55, 7, 9);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (17, 'Introduction to Psychology', 40, 8, 10);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (18, 'Sociological Theories', 45, 6, 11);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (19, 'Criminal Law', 50, 9, 12);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (20, 'Microeconomics', 60, 10, 13);

-- Sing up students for courses

INSERT INTO course_student (course_id, student_id)
VALUES (1, 1);

INSERT INTO course_student (course_id, student_id)
VALUES (2, 2);

INSERT INTO course_student (course_id, student_id)
VALUES (3, 3);

INSERT INTO course_student (course_id, student_id)
VALUES (4, 4);

INSERT INTO course_student (course_id, student_id)
VALUES (5, 5);

INSERT INTO course_student (course_id, student_id)
VALUES (6, 13);

INSERT INTO course_student (course_id, student_id)
VALUES (7, 14);

INSERT INTO course_student (course_id, student_id)
VALUES (8, 15);

INSERT INTO course_student (course_id, student_id)
VALUES (9, 16);

INSERT INTO course_student (course_id, student_id)
VALUES (10, 17);

INSERT INTO course_student (course_id, student_id)
VALUES (11, 18);

INSERT INTO course_student (course_id, student_id)
VALUES (12, 19);

INSERT INTO course_student (course_id, student_id)
VALUES (13, 20);

INSERT INTO course_student (course_id, student_id)
VALUES (14, 1);

INSERT INTO course_student (course_id, student_id)
VALUES (15, 2);

INSERT INTO course_student (course_id, student_id)
VALUES (16, 3);

INSERT INTO course_student (course_id, student_id)
VALUES (17, 4);

INSERT INTO course_student (course_id, student_id)
VALUES (18, 5);

INSERT INTO course_student (course_id, student_id)
VALUES (19, 13);

INSERT INTO course_student (course_id, student_id)
VALUES (20, 14);

INSERT INTO course_student (course_id, student_id)
VALUES (1, 15);

INSERT INTO course_student (course_id, student_id)
VALUES (2, 16);

INSERT INTO course_student (course_id, student_id)
VALUES (3, 17);

INSERT INTO course_student (course_id, student_id)
VALUES (4, 18);

INSERT INTO course_student (course_id, student_id)
VALUES (5, 19);

INSERT INTO course_student (course_id, student_id)
VALUES (6, 20);

INSERT INTO course_student (course_id, student_id)
VALUES (7, 1);

-- Insert courses to degrees

INSERT INTO degree_course (degree_id, course_id)
VALUES (1, 1);

INSERT INTO degree_course (degree_id, course_id)
VALUES (2, 2);

INSERT INTO degree_course (degree_id, course_id)
VALUES (3, 3);

INSERT INTO degree_course (degree_id, course_id)
VALUES (4, 4);

INSERT INTO degree_course (degree_id, course_id)
VALUES (5, 5);

INSERT INTO degree_course (degree_id, course_id)
VALUES (6, 6);

INSERT INTO degree_course (degree_id, course_id)
VALUES (7, 7);

INSERT INTO degree_course (degree_id, course_id)
VALUES (8, 8);

INSERT INTO degree_course (degree_id, course_id)
VALUES (9, 9);

INSERT INTO degree_course (degree_id, course_id)
VALUES (10, 10);

INSERT INTO degree_course (degree_id, course_id)
VALUES (11, 11);

INSERT INTO degree_course (degree_id, course_id)
VALUES (12, 12);

INSERT INTO degree_course (degree_id, course_id)
VALUES (13, 13);

INSERT INTO degree_course (degree_id, course_id)
VALUES (14, 14);

INSERT INTO degree_course (degree_id, course_id)
VALUES (15, 15);

INSERT INTO degree_course (degree_id, course_id)
VALUES (16, 16);

INSERT INTO degree_course (degree_id, course_id)
VALUES (17, 17);

INSERT INTO degree_course (degree_id, course_id)
VALUES (18, 18);

INSERT INTO degree_course (degree_id, course_id)
VALUES (19, 19);

INSERT INTO degree_course (degree_id, course_id)
VALUES (20, 20);