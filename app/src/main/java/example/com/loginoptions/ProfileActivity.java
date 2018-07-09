package example.com.loginoptions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    //view objects
    private TextView textViewUserDetail;
    private Button buttonLogout;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    String userPhone, userEmail, userName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //initializing views
        textViewUserDetail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //displaying logged in user name
        userName = user.getDisplayName();
        userEmail = user.getEmail();
        userPhone = user.getPhoneNumber();
        if (!TextUtils.isEmpty(userPhone)) {
            textViewUserDetail.setText("Welcome " + userPhone);
        }
        if (!TextUtils.isEmpty(userEmail)) {
            textViewUserDetail.setText("Welcome " + userEmail);
        }
        if (!TextUtils.isEmpty(userName)) {
            textViewUserDetail.setText("Welcome " + userName);
        }

        //adding listener to button
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if logout is pressed
                if (view == buttonLogout) {
                    //logging out the user
                    firebaseAuth.signOut();
                    //closing activity
                    finish();
                    //starting login activity
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                }
            }
        });
    }
}