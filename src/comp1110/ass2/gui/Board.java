package comp1110.ass2.gui;

import comp1110.ass2.FocusGame;
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


    private static final int SQUARE_SIZE = 50;

    private static final int MARGIN_X = 30;
    private static final int MARGIN_Y = 30;

    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;
    private static final int BOARD_MARGIN_X = 30;
    private static final int BOARD_MARGIN_Y = 80;

    private static final int OBJECTIVE_WIDTH = 162;
    private static final int OBJECTIVE_HEIGHT = 150;
    private static final int OBJECTIVE_MARGIN_X = 100;
    private static final int OBJECTIVE_MARGIN_Y = 20;

    private static final int BOARD_Y = MARGIN_Y;
    private static final int BOARD_X = MARGIN_X + (3 * SQUARE_SIZE) + SQUARE_SIZE + MARGIN_X;

    private static final int PLAY_AREA_Y = BOARD_Y + BOARD_MARGIN_Y;
    private static final int PLAY_AREA_X = BOARD_X +15*MARGIN_X+ BOARD_MARGIN_X;
    private static final int GAME_WIDTH = BOARD_X + BOARD_WIDTH +15* MARGIN_X;
    private static final int GAME_HEIGHT = BOARD_HEIGHT+100;
    private static final long ROTATION_THRESHOLD = 50; // Allow rotation every 50 ms

    /* marker for unplaced tiles */
    public static final char NOT_PLACED = 255;

    /* where to find media assets */
    private static final String URI_BASE = "assets/";
    private static final String BASEBOARD_URI = Board.class.getResource(URI_BASE + "board.png").toString();


    /* node groups */
    private final Group root = new Group();
    private final Group gtiles = new Group();
    private final Group solution = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group exposed = new Group();
    private final Group objective = new Group();

    private static String solutionString;

    /* the state of the tiles */
    char[] tileState = new char[10];   //  all off screen to begin with

    /* The underlying game */
    FocusGame focusgame;

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
                setFitHeight(2*SQUARE_SIZE);
                setFitWidth(4*SQUARE_SIZE);
            }else if (tileID==5){
                setFitHeight(SQUARE_SIZE);
                setFitWidth(3*SQUARE_SIZE);
            }else if (tileID==7){
                setFitHeight(3*SQUARE_SIZE);
                setFitWidth(3*SQUARE_SIZE);
            }else {
                setFitHeight(2*SQUARE_SIZE);
                setFitWidth(2*SQUARE_SIZE);
            }
        }

        /**
         * Construct a playing tile, which is placed on the board at the start of the game,
         * as a part of some challenges
         *
         * @param tile  The letter representing the tile to be created.
         * @param orientation   The integer representation of the tile to be constructed
         */
        GameTile(char tile, int orientation) {
            if (tile > 'j' || tile < 'a') {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            this.tileID = tile - 'a';
            if (orientation%2 == 0) {
                if (tileID==0||tileID==3||tileID==4||tileID==6){
                    setFitHeight(2*SQUARE_SIZE);
                    setFitWidth(3*SQUARE_SIZE);
                }else if (tileID==1||tileID==2||tileID==9){
                    setFitHeight(2*SQUARE_SIZE);
                    setFitWidth(4*SQUARE_SIZE);
                }else if (tileID==5){
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(3*SQUARE_SIZE);
                }else if (tileID==7){
                    setFitHeight(3*SQUARE_SIZE);
                    setFitWidth(3*SQUARE_SIZE);
                }else {
                    setFitHeight(2*SQUARE_SIZE);
                    setFitWidth(2*SQUARE_SIZE);
                }
            }
            else {
                if (tileID==0||tileID==3||tileID==4||tileID==6){
                    setFitHeight(3*SQUARE_SIZE);
                    setFitWidth(2*SQUARE_SIZE);
                }else if (tileID==1||tileID==2||tileID==9){
                    setFitHeight(4*SQUARE_SIZE);
                    setFitWidth(2*SQUARE_SIZE);
                }else if (tileID==5){
                    setFitHeight(3*SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE);
                }else if (tileID==7){
                    setFitHeight(3*SQUARE_SIZE);
                    setFitWidth(3*SQUARE_SIZE);
                }else {
                    setFitHeight(2*SQUARE_SIZE);
                    setFitWidth(2*SQUARE_SIZE);
                }
            }
            setImage(new Image(Board.class.getResource(URI_BASE + tile + "-" + (char)(orientation+'0') + ".png").toString()));
        }

        //TODO another constructor for objective tile
    }

    // FIXME Task 7: Implement a basic playable Focus Game in JavaFX that only allows pieces to be placed in valid places
    class DraggableTile extends GameTile{
        int homeX, homeY;

        double mouseX, mouseY;
        Image[] images = new Image[4];
        int orientation; // 0=North ... 3=West
        long lastRotationTime = System.currentTimeMillis();// only allow rotation every ROTATION_THRESHOLD (ms)
        // This caters for mice which send multiple scroll events per tick.

        /**
         * Construct a draggable tile
         *
         * @param tile The tile identifier ('a' - 'j')
         */
        DraggableTile(char tile){
            super(tile);
            for(int i=0;i<4;i++){
                char imgId = (char)(i+'0');
                images[i] = new Image(Board.class.getResource(URI_BASE+ tile + "-" + imgId + ".png").toString());
            }
            setImage(images[0]);
            orientation = 0;
            tileState[tile-'a'] = NOT_PLACED;
            homeX = MARGIN_X + ((tile-'a')%3)*4*SQUARE_SIZE;
            setLayoutX(homeX);
            homeY = OBJECTIVE_MARGIN_Y + OBJECTIVE_HEIGHT + MARGIN_Y+((tile-'a')/3)*2*SQUARE_SIZE;
            setLayoutY(homeY);

            //handling events
            //TODO rotate and completion
            setOnScroll(event ->{
                if (System.currentTimeMillis()-lastRotationTime>ROTATION_THRESHOLD){
                    lastRotationTime = System.currentTimeMillis();
                    //hideCompletion();
                    rotate();
                    event.consume();
                    //checkCompletion();
                }
            });



            //start of the drag
            setOnMousePressed(event->{
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });

            //dragging
            setOnMouseDragged(event->{
                //hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });

            //finish drag
            setOnMouseReleased(event->{
                snapToGrid();
            });


        }

        private void snapToGrid(){
//           if (onBoard() && (!alreadyOccupied())){
            if (onBoard()){

            }else {
                snapToHome();
            }
        }

        /**
         * check whether the tile is on the board
         * */
        private boolean onBoard(){
//Testing line
//            System.out.println(getLayoutX());
//            System.out.println(PLAY_AREA_X - (SQUARE_SIZE / 2));
//            System.out.println(PLAY_AREA_X + 8.5 * SQUARE_SIZE);
//            System.out.println("-------------");
//            System.out.println(getLayoutY());
//            System.out.println(PLAY_AREA_Y);
//            System.out.println(PLAY_AREA_Y + 6 * SQUARE_SIZE);
            return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 5 * SQUARE_SIZE))
                    && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 4.5 * SQUARE_SIZE));
        }

        private boolean alreadyOccupied(){
            return true;
        }

        private void setPosition(){
            int x = (int) (getLayoutX() - PLAY_AREA_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() - PLAY_AREA_Y) / SQUARE_SIZE;
            if (x < 0)
                tileState[tileID] = NOT_PLACED;
            else {

            }
        }

        /**
         * rotate the tile
         * */
        private void rotate(){
            orientation = (orientation + 1)%4;
            setImage(images[orientation]);
            if (orientation%2 == 0) {
                if (tileID==0||tileID==3||tileID==4||tileID==6){
                    setFitHeight(2*SQUARE_SIZE);
                    setFitWidth(3*SQUARE_SIZE);
                }else if (tileID==1||tileID==2||tileID==9){
                    setFitHeight(2*SQUARE_SIZE);
                    setFitWidth(4*SQUARE_SIZE);
                }else if (tileID==5){
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(3*SQUARE_SIZE);
                }else if (tileID==7){
                    setFitHeight(3*SQUARE_SIZE);
                    setFitWidth(3*SQUARE_SIZE);
                }else {
                    setFitHeight(2*SQUARE_SIZE);
                    setFitWidth(2*SQUARE_SIZE);
                }
            }
            else {
                if (tileID==0||tileID==3||tileID==4||tileID==6){
                    setFitHeight(3*SQUARE_SIZE);
                    setFitWidth(2*SQUARE_SIZE);
                }else if (tileID==1||tileID==2||tileID==9){
                    setFitHeight(4*SQUARE_SIZE);
                    setFitWidth(2*SQUARE_SIZE);
                }else if (tileID==5){
                    setFitHeight(3*SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE);
                }else if (tileID==7){
                    setFitHeight(3*SQUARE_SIZE);
                    setFitWidth(3*SQUARE_SIZE);
                }else {
                    setFitHeight(2*SQUARE_SIZE);
                    setFitWidth(2*SQUARE_SIZE);
                }
            }
            toFront();
            setPosition();
        }


        /**
         * set the tile to original position
         * */
        private void snapToHome(){
            setLayoutX(homeX);
            setLayoutY(homeY);
            if (tileID==0||tileID==3||tileID==4||tileID==6){
                setFitHeight(2*SQUARE_SIZE);
                setFitWidth(3*SQUARE_SIZE);
            }else if (tileID==1||tileID==2||tileID==9){
                setFitHeight(2*SQUARE_SIZE);
                setFitWidth(4*SQUARE_SIZE);
            }else if (tileID==5){
                setFitHeight(SQUARE_SIZE);
                setFitWidth(3*SQUARE_SIZE);
            }else if (tileID==7){
                setFitHeight(3*SQUARE_SIZE);
                setFitWidth(3*SQUARE_SIZE);
            }else {
                setFitHeight(2*SQUARE_SIZE);
                setFitWidth(2*SQUARE_SIZE);
            }
            setImage(images[0]);
            orientation=0;
            tileState[tileID]=NOT_PLACED;
        }
    }

    // FIXME Task 8: Implement challenges (you may use challenges and assets provided for you in comp1110.ass2.gui.assets: sq-b.png, sq-g.png, sq-r.png & sq-w.png)

    // FIXME Task 10: Implement hints

    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)



    /**
     * Set the gaming board
     */
    private void showBoard(){
        board.getChildren().clear();

        ImageView baseboard = new ImageView();
        baseboard.setImage(new Image(BASEBOARD_URI));
        baseboard.setFitWidth(BOARD_WIDTH);
        baseboard.setFitHeight(BOARD_HEIGHT);
        baseboard.setLayoutX(BOARD_X+14*MARGIN_X);
        baseboard.setLayoutY(BOARD_Y);
        board.getChildren().add(baseboard);

        board.toBack();
    }

    /**
     * Set the ten tiles
     */
    private void showTiles(){
        gtiles.getChildren().clear();
        for (char m='a';m<='j';m++){
            gtiles.getChildren().add(new DraggableTile(m));
        }
    }


    private void newGame(){
        try{
            //hideCompletioni();
            focusgame = new FocusGame();
            String[] reSet={""};
            showTiles();
            // TODO check solution
        }catch (IllegalArgumentException e){
            System.err.println(e);
            e.printStackTrace();
            Platform.exit();
        }

    }


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("FOCUSGAME - Fun with the Tiles");
        Scene scene = new Scene(root,GAME_WIDTH, GAME_HEIGHT);

        root.getChildren().add(gtiles);
        root.getChildren().add(board);
        root.getChildren().add(solution);
        root.getChildren().add(controls);
        root.getChildren().add(exposed);
        root.getChildren().add(objective);

        // TODO set handlers, sound, board, tiles
        //setUpHandlers(scene);
        showBoard();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
