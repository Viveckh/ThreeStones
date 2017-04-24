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

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import org.w3c.dom.Text;

public class HomeActivity extends Activity {
    private ImageButton m_btnStartNewGame;
    private ImageButton m_btnProceedToGame;
    private ImageButton m_btnRestoreGame;
    private TextView m_labelGameName;
    private TextView m_txtViewTossResults;
    private String m_dataStorageDirectory;
    private RadioGroup m_radioGrpTeams;
    private RadioButton m_radioWhite;
    private RadioButton m_radioBlack;

    //Package public, location of internal storage
    public static File m_internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m_dataStorageDirectory = "DataFiles";
        m_btnStartNewGame = (ImageButton)findViewById(R.id.btnStartNewGame);
        m_btnProceedToGame = (ImageButton) findViewById(R.id.btnProceedToGame);
        m_btnRestoreGame = (ImageButton) findViewById(R.id.btnRestoreGame);
        m_labelGameName = (TextView) findViewById(R.id.labelGameName);
        m_txtViewTossResults = (TextView) findViewById(R.id.txtView_TossResults);
        m_radioGrpTeams = (RadioGroup)findViewById(R.id.radioGrp_Teams);
        m_radioWhite = (RadioButton)findViewById(R.id.radio_teamWhite);
        m_radioBlack = (RadioButton)findViewById(R.id.radio_teamBlack);

        m_btnProceedToGame.setVisibility(View.INVISIBLE);
        m_radioGrpTeams.setVisibility(View.INVISIBLE);

        UpdateControlViews();
        //If user chooses to start a new game, conduct toss and activate the proceed to game button
        m_btnStartNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Show only components necessary to view toss results and to proceed to a new game
                m_radioGrpTeams.setVisibility(View.VISIBLE);
                m_btnProceedToGame.setVisibility(View.VISIBLE);
                Tournament.ResetScores();
                TossToBegin();
            }
        });

        //If user chooses to proceed to the game after the toss
        m_btnProceedToGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ProceedToGame(m_btnProceedToGame);
            }
        });

        // Get the files in the given folder and display in the ListView (For restoring saved game)
        ArrayList<String> filesInFolder = GetFiles(m_dataStorageDirectory);
        final ListView listView_SerializationFiles = (ListView) findViewById(R.id.listView_SerializationFiles);
        listView_SerializationFiles.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filesInFolder));

        listView_SerializationFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Clicking on items
                String selectedItem = (String) (listView_SerializationFiles.getItemAtPosition(position));
                RestoreGame(selectedItem);
            }
        });
    }

    private void RestoreGame(String a_fileName) {
        //Read from file
        Board board = new Board();
        Serializer serializer = new Serializer(HomeActivity.m_internalStorage);
        Tournament.ResetScores();
        if (serializer.ReadFromFile(a_fileName, board)) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("startMode", "restore");
            intent.putExtra("gameBoard", board);
            Toast.makeText(getApplicationContext(), "Restoring your game...",
                    Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Corrupt file! Restore failed.",
                    Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Gets the names of files in a given directory and puts them into an array list
     *
     * @param a_directoryName Directory whose files are to be listed
     * @return ArrayList that consists of all the names of files in the directory
     */
    private ArrayList<String> GetFiles(String a_directoryName) {
        ArrayList<String> dataFiles = new ArrayList<String>();

        Context context = getApplicationContext();
        //Create the data storage folder if it doesn't exist yet
        File folder = context.getDir(a_directoryName, Context.MODE_PRIVATE);
        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        else {
            success = true;
        }
        m_internalStorage = folder;
        m_dataStorageDirectory = folder.getAbsolutePath();

        if (success) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++)
                    dataFiles.add(files[i].getName());
            }
        }
        return dataFiles;
    }

    /**
     * Starts next activity and passes necessary intents to start a fresh game
     *
     * @param view button whose click instigates the function
     */
    public void ProceedToGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("startMode", "new");
        //Next player is already passed through Tournament's static variable
        //Whichever radio button is checked is human's choice of stone
        if (m_radioWhite.isChecked()) {
            intent.putExtra("humanStone", 'w');
            intent.putExtra("computerStone", 'b');
        }
        else {
            intent.putExtra("humanStone", 'b');
            intent.putExtra("computerStone", 'w');
        }
        startActivity(intent);
    }

    /**
     * Does a toss until one side wins, refreshes view with the toss result and sets the necessary values in static Tournament class regarding which side is the next player
     */
    private void TossToBegin() {
        Random rand = new Random();
        int humanDieToss, botDieToss;

        // Continue until both have different Toss results
        do {
            humanDieToss = rand.nextInt(2);
            botDieToss = rand.nextInt(2);
        } while (humanDieToss == botDieToss);

        // Whoever has the highest number on top - wins the toss
        if (humanDieToss > botDieToss) {
            Tournament.SetControls(-1, -1, "human");
            m_txtViewTossResults.setText("Toss Results:\nBot: " + botDieToss + ", You: " + humanDieToss + "\nYou won the toss.");
        } else {
            Tournament.SetControls(-1, -1, "computer");
            m_txtViewTossResults.setText("Toss Results:\nBot: " + botDieToss + ", You: " + humanDieToss + "\nBot won the toss.");
        }
    }

    private void UpdateControlViews() {
        //Initializing fonts
        Typeface tfRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface tfCaviar = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf");
        Typeface tfSeaside = Typeface.createFromAsset(getAssets(), "fonts/SeasideResortNF.ttf");

        //Setting the fonts
        m_labelGameName.setTypeface(tfSeaside);
        m_txtViewTossResults.setTypeface(tfCaviar);
        m_radioBlack.setTypeface(tfCaviar);
        m_radioWhite.setTypeface(tfCaviar);

        //Displaying images next to the radiobuttons
        Drawable resized_white_circle = getResources().getDrawable(R.drawable.white_circle);
        Drawable resized_black_circle = getResources().getDrawable(R.drawable.black_circle);
        resized_white_circle.setBounds(0, 0, 110, 110);
        resized_black_circle.setBounds(0, 0, 110, 110);
        m_radioWhite.setCompoundDrawables(resized_white_circle, null, null, null);
        m_radioBlack.setCompoundDrawables(resized_black_circle, null, null, null);
    }
}
