package com.chess.pieces;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.chess.enums.PieceColor;

/**
 * Bishop class to represent the bishop chess piece and its properties
 *  
 * Date: 1/29/2015
 * @author Omar Bonilla
 *
 */
public class Bishop extends ChessPiece {

	/**
	 * Constructor for Bishop objects to set image, color, possible movement directions, and starting locations
	 * 
	 * @param color Color of the bishop piece
	 * @param x The starting x-coordinate location
	 * @param y The starting y-coordinate location
	 */
	public Bishop(PieceColor color, int x, int y) {
		
		super(color, x, y);
		
		switch(color) {
		
		case BLACK:
			try {
				this.image = ImageIO.read(getClass().getResource("/resources/chess_blackBishop.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case WHITE:
			try {
				this.image = ImageIO.read(getClass().getResource("/resources/chess_whiteBishop.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		
		for(int i = 1; i < 8; i++) {
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
