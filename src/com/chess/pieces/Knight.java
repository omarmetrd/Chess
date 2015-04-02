package com.chess.pieces;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.chess.enums.PieceColor;

/**
 * Knight class to represent the knight chess piece and its properties
 *  
 * Date: 1/29/2015
 * @author Omar Bonilla
 *
 */
public class Knight extends ChessPiece {
	
	/**
	 * Constructor for Knight objects to set image, color, possible movement directions, and starting locations
	 * 
	 * @param color Color of the knight piece
	 * @param x The starting x-coordinate location
	 * @param y The starting y-coordinate location
	 */
	public Knight(PieceColor color, int x, int y) {
		
		super(color, x, y);
		
		switch(color) {
			case BLACK:
				try {
					this.image = ImageIO.read(getClass().getResource("/resources/chess_blackKnight.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case WHITE:
				try {
					this.image = ImageIO.read(getClass().getResource("/resources/chess_whiteKnight.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
		
		int[] knightCoordinate1 = {2, -1};
		possibleDirections.add(knightCoordinate1);
		
		int[] knightCoordinate2 = {2, 1};
		possibleDirections.add(knightCoordinate2);
		
		int[] knightCoordinate3 = {1, 2};
		possibleDirections.add(knightCoordinate3);
		
		int[] knightCoordinate4 = {1, -2};
		possibleDirections.add(knightCoordinate4);
		
		int[] knightCoordinate5 = {-1, -2};
		possibleDirections.add(knightCoordinate5);
		
		int[] knightCoordinate6 = {-1 , 2};
		possibleDirections.add(knightCoordinate6);
		
		int[] knightCoordinate7 = {-2, -1};
		possibleDirections.add(knightCoordinate7);
		
		int[] knightCoordinate8 = {-2, 1};
		possibleDirections.add(knightCoordinate8);
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		
	}
}
