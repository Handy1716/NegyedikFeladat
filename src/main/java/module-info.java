module com.example.negyedikfeladat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens hu.petrik.negyedikfeladat to javafx.fxml;
    exports hu.petrik.negyedikfeladat;
}