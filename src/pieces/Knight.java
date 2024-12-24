package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Knight extends Piece{
    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "Knight";

        this.sprite = sheet.getSubimage(3*sheetScale,isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);


    }
    public boolean isValidMovement(int col, int row) {
        return Math.abs(col - this.col) * Math.abs(row - this.row) ==2;
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