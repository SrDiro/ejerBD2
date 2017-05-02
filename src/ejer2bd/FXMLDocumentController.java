package ejer2bd;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Button btGuardar;
    @FXML
    private Label lbEstado;
    @FXML
    private TextField tfIdParque;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfExtension;
    private TextField tfComunidadId;
    @FXML
    private Label lbNext;
    @FXML
    private ComboBox<String> cbComunidad;

    //ATRIBUTOS
    ResultSet rs;
    Connection conexion;
    PreparedStatement ps;

    ObservableList<String> listaComunidades = FXCollections.observableArrayList("");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String consulta, consultaComunidad;
        int siguiente;

        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/parques", "root", "root");
            lbEstado.setText("CONECTADO");
            lbEstado.setStyle("-fx-text-fill: green;");

            consulta = "SELECT max(id) AS 'id' FROM parque;";
            ps = conexion.prepareStatement(consulta);
            rs = ps.executeQuery();
            rs.next();
            siguiente = rs.getInt("id") + 1;

            lbNext.setText("Siguiente: " + siguiente + "");
            consultaComunidad = "SELECT concat(id, ' (', nombre, ')') AS 'comunidades' FROM comunidad;";

            ps = conexion.prepareStatement(consultaComunidad);
            rs = ps.executeQuery();
            rs.next();

            do {
                listaComunidades.add(rs.getString("comunidades"));
            } while (rs.next());

            cbComunidad.setItems(listaComunidades);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            lbEstado.setText("DESCONECTADO");
            lbEstado.setStyle("-fx-text-fill: red;");
        }

    }

    @FXML
    private void guardar(ActionEvent event) {
        double extension;
        int idParque, comunidadId;
        String consulta, nombre, valorCB;

        if (btGuardar.isFocused()) {
            idParque = Integer.parseInt(tfIdParque.getText());
            nombre = tfNombre.getText();
            
            if (!tfExtension.getText().isEmpty()) {
                extension = Double.parseDouble(tfExtension.getText());
            } else {
                extension = -1;
            }

            try {
                consulta = "INSERT INTO parque "
                        + "VALUES (?, ?, ?, ?);";
//            INSERT INTO parque VALUES (?, ?, ?, ?);
                ps = conexion.prepareStatement(consulta);
                ps.setInt(1, idParque);
                ps.setString(2, nombre);
                if (extension != -1) {
                    ps.setDouble(3, extension);
                } else {
                    ps.setNull(3, java.sql.Types.DOUBLE);
                }
                valorCB = cbComunidad.getValue();

                StringTokenizer tokens = new StringTokenizer(valorCB, " ");
                comunidadId = Integer.parseInt(tokens.nextToken());

                ps.setInt(4, comunidadId);

                ps.executeUpdate();

                Alert registroIntroducido = new Alert(Alert.AlertType.INFORMATION);
                registroIntroducido.setTitle("Conexion");
                registroIntroducido.setHeaderText("Registro introducido correctamente.");
                registroIntroducido.show();

            } catch (MySQLIntegrityConstraintViolationException ex) {
                if (ex.getErrorCode() == 1062) {
                    Alert alertaClaveDuplicada = new Alert(Alert.AlertType.WARNING);
                    alertaClaveDuplicada.setTitle("Conexion");
                    alertaClaveDuplicada.setHeaderText(ex.getMessage() + "\n" + "Codigo SQL:" + ex.getErrorCode());
                    alertaClaveDuplicada.show();
                } else {
                    System.out.println(ex.getMessage() + " " + ex.getErrorCode());
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage() + " " + e.getErrorCode());
            }
        }

    }

}
