package softagi.ss.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.balysv.materialripple.MaterialRippleLayout;
import com.samar.chatroom.R;

public class StartActivity extends AppCompatActivity {

    MaterialRippleLayout register,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        register = findViewById(R.id.register_btn);
        login = findViewById(R.id.login_btn);

        register.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(intent);
        });
        login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        });



    }
}
