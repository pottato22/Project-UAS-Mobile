package id.ac.umn.jameschristianwira;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String DB_NAME = "credential.db";
    EditText edtUsername, edtPassword;
    String username, password;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Log in");


        btnLogin = findViewById(R.id.main_btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyLogin()) {
                    Log.i("Login Process", "Username and password match");
                    Log.i("Login Process", "Changing intent");
                    makeToast("Login success");
                    //Start change intent here

                    Intent intent = new Intent(MainActivity.this, DataActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    Log.i("Login Process", "Username and password don't match");
                    makeToast("Username and password don't match");
                }
            }
        });
    }

    public boolean verifyLogin() {
        //Toast.makeText(MainActivity.this, "Login button clicked", Toast.LENGTH_SHORT).show();

        Log.i("Login Process", "Get username and password from db");
        DBAdapter dbAdapter = new DBAdapter(getApplicationContext(), DB_NAME, null, 1);
        dbAdapter.openDatabase();

        Cursor cursor = dbAdapter.getCredential();

        cursor.moveToFirst();

        username = cursor.getString(0);
        password = cursor.getString(1);

        dbAdapter.close();

        Log.i("Login Process", "Comparing input and data from db");

        String inputUser = getUserInput("username");
        String inputPass = getUserInput("password");

        return inputUser.equals(username) && inputPass.equals(password);
    }

    public String getUserInput(String text) {
        String result = "";

        if (text.equals("username")) {
            edtUsername = findViewById(R.id.login_edit_username);
            result = edtUsername.getText().toString();
        }
        if (text.equals("password")) {
            edtPassword = findViewById(R.id.login_edit_password);
            result = edtPassword.getText().toString();
        }

        return result;
    }

    public void makeToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0){
            makeToast("You are not connected to internet");
        }
        if(resultCode == 1){
            finish();
        }
        if(resultCode == 2){
            makeToast("You have been logged out");
            edtUsername.setText("");
            edtPassword.setText("");
        }
    }
}
