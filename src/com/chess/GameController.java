package com.chess;

/**
 * Main class for generating and displaying the chess board GUI
 * 
 * Date: 1/29/2015
 * @author Omar Bonilla
 */
public class GameController {
	
	public static void main(String[] args) {
		
		// Create instance of the chess board
		ChessBoard board = new ChessBoard();
		board.setVisible(true);
	}
}
