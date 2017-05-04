package BDA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class bda {
    
    private Connection conexion;
    private ResultSet rs;
    private PreparedStatement ps;
    
    public bda() {
    }

    public String conectar() throws SQLException {
        String consulta;
        int num;
        
        conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/parques", "root", "ROOT");

        consulta = "SELECT max(id) AS 'id' FROM parque;";
        ps = conexion.prepareStatement(consulta);
        rs = ps.executeQuery();
        rs.next();
        
        num = rs.getInt("id");
        
        return num + "";
    }
    

}
