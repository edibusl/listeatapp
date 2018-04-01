package com.edibusl.listeatapp.components.start;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.jwt.JWT;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.model.datatypes.User;
import com.edibusl.listeatapp.model.repository.AppData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link StartFragment}), retrieves the data and updates the
 * UI as required.
 */
public class StartPresenter implements StartContract.Presenter {
    public static final String LOG_TAG = "StartPresenter";

    private Activity mActivityContext;
    private final AppData mAppData;
    private final StartContract.View mStartView;
    private boolean mLoginShown = false;

    public StartPresenter(@NonNull StartContract.View view, Activity activityContext) {
        mAppData = AppData.getInstance();
        mActivityContext = activityContext;
        mStartView = checkNotNull(view, "view cannot be null!");
        mStartView.setPresenter(this);
    }

    @Override
    public void start() {
        //Make auto login only once
        //Next time, do login only when clicking on the login button
        if(mLoginShown) {
            return;
        }
        mLoginShown = true;

        //If user id is saved in configs, just notify the server about login and proceed into main activity
        if(mAppData.UserRepo().getCurrentUserId() != null) {
            User user = new User();
            user.setUser_id(mAppData.UserRepo().getCurrentUserId());
            loginToServer(user);
        } else {
            //No user id is saved, authenticate the user
            authenticate();
        }
    }

    @Override
    public void authenticate() {
        Auth0 auth0 = new Auth0(mActivityContext);
        auth0.setOIDCConformant(true);
        WebAuthProvider.init(auth0)
                .withScheme("custom")
                .withScope("openid profile email")
                .withAudience(String.format("https://%s/userinfo", mActivityContext.getString(R.string.com_auth0_domain)))
                .start(mActivityContext, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull Dialog dialog) {
                        Log.e(LOG_TAG, "Authentication error");
                        mStartView.showLoginError();

                    }

                    @Override
                    public void onFailure(AuthenticationException exception) {
                        Log.e(LOG_TAG, exception.getMessage());
                        mStartView.showLoginError();
                    }

                    @Override
                    public void onSuccess(@NonNull Credentials credentials) {
                        //Parse the JWT token and get all needed profile data
                        JWT jwt = new JWT(credentials.getIdToken());
                        final User user = new User();
                        user.setUserName(jwt.getClaim("email").asString());
                        user.setName(jwt.getClaim("name").asString());
                        user.setProfileImage(jwt.getClaim("picture").asString());

                        loginToServer(user);
                    }
                });
    }

    private void loginToServer(User user) {
        mAppData.UserRepo().login(user, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mStartView.moveToGListActivity();
            }

            @Override
            public void onError(String error) {
                mStartView.showLoginError();
            }
        });
    }
}
