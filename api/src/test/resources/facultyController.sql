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

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (101, 'BACHELOR_OF_SCIENCE', 'Computer Science', 101, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (102, 'MASTER', 'Software Engineering', 101, 'This is another description', 4.0, 21500.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (103, 'BACHELOR', 'Nano', 102, 'This is test description', 3.5, 9000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (104, 'BACHELOR_OF_SCIENCE', 'Math', 102, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (105, 'PROFESSOR', 'Computer Science', 103, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (106, 'DOCTOR', 'Physics', 103, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (107, 'BACHELOR', 'Nano', 104, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (108, 'DOCTOR', 'Computer Science', 104, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (109, 'BACHELOR_OF_SCIENCE', 'Math', 105, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (110, 'MASTER', 'Electronics', 105, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (111, 'DOCTOR', 'Nano', 106, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (112, 'BACHELOR_OF_SCIENCE', 'Physics', 106, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (113, 'DOCTOR', 'Math', 107, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (114, 'MASTER', 'Software Engineering', 107, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (115, 'BACHELOR', 'Electronics', 108, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (116, 'BACHELOR', 'Math', 108, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (117, 'DOCTOR', 'Nano', 109, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (118, 'PROFESSOR', 'Electronics', 110, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (119, 'BACHELOR', 'Physics', 111, 'This is test description', 3.0, 15000.00);

INSERT INTO degree (id, title, field_of_study, faculty, description, length_of_study, tuition_fee_per_year)
VALUES (120, 'DOCTOR', 'Math', 112, 'This is test description', 3.0, 15000.00);

-- Degree languages

INSERT INTO languages_table (degree_id, language)
VALUES (101, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (102, 'POLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (103, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (103, 'POLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (104, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (105, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (106, 'POLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (107, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (108, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (109, 'POLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (110, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (111, 'POLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (112, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (113, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (114, 'POLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (115, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (116, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (116, 'POLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (117, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (118, 'POLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (119, 'ENGLISH');

INSERT INTO languages_table (degree_id, language)
VALUES (120, 'POLISH');



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

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, description, language)
VALUES (1, 'Introduction to Programming', 40, 6, 101, 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, description, language)
VALUES (2, 'Data Structures and Algorithms', 60, 7, 101 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language)
VALUES (3, 'Software Engineering Principles', 50, 8, 101, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language)
VALUES (4, 'Cell Biology', 45, 9, 102, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language)
VALUES (5, 'Genetics', 50, 10, 102, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (6, 'Electromagnetics', 55, 6, 103, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (7, 'Quantum Mechanics', 60, 7, 103, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (8, 'Organic Chemistry', 45, 8, 104, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (9, 'Inorganic Chemistry', 40, 9, 104, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (10, 'Classical Mechanics', 55, 10, 105, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (11, 'Introduction to Electronics', 50, 6, 105, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (12, 'Number Theory', 40, 7, 106, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (13, 'Calculus', 60, 8, 106, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (14, 'Geography of World', 45, 9, 107, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (15, 'Historical Events', 50, 10, 108, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (16, 'Philosophy and Ethics', 55, 7, 109, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (17, 'Introduction to Psychology', 40, 8, 110, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (18, 'Sociological Theories', 45, 6, 111, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (19, 'Criminal Law', 50, 9, 112, 'ENGLISH');

INSERT INTO course (id, title, duration_in_hours, teacher, faculty, language))
VALUES (20, 'Microeconomics', 60, 10, 113, 'ENGLISH');

-- Course scopes

INSERT INTO course_scope (course_id, scope)
VALUES (1, 'LABORATORY');

INSERT INTO course_scope (course_id, scope)
VALUES (2, 'LECTURES');

INSERT INTO course_scope (course_id, scope)
VALUES (3, 'EXERCISES');

INSERT INTO course_scope (course_id, scope)
VALUES (4, 'PROJECT');

INSERT INTO course_scope (course_id, scope)
VALUES (5, 'LECTURES');

INSERT INTO course_scope (course_id, scope)
VALUES (6, 'EXERCISES');

INSERT INTO course_scope (course_id, scope)
VALUES (7, 'LABORATORY');

INSERT INTO course_scope (course_id, scope)
VALUES (8, 'PROJECT');

INSERT INTO course_scope (course_id, scope)
VALUES (9, 'LECTURES');

INSERT INTO course_scope (course_id, scope)
VALUES (10, 'EXERCISES');

INSERT INTO course_scope (course_id, scope)
VALUES (11, 'LABORATORY');

INSERT INTO course_scope (course_id, scope)
VALUES (12, 'PROJECT');

INSERT INTO course_scope (course_id, scope)
VALUES (13, 'LECTURES');

INSERT INTO course_scope (course_id, scope)
VALUES (14, 'EXERCISES');

INSERT INTO course_scope (course_id, scope)
VALUES (15, 'LABORATORY');

INSERT INTO course_scope (course_id, scope)
VALUES (16, 'PROJECT');

INSERT INTO course_scope (course_id, scope)
VALUES (17, 'LECTURES');

INSERT INTO course_scope (course_id, scope)
VALUES (18, 'EXERCISES');

INSERT INTO course_scope (course_id, scope)
VALUES (19, 'LABORATORY');

INSERT INTO course_scope (course_id, scope)
VALUES (20, 'PROJECT');

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

-- ARTICLES

INSERT INTO article (title, preview, content, author_id, category, faculty_id, image_name)
VALUES('TO THE NEW STUDENTS: A HEARTFELT WELCOME TO OUR COMMUNITY!', 'The strong pull of the University of Helsinki persists: more than 30,000 individuals applied to UH and 4,000 of them were admitted. The most popular programmes were law and medicine. Nearly half of those who applied to a master’s programme applied to the new Programme in the Development of Health Care Services.', 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 18, 'NEWS',101, '681c24e8-756f-462a-b119-8ed3cd68d830.webp');

INSERT INTO article (title, preview, content, author_id, category, faculty_id, image_name)
VALUES('TO THE NEW STUDENTS: A HEARTFELT WELCOME TO OUR COMMUNITY!', 'The strong pull of the University of Helsinki persists: more than 30,000 individuals applied to UH and 4,000 of them were admitted. The most popular programmes were law and medicine. Nearly half of those who applied to a master’s programme applied to the new Programme in the Development of Health Care Services.', 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 12, 'NEWS',101, '681c24e8-756f-462a-b119-8ed3cd68d830.webp');

INSERT INTO article (title, preview, content, author_id, category, faculty_id, image_name)
VALUES('TO THE NEW STUDENTS: A HEARTFELT WELCOME TO OUR COMMUNITY!', 'The strong pull of the University of Helsinki persists: more than 30,000 individuals applied to UH and 4,000 of them were admitted. The most popular programmes were law and medicine. Nearly half of those who applied to a master’s programme applied to the new Programme in the Development of Health Care Services.', 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 9, 'NEWS',101, '681c24e8-756f-462a-b119-8ed3cd68d830.webp');

INSERT INTO article (title, preview, content, author_id, category, faculty_id, image_name)
VALUES('TO THE NEW STUDENTS: A HEARTFELT WELCOME TO OUR COMMUNITY!', 'The strong pull of the University of Helsinki persists: more than 30,000 individuals applied to UH and 4,000 of them were admitted. The most popular programmes were law and medicine. Nearly half of those who applied to a master’s programme applied to the new Programme in the Development of Health Care Services.', 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 1, 'NEWS',101, '681c24e8-756f-462a-b119-8ed3cd68d830.webp');

INSERT INTO article (title, preview, content, author_id, category, faculty_id, image_name)
VALUES('TO THE NEW STUDENTS: A HEARTFELT WELCOME TO OUR COMMUNITY!', 'The strong pull of the University of Helsinki persists: more than 30,000 individuals applied to UH and 4,000 of them were admitted. The most popular programmes were law and medicine. Nearly half of those who applied to a master’s programme applied to the new Programme in the Development of Health Care Services.', 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 16, 'SCIENCE',101, '681c24e8-756f-462a-b119-8ed3cd68d830.webp');

INSERT INTO article (title, preview, content, author_id, category, faculty_id, image_name)
VALUES('TO THE NEW STUDENTS: A HEARTFELT WELCOME TO OUR COMMUNITY!', 'The strong pull of the University of Helsinki persists: more than 30,000 individuals applied to UH and 4,000 of them were admitted. The most popular programmes were law and medicine. Nearly half of those who applied to a master’s programme applied to the new Programme in the Development of Health Care Services.', 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 7, 'SCIENCE',101, '681c24e8-756f-462a-b119-8ed3cd68d830.webp');

INSERT INTO article (title, preview, content, author_id, category, faculty_id, image_name)
VALUES('TO THE NEW STUDENTS: A HEARTFELT WELCOME TO OUR COMMUNITY!', 'The strong pull of the University of Helsinki persists: more than 30,000 individuals applied to UH and 4,000 of them were admitted. The most popular programmes were law and medicine. Nearly half of those who applied to a master’s programme applied to the new Programme in the Development of Health Care Services.', 'This year, the University of Helsinki was again the most popular higher education institution in Finland, in terms of both the total number of applicants in the spring joint application procedure and the number of first-priority applications. With the number of applicants in the joint application procedure growing by 1.5% from last year, the attractiveness of the University of Helsinki has remained very strong.   In the joint application procedure, 4000 new students were admitted to the University of Helsinki. There were about 27,300 applicants to bachelor''s programmes, of whom 13,1% received a student place. A total of 3,200 people applied for Finnish- and Swedish-language master''s programmes, of whom 13,7% were admitted.   I am excited and proud to welcome you, our new students, to our University! Congratulations on your admission -- you have earned this achievement thanks to your hard work and determination. The stage in your lives you are now embarking on will open the door to countless opportunities and new challenges, says Rector **Sari Lindblom**   Also keep in mind that learning at university is about more than just completing courses. It is a journey to self-awareness and personal growth. Don''t hesitate to turn to your teachers and supervisors when you need support. We are here for yo   The University of Helsinki and its community have been building the future and transforming the world for more than 380 years. Their aim is to continue to provide students with the best tools for studies and life.   We encourage independent and critical thinking and competent citizenship in our students. We strive to support the development of our students and provide them with the capacity to act as agents of change in society. Our University is a multicultural and international community where students meet peers from different cultures and backgrounds. This diversity opens up new perspectives for them and promotes international solidarity. At the same time, it prepares them for the global labour market and offers them valuable networking opportunities, Rector Lindblom notes.   Law and medicine the most popular degree programme  ----------------------------------------------------', 2, 'SCIENCE',101, '681c24e8-756f-462a-b119-8ed3cd68d830.webp');
