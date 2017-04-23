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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;

public class GameActivity extends Activity {

	//Declaring variables
	final private Button[][] buttons = new Button[11][11];
	private Board m_board;
	private Human m_human;
	private Computer m_computer;
	private String[] m_stones;
	private char m_humanStoneColor;
	private char m_computerStoneColor;
	private String m_stoneChoice;
	private int m_turn;		//0 refers to computer's turn, 1 refers to human's turn
	private int m_blankPic, m_whitePic, m_blackPic, m_clearPic;

	//View objects
	private Button btnComputerPlay, btnHelp, btnSave;
	private RadioGroup radioStonePicker;
	TextView txtViewNotifications;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		//Background for game board buttons
		m_blankPic = getResources().getIdentifier("blank_circle", "drawable", "com.viveckh.threestones");
		m_whitePic = getResources().getIdentifier("white_circle", "drawable", "com.viveckh.threestones");
		m_blackPic = getResources().getIdentifier("black_circle", "drawable", "com.viveckh.threestones");
		m_clearPic = getResources().getIdentifier("clear_circle", "drawable", "com.viveckh.threestones");

		//Initializing view objects
		btnComputerPlay = (Button)findViewById(R.id.btnComputerPlay);
		btnHelp = (Button)findViewById(R.id.btnHelp);
		btnSave = (Button)findViewById(R.id.btnSave);
		txtViewNotifications = (TextView)findViewById(R.id.txtViewNotifications);
		radioStonePicker = (RadioGroup)findViewById(R.id.radioStonePicker);
		SetStoneChoiceUsingRadioGroupListener();

		//Initializing Variables
		m_board = new Board();

		//Getting the intent from previous activity
		Bundle extras = getIntent().getExtras();
		m_humanStoneColor = extras.getChar("humanStone");
		m_computerStoneColor = extras.getChar("computerStone");

		//Initializing turn value. 0 for computer, 1 for human
		if (Tournament.GetNextPlayer().equals("computer")) {
			m_turn = 0;
		}
		else {
			m_turn = 1;
		}

		//Initializing players
		if (m_humanStoneColor == 'b') {
			m_human = new Human('b');
			m_computer = new Computer('w');
			//Selecting black stone choice by default for the human using radio button.
			radioStonePicker.check(R.id.radioButton2);
		}
		else {
			m_human = new Human('w');
			m_computer = new Computer('b');
			//Selecting white stone choice by default for the human using the radio button
			radioStonePicker.check(R.id.radioButton1);
		}
		UpdatePlayerImages();
		UpdateGameStatusView();
		createBoard();
	}

	//Kill activity on pressing back button
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			m_computer.ResetPreviousPlacements();
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}

	public void createBoard() {

		/*
		//Setting up java counterparts for the android layout objects
		final Spinner stonePicker = (Spinner)findViewById(R.id.stonePicker);
		m_stones = getResources().getStringArray(R.array.stones);

		//Setting up adapter for stone color picking dropdown
		ArrayAdapter<String> stone_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, m_stones);
		stone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stonePicker.setAdapter(stone_adapter);
		*/
		btnHelp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			if (m_computer.Play(true, m_board, m_human)) {
				Notifications.Msg_HelpModeRecommendedMove(m_computer.GetRecommendedStone(), m_computer.GetHighestScorePossible());
				DisplayNotifications();
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_around_center_point);
				buttons[m_computer.GetRecommendedRow()][m_computer.GetRecommendedColumn()].startAnimation(animation);
			}
			}
		});

		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SaveGame();
			}
		});

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

				/*
				//Listening to which colored stone to use
				stonePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
						if(stonePicker.getSelectedItemPosition() == 0) {
							if (m_turn == 0) { m_stoneChoice = m_computerStoneColor; }
							if (m_turn == 1) { m_stoneChoice = m_humanStoneColor; }
						}

						else if (stonePicker.getSelectedItemPosition() == 1) {
							if (m_turn == 0) { m_stoneChoice = m_humanStoneColor; }
							if (m_turn == 1) { m_stoneChoice = m_computerStoneColor; }
						}

						else { m_stoneChoice = "clear"; }

						UpdateGameStatusView();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
				*/

				//Setting onclicklistener for buttons in the gameBoard
				final int x = i;
				final int y = j;
				buttons[x][y].setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//
						//Either human or computer's function can be used to check permission
						//if (m_human.HasPermissionToOccupyVacantSpot(x, y, m_board)) {
							if (m_turn == 1) {
								//Human's turn
								if (StoneFiller(buttons, x, y, m_stoneChoice)) {

									//Handing control to the other player
									m_turn = 0;
									UpdateGameStatusView();
									/*
									if (stonePicker.getSelectedItemPosition() == 0) {
										m_stoneChoice = m_computerStoneColor;
									} else if (stonePicker.getSelectedItemPosition() == 1) {
										m_stoneChoice = m_humanStoneColor;
									} else {
										m_stoneChoice = "clear";
									}
									*/
								}
							}
							DisplayNotifications();
							//NOTE: Job of updating the location where stone was last inserted is already done within Player class after insertion

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
					//}
				});

				//If computer's play button is pressed
				btnComputerPlay.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						//Computer's turn
						if (m_turn == 0) {
							int rowOfPlacement = 0, columnOfPlacement = 0;
							char stoneOfPlacement = 'x';
							if (m_computer.Play(false, m_board, m_human)) {
								rowOfPlacement = m_computer.GetRowOfPreviousPlacement();
								columnOfPlacement = m_computer.GetColumnOfPreviousPlacement();
								stoneOfPlacement = m_computer.GetStoneOfPreviousPlacement();

								//Update Opponent's score as well
								m_human.UpdateScoreAfterMove(m_human.GetPlayerStoneColor(), rowOfPlacement, columnOfPlacement, m_board);
								buttons[rowOfPlacement][columnOfPlacement].setClickable(false);

								if (stoneOfPlacement == 'w') {
									buttons[rowOfPlacement][columnOfPlacement].setBackgroundResource(m_whitePic);
								}
								if (stoneOfPlacement == 'b') {
									buttons[rowOfPlacement][columnOfPlacement].setBackgroundResource(m_blackPic);
								}
								if (stoneOfPlacement == 'c') {
									buttons[rowOfPlacement][columnOfPlacement].setBackgroundResource(m_clearPic);
								}
							}

							//Handing control to the other player
							//ATTENTION: Do this only upon successful move, not default as now
							m_turn = 1;
							UpdateGameStatusView();
							DisplayNotifications();
							/*
							if (stonePicker.getSelectedItemPosition() == 0) {
								m_stoneChoice = m_humanStoneColor;
							} else if (stonePicker.getSelectedItemPosition() == 1) {
								m_stoneChoice = m_computerStoneColor;
							} else {
								m_stoneChoice = "clear";
							}
							*/
						}
					}
				});
			}
		}
	}

	private void SaveGame() {
		//Saving current game status to the Tournament variables
		Tournament.SaveCurrentGameStatus(m_humanStoneColor, m_computerStoneColor, m_human.GetWhiteStonesAvailable(), m_human.GetBlackStonesAvailable(), m_human.GetClearStonesAvailable(), m_computer.GetWhiteStonesAvailable(), m_computer.GetBlackStonesAvailable(), m_computer.GetClearStonesAvailable(), m_human.GetScore(), m_computer.GetScore());
		if (m_turn == 0) {
			Tournament.SetNextPlayer("computer");
		}
		else {
			Tournament.SetNextPlayer("human");
		}

		//Write to the file
		Serializer serializer = new Serializer(HomeActivity.m_internalStorage);
		serializer.WriteToFile("LastGame.txt", m_board);
	}

	private void SetStoneChoiceUsingRadioGroupListener() {
		radioStonePicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.radioButton1:
						m_stoneChoice = "white";
						break;
					case R.id.radioButton2:
						m_stoneChoice = "black";
						break;
					case R.id.radioButton3:
						m_stoneChoice = "clear";
						break;
				}
			}
		});
	}

	// This function fills the chosen stone in the given pouch and updates the necessary properties in the model class.
	public boolean StoneFiller(Button[][] a_buttons, int a_row, int a_column, String a_stoneChoice) {
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
		//THE PLACEASTONE FUNCTION SHOULD BE CALLED IN CONJUNCTION WITH THE UPDATESCORESAFTERMOVE FUNCTION FOR EACH PLAYER
		//If Human's turn
		if (m_turn == 1) {
			if (m_human.Play(stone, a_row, a_column, m_board)) {
				//Update Opponent's score as well
				m_computer.UpdateScoreAfterMove(m_computer.GetPlayerStoneColor(), a_row, a_column, m_board);
				a_buttons[a_row][a_column].setClickable(false);
				a_buttons[a_row][a_column].setBackgroundResource(newButtonBackground);
				return true;
			}
		}
		return false;
	}

	private void DisplayNotifications() {
		String msgToDisplay;
		// If the msg vector is not empty, print out the msges that have been stored in it
		if (!Notifications.GetNotificationsList().isEmpty()) {
			msgToDisplay = Notifications.GetNotificationsList().toString()
				  .replace(",", "")
				  .replace("[", "")
				  .replace("]", "");
			txtViewNotifications.setText(msgToDisplay);
		}
		Notifications.ClearNotificationsList();
	}

	//Sets the stone images for each player in the scoreboard. Supposed to be called once the stone values read from previous intent
	private void UpdatePlayerImages() {
		ImageButton imgHumanStone = (ImageButton)findViewById(R.id.imgHumanStone);
		ImageButton imgComputerStone = (ImageButton)findViewById(R.id.imgComputerStone);

		if (m_humanStoneColor == 'b') {
			imgHumanStone.setBackgroundResource(m_blackPic);
			imgComputerStone.setBackgroundResource(m_whitePic);
		}
		else {
			imgHumanStone.setBackgroundResource(m_whitePic);
			imgComputerStone.setBackgroundResource(m_blackPic);
		}
	}

	private void UpdateGameStatusView() {
		Button btnHumanScore = (Button)findViewById(R.id.btnHumanScore);
		Button btnComputerScore = (Button)findViewById(R.id.btnComputerScore);
		TextView labelHumanScore = (TextView)findViewById(R.id.labelHumanScore);
		TextView labelComputerScore = (TextView)findViewById(R.id.labelComputerScore);

		TextView labelScoreBoard = (TextView) findViewById(R.id.labelScoreBoard);
		TextView labelControls = (TextView) findViewById(R.id.labelControls);
		TextView labelNotifications = (TextView) findViewById(R.id.labelNotifications);

		//Retrieve stone picker components from the view
		TextView labelStonePicker = (TextView)findViewById(R.id.labelStonePicker);
		RadioGroup radioStonePicker = (RadioGroup)findViewById(R.id.radioStonePicker);
		RadioButton radioButton1 = (RadioButton)findViewById(R.id.radioButton1);
		RadioButton radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
		RadioButton radioButton3 = (RadioButton)findViewById(R.id.radioButton3);

		//Setting up animations for turns
		Animation turnHighlighter = new AlphaAnimation(1, 0.2f);
		turnHighlighter.setDuration(200);
		turnHighlighter.setInterpolator(new AccelerateDecelerateInterpolator()); // do not alter animation rate
		turnHighlighter.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
		turnHighlighter.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

		//Displaying images next to the radiobuttons
		Drawable resized_white_circle = getResources().getDrawable(m_whitePic);
		Drawable resized_black_circle = getResources().getDrawable(m_blackPic);
		Drawable resized_clear_circle = getResources().getDrawable(m_clearPic);
		resized_white_circle.setBounds(0, 0, 50, 50);
		resized_black_circle.setBounds(0, 0, 50, 50);
		resized_clear_circle.setBounds(0, 0, 50, 50);
		radioButton1.setCompoundDrawables(resized_white_circle, null, null, null);
		radioButton2.setCompoundDrawables(resized_black_circle, null, null, null);
		radioButton3.setCompoundDrawables(resized_clear_circle, null, null, null);

		//Setting the fonts
		Typeface tfRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
		Typeface tfCaviar = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf");
		Typeface tfSeaside = Typeface.createFromAsset(getAssets(), "fonts/SeasideResortNF.ttf");

		//Set fonts of section headers
		labelScoreBoard.setTypeface(tfCaviar);
		labelControls.setTypeface(tfCaviar);
		labelNotifications.setTypeface(tfCaviar);

		//Set fonts for the score board
		labelHumanScore.setTypeface(tfCaviar);
		labelComputerScore.setTypeface(tfCaviar);
		btnHumanScore.setTypeface(tfCaviar);
		btnComputerScore.setTypeface(tfCaviar);

		//Set font of gameplay controls
		btnComputerPlay.setTypeface(tfSeaside);
		btnHelp.setTypeface(tfSeaside);
		btnSave.setTypeface(tfSeaside);
		labelStonePicker.setTypeface(tfCaviar);
		radioButton1.setTypeface(tfCaviar);
		radioButton2.setTypeface(tfCaviar);
		radioButton3.setTypeface(tfCaviar);

		//Set font for notifications board
		txtViewNotifications.setTypeface(tfRoboto);

		//If computer's turn
		if (m_turn == 0) {
			labelHumanScore.clearAnimation();
			labelComputerScore.startAnimation(turnHighlighter);

			btnComputerPlay.setVisibility(View.VISIBLE);
			btnHelp.setVisibility(View.INVISIBLE);

			//Disable radiobuttons for stone picking
			for(int i = 0; i < radioStonePicker.getChildCount(); i++){
				radioStonePicker.getChildAt(i).setEnabled(false);
			}

			radioButton1.setText(String.valueOf(m_computer.GetWhiteStonesAvailable()));
			radioButton2.setText(String.valueOf(m_computer.GetBlackStonesAvailable()));
			radioButton3.setText(String.valueOf(m_computer.GetClearStonesAvailable()));
		}
		//If human's turn
		else {
			labelComputerScore.clearAnimation();
			labelHumanScore.startAnimation(turnHighlighter);

			btnHelp.setVisibility(View.VISIBLE);
			btnComputerPlay.setVisibility(View.INVISIBLE);

			for(int i = 0; i < radioStonePicker.getChildCount(); i++){
				radioStonePicker.getChildAt(i).setEnabled(true);
			}

			radioButton1.setText(String.valueOf(m_human.GetWhiteStonesAvailable()));
			radioButton2.setText(String.valueOf(m_human.GetBlackStonesAvailable()));
			radioButton3.setText(String.valueOf(m_human.GetClearStonesAvailable()));
		}

		//Score board update
		btnHumanScore.setText(String.valueOf(m_human.GetScore()));
		btnComputerScore.setText(String.valueOf(m_computer.GetScore()));

		//Animate the buttons
		AnimateValidCells(m_computer.GetRowOfPreviousPlacement(), m_computer.GetColumnOfPreviousPlacement());
	}

	private void AnimateValidCells(int a_row, int a_column) {
		//If buttons on the board are initialized
		if (buttons[0][0] != null) {
			if (a_row >= 0 && a_column >= 0) {
				// Prepare the animation for highlighting latest placement
				Animation moveHighlighter = new AlphaAnimation(1, 0.2f);
				moveHighlighter.setDuration(1000);
				moveHighlighter.setInterpolator(new AccelerateDecelerateInterpolator()); // do not alter animation rate
				moveHighlighter.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
				moveHighlighter.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

				//Prepare the animation for blurring invalid buttons
				Animation animation = new AlphaAnimation(0.3f, 0.3f);     // Change alpha from fully visible to invisible
				animation.setDuration(10000); // duration - half a second
				animation.setInterpolator(new AccelerateDecelerateInterpolator()); // do not alter animation rate
				animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
				animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

				//Check if all the blocks in the row/column of last placement are occupied.
				//If so, the player has the option to place stone anywhere on board
				boolean doNotOpenEntireBoardForPlacement = false;
				for (int row = 0; row < m_board.GetBoardDimension(); row++) {
					for (int col = 0; col < m_board.GetBoardDimension(); col++) {
						//if at least one spot is vacant in the row/column of last placement
						if (row == a_row || col == a_column) {
							if (m_board.GetBlockAtLocation(row, col) != null && m_board.GetBlockAtLocation(row, col).IsInitialized() && !m_board.GetBlockAtLocation(row, col).IsOccupied()) {
								doNotOpenEntireBoardForPlacement = true;
							}
						}
					}
				}

				//Start animations
				for (int row = 0; row < m_board.GetBoardDimension(); row++) {
					for (int col = 0; col < m_board.GetBoardDimension(); col++) {

						//If 'doNotOpenEntireBoardForPlacement' is true, then only enable blocks in 1 row and column for placement
						if (doNotOpenEntireBoardForPlacement) {
							//First blur all the buttons on the board
							buttons[row][col].startAnimation(animation);

							//Now, clear blur effect from valid row and column for next move
							if (row == a_row || col == a_column) {
								buttons[row][col].clearAnimation();
							}

							//Now, clear blur effect from already occupied blocks as well
							if (m_board.GetBlockAtLocation(row, col) != null && m_board.GetBlockAtLocation(row, col).IsInitialized() && m_board.GetBlockAtLocation(row, col).IsOccupied()) {
								buttons[row][col].clearAnimation();
							}
						}
						else {
							//Clear all forms of previous animations
							buttons[row][col].clearAnimation();
						}

						//Now, start move highlighting animation in the block where stone was last placed
						if (row == a_row && col == a_column) {
							buttons[row][col].startAnimation(moveHighlighter);
						}
					}
				}
			}
		}
	}
}