package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    EditText username;
    EditText password;
    Button loginButton;
    TextView newUser;
    MyDBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        newUser = findViewById(R.id.newUser);

        handler = new MyDBHandler(this, "WhackAMole.db", null, 1);

        newUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME + ": Logging in with: " + username.getText().toString() + ": " + password.getText().toString());
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();

                boolean validity = isValidUser(inputUsername, inputPassword);

                if (validity == true)
                {
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                    intent.putExtra("Username", inputUsername);
                    intent.putExtra("Password", inputPassword);
                    startActivity(intent);
                }
                else
                {
                    Log.v(TAG, FILENAME + ": Invalid user!");
                    Toast.makeText(getApplicationContext(), "Invalid Username or/and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        UserData dbData = handler.findUser(userName);
        boolean output;
        Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
        if (dbData != null)
        {
            if (dbData.getMyUserName() == userName && dbData.getMyPassword() == password)
            {
                output = true;
            }
            else
            {
                output = false;
            }
        }
        else
        {
            output = false;
        }
        return output;
    }

}
