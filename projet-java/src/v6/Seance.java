package v6;


public class Seance {

    private int    id;
    private String jour;
    private String heure;
    private String enseignant;
    private String salle;
    private String groupe;
    private String matiere;

    public Seance(int id, String jour, String heure,
                  String enseignant, String salle, String groupe, String matiere) {
        this.id         = id;
        this.jour       = jour;
        this.heure      = heure;
        this.enseignant = enseignant;
        this.salle      = salle;
        this.groupe     = groupe;
        this.matiere    = matiere;
    }

    
    private int enseignantId, salleId, groupeId, matiereId;

    public Seance(int id, String jour, String heure, String enseignant, String salle,
                  String groupe, String matiere,
                  int enseignantId, int salleId, int groupeId, int matiereId) {
        this(id, jour, heure, enseignant, salle, groupe, matiere);
        this.enseignantId = enseignantId;
        this.salleId      = salleId;
        this.groupeId     = groupeId;
        this.matiereId    = matiereId;
    }

    public int  getEnseignantId() { return enseignantId; }
    public int  getSalleId()      { return salleId; }
    public int  getGroupeId()     { return groupeId; }
    public int  getMatiereId()    { return matiereId; }

    public int    getId()          { return id; }
    public String getJour()        { return jour; }
    public String getHeure()       { return heure; }
    public String getEnseignant()  { return enseignant; }
    public String getSalle()       { return salle; }
    public String getGroupe()      { return groupe; }
    public String getMatiere()     { return matiere; }

    public void setId(int id)             { this.id = id; }
    public void setJour(String v)         { this.jour = v; }
    public void setHeure(String v)        { this.heure = v; }
    public void setEnseignant(String v)   { this.enseignant = v; }
    public void setSalle(String v)        { this.salle = v; }
    public void setGroupe(String v)       { this.groupe = v; }
    public void setMatiere(String v)      { this.matiere = v; }
}
