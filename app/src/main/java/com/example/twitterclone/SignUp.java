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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername,edtEmail,edtPassword;

    private Button btnSignUp,btnLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");


        edtUsername=findViewById(R.id.edtUsernameSignUp);
        edtEmail=findViewById(R.id.edtEmailSignUp);
        edtPassword=findViewById(R.id.edtPasswordSignUp);
        btnLoginPage=findViewById(R.id.btnLoginPage);
        btnSignUp=findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
        btnLoginPage.setOnClickListener(this);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUp);
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser()!=null)
        {
            ParseUser.logOut();
//            TransitionToSocialMediaActivity();
        }


    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnSignUp:

                if(edtEmail.getText().toString().equals("")||edtUsername.getText().toString().equals("")||edtPassword.getText().toString().equals(""))
                {
                    Toast.makeText(SignUp.this,"Empty Fields not Allowed",Toast.LENGTH_SHORT).show();
                }
                else {

                    final ParseUser newUser = new ParseUser();
                    Log.i("App Credential SignUp ", "\nSign Up\nEmail : " + edtEmail.getText().toString() + "\nName : " + edtUsername.getText().toString() + "\n Password :" + edtPassword.getText().toString());
                    newUser.setUsername(edtUsername.getText().toString());
                    newUser.setPassword(edtPassword.getText().toString());
                    newUser.setEmail(edtEmail.getText().toString());


                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(SignUp.this, newUser.getUsername() + " Sign Up Successful", Toast.LENGTH_SHORT).show();

                                TransitionToSocialMediaActivity();
                            } else {
                                Toast.makeText(SignUp.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                break;

            case R.id.btnLoginPage:

                Intent intent=new Intent(SignUp.this,Login.class);
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
        Intent intent=new Intent(SignUp.this, TwitterActivity.class);
        startActivity(intent);
        finish();
    }


}