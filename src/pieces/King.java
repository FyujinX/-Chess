package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class King extends Piece {
    public King(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "King";

        this.sprite = sheet.getSubimage(0 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        // The King can move one square in any direction
        return Math.abs(this.col - col) <= 1 && Math.abs(this.row - row) <= 1;
    }
    public boolean moveCollidesWithPiece(int col, int row) {
        // Check if the target square is occupied by a piece of the same color (friendly piece)
        Piece targetPiece = board.getPiece(col, row);
        if (targetPiece != null && targetPiece.isWhite == this.isWhite) {
            return true;  // Collides with a friendly piece
        }
        return false;  // No collision
    }
}