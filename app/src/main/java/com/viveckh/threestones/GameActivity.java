package com.viveckh.threestones;


/* Name: Vivek Pandey
 * Date: 3/14/2015
*/

/* This activity class serves as the most important View/Controller of the game which handles the game play.
* It starts by creating the view with the appropriate layouts, buttons and TextViews.
* Then, it sets the onClickListener on every pouches(which are Buttons) and onSelectedItemListener on the dropdowns.
* Before every insertion, the user can see whose turn it is and also has the choice to select stones if it is his/her turn.
* On inserting a stone, it updates the Board, players' scores & stone numbers and the view itself.
* The background of the pouch is changed depending on which stone is inserted giving the user a feel of an actual stone.
* If the user wants to save and quit, he/she can press the "serialize" button during anytime of the game.
* When all the stones are used by both the players, the winner is announced.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

public class GameActivity extends Activity {

	//Declaring variables
	final private Button[][] buttons = new Button[11][11];
	private Board m_board;
	private Human m_human;
	private Human m_computer;
	private String[] m_stones;
	private String m_humanStoneColor;
	private String m_computerStoneColor;
	private String m_stoneChoice;
	private int turn;
	int m_blankPic, m_whitePic, m_blackPic, m_clearPic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		//Initializing Variables
		m_board = new Board();

		//Getting the intent from previous activity
		Bundle extras = getIntent().getExtras();
		m_humanStoneColor = extras.getString("human_stoneColor");
		m_computerStoneColor = extras.getString("computer_stoneColor");

		TextView turnMsg = (TextView)findViewById(R.id.turn);

		//Initializing turn value. 0 for computer, 1 for human
		if (m_humanStoneColor.equals("black")) {
			m_human = new Human('b');
			m_computer = new Human('w');
			turn = 1;
			turnMsg.setText("It's YOUR turn.");
		}
		else {
			m_human = new Human('w');
			m_computer = new Human('b');
			turn = 0;
			turnMsg.setText("It's COMPUTER'S turn.");
		}

		//Background for game board buttons
		m_blankPic = getResources().getIdentifier("blank_circle", "drawable", "com.viveckh.threestones");
		m_whitePic = getResources().getIdentifier("white_circle", "drawable", "com.viveckh.threestones");
		m_blackPic = getResources().getIdentifier("black_circle", "drawable", "com.viveckh.threestones");
		m_clearPic = getResources().getIdentifier("clear_circle", "drawable", "com.viveckh.threestones");
		
		createBoard();
	}

	public void createBoard() {

		//Setting up java counterparts for the android layout objects
		final Spinner stonePicker = (Spinner)findViewById(R.id.stonePicker);
		m_stones = getResources().getStringArray(R.array.stones);

		//Setting up adapter for stone color picking dropdown
		ArrayAdapter<String> stone_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, m_stones);
		stone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stonePicker.setAdapter(stone_adapter);

		//Setting up layouts
		LinearLayout layoutVertical = (LinearLayout) findViewById(R.id.linear_layout1);
		layoutVertical.setWeightSum(11);

		LinearLayout rowLayout = null;
		int count=122;

		LayoutParams p = new LinearLayout.LayoutParams
			  (
				    LayoutParams.MATCH_PARENT,
				    LayoutParams.MATCH_PARENT, 1
			  );

		for (int i = 0; i<11; i++)
		{
			if(count%11==1)
			{
				rowLayout = new LinearLayout(this);
				rowLayout.setWeightSum(11);
				layoutVertical.addView(rowLayout,p);
				count=count-11;
			}
			for(int j=0;j<11;j++)
			{
				buttons[i][j]=new Button(this);
				if (m_board.GetBlockAtLocation(i, j) == null) {
					//Disable the unnecessary cells from the Multidimensional Board
					buttons[i][j].setEnabled(false);
					buttons[i][j].setBackgroundResource(R.drawable.disabled_buttons);
				}
				else {
					buttons[i][j].setBackgroundResource(R.drawable.blank_circle);
				}


				rowLayout.addView(buttons[i][j], p);

				//Getting reference from xml for Scores and available stone displays
				final TextView turnMsg = (TextView)findViewById(R.id.turn);
				final TextView humanScore = (TextView)findViewById(R.id.humanScore);
				final TextView computerScore = (TextView)findViewById(R.id.computerScore);
				final TextView remWhite = (TextView)findViewById(R.id.remWhite);
				final TextView remBlack = (TextView)findViewById(R.id.remBlack);
				final TextView remClear = (TextView)findViewById(R.id.remClear);

				//Listening to which colored stone to use
				stonePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
						if(stonePicker.getSelectedItemPosition() == 0) {
							if (turn == 0) { m_stoneChoice = m_computerStoneColor; }
							if (turn == 1) { m_stoneChoice = m_humanStoneColor; }
						}

						else if (stonePicker.getSelectedItemPosition() == 1) {
							if (turn == 0) { m_stoneChoice = m_humanStoneColor; }
							if (turn == 1) { m_stoneChoice = m_computerStoneColor; }
						}

						else { m_stoneChoice = "clear"; }

						if (turn == 0) { turnMsg.setText("It's COMPUTER'S turn. Stone Choice: " + m_stoneChoice); }
						if (turn == 1) { turnMsg.setText("It's YOUR turn. Stone Choice: " + m_stoneChoice); }
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

				//Setting onclicklistener for buttons in the gameBoard
				final int x = i;
				final int y = j;
				buttons[x][y].setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//
						//Either human or computer's function can be used to check permission
						if (m_computer.HasPermissionToOccupyVacantSpot(x, y, m_board)) {
							//Computer's turn
							if (turn == 0) {

								stoneFiller(buttons, x, y, m_stoneChoice);
								//Handing control to the other player
								if (stonePicker.getSelectedItemPosition() == 0) {
									m_stoneChoice = m_humanStoneColor;
								} else if (stonePicker.getSelectedItemPosition() == 1) {
									m_stoneChoice = m_computerStoneColor;
								} else {
									m_stoneChoice = "clear";
								}

								turn = 1;
								turnMsg.setText("It's YOUR turn. Stone Choice: " + m_stoneChoice);
								remWhite.setText("Remaining White Stones: " + String.valueOf(m_human.GetWhiteStonesAvailable()));
								remBlack.setText("Remaining Black Stones: " + String.valueOf(m_human.GetBlackStonesAvailable()));
								remClear.setText("Remaining Clear Stones: " + String.valueOf(m_human.GetClearStonesAvailable()));

								// return;
							}
							else {
								//Human's turn
								stoneFiller(buttons, x, y, m_stoneChoice);

								//Handing control to the other player
								if (stonePicker.getSelectedItemPosition() == 0) {
									m_stoneChoice = m_computerStoneColor;
								} else if (stonePicker.getSelectedItemPosition() == 1) {
									m_stoneChoice = m_humanStoneColor;
								} else {
									m_stoneChoice = "clear";
								}

								turn = 0;

								turnMsg.setText("It's COMPUTER'S turn. Stone Choice: " + m_stoneChoice);
								remWhite.setText("Remaining White Stones: " + String.valueOf(m_computer.GetWhiteStonesAvailable()));
								remBlack.setText("Remaining Black Stones: " + String.valueOf(m_computer.GetBlackStonesAvailable()));
								remClear.setText("Remaining Clear Stones: " + String.valueOf(m_computer.GetClearStonesAvailable()));
							}
							//NOTE: Job of updating the location where stone was last inserted is already done within Player class after insertion
							humanScore.setText("Human Score: " + String.valueOf(m_human.GetScore()));
							computerScore.setText("Computer Score: " + String.valueOf(m_computer.GetScore()));

							// Game over
							/*
							if (humanPlayer.getWhite() == 0 && humanPlayer.getBlack() == 0 && humanPlayer.getClear() == 0
								  && compPlayer.getWhite() == 0 && compPlayer.getBlack() == 0 && compPlayer.getClear() == 0) {
								if (humanPlayer.getScore() > compPlayer.getScore()) {
									turnMsg.setText("Game Over. You Win");
								}
								else if (humanPlayer.getScore() < compPlayer.getScore()){
									turnMsg.setText("Game Over. The Computer Wins");
								}
								else {
									turnMsg.setText("Game Over. It's a draw");
								}
							}
							*/
						}
					}
				});

				/*
				//Serialize the game to a text file
				Button serialize = (Button)findViewById(R.id.serialize);
				serialize.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						// Perform action on click
						Serialize seri = new Serialize(board, compPlayer, humanPlayer, m_computerStoneColor, m_humanStoneColor, turn, lastX, lastY);

						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();
					}
				});
				*/

			}
		}
	}

	// This function fills the chosen stone in the given pouch and updates the necessary properties in the model class.
	public void stoneFiller(Button[][] a_buttons, int a_row, int a_column, String a_stoneChoice) {
		//Retrieve the passed values and convert them in the proper form
		char stone;
		int newButtonBackground;
		if (a_stoneChoice.equals("white")) {
			stone = 'w';
			newButtonBackground = m_whitePic;
		}
		else if (a_stoneChoice.equals("black")) {
			stone = 'b';
			newButtonBackground = m_blackPic;
		}
		//ATTENTION: Making the default case a clear stone (maybe don't do it?)
		else {
			stone = 'c';
			newButtonBackground = m_clearPic;
		}

		//Setting up the necessary flags/values in the view upon success in model
		//The PlaceAStone function automatically handles score update, and update of available stones
		//If Computer's turn
		if (turn == 0) {
			if (m_computer.PlaceAStone(stone, a_row, a_column, m_board)) {
				a_buttons[a_row][a_column].setClickable(false);
				a_buttons[a_row][a_column].setBackgroundResource(newButtonBackground);
			}
		}
		//If Human's turn
		else {
			if (m_human.PlaceAStone(stone, a_row, a_column, m_board)) {
				a_buttons[a_row][a_column].setClickable(false);
				a_buttons[a_row][a_column].setBackgroundResource(newButtonBackground);
			}
		}
	}

	// Checks if any pouches are available in the current row/column
	public boolean available (Button[][] buttons, int lastX, int lastY) {
		for (int i = 0; i < 11; i++) {
			if (buttons[lastX][i].isEnabled() && buttons[lastX][i].isClickable()) return true;
			if (buttons[i][lastY].isEnabled() && buttons[i][lastY].isClickable()) return true;
		}
		return false;
	}
}