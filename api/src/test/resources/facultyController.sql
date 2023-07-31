-- Faculties

INSERT INTO faculty (id, name)
VALUES (101, 'Informatics');

INSERT INTO faculty (id, name)
VALUES (102, 'Biology');

INSERT INTO faculty (id, name)
VALUES (103, 'Electronics');

INSERT INTO faculty (id, name)
VALUES (104, 'Chemistry');

INSERT INTO faculty (id, name)
VALUES (105, 'Physics');

INSERT INTO faculty (id, name)
VALUES (106, 'Mathematics');

INSERT INTO faculty (id, name)
VALUES (107, 'Geography');

INSERT INTO faculty (id, name)
VALUES (108, 'History');

INSERT INTO faculty (id, name)
VALUES (109, 'Philosophy');

INSERT INTO faculty (id, name)
VALUES (110, 'Psychology');

INSERT INTO faculty (id, name)
VALUES (111, 'Sociology');

INSERT INTO faculty (id, name)
VALUES (112, 'Law');

INSERT INTO faculty (id, name)
VALUES (113, 'Economics');

-- Degrees

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (101, 'BACHELOR_OF_SCIENCE', 'Computer Science', 101);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (102, 'MASTER', 'Software Engineering', 101);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (103, 'BACHELOR', 'Nano', 102);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (104, 'BACHELOR_OF_SCIENCE', 'Math', 102);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (105, 'PROFESSOR', 'Computer Science', 103);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (106, 'DOCTOR', 'Physics', 103);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (107, 'BACHELOR', 'Nano', 104);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (108, 'DOCTOR', 'Computer Science', 104);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (109, 'BACHELOR_OF_SCIENCE', 'Math', 105);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (110, 'MASTER', 'Electronics', 105);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (111, 'DOCTOR', 'Nano', 106);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (112, 'BACHELOR_OF_SCIENCE', 'Physics', 106);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (113, 'DOCTOR', 'Math', 107);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (114, 'MASTER', 'Software Engineering', 107);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (115, 'BACHELOR', 'Electronics', 108);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (116, 'BACHELOR', 'Math', 108);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (117, 'DOCTOR', 'Nano', 109);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (118, 'PROFESSOR', 'Electronics', 110);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (119, 'BACHELOR', 'Physics', 111);

INSERT INTO degree (id, title, field_of_study, faculty)
VALUES (120, 'DOCTOR', 'Math', 112);


-- Users

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('John', 'Doe', 'johndoe@example.com', '12345', 101, 101, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Jane', 'Smith', 'janesmith@example.com', 'password123', 102, 103, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Michael', 'Johnson', 'michaeljohnson@example.com', 'test123', 103, 105, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Emily', 'Anderson', 'emily.anderson@example.com', 'password123', 104, 107, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Daniel', 'Wilson', 'daniel.wilson@example.com', 'test123', 105, 110, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Olivia', 'Martinez', 'olivia.martinez@example.com', 'securepass', 106, 111, 'ROLE_TEACHER');

INSERT INTO teacher_details (teacher_id, degree_field, title, bio, tutorship)
VALUES (6, 'Informatics', 'BACHELOR_OF_SCIENCE', 'I am a informatics teacher', 'I am a tutor');


INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('James', 'Garcia', 'james.garcia@example.com', 'mypassword', 107, 113, 'ROLE_TEACHER');

INSERT INTO teacher_details (teacher_id, degree_field, title, bio, tutorship)
VALUES (7, 'Computer Science', 'BACHELOR_OF_SCIENCE', 'I am a teacher', 'I am a tutor');


INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Ava', 'Thomas', 'ava.thomas@example.com', 'pass1234', 108, 115, 'ROLE_TEACHER');

INSERT INTO teacher_details (teacher_id, degree_field, title, bio, tutorship)
VALUES (8, 'Science', 'BACHELOR_OF_SCIENCE', 'I am a scientist', 'I am not tutoring');


INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('William', 'Robinson', 'william.robinson@example.com', 'myp@ssw0rd', 109, 117, 'ROLE_TEACHER');

INSERT INTO teacher_details (teacher_id, degree_field, title, bio, tutorship)
VALUES (9, 'Computer Science', 'BACHELOR', 'I am a teacher', 'I am a tutor');


INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Sophia', 'Walker', 'sophia.walker@example.com', 'password123!', 110, 118, 'ROLE_TEACHER');

INSERT INTO teacher_details (teacher_id, degree_field, title, bio, tutorship)
VALUES (10, 'Biology', 'PROFESSOR', 'I am a professor', 'I profess');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Benjamin', 'Young', 'benjamin.young@example.com', 'testpass', 111, 119, 'ROLE_ADMIN');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Isabella', 'Harris', 'isabella.harris@example.com', 'myp@ssword', 112, 120, 'ROLE_ADMIN');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Elijah', 'Lee', 'elijah.lee@example.com', 'mypass123', 113, 120, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Mia', 'Wright', 'mia.wright@example.com', 'password1234', 101, 102, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Oliver', 'Lopez', 'oliver.lopez@example.com', 'myp@ssword123', 102, 104, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Amelia', 'Hall', 'amelia.hall@example.com', 'testpassword', 103, 106, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Lucas', 'Scott', 'lucas.scott@example.com', 'mypassword123', 104, 108, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Aria', 'Perez', 'aria.perez@example.com', 'myp@ss123', 105, 110, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Alice', 'Smith', 'alicesmith@example.com', '54321', 101, 101, 'ROLE_STUDENT');

INSERT INTO users (first_name, last_name, email, password, faculty, role)
VALUES ('Bob', 'Johnson', 'bobjohnson@example.com', 'abcde', 101, 'ROLE_TEACHER');

INSERT INTO teacher_details (teacher_id, degree_field, title, bio, tutorship)
VALUES (20, 'Biology', 'PROFESSOR', 'I am a professor', 'I profess');

INSERT INTO users (first_name, last_name, email, password, faculty, degree, role)
VALUES ('Emily', 'Wilson', 'emilywilson@example.com', 'qwerty', 101, 102, 'ROLE_STUDENT');


-- Courses

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (1, 'Introduction to Programming', 40, 6, 101);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (2, 'Data Structures and Algorithms', 60, 7, 101);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (3, 'Software Engineering Principles', 50, 8, 101);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (4, 'Cell Biology', 45, 9, 102);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (5, 'Genetics', 50, 10, 102);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (6, 'Electromagnetics', 55, 6, 103);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (7, 'Quantum Mechanics', 60, 7, 103);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (8, 'Organic Chemistry', 45, 8, 104);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (9, 'Inorganic Chemistry', 40, 9, 104);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (10, 'Classical Mechanics', 55, 10, 105);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (11, 'Introduction to Electronics', 50, 6, 105);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (12, 'Number Theory', 40, 7, 106);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (13, 'Calculus', 60, 8, 106);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (14, 'Geography of World', 45, 9, 107);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (15, 'Historical Events', 50, 10, 108);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (16, 'Philosophy and Ethics', 55, 7, 109);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (17, 'Introduction to Psychology', 40, 8, 110);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (18, 'Sociological Theories', 45, 6, 111);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (19, 'Criminal Law', 50, 9, 112);

INSERT INTO course (id, title, duration_in_hours, teacher, faculty)
VALUES (20, 'Microeconomics', 60, 10, 113);

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
VALUES (101, 1);

INSERT INTO degree_course (degree_id, course_id)
VALUES (102, 2);

INSERT INTO degree_course (degree_id, course_id)
VALUES (103, 3);

INSERT INTO degree_course (degree_id, course_id)
VALUES (104, 4);

INSERT INTO degree_course (degree_id, course_id)
VALUES (105, 5);

INSERT INTO degree_course (degree_id, course_id)
VALUES (106, 6);

INSERT INTO degree_course (degree_id, course_id)
VALUES (107, 7);

INSERT INTO degree_course (degree_id, course_id)
VALUES (108, 8);

INSERT INTO degree_course (degree_id, course_id)
VALUES (109, 9);

INSERT INTO degree_course (degree_id, course_id)
VALUES (110, 10);

INSERT INTO degree_course (degree_id, course_id)
VALUES (111, 11);

INSERT INTO degree_course (degree_id, course_id)
VALUES (112, 12);

INSERT INTO degree_course (degree_id, course_id)
VALUES (113, 13);

INSERT INTO degree_course (degree_id, course_id)
VALUES (114, 14);

INSERT INTO degree_course (degree_id, course_id)
VALUES (115, 15);

INSERT INTO degree_course (degree_id, course_id)
VALUES (116, 16);

INSERT INTO degree_course (degree_id, course_id)
VALUES (117, 17);

INSERT INTO degree_course (degree_id, course_id)
VALUES (118, 18);

INSERT INTO degree_course (degree_id, course_id)
VALUES (119, 19);

INSERT INTO degree_course (degree_id, course_id)
VALUES (120, 20);