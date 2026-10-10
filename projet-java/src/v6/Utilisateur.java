package v6;


public class Utilisateur {

    private int    id;
    private String identifiant;
    private String motDePasse;
    private String role;

    public Utilisateur(int id, String identifiant, String motDePasse, String role) {
        this.id          = id;
        this.identifiant = identifiant;
        this.motDePasse  = motDePasse;
        this.role        = role;
    }

    
    private int enseignantId;
    private int groupeId;

    public Utilisateur(int id, String identifiant, String motDePasse, String role,
                       int enseignantId, int groupeId) {
        this(id, identifiant, motDePasse, role);
        this.enseignantId = enseignantId;
        this.groupeId     = groupeId;
    }

    public int  getEnseignantId()         { return enseignantId; }
    public int  getGroupeId()             { return groupeId; }
    public void setEnseignantId(int v)    { this.enseignantId = v; }
    public void setGroupeId(int v)        { this.groupeId = v; }

    public int    getId()          { return id; }
    public String getIdentifiant() { return identifiant; }
    public String getMotDePasse()  { return motDePasse; }
    public String getRole()        { return role; }

    public void setId(int id)              { this.id = id; }
    public void setIdentifiant(String v)   { this.identifiant = v; }
    public void setMotDePasse(String v)    { this.motDePasse = v; }
    public void setRole(String v)          { this.role = v; }
}
