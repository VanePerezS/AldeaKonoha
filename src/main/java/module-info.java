module org.example.aldeakonoha {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens org.example.aldeakonoha to javafx.fxml;
    exports org.example.aldeakonoha;
}