


USE eduschedule;

ALTER TABLE utilisateur
    ADD COLUMN enseignant_id INT NULL,
    ADD COLUMN groupe_id     INT NULL,
    ADD CONSTRAINT fk_user_ens FOREIGN KEY (enseignant_id) REFERENCES enseignant(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_user_grp FOREIGN KEY (groupe_id)     REFERENCES groupe(id)     ON DELETE SET NULL;

ALTER TABLE seance
    ADD COLUMN enseignant_id INT NULL,
    ADD COLUMN salle_id      INT NULL,
    ADD COLUMN groupe_id     INT NULL,
    ADD COLUMN matiere_id    INT NULL,
    ADD CONSTRAINT fk_seance_ens FOREIGN KEY (enseignant_id) REFERENCES enseignant(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_seance_sal FOREIGN KEY (salle_id)      REFERENCES salle(id)      ON DELETE SET NULL,
    ADD CONSTRAINT fk_seance_grp FOREIGN KEY (groupe_id)     REFERENCES groupe(id)     ON DELETE SET NULL,
    ADD CONSTRAINT fk_seance_mat FOREIGN KEY (matiere_id)    REFERENCES matiere(id)    ON DELETE SET NULL;



UPDATE seance s JOIN enseignant e
    ON s.enseignant IN (e.nom, CONCAT(e.nom,' ',e.prenom), CONCAT(e.prenom,' ',e.nom))
   SET s.enseignant_id = e.id;
UPDATE seance s JOIN salle    x ON s.salle   = x.nom SET s.salle_id   = x.id;
UPDATE seance s JOIN groupe   g ON s.groupe  = g.nom SET s.groupe_id  = g.id;
UPDATE seance s JOIN matiere  m ON s.matiere = m.nom SET s.matiere_id = m.id;



UPDATE utilisateur u JOIN enseignant e
    ON u.identifiant IN (e.nom, CONCAT(e.nom,' ',e.prenom), CONCAT(e.prenom,' ',e.nom))
   SET u.enseignant_id = e.id
 WHERE u.role = 'Enseignant';



