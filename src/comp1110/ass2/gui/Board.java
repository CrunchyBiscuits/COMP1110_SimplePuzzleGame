package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.scene.control.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class Board extends Application {
    /**
     * variable setting
     * author Zheyuan Zhang u6870923   line 40 - line 81
     */
    private static final int SQUARE_SIZE = 40;

    private static final int MARGIN_X = 15;
    private static final int MARGIN_Y = 15;

    private static final int BOARD_WIDTH = 420;
    private static final int BOARD_HEIGHT = 290;
    private static final int BOARD_MARGIN_X = 30;
    private static final int BOARD_MARGIN_Y = 60;

    private static final int OBJECTIVE_HEIGHT = 50;
    private static final int OBJECTIVE_MARGIN_Y = 20;

    private static final int BOARD_Y = 350;
    private static final int BOARD_X = 200;

    private static final int PLAY_AREA_Y = BOARD_Y + BOARD_MARGIN_Y;
    private static final int PLAY_AREA_X = BOARD_X + BOARD_MARGIN_X;
    private static final int GAME_WIDTH = 933;
    private static final int GAME_HEIGHT = 700;
    private static final long ROTATION_THRESHOLD = 50; // Allow rotation every 50 ms

    /* marker for unplaced tiles */
    public static final int NOT_PLACED = 255;

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
    private final Group hint = new Group();
//    private final Group window = new Group();

    private static String challengeString;

    /**
     * tile state setting
     * author Zheyuan Zhang u6870923
     */
    int[] tileState = new int[10];   //  all off screen to begin with

    /**
     * finish text and difficulty choice
     * author Zheyuan Zhang u6870923   line 109 - line 126,   author Siyu Zhou line 113-120
     */
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


    /**
     * GameTile class
     * Construct particular playing tiles
     * author Zheyuan Zhang u6870923   line 134 - line 249
     */
    class GameTile extends ImageView{
        int tileID;

        /**
         * Construct a particular playing tile
         *
         * @param tile The letter representing the tile to be created
         */
         GameTile(char tile) {
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
            char col = getRowAndCol(tileID, orientation).charAt(0);
            char row = getRowAndCol(tileID, orientation).charAt(1);
            setFitHeight(row*SQUARE_SIZE);
            setFitWidth(col*SQUARE_SIZE);
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


        public String getRowAndCol(int tileID, int orientation){
            if (orientation%2 == 0) {
                if (tileID==0||tileID==3||tileID==4||tileID==6){
                    return "23";
                }else if (tileID==1||tileID==2||tileID==9){
                    return "24";
                }else if (tileID==5){
                    return "13";
                }else if (tileID==7){
                    return "33";
                }else {
                    return "22";
                }
            }
            else {
                if (tileID==0||tileID==3||tileID==4||tileID==6){
                    return "32";
                }else if (tileID==1||tileID==2||tileID==9){
                    return "42";
                }else if (tileID==5){
                    return "31";
                }else if (tileID==7){
                    return "33";
                }else {
                    return "22";
                }
            }
        }
    }

    // FIXME Task 7: Implement a basic playable Focus Game in JavaFX that only allows pieces to be placed in valid places
    // DraggableTile class author Zheyuan Zhang u6870923
    /**
     * DraggableTile class
     * Construct particular draggable tiles
     * author Zheyuan Zhang u6870923   line 253 - line 912
     */
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
            homeX = MARGIN_X + ((tile-'a')%5)*4*SQUARE_SIZE;
            setLayoutX(homeX);
            homeY = OBJECTIVE_MARGIN_Y  + MARGIN_Y+((tile-'a')/5)*4*SQUARE_SIZE;
            setLayoutY(homeY);

            //handling events
            setOnScroll(event ->{
                if (System.currentTimeMillis()-lastRotationTime>ROTATION_THRESHOLD){
                    lastRotationTime = System.currentTimeMillis();
                    hideCompletion();
                    rotate();
                    event.consume();
                }
            });



            //start of the drag
            setOnMousePressed(event->{
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                tileState[tileID]=NOT_PLACED;
            });

            //dragging
            setOnMouseDragged(event->{
                hideCompletion();
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
                if ((getLayoutX() >= (PLAY_AREA_X - (SQUARE_SIZE / 2))) && (getLayoutX() < (PLAY_AREA_X + (SQUARE_SIZE / 2)))) {
                    setLayoutX(PLAY_AREA_X);
                } else if ((getLayoutX() >= PLAY_AREA_X + (SQUARE_SIZE / 2)) && (getLayoutX() < PLAY_AREA_X + 1.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 1.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 2.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 2 * SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 2.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 3.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 3 * SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 3.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 4.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 4 * SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 4.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 5.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 5 * SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 5.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 6.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 6 * SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 6.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 7.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 7 * SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 7.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 8.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 8 * SQUARE_SIZE);
                }


                if ((getLayoutY() >= (PLAY_AREA_Y - (SQUARE_SIZE / 2))) && (getLayoutY() < (PLAY_AREA_Y + (SQUARE_SIZE / 2)))) {
                    setLayoutY(PLAY_AREA_Y);
                } else if ((getLayoutY() >= PLAY_AREA_Y + (SQUARE_SIZE / 2)) && (getLayoutY() < PLAY_AREA_Y + 1.5 * SQUARE_SIZE)) {
                    setLayoutY(PLAY_AREA_Y + SQUARE_SIZE+5);
                } else if ((getLayoutY() >= PLAY_AREA_Y + 1.5 * SQUARE_SIZE) && (getLayoutY() < PLAY_AREA_Y + 2.5 * SQUARE_SIZE)) {
                    setLayoutY(PLAY_AREA_Y + 2 * SQUARE_SIZE+ 5);
                } else if ((getLayoutY() >= PLAY_AREA_Y + 2.5 * SQUARE_SIZE) && (getLayoutY() < PLAY_AREA_Y + 3.5 * SQUARE_SIZE)) {
                    setLayoutY(PLAY_AREA_Y + 3 * SQUARE_SIZE+ 5);
                } else if ((getLayoutY() >= PLAY_AREA_Y + 3.5 * SQUARE_SIZE) && (getLayoutY() < PLAY_AREA_Y + 4.5 * SQUARE_SIZE)) {
                    setLayoutY(PLAY_AREA_Y + 4 * SQUARE_SIZE+ 5);
                }

            if (onBoard()&&!alreadyOccupied()) {
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
            char row = getRowAndCol(tileID, orientation).charAt(0);
            char col = getRowAndCol(tileID, orientation).charAt(1);
            switch (row){
                case '1':
                    return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 6.5 * SQUARE_SIZE))
                            && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 4.5 * SQUARE_SIZE));
                case '2':
                    if (col=='2'){
                        return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 7.5 * SQUARE_SIZE))
                                && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 3.5 * SQUARE_SIZE));
                    }else if (col=='3'){
                        return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 6.5 * SQUARE_SIZE))
                                && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 3.5 * SQUARE_SIZE));
                    }else if (col=='4'){
                        return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 5.5 * SQUARE_SIZE))
                                && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 3.5 * SQUARE_SIZE));
                    }
                case '3':
                    if (col=='1'){
                        return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 8.5 * SQUARE_SIZE))
                                && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 2.5 * SQUARE_SIZE));
                    }else if (col=='2'){
                        return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 7.5 * SQUARE_SIZE))
                                && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 2.5 * SQUARE_SIZE));
                    }else if (col=='3'){
                        return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 6.5 * SQUARE_SIZE))
                                && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 2.5 * SQUARE_SIZE));
                    }
                case '4':
                    if (col=='2'){
                        return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 7.5 * SQUARE_SIZE))
                                && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 1.5 * SQUARE_SIZE));
                    }
            }
            return false;
        }

        /**
         * Get the occupied place align with different tiles
         * */
        private String getOccupiedPosition(int tileID, int col, int row, int orientation){
            String result="";
            switch (tileID){
                case 0:
                    if (orientation==0){
                        for (int i=0;i<3;i++){
                            result += col+i;
                            result += row;
                        }
                        result += col+1;
                        result += row+1;
                        return result;
                    }else if (orientation==1){
                        result += col;
                        result += row+1;
                        for (int i=0;i<3;i++){
                            result += col+1;
                            result += row+i;
                        }
                        return result;
                    }else if (orientation==2){
                        result += col+1;
                        result += row;
                        for (int i=0;i<3;i++){
                            result += col+i;
                            result += row+1;
                        }
                        return result;
                    }else {
                        for (int i=0;i<3;i++){
                            result += col;
                            result += row+i;
                        }
                        result += col+1;
                        result += row+1;
                        return result;
                    }
                case 1:
                    if (orientation==0){
                        for (int i=0;i<3;i++){
                            result += col+1+i;
                            result += row;
                        }

                        for (int i=0;i<2;i++){
                            result += col+i;
                            result += row+1;
                        }
                        return  result;
                    }else if (orientation==1){
                        for (int i=0;i<2;i++){
                            result += col;
                            result += row+i;
                        }

                        for (int i=0;i<3;i++){
                            result += col+1;
                            result += row+i+1;
                        }
                        return result;
                    }else if (orientation==2){
                        for (int i=0;i<2;i++){
                            result += col+2+i;
                            result += row;
                        }
                        for (int i=0;i<3;i++){
                            result += col+i;
                            result += row+1;
                        }
                        return result;
                    }else {
                        for (int i=0;i<2;i++){
                            result += col+1;
                            result += row+i+2;
                        }
                        for (int i=0;i<3;i++){
                            result += col;
                            result += row+i;
                        }
                        return result;
                    }
                case 2:
                    if (orientation==0){
                        result+=col+2;
                        result+=row;
                        for (int i=0;i<4;i++){
                            result+=col+i;
                            result+=row+1;
                        }
                        return result;
                    }else if (orientation==1){
                        result+=col+1;
                        result+=row+2;
                        for (int i=0;i<4;i++){
                            result+=col;
                            result+=row+i;
                        }
                        return result;
                    }else if (orientation==2){
                        result+=col+1;
                        result+=row+1;
                        for (int i=0;i<4;i++){
                            result+=col+i;
                            result+=row;
                        }
                        return result;
                    }else {
                        result+=col;
                        result+=row+1;
                        for (int i=0;i<4;i++){
                            result+=col+1;
                            result+=row+i;
                        }
                        return result;
                    }
                case 3:
                    if (orientation==0){
                        result += col+2;
                        result += row+1;
                        for (int i=0;i<3;i++){
                            result += col+i;
                            result += row;
                        }
                        return result;
                    }else if (orientation==1){
                        result += col;
                        result += row+2;
                        for (int i=0;i<3;i++){
                            result += col+1;
                            result += row+i;
                        }
                        return result;
                    }else if (orientation==2){
                        result += col;
                        result += row;
                        for (int i=0;i<3;i++){
                            result += col+i;
                            result += row+1;
                        }
                        return result;
                    }else {
                        result += col+1;
                        result += row;
                        for (int i=0;i<3;i++){
                            result += col;
                            result += row+i;
                        }
                        return result;
                    }
                case 4:
                    if (orientation==0){
                        for (int i=0;i<3;i++){
                            for (int j=0;j<2;j++){
                                if (j==1&&i==2)
                                    continue;
                                else {
                                    result +=col+i;
                                    result +=row+j;
                                }
                            }
                        }
                        return result;
                    }else if (orientation==1){
                        for (int i=0;i<3;i++){
                            for (int j=0;j<2;j++){
                                if (j==0&&i==2)
                                    continue;
                                else {
                                    result +=col+j;
                                    result +=row+i;
                                }
                            }
                        }
                        return result;
                    }else if (orientation==2){
                        for (int i=0;i<3;i++){
                            for (int j=0;j<2;j++){
                                if (j==0&&i==0)
                                    continue;
                                else {
                                    result +=col+i;
                                    result +=row+j;
                                }
                            }
                        }
                        return result;
                    }else {
                        for (int i=0;i<3;i++){
                            for (int j=0;j<2;j++){
                                if (j==1&&i==0)
                                    continue;
                                else {
                                    result +=col+j;
                                    result +=row+i;
                                }
                            }
                        }
                        return result;
                    }
                case 5:
                    if (orientation%2==0){
                        for (int i=0;i<3;i++){
                            result+=col+i;
                            result+=row;
                        }
                        return result;
                    }else {
                        for (int i=0;i<3;i++){
                            result+=col;
                            result+=row+i;
                        }
                        return result;
                    }
                case 6:
                    if (orientation%2==0){
                        for (int i=0;i<3;i++){
                            for (int j=0;j<2;j++){
                                if ((i==2&&j==0)||(i==0&&j==1)){
                                    continue;
                                }else {
                                    result += col+i;
                                    result += row+j;
                                }
                            }
                        }
                        return result;
                    }else {
                        for (int i=0;i<3;i++){
                            for (int j=0;j<2;j++){
                                if ((i==0&&j==0)||(i==2&&j==1)){
                                    continue;
                                }else {
                                    result += col+j;
                                    result += row+i;
                                }
                            }
                        }
                        return result;
                    }
                case 7:
                    if (orientation==0){
                        for (int i=0;i<3;i++){
                            result += col+i;
                            result += row;
                        }
                        for (int i=0;i<2;i++){
                            result += col;
                            result += row+1+i;
                        }
                        return result;
                    }else if (orientation==1){
                        for (int i=0;i<3;i++){
                            result += col+i;
                            result += row;
                        }
                        for (int i=0;i<2;i++){
                            result += col+2;
                            result += row+1+i;
                        }
                        return result;
                    }else if (orientation==2){
                        for (int i=0;i<3;i++){
                            result += col+i;
                            result += row+2;
                        }
                        for (int i=0;i<2;i++){
                            result += col+2;
                            result += row+i;
                        }
                        return result;
                    }else {
                        for (int i=0;i<3;i++){
                            result += col;
                            result += row+i;
                        }
                        for (int i=0;i<2;i++){
                            result += col+1+i;
                            result += row+2;
                        }
                        return result;
                    }
                case 8:
                    if (orientation==0){
                       for (int i=0;i<2;i++){
                           for (int j=0;j<2;j++){
                                if (i==0&&j==1)
                                    continue;
                                else {
                                    result+=col+i;
                                    result+=row+j;
                                }
                           }
                       }
                       return result;
                    }else if (orientation==1){
                        for (int i=0;i<2;i++){
                            for (int j=0;j<2;j++){
                                if (i==0&&j==0)
                                    continue;
                                else {
                                    result+=col+i;
                                    result+=row+j;
                                }
                            }
                        }
                        return result;
                    }else if (orientation==2){
                        for (int i=0;i<2;i++){
                            for (int j=0;j<2;j++){
                                if (i==1&&j==0)
                                    continue;
                                else {
                                    result+=col+i;
                                    result+=row+j;
                                }
                            }
                        }
                        return result;
                    }else {
                        for (int i=0;i<2;i++){
                            for (int j=0;j<2;j++){
                                if (i==1&&j==1)
                                    continue;
                                else {
                                    result+=col+i;
                                    result+=row+j;
                                }
                            }
                        }
                        return result;
                    }
                case 9:
                    if (orientation==0){
                        result += col;
                        result += row+1;
                        for (int i=0;i<4;i++){
                            result += col+i;
                            result += row;
                        }
                        return result;
                    }else if (orientation==1){
                        result += col;
                        result += row;
                        for (int i=0;i<4;i++){
                            result += col+1;
                            result += row+i;
                        }
                        return result;
                    }else if (orientation==2){
                        result += col+3;
                        result += row;
                        for (int i=0;i<4;i++){
                            result += col+i;
                            result += row+1;
                        }
                        return result;
                    }else {
                        result += col+1;
                        result += row+3;
                        for (int i=0;i<4;i++){
                            result += col;
                            result += row+i;
                        }
                        return result;
                    }
            }
            return "";
        }

        /**
         * check whether the square of board is occupied or not
         * */
        private boolean checkOccupied(String existedPlacement, String currentPlacement){
            String existedString[]=new String[existedPlacement.length()/2];
            String currentString[]=new String[currentPlacement.length()/2];
            for (int i=0;i<existedString.length;i++){
                existedString[i]=existedPlacement.substring(i*2,i*2+2);
            }
            for (int i=0;i<currentString.length;i++){
                currentString[i]=currentPlacement.substring(i*2,i*2+2);
            }

            for (int i=0;i<existedString.length;i++){
                for (int j=0;j<currentString.length;j++){
                    if (existedString[i].equals(currentString[j]))
                        return true;
                }
            }
            return false;
        }

        /**
         * Get the occupied squares and new tile can not be placed on them
         */
        private boolean alreadyOccupied(){
            int x = (int) (getLayoutX() - PLAY_AREA_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() - PLAY_AREA_Y) / SQUARE_SIZE;

            // place for two block area
            String block1="04";
            String block2="84";

            String occupiedPlacement= getOccupiedPosition(tileID,x,y,orientation);
            if (checkOccupied(block1,occupiedPlacement)||checkOccupied(block2,occupiedPlacement)){
                return true;
            }
            for (int i=0;i<10;i++){
                if (tileState[i]==NOT_PLACED)
                    continue;

                int tx = tileState[i]/100%10;
                int ty = tileState[i]/10%10;
                int tOri = tileState[i]%10;
                String existedPlacement = getOccupiedPosition(i, tx, ty, tOri);
                if (checkOccupied(existedPlacement, occupiedPlacement)){
                    return true;
                }
            }
            return false;
        }

        /**
         * set the position of tile
         * update the tile state
         */
        private void setPosition(){
            int x = (int) (getLayoutX() - PLAY_AREA_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() - PLAY_AREA_Y) / SQUARE_SIZE;
            if (x < 0)
                tileState[tileID] = NOT_PLACED;
            else {
                tileState[tileID] =tileID*1000+x*100+y*10+orientation;
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

    private String music = getClass().getResource( "assets/Toby Fox - Start Menu.mp3").toString();
    private AudioClip loop;

    /**
     * set the background music repeating
     * author Siyu Zhou u6692356 line 933-938
     * */
    private void setUpSoundLoop() {
            loop = new AudioClip(music);
            loop.setCycleCount(AudioClip.INDEFINITE);
            loop.setVolume(5.0);
            loop.play();
    }



    // FIXME Task 8: Implement challenges (you may use challenges and assets provided for you in comp1110.ass2.gui.assets: sq-b.png, sq-g.png, sq-r.png & sq-w.png)
    /**
     * show the images of challenge align with challenge string
     * author Siyu Zhou u6692356 line 944-958
     * */
    private void getChallenge(){
        challengeString = Challenge.getInterestingChallenge();
        for (int i =0;i<challengeString.length();i++){
            /*loop to get each challenge and get the resource of pictures*/
            String pic = getClass().getResource("assets/sq-" + challengeString.charAt(i) + ".png").toString();
            ImageView image = new ImageView(pic); // basic
            int col = i%3;
            int row = i/3;
            image.setY(350+row*40);
            image.setX(750+col*40);
            image.setFitHeight(SQUARE_SIZE);
            image.setFitWidth(SQUARE_SIZE);
            challenge.getChildren().add(image);
        }
    }


    // FIXME Task 10: Implement hints
    /**
     * create hints for players
     * author Siyu Zhou u6692356 line 967-1036, 1055-1072
     */
    /* transform the state into the placement */
    private String stateToPlacement() {
        String placement = "";
        for (int i = 0; i < 10; i++) {
            if (tileState[i] == NOT_PLACED)
                continue;

            int tx = tileState[i]/100%10;
            int ty = tileState[i]/10%10;
            int tOri = tileState[i]%10;
            placement = placement + (char) (i + 'a') + tx + ty + tOri;
        }
        return placement;
    }

    /* find next move based on placement and solution */
    private String findNextMove(String placement, String solution) {
        /* split placement into groups and the length of each group is four */
        String[] pPieces = placement.split("(?<=\\G.{4})");
        if(pPieces.length == 10) {
            return null;
        }
        /* split solution into groups and the length of each group is four */
        String[] sPieces = solution.split("(?<=\\G.{4})");

        /* set the initial nextMove is a empty string, once it find the placement for nextMove, then add into nextMove */
       String nextMOve = "";
        if (!placement.isBlank()) {
            List<String> pieceNames = new ArrayList<>();
            for (String piece : pPieces)
                pieceNames.add(piece.substring(0, 1));

            for (String piece : sPieces) {
                if (!pieceNames.contains(piece.substring(0, 1))) {
                    nextMOve = piece;
                    break;
                }
            }
        } else {
            nextMOve = sPieces[0];
        }
        return nextMOve;
    }

    /* place the hint piece on the board */
    private void placeHintPiece(String nextMove) {
        /* get hint placement details from the substring of nextMove*/
        String pieceName = nextMove.substring(0, 1);
        Integer pieceX = Integer.parseInt(nextMove.substring(1, 2));
        Integer pieceY = Integer.parseInt(nextMove.substring(2, 3));
        String pieceOri = nextMove.substring(3, 4);
        TileType pieceType = TileType.valueOf(pieceName.toUpperCase());

        /* based on hint placement and find the relevant the tile piece image */
        String piecePath = Board.class.getResource(URI_BASE + pieceName + "-" + pieceOri + ".png").toString();

        /* display the hint*/
        ImageView pieceView = new ImageView(new Image(piecePath));

        /* set hint view size which can fit on the board */
        pieceView.setFitWidth(pieceType.getWidth(Integer.parseInt(pieceOri)) * SQUARE_SIZE);
        pieceView.setFitHeight(pieceType.getHeight(Integer.parseInt(pieceOri)) * SQUARE_SIZE);

        pieceView.setX(PLAY_AREA_X + pieceX * SQUARE_SIZE);
        pieceView.setY(PLAY_AREA_Y + pieceY * SQUARE_SIZE);

        /**
         * author: Jianwu Yao u6987162 from line 1030 to line 1037
         * this part of code was learned from stackOverFlow mainly on the use of PauseTransition
         * reason: did not know all methods of JavaFx but we just need those method to help us
         * thanks stackOverFlow
         */
        pieceView.setOpacity(0.5);
        board.getChildren().add(pieceView);
        PauseTransition wait = new PauseTransition(Duration.seconds(1.5));
        wait.setOnFinished((e) -> {
            board.getChildren().remove(pieceView);
            wait.playFromStart();
        });
        wait.play();
    }

    /* create a hint button and implement the button with nextMove */
    private void getHints() {
        String solution = FocusGame.getSolution(challengeString);

        Button button = new Button("Hints");
        button.setLayoutX(BOARD_X + 500);
        button.setLayoutY(GAME_HEIGHT - 100);

        button.setOnAction(event -> {

            String placement = stateToPlacement();
            String nextMove = findNextMove(placement, solution);

            if (nextMove != null)
                placeHintPiece(nextMove);
        });
        controls.getChildren().add(button);
    }


    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)
    private void setSolution() {
        String c = Challenge.getInterestingChallenge();
        String s = FocusGame.getSolution(c);

    }

    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     * author Zheyuan Zhang u6870923
     * author Siyu Zhou u6692356
     * line 1107 - line 1142
     */
    private void makeControls() {
        Button button = new Button("NewGame");
        button.setLayoutX(BOARD_X + 500);
        button.setLayoutY(GAME_HEIGHT - 170);
        button.setOnAction(e -> {
            newGame();
            challenge.getChildren().clear();
            getChallenge();
            getHints();
        });

        controls.getChildren().add(button);

        // author: Jianwu Yao u6987162 From line 1112 to line 1180
        // co-author: zheyuan Zhang
        Button ruleButton = new Button("PlayRule");
        ruleButton.setLayoutX(BOARD_X + 600);
        ruleButton.setLayoutY(GAME_HEIGHT - 170);

        ruleButton.setOnMouseClicked((e) -> {
            Group anotherRoot = new Group();
            Stage popRuleWindow = new Stage();
            Scene scene = new Scene(anotherRoot, 300, 350);
            popRuleWindow.setTitle("How To Play");
            popRuleWindow.setScene(scene);


            Label labelGamePlay = new Label("Gameplay: We need to fill the top ten differently shaped blocks into the lower board. We can use the mouse to place the block on a separate block to rotate the block to adjust the pose of the block to get the shape we need. In addition, we have the color requirements of the nine points of the center on the right side, that is, the third point of the second line to the sixth point of the fourth line. When we put all the blocks into the board and the nine points in the center meet the challenge requirements, you win!");
            labelGamePlay.setWrapText(true);
            labelGamePlay.setFont(new Font("Arial", 14));
            labelGamePlay.setMaxWidth(300);

            Label labelRestart = new Label("NewGame is to restart a new game");
            Label labelHints = new Label("Hints are giving hints, such as placing a block in a location");
            Label labelPlayRule = new Label("Playrule will introduce the gameplay");
            Label labelClear = new Label("Restart will empty the board, but will not start a new game");

            labelRestart.setWrapText((true));
            labelRestart.setLayoutX(0);
            labelRestart.setLayoutY(200);
            labelRestart.setFont(new Font("Arial", 14));
            labelRestart.setMaxWidth(300);

            labelHints.setWrapText((true));
            labelHints.setLayoutX(0);
            labelHints.setLayoutY(220);
            labelHints.setFont(new Font("Arial", 14));
            labelHints.setMaxWidth(300);

            labelPlayRule.setWrapText((true));
            labelPlayRule.setLayoutX(0);
            labelPlayRule.setLayoutY(260);
            labelPlayRule.setFont(new Font("Arial", 14));
            labelPlayRule.setMaxWidth(300);

            labelClear.setWrapText((true));
            labelClear.setLayoutX(0);
            labelClear.setLayoutY(280);
            labelClear.setFont(new Font("Arial", 14));
            labelClear.setMaxWidth(300);


            anotherRoot.getChildren().add(labelGamePlay);
            anotherRoot.getChildren().add(labelRestart);
            anotherRoot.getChildren().add(labelHints);
            anotherRoot.getChildren().add(labelPlayRule);
            anotherRoot.getChildren().add(labelClear);

            popRuleWindow.show();
        });

        controls.getChildren().add(ruleButton);

        Button clearButton = new Button("Restart");
        clearButton.setLayoutX(BOARD_X + 600);
        clearButton.setLayoutY(GAME_HEIGHT - 100);

        clearButton.setOnMouseClicked(e -> {
            resetPieces();
            hideCompletion();
        });

        controls.getChildren().add(clearButton);


//        difficulty.setMin(1);
//        difficulty.setMax(5);
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
     * Set the gaming board and tiles
     * author Zheyuan Zhang u6870923   line 1149 - line 1161
     */
    private void showBoard(){
        board.getChildren().clear();

        ImageView baseboard = new ImageView();
        baseboard.setImage(new Image(BASEBOARD_URI));
        baseboard.setFitWidth(BOARD_WIDTH);
        baseboard.setFitHeight(BOARD_HEIGHT);
        baseboard.setLayoutX(BOARD_X);
        baseboard.setLayoutY(BOARD_Y);
        board.getChildren().add(baseboard);

        board.toBack();
        hideCompletion();
    }

    /**
     * Set the gaming board and tiles
     * author Zheyuan Zhang u6870923   line 1167 - line 1171
     */
    private void showTiles(){
        gtiles.getChildren().clear();
        for (char m='a';m<='j';m++){
            gtiles.getChildren().add(new DraggableTile(m));
        }
    }

    /**
     * Check game completion and update status
     * author Zheyuan Zhang u6870923   line 1186 - line 1223
     */
    private void checkCompletion() {
        String solution = FocusGame.getSolution(challengeString);
        String[] states = new String[10];
        String[] answers = new String[10];

        for (int i = 0; i < 10; i++) {
            if (tileState[i] == NOT_PLACED)
                return;
            states[i] = "" +
                    (char)(i + 'a') +
                    (tileState[i]/100%10) +
                    (tileState[i]/10%10) +
                    (tileState[i]%10);

            answers[i]=solution.substring(i*4,i*4+4);
           // stateString += states[i];
        }

        for (int i=0;i<10;i++){
            boolean equalFlag = false;
            for (int j=0;j<10;j++){
                if (answers[i].equals(states[j])){
                    equalFlag = true;
                } else if (answers[i].charAt(0)==states[j].charAt(0)){
                    if ((states[j].charAt(3)+2)==answers[i].charAt(3)||(states[j].charAt(3)-2)==answers[i].charAt(3)){
                        equalFlag = true;
                    }
                }
            }
            if (!equalFlag){
                return;
            }
        }

        showCompletion();
    }

    /**
     * Show the completion message
     * author Zheyuan Zhang u6870923   line 1229 - line 1232
     */
    private void showCompletion() {
        completionText.toFront();
        completionText.setOpacity(1);
    }

    /**
     * Hide the completion message
     * author Zheyuan Zhang u6870923   line 1238 - line 1241
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }




    /**
     * Create the message to be displayed when the player completes the puzzle.
     * author Zheyuan Zhang u6870923   line 1250 - line 1259
     */
    private void makeCompletion() {
        completionText.setFill(Color.BLACK);
        completionText.setEffect(dropShadow);
        completionText.setCache(true);
        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        completionText.setLayoutX(300);
        completionText.setLayoutY(175);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }


    /**
     * Put all of the tiles back in their home position
     * author Zheyuan Zhang u6870923   line 1267 - line 1272
     */
    private void resetPieces() {
        gtiles.toFront();
        for (Node n : gtiles.getChildren()) {
            ((DraggableTile) n).snapToHome();
        }
    }

    /**
     * Create a new game
     * author Zheyuan Zhang u6870923   line 1278 - line 1288
     */
    private void newGame(){
        try{
            hideCompletion();
            focusgame = new FocusGame();
            showTiles();
        }catch (IllegalArgumentException e){
            System.err.println(e);
            e.printStackTrace();
            Platform.exit();
        }
    }


    /**
     * Start a game
     * author Zheyuan Zhang u6870923   line 1296 - line 1328
     */
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
        root.getChildren().add(hint);

        setUpSoundLoop();
        showBoard();
        makeControls();
        makeCompletion();
        getHints();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
