package com.chess.pieces;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.chess.enums.PieceColor;

/**
 * Queen class to represent the queen chess piece and its properties
 *  
 * Date: 1/29/2015
 * @author Omar Bonilla
 *
 */
public class Queen extends ChessPiece {

	/**
	 * Constructor for Queen objects to set image, color, possible movement directions, and starting locations
	 * 
	 * @param color Color of the queen piece
	 * @param x The starting x-coordinate location
	 * @param y The starting y-coordinate location
	 */
	public Queen(PieceColor color, int x, int y) {
		
		super(color, x, y);
		
		switch(color) {
		
		case BLACK:
			try {
				this.image = ImageIO.read(getClass().getResource("/resources/chess_blackQueen.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case WHITE:
			try {
				this.image = ImageIO.read(getClass().getResource("/resources/chess_whiteQueen.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		
		for(int i = 1; i < 8; i++) {
			int[] northCoordinate = {-i, 0};
			possibleDirections.add(northCoordinate);
			
			int[] westCoordinate = {0, -i};
			possibleDirections.add(westCoordinate);
			
			int[] eastCoordinate = {0, i};
			possibleDirections.add(eastCoordinate);
			
			int[] southCoordinate = {i, 0};
			possibleDirections.add(southCoordinate);
			
			int[] upperLeftCoordinate = {-i, -i};
			possibleDirections.add(upperLeftCoordinate);
			
			int[] upperRightCoordinate = {-i, i};
			possibleDirections.add(upperRightCoordinate);
			
			int[] lowerLeftCoordinate = {i, -i};
			possibleDirections.add(lowerLeftCoordinate);
			
			int[] lowerRightCoordinate = {i, i};
			possibleDirections.add(lowerRightCoordinate);
		}
	}
}
