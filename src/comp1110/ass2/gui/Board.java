package comp1110.ass2.gui;

import comp1110.ass2.FocusGame;
import comp1110.ass2.Orientation;
import comp1110.ass2.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Board extends Application {


    private static final int SQUARE_SIZE = 50;

    private static final int MARGIN_X = 30;
    private static final int MARGIN_Y = 30;

    private static final int BOARD_WIDTH = 520;
    private static final int BOARD_HEIGHT = 360;
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
    private static final int GAME_HEIGHT = 700;
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
    private final Group challenge = new Group();
    private final Group shadow = new Group();
//    private final Group window = new Group();

    private static String solutionString;

    /* the state of the tiles */
    char[] tileState = new char[10];   //  all off screen to begin with

    /* The underlying game */
    FocusGame focusgame;

    /* message on completion */
    private final Text completionText = new Text("Well done!");

    /* the difficulty slider */
    private final Slider difficulty = new Slider();

    /* Define a drop shadow effect that we will appy to tiles */
    private static DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */ {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }


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
                    hideCompletion();
                    rotate();
                    event.consume();
                    checkCompletion();
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
            if (onBoard()) {
                if ((getLayoutX() >= (PLAY_AREA_X - (SQUARE_SIZE / 2))) && (getLayoutX() < (PLAY_AREA_X + (SQUARE_SIZE / 2)))) {
                    setLayoutX(PLAY_AREA_X);
                } else if ((getLayoutX() >= PLAY_AREA_X + (SQUARE_SIZE / 2)) && (getLayoutX() < PLAY_AREA_X + 1.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 1.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 2.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 2 * SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 2.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 3.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 3 * SQUARE_SIZE);
                }

                if ((getLayoutY() >= (PLAY_AREA_Y - (SQUARE_SIZE / 2))) && (getLayoutY() < (PLAY_AREA_Y + (SQUARE_SIZE / 2)))) {
                    setLayoutY(PLAY_AREA_Y);
                } else if ((getLayoutY() >= PLAY_AREA_Y + (SQUARE_SIZE / 2)) && (getLayoutY() < PLAY_AREA_Y + 1.5 * SQUARE_SIZE)) {
                    setLayoutY(PLAY_AREA_Y + SQUARE_SIZE);
                } else if ((getLayoutY() >= PLAY_AREA_Y + 1.5 * SQUARE_SIZE) && (getLayoutY() < PLAY_AREA_Y + 2.5 * SQUARE_SIZE)) {
                    setLayoutY(PLAY_AREA_Y + 2 * SQUARE_SIZE);
                }
                setPosition();
            } else {
                snapToHome();
            }
            checkCompletion();
        }

        /**
         * check whether the tile is on the board
         * */
        private boolean onBoard(){
            return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 5 * SQUARE_SIZE))
                    && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 4.5 * SQUARE_SIZE));
        }

        private boolean alreadyOccupied(){
            int x = (int) (getLayoutX() + (SQUARE_SIZE / 2) - PLAY_AREA_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() + (SQUARE_SIZE / 2) - PLAY_AREA_Y) / SQUARE_SIZE;

            // it occupies two cells
            int idx1 = y * 4 + x;
            int idx2;

            if (orientation%2 == 0)
                idx2 = (y+1) * 4 + x;
            else
                idx2 = y * 4 + x + 1;

            for (int i = 0; i < 6; i++) {
                if (tileState[i] == NOT_PLACED)
                    continue;

                int tIdx1 = tileState[i] / 4;
                int tIdx2;
                int tOrn = tileState[i] % 4;

                if (tOrn%2 == 0)
                    tIdx2 = tIdx1 + 4;
                else
                    tIdx2 = tIdx1 + 1;

                if (tIdx1 == idx1 || tIdx2 == idx1 || tIdx1 == idx2 || tIdx2 == idx2)
                    return true;
            }
            return false;
        }

        private void setPosition(){
            int x = (int) (getLayoutX() - PLAY_AREA_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() - PLAY_AREA_Y) / SQUARE_SIZE;
            if (x < 0)
                tileState[tileID] = NOT_PLACED;
            else {
                char val = (char) ((y * 4 + x) * 4 + orientation);
                tileState[tileID] = val;
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



    // add sound
    private boolean startLoop = false;

    private String music = getClass().getResource( "assets/484103__greenfiresound__click-08.wav").toString();
    private AudioClip loop;

    private void setUpSoundLoop() {
            loop = new AudioClip(music);
            loop.setCycleCount(AudioClip.INDEFINITE);
    }

    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnMouseClicked(event -> {
            if (startLoop)
                loop.stop();
            else
                loop.play();
            startLoop = !startLoop;
        });
}



    // FIXME Task 8: Implement challenges (you may use challenges and assets provided for you in comp1110.ass2.gui.assets: sq-b.png, sq-g.png, sq-r.png & sq-w.png)

    String c = "RRBWGBWRR".toLowerCase();
    private void getChallenge(){
        for (int i =0;i<c.length();i++){
            String pic = getClass().getResource("assets/sq-" + c.charAt(i) + ".png").toString();
            ImageView image = new ImageView(pic);
            int col = i%3;
            int row = i/3;
            image.setY(35+row*50);
            image.setX(200+col*50);
            image.setFitHeight(SQUARE_SIZE);
            image.setFitWidth(SQUARE_SIZE);
            challenge.getChildren().add(image);
        }
    }

    /**
     * Set up the group that represents the solution (and make it transparent)
     *
     * @param solution The solution as an array of chars.
     */
    private void makeSolution(String solution) {
        this.solution.getChildren().clear();

        if (solution.length() == 0) {
            return;
        }

        if (solution.length() != 40) {
            throw new IllegalArgumentException("Solution incorrect length: " + solution);
        }

        solutionString = solution;
        for (int i = 0; i < solution.length(); i+=4) {
            GameTile gtile = new GameTile(solution.charAt(i), Tile.placementToOrientation(solution.substring(i,i+4)).ordinal());
            int x = solution.charAt(i+1) - '0';
            int y = solution.charAt(i+2) - '0';

            gtile.setLayoutX(PLAY_AREA_X + (x * SQUARE_SIZE));
            gtile.setLayoutY(PLAY_AREA_Y + (y * SQUARE_SIZE));

            this.solution.getChildren().add(gtile);
        }
        this.solution.setOpacity(0);
    }

    // FIXME Task 10: Implement hints
    private void glowHints(){
        Glow glow = new Glow();
        glow.setLevel(0.9);
    }

     // add a pop up window but have a bug
//    private void popUpWindow(){
//        Stage window = new Stage();
//        window.initModality(Modality.APPLICATION_MODAL);
//        window.setTitle("Hints");
//        window.setMinWidth(250);
//
//        Label label = new Label();
//        label.setText("Continuing to build Hint for solving the challenge");
//        Button closeButton = new Button("Close the window");
//        closeButton.setOnAction(e -> window.close());
//
//        VBox layout = new VBox(10);
//        layout.getChildren().addAll(label,closeButton);
//        layout.setAlignment(Pos.CENTER);
//
//        Scene s1 = new Scene(layout);
//        window.setScene(s1);
//        window.showAndWait();
//
//    }







    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)


    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     */
    private void makeControls() {
        Button button = new Button("Restart");
        button.setLayoutX(BOARD_X + 240);
        button.setLayoutY(GAME_HEIGHT - 55);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                newGame();
            }
        });
        controls.getChildren().add(button);

//        difficulty.setMin(1);
//        difficulty.setMax(4);
//        difficulty.setValue(0);
//        difficulty.setShowTickLabels(true);
//        difficulty.setShowTickMarks(true);
//        difficulty.setMajorTickUnit(1);
//        difficulty.setMinorTickCount(1);
//        difficulty.setSnapToTicks(true);
//
//        difficulty.setLayoutX(BOARD_X  + 70);
//        difficulty.setLayoutY(GAME_HEIGHT - 50);
//        controls.getChildren().add(difficulty);
//
//        final Label difficultyCaption = new Label("Difficulty:");
//        difficultyCaption.setTextFill(Color.GREY);
//        difficultyCaption.setLayoutX(BOARD_X);
//        difficultyCaption.setLayoutY(GAME_HEIGHT - 50);
//        controls.getChildren().add(difficultyCaption);
    }


    /**
     * Set the gaming board
     */
    private void showBoard(){
        board.getChildren().clear();

        ImageView baseboard = new ImageView();
        baseboard.setImage(new Image(BASEBOARD_URI));
        baseboard.setFitWidth(BOARD_WIDTH);
        baseboard.setFitHeight(BOARD_HEIGHT);
        baseboard.setLayoutX(BOARD_X+15*MARGIN_X);
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


    /**
     * Add the objective to the board
     */
    private void addObjectiveToBoard() {
        objective.getChildren().clear();
        //objective.getChildren().add(new GameTile(FocusGame.getObjective().getProblemNumber(), OBJECTIVE_MARGIN_X, OBJECTIVE_MARGIN_Y));
    }

    /**
     * Check game completion and update status
     */
    private void checkCompletion() {
        String state = new String("");
        for (int i = 0; i < 10; i++) {
            if (tileState[i] == NOT_PLACED)
                return;
            state = state +
                    (char)(i + 'a') +
                    (char)(((tileState[i]/4)%4)+'0') +
                    (char)(((tileState[i]/4)/4)+'0') +
                    (Orientation.values()[tileState[i]%4]);
        }

        if (state.equals(solutionString))
            showCompletion();
        else
            return;
    }

    /**
     * Show the completion message
     */
    private void showCompletion() {
        completionText.toFront();
        completionText.setOpacity(1);
    }

    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }

    /**
     * Create the message to be displayed when the player completes the puzzle.
     */
    private void makeCompletion() {
        completionText.setFill(Color.BLACK);
        completionText.setEffect(dropShadow);
        completionText.setCache(true);
        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        completionText.setLayoutX(20);
        completionText.setLayoutY(375);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }


    /**
     * Put all of the tiles back in their home position
     */
    private void resetPieces() {
        gtiles.toFront();
        for (Node n : gtiles.getChildren()) {
            ((DraggableTile) n).snapToHome();
        }
    }

    private void newGame(){
        try{
            hideCompletion();
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
        getChallenge();
        setUpSoundLoop();
        root.getChildren().add(gtiles);
        root.getChildren().add(board);
        root.getChildren().add(solution);
        root.getChildren().add(controls);
        root.getChildren().add(exposed);
        root.getChildren().add(objective);
        root.getChildren().add(challenge);

        root.getChildren().add(shadow);
//        root.getChildren().add(window);


        // TODO set handlers, sound, board, tiles

        setUpHandlers(scene);
//        popUpWindow();
//        setUpSoundLoop();
        showBoard();
        makeControls();
        makeCompletion();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
