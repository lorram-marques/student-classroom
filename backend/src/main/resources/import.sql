INSERT INTO tb_student (first_name, last_name, enrollment, birthdate) VALUES ('Anderson', 'Silva', 314511321, '2007-08-23T10:30:00Z');
INSERT INTO tb_student (first_name, last_name, enrollment, birthdate) VALUES ('Bruno', 'Lima', 424612471, '2007-07-17T10:30:00Z');
INSERT INTO tb_student (first_name, last_name, enrollment, birthdate) VALUES ('Carlos', 'Brito', 314511321, '2004-02-21T10:30:00Z');

INSERT INTO tb_classroom (grade, team) VALUES (1, 'A');
INSERT INTO tb_classroom (grade, team) VALUES (2, 'B');
INSERT INTO tb_classroom (grade, team) VALUES (3, 'c');

INSERT INTO tb_student_classroom (student_id, classroom_id) VALUES (1, 1);
INSERT INTO tb_student_classroom (student_id, classroom_id) VALUES (2, 1);
INSERT INTO tb_student_classroom (student_id, classroom_id) VALUES (3, 1);