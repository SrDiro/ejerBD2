package Modelo;

import BDA.bda;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Comunidad {

    private final IntegerProperty id;
    private final StringProperty nombre;
    List<Comunidad> listaComunidades = new ArrayList<>();

    public Comunidad(int id, String nombre) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
    }

    private int getId() {
        return id.get();
    }

    private void setId(int value) {
        id.set(value);
    }

    private IntegerProperty idProperty() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String value) {
        nombre.set(value);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public List<Comunidad> buscarComunidad() throws SQLException {
        bda conexion = new bda();
        ResultSet rs;
        PreparedStatement ps;
        
        String conId, conComunidad;
        conId = "SELECT id FROM comunidad;";
        
        conComunidad = "SELECT concat(id, ' (', nombre, ')') AS 'comunidades' FROM comunidad;";

        ps = conexion.prepareStatement(conComunidad);
        rs = ps.executeQuery();


        return listaComunidades;

    }

}
