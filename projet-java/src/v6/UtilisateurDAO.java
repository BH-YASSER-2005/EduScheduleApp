package v6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UtilisateurDAO extends DAO<Utilisateur> {

    public UtilisateurDAO() throws SQLException {
        super();
    }

    
    public Utilisateur connecter(String identifiant, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE identifiant = ? AND mot_de_passe = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, identifiant);
        ps.setString(2, motDePasse);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return lire(rs);
        }
        return null;
    }

   
    public List<Utilisateur> getTous() throws SQLException {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            liste.add(lire(rs));
        }
        return liste;
    }

    private Utilisateur lire(ResultSet rs) throws SQLException {
        return new Utilisateur(
            rs.getInt("id"),
            rs.getString("identifiant"),
            rs.getString("mot_de_passe"),
            rs.getString("role"),
            rs.getInt("enseignant_id"),   
            rs.getInt("groupe_id"));
    }

    private void setIdOuNull(PreparedStatement ps, int idx, int v) throws SQLException {
        if (v > 0) ps.setInt(idx, v); else ps.setNull(idx, java.sql.Types.INTEGER);
    }

    @Override
    public boolean ajouter(Utilisateur u) throws SQLException {
        String sql = "INSERT INTO utilisateur (identifiant, mot_de_passe, role, enseignant_id, groupe_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, u.getIdentifiant());
        ps.setString(2, u.getMotDePasse());
        ps.setString(3, u.getRole());
        setIdOuNull(ps, 4, u.getEnseignantId());
        setIdOuNull(ps, 5, u.getGroupeId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean modifier(Utilisateur u) throws SQLException {
        String sql = "UPDATE utilisateur SET identifiant=?, mot_de_passe=?, role=?, enseignant_id=?, groupe_id=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, u.getIdentifiant());
        ps.setString(2, u.getMotDePasse());
        ps.setString(3, u.getRole());
        setIdOuNull(ps, 4, u.getEnseignantId());
        setIdOuNull(ps, 5, u.getGroupeId());
        ps.setInt(6, u.getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }
}
