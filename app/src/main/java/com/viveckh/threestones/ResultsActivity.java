package com.viveckh.threestones;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * ResultsActivity Controller Class
 * This activity display the results of the game/tournament and redirects to next action based on
 * user's choice to quit/continue
 *
 * @author Vivek Pandey
 * @since 2017-04-26
 */
public class ResultsActivity extends Activity {

	//Variable Declarations, names are self explanatory
	Bundle m_extras;
	TextView m_txtViewGameResult;
	Button m_btnHumanFinalScore;
	Button m_btnComputerFinalScore;
	Button m_btnHumanWins;
	Button m_btnComputerWins;
	ImageButton m_btnYes;
	ImageButton m_btnNo;

	/**
	 * Overridden onCreate method
	 * Sets the view to the proper layout, and attaches action listeners to key view objects so
	 * that they are ready to respond to user activities
	 *
	 * @param savedInstanceState Bundle, saved instance of the game, if any
	 * @author Vivek Pandey
	 * @since 2017-04-26
	 */
	protected void onCreate(Bundle savedInstanceState) {
		//Create activity and set the view
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		//Getting the intent from previous activity
		m_extras = getIntent().getExtras();

		//Initializing and setting the view objects to reflect the scores and wins so far
		m_txtViewGameResult = (TextView) findViewById(R.id.txtViewGameResult);
		m_txtViewGameResult.setText(m_extras.getString("winnerMsg"));
		m_btnHumanFinalScore = (Button) findViewById(R.id.btnHumanFinalScore);
		m_btnHumanFinalScore.setText(String.valueOf(Tournament.GetHumanScore()));
		m_btnComputerFinalScore = (Button) findViewById(R.id.btnComputerFinalScore);
		m_btnComputerFinalScore.setText(String.valueOf(Tournament.GetComputerScore()));
		m_btnHumanWins = (Button) findViewById(R.id.btnHumanWins);
		m_btnHumanWins.setText(String.valueOf(Tournament.GetHumanWins()));
		m_btnComputerWins = (Button) findViewById(R.id.btnComputerWins);
		m_btnComputerWins.setText(String.valueOf(Tournament.GetComputerWins()));

		//Handle user choices to whether continue or end the tournament
		m_btnYes = (ImageButton) findViewById(R.id.btnYes);
		m_btnNo = (ImageButton) findViewById(R.id.btnNo);

		//If user chooses to continue the tournament, start GameActivity
		m_btnYes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), GameActivity.class);
				intent.putExtra("startMode", "new");
				intent.putExtra("humanStone", m_extras.getChar("humanStone"));
				intent.putExtra("computerStone", m_extras.getChar("computerStone"));
				startActivity(intent);
			}
		});

		//If user chooses to end the tournament, go back to the HomeActivity
		m_btnNo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Dispatch an event to go back, onKeyDown function handles what to do
				dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
			}
		});
	}

	/**
	 * Overridden onKeyDown method
	 * @param keyCode Integer, key code identifying the pressed button
	 * @param event KeyEvent, event that triggers it
	 * @return true if action successful, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-26
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//If user presses back, go to the home activity directly
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
