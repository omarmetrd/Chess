package com.chess.pieces;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.chess.enums.PieceColor;

/**
 * Rook class to represent the rook chess piece and its properties
 *  
 * Date: 1/29/2015
 * @author Omar Bonilla
 * 
 */
public class Rook extends ChessPiece {

	private boolean isFirstMove = true; //Records when the rook has made its first move
	
	/**
	 * Constructor for Rook objects to set image, color, possible movement directions, and starting locations
	 * 
	 * @param color Color of the rook piece
	 * @param x The starting x-coordinate location
	 * @param y The starting y-coordinate location
	 */
	public Rook(PieceColor color, int x, int y) {
		
		super(color, x, y);
		
		switch(color) {
		
		case BLACK:
			try {
				this.image = ImageIO.read(getClass().getResource("/resources/chess_blackRook.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case WHITE:
			try {
				this.image = ImageIO.read(getClass().getResource("/resources/chess_whiteRook.png"));
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
		}
	}

	/**
	 * @return Whether the rook has made any moves or not
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
