package com.viveckh.threestones;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResultsActivity extends Activity {

	TextView m_txtViewGameResult;
	Button m_btnHumanFinalScore;
	Button m_btnComputerFinalScore;
	Button m_btnHumanWins;
	Button m_btnComputerWins;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		//Getting the intent from previous activity
		Bundle extras = getIntent().getExtras();

		m_txtViewGameResult = (TextView)findViewById(R.id.txtViewGameResult);
		m_txtViewGameResult.setText(extras.getString("winnerMsg"));

		m_btnHumanFinalScore = (Button)findViewById(R.id.btnHumanFinalScore);
		m_btnHumanFinalScore.setText(String.valueOf(Tournament.GetHumanScore()));

		m_btnComputerFinalScore = (Button)findViewById(R.id.btnComputerFinalScore);
		m_btnComputerFinalScore.setText(String.valueOf(Tournament.GetComputerScore()));

		m_btnHumanWins = (Button)findViewById(R.id.btnHumanWins);
		m_btnHumanWins.setText(String.valueOf(Tournament.GetHumanWins()));

		m_btnComputerWins = (Button)findViewById(R.id.btnComputerWins);
		m_btnComputerWins.setText(String.valueOf(Tournament.GetComputerWins()));
	}

	//Kill activity on pressing back button
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		//If user presses back, go to the home activity directly
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
