package com.example.sociallogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.example.sociallogin.GoogleActivity;

import org.json.JSONException;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView tv1;
    LoginButton fbbtn;
    Button google;
    CallbackManager c;
    String EMAIL = "email";
    GoogleSignInClient mGoogleSignInClient;

    private static int RC_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv1 = findViewById(R.id.tv1);
        fbbtn = findViewById(R.id.login_button);
        google = findViewById(R.id.googlesignin);


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GoogleActivity.class);
                startActivity(i);
            }
        });

        c = CallbackManager.Factory.create();

        fbbtn.setPermissions(Arrays.asList(EMAIL, "user_birthday"));

        LoginManager.getInstance().registerCallback(c, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        c.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker att = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                tv1.setText("");
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            } else {
                loaduserProfile(currentAccessToken);
            }
        }
    };

    private void loaduserProfile(AccessToken currentAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, ((object, response) -> {

            if (object != null) {
                try {
                    String email = object.getString("email");
                    String id = object.getString("id");
                    tv1.setText(email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }));


        Bundle parameter = new Bundle();
        parameter.putString("fields", "email, id");
        request.setParameters(parameter);
        request.executeAsync();


    }


}







