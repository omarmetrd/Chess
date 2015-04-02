package com.chess.pieces;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.chess.enums.PieceColor;

/**
 * Pawn class to represent the pawn chess piece and its properties
 *  
 * Date: 1/29/2015
 * @author Omar Bonilla
 *
 */
public class Pawn extends ChessPiece {

	private boolean isFirstMove = true; //Records when the pawn has made its first move
	private boolean enPassant = false; //Records if the pawn is able to perform an En Passant attack
	
	/**
	 * Constructor for Pawn objects to set image, color, and starting locations
	 * 
	 * @param color Color of the Pawn piece
	 * @param x The starting x-coordinate location
	 * @param y The starting y-coordinate location
	 */
	public Pawn(PieceColor color, int x, int y) {

		super(color, x, y);
		
		switch(color) {
			case BLACK:
				try {
					this.image = ImageIO.read(getClass().getResource("/resources/chess_blackPawn.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case WHITE:
				try {
					this.image = ImageIO.read(getClass().getResource("/resources/chess_whitePawn.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
	}

	/**
	 * @return Whether the pawn has made any moves or not
	 */
	public boolean hasNotMadeFirstMove() {
		return this.isFirstMove;
	}
	
	/**
	 * @return Whether the pawn is able to perform an En Passant attack
	 */
	public boolean isPawnEnPassant() {
		return this.enPassant;
	}
	
	/**
	 * Method call for enabling a pawn to perform an En Passant attack
	 */
	public void makeValidForEnPassant() {
		this.enPassant = true;
	}
	
	/**
	 * Method call for removing the ability to perform an En Passant attack
	 */
	public void removeEnPassant() {
		this.enPassant = false;
	}
	
	@Override
	public void move(int x, int y) {
		super.move(x, y);
		
		if(this.isFirstMove) {
			this.isFirstMove = false;
		}
	}
	
	@Override
	public ArrayList<int[]> getPossibleDirections(){
		ArrayList<int[]> possibleDirections = new ArrayList<int[]>();
		int xCoordinate = 1;
		
		//Determine which direction the pawn is facing
		if(this.isWhite) {
			xCoordinate = -1;
		}
		
		//Add moving two spaces ahead direction if pawn is making its first move
		if(this.isFirstMove) {
			int[] direction = {2 * xCoordinate, 0};
			possibleDirections.add(direction);
		}
		//Add the remaining possible directions
		int[] diagonalDirection1 = {xCoordinate, 1};
		possibleDirections.add(diagonalDirection1);
		int[] diagonalDirection2 = {xCoordinate, -1};
		possibleDirections.add(diagonalDirection2);
		int[] verticalDirection = {xCoordinate, 0};
		possibleDirections.add(verticalDirection);
		
		return possibleDirections;
	}
}
