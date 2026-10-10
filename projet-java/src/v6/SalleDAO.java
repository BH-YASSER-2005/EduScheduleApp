package v6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SalleDAO extends DAO<Salle> {

    public SalleDAO() throws SQLException {
        super();
    }

    
    public List<Salle> getTous() throws SQLException {
        List<Salle> liste = new ArrayList<>();
        String sql = "SELECT * FROM salle";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            liste.add(new Salle(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("type"),
                rs.getString("capacite"),
                rs.getString("batiment")
            ));
        }
        return liste;
    }

    @Override
    public boolean ajouter(Salle s) throws SQLException {
        String sql = "INSERT INTO salle (nom, type, capacite, batiment) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, s.getNom());
        ps.setString(2, s.getType());
        ps.setString(3, s.getCapacite());
        ps.setString(4, s.getBatiment());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean modifier(Salle s) throws SQLException {
        String sql = "UPDATE salle SET nom=?, type=?, capacite=?, batiment=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, s.getNom());
        ps.setString(2, s.getType());
        ps.setString(3, s.getCapacite());
        ps.setString(4, s.getBatiment());
        ps.setInt(5, s.getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM salle WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }
}
