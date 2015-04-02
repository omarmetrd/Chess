package com.chess.pieces;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.chess.enums.PieceColor;

/**
 * King class to represent the king chess piece and its properties
 *  
 * Date: 1/29/2015
 * @author Omar Bonilla
 *
 */
public class King extends ChessPiece {

	private boolean isFirstMove = true; //Records when the king has made its first move
	
	/**
	 * Constructor for King objects to set image, color, possible movement directions, and starting locations
	 * 
	 * @param color Color of the king piece
	 * @param x The starting x-coordinate location
	 * @param y The starting y-coordinate location
	 */
	public King(PieceColor color, int x, int y) {
	
		super(color, x, y);
		
		switch(color) {
		
		case BLACK:
			try {
				this.image = ImageIO.read(getClass().getResource("/resources/chess_blackKing.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case WHITE:
			try {
				this.image = ImageIO.read(getClass().getResource("/resources/chess_whiteKing.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		
		int[] northCoordinate = {-1, 0};
		possibleDirections.add(northCoordinate);
		
		int[] westCoordinate = {0, -1};
		possibleDirections.add(westCoordinate);
		
		int[] eastCoordinate = {0, 1};
		possibleDirections.add(eastCoordinate);
		
		int[] southCoordinate = {1, 0};
		possibleDirections.add(southCoordinate);
		
		int[] upperLeftCoordinate = {-1, -1};
		possibleDirections.add(upperLeftCoordinate);
		
		int[] upperRightCoordinate = {-1, 1};
		possibleDirections.add(upperRightCoordinate);
		
		int[] lowerLeftCoordinate = {1, -1};
		possibleDirections.add(lowerLeftCoordinate);
		
		int[] lowerRightCoordinate = {1, 1};
		possibleDirections.add(lowerRightCoordinate);
	}

	/**
	 * @return Whether the king has made any moves or not
	 */
	public boolean hasNotMadeFirstMove() {
		return this.isFirstMove;
	}
	
	@Override
	public void move(int x, int y) {
		super.move(x, y);
		
		if(this.isFirstMove) {
			this.isFirstMove = false;
		}	
	}
}
