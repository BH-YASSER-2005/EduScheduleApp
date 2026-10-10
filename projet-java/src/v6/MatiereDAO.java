package v6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MatiereDAO extends DAO<Matiere> {

    public MatiereDAO() throws SQLException {
        super();
    }

    
    public List<Matiere> getTous() throws SQLException {
        List<Matiere> liste = new ArrayList<>();
        String sql = "SELECT * FROM matiere";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            liste.add(new Matiere(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("code"),
                rs.getString("volume_horaire"),
                rs.getString("type")
            ));
        }
        return liste;
    }

    @Override
    public boolean ajouter(Matiere m) throws SQLException {
        String sql = "INSERT INTO matiere (nom, code, volume_horaire, type) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, m.getNom());
        ps.setString(2, m.getCode());
        ps.setString(3, m.getVolumeHoraire());
        ps.setString(4, m.getType());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean modifier(Matiere m) throws SQLException {
        String sql = "UPDATE matiere SET nom=?, code=?, volume_horaire=?, type=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, m.getNom());
        ps.setString(2, m.getCode());
        ps.setString(3, m.getVolumeHoraire());
        ps.setString(4, m.getType());
        ps.setInt(5, m.getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM matiere WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }
}
