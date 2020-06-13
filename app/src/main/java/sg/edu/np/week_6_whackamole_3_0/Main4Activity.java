package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import javax.crypto.Cipher;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    final int[] score = {0};
    int secondsTime;
    TextView scoreText;
    MyDBHandler handler;
    String selectedLevel;
    String Username;
    UserData user;

    private void readyTimer(final int argSecondsTime){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
                Toast.makeText(getApplicationContext(), "Get ready in" + millisUntilFinished/1000 + " seconds", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                placeMoleTimer(argSecondsTime);
                readyTimer.cancel();
            }
        };
        readyTimer.start();
    }
    private void placeMoleTimer(final int argSecondsTime){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        newMolePlaceTimer = new CountDownTimer(argSecondsTime * 1000, argSecondsTime * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (argSecondsTime <= 5)
                {
                    setNewMole();
                    setNewMole();
                }
                setNewMole();
            }

            @Override
            public void onFinish() {
                newMolePlaceTimer.start();
            }
        };
    }
    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */

        Intent receiver = getIntent();
        selectedLevel = receiver.getStringExtra("Level");
        Username = receiver.getStringExtra("Username");

        secondsTime = timeSetter(selectedLevel);
        if (secondsTime <= 5)
        {
            setNewMole();
            setNewMole();
        }
        else
        {
            setNewMole();
        }
        scoreText = findViewById(R.id.scoreText);
        scoreText.setText(score[0] + "");
        readyTimer(secondsTime);
        handler = new MyDBHandler(this, "WhackAMole.db", null, 1);
        user = handler.findUser(Username);



        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            final Button currentButton = findViewById(id);
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCheck(currentButton);
                }
            });
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        if (checkButton.getText() == "*")
        {
            score[0]++;
            Log.v(TAG, FILENAME + ": Hit, score added!");
        }
        else
        {
            score[0]--;
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
        }

        if (Integer.parseInt(selectedLevel) - 1 >= 6)
        {
            setNewMole();
            setNewMole();
        }
        else
        {
            setNewMole();
        }
        scoreText.setText(score[0] + "");
    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);

        for (int i : BUTTON_IDS)
        {
            Button currentButton = findViewById(BUTTON_IDS[randomLocation]);
            currentButton.setText("0");
        }
        Button moleButton = findViewById(BUTTON_IDS[randomLocation]);
        moleButton.setText("*");

    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        newMolePlaceTimer.cancel();
        readyTimer.cancel();
        int currentScore = Integer.parseInt(scoreText.getText().toString());

        if (currentScore > user.getScores().get(Integer.parseInt(selectedLevel) - 1))
        {
            ArrayList<Integer> scoreList = user.getScores();
            scoreList.set(Integer.parseInt(selectedLevel) - 1, currentScore);
        }

    }

    public int timeSetter(String argSelectedLevel)
    {
        int timeSeconds;

        if (argSelectedLevel == "1")
        {
            timeSeconds = 10;
        }
        else if (argSelectedLevel == "2")
        {
            timeSeconds = 9;
        }
        else if (argSelectedLevel == "3")
        {
            timeSeconds = 8;
        }
        else if (argSelectedLevel == "4")
        {
            timeSeconds = 7;
        }
        else if (argSelectedLevel == "5")
        {
            timeSeconds = 6;
        }
        else if (argSelectedLevel == "6")
        {
            timeSeconds = 5;
        }
        else if (argSelectedLevel == "7")
        {
            timeSeconds = 4;
        }
        else if (argSelectedLevel == "8")
        {
            timeSeconds = 3;
        }
        else if (argSelectedLevel == "9")
        {
            timeSeconds = 2;
        }
        else
        {
            timeSeconds = 1;
        }

        return timeSeconds;
    }

}
