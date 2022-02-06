module games.sudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens games.sudoku to javafx.fxml;
    exports games.sudoku;
}