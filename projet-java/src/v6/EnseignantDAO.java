package v6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EnseignantDAO extends DAO<Enseignant> {

    public EnseignantDAO() throws SQLException {
        super();
    }

    
    public List<Enseignant> getTous() throws SQLException {
        List<Enseignant> liste = new ArrayList<>();
        String sql = "SELECT * FROM enseignant";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            liste.add(new Enseignant(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("specialite")
            ));
        }
        return liste;
    }

    @Override
    public boolean ajouter(Enseignant e) throws SQLException {
        String sql = "INSERT INTO enseignant (nom, prenom, email, specialite) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, e.getNom());
        ps.setString(2, e.getPrenom());
        ps.setString(3, e.getEmail());
        ps.setString(4, e.getSpecialite());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean modifier(Enseignant e) throws SQLException {
        String sql = "UPDATE enseignant SET nom=?, prenom=?, email=?, specialite=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, e.getNom());
        ps.setString(2, e.getPrenom());
        ps.setString(3, e.getEmail());
        ps.setString(4, e.getSpecialite());
        ps.setInt(5, e.getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM enseignant WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }
}
