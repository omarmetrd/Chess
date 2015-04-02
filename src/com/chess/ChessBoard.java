package com.chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import com.chess.enums.PieceColor;
import com.chess.pieces.Bishop;
import com.chess.pieces.ChessPiece;
import com.chess.pieces.King;
import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Queen;
import com.chess.pieces.Rook;

/**
 * GUI used to be displayed on screen for the user to interact and play chess
 * 
 * Date: 1/29/2015
 * @author Omar Bonilla
 */
public class ChessBoard extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JButton[][] spaces = new JButton[8][8]; //Represents an array of spaces that are on the board
	private ChessPiece[][] referenceGrid = new ChessPiece[8][8]; //Represents an array of possible chess piece locations
	private ArrayList<int[]> legalPositions = new ArrayList<int[]>(); //Represents legal positions available to a selected piece
	private ChessPiece selectedPiece; //Represents the chess piece selected by a user
	private PieceColor playerTurn; //Represents who's turn it is in the game
	private int turnCounter = 1; //Record used for checking for possible stalemates
	
	
	private JPanel dashboardPanel;
	private JPanel messagePanel;
	private JTextArea textArea;
	
	/**
	 * Default constructor for the ChessBoard class
	 */
	public ChessBoard()
	{
		setLayout(new BorderLayout());
		setTitle("Welcome to Chess!");
		setSize(800, 800);
		setLocationRelativeTo(null);				
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		buildMainPanel();
		initializeChessPieces();
		playerTurn = PieceColor.WHITE;
		textArea.setText("Player White's move");
	}
	
	/**
	 * Places the chess piece onto the chess board
	 * and updating the button icon's image for the corresponding location.
	 * 
	 * @param piece 	Chess piece to be placed onto the board
	 */
	public void placePieceOntoBoard(ChessPiece piece) {
		Component[] components = dashboardPanel.getComponents();
		int[] location = piece.getPieceLocation();
		
		int xCoordinate = location[0];
		int yCoordinate = location[1];
		
		for(int i = 0; i < components.length; i++) {
			if(components[i].getName().equals("Grid [" + xCoordinate + "," + yCoordinate + "]")) {
				((JButton)components[i]).setIcon(new ImageIcon(piece.getPieceImage()));
				referenceGrid[xCoordinate][yCoordinate] = piece;
				break;
			}
		}
	}
	
	/**
	 * Creates the main panel of the chess board
	 */
	private void buildMainPanel()
	{
		buildDashboard();
		buildMessagePanel();
		
		add(messagePanel, BorderLayout.NORTH);
		add(dashboardPanel, BorderLayout.CENTER);
	}
	
	private void buildDashboard()
	{
		dashboardPanel = new JPanel(new GridLayout(8,8));
		Color[] colors = {Color.BLACK, Color.WHITE};
		int k = 1;
		
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				spaces[i][j] = new JButton();
				spaces[i][j].setOpaque(true); 
				spaces[i][j].setBackground(colors[k % 2]);
				spaces[i][j].setName("Grid [" + i + "," + j + "]");
				
				final JButton gridBoard = spaces[i][j];
				
				//Implement an ActionListener to the JButton
				gridBoard.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						JButton clickedComponent = (JButton) arg0.getSource();
						String gridName = clickedComponent.getName();
						String gridLocation = gridName.substring(gridName.indexOf("[") + 1, gridName.indexOf("[") + 4);
						String[] gridCoordinates = gridLocation.split(",");
						
						int[] targetPieceLocation = {Integer.parseInt(gridCoordinates[0]), Integer.parseInt(gridCoordinates[1])};
						
						ChessPiece chessPiece = referenceGrid[targetPieceLocation[0]][targetPieceLocation[1]]; //Try to retrieve piece from reference grid
						
						//Check if there is a piece at the selected coordinates
						if(chessPiece != null) {
							
							//Check that the selected piece is allowed to make a move
							if(isPieceMoveAllowed(chessPiece)) {
									
								//Check if a piece has already been selected previously and that a legal move is not being made
								if(selectedPiece != null && legalPositions.size() < 0) {
									clearHighlightedPositions(); //Remove old highlighted points
								}
								
								//Check if a piece has already been selected and that a legal move is being made
								else if (selectedPiece != null && legalPositions.size() > 0) {
									
									moveChessPiece(selectedPiece, targetPieceLocation);
									checkBoard();
									clearHighlightedPositions(); //Remove old highlighted points
								}
								selectedPiece = chessPiece;
								
								//Check the type of the selected piece to highlight possible moves
								if(chessPiece instanceof Pawn) {
									VerificationHelper.findLegalPawnMovements((Pawn)chessPiece, referenceGrid, legalPositions);
								} 
								else if(chessPiece instanceof Knight) {
									VerificationHelper.findLegalKnightMovements((Knight)chessPiece, referenceGrid, legalPositions);
								} 
								else if(chessPiece instanceof Bishop) {
									VerificationHelper.findLegalBishopMovements((Bishop)chessPiece, referenceGrid, legalPositions);
								} 
								else if(chessPiece instanceof Rook) {
									VerificationHelper.findLegalRookMovements((Rook)chessPiece, referenceGrid, legalPositions);
								} 
								else if(chessPiece instanceof Queen) {
									VerificationHelper.findLegalQueenMovements((Queen)chessPiece, referenceGrid, legalPositions);
								} 
								else if(chessPiece instanceof King) {
									VerificationHelper.findLegalKingMovements((King)chessPiece, referenceGrid, legalPositions);
								}		
								highightPossiblePositions();
							}
							
							else if(selectedPiece != null) {
								if((selectedPiece.isPieceWhite() && !(chessPiece.isPieceWhite())) || (!(selectedPiece.isPieceWhite()) && chessPiece.isPieceWhite())) {
									
									moveChessPiece(selectedPiece, targetPieceLocation);
									checkBoard();
									clearHighlightedPositions(); //Remove old highlighted points
								}
							}
						}
						//A piece has not been selected, so check if an attempt to move a piece is occurring 
						else if(selectedPiece != null) {
							
							//Check if the move is a legal position to move the piece
							if(legalPositions.size() > 0) {
								
								moveChessPiece(selectedPiece, targetPieceLocation);
								checkBoard();
								clearHighlightedPositions(); //Remove old highlighted points
							}
							//A piece has not been selected and highlighted positions must be removed
							else {
								selectedPiece = null;
								clearHighlightedPositions();
							}
						}
					}
				});
				
				dashboardPanel.add(spaces[i][j]);
				
				k++;
			}
			k++;
		}
	}
	
	private void buildMessagePanel() {
		messagePanel = new JPanel();
		messagePanel.setBorder(new TitledBorder("Game Status"));
		
		textArea = new JTextArea(5, 30);
        textArea.setEditable(false);
        textArea.setFont(new Font("Verdana", Font.BOLD, 14));
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAutoscrolls(true);
        
		messagePanel.add(scrollPane);
	}
	
	/**
	 * Creates and places the chess pieces onto the board
	 */
	private void initializeChessPieces() {
		//Create and place the pawns
		for(int i = 0; i < 8; i++) {
			placePieceOntoBoard(new Pawn(PieceColor.BLACK, 1, i));
			placePieceOntoBoard(new Pawn(PieceColor.WHITE, 6, i));
			
			//Place the rest of the black pieces
			if(i == 0) {
				placePieceOntoBoard(new Knight(PieceColor.BLACK, i, 1));
				placePieceOntoBoard(new Knight(PieceColor.BLACK, i, 6));
				
				placePieceOntoBoard(new Bishop(PieceColor.BLACK, i, 2));
				placePieceOntoBoard(new Bishop(PieceColor.BLACK, i, 5));
				
				placePieceOntoBoard(new Rook(PieceColor.BLACK, i, 0));
				placePieceOntoBoard(new Rook(PieceColor.BLACK, i, 7));
				
				placePieceOntoBoard(new Queen(PieceColor.BLACK, i, 3));
				
				placePieceOntoBoard(new King(PieceColor.BLACK, i, 4));
			}
			//Place the rest of the black pieces
			else if(i == 7) {
				placePieceOntoBoard(new Knight(PieceColor.WHITE, i, 1));
				placePieceOntoBoard(new Knight(PieceColor.WHITE, i, 6));
				
				placePieceOntoBoard(new Bishop(PieceColor.WHITE, i, 2));
				placePieceOntoBoard(new Bishop(PieceColor.WHITE, i, 5));
				
				placePieceOntoBoard(new Rook(PieceColor.WHITE, i, 0));
				placePieceOntoBoard(new Rook(PieceColor.WHITE, i, 7));
				
				placePieceOntoBoard(new Queen(PieceColor.WHITE, i, 3));
				
				placePieceOntoBoard(new King(PieceColor.WHITE, i, 4));
			}
		}
	}
	
	/**
	 * Searches and highlights the legal moves referenced in the legalPositions array
	 */
	private void highightPossiblePositions() {
		for (int[] coordinate : legalPositions) {
			JButton buttonToHighlight = findButtonOnBoard(coordinate[0], coordinate[1]);
			buttonToHighlight.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
		}
	}
	
	/**
	 * Clears all the legal positions referenced in the legalPositions array 
	 * and the highlighted spaces
	 */
	private void clearHighlightedPositions() {
		for (int[] coordinate : legalPositions) {	
			JButton buttonToHighlight = findButtonOnBoard(coordinate[0], coordinate[1]);
			buttonToHighlight.setBorder(new EmptyBorder(10, 10, 10, 10));;
		}
		legalPositions.clear();
	}
	
	/**
	 * Updates the selected chess piece's location and updating the corresponding
	 * space's icons
	 * 
	 * @param chessPiece
	 * @param targetPieceLocation
	 */
	private void moveChessPiece(ChessPiece chessPiece, int[] targetPieceLocation) {
		
		final int xTargetAxis = targetPieceLocation[0];
		final int yTargetAxis = targetPieceLocation[1];
		
		//Find previous piece location in order to find the component that needs its icon removed
		int[] previousLocation = selectedPiece.getPieceLocation();
		
		for(int[] coordinate: legalPositions) {
			if(coordinate[0] == xTargetAxis && coordinate[1] == yTargetAxis) {
				
				JButton oldLocationButton = findButtonOnBoard(previousLocation[0], previousLocation[1]);
				oldLocationButton.setIcon(null);
				
				JButton newLocationButton = findButtonOnBoard(xTargetAxis, yTargetAxis);
				newLocationButton.setIcon(new ImageIcon(selectedPiece.getPieceImage()));
				
				
				//Update the new square with the selected piece's icon
				if(newLocationButton != null) {
					newLocationButton.setIcon(new ImageIcon(selectedPiece.getPieceImage()));

					referenceGrid[previousLocation[0]][previousLocation[1]] = null;
					ChessPiece eliminatedPiece = referenceGrid[xTargetAxis][yTargetAxis];
					referenceGrid[xTargetAxis][yTargetAxis] = chessPiece;
					
					//Check the type of the selected piece to call the corresponding method
					if(chessPiece instanceof Pawn) {
						
						//Check for null eliminated piece in case En Passant attack rule is needed
						if(eliminatedPiece == null) {
							if(chessPiece.isPieceWhite() && previousLocation[0] - 1 == xTargetAxis && (previousLocation[1] + 1 == yTargetAxis || previousLocation[1] - 1 == yTargetAxis)) {
								
								//Find and remove the icon and piece from the board
								JButton button = findButtonOnBoard(xTargetAxis + 1, yTargetAxis);
								button.setIcon(null);
								referenceGrid[xTargetAxis + 1][yTargetAxis] = null;
							}
							else if(!(chessPiece.isPieceWhite()) && previousLocation[0] + 1 == xTargetAxis && (previousLocation[1] + 1 == yTargetAxis || previousLocation[1] - 1 == yTargetAxis)) {
								
								//Find and remove the icon and piece from the board
								JButton button = findButtonOnBoard(xTargetAxis - 1, yTargetAxis);
								button.setIcon(null);
								referenceGrid[xTargetAxis - 1][yTargetAxis] = null;
							}
						}
						
						//Store previous x-axis position from pawn and direction it's moving
						int previousX = previousLocation[0];
						int direction = 1;
						
						if(chessPiece.isPieceWhite()) {
							direction = -1;
						}
						
						((Pawn)chessPiece).move(xTargetAxis, yTargetAxis); //Move the piece
						
						//Check if pawn made two moves forward from initial position
						if(previousX + (direction * 2) == previousLocation[0]) {
						
							ChessPiece leftSidePiece = null;
							ChessPiece rightSidePiece = null;
							
							//Check if enemy pawns are located next to the pawn for possible En Passant rule based on its position
							if(yTargetAxis == 7) {
								leftSidePiece = referenceGrid[xTargetAxis][yTargetAxis - 1];
								rightSidePiece = null;
							} 
							else if(yTargetAxis == 0) {
								leftSidePiece = null;
								rightSidePiece = referenceGrid[xTargetAxis][yTargetAxis + 1];
							}
							else {
								leftSidePiece = referenceGrid[xTargetAxis][yTargetAxis - 1];
								rightSidePiece = referenceGrid[xTargetAxis][yTargetAxis + 1];
							}
							
							if(leftSidePiece != null) {
								if(VerificationHelper.arePieceColorsDifferent(chessPiece, leftSidePiece)) {
									((Pawn) chessPiece).makeValidForEnPassant();
								}
							}
							else if(rightSidePiece != null) {
								if(VerificationHelper.arePieceColorsDifferent(chessPiece, rightSidePiece)) {
									((Pawn) chessPiece).makeValidForEnPassant();
								}
							}
						}
						
						//Check if pawn can be promoted based on its target location
						if(xTargetAxis == 0 || xTargetAxis == 7) {
							String[] possiblePromotions = {"Queen", "Knight", "Rook", "Bishop"};
							String userOption = (String) JOptionPane.showInputDialog(null, "Select which piece to promote your pawn to: ", "Pawn Promotion", 
																			JOptionPane.INFORMATION_MESSAGE, null, possiblePromotions, possiblePromotions[0]);
							//Check for possible null exception
							if(userOption == null) {
								userOption = "Queen"; //Set default option as Queen
							}
							switch(userOption) {
							
							case "Knight":
								Knight newKnight;
								
								if(playerTurn == PieceColor.WHITE) {
									newKnight = new Knight(PieceColor.WHITE, xTargetAxis, yTargetAxis);
								}
								else {
									newKnight = new Knight(PieceColor.BLACK, xTargetAxis, yTargetAxis);
								}
								referenceGrid[xTargetAxis][yTargetAxis] = newKnight;
								newLocationButton.setIcon(new ImageIcon(newKnight.getPieceImage()));
								break;
							
							case "Rook":
								Rook newRook;
								
								if(playerTurn == PieceColor.WHITE) {
									newRook = new Rook(PieceColor.WHITE, xTargetAxis, yTargetAxis);
								}
								else {
									newRook = new Rook(PieceColor.BLACK, xTargetAxis, yTargetAxis);
								}
								referenceGrid[xTargetAxis][yTargetAxis] = newRook;
								newLocationButton.setIcon(new ImageIcon(newRook.getPieceImage()));
								break;
								
							case "Bishop":
								Bishop newBishop;
								
								if(playerTurn == PieceColor.WHITE) {
									newBishop = new Bishop(PieceColor.WHITE, xTargetAxis, yTargetAxis);
								}
								else {
									newBishop = new Bishop(PieceColor.BLACK, xTargetAxis, yTargetAxis);
								}
								referenceGrid[xTargetAxis][yTargetAxis] = newBishop;
								newLocationButton.setIcon(new ImageIcon(newBishop.getPieceImage()));
								break;
							
							case "Queen":
							default:
								Queen newQueen;
								
								if(playerTurn == PieceColor.WHITE) {
									newQueen = new Queen(PieceColor.WHITE, xTargetAxis, yTargetAxis);
								}
								else {
									newQueen = new Queen(PieceColor.BLACK, xTargetAxis, yTargetAxis);
								}
								referenceGrid[xTargetAxis][yTargetAxis] = newQueen;
								newLocationButton.setIcon(new ImageIcon(newQueen.getPieceImage()));
								break;
							}
						}
					} 
					else if(chessPiece instanceof Knight) {
						((Knight)chessPiece).move(xTargetAxis, yTargetAxis);
					} 
					else if(chessPiece instanceof Bishop) {
						((Bishop)chessPiece).move(xTargetAxis, yTargetAxis);
					} 
					else if(chessPiece instanceof Rook) {
						((Rook)chessPiece).move(xTargetAxis, yTargetAxis);
					} 
					else if(chessPiece instanceof Queen) {
						((Queen)chessPiece).move(xTargetAxis, yTargetAxis);
					} 
					else if(chessPiece instanceof King) {
						
						int previousY = previousLocation[1];
						
						((King)chessPiece).move(xTargetAxis, yTargetAxis);
						
						//Check if King movement resembles that of Castling
						if(previousLocation[1] - 2 == previousY || previousLocation[1] + 2 == previousY) {
							
							if(previousLocation[1] - 2 == previousY) {
								//Move the Rook to the correct side of the King
								Rook rook = (Rook) referenceGrid[xTargetAxis][yTargetAxis + 1];
								rook.move(xTargetAxis, yTargetAxis - 1);
								
								//Update the corresponding button icons
								JButton button = findButtonOnBoard(xTargetAxis, yTargetAxis + 1);
								button.setIcon(null);
								
								button = findButtonOnBoard(xTargetAxis, yTargetAxis - 1);
								button.setIcon(new ImageIcon(rook.getPieceImage()));
								
								//Update the rook's new location and remove old reference
								referenceGrid[xTargetAxis][yTargetAxis - 1] = rook;
								referenceGrid[xTargetAxis][yTargetAxis + 1] = null;
							}
							else {
							
								//Move the Rook to the correct side of the King
								Rook rook = (Rook) referenceGrid[xTargetAxis][yTargetAxis - 2];
								rook.move(xTargetAxis, yTargetAxis + 1);
								
								//Update the corresponding button icons
								JButton button = findButtonOnBoard(xTargetAxis, yTargetAxis - 2);
								button.setIcon(null);
								
								button = findButtonOnBoard(xTargetAxis, yTargetAxis + 1);
								button.setIcon(new ImageIcon(rook.getPieceImage()));
								
								//Update the rook's new location and remove old reference
								referenceGrid[xTargetAxis][yTargetAxis + 1] = rook;
								referenceGrid[xTargetAxis][yTargetAxis - 2] = null;
							}
						}
					}
					selectedPiece = null;
					
					//Check to see if the King has been eliminated to declare a winner
					if(eliminatedPiece instanceof King) {
						declareWinner();
					}
					else {
						turnCounter++;
						
						//Determine who's turn is next
						switch(playerTurn) {
						case BLACK:
							playerTurn = PieceColor.WHITE;
							displayMessage("\nPlayer White's move ");
							searchAndRemoveEnPassantStatuses(PieceColor.WHITE);
							break;
						case WHITE:
							playerTurn = PieceColor.BLACK;
							displayMessage("\nPlayer Black's move ");
							searchAndRemoveEnPassantStatuses(PieceColor.BLACK);
							break;
						}
					}
					break;
				}
			}
		}
	}
	
	private JButton findButtonOnBoard(int xCoordinate, int yCoordinate) {
		
		Component[] components = dashboardPanel.getComponents();
		
		for(int i = 0; i < components.length; i++) {
			if(components[i].getName().equals("Grid [" + xCoordinate + "," + yCoordinate + "]")) {
				return ((JButton)components[i]);
			}
		}
		
		return null;
	}
	
	private boolean isPieceMoveAllowed(ChessPiece chessPiece) {
		if((chessPiece.isPieceWhite() && playerTurn == PieceColor.WHITE) || (!(chessPiece.isPieceWhite()) && playerTurn == PieceColor.BLACK)) {
			return true;
		}
		else 
			return false;
	}
	
	private void searchAndRemoveEnPassantStatuses(PieceColor pieceColor) {
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				ChessPiece possiblePawn = referenceGrid[i][j];
				
				if(possiblePawn != null) {
					
					switch(pieceColor) {
					case BLACK:

						if(possiblePawn instanceof Pawn && !(possiblePawn.isPieceWhite())) {
							((Pawn)possiblePawn).removeEnPassant();
							referenceGrid[i][j] = possiblePawn;
						}	
						break;
					case WHITE:
						
						if(possiblePawn instanceof Pawn && possiblePawn.isPieceWhite()) {
							((Pawn)possiblePawn).removeEnPassant();
							referenceGrid[i][j] = possiblePawn;
						}
						break;
					}
				}
			}
		}
	}
	
	private void declareWinner() {
		if(playerTurn == PieceColor.BLACK) {
			JOptionPane.showMessageDialog(null, "Player Black has won the game!\nOverall moves played: " + turnCounter, "Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, "Player White has won the game!\nOverall moves played: " + turnCounter, "Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
		//Disable all buttons on the board
		for(int i = 0; i < spaces.length; i++) {
			for(int j = 0; j < spaces.length; j++) {
				spaces[i][j].setEnabled(false);
			}
		}
	}
	
	private void checkBoard() {
		if(VerificationHelper.checkForCheckmate(referenceGrid, playerTurn)) {
			declareWinner();
		}
		//Call method for validating if player has forced a Check to the other player
		else if(VerificationHelper.isKingInCheck(referenceGrid, playerTurn)) {
			if(playerTurn != PieceColor.WHITE) {
				JOptionPane.showMessageDialog(null, "Warning! Player Black is now in Check!");
			}
			else {
				JOptionPane.showMessageDialog(null, "Warning! Player White is now in Check!");
			}
		}
	}
	
	private void displayMessage(String message) {
		textArea.append(message);
	}
}
