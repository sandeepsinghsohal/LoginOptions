package example.com.loginoptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AddPhone extends AppCompatActivity {

    EditText editPhone;
    Button done;
    String phoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);
        editPhone = findViewById(R.id.editPhone);
        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = editPhone.getText().toString();
                Toast.makeText(AddPhone.this, phoneNumber, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddPhone.this,VerificationPhoneCode.class);
                intent.putExtra("Phone",phoneNumber);
                startActivity(intent);
            }
        });
    }
}
