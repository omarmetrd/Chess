package com.chess;

import java.util.ArrayList;

import com.chess.enums.PieceColor;
import com.chess.pieces.Bishop;
import com.chess.pieces.ChessPiece;
import com.chess.pieces.King;
import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Queen;
import com.chess.pieces.Rook;

/**
 * Helper class for performing various checks for legal piece movements
 * 
 * Date: 1/29/2015
 * @author Omar Bonilla
 *
 */
public class VerificationHelper {

	/**
	 * Verifies if the two chess pieces are of a different color  
	 * 
	 * @param firstPiece	The first chess piece to compare
	 * @param secondPiece	The second chess piece to compare
	 * @return 				Boolean value representing whether the two pieces are of the same color
	 */
	public static boolean arePieceColorsDifferent(ChessPiece firstPiece, ChessPiece secondPiece) {
		return (secondPiece.isPieceWhite() && !(firstPiece.isPieceWhite())) || (!(secondPiece.isPieceWhite()) && firstPiece.isPieceWhite());
	}
	
	/**
	 * Simple check for target position's legality, while also checking if 
	 * the position doesn't cause the player's own king to be in check
	 * 
	 * @param xCoordinate 		The x-coordinate of the target location
	 * @param yCoordinate 		The y-coordinate of the target location
	 * @param piece				Chess piece making the movement
	 * @param referenceGrid		Reference of all the chess pieces on the board
	 * @param positionsFound	Array of all legal positions found for the selected chess piece
	 * @return					Boolean value of whether the passed coordinates created a legal position to move to
	 */
	private static boolean checkAndAddLegalMove(int xCoordinate, int yCoordinate, ChessPiece piece, 
			ChessPiece[][] referenceGrid, ArrayList<int[]> positionsFound) {
		
		//Check if target location is within range of the chess board
		if(xCoordinate > -1 && yCoordinate > -1 && xCoordinate < 8 && yCoordinate < 8) {
			ChessPiece targetPiece = referenceGrid[xCoordinate][yCoordinate];
			
			if(targetPiece == null) {
				int[] legalCoordinates = {xCoordinate, yCoordinate};
				if(isPositionNotCausingCheck(referenceGrid, legalCoordinates, piece)) {
					positionsFound.add(legalCoordinates); //Add legal positions to the array
					return true;
				}
			}
			else if(arePieceColorsDifferent(piece, targetPiece)) {
				int[] legalCoordinates = {xCoordinate, yCoordinate};
				if(isPositionNotCausingCheck(referenceGrid, legalCoordinates, piece)) {
					positionsFound.add(legalCoordinates); //Add legal positions to the array
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Searches for all possible legal moves that can be made by a pawn
	 * 
	 * @param pawn 				The selected pawn for movement
	 * @param referenceGrid 	The array that references all the possible chess piece locations
	 * @param positionsFound 	The array for storing the legal positions found for the pawn
	 * @return 					The array of legal positions the pawn piece can move to
	 */
	public static ArrayList<int[]> findLegalPawnMovements(Pawn pawn, final ChessPiece[][] referenceGrid, ArrayList<int[]> positionsFound) {
		int[] currentLocation = pawn.getPieceLocation();
		ArrayList<int[]> possibleDirections = pawn.getPossibleDirections();
		ChessPiece targetPiece;
		
		for(int[] direction : possibleDirections) {
			
			int targetXCoordinate = currentLocation[0] + direction[0];
			int targetYCoordinate = currentLocation[1] + direction[1];
			
			//Check if two spaces in front of the pawn is valid
			if(Math.abs(direction[0]) == 2) {
				//Check if no pieces are located one and two spaces in front
				if(referenceGrid[currentLocation[0] + (direction[0] / 2)][currentLocation[1]] == null 
						&& referenceGrid[currentLocation[0] + direction[0]][currentLocation[1]] == null)
					checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, pawn, referenceGrid, positionsFound);
			}
			//Check if target location is within range of the chess board
			else if(targetXCoordinate > -1 && targetYCoordinate > -1 && targetXCoordinate < 8 && targetYCoordinate < 8) {
				//Check if direction is going diagonally
				if(Math.abs(direction[1]) == 1) {
					//Check if there is a piece diagonal to the pawn
					if(referenceGrid[targetXCoordinate][targetYCoordinate] != null)
						checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, pawn, referenceGrid, positionsFound);
					
					//Check if a move for En Passant is possible
					else if(referenceGrid[currentLocation[0]][targetYCoordinate] != null) {	
						//Get the chess piece next to the pawn
						targetPiece = referenceGrid[currentLocation[0]][targetYCoordinate];
						
						//Verify that chess piece is a pawn and of a different color
						if(targetPiece instanceof Pawn && arePieceColorsDifferent(pawn, targetPiece)) {
							if(((Pawn)targetPiece).isPawnEnPassant())
								checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, pawn, referenceGrid, positionsFound);
						}
					}
				}
				//Check if space is empty
				else if(referenceGrid[targetXCoordinate][targetYCoordinate] == null)
					checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, pawn, referenceGrid, positionsFound);
			}
		}
		return positionsFound;
	}
	
	/**
	 * Searches for all possible legal moves that can be made by a knight
	 * 
	 * @param knight 			The selected knight for movement
	 * @param referenceGrid 	The array that references all the possible chess piece locations
	 * @param positionsFound 	The array for storing the legal positions found for the knight
	 * @return 					The array of legal positions the knight piece can move to
	 */
	public static ArrayList<int[]> findLegalKnightMovements(Knight knight, ChessPiece[][] referenceGrid, ArrayList<int[]> positionsFound) {
		int[] currentLocation = knight.getPieceLocation();
		ArrayList<int[]> possibleDirections = knight.getPossibleDirections();
		
		//Validate that knight coordinates are legal moves on the board
		for(int[] direction : possibleDirections) {
			
			int targetXCoordinate = currentLocation[0] + direction[0];
			int targetYCoordinate = currentLocation[1] + direction[1];
			
			checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, knight, referenceGrid, positionsFound);
		}
		return positionsFound;
	}
	
	/**
	 * Searches for all possible legal moves that can be made by a bishop
	 * 
	 * @param bishop 			The selected bishop for movement
	 * @param referenceGrid 	The array that references all the possible chess piece locations
	 * @param positionsFound 	The array for storing the legal positions found for the bishop
	 * @return 					The array of legal positions the bishop piece can move to
	 */
	public static ArrayList<int[]> findLegalBishopMovements(Bishop bishop, ChessPiece[][] referenceGrid, ArrayList<int[]> positionsFound) {
		int[] currentLocation = bishop.getPieceLocation();
		ArrayList<int[]> possibleDirections = bishop.getPossibleDirections();
		boolean upperLeftPathValid = true;
		boolean upperRightPathValid = true;
		boolean lowerLeftPathValid = true;
		boolean lowerRightPathValid = true;
		
		for(int[] direction : possibleDirections) {
			int targetXCoordinate = direction[0] + currentLocation[0];
			int targetYCoordinate = direction[1] + currentLocation[1];
				
			//Check if direction is leading upper left
			if(direction[0] < 0 && direction[1] < 0 && upperLeftPathValid)
				upperLeftPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, bishop, referenceGrid, positionsFound);
			//Check if direction is leading upper right
			else if(direction[0] < 0 && direction[1] > 0 && upperRightPathValid)
				upperRightPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, bishop, referenceGrid, positionsFound);
			//Check if direction is leading lower left
			else if(direction[0] > 0 && direction[1] < 0 && lowerLeftPathValid)
				lowerLeftPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, bishop, referenceGrid, positionsFound);
			//Check if direction is leading lower right
			else if(direction[0] > 0 && direction[1] > 0 && lowerRightPathValid)
				lowerRightPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, bishop, referenceGrid, positionsFound);
		}
		return positionsFound;
	}
	
	/**
	 * Searches for all possible legal moves that can be made by a rook
	 * 
	 * @param rook 				The selected rook for movement
	 * @param referenceGrid 	The array that references all the possible chess piece locations
	 * @param positionsFound 	The array for storing the legal positions found for the rook
	 * @return 					The array of legal positions the rook piece can move to
	 */
	public static ArrayList<int[]> findLegalRookMovements(Rook rook, ChessPiece[][] referenceGrid, ArrayList<int[]> positionsFound) {
		int[] currentLocation = rook.getPieceLocation();
		ArrayList<int[]> possibleDirections = rook.getPossibleDirections();
		boolean northPathValid = true;
		boolean westPathValid = true;
		boolean eastPathValid = true;
		boolean southPastValid = true;
		
		for(int[] direction : possibleDirections) {
			int targetXCoordinate = direction[0] + currentLocation[0];
			int targetYCoordinate = direction[1] + currentLocation[1];
			
			//Check if direction is leading north
			if(direction[0] < 0 && northPathValid)
				northPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, rook, referenceGrid, positionsFound);
			//Check if direction is leading west
			else if(direction[1] < 0 && westPathValid)
				westPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, rook, referenceGrid, positionsFound);
			//Check if direction is leading east
			else if(direction[1] > 0 && eastPathValid)
				eastPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, rook, referenceGrid, positionsFound);
			//Check if direction is leading south
			else if(direction[0] > 0 && southPastValid)
				southPastValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, rook, referenceGrid, positionsFound);
		}
		return positionsFound;
	}
	
	/**
	 * Searches for all possible legal moves that can be made by a queen
	 * 
	 * @param rook 				The selected queen for movement
	 * @param referenceGrid 	The array that references all the possible chess piece locations
	 * @param positionsFound 	The array for storing the legal positions found for the queen
	 * @return 					The array of legal positions the queen piece can move to
	 */
	public static ArrayList<int[]> findLegalQueenMovements(Queen queen, ChessPiece[][] referenceGrid, ArrayList<int[]> positionsFound) {
		
		int[] currentLocation = queen.getPieceLocation();
		ArrayList<int[]> possibleDirections = queen.getPossibleDirections();
		boolean northPathValid = true;
		boolean westPathValid = true;
		boolean eastPathValid = true;
		boolean southPastValid = true;
		boolean upperLeftPathValid = true;
		boolean upperRightPathValid = true;
		boolean lowerLeftPathValid = true;
		boolean lowerRightPathValid = true;
		
		for(int[] direction : possibleDirections) {
			int targetXCoordinate = direction[0] + currentLocation[0];
			int targetYCoordinate = direction[1] + currentLocation[1];
			
			//Check if direction is leading upwards
			if(direction[0] < 0 && direction[1] == 0 && northPathValid)
				northPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, queen, referenceGrid, positionsFound);
			//Check if direction is leading upper left
			else if(direction[0] < 0 && direction[1] < 0 && upperLeftPathValid)
				upperLeftPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, queen, referenceGrid, positionsFound);
			//Check if direction is leading upper right
			else if(direction[0] < 0 && direction[1] > 0 && upperRightPathValid)
				upperRightPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, queen, referenceGrid, positionsFound);
			//Check if direction is leading west
			else if(direction[0] == 0 && direction[1] < 0 && westPathValid)
				westPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, queen, referenceGrid, positionsFound);
			//Check if direction is leading east
			else if(direction[0] == 0 && direction[1] > 0 && eastPathValid)
				eastPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, queen, referenceGrid, positionsFound);
			//Check if direction is leading downwards
			else if(direction[0] > 0 && direction[1] == 0 && southPastValid)
				southPastValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, queen, referenceGrid, positionsFound);
			//Check if direction is leading lower left
			else if(direction[0] > 0 && direction[1] < 0 && lowerLeftPathValid)
				lowerLeftPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, queen, referenceGrid, positionsFound);
			//Check if direction is leading lower right
			else if(direction[0] > 0 && direction[1] > 0 && lowerRightPathValid)
				lowerRightPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, queen, referenceGrid, positionsFound);
		}
		return positionsFound;
	}
	
	/**
	 * Searches for all possible legal moves that can be made by a king
	 * 
	 * @param piece 			The selected king for movement
	 * @param referenceGrid 	The array that references all the possible chess piece locations
	 * @param positionsFound 	The array for storing the legal positions found for the king
	 * @return 					The array of legal positions the king piece can move to
	 */
	public static ArrayList<int[]> findLegalKingMovements(King king, ChessPiece[][] referenceGrid, ArrayList<int[]> positionsFound) {
		int[] currentLocation = king.getPieceLocation();
		ArrayList<int[]> possibleDirections = king.getPossibleDirections();
		boolean northPathValid = true;
		boolean westPathValid = true;
		boolean eastPathValid = true;
		boolean southPastValid = true;
		boolean upperLeftPathValid = true;
		boolean upperRightPathValid = true;
		boolean lowerLeftPathValid = true;
		boolean lowerRightPathValid = true;
		
		for(int[] direction : possibleDirections) {
			int targetXCoordinate = direction[0] + currentLocation[0];
			int targetYCoordinate = direction[1] + currentLocation[1];
			
			//Check if direction is leading upwards
			if(direction[0] < 0 && direction[1] == 0 && northPathValid)
				northPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, king, referenceGrid, positionsFound);
			//Check if direction is leading upper left
			else if(direction[0] < 0 && direction[1] < 0 && upperLeftPathValid)
				upperLeftPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, king, referenceGrid, positionsFound);
			//Check if direction is leading upper right
			else if(direction[0] < 0 && direction[1] > 0 && upperRightPathValid)
				upperRightPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, king, referenceGrid, positionsFound);
			//Check if direction is leading west
			else if(direction[0] == 0 && direction[1] < 0 && westPathValid)
				westPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, king, referenceGrid, positionsFound);
			//Check if direction is leading east
			else if(direction[0] == 0 && direction[1] > 0 && eastPathValid)
				eastPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, king, referenceGrid, positionsFound);
			//Check if direction is leading downwards
			else if(direction[0] > 0 && direction[1] == 0 && southPastValid)
				southPastValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, king, referenceGrid, positionsFound);
			//Check if direction is leading lower left
			else if(direction[0] > 0 && direction[1] < 0 && lowerLeftPathValid)
				lowerLeftPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, king, referenceGrid, positionsFound);
			//Check if direction is leading lower right
			else if(direction[0] > 0 && direction[1] > 0 && lowerRightPathValid)
				lowerRightPathValid = checkAndAddLegalMove(targetXCoordinate, targetYCoordinate, king, referenceGrid, positionsFound);
		}
		
		////////////////////////
		// Check for Castling //
		////////////////////////
		
		if(king.hasNotMadeFirstMove()) {
			
			//Check if King can start castling to the west
			int xCoordinate = currentLocation[0];
			int yCoordinate = currentLocation[1] - 4;
			
			ChessPiece possibleRook = referenceGrid[xCoordinate][yCoordinate];
			
			if(possibleRook != null) {
				if(possibleRook instanceof Rook) {
					if(((Rook) possibleRook).hasNotMadeFirstMove() && !(arePieceColorsDifferent(king, possibleRook)) 
							&& referenceGrid[xCoordinate][yCoordinate + 3] == null 
							&& referenceGrid[xCoordinate][yCoordinate + 2] == null 
							&& referenceGrid[xCoordinate][yCoordinate + 1] == null) {
						
						int[] legalCoordinates = {xCoordinate, yCoordinate + 2};
						if(isPositionNotCausingCheck(referenceGrid, legalCoordinates, king))
							positionsFound.add(legalCoordinates);
					}
				}
			}
			//Check if King can start castling to the east
			xCoordinate = currentLocation[0];
			yCoordinate = currentLocation[1] + 3;
			
			possibleRook = referenceGrid[xCoordinate][yCoordinate];
			
			if(possibleRook != null) {
				if(possibleRook instanceof Rook) {
					if(((Rook) possibleRook).hasNotMadeFirstMove() && !(arePieceColorsDifferent(king, possibleRook))
							&& referenceGrid[xCoordinate][yCoordinate - 1] == null 
							&& referenceGrid[xCoordinate][yCoordinate - 2] == null) {
						int[] legalCoordinates = {xCoordinate, yCoordinate - 1};
						if(isPositionNotCausingCheck(referenceGrid, legalCoordinates, king))
							positionsFound.add(legalCoordinates);
					}
				}
			}
		}
		return positionsFound;
	}
	
	/**
	 * Looks for any checks made by a chess piece across the board
	 * 
	 * @param referenceGrid 	The array that references all the possible chess piece locations
	 * @param playerTurn 		Represents the player's turn
	 * @return 					Whether a check has been found anywhere on the board
	 */
	public static boolean checkForCheckmate(ChessPiece[][] referenceGrid, PieceColor playerTurn) {
		
		ArrayList<int[]> positionsFound = new ArrayList<int[]>();
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				ChessPiece possiblePiece = referenceGrid[i][j];
				
				if(possiblePiece != null) {
					if((possiblePiece.isPieceWhite() && playerTurn == PieceColor.WHITE) || (!possiblePiece.isPieceWhite() && playerTurn == PieceColor.BLACK)) {
						if(possiblePiece instanceof Pawn)
							findLegalPawnMovements((Pawn)possiblePiece, referenceGrid, positionsFound);
						else if(possiblePiece instanceof Knight)
							findLegalKnightMovements((Knight)possiblePiece, referenceGrid, positionsFound);
						else if(possiblePiece instanceof Bishop)
							findLegalBishopMovements((Bishop)possiblePiece, referenceGrid, positionsFound);
						else if(possiblePiece instanceof Rook)
							findLegalRookMovements((Rook)possiblePiece, referenceGrid, positionsFound);
						else if(possiblePiece instanceof Queen)
							findLegalQueenMovements((Queen)possiblePiece, referenceGrid, positionsFound);
						else if(possiblePiece instanceof King)
							findLegalKingMovements((King)possiblePiece, referenceGrid, positionsFound);
					}
				}
			}
		}
		if(positionsFound.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see a king is in check
	 * 
	 * @param referenceGrid 	The reference to all the chess piece on the board
	 * @param playerTurn 		Represents the player's turn
	 * @return 					Whether a king is in check
	 */
	public static boolean isKingInCheck(ChessPiece[][] referenceGrid, PieceColor playerTurn) {
		return !isPositionNotCausingCheck(referenceGrid, null, new Pawn(playerTurn, 0, 0));
	}
	
	/**
	 * Verifies that a position is not causing the player's own king into check
	 * 
	 * @param referenceGrid		The reference to all the chess piece on the board
	 * @param positionToTest	The position containing the coordinates to test
	 * @param pieceReference	The chess piece used for testing the movement position
	 * @return					Whether the tested position is causing the player's own king to be in check
	 */
	private static boolean isPositionNotCausingCheck(ChessPiece[][] referenceGrid, int[] positionToTest, ChessPiece pieceReference) {
		
		King kingToCheckForCheck;
		ChessPiece[][] referenceGridCopy = new ChessPiece[8][8];
		
		//Copy the reference grid array to a copy
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				referenceGridCopy[i][j] = referenceGrid[i][j];
			}
		}
		
		if(positionToTest != null) {
			//Move the chess piece to the position to test and remove from previous location
			int[] pieceLocation = pieceReference.getPieceLocation();
			
			referenceGridCopy[positionToTest[0]][positionToTest[1]] = pieceReference;
			referenceGridCopy[pieceLocation[0]][pieceLocation[1]] = null;
		}
		
		//Get the current player's King
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				ChessPiece possiblePiece = referenceGridCopy[i][j];
				if(possiblePiece != null) {
					if(!arePieceColorsDifferent(possiblePiece, pieceReference) && possiblePiece instanceof King) {
						kingToCheckForCheck = (King)possiblePiece;
						ChessPiece possibleAttacker;
						
						int xCoordinate;
						int yCoordinate;
						
						/////////////////////////////////////////////////////////////////
						//Check to see if any enemy pieces are able to capture the King//
						/////////////////////////////////////////////////////////////////
						
						int[] kingLocation = {i, j};
						
						////////////////////////////////////
						// Look for possible pawn attacks //
						////////////////////////////////////
						if(kingToCheckForCheck.isPieceWhite()) {
							if(i > 0 && j < 7) {
								possibleAttacker = referenceGridCopy[kingLocation[0] - 1][kingLocation[1] + 1];
								if(possibleAttacker != null) {
									if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Pawn) {
										return false;
									}
								}
							}
							else if(i > 0 && j > 0) {
								possibleAttacker = referenceGridCopy[kingLocation[0] - 1][kingLocation[1] - 1];
								if(possibleAttacker != null) {
									if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Pawn) {
										return false;
									}
								}
							}
						}
						else if(!kingToCheckForCheck.isPieceWhite()) {
							if(i < 7 && j < 7) {
								possibleAttacker = referenceGridCopy[kingLocation[0] + 1][kingLocation[1] + 1];
								if(possibleAttacker != null) {
									if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Pawn) {
										return false;
									}
								}
							}
							else if(i < 7 && j > 0) {
								possibleAttacker = referenceGridCopy[kingLocation[0] + 1][kingLocation[1] - 1];
								if(possibleAttacker != null) {
									if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Pawn) {
										return false;
									}
								}
							}
						}
						
						//////////////////////////////////////
						// Look for possible knight attacks //
						//////////////////////////////////////
						xCoordinate = kingLocation[0] + 2;
						yCoordinate = kingLocation[1] - 1;
						
						if(xCoordinate < 8 && yCoordinate > 0) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Knight) {
									return false;
								}
							}
						}
						
						xCoordinate = kingLocation[0] + 2;
						yCoordinate = kingLocation[1] + 1;
						
						if(xCoordinate < 8 && yCoordinate < 8) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Knight) {
									return false;
								}
							}
						}
						
						xCoordinate = kingLocation[0] + 1;
						yCoordinate = kingLocation[1] + 2;
						
						if(xCoordinate < 8 && yCoordinate < 8) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Knight) {
									return false;
								}
							}
						}
						
						xCoordinate = kingLocation[0] + 1;
						yCoordinate = kingLocation[1] - 2;
						
						if(xCoordinate < 8 && yCoordinate > 0) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Knight) {
									return false;
								}
							}
						}
						
						xCoordinate = kingLocation[0] - 1;
						yCoordinate = kingLocation[1] - 2;
						
						if(xCoordinate > 0 && yCoordinate > 0) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Knight) {
									return false;
								}
							}
						}
						
						xCoordinate = kingLocation[0] - 1;
						yCoordinate = kingLocation[1] + 2;
						
						if(xCoordinate > 0 && yCoordinate < 8) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Knight) {
									return false;
								}
							}
						}
						
						xCoordinate = kingLocation[0] - 2;
						yCoordinate = kingLocation[1] - 1;
						
						if(xCoordinate > 0 && yCoordinate > 0) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Knight) {
									return false;
								}
							}
						}
						
						xCoordinate = kingLocation[0] - 2;
						yCoordinate = kingLocation[1] + 1;
						
						if(xCoordinate > 0 && yCoordinate < 8) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && possibleAttacker instanceof Knight) {
									return false;
								}
							}
						}
						
						////////////////////////////////////////////
						// Look for possible bishop/queen attacks //
						////////////////////////////////////////////
						
						//Initialise to Check the path to the upper left
						xCoordinate = kingLocation[0] - 1;
						yCoordinate = kingLocation[1] - 1;
						
						while (xCoordinate > -1 && yCoordinate > -1) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
								
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && (possibleAttacker instanceof Bishop || possibleAttacker instanceof Queen)) {
									return false;
								}
								else {
									break;
								}
							}
							xCoordinate--;
							yCoordinate--;
						}
						
						//Reinitialise to Check the path to the upper right
						xCoordinate = kingLocation[0] - 1;
						yCoordinate = kingLocation[1] + 1;
						
						while (xCoordinate > -1 && yCoordinate < 8) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && (possibleAttacker instanceof Bishop || possibleAttacker instanceof Queen)) {
									return false;
								}
								else {
									break;
								}
							}
							xCoordinate--;
							yCoordinate++;
						}

						//Reinitialise to Check the path to the lower left
						xCoordinate = kingLocation[0] + 1;
						yCoordinate = kingLocation[1] - 1;
							
						while (xCoordinate < 8 && yCoordinate > -1) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && (possibleAttacker instanceof Bishop || possibleAttacker instanceof Queen)) {
									return false;
								}
								else {
									break;
								}
							}
							xCoordinate++;
							yCoordinate--;
						}
						
						//Reinitialise to Check the path to the lower right
						xCoordinate = kingLocation[0] + 1;
						yCoordinate = kingLocation[1] + 1;
							
						while (xCoordinate < 8 && yCoordinate < 8) {
							possibleAttacker = referenceGridCopy[xCoordinate][yCoordinate];
							
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && (possibleAttacker instanceof Bishop || possibleAttacker instanceof Queen)) {
									return false;
								}
								else {
									break;
								}
							}
							xCoordinate++;
							yCoordinate++;
						}
						
						//////////////////////////////////////////
						// Look for possible rook/queen attacks //
						//////////////////////////////////////////
						
						//Check the path north
						for (int x = kingLocation[0] - 1; x > -1; x--) { 
							possibleAttacker = referenceGridCopy[x][kingLocation[1]];
							
							//Check to see if piece exists at the current location
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && (possibleAttacker instanceof Rook || possibleAttacker instanceof Queen)) {
									return false;
								}
								else {
									break;
								}
							}
						}
						
						//Check the path west
						for (int y = kingLocation[1] - 1; y > -1; y--) {
							possibleAttacker = referenceGridCopy[kingLocation[0]][y];
							
							//Check to see if piece exists at the current location
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && (possibleAttacker instanceof Rook || possibleAttacker instanceof Queen)) {
									return false;
								}
								else {
									break;
								}
							}
						}

						//Check the path east
						for (int y = kingLocation[1] + 1; y < 8; y++) {
							possibleAttacker = referenceGridCopy[kingLocation[0]][y];
							
							//Check to see if piece exists at the current location
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && (possibleAttacker instanceof Rook || possibleAttacker instanceof Queen)) {
									return false;
								}
								else {
									break;
								}
							}
						}
						
						//Check the path south
						for (int x = kingLocation[0] + 1; x < 8; x++) {
							possibleAttacker = referenceGridCopy[x][kingLocation[1]];
							
							//Check to see if piece exists at the current location
							if(possibleAttacker != null) {
								if(arePieceColorsDifferent(kingToCheckForCheck, possibleAttacker) && (possibleAttacker instanceof Rook || possibleAttacker instanceof Queen)) {
									return false;
								}
								else {
									break;
								}
							}
						}
						break;
					}
				}
			}
		}
		return true;
	}
}
