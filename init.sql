DROP SCHEMA IF EXISTS InternshipManagement CASCADE;
CREATE SCHEMA InternshipManagement;

CREATE TABLE InternshipManagement.users(
                                           id_user SERIAL PRIMARY KEY,
                                           lastname_user TEXT NOT NULL,
                                           firstname_user TEXT NOT NULL,
                                           email TEXT NOT NULL,
                                           phone_number TEXT NOT NULL,
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
                                                trade_name TEXT UNIQUE NOT NULL,
                                                designation TEXT UNIQUE,
                                                adresse TEXT NOT NULL,
                                                phone_number TEXT NOT NULL,
                                                email TEXT,
                                                blak_listed BOOLEAN NOT NULL,
                                                black_liste_motivation TEXT
);



CREATE TABLE InternshipManagement.contacts(
                                              id_contacts SERIAL PRIMARY KEY ,
                                              interview_method TEXT NOT NULL,
                                              tool TEXT,
                                              state_contact TEXT NOT NULL,

                                              student INT NOT NULL REFERENCES InternshipManagement.student (id_user),
                                              enterprise INT NOT NULL REFERENCES InternshipManagement.enterprise (id_enterprise),

                                              academic_year INT NOT NULL REFERENCES InternshipManagement.academic_year (id_academic_year)
);

CREATE TABLE InternshipManagement.internship_supervisor(
                                                           id_internship_supervisor SERIAL PRIMARY KEY,
                                                           last_name_supervisor TEXT NOT NULL,
                                                           first_name_supervisor TEXT NOT NULL,
                                                           phone_number TEXT NOT NULL,
                                                           email TEXT,
                                                           enterprise INT NOT NULL REFERENCES InternshipManagement.enterprise (id_enterprise)
);

CREATE TABLE InternshipManagement.internship(
                                                id_internship SERIAL PRIMARY KEY,
                                                signature_date DATE NOT NULL,
                                                internship_supervisor INT NOT NULL REFERENCES InternshipManagement.internship_supervisor (id_internship_supervisor),
                                                academic_year INT NOT NULL REFERENCES InternshipManagement.academic_year (id_academic_year)
);

INSERT INTO InternshipManagement.users VALUES (DEFAULT,'Erismann','Matteo',' "','+320470000001',CURRENT_DATE,'étudiant','$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe');
INSERT INTO InternshipManagement.users VALUES (DEFAULT,'Arbureanu','Teodor','teodor@vinci.be','+320470000002',CURRENT_DATE,'professeur','$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe');
INSERT INTO InternshipManagement.users VALUES (DEFAULT,'Admin', 'Admin','admin','+32470000000',CURRENT_DATE,'administrator','$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe');

INSERT INTO InternshipManagement.academic_year VALUES (DEFAULT,'2023-2024');

INSERT INTO InternshipManagement.student VALUES (1,1);

INSERT INTO InternshipManagement.enterprise VALUES (DEFAULT,'BE',null,'3 rue du test Bruxelles','+320470000003','be@entreprise.be',FALSE);