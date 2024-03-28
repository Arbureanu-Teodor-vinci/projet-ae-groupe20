DROP SCHEMA IF EXISTS InternshipManagement CASCADE;
CREATE SCHEMA InternshipManagement;

CREATE TABLE InternshipManagement.users(
                                           id_user SERIAL PRIMARY KEY,
                                           lastname_user TEXT NOT NULL,
                                           firstname_user TEXT NOT NULL,
                                           email TEXT NOT NULL,
                                           phone_number TEXT,
                                           registration_date DATE NOT NULL,
                                           role_user TEXT NOT NULL,
                                           password_user TEXT NOT NULL
);

CREATE TABLE InternshipManagement.academic_year(
                                                   id_academic_year SERIAL PRIMARY KEY,
                                                   academic_year TEXT NOT NULL
);

CREATE TABLE InternshipManagement.student(
                                             id_user SERIAL REFERENCES InternshipManagement.users (id_user) PRIMARY KEY,
                                             academic_year INT NOT NULL REFERENCES InternshipManagement.academic_year (id_academic_year)
);

CREATE TABLE InternshipManagement.enterprise(
                                                id_enterprise SERIAL PRIMARY KEY,
                                                trade_name TEXT,
                                                designation TEXT,
                                                address TEXT,
                                                phone_number TEXT,
                                                city TEXT,
                                                email TEXT,
                                                black_listed BOOLEAN,
                                                black_listed_motivation TEXT
);



CREATE TABLE InternshipManagement.contacts(
                                              id_contacts SERIAL PRIMARY KEY ,
                                              interview_method TEXT,
                                              tool TEXT,
                                              refusal_reason TEXT,
                                              state_contact TEXT NOT NULL,
                                              student INT NOT NULL REFERENCES InternshipManagement.student (id_user),
                                              enterprise INT NOT NULL REFERENCES InternshipManagement.enterprise (id_enterprise),
                                              academic_year INT NOT NULL REFERENCES InternshipManagement.academic_year (id_academic_year)
);

CREATE TABLE InternshipManagement.internship_supervisor(
                                                           id_internship_supervisor SERIAL PRIMARY KEY,
                                                           last_name_supervisor TEXT,
                                                           first_name_supervisor TEXT,
                                                           phone_number TEXT,
                                                           email TEXT,
                                                           enterprise INT NOT NULL REFERENCES InternshipManagement.enterprise (id_enterprise)
);

CREATE TABLE InternshipManagement.internship(
                                                id_internship SERIAL PRIMARY KEY,
                                                subject TEXT,
                                                signature_date TEXT,
                                                internship_supervisor INT NOT NULL REFERENCES InternshipManagement.internship_supervisor (id_internship_supervisor),
                                                contact INT NOT NULL REFERENCES InternshipManagement.contacts(id_contacts),
                                                academic_year INT NOT NULL REFERENCES InternshipManagement.academic_year (id_academic_year)
);
INSERT INTO InternshipManagement.academic_year VALUES (DEFAULT,'2023-2024');


--**************************DEMOOOO********************************************************************************************************************************************************************************
--USERS & STUDENTS---------------------------------------------------------------------------
INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('Baroni', 'Raphaël', 'raphael.baroni@vinci.be', '0481 01 01 01', '2020-09-21', 'Professeur', '$2a$10$BjSQSRskV/sJ9VPFVit1DeVdyyLcnv810nEgReb44Gj4k9UxKhywm');

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('Lehmann', 'Brigitte', 'brigitte.lehmann@vinci.be', '0482 02 02 02', '2020-09-21', 'Professeur', '$2a$10$BjSQSRskV/sJ9VPFVit1DeVdyyLcnv810nEgReb44Gj4k9UxKhywm');

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('Leleux', 'Laurent', 'laurent.leleux@vinci.be', '0483 03 03 03', '2020-09-21', 'Professeur', '$2a$10$BjSQSRskV/sJ9VPFVit1DeVdyyLcnv810nEgReb44Gj4k9UxKhywm');

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('Lancaster', 'Annouck', 'annouck.lancaster@vinci.be', '0484 04 04 04', '2020-09-21', 'Administratif', '$2a$10$k8qZQ.R25r5fm1/hXdp/N.DBJ/97eR8p2Yn/XTA7q10.Nuy1qTpaW');

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('Line', 'Caroline', 'Caroline.line@student.vinci.be', '0486 00 00 01', '2023-09-18', 'Etudiant', '$2a$10$5y4r5tE.EJpGaGoj09M8M.wlqTo9MKzVsbikNPevGXjAfGj686Ary');
INSERT INTO InternshipManagement.student VALUES (5,1);

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('Ile', 'Achille', 'Ach.ile@student.vinci.be', '0487 00 00 01', '2023-09-18', 'Etudiant', '$2a$10$5y4r5tE.EJpGaGoj09M8M.wlqTo9MKzVsbikNPevGXjAfGj686Ary');
INSERT INTO InternshipManagement.student VALUES (6,1);

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('Ile', 'Basile', 'Basile.Ile@student.vinci.be', '0488 00 00 01', '2023-09-18', 'Etudiant', '$2a$10$5y4r5tE.EJpGaGoj09M8M.wlqTo9MKzVsbikNPevGXjAfGj686Ary');
INSERT INTO InternshipManagement.student VALUES (7,1);

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('skile', 'Achille', 'Achille.skile@student.vinci.be', '0490 00 00 01', '2023-09-18', 'Etudiant', '$2a$10$5y4r5tE.EJpGaGoj09M8M.wlqTo9MKzVsbikNPevGXjAfGj686Ary');
INSERT INTO InternshipManagement.student VALUES (8,1);

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)
VALUES ('skile', 'Carole', 'Carole.skile@student.vinci.be', '0489 00 00 01', '2023-09-18', 'Etudiant', '$2a$10$5y4r5tE.EJpGaGoj09M8M.wlqTo9MKzVsbikNPevGXjAfGj686Ary');
INSERT INTO InternshipManagement.student VALUES (9,1);

--ENTERPRISES-----------------------------------------------------------------------
INSERT INTO InternshipManagement.enterprise(trade_name, designation, address, phone_number, city, email, black_listed, black_listed_motivation)
VALUES ('Assyst Europe',NULL,'Avenue du Japon, 1/B9','02.609.25.00', '1420 Braine-l Alleud', NULL,NULL,NULL);

INSERT INTO InternshipManagement.enterprise(trade_name, designation, address, phone_number, city, email, black_listed, black_listed_motivation)
VALUES ('LetsBuild',NULL,'Chaussée de Bruxelles, 135A','014 54 67 54', '1310 La Hulpe', NULL,NULL,NULL);

INSERT INTO InternshipManagement.enterprise(trade_name, designation, address, phone_number, city, email, black_listed, black_listed_motivation)
VALUES ('Niboo',NULL,'Boulevard du Souverain, 24','0487 02 79 13', '1170 Watermael-Boisfort', NULL,NULL,NULL);

INSERT INTO InternshipManagement.enterprise(trade_name, designation, address, phone_number, city, email, black_listed, black_listed_motivation)
VALUES ('Sopra Steria',NULL,'Avenue Arnaud Fraiteur, 15/23','02 566 66 66', '1050 Bruxelles', NULL,NULL,NULL);

--INTERNSHIP SUPERVISORS-----------------------------------------------------------
INSERT INTO InternshipManagement.internship_supervisor(last_name_supervisor, first_name_supervisor, phone_number, email, enterprise)
VALUES ('Dossche','Stéphanie','014.54.67.54','stephanie.dossche@letsbuild.com',2);

INSERT INTO InternshipManagement.internship_supervisor(last_name_supervisor, first_name_supervisor, phone_number, email, enterprise)
VALUES ('ALVAREZ CORCHETE','Roberto','02.566.60.14',NULL,4);

INSERT INTO InternshipManagement.internship_supervisor(last_name_supervisor, first_name_supervisor, phone_number, email, enterprise)
VALUES ('Assal','Farid','0474 39 69 09','f.assal@assyst-europe.com',1);

--CONTACTS------------------------------------------------------------------------
INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES ('A distance', NULL,'accepté',9,2,1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES ('Dans l entreprise', NULL, 'accepté', 6, 4, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES ('A distance', 'N ont pas accepté d avoir un entretien', 'refusé', 6, 3, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES ('Dans l entreprise', NULL, 'accepté', 7, 1, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES ('A distance', NULL, 'suspendu', 7, 2, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES (NULL, NULL, 'suspendu', 7, 4, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES ('Dans l entreprise', 'ne prennent qu un seul étudiant', 'refusé', 7, 3, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES ('A distance', NULL, 'pris', 5, 3, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES (NULL, NULL, 'initié', 5, 4, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES (NULL, NULL, 'initié', 5, 2, 1);

INSERT INTO InternshipManagement.contacts(interview_method, refusal_reason, state_contact, student, enterprise, academic_year)
VALUES (NULL, NULL, 'initié', 8, 4, 1);

--INTERNSHIPS---------------------------------------------------------------------------
INSERT INTO InternshipManagement.internship(subject, signature_date, internship_supervisor, contact, academic_year)
VALUES ('Un ERP : Odoo','10/10/2023',1,1,1);

INSERT INTO InternshipManagement.internship(subject, signature_date, internship_supervisor, contact, academic_year)
VALUES ('sBMS project - a complex environment','13/11/2023',2,2,1);

INSERT INTO InternshipManagement.internship(subject, signature_date, internship_supervisor, contact, academic_year)
VALUES ('CRM : Microsoft Dynamics 365 For Sales','12/10/2023',3,3,1);

--***************************OUR TESTS****************************************************************************************************************************************************************************
INSERT INTO InternshipManagement.users VALUES (DEFAULT,'Erismann','Matteo','matteo@student.vinci.be','+320470000001',CURRENT_DATE,'Etudiant','$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe');
INSERT INTO InternshipManagement.student VALUES (10,1);
INSERT INTO InternshipManagement.users VALUES (DEFAULT,'Arbureanu','Teodor','teodor@vinci.be','+320470000002',CURRENT_DATE,'Professeur','$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe');
INSERT INTO InternshipManagement.users VALUES (DEFAULT,'Admin', 'Admin','admin','+32470000000',CURRENT_DATE,'Administratif','$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe');
--*****************************************************************************************************************************************************************************************************************

