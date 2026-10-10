package v6;


public class Salle {

    private int    id;
    private String nom;
    private String type;
    private String capacite;
    private String batiment;

    public Salle(int id, String nom, String type, String capacite, String batiment) {
        this.id       = id;
        this.nom      = nom;
        this.type     = type;
        this.capacite = capacite;
        this.batiment = batiment;
    }

    public int    getId()       { return id; }
    public String getNom()      { return nom; }
    public String getType()     { return type; }
    public String getCapacite() { return capacite; }
    public String getBatiment() { return batiment; }

    public void setId(int id)          { this.id = id; }
    public void setNom(String v)       { this.nom = v; }
    public void setType(String v)      { this.type = v; }
    public void setCapacite(String v)  { this.capacite = v; }
    public void setBatiment(String v)  { this.batiment = v; }
}
