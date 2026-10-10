package v6;


public class Enseignant {

    private int    id;
    private String nom;
    private String prenom;
    private String email;
    private String specialite;

    public Enseignant(int id, String nom, String prenom, String email, String specialite) {
        this.id         = id;
        this.nom        = nom;
        this.prenom     = prenom;
        this.email      = email;
        this.specialite = specialite;
    }

    public int    getId()         { return id; }
    public String getNom()        { return nom; }
    public String getPrenom()     { return prenom; }
    public String getEmail()      { return email; }
    public String getSpecialite() { return specialite; }

    public void setId(int id)            { this.id = id; }
    public void setNom(String v)         { this.nom = v; }
    public void setPrenom(String v)      { this.prenom = v; }
    public void setEmail(String v)       { this.email = v; }
    public void setSpecialite(String v)  { this.specialite = v; }
}
