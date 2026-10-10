package v6;


public class Matiere {

    private int    id;
    private String nom;
    private String code;
    private String volumeHoraire;
    private String type;

    public Matiere(int id, String nom, String code, String volumeHoraire, String type) {
        this.id            = id;
        this.nom           = nom;
        this.code          = code;
        this.volumeHoraire = volumeHoraire;
        this.type          = type;
    }

    public int    getId()            { return id; }
    public String getNom()           { return nom; }
    public String getCode()          { return code; }
    public String getVolumeHoraire() { return volumeHoraire; }
    public String getType()          { return type; }

    public void setId(int id)               { this.id = id; }
    public void setNom(String v)            { this.nom = v; }
    public void setCode(String v)           { this.code = v; }
    public void setVolumeHoraire(String v)  { this.volumeHoraire = v; }
    public void setType(String v)           { this.type = v; }
}
