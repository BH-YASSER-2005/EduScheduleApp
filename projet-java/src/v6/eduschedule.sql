

CREATE DATABASE IF NOT EXISTS eduschedule
    CHARACTER SET utf8
    COLLATE utf8_general_ci;

USE eduschedule;


CREATE TABLE IF NOT EXISTS utilisateur (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    identifiant  VARCHAR(100) NOT NULL,
    mot_de_passe VARCHAR(100) NOT NULL,
    role         VARCHAR(50)  NOT NULL,
    enseignant_id INT NULL,   
    groupe_id     INT NULL    
);



CREATE TABLE IF NOT EXISTS enseignant (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nom        VARCHAR(100) NOT NULL,
    prenom     VARCHAR(100) NOT NULL,
    email      VARCHAR(150),
    specialite VARCHAR(100)
);




CREATE TABLE IF NOT EXISTS groupe (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nom      VARCHAR(100) NOT NULL,
    niveau   VARCHAR(50),
    section  VARCHAR(50),
    effectif VARCHAR(10)
);




CREATE TABLE IF NOT EXISTS salle (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nom      VARCHAR(100) NOT NULL,
    type     VARCHAR(50),
    capacite VARCHAR(10),
    batiment VARCHAR(100)
);




CREATE TABLE IF NOT EXISTS matiere (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nom            VARCHAR(100) NOT NULL,
    code           VARCHAR(50),
    volume_horaire VARCHAR(20),
    type           VARCHAR(50)
);




CREATE TABLE IF NOT EXISTS seance (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    jour       VARCHAR(20),
    heure      VARCHAR(20),
    enseignant VARCHAR(150),
    salle      VARCHAR(100),
    groupe     VARCHAR(100),
    matiere    VARCHAR(100),
    enseignant_id INT NULL,
    salle_id      INT NULL,
    groupe_id     INT NULL,
    matiere_id    INT NULL
);


ALTER TABLE utilisateur
    ADD CONSTRAINT fk_user_ens FOREIGN KEY (enseignant_id) REFERENCES enseignant(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_user_grp FOREIGN KEY (groupe_id)     REFERENCES groupe(id)     ON DELETE SET NULL;
ALTER TABLE seance
    ADD CONSTRAINT fk_seance_ens FOREIGN KEY (enseignant_id) REFERENCES enseignant(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_seance_sal FOREIGN KEY (salle_id)      REFERENCES salle(id)      ON DELETE SET NULL,
    ADD CONSTRAINT fk_seance_grp FOREIGN KEY (groupe_id)     REFERENCES groupe(id)     ON DELETE SET NULL,
    ADD CONSTRAINT fk_seance_mat FOREIGN KEY (matiere_id)    REFERENCES matiere(id)    ON DELETE SET NULL;



INSERT INTO utilisateur (identifiant, mot_de_passe, role)
VALUES ('admin', '123', 'Admin');
