package hu.petrik.negyedikfeladat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.Optional;

public class HelloController {

    @FXML
    private Button torlesGomb;
    @FXML
    private TableView<Konyv> konyvTabla;
    @FXML
    private TableColumn<Konyv,String> cimO;
    @FXML
    private TableColumn<Konyv,String> szerzoO;
    @FXML
    private TableColumn<Konyv,Integer> kiadasO;
    @FXML
    private TableColumn<Konyv,Integer> oldalO;

    public HelloController() {
    }

    @FXML
    private void initialize() {
        // Konyv konyv =
        cimO.setCellValueFactory(new PropertyValueFactory<>("title"));
        szerzoO.setCellValueFactory(new PropertyValueFactory<>("author"));
        kiadasO.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        oldalO.setCellValueFactory(new PropertyValueFactory<>("page_count"));

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from books");
            while (rs.next()) {
                konyvTabla.getItems().add(
                    new Konyv(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publish_year"),
                        rs.getInt("page_count")
                    )
                );
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void TorlesKattintas(ActionEvent actionEvent) {
        Konyv selected = konyvTabla.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("Nincs kivalasztva sor");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(String.format("Biztos torolni akarod a konyvet: '%s'?", selected.getTitle()));
        Optional<ButtonType> optionalButtonType = confirmation.showAndWait();
        ButtonType clickedButton = optionalButtonType.get();
        if (clickedButton.equals(ButtonType.OK)) {
            konyvTabla.getItems().remove(selected);
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
                PreparedStatement st = connection.prepareStatement("delete from books where id = ?");
                st.setInt(1, selected.getId());
                st.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
