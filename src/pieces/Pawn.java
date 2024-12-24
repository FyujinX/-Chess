package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Pawn extends Piece {
    public boolean justMovedTwoSquares = false; // Track if pawn moved two squares

    public Pawn(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "Pawn";

        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;

        // Moving forward one square
        if (this.col == col && this.row + direction == row && board.getPiece(col, row) == null) {
            return !moveCollidesWithPiece(col, row);
        }

        // Moving forward two squares from the starting position
        if (this.col == col && this.row == startRow && this.row + 2 * direction == row && board.getPiece(col, row) == null) {
            return !moveCollidesWithPiece(col, row);
        }

        // Capturing diagonally
        if (Math.abs(this.col - col) == 1 && this.row + direction == row && board.getPiece(col, row) != null && board.getPiece(col, row).isWhite != this.isWhite) {
            return true; // No need to check collision for capture
        }

        // En Passant
        if (Math.abs(this.col - col) == 1 && this.row + direction == row && board.getPiece(col, row) == null) {
            Piece adjacentPiece = board.getPiece(col, this.row);
            if (adjacentPiece instanceof Pawn && adjacentPiece.isWhite != this.isWhite && ((Pawn) adjacentPiece).justMovedTwoSquares) {
                return true;
            }
        }

        return false;
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        int direction = isWhite ? -1 : 1;

        // For one-square forward movement
        if (this.col == col && this.row + direction == row) {
            return board.getPiece(col, row) != null;
        }

        // For two-square forward movement, check both squares
        if (this.col == col && Math.abs(this.row - row) == 2) {
            int intermediateRow = this.row + direction;
            return board.getPiece(col, intermediateRow) != null || board.getPiece(col, row) != null;
        }

        // No collision for diagonal captures
        return false;
    }
}
