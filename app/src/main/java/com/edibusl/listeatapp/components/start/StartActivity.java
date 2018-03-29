package com.edibusl.listeatapp.components.start;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.jwt.JWT;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.model.datatypes.User;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login();
    }

    private void login() {
        Auth0 auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);
        WebAuthProvider.init(auth0)
                .withScheme("https")
                .withScope("openid profile email")
                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
                .start(StartActivity.this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull Dialog dialog) {
                        StartActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //StartActivity.showError(StartActivity.this, "Error", "Authentication error");
                            }
                        });
                    }

                    @Override
                    public void onFailure(AuthenticationException exception) {
                        StartActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //StartActivity.showError(StartActivity.this, "Error", "Authentication error");
                            }
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull Credentials credentials) {
                        //TODO - After the expires date passes, we should relogin the user with "refreshToken" method
                        //Date expires = credentials.getExpiresAt();

                        //Parse the JWT token and get all needed profile data
                        JWT jwt = new JWT(credentials.getIdToken());
                        final User user = new User();
//                        user.FBUserId = jwt.getSubject();
//                        user.FBFirstName = jwt.getClaim("name").asString();
//                        user.FBName = jwt.getClaim("name").asString();
//                        user.FBUsername = jwt.getClaim("email").asString();
//                        user.FBImgUrl = jwt.getClaim("picture").asString();

                    }
                });
    }
}
