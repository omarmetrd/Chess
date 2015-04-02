package com.chess.pieces;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.chess.enums.PieceColor;

/**
 * Abstract class for all chess pieces holding generic properties
 * 
 * Date: 1/29/2015
 * @author Omar Bonilla
 *
 */
public abstract class ChessPiece {

	protected int[] location = new int[2]; //Current location of the chess piece
	protected int numOfMoves; //Total number of moves made by the chess piece
	protected BufferedImage image; //Image representing the chess piece
	protected boolean isWhite; //Determines the chess piece's actual color
	protected ArrayList<int[]> possibleDirections = new ArrayList<int[]>(); //Represents the knight's possible move directions
	
	/**
	 * Constructor for ChessPiece objects
	 * 
	 * @param color Color of the chess piece
	 * @param x The starting x-coordinate location
	 * @param y The starting y-coordinate location
	 */
	protected ChessPiece(PieceColor color, int x, int y) {
		this.location[0] = x;
		this.location[1] = y;
		
		switch(color) {
		
		case BLACK:
			this.isWhite = false;
			break;
		case WHITE:
			this.isWhite = true;
			break;
		}
	}
	
	/**
	 * Method call for updating the chess piece's location and incrementing the
	 * total number of moves it made
	 * 
	 * @param x The new x-coordinate location to update
	 * @param y The new y-coordinate location to update
	 */
	public void move(int x, int y) {
		this.location[0] = x;
		this.location[1] = y;
		
		numOfMoves++;
	}
	
	/**
	 * @return The chess piece's location
	 */
	public int[] getPieceLocation() {
		return this.location;
	}
	
	/**
	 * @return The buffered image of the chess piece
	 */
	public BufferedImage getPieceImage() {
		return this.image;
	}
	
	/**
	 * @return The boolean value of whether the piece is white or black
	 */
	public boolean isPieceWhite() {
		return this.isWhite;
	}
	
	/**
	 * @return The possible directions that can be made by the
	 * chess piece
	 */
	public ArrayList<int[]> getPossibleDirections() {
		return this.possibleDirections;
	};
}
