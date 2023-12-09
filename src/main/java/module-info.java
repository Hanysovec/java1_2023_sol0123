module com.example.java1_2023_sol0123 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.java1_2023_sol0123 to javafx.fxml;
    exports com.example.java1_2023_sol0123;
}