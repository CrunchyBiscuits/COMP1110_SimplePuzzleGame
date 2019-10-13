package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
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
import java.util.concurrent.TimeUnit;

import static comp1110.ass2.FocusGame.*;

public class Board extends Application {

    //variable setting author Zheyuan Zhang u6870923
    private static final int SQUARE_SIZE = 40;

    private static final int MARGIN_X = 15;
    private static final int MARGIN_Y = 15;

    private static final int BOARD_WIDTH = 420;
    private static final int BOARD_HEIGHT = 290;
    private static final int BOARD_MARGIN_X = 30;
    private static final int BOARD_MARGIN_Y = 60;

    private static final int OBJECTIVE_WIDTH = 162;
    private static final int OBJECTIVE_HEIGHT = 150;
    private static final int OBJECTIVE_MARGIN_X = 100;
    private static final int OBJECTIVE_MARGIN_Y = 20;

    private static final int BOARD_Y = MARGIN_Y;
    private static final int BOARD_X = 500;

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
    private static String solutionString;

    /* the state of the tiles */
    int[] tileState = new int[10];   //  all off screen to begin with

    public void showTileStateNow() {
        for(int i = 0; i < tileState.length; i++) {
            System.out.print(tileState[i] + " ");
        }
        System.out.println();
    }

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

    //GameTile class author Zheyuan Zhang u6870923
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
            /*if (orientation%2 == 0) {
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
            }*/
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
        //TODO another constructor for objective tile
    }

    // FIXME Task 7: Implement a basic playable Focus Game in JavaFX that only allows pieces to be placed in valid places
    // DraggableTile class author Zheyuan Zhang u6870923
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
            homeY = OBJECTIVE_MARGIN_Y + OBJECTIVE_HEIGHT + MARGIN_Y+((tile-'a')/3)*3*SQUARE_SIZE;
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
                tileState[tileID]=NOT_PLACED;
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
                showTileStateNow();
//                System.out.println(getLayoutY());
//                System.out.println(getLayoutX());
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
//            System.out.println(col);
//            System.out.println(row);
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
                            result += row+i;
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
                            result += row+i;
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

        private boolean alreadyOccupied(){
            int x = (int) (getLayoutX() - PLAY_AREA_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() - PLAY_AREA_Y) / SQUARE_SIZE;

//            System.out.println(x);
//            System.out.println(y);
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
    private boolean startLoop = false;

    private String music = getClass().getResource( "assets/484103__greenfiresound__click-08.wav").toString();
    private AudioClip loop;

    private void setUpSoundLoop() {
            loop = new AudioClip(music);
            loop.setCycleCount(AudioClip.INDEFINITE);
    }

    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
       /* scene.setOnMouseClicked(event -> {
            if (startLoop)
                loop.stop();
            else
                loop.play();
            startLoop = !startLoop;
        });*/
}



    // FIXME Task 8: Implement challenges (you may use challenges and assets provided for you in comp1110.ass2.gui.assets: sq-b.png, sq-g.png, sq-r.png & sq-w.png)

    /**
     * show the images of challenge align with challenge string
    * */

    private void getChallenge(){
        challengeString = Challenge.getInterestingChallenge();
        for (int i =0;i<challengeString.length();i++){
            //loop to get each challenge and get the resource of pictures
            String pic = getClass().getResource("assets/sq-" + challengeString.charAt(i) + ".png").toString();
            ImageView image = new ImageView(pic); // basic
            image.setOpacity(0.5);
            int col = i%3;
            int row = i/3;
            image.setY(50+row*40);
            image.setX(200+col*40);
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

    private String stateToPlacement() {
        String placement = "";
        for (int i = 0; i < 10; i++) {
            if (tileState[i] == NOT_PLACED)
                continue;

            String currentPiece = Integer.toString(tileState[i]);
            placement = placement + (char) (i + 'a') + currentPiece.substring(1, 4);

        }
        return placement;
    }

    private String findNextMove(String placement, String solution) {
        String[] pPieces = placement.split("(?<=\\G.{4})");
        System.out.println("打印placement的状态");
        for(int i = 0; i < pPieces.length; i++) {
            System.out.println(pPieces[i]);
        }
        System.out.println("输入的placement的状态打印完毕");

        String[] sPieces = solution.split("(?<=\\G.{4})");
//        for(int i = 0; i < sPieces.length; i++)
//            System.out.println(sPieces[i]);

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
        System.out.println("这里是推荐的下一块 "+ nextMOve);

        System.out.println("下面是棋盘上的最新状态");
        showTileStateNow();

        return nextMOve;
    }


    private void placeHintPiece(String nextMove) {
        String pieceName = nextMove.substring(0, 1);
        Integer pieceX = Integer.parseInt(nextMove.substring(1, 2));
        Integer pieceY = Integer.parseInt(nextMove.substring(2, 3));
        String pieceOri = nextMove.substring(3, 4);
        TileType pieceType = TileType.valueOf(pieceName.toUpperCase());

        String piecePath = Board.class.getResource(URI_BASE + pieceName + "-" + pieceOri + ".png").toString();

        ImageView pieceView = new ImageView(new Image(piecePath));
        pieceView.setFitWidth(pieceType.getWidth(Integer.parseInt(pieceOri)) * SQUARE_SIZE);
        pieceView.setFitHeight(pieceType.getHeight(Integer.parseInt(pieceOri)) * SQUARE_SIZE);

        pieceView.setX(PLAY_AREA_X + pieceX * SQUARE_SIZE);
        pieceView.setY(PLAY_AREA_Y + pieceY * SQUARE_SIZE);


        board.getChildren().add(pieceView);
        PauseTransition wait = new PauseTransition(Duration.seconds(5));
        wait.setOnFinished((e) -> {
            /*YOUR METHOD*/
            board.getChildren().remove(pieceView);
            wait.playFromStart();
        });
        wait.play();

//        try {
//            hint.getChildren().add(pieceView);
//
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            hint.getChildren().clear();
//        }
//        finally {
//            hint.getChildren().remove(pieceView);
//        }
    }


    private void getHints() {
        String solution = FocusGame.getSolution(challengeString);

        Button button = new Button("Hints");
        button.setLayoutX(BOARD_X + 300);
        button.setLayoutY(GAME_HEIGHT - 55);
        button.setOnAction(event -> {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!");

            String placement = stateToPlacement();
            String nextMove = findNextMove(placement, solution);

            System.out.println(nextMove);

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
     */
    private void makeControls() {
        Button button = new Button("Restart");
        button.setLayoutX(BOARD_X + 240);
        button.setLayoutY(GAME_HEIGHT - 55);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                newGame();
                challenge.getChildren().clear();
                getChallenge();

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
        baseboard.setLayoutX(BOARD_X);
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
            //TODO change the encode tpye

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
//            String sol = FocusGame.getSolution("");
//            if (sol!=null)
//                makeSolution(sol);
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
        root.getChildren().add(hint);


        // TODO set handlers, sound, board, tiles

        setUpHandlers(scene);
//        popUpWindow();
//        setUpSoundLoop();
        showBoard();
        makeControls();
        makeCompletion();
        getHints();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
