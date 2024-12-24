package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Board extends JPanel {
    public boolean isWhiteTurn = true;

    public int titleSize = 95;

    int cols = 8;
    int rows = 8;

    ArrayList<Piece> pieceList = new ArrayList<Piece>();

    ArrayList<Piece> capturedWhitePieces = new ArrayList<>();
    ArrayList<Piece> capturedBlackPieces = new ArrayList<>();

    public Piece selectedPiece;

    Input input = new Input(this);

    private boolean isCheck = false;
    private boolean isCheckmate = false;
    private JLabel statusLabel;
    private JButton quitButton;
    private JButton replayButton;

    public Board() {
        this.setPreferredSize(new Dimension(cols * titleSize, rows * titleSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        loadPosition();

        // Initialize UI elements
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 30));
        statusLabel.setForeground(Color.RED);
        this.add(statusLabel, BorderLayout.CENTER);       // It didn't work as i expected because in main I used GridBagLayout() not BorderLayout

        JPanel buttonPanel = new JPanel();
        quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(quitButton);

        replayButton = new JButton("Replay");
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        buttonPanel.add(replayButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }



    public void resetGame() {
        pieceList.clear();
        capturedWhitePieces.clear();
        capturedBlackPieces.clear();
        isWhiteTurn = true;
        isCheck = false;
        isCheckmate = false;
        statusLabel.setText("");
        loadPosition();
        repaint();
    }

    public Piece getPiece(int col, int row) {
        for (Piece piece : pieceList) {
            if (piece.col == col && piece.row == row)  // WHEN THE MOVEMENT ROW AND COL MEET A PIECE THEN IT ADD IT TO PIECE LIST
                return piece;
        }
        return null;
    }

    public void makeMove(Move move) {
        if (isCheckmate) {
            return; // Prevent further moves if the game is over
        }

        // Update the piece's position
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * titleSize;
        move.piece.yPos = move.newRow * titleSize;

        // Switch turns
        isWhiteTurn = !isWhiteTurn;                 // if I do false it will always stay false.
        // Capture the piece if there is one
        capture(move);
        // Check for check and checkmate
        checkForCheck();
        if (isCheck) {
            if (isCheckmate()) {
                isCheckmate = true;
                statusLabel.setText("CHECKMATE - " + (isWhiteTurn ? "Black" : "White") + " wins!");
            } else {
                statusLabel.setText("CHECK");
            }
        } else {
            statusLabel.setText("");
        }

        repaint(); // Ensure the board is repainted
    }


    public void capture(Move move) {
        if (move.capture != null) {
            if (move.capture.isWhite) {
                capturedWhitePieces.add(move.capture);
            } else {
                capturedBlackPieces.add(move.capture);
            }
            pieceList.remove(move.capture);

            // Check if the captured piece is a King
            if (move.capture.name.equals("King")) {
                // Declare the winner
                String winner = move.capture.isWhite ? "Black" : "White";
                isCheckmate = true; // Set the game-over state
                System.out.println("CHECKMATE - " + winner + " wins!");
                //statusLabel.setText("CHECKMATE - " + winner + " wins!");
                System.exit(0);
            }
        }
    }





    public boolean isValidMove(Move move) {

        if (sameTeam(move.piece, move.capture)) {
            return false;
        }

        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }

        // to show green available moves
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }

        // Check if the move is valid for the current turn
        if ((move.piece.isWhite && !isWhiteTurn) || (!move.piece.isWhite && isWhiteTurn)) {
            return false;
        }

        return true;
    }

    public boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public void loadPosition() {
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));

        pieceList.add(new Pawn(this, 0, 1, false));
        pieceList.add(new Pawn(this, 1, 1, false));
        pieceList.add(new Pawn(this, 2, 1, false));
        pieceList.add(new Pawn(this, 3, 1, false));
        pieceList.add(new Pawn(this, 4, 1, false));
        pieceList.add(new Pawn(this, 5, 1, false));
        pieceList.add(new Pawn(this, 6, 1, false));
        pieceList.add(new Pawn(this, 7, 1, false));

        pieceList.add(new Pawn(this, 0, 6, true));
        pieceList.add(new Pawn(this, 1, 6, true));
        pieceList.add(new Pawn(this, 2, 6, true));
        pieceList.add(new Pawn(this, 3, 6, true));
        pieceList.add(new Pawn(this, 4, 6, true));
        pieceList.add(new Pawn(this, 5, 6, true));
        pieceList.add(new Pawn(this, 6, 6, true));
        pieceList.add(new Pawn(this, 7, 6, true));

        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Paint the board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((c + r) % 2 == 0) {
                    g2d.setColor(new Color(150, 77, 34));
                } else {
                    g2d.setColor(new Color(238, 220, 151));
                }
                g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
            }
        }

        // Paint highlights
        if (selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
                    }
                }
            }
        }

        // Paint pieces
        for (Piece piece : pieceList) {
            piece.paint(g2d);
        }
    }

    private void checkForCheck() {
        // It checks if the current player's King is in check
        Piece king = findKing(isWhiteTurn);
        if (king != null) {
            for (Piece piece : pieceList) {
                //If isWhiteTurn == true the opponent's pieces will have piece.isWhite == false.
                //This checks if the piece can legally move to the King's position.
                if (piece.isWhite != isWhiteTurn && piece.isValidMovement(king.col, king.row)) {
                    isCheck = true;
                    return;
                }
            }
        }
        isCheck = false;
    }

    private boolean isCheckmate() {
        if (!isCheck) {return false;}

        // Check if the King can move out of check
        Piece king = findKing(isWhiteTurn);
        if (king != null) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    int newCol = king.col + i;
                    int newRow = king.row + j;
                    if (newCol >= 0 && newCol < 8 && newRow >= 0 && newRow < 8) {
                        if (isValidMove(new Move(this, king, newCol, newRow))) {
                            return false;
                        }
                    }
                }
            }
        }

        // Check if any other piece can block the check
        for (Piece piece : pieceList) {
            if (piece.isWhite == isWhiteTurn) {
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        if (isValidMove(new Move(this, piece, c, r))) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private Piece findKing(boolean isWhite) {
        for (Piece piece : pieceList) {
            if (piece.isWhite == isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        return null;
    }
}
