import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.application.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public class Main extends Application implements EventHandler<ActionEvent> {
    public static void main(String[] args) {
        launch(args);
    }

    Scene menu;
    Scene game;
    Scene end;
    Stage window;

    Button tRight = new Button();
    Button tMiddle = new Button();
    Button tLeft = new Button();
    Button mRight = new Button();
    Button mMiddle = new Button();
    Button mLeft = new Button();
    Button bRight = new Button();
    Button bMiddle = new Button();
    Button bLeft = new Button();
    Button playButton = new Button();
    GridPane gameRoot = new GridPane();

    String currentPlayer = "X";
    StackPane endPane = new StackPane();
    @Override
    public void start(Stage stage) throws Exception {
        this.window = stage;
        window.setTitle("naughts and crosses"); //window name
        menu = new Scene(menuScene(), 600, 400); // assigns menu scene the menu layout
        game = new Scene(gameScene(), 600, 400);
        end = new Scene(endPane, 600, 400);
        String css = this.getClass().getResource("myCSS.css").toExternalForm();
        game.getStylesheets().add(css);
        window.setScene(menu);
        window.show();
    }

    private Pane menuScene() { // contains main menu layout and contents
        Pane root;
        //button

        playButton.setText("Play");
        playButton.setPrefSize(100, 30);
        playButton.setLayoutX(250);
        playButton.setLayoutY(200);
        playButton.setOnAction(e -> window.setScene(game));

        //layout that shows items --> i.e., pane
        root = new Pane();
        root.getChildren().add(playButton);
        return root;
    }

    Text playerTurn;
    List<Button> buttons;


    private Pane gameScene() {
        StackPane stackPane = new StackPane();
        playerTurn = new Text();
        playerTurn.setText("Player Turn: " + currentPlayer);
        buttons = Arrays.asList(tLeft, tMiddle, tRight, mLeft, mMiddle, mRight, bLeft, bMiddle, bRight);
        for (Button b : buttons) {
            b.setPrefSize(100, 100);
            b.setOnAction(this);
            b.setId("gButton");
        }

        gameRoot.setMinSize(1600, 1400);
        gameRoot.add(tLeft, 0, 0);
        gameRoot.add(tMiddle, 1, 0);
        gameRoot.add(tRight, 2, 0);
        gameRoot.add(mLeft, 0, 1);
        gameRoot.add(mMiddle, 1, 1);
        gameRoot.add(mRight, 2, 1);
        gameRoot.add(bLeft, 0, 2);
        gameRoot.add(bMiddle, 1, 2);
        gameRoot.add(bRight, 2, 2);
        gameRoot.setAlignment(Pos.CENTER);

        gameRoot.setAlignment(Pos.CENTER);
        Pane pane = new Pane();
        pane.getChildren().add(playerTurn);
        playerTurn.setLayoutY(15);
        playerTurn.setLayoutX(10);
        stackPane.getChildren().add(pane);
        stackPane.getChildren().add(gameRoot);
        return stackPane;
    }


    @Override
    public void handle(ActionEvent actionEvent) {
        Button current = (Button) actionEvent.getSource();
        Text text = new Text();
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 80));
        if (currentPlayer == "X") {
            text.setText("X");
            current.setText("X");
            winCheck(current);
            currentPlayer = "O";
            playerTurn.setText("Player Turn: " + currentPlayer);
        } else if (currentPlayer == "O") {
            text.setText("O");
            current.setText("O");
            winCheck(current);
            currentPlayer = "X";
            playerTurn.setText("Player Turn: " + currentPlayer);
        } else {
            currentPlayer = "noone";
        }

        gameRoot.add(text, GridPane.getColumnIndex(current), GridPane.getRowIndex(current));
        current.setDisable(true);
        current.setStyle("-fx-opacity: 1");
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);
    }
    String[][] grid = new String[3][3];
    String winner;
    int wait;
    private void winCheck(Button currentNode) {
        int x = GridPane.getColumnIndex(currentNode);
        int y = GridPane.getRowIndex(currentNode);
        grid[x][y] = currentNode.getText();

        int[] xoCounts = new int[6];

        //diagonal
        if (grid[1][1] != null)
            if (((grid[0][0] == grid[1][1]) && (grid[1][1]==grid[2][2])) || ((grid[0][2] == grid[1][1]) && (grid[1][1] == grid[2][0])))
                xoCounts[4] = 3;
                winner = grid[1][1];

        for (int i = 0; i < 3; i++) {
            //horizontal check
            if(grid[i][y] != null) {
                if (grid[i][y] == "O") {
                    xoCounts[0]++;
                    if (xoCounts[0] == 3) {
                        winner = "O";
                    }
                } else {
                    xoCounts[1]++;
                    if (xoCounts[1] == 3) {
                        winner = "X";
                    }

                }
            }
            //vertical check
            if (grid[x][i] != null){
                if (grid[x][i] == "O"){
                    xoCounts[2]++;
                    if (xoCounts[2] == 3){
                        winner = "O";
                    }
                }
                else {
                    xoCounts[3]++;
                    if (xoCounts[3] == 3) {
                        winner = "X";
                    }
                }
            }
        }

        for (int j:xoCounts) {
            if (j == 3){
                window.setScene(end);
                Text winnerText = new Text();
                endPane.getChildren().add(winnerText);
                winnerText.setText("THE WINNER IS "+winner);
                winnerText.setStyle("-fx-font-size: 40px");
            }
        }
        wait++;

    }
}