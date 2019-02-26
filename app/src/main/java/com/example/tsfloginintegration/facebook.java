package com.example.tsfloginintegration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.EventListenerProxy;

public class facebook extends AppCompatActivity {
    private CallbackManager callbackManager;
    TextView profileText;
    ProgressDialog mDialog;
    ProfilePictureView pictureView;
    AccessToken accessToken;
    private String toShowText ="";
    LoginButton logoutbtn;
    Button lgOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook);

        lgOut = findViewById(R.id.realLogOut);
        callbackManager = CallbackManager.Factory.create();
        pictureView = findViewById(R.id.proPic);
        profileText = findViewById(R.id.fbProfile);
        logoutbtn = findViewById(R.id.logout_button);
        logoutbtn.setReadPermissions(Arrays.asList("email"));

        logoutbtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                mDialog = new ProgressDialog(facebook.this);
                mDialog.setMessage("Retrieving Data");
                mDialog.show();
                toShowText += "Name :" + Profile.getCurrentProfile().getName() + "\n";
                pictureView.setProfileId(loginResult.getAccessToken().getUserId());

                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                        getData(object);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields","id,email");
                request.setParameters(parameters);
                request.executeAsync();
                logoutbtn.setVisibility(View.INVISIBLE);
                lgOut.setVisibility(View.VISIBLE);
                profileText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                Toast.makeText(facebook.this, "Being Cancelled", Toast.LENGTH_SHORT).show();
                profileText.setText(null);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(facebook.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        lgOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                pictureView.setVisibility(View.INVISIBLE);
                profileText.setText(null);
                profileText.setVisibility(View.INVISIBLE);
                logoutbtn.setVisibility(View.VISIBLE);
                lgOut.setVisibility(View.INVISIBLE);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void getData(JSONObject object) {
        try {
            toShowText += "Email:" +object.getString("email")+ "\n";
//            toShowText += "BirthDate: "+ object.getString("birthday")+ "\n";
            profileText.setText(toShowText);
            pictureView.setVisibility(View.VISIBLE);
            toShowText = "";

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
