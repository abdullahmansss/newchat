package softagi.ss.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.balysv.materialripple.MaterialRippleLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.samar.chatroom.R;

public class Login extends AppCompatActivity {

    EditText email_field, password_field;
    MaterialRippleLayout login;
    TextView register;

    String email_txt,password_txt;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_field = findViewById(R.id.email);
        password_field = findViewById(R.id.password);
        login = findViewById(R.id.login);

        register = findViewById(R.id.register);

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            email_txt = email_field.getText().toString();
            password_txt = password_field.getText().toString();
            if (TextUtils.isEmpty(email_txt)) {
                Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password_txt)) {
                Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
                return;
            }

            loginUserAccount(email_txt,password_txt);
        });

    }

    private void loginUserAccount(String email , String  password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        String taskmessage = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(), taskmessage, Toast.LENGTH_LONG).show();

                    }
                });
    }

        }



