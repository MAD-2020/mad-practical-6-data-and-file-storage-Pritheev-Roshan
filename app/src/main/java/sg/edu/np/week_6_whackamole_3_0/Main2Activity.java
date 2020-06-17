package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    EditText username;
    EditText password;
    Button cancelButton;
    Button createButton;
    MyDBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cancelButton = findViewById(R.id.cancelButton);
        createButton = findViewById(R.id.createButton);
        handler = new MyDBHandler(this, "WhackAMole.db", null, 1);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Creating user!");
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();
                accountCreation(inputUsername, inputPassword, handler);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Cancel user creation");
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void onStop() {
        super.onStop();
        finish();
    }

    public void accountCreation(String argUsername, String argPassword, MyDBHandler argHandler)
    {
        if (argUsername.length() != 0 && argPassword.length() != 0)
        {
            ArrayList<Integer> scoreList = new ArrayList<>();
            ArrayList<Integer> levelList = new ArrayList<>();

            UserData account = argHandler.findUser(argUsername);

            if (account == null)
            {
                for (int i = 1; i <= 10; i++)
                {
                    levelList.add(i);
                    scoreList.add(0);
                }
                argHandler.addUser(new UserData(argUsername, argPassword, levelList, scoreList));
                Log.v(TAG, FILENAME + ": New user created successfully!");
            }
            else
            {
                Log.v(TAG, FILENAME + ": User already exist during new user creation!");
                Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Your Username or Password field is empty!", Toast.LENGTH_SHORT).show();
        }
    }

}
