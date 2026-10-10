package v6;


public class Groupe {

    private int    id;
    private String nom;
    private String niveau;
    private String section;
    private String effectif;

    public Groupe(int id, String nom, String niveau, String section, String effectif) {
        this.id       = id;
        this.nom      = nom;
        this.niveau   = niveau;
        this.section  = section;
        this.effectif = effectif;
    }

    public int    getId()       { return id; }
    public String getNom()      { return nom; }
    public String getNiveau()   { return niveau; }
    public String getSection()  { return section; }
    public String getEffectif() { return effectif; }

    public void setId(int id)          { this.id = id; }
    public void setNom(String v)       { this.nom = v; }
    public void setNiveau(String v)    { this.niveau = v; }
    public void setSection(String v)   { this.section = v; }
    public void setEffectif(String v)  { this.effectif = v; }
}
