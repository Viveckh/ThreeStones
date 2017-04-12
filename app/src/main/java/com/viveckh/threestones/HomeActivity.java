package com.viveckh.threestones;

/* Name: Vivek Pandey
 * Date: 3/15/2017
*/

/*
The Home activity extends the Activity and sets up the Home Screen of the application.
On creation, it sets up the layout to activity_start_screen which prompts the user to do the coin toss.
Depending on the toss, appropriate colors are assigned to the Computer & Human Player. And the intent is passed
to Home activity.
*/

import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;

public class HomeActivity extends Activity {
    private Spinner pick_coinFace;
    private Spinner pick_stoneColor;
    private Button toss_the_coin;
    private Button startGame;
    private TextView computerStone;
    private TextView humanStone;
    private TextView tossResult;

    private String[] coinFaces;
    private String[] stoneColors;
    private String tossWinner;
    private String human_stoneColor;
    private String computer_stoneColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initializing DropDown objects in java by getting reference from xml
        pick_coinFace = (Spinner) findViewById(R.id.pick_coinFace);
        pick_stoneColor = (Spinner) findViewById(R.id.pick_stoneColor);
        toss_the_coin = (Button) findViewById(R.id.toss_the_coin);
        startGame = (Button) findViewById(R.id.start_game);
        tossResult = (TextView) findViewById(R.id.result_of_toss);
        computerStone = (TextView) findViewById(R.id.computerStone);
        humanStone = (TextView) findViewById(R.id.imgHumanStone);

        //Setting the dropdown of picking color to invisible until human wins the toss.
        pick_stoneColor.setVisibility(View.INVISIBLE);
        humanStone.setVisibility(View.INVISIBLE);
        computerStone.setVisibility(View.INVISIBLE);

        //Initializing string arrays of coin faces and stone colors in java by getting reference from xml
        coinFaces = getResources().getStringArray(R.array.coinFaces);
        stoneColors = getResources().getStringArray(R.array.stoneColors);

        //Setting up adapter for coin face picking dropdown
        ArrayAdapter<String> coin_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, coinFaces);
        coin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pick_coinFace.setAdapter(coin_adapter);

        //Setting up adapter for stone color picking dropdown
        ArrayAdapter<String> stone_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stoneColors);
        stone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pick_stoneColor.setAdapter(stone_adapter);

        toss_the_coin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                humanStone.setVisibility(View.VISIBLE);
                computerStone.setVisibility(View.VISIBLE);
                if (randomNum(2) == pick_coinFace.getSelectedItemPosition()) {
                    //Human wins the toss
                    tossResult.setText("You win the Toss");
                    pick_stoneColor.setVisibility(View.VISIBLE);
                }
                else {
                    //Computer wins the toss
                    tossResult.setText("The Computer wins the Toss");
                    pick_stoneColor.setVisibility(View.INVISIBLE);
                    human_stoneColor = stoneColors[0];
                    humanStone.setText("Your stone : " + human_stoneColor);
                    computer_stoneColor = stoneColors[1];
                    computerStone.setText("Computer's stone : " + computer_stoneColor);
                }
            }
        });

        //Listening to stone selection
        pick_stoneColor.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                human_stoneColor = stoneColors[pick_stoneColor.getSelectedItemPosition()];
                humanStone.setText("Your stone : " + human_stoneColor);

                if (human_stoneColor == stoneColors[0]) {
                    computer_stoneColor = stoneColors[1];
                }
                else {
                    computer_stoneColor = stoneColors[0];
                }
                computerStone.setText("Computer's stone : " + computer_stoneColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    // Checks the input values and passes intent to create the next view once
    // the button is pressed.
    public void onClick(View view) {

        if ((humanStone.getVisibility() != View.VISIBLE) || (computerStone.getVisibility() != View.VISIBLE)) {
            return;
        }

        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("human_stoneColor", human_stoneColor);
        i.putExtra("computer_stoneColor", computer_stoneColor);
        startActivity(i);
    }

    public int randomNum(int size) {
        Random rand = new Random();
        return rand.nextInt(size);
    }
}
