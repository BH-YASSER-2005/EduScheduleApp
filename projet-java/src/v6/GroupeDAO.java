package v6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GroupeDAO extends DAO<Groupe> {

    public GroupeDAO() throws SQLException {
        super();
    }

    
    public List<Groupe> getTous() throws SQLException {
        List<Groupe> liste = new ArrayList<>();
        String sql = "SELECT * FROM groupe";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            liste.add(new Groupe(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("niveau"),
                rs.getString("section"),
                rs.getString("effectif")
            ));
        }
        return liste;
    }

    @Override
    public boolean ajouter(Groupe g) throws SQLException {
        String sql = "INSERT INTO groupe (nom, niveau, section, effectif) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, g.getNom());
        ps.setString(2, g.getNiveau());
        ps.setString(3, g.getSection());
        ps.setString(4, g.getEffectif());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean modifier(Groupe g) throws SQLException {
        String sql = "UPDATE groupe SET nom=?, niveau=?, section=?, effectif=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, g.getNom());
        ps.setString(2, g.getNiveau());
        ps.setString(3, g.getSection());
        ps.setString(4, g.getEffectif());
        ps.setInt(5, g.getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM groupe WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }
}
