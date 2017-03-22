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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;
public class GameActivity extends Activity {

	//Declaring variables
	final private Button[][] buttons = new Button[11][11];
	final private Board gameSpace = new Board();
	private Player humanPlayer = new Player();
	private Player compPlayer = new Player();
	private String[] stones;
	private String human_stoneColor;
	private String computer_stoneColor;
	private String use_stone;
	private int turn;
	private int lastX = 100, lastY = 100;
	int blank_pic, white_pic, black_pic, clear_pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		//Getting the intent from previous activity
		Bundle extras = getIntent().getExtras();
		human_stoneColor = extras.getString("human_stoneColor");
		computer_stoneColor = extras.getString("computer_stoneColor");
		//Background for game board buttons
		blank_pic = getResources().getIdentifier("blank_circle", "drawable", "com.viveckh.threestones");
		white_pic = getResources().getIdentifier("white_circle", "drawable", "com.viveckh.threestones");
		black_pic = getResources().getIdentifier("black_circle", "drawable", "com.viveckh.threestones");
		clear_pic = getResources().getIdentifier("clear_circle", "drawable", "com.viveckh.threestones");

		final TextView turnMsg = (TextView)findViewById(R.id.turn);

		//Initializing turn value. 0 for computer, 1 for human
		if (human_stoneColor.equals("black")) {
			turn = 1;
			turnMsg.setText("It's YOUR turn.");
		}
		else {
			turn = 0;
			turnMsg.setText("It's COMPUTER'S turn.");
		}
		createBoard();
	}

	public void createBoard() {

		//Setting up java counterparts for the android layout objects
		final Spinner stonePicker = (Spinner)findViewById(R.id.stonePicker);
		stones = getResources().getStringArray(R.array.stones);

		//Setting up adapter for stone color picking dropdown
		ArrayAdapter<String> stone_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stones);
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
				if (gameSpace.cell[i][j] == null) {
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
							if (turn == 0) { use_stone = computer_stoneColor; }
							if (turn == 1) { use_stone = human_stoneColor; }
						}

						else if (stonePicker.getSelectedItemPosition() == 1) {
							if (turn == 0) { use_stone = human_stoneColor; }
							if (turn == 1) { use_stone = computer_stoneColor; }
						}

						else { use_stone = "clear"; }

						if (turn == 0) { turnMsg.setText("It's COMPUTER'S turn. Stone Choice: " + use_stone); }
						if (turn == 1) { turnMsg.setText("It's YOUR turn. Stone Choice: " + use_stone); }
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
						System.out.println("LastX " + lastX);
						if ((lastX == 100) || (x == lastX) || (y == lastY)|| !available(buttons, lastX, lastY) ) {
							//Computer's turn
							if (turn == 0) {

								stoneFiller(buttons, gameSpace, compPlayer, x, y, use_stone);
								//Handing control to the other player
								if (stonePicker.getSelectedItemPosition() == 0) {
									use_stone = human_stoneColor;
								} else if (stonePicker.getSelectedItemPosition() == 1) {
									use_stone = computer_stoneColor;
								} else {
									use_stone = "clear";
								}

								turn = 1;
								turnMsg.setText("It's YOUR turn. Stone Choice: " + use_stone);
								remWhite.setText("Remaining White Stones: " + String.valueOf(humanPlayer.getWhite()));
								remBlack.setText("Remaining Black Stones: " + String.valueOf(humanPlayer.getBlack()));
								remClear.setText("Remaining Clear Stones: " + String.valueOf(humanPlayer.getClear()));

								// return;
							}
							else {
								//Human's turn
								stoneFiller(buttons, gameSpace, humanPlayer, x, y, use_stone);

								//Handing control to the other player
								if (stonePicker.getSelectedItemPosition() == 0) {
									use_stone = computer_stoneColor;
								} else if (stonePicker.getSelectedItemPosition() == 1) {
									use_stone = human_stoneColor;
								} else {
									use_stone = "clear";
								}

								turn = 0;

								turnMsg.setText("It's COMPUTER'S turn. Stone Choice: " + use_stone);
								remWhite.setText("Remaining White Stones: " + String.valueOf(compPlayer.getWhite()));
								remBlack.setText("Remaining Black Stones: " + String.valueOf(compPlayer.getBlack()));
								remClear.setText("Remaining Clear Stones: " + String.valueOf(compPlayer.getClear()));
							}
							//Updating the location where stone was last inserted
							lastX = x;
							lastY = y;
							humanScore.setText("Human Score: " + String.valueOf(humanPlayer.getScore()));
							computerScore.setText("Computer Score: " + String.valueOf(compPlayer.getScore()));

							// Game over
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
						}
					}
				});

				/*
				//Serialize the game to a text file
				Button serialize = (Button)findViewById(R.id.serialize);
				serialize.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						// Perform action on click
						Serialize seri = new Serialize(gameSpace, compPlayer, humanPlayer, computer_stoneColor, human_stoneColor, turn, lastX, lastY);

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
	public void stoneFiller(Button[][] buttons, Board board, Player player, int x, int y, String use_stone) {
		//Setting up the necessary flags/values in the Model/View after filling stones
		gameSpace.cell[x][y].setFilled();
		buttons[x][y].setClickable(false);
		if (use_stone.equals("white") && player.getWhite()!= 0) {
			gameSpace.cell[x][y].setStone('w');
			player.useWhite();
			buttons[x][y].setBackgroundResource(white_pic);
		}
		if (use_stone.equals("black") && player.getBlack()!= 0) {
			gameSpace.cell[x][y].setStone('b');
			player.useBlack();
			buttons[x][y].setBackgroundResource(black_pic);
		}
		if (use_stone.equals("clear") && player.getClear()!= 0) {
			gameSpace.cell[x][y].setStone('c');
			player.useClear();
			buttons[x][y].setBackgroundResource(clear_pic);
		}

		//Scoring the move
		if (human_stoneColor.equals("black")) {
			humanPlayer.addScore(gameSpace.score(x, y, 'b'));
			compPlayer.addScore(gameSpace.score(x, y, 'w'));
		}
		else {
			humanPlayer.addScore(gameSpace.score(x, y, 'w'));
			compPlayer.addScore(gameSpace.score(x, y, 'b'));
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
