INSERT INTO tb_student (first_name, last_name, enrollment, birthdate) VALUES ('Anderson', 'Silva', 314511321, '2007-08-23T10:30:00Z');

INSERT INTO tb_classroom (grade, team) VALUES (1, 'A');
INSERT INTO tb_classroom (grade, team) VALUES (2, 'B');

INSERT INTO tb_student_classroom (student_id, classroom_id) VALUES (1, 1);