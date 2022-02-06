package games.sudoku;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Controller {
    @FXML
    private Pane pane;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Label label;

    @FXML
    private FlowPane flowPane;

    @FXML
    private ComboBox comboBox;

    private final int paneLights = 594;
    private final int cell = paneLights / 9;
    private final int stepX = 10;
    private final int stepY = 55;//47
    private final int witchLine = 5;
    private boolean fillCell = false;
    private final int stepCell = cell / 6;
    private int[] arrXY = new int[10];
    private int x, y;

    public void initialize() {
        coreSudoku.startGame();
        flowPane.setHgap(20);
        comboBox.getItems().addAll(coreSudoku.Levels.values());
        comboBox.setValue(coreSudoku.Levels.values()[coreSudoku.level]);
        render();
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                x = coreSudoku.normalX((int) mouseEvent.getSceneX() - stepX, cell);
                y = coreSudoku.normalY((int) mouseEvent.getSceneY() - stepY, cell);
                coreSudoku.arrUserNumber[y][x] = coreSudoku.arrSetNext(coreSudoku.arrUserNumber[y][x]);
                arrXY = coreSudoku.generateXY(x, y, arrXY, cell, stepCell);
                render();
                if (coreSudoku.winSudoku()) {
                    alert("WIN!", "Congratulations!!!");
                    buttonUpdate();
                }
            }
        });
    }

    @FXML
    protected void buttonUpdate() {
        coreSudoku.gameStart = false;
        coreSudoku.startGame();
        render();
    }

    @FXML
    public void comboBoxList(ActionEvent actionEvent) {
        coreSudoku.level = coreSudoku.Levels.valueOf(comboBox.getSelectionModel().getSelectedItem().toString()).ordinal();
        buttonUpdate();
    }

    @FXML
    public void radioButton(ActionEvent actionEvent) {
        if (fillCell == false) {
            fillCell = true;
        } else {
            fillCell = false;
        }
        render();
    }

    void render() {
        pane.getChildren().clear();
        if (fillCell == true) {
            setFillCell(Color.PALEGREEN);
        }
        label.setText("Unknown: " + coreSudoku.getHidenNumber());
        setLineCell();
        setAllNumber(coreSudoku.sudokuArr, Color.BLACK);
        coreSudoku.synchrArr(coreSudoku.arrUserNumber, coreSudoku.sudokuArr);
        setAllNumber(coreSudoku.arrUserNumber, Color.CHOCOLATE);

    }

    private void alert(String title, String meseg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(meseg);
        alert.showAndWait();
    }

    private void setLineCell() {
        for (double i = 0; i < paneLights + 1; i += paneLights / 9) {
            Line h = new Line(i, 0, i, paneLights);
            Line v = new Line(0, i, paneLights, i);
            if (i == 0 || i == 198 || i == 396 || i == 594) {
                h.setStrokeWidth(3);
                v.setStrokeWidth(3);
            }
            pane.getChildren().add(h);
            pane.getChildren().add(v);
        }
    }

    private void setFillCell(Paint paint) {
        coreSudoku.mergerArr(coreSudoku.arrUserNumber, coreSudoku.sudokuArr);
        for (int i = 0; i < 9; i++) {
            coreSudoku.generateXY(i, i, arrXY, cell, stepCell);
            if (coreSudoku.winerH(i, coreSudoku.winnerArr)) {
                paneFillCell(0, cell * i, paneLights, cell, paint);
            }
            if (coreSudoku.winerV(i, coreSudoku.winnerArr)) {
                paneFillCell(cell * i, 0, cell, paneLights, paint);
            }
            if (coreSudoku.winerRactzngle(i, coreSudoku.winnerArr)) {
                int arr[] = coreSudoku.gpsRactangle(i);
                paneFillCell(cell * arr[1], cell * arr[0], paneLights / 3, paneLights / 3, paint);
            }
        }
    }

    public void paneFillCell(int horiz, int vert, int hight, int width, Paint paint) {
        Rectangle rectangle = new Rectangle(horiz, vert, hight, width);
        rectangle.setFill(paint);
        pane.getChildren().add(rectangle);
    }


    private void setAllNumber(int arr[][], Paint paint) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arrXY = coreSudoku.generateXY(i, j, arrXY, cell, stepCell);
                bruteForce(i, j, arr, paint);
            }
        }
    }

    private void bruteForce(int vert, int horiz, int arr[][], Paint paint) {
        switch (arr[vert][horiz]) {
            case 1:
                paneOne(paint);
                break;
            case 2:
                paneTwo(paint);
                break;
            case 3:
                paneThree(paint);
                break;
            case 4:
                paneFour(paint);
                break;
            case 5:
                paneFive(paint);
                break;
            case 6:
                paneSix(paint);
                break;
            case 7:
                paneSeven(paint);
                break;
            case 8:
                paneEight(paint);
                break;
            case 9:
                paneNine(paint);
                break;
        }
    }

    private void paneOne(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[2]);
        Line l2 = new Line(arrXY[1], arrXY[2], arrXY[1], arrXY[4]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
    }

    private void paneTwo(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[2], arrXY[1], arrXY[2]);
        Line l2 = new Line(arrXY[1], arrXY[2], arrXY[1], arrXY[3]);
        Line l3 = new Line(arrXY[0], arrXY[4], arrXY[1], arrXY[3]);
        Line l4 = new Line(arrXY[0], arrXY[4], arrXY[1], arrXY[4]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        l3.setStrokeWidth(witchLine);
        l3.setStroke(paint);
        l4.setStrokeWidth(witchLine);
        l4.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
        pane.getChildren().add(l3);
        pane.getChildren().add(l4);
    }

    private void paneThree(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[2], arrXY[1], arrXY[2]);
        Line l2 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[2]);
        Line l3 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[3]);
        Line l4 = new Line(arrXY[0], arrXY[4], arrXY[1], arrXY[3]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        l3.setStrokeWidth(witchLine);
        l3.setStroke(paint);
        l4.setStrokeWidth(witchLine);
        l4.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
        pane.getChildren().add(l3);
        pane.getChildren().add(l4);
    }

    private void paneFour(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[2], arrXY[0], arrXY[3]);
        Line l2 = new Line(arrXY[1], arrXY[2], arrXY[1], arrXY[4]);
        Line l3 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[3]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        l3.setStrokeWidth(witchLine);
        l3.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
        pane.getChildren().add(l3);
    }

    private void paneFive(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[2], arrXY[1], arrXY[2]);
        Line l2 = new Line(arrXY[0], arrXY[3], arrXY[0], arrXY[2]);
        Line l3 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[3]);
        Line l4 = new Line(arrXY[1], arrXY[3], arrXY[1], arrXY[4]);
        Line l5 = new Line(arrXY[0], arrXY[4], arrXY[1], arrXY[4]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        l3.setStrokeWidth(witchLine);
        l3.setStroke(paint);
        l4.setStrokeWidth(witchLine);
        l4.setStroke(paint);
        l5.setStrokeWidth(witchLine);
        l5.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
        pane.getChildren().add(l3);
        pane.getChildren().add(l4);
        pane.getChildren().add(l5);
    }

    private void paneSix(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[2]);
        Line l2 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[3]);
        Line l3 = new Line(arrXY[0], arrXY[4], arrXY[0], arrXY[3]);
        Line l4 = new Line(arrXY[0], arrXY[4], arrXY[1], arrXY[4]);
        Line l5 = new Line(arrXY[1], arrXY[3], arrXY[1], arrXY[4]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        l3.setStrokeWidth(witchLine);
        l3.setStroke(paint);
        l4.setStrokeWidth(witchLine);
        l4.setStroke(paint);
        l5.setStrokeWidth(witchLine);
        l5.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
        pane.getChildren().add(l3);
        pane.getChildren().add(l4);
        pane.getChildren().add(l5);
    }

    private void paneSeven(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[2], arrXY[1], arrXY[2]);
        Line l2 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[2]);
        Line l3 = new Line(arrXY[0], arrXY[4], arrXY[0], arrXY[3]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        l3.setStrokeWidth(witchLine);
        l3.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
        pane.getChildren().add(l3);
    }

    private void paneEight(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[4], arrXY[0], arrXY[2]);
        Line l2 = new Line(arrXY[1], arrXY[4], arrXY[1], arrXY[2]);
        Line l3 = new Line(arrXY[0], arrXY[2], arrXY[1], arrXY[2]);
        Line l4 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[3]);
        Line l5 = new Line(arrXY[0], arrXY[4], arrXY[1], arrXY[4]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        l3.setStrokeWidth(witchLine);
        l3.setStroke(paint);
        l4.setStrokeWidth(witchLine);
        l4.setStroke(paint);
        l5.setStrokeWidth(witchLine);
        l5.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
        pane.getChildren().add(l3);
        pane.getChildren().add(l4);
        pane.getChildren().add(l5);
    }

    private void paneNine(Paint paint) {
        Line l1 = new Line(arrXY[0], arrXY[2], arrXY[1], arrXY[2]);
        Line l2 = new Line(arrXY[0], arrXY[3], arrXY[0], arrXY[2]);
        Line l3 = new Line(arrXY[0], arrXY[3], arrXY[1], arrXY[3]);
        Line l4 = new Line(arrXY[1], arrXY[3], arrXY[1], arrXY[2]);
        Line l5 = new Line(arrXY[0], arrXY[4], arrXY[1], arrXY[3]);
        l1.setStrokeWidth(witchLine);
        l1.setStroke(paint);
        l2.setStrokeWidth(witchLine);
        l2.setStroke(paint);
        l3.setStrokeWidth(witchLine);
        l3.setStroke(paint);
        l4.setStrokeWidth(witchLine);
        l4.setStroke(paint);
        l5.setStrokeWidth(witchLine);
        l5.setStroke(paint);
        pane.getChildren().add(l1);
        pane.getChildren().add(l2);
        pane.getChildren().add(l3);
        pane.getChildren().add(l4);
        pane.getChildren().add(l5);
    }
}