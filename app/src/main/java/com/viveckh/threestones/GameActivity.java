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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class GameActivity extends Activity {

	//Variable Declarations
	private Bundle m_intentExtras;
	final private Button[][] buttons = new Button[11][11];
	private Board m_board;
	private Human m_human;
	private Computer m_computer;
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
		setContentView(R.layout.activity_game);

		//Background for game board buttons
		m_blankPic = getResources().
			  getIdentifier("blank_circle", "drawable", "com.viveckh.threestones");
		m_whitePic = getResources().
			  getIdentifier("white_circle", "drawable", "com.viveckh.threestones");
		m_blackPic = getResources().
			  getIdentifier("black_circle", "drawable", "com.viveckh.threestones");
		m_clearPic = getResources().
			  getIdentifier("clear_circle", "drawable", "com.viveckh.threestones");

		//Initializing view objects
		btnComputerPlay = (Button)findViewById(R.id.btnComputerPlay);
		btnHelp = (Button)findViewById(R.id.btnHelp);
		btnSave = (Button)findViewById(R.id.btnSave);
		txtViewNotifications = (TextView)findViewById(R.id.txtViewNotifications);
		radioStonePicker = (RadioGroup)findViewById(R.id.radioStonePicker);
		SetStoneChoiceUsingRadioGroupListener();

		//Getting the intent from previous activity
		m_intentExtras = getIntent().getExtras();

		//If restore mode, get content from Tournament class. Otherwise, start a fresh game
		if (m_intentExtras.getString("startMode").equals("restore")) {
			m_board = new Board((Board)m_intentExtras.getSerializable("gameBoard"));
			m_humanStoneColor = Tournament.GetHumanStone();
			m_computerStoneColor = Tournament.GetComputerStone();
		}
		else {
			//Initializing Variables for fresh game
			m_board = new Board();
			m_humanStoneColor = m_intentExtras.getChar("humanStone");
			m_computerStoneColor = m_intentExtras.getChar("computerStone");
		}

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

		//Update the available stones count if the game is being restored
		if (m_intentExtras.getString("startMode").equals("restore")) {
			m_computer.SetStonesAvailability(Tournament.GetComputerWhiteStonesCount(),
				  Tournament.GetComputerBlackStonesCount(),
				  Tournament.GetComputerClearStonesCount());
			m_computer.SetScore(Tournament.GetComputerScore());

			m_human.SetStonesAvailability(Tournament.GetHumanWhiteStonesCount(),
				  Tournament.GetHumanBlackStonesCount(),
				  Tournament.GetHumanClearStonesCount());
			m_human.SetScore(Tournament.GetHumanScore());

			m_computer.SetPreviousPlacements(Tournament.GetRowOfLastPlacement(),
				  Tournament.GetColumnOfLastPlacement());
		}

		UpdatePlayerImages();
		CreateBoard();
		UpdateGameStatusView();
	}

	//Kill activity on pressing back button
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		//If user taps back button
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			m_computer.ResetPreviousPlacements();
			finish();

		}
		//If user taps enter to perform computer move
		if ((keyCode == KeyEvent.KEYCODE_SPACE)) {
			btnComputerPlay.performClick();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void CreateBoard() {
		//Handle taps for Help Button
		btnHelp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			if (m_computer.Play(true, m_board, m_human)) {
				Notifications.Msg_HelpModeRecommendedMove(m_computer.GetRecommendedStone(),
					  m_computer.GetHighestScorePossible());
				DisplayNotifications();

				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
					  R.anim.rotate_around_center_point);
				buttons[m_computer.GetRecommendedRow()][m_computer.GetRecommendedColumn()].
					  startAnimation(animation);
			}
			}
		});

		//Handle taps for Save Button
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
					if (m_board.GetStoneAtLocation(i, j) == 'w') {
						buttons[i][j].setBackgroundResource(R.drawable.white_circle);
					}
					else if (m_board.GetStoneAtLocation(i, j) == 'b') {
						buttons[i][j].setBackgroundResource(R.drawable.black_circle);
					}
					else if (m_board.GetStoneAtLocation(i, j) == 'c') {
						buttons[i][j].setBackgroundResource(R.drawable.clear_circle);
					}
					else {
						buttons[i][j].setBackgroundResource(R.drawable.blank_circle);
					}
				}


				rowLayout.addView(buttons[i][j], p);

				//Setting onclicklistener for buttons in the gameBoard
				final int x = i;
				final int y = j;
				buttons[x][y].setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
					if (m_turn == 1) {
						//Human's turn
						if (StoneFiller(buttons, x, y, m_stoneChoice)) {
							//Handing control to the other player
							m_turn = 0;
							UpdateGameStatusView();
						}
					}
					DisplayNotifications();
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
							rowOfPlacement = m_computer.
								  GetRowOfPreviousPlacement();
							columnOfPlacement = m_computer.
								  GetColumnOfPreviousPlacement();
							stoneOfPlacement = m_computer.
								  GetStoneOfPreviousPlacement();

							//Update Opponent's score as well
							m_human.UpdateScoreAfterMove(m_human.GetPlayerStoneColor(),
								  rowOfPlacement, columnOfPlacement, m_board);
							buttons[rowOfPlacement][columnOfPlacement].
								  setClickable(false);

							//Set background for the button clicked
							if (stoneOfPlacement == 'w') {
								buttons[rowOfPlacement][columnOfPlacement].
									  setBackgroundResource(m_whitePic);
							}
							if (stoneOfPlacement == 'b') {
								buttons[rowOfPlacement][columnOfPlacement].
									  setBackgroundResource(m_blackPic);
							}
							if (stoneOfPlacement == 'c') {
								buttons[rowOfPlacement][columnOfPlacement].
									  setBackgroundResource(m_clearPic);
							}
						}

						//Handing control to the other player
						m_turn = 1;
						UpdateGameStatusView();
						DisplayNotifications();
					}
					}
				});
			}
		}
	}

	private void SaveGame() {
		// Display an alert dialog box to confirm the user wants to save and exit the game
		AlertDialog.Builder alert = new AlertDialog
			  .Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
		final EditText txtFileName = new EditText(getApplicationContext());
		alert.setTitle("Saving your game...");
		alert.setMessage("Enter a name to access it later:");

		alert.setView(txtFileName);

		Typeface tfCaviar = Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams_Bold.ttf");
		txtFileName.setTypeface(tfCaviar);
		txtFileName.setTextColor(Color.WHITE);

		//If user chooses to move forward with saving the game
		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface a_dialog, int a_whichButton) {
				//Retrieve the user entered file name
				String fileName = txtFileName.getText().toString();

				//Saving current game status to the Tournament variables
				Tournament.SaveCurrentGameStatus(m_humanStoneColor, m_computerStoneColor,
					  m_human.GetWhiteStonesAvailable(),
					  m_human.GetBlackStonesAvailable(),
					  m_human.GetClearStonesAvailable(),
					  m_computer.GetWhiteStonesAvailable(),
					  m_computer.GetBlackStonesAvailable(),
					  m_computer.GetClearStonesAvailable(),
					  m_human.GetScore(), m_computer.GetScore());
				if (m_turn == 0) {
					Tournament.SetControls(m_computer.GetRowOfPreviousPlacement(),
						  m_computer.GetColumnOfPreviousPlacement(), "computer");
				}
				else {
					Tournament.SetControls(m_human.GetRowOfPreviousPlacement(),
						  m_human.GetColumnOfPreviousPlacement(), "human");
				}

				//Write to the file
				Serializer serializer = new Serializer(HomeActivity.m_internalStorage);
				if (serializer.WriteToFile(fileName + ".txt", m_board)) {
					//On success, dispatch a tap on the back button
					Toast.makeText(getApplicationContext(),
						  "Your game was successfully saved...",
						  Toast.LENGTH_LONG).show();
					dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
						  KeyEvent.KEYCODE_BACK));
				}
			}
		});

		//If user decides to continue with game without saving
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface a_dialog, int a_whichButton) {
				// Leave the game state as it is
			}
		});

		alert.show();
	}

	private void CheckIfGameOver() {
		//Saving current game status to the Tournament variables
		Tournament.SaveCurrentGameStatus(m_humanStoneColor, m_computerStoneColor,
			  m_human.GetWhiteStonesAvailable(),
			  m_human.GetBlackStonesAvailable(),
			  m_human.GetClearStonesAvailable(),
			  m_computer.GetWhiteStonesAvailable(),
			  m_computer.GetBlackStonesAvailable(),
			  m_computer.GetClearStonesAvailable(),
			  m_human.GetScore(), m_computer.GetScore());

		//If all the stones of both the players are over, the game is over
		if (m_human.GetWhiteStonesAvailable() == 0 && m_human.GetBlackStonesAvailable() == 0 &&
			m_human.GetClearStonesAvailable() == 0)
		{

			if (m_computer.GetWhiteStonesAvailable() == 0 &&
				 m_computer.GetBlackStonesAvailable() == 0 &&
				 m_computer.GetClearStonesAvailable() == 0)
			{
				Intent intent = new Intent(this, ResultsActivity.class);
				//Increment Computer's tournament score if computer wins
				if(m_computer.GetScore() > m_human.GetScore()) {
					Tournament.IncrementComputerWinsBy(1);
					intent.putExtra("winnerMsg", "BOT WON");
					Toast.makeText(getApplicationContext(), "Bot won!",
						  Toast.LENGTH_LONG).show();
				}
				//Increment Human's tournament score if human wins
				if(m_computer.GetScore() < m_human.GetScore()) {
					Tournament.IncrementHumanWinsBy(1);
					intent.putExtra("winnerMsg", "YOU WON");
					Toast.makeText(getApplicationContext(), "You won!",
						  Toast.LENGTH_LONG).show();
				}
				//Don't increase anyone's tournament score if a draw
				if(m_computer.GetScore() == m_human.GetScore()) {
					intent.putExtra("winnerMsg", "IT'S A DRAW");
					Toast.makeText(getApplicationContext(), "It's a draw!",
						  Toast.LENGTH_LONG).show();
				}

				intent.putExtra("humanStone", m_intentExtras.getChar("humanStone"));
				intent.putExtra("computerStone", m_intentExtras.getChar("computerStone"));
				m_computer.ResetPreviousPlacements();

				startActivity(intent);
			}
		}
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

	// This function fills the chosen stone in the given pouch and updates necessary properties
	public boolean StoneFiller(Button[][] a_buttons, int a_row, int a_column, String a_stoneChoice)
	{
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
		else {
			stone = 'c';
			newButtonBackground = m_clearPic;
		}

		/*Setting up the necessary flags/values in the view upon success in model
		* THE PLACEASTONE FUNCTION SHOULD BE CALLED IN CONJUNCTION WITH THE
		* UPDATESCORESAFTERMOVE FUNCTION FOR EACH PLAYER*/

		//If Human's turn
		if (m_turn == 1) {
			if (m_human.Play(stone, a_row, a_column, m_board)) {
				//Update Opponent's score as well
				m_computer.UpdateScoreAfterMove(m_computer.GetPlayerStoneColor(),
					  a_row, a_column, m_board);
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

	//Sets the stone images for each player in the scoreboard. Call once knowing stone values
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
		CheckIfGameOver();
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
		turnHighlighter.setInterpolator(new AccelerateDecelerateInterpolator());
		turnHighlighter.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
		turnHighlighter.setRepeatMode(Animation.REVERSE); //Reverse animation to fade back in

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
		Typeface tfCaviar = Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams_Bold.ttf");
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
		AnimateValidCells(m_computer.GetRowOfPreviousPlacement(),
			  m_computer.GetColumnOfPreviousPlacement());
	}

	private void AnimateValidCells(int a_row, int a_column) {
		//If buttons on the board are initialized
		if (buttons[0][0] != null) {
			if (a_row >= 0 && a_column >= 0) {
				// Prepare the animation for highlighting latest placement
				Animation moveHighlighter = new AlphaAnimation(1, 0.2f);
				moveHighlighter.setDuration(1000);
				moveHighlighter.setInterpolator(new AccelerateDecelerateInterpolator());
				moveHighlighter.setRepeatCount(Animation.INFINITE);
				moveHighlighter.setRepeatMode(Animation.REVERSE);

				//Prepare the animation for blurring invalid buttons
				Animation animation = new AlphaAnimation(0.3f, 0.3f);
				animation.setDuration(10000);
				animation.setInterpolator(new AccelerateDecelerateInterpolator());
				animation.setRepeatCount(Animation.INFINITE);
				animation.setRepeatMode(Animation.REVERSE);

				//Check if all the blocks in the row/column of last placement are occupied.
				//If so, the player has the option to place stone anywhere on board
				boolean doNotOpenEntireBoardForPlacement = false;
				for (int row = 0; row < m_board.GetBoardDimension(); row++) {
					for (int col = 0; col < m_board.GetBoardDimension(); col++) {
						//if one spot is vacant in the row/column of last placement
						if (row == a_row || col == a_column) {
							if (m_board.GetBlockAtLocation(row, col) != null &&
								m_board.GetBlockAtLocation(row, col).IsInitialized()
								&& !m_board.GetBlockAtLocation(row, col).IsOccupied())
							{
								doNotOpenEntireBoardForPlacement = true;
							}
						}
					}
				}

				//Start animations
				for (int row = 0; row < m_board.GetBoardDimension(); row++) {
					for (int col = 0; col < m_board.GetBoardDimension(); col++) {

						/*If 'doNotOpenEntireBoardForPlacement' is true,
						* then only enable blocks in 1 row and column for placement*/
						if (doNotOpenEntireBoardForPlacement) {
							//First blur all the buttons on the board
							buttons[row][col].startAnimation(animation);

							//Now, clear blur from valid row/column for next move
							if (row == a_row || col == a_column) {
								buttons[row][col].clearAnimation();
							}

							//Now, clear blur effect from occupied blocks as well
							if (m_board.GetBlockAtLocation(row, col) != null
								&& m_board.GetBlockAtLocation(row, col).IsInitialized()
								&& m_board.GetBlockAtLocation(row, col).IsOccupied())
							{
								buttons[row][col].clearAnimation();
							}
						}
						else {
							//Clear all forms of previous animations
							buttons[row][col].clearAnimation();
						}

						//Now, start highlighting block where stone was last placed
						if (row == a_row && col == a_column) {
							buttons[row][col].startAnimation(moveHighlighter);
						}
					}
				}
			}
		}
	}
}