package example.com.loginoptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Button googleButton, emailButton, phoneButton, facebookButton;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private CallbackManager mCallbackManager;
    //a constant for detecting the Google login intent result
    private static final int RC_SIGN_IN = 234;

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    String FacebookLog = "FACEBOOK LOG";
    String FBAccessToken = "FB ACCESS TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebookButton = findViewById(R.id.button_facebook_login);
        googleButton = findViewById(R.id.google_login_button);
        emailButton = findViewById(R.id.email_login_button);
        phoneButton = findViewById(R.id.phone_login_button);
        mCallbackManager = CallbackManager.Factory.create();
        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        // activity intent method
        //Then we need a GoogleSignInOptions object
        //And we need to build it as below

        //Google Login code
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //button intents
        intentMove();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("", "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            Toast.makeText(MainActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            //if getCurrentUser does not returns null
                            finish();
                            //and open profile activity
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        //if getCurrentUser does not returns null
        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();
            //and open profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }

    // move to next activity using this method
    private void intentMove() {
        LoginInWithEmail();
        LoginWithGoogle();
        LoginWithPhone();
        LoginWithFacebook();
    }


    private void LoginInWithEmail() {
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToEmail = new Intent(MainActivity.this, EmailRegistration.class);
                startActivity(moveToEmail);
            }
        });
    }

    private void LoginWithGoogle() {
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting the google signin intent
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void LoginWithPhone() {

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddPhone.class));
            }
        });

    }

    private void LoginWithFacebook() {
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(FacebookLog, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(FacebookLog, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(FacebookLog, "facebook:onError", error);
                        // ...
                    }
                });
            }
        });
    }

    //handle facebook credential with firebase
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(FBAccessToken, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FBAccessToken, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            finish();
                            //and open profile activity
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FBAccessToken, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
