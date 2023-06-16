package com.example.checkersg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CheckersGame extends Application {
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 80;

    private static final Color DARK_COLOR = Color.rgb(128, 64, 0);
    private static final Color LIGHT_COLOR = Color.rgb(255, 192, 128);
    private static final Color HIGHLIGHT_COLOR = Color.rgb(0, 255, 0, 0.5);
    private static final Color PIECE_COLOR = Color.BLACK;

    private Group board;
    private Rectangle[][] squares;
    private Piece[][] pieces;
    private Rectangle[][] highlights;

    private Piece selectedPiece;
    private boolean whiteTurn;
    private boolean gameOver;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkers");

        board = new Group();
        squares = new Rectangle[BOARD_SIZE][BOARD_SIZE];
        pieces = new Piece[BOARD_SIZE][BOARD_SIZE];
        highlights = new Rectangle[BOARD_SIZE][BOARD_SIZE];

        // CREATING THE BOARD
        for (int col = 0; col < BOARD_SIZE; col++) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setX(col * SQUARE_SIZE);
                square.setY(row * SQUARE_SIZE);
                square.setFill((col + row) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);

                squares[col][row] = square;
                board.getChildren().add(square);

                // INITIAL POSITIONS OF PIECES
                if ((col + row) % 2 != 0 && row < 3) {
                    Piece piece = new Piece(col * SQUARE_SIZE + SQUARE_SIZE / 2, row * SQUARE_SIZE + SQUARE_SIZE / 2, true);
                    pieces[col][row] = piece;
                    board.getChildren().add(piece);
                } else if ((col + row) % 2 != 0 && row > 4) {
                    Piece piece = new Piece(col * SQUARE_SIZE + SQUARE_SIZE / 2, row * SQUARE_SIZE + SQUARE_SIZE / 2, false);
                    pieces[col][row] = piece;
                    board.getChildren().add(piece);
                }
            }
        }

        // CLICKING ACTIONS ON PIECES
        board.setOnMouseClicked(e -> handleBoardClick(e.getX(), e.getY()));

        Scene scene = new Scene(board, BOARD_SIZE * SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // STARTING SETTINGS
        selectedPiece = null;
        whiteTurn = true;
        gameOver = false;
    }

    private void handleBoardClick(double x, double y) {
        if (gameOver) {
            return;
        }

        int col = (int) (x / SQUARE_SIZE);
        int row = (int) (y / SQUARE_SIZE);

        if (selectedPiece == null) {
            Piece piece = pieces[col][row];
            if (piece != null && piece.isWhite() == whiteTurn) {
                selectedPiece = piece;
                showValidMoves(col, row);
            }
        } else {
            if (isValidMove(col, row)) {
                movePiece(col, row);
                clearHighlights();
                selectedPiece = null;
                whiteTurn = !whiteTurn;
            }
        }
    }

    private void showValidMoves(int col, int row) {
        clearHighlights();

        // Mark valid moves
        // Burada geçerli hamleleri işaretleme işlemini yapmanız gerekmektedir

        // Örnek olarak, seçili taşın hemen yanındaki boş kareleri işaretleyelim
        if (col > 0 && row > 0 && pieces[col - 1][row - 1] == null) {
            Rectangle highlight = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
            highlight.setX((col - 1) * SQUARE_SIZE);
            highlight.setY((row - 1) * SQUARE_SIZE);
            highlight.setFill(HIGHLIGHT_COLOR);
            highlights[col - 1][row - 1] = highlight;
            board.getChildren().add(highlight);
        }

        if (col < BOARD_SIZE - 1 && row > 0 && pieces[col + 1][row - 1] == null) {
            Rectangle highlight = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
            highlight.setX((col + 1) * SQUARE_SIZE);
            highlight.setY((row - 1) * SQUARE_SIZE);
            highlight.setFill(HIGHLIGHT_COLOR);
            highlights[col + 1][row - 1] = highlight;
            board.getChildren().add(highlight);
        }
    }

    private boolean isValidMove(int col, int row) {
        // Check if the move is valid
        // Burada geçerli hamle kontrolünü yapmanız gerekmektedir

        // Örnek olarak, seçili taşın yalnızca boş kareye hareket etmesine izin verelim
        return pieces[col][row] == null;
    }

    private void movePiece(int col, int row) {
        int selectedCol = (int) (selectedPiece.getCenterX() / SQUARE_SIZE);
        int selectedRow = (int) (selectedPiece.getCenterY() / SQUARE_SIZE);

        pieces[selectedCol][selectedRow] = null;
        pieces[col][row] = selectedPiece;
        selectedPiece.setCenterX(col * SQUARE_SIZE + SQUARE_SIZE / 2);
        selectedPiece.setCenterY(row * SQUARE_SIZE + SQUARE_SIZE / 2);

        // Reorder the piece on the board
        board.getChildren().remove(selectedPiece);
        board.getChildren().add(selectedPiece);

        if (row == 0 && !selectedPiece.isWhite()) {
            selectedPiece.makeKing();
        } else if (row == BOARD_SIZE - 1 && selectedPiece.isWhite()) {
            selectedPiece.makeKing();
        }
    }

    private void clearHighlights() {
        for (int col = 0; col < BOARD_SIZE; col++) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                Rectangle highlight = highlights[col][row];
                if (highlight != null) {
                    board.getChildren().remove(highlight);
                    highlights[col][row] = null;
                }
            }
        }
    }

    public static class Piece extends Circle {
        private boolean isWhite;
        private boolean isKing;

        public Piece(double centerX, double centerY, boolean isWhite) {
            super(centerX, centerY, SQUARE_SIZE / 2 - 10);
            this.isWhite = isWhite;
            this.isKing = false;
            setFill(isWhite ? Color.WHITE : PIECE_COLOR);
        }

        public boolean isWhite() {
            return isWhite;
        }

        public boolean isKing() {
            return isKing;
        }

        public void makeKing() {
            isKing = true;
            setRadius(SQUARE_SIZE / 2 - 5);
            setStroke(isWhite ? Color.WHITE : PIECE_COLOR);
            setStrokeWidth(5);
        }
    }
}
