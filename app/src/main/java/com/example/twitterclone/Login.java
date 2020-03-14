package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmailLogin,edtPasswordLogin;
    Button btnLogin,btnSignUpPage;
    private String allUsers="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");

        edtEmailLogin=findViewById(R.id.edtEmailLogin);
        edtPasswordLogin=findViewById(R.id.edtPasswordLogin);
        btnLogin=findViewById(R.id.btnLogin);
        btnSignUpPage=findViewById(R.id.btnSignUpPage);

        btnSignUpPage.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        edtPasswordLogin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    {
                        onClick(btnLogin);
                    }

                }
                return false;
            }

        });

        if(ParseUser.getCurrentUser()!=null)
        {

            Toast.makeText(Login.this," Logged Out First ",Toast.LENGTH_SHORT).show();

            TransitionToSocialMediaActivity();
        }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnLogin:


                if(edtEmailLogin.getText().toString().equals("")||edtPasswordLogin.getText().toString().equals(""))
                {
                    Toast.makeText(Login.this,"Empty Fields not Allowed",Toast.LENGTH_SHORT).show();
                }

                else {

                    Log.i("App Credential Login ", "\nLogin\nEmail : " + edtEmailLogin.getText().toString() + "\n Password :" + edtPasswordLogin.getText().toString());
                    ParseUser.logInInBackground(edtEmailLogin.getText().toString(), edtPasswordLogin.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null && user != null) {
                                Toast.makeText(Login.this, user.getUsername() + " Logged In ", Toast.LENGTH_SHORT).show();

                                TransitionToSocialMediaActivity();

                            } else {
                                Toast.makeText(Login.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                break;

            case R.id.btnSignUpPage:

                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();

                break;
        }
    }


    public void ConstraintLayoutClicked(View view)
    {
        try {
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void TransitionToSocialMediaActivity()
    {
        Intent intent=new Intent(Login.this, TwitterActivity.class);
        startActivity(intent);
        finish();
    }
}