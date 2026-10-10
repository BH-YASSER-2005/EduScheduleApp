package v6;

import java.sql.Connection;
import java.sql.SQLException;


public abstract class DAO<T> {

    
    protected Connection connection;

    
    public DAO() throws SQLException {
        this.connection = DBConnection.getConnection();
    }

    
    public abstract boolean ajouter(T objet)    throws SQLException;
    public abstract boolean modifier(T objet)   throws SQLException;
    public abstract boolean supprimer(int id)   throws SQLException;
}
