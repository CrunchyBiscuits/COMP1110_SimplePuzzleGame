package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Board extends Application {


    private static final int SQUARE_SIZE = 60;

    private static final int MARGIN_X = 30;
    private static final int MARGIN_Y = 30;

    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;
    private static final int BOARD_MARGIN = 70;

    private static final int OBJECTIVE_WIDTH = 162;
    private static final int OBJECTIVE_HEIGHT = 150;
    private static final int OBJECTIVE_MARGIN_X = 100;
    private static final int OBJECTIVE_MARGIN_Y = 20;

    private static final int BOARD_Y = MARGIN_Y;
    private static final int BOARD_X = MARGIN_X + (3 * SQUARE_SIZE) + SQUARE_SIZE + MARGIN_X;

    private static final int PLAY_AREA_Y = BOARD_Y + BOARD_MARGIN;
    private static final int PLAY_AREA_X = BOARD_X + BOARD_MARGIN;
    private static final int GAME_WIDTH = BOARD_X + BOARD_WIDTH + MARGIN_X;
    private static final int GAME_HEIGHT = 620;
    private static final long ROTATION_THRESHOLD = 50; // Allow rotation every 50 ms

    /* marker for unplaced tiles */
    public static final char NOT_PLACED = 255;

    /* node groups */
    private final Group root = new Group();
    private final Group gtiles = new Group();
    private final Group solution = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group exposed = new Group();
    private final Group objective = new Group();

    private static String solutionString;

    class GameTile extends ImageView{
        int tileID;

        /**
         * Construct a particular playing tile
         *
         * @param tile The letter representing the tile to be created
         */
        public GameTile(char tile) {
            if (tile>'j'||tile<'a'){
                throw new IllegalArgumentException("Bad tile: \""+tile+"\"");
            }
            this.tileID = tile - 'a';
            if (tileID==0||tileID==3||tileID==4||tileID==6){
                setFitHeight(2*SQUARE_SIZE);
                setFitWidth(3*SQUARE_SIZE);
            }else if (tileID==1||tileID==2||tileID==9){
                setFitHeight(2);
                setFitWidth(4);
            }else if (tileID==5){
                setFitHeight(1);
                setFitWidth(3);
            }else if (tileID==7){
                setFitHeight(3);
                setFitWidth(3);
            }else {
                setFitHeight(2);
                setFitWidth(2);
            }
        }

        /**
         *
         */
    }

    // FIXME Task 7: Implement a basic playable Focus Game in JavaFX that only allows pieces to be placed in valid places


    // FIXME Task 8: Implement challenges (you may use challenges and assets provided for you in comp1110.ass2.gui.assets: sq-b.png, sq-g.png, sq-r.png & sq-w.png)

    // FIXME Task 10: Implement hints

    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)


    private void newGame(){
        try{

        }catch (IllegalArgumentException e){
            System.err.println(e);
            e.printStackTrace();
            Platform.exit();
        }

    }


    @Override
    public void start(Stage primaryStage) {

    }
}
