package com.viveckh.threestones;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class ResultsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
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
