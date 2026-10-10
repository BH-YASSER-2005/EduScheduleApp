package v6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SeanceDAO extends DAO<Seance> {

    public SeanceDAO() throws SQLException {
        super();
    }

    
    public List<Seance> getTous() throws SQLException {
        List<Seance> liste = new ArrayList<>();
        String sql = "SELECT * FROM seance";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            liste.add(lireSeance(rs));
        }
        return liste;
    }

    
    public List<Seance> getParEnseignant(String nomEnseignant) throws SQLException {
        List<Seance> liste = new ArrayList<>();
        String sql = "SELECT * FROM seance WHERE enseignant = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, nomEnseignant);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            liste.add(lireSeance(rs));
        }
        return liste;
    }

    
    
    public List<Seance> getParEnseignantId(int enseignantId) throws SQLException {
        return requeteParId("SELECT * FROM seance WHERE enseignant_id = ?", enseignantId);
    }

    
    public List<Seance> getParGroupeId(int groupeId) throws SQLException {
        return requeteParId("SELECT * FROM seance WHERE groupe_id = ?", groupeId);
    }

    private List<Seance> requeteParId(String sql, int id) throws SQLException {
        List<Seance> liste = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(sql + " ORDER BY FIELD(jour,'Lundi','Mardi','Mercredi','Jeudi','Vendredi','Samedi'), heure");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) liste.add(lireSeance(rs));
        return liste;
    }

    private void setIdOuNull(PreparedStatement ps, int idx, int v) throws SQLException {
        if (v > 0) ps.setInt(idx, v); else ps.setNull(idx, java.sql.Types.INTEGER);
    }

    private Seance lireSeance(ResultSet rs) throws SQLException {
        return new Seance(
            rs.getInt("id"),
            rs.getString("jour"),
            rs.getString("heure"),
            rs.getString("enseignant"),
            rs.getString("salle"),
            rs.getString("groupe"),
            rs.getString("matiere"),
            rs.getInt("enseignant_id"),
            rs.getInt("salle_id"),
            rs.getInt("groupe_id"),
            rs.getInt("matiere_id")
        );
    }

    @Override
    public boolean ajouter(Seance s) throws SQLException {
        String sql = "INSERT INTO seance (jour, heure, enseignant, salle, groupe, matiere, enseignant_id, salle_id, groupe_id, matiere_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, s.getJour());
        ps.setString(2, s.getHeure());
        ps.setString(3, s.getEnseignant());
        ps.setString(4, s.getSalle());
        ps.setString(5, s.getGroupe());
        ps.setString(6, s.getMatiere());
        setIdOuNull(ps, 7, s.getEnseignantId());
        setIdOuNull(ps, 8, s.getSalleId());
        setIdOuNull(ps, 9, s.getGroupeId());
        setIdOuNull(ps, 10, s.getMatiereId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean modifier(Seance s) throws SQLException {
        String sql = "UPDATE seance SET jour=?, heure=?, enseignant=?, salle=?, groupe=?, matiere=?, enseignant_id=?, salle_id=?, groupe_id=?, matiere_id=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, s.getJour());
        ps.setString(2, s.getHeure());
        ps.setString(3, s.getEnseignant());
        ps.setString(4, s.getSalle());
        ps.setString(5, s.getGroupe());
        ps.setString(6, s.getMatiere());
        setIdOuNull(ps, 7, s.getEnseignantId());
        setIdOuNull(ps, 8, s.getSalleId());
        setIdOuNull(ps, 9, s.getGroupeId());
        setIdOuNull(ps, 10, s.getMatiereId());
        ps.setInt(11, s.getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM seance WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }
}
