DROP SCHEMA IF EXISTS InternshipManagement CASCADE;
CREATE SCHEMA InternshipManagement;

CREATE TABLE InternshipManagement.users
(
    id_user           SERIAL PRIMARY KEY,
    lastname_user     TEXT NOT NULL,
    firstname_user    TEXT NOT NULL,
    email             TEXT NOT NULL,
    phone_number      TEXT,
    registration_date DATE NOT NULL,
    role_user         TEXT NOT NULL,
    password_user     TEXT NOT NULL,
    version           INT  NOT NULL
);

CREATE TABLE InternshipManagement.academic_year
(
    id_academic_year SERIAL PRIMARY KEY,
    academic_year    TEXT NOT NULL
);

CREATE TABLE InternshipManagement.student
(
    id_user       SERIAL REFERENCES InternshipManagement.users (id_user) PRIMARY KEY,
    academic_year INT NOT NULL REFERENCES InternshipManagement.academic_year (id_academic_year)
);

CREATE TABLE InternshipManagement.enterprise
(
    id_enterprise           SERIAL PRIMARY KEY,
    trade_name              TEXT,
    designation             TEXT,
    address                 TEXT,
    phone_number            TEXT,
    city                    TEXT,
    email                   TEXT,
    black_listed            BOOLEAN,
    black_listed_motivation TEXT,
    version                 INT NOT NULL
);



CREATE TABLE InternshipManagement.contacts
(
    id_contacts      SERIAL PRIMARY KEY,
    interview_method TEXT,
    tool             TEXT,
    refusal_reason   TEXT,
    state_contact    TEXT NOT NULL,
    student          INT  NOT NULL REFERENCES InternshipManagement.student (id_user),
    enterprise       INT  NOT NULL REFERENCES InternshipManagement.enterprise (id_enterprise),
    academic_year    INT  NOT NULL REFERENCES InternshipManagement.academic_year (id_academic_year),
    version          INT  NOT NULL
);

CREATE TABLE InternshipManagement.internship_supervisor
(
    id_internship_supervisor SERIAL PRIMARY KEY,
    last_name_supervisor     TEXT,
    first_name_supervisor    TEXT,
    phone_number             TEXT,
    email                    TEXT,
    enterprise               INT NOT NULL REFERENCES InternshipManagement.enterprise (id_enterprise)
);

CREATE TABLE InternshipManagement.internship
(
    id_internship         SERIAL PRIMARY KEY,
    subject               TEXT,
    signature_date        TEXT,
    internship_supervisor INT NOT NULL REFERENCES InternshipManagement.internship_supervisor (id_internship_supervisor),
    contact               INT NOT NULL REFERENCES InternshipManagement.contacts (id_contacts),
    academic_year         INT NOT NULL REFERENCES InternshipManagement.academic_year (id_academic_year),
    version               INT NOT NULL
);
INSERT INTO InternshipManagement.academic_year
VALUES (DEFAULT, '2021-2022');
INSERT INTO InternshipManagement.academic_year
VALUES (DEFAULT, '2022-2023');
INSERT INTO InternshipManagement.academic_year
VALUES (DEFAULT, '2023-2024');

--**************************DEMOOOO********************************************************************************************************************************************************************************
--USERS & STUDENTS---------------------------------------------------------------------------

INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date,
                                        role_user, password_user, version)
VALUES ('Baroni', 'Raphaël', 'raphael.baroni@vinci.be', '0481 01 01 01', '2020-09-21', 'Professeur',
        '$2a$10$pvSmu2tvxsG8h8c5gqyzm..peYotb.tZtqFY62ZDM3ckCs0Sj8xly', 1),
       ('Lehmann', 'Brigitte', 'brigitte.lehmann@vinci.be', '0482 02 02 02', '2020-09-21', 'Professeur',
        '$2a$10$pvSmu2tvxsG8h8c5gqyzm..peYotb.tZtqFY62ZDM3ckCs0Sj8xly', 1),
       ('Leleux', 'Laurent', 'laurent.leleux@vinci.be', '0483 03 03 03', '2020-09-21', 'Professeur',
        '$2a$10$pvSmu2tvxsG8h8c5gqyzm..peYotb.tZtqFY62ZDM3ckCs0Sj8xly', 1),
       ('Lancaster', 'Annouck', 'annouck.lancaster@vinci.be', '0484 04 04 04', '2020-09-21', 'Administratif',
        '$2a$10$vdk22vK62VCKrQz1N7pPt.I4V1isXoCcrJ9TgU65wFhU0hJq/3ooO', 1),
       ('Skile', 'Elle', 'elle.skile@student.vinci.be', '0491 00 00 01', '2021-09-21', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Ilotie', 'Basile', 'basile.ilotie@student.vinci.be', '0491 00 00 11', '2021-09-21', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Frilot', 'Basile', 'basile.frilot@student.vinci.be', '0491 00 00 21', '2021-09-21', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Ilot', 'Basile', 'basile.ilot@student.vinci.be', '0492 00 00 01', '2021-09-21', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Dito', 'Arnaud', 'arnaud.dito@student.vinci.be', '0493 00 00 01', '2021-09-21', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Dilo', 'Arnaud', 'arnaud.dilo@student.vinci.be', '0494 00 00 01', '2021-09-21', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Dilot', 'Cedric', 'cedric.dilot@student.vinci.be', '0495 00 00 01', '2021-09-21', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Linot', 'Auristelle', 'auristelle.linot@student.vinci.be', '0496 00 00 01', '2021-09-21', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Demoulin', 'Basile', 'basile.demoulin@student.vinci.be', '0496 00 00 02', '2022-09-23', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Moulin', 'Arthur', 'arthur.moulin@student.vinci.be', '0497 00 00 02', '2022-09-23', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Moulin', 'Hugo', 'hugo.moulin@student.vinci.be', '0497 00 00 03', '2022-09-23', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Demoulin', 'Jeremy', 'jeremy.demoulin@student.vinci.be', '0497 00 00 20', '2022-09-23', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Mile', 'Aurèle', 'aurele.mile@student.vinci.be', '0497 00 00 21', '2022-09-23', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Mile', 'Frank', 'frank.mile@student.vinci.be', '0497 00 00 75', '2022-09-27', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Dumoulin', 'Basile', 'basile.dumoulin@student.vinci.be', '0497 00 00 58', '2022-09-27', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Dumoulin', 'Axel', 'axel.dumoulin@student.vinci.be', '0497 00 00 97', '2022-09-27', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Line', 'Caroline', 'caroline.line@student.vinci.be', '0486 00 00 01', '2023-09-18', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Ile', 'Achille', 'ach.ile@student.vinci.be', '0487 00 00 01', '2023-09-18', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Ile', 'Basile', 'basile.ile@student.vinci.be', '0488 00 00 01', '2023-09-18', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Skile', 'Achille', 'achille.skile@student.vinci.be', '0490 00 00 01', '2023-09-18', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Skile', 'Carole', 'carole.skile@student.vinci.be', '0489 00 00 01', '2023-09-18', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1),
       ('Ile', 'Théophile', 'theophile.ile@student.vinci.be', '0488 35 33 89', '2024-03-01', 'Etudiant',
        '$2a$10$3XnhqWGtxAIPSN0UfktkresEQzAOVhpqu0DIvUbhF9zAI/LWkeHYq', 1);

INSERT INTO InternshipManagement.student (id_user, academic_year)
VALUES ((SELECT id_user FROM InternshipManagement.users WHERE email = 'elle.skile@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ilotie@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.frilot@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ilot@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'arnaud.dito@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'arnaud.dilo@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'cedric.dilot@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'auristelle.linot@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.demoulin@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'arthur.moulin@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'hugo.moulin@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'jeremy.demoulin@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'aurele.mile@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'frank.mile@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.dumoulin@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'axel.dumoulin@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'caroline.line@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'ach.ile@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ile@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'achille.skile@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'carole.skile@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024')),
       ((SELECT id_user FROM InternshipManagement.users WHERE email = 'theophile.ile@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'));


--ENTERPRISES-----------------------------------------------------------------------
INSERT INTO InternshipManagement.enterprise (trade_name, phone_number, address, city, version)
VALUES ('Assyst Europe', '02.609.25.00', 'Avenue du Japon, 1/B9', '1420 Braine-l Alleud', 1),
       ('AXIS SRL', '02 752 17 60', 'Avenue de l Hélianthe, 63', '1180 Uccle', 1),
       ('Infrabel', '02 525 22 11', 'Rue Bara, 135', '1070 Bruxelles', 1),
       ('La route du papier', '02 586 16 65', 'Avenue des Mimosas, 83', '1150 Woluwe-Saint-Pierre', 1),
       ('LetsBuild', '014 54 67 54', 'Chaussée de Bruxelles, 135A', '1310 La Hulpe', 1),
       ('Niboo', '0487 02 79 13', 'Boulevard du Souverain, 24', '1170 Watermael-Boisfort', 1),
       ('Sopra Steria', '02 566 66 66', 'Avenue Arnaud Fraiteur, 15/23', '1050 Bruxelles', 1),
       ('The Bayard Partnership', '02 309 52 45', 'Grauwmeer, 1/57 bte 55', '3001 Leuven', 1);

--INTERNSHIP SUPERVISORS-----------------------------------------------------------
INSERT INTO InternshipManagement.internship_supervisor (last_name_supervisor, first_name_supervisor, phone_number,
                                                        email, enterprise)
VALUES ('Dossche', 'Stéphanie', '014.54.67.54', 'stephanie.dossche@letsbuild.com',
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'LetsBuild')),
       ('Alvarez Corchete', 'Roberto', '02.566.60.14', NULL,
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria')),
       ('Assal', 'Farid', '0474 39 69 09', 'f.assal@assyst-europe.com',
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Assyst Europe')),
       ('Ile', 'Emile', '0489 32 16 54', NULL,
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'La route du papier')),
       ('Hibo', 'Owln', '0456 678 567', NULL,
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Infrabel')),
       ('Barn', 'Henri', '02 752 17 60', NULL,
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'AXIS SRL'));

--CONTACTS------------------------------------------------------------------------

INSERT INTO InternshipManagement.contacts (academic_year, student, enterprise, state_contact, interview_method,
                                           refusal_reason, version)
VALUES ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'carole.skile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'LetsBuild'),
        'accepté', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'ach.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'accepté', 'Dans l''entreprise', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'ach.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Niboo'),
        'refusé', 'A distance', 'N''ont pas accepté d''avoir un entretien', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Assyst Europe'),
        'accepté', 'Dans l''entreprise', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'LetsBuild'),
        'suspendu', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'suspendu', NULL, NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Niboo'),
        'refusé', 'Dans l''entreprise', 'ne prennent qu''un seul étudiant', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'caroline.line@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Niboo'),
        'refusé', 'A distance', 'Pas d’affinité avec le l’ERP Odoo', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'caroline.line@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'non suivi', NULL, NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'caroline.line@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'LetsBuild'),
        'pris', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'theophile.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'initié', NULL, NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'theophile.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Niboo'),
        'initié', NULL, NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'theophile.ile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'LetsBuild'),
        'initié', NULL, NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'achille.skile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'initié', NULL, NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'elle.skile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'La route du papier'),
        'accepté', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ilot@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'non suivi', NULL, NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.frilot@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'The Bayard Partnership'),
        'refusé', 'A distance', 'ne prennent pas de stage', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'arnaud.dito@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'accepté', 'Dans l''entreprise', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'arnaud.dilo@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'accepté', 'Dans l''entreprise', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'cedric.dilot@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Assyst Europe'),
        'accepté', 'Dans l''entreprise', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'cedric.dilot@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'refusé', 'Dans l''entreprise', 'Choix autre étudiant', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'auristelle.linot@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Infrabel'),
        'accepté', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'auristelle.linot@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'suspendu', NULL, NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'auristelle.linot@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Niboo'),
        'refusé', 'A distance', 'Choix autre étudiant', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'jeremy.demoulin@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Assyst Europe'),
        'accepté', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'arthur.moulin@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'AXIS SRL'),
        'accepté', 'Dans l''entreprise', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'hugo.moulin@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'AXIS SRL'),
        'accepté', 'Dans l''entreprise', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'aurele.mile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'AXIS SRL'),
        'accepté', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'frank.mile@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'AXIS SRL'),
        'accepté', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.dumoulin@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'AXIS SRL'),
        'refusé', 'Dans l''entreprise', 'Entretien n''a pas eu lieu', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.dumoulin@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Niboo'),
        'refusé', 'Dans l''entreprise', 'Entretien n''a pas eu lieu', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.dumoulin@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'refusé', 'A distance', 'Entretien n''a pas eu lieu', 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'axel.dumoulin@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'accepté', 'A distance', NULL, 1),
       ((SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'),
        (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.frilot@student.vinci.be'),
        (SELECT id_enterprise FROM InternshipManagement.enterprise WHERE trade_name = 'Sopra Steria'),
        'refusé', 'A distance', 'Choix autre étudiant', 1);

--********************************Internships************************************************************************************************************************************************************************************

INSERT INTO InternshipManagement.internship (subject, signature_date, internship_supervisor, contact, academic_year,
                                             version)
VALUES ('Un ERP:Odoo', '10-10-2023',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Dossche'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND
             student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'carole.skile@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'), 1),
       ('sBMS project - a complex environment', '23-11-2023',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Alvarez Corchete'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'ach.ile@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'), 1),
       ('CRM : Microsoft Dynamics 365 For Sales', '12-10-2023',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Assal'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'basile.ile@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'), 1),
       ('Conservation et restauration d’œuvres d’art', '2021-11-25',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Ile'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'elle.skile@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'), 1),
       ('L''analyste au centre du développement', '2021-11-17',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Alvarez Corchete'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'arnaud.dito@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'), 1),
       ('L''analyste au centre du développement', '2021-11-17',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Alvarez Corchete'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'arnaud.dilo@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'), 1),
       ('ERP : Microsoft Dynamics 366', '2021-11-23',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Assal'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND
             student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'cedric.dilot@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'), 1),
       ('Entretien des rails', '2021-11-22',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Hibo'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student =
               (SELECT id_user FROM InternshipManagement.users WHERE email = 'auristelle.linot@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2021-2022'), 1),
       ('Un métier : chef de projet', '2022-10-19',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Barn'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND
             student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'arthur.moulin@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'), 1),
       ('Un métier : chef de projet', '2022-10-19',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Barn'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'hugo.moulin@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'), 1),
       ('Un métier : chef de projet', '2022-10-19',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Barn'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'aurele.mile@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'), 1),
       ('Un métier : chef de projet', '2022-10-19',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Barn'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'frank.mile@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'), 1),
       ('sBMS project - Java Development', '2022-10-17',
        (SELECT id_internship_supervisor
         FROM InternshipManagement.internship_supervisor
         WHERE last_name_supervisor = 'Alvarez Corchete'),
        (SELECT id_contacts
         FROM InternshipManagement.contacts
         WHERE state_contact = 'accepté'
           AND
             student = (SELECT id_user FROM InternshipManagement.users WHERE email = 'axel.dumoulin@student.vinci.be')),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2022-2023'), 1);


--***************************OUR TESTS****************************************************************************************************************************************************************************
INSERT INTO InternshipManagement.users
VALUES (DEFAULT, 'Erismann', 'Matteo', 'matteo@student.vinci.be', '+320470000001', CURRENT_DATE, 'Etudiant',
        '$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe', 1);
INSERT INTO InternshipManagement.student
VALUES ((SELECT id_user FROM InternshipManagement.users WHERE email = 'matteo@student.vinci.be'),
        (SELECT id_academic_year FROM InternshipManagement.academic_year WHERE academic_year = '2023-2024'));
INSERT INTO InternshipManagement.users
VALUES (DEFAULT, 'Arbureanu', 'Teodor', 'teodor@vinci.be', '+320470000002', CURRENT_DATE, 'Professeur',
        '$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe', 1);
INSERT INTO InternshipManagement.users
VALUES (DEFAULT, 'Admin', 'Admin', 'admin', '+32470000000', CURRENT_DATE, 'Administratif',
        '$2a$10$6Vkfvd7L5XwsLSLDj/6flug4l9z1DphKelINuGAmjuxA0xed0VkDe', 1);
--*****************************************************************************************************************************************************************************************************************

--DEMO 1
/*SELECT COUNT(*) as nbr_users FROM InternshipManagement.users;
SELECT COUNT(*) as nbr_entreprises FROM InternshipManagement.enterprise;
SELECT COUNT(*) as nbr_internships, ay.academic_year FROM InternshipManagement.internship i, InternshipManagement.academic_year  ay
WHERE i.academic_year= ay.id_academic_year
GROUP BY ay.academic_year;

SELECT COUNT(*) as nbr_contacts, ay.academic_year FROM InternshipManagement.contacts c, InternshipManagement.academic_year  ay
WHERE c.academic_year= ay.id_academic_year
GROUP BY ay.academic_year;

SELECT c.state_contact, COUNT(*) as nbr_contacts_by_state
FROM InternshipManagement.contacts c
GROUP BY c.state_contact;*/

--DEMO 2
--Comptage du nombre d’utilisateurs, par rôle et par année académique.
/*SELECT u.role_user, a.academic_year, COUNT(*) as user_count
FROM InternshipManagement.users u
LEFT JOIN InternshipManagement.student s ON u.id_user = s.id_user
LEFT JOIN InternshipManagement.academic_year a ON s.academic_year = a.id_academic_year
GROUP BY u.role_user, a.academic_year;*/
