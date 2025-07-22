package com.example.fcmtokenapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText editUserId, editPassword;
    Button btnLogin;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUserId = findViewById(R.id.editUserId);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String userId = editUserId.getText().toString().trim();
            if (userId.isEmpty()) {
                editUserId.setError("User ID requis");
                return;
            }

            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Erreur FCM token", task.getException());
                    return;
                }

                String token = task.getResult();
                Log.d(TAG, "Token FCM : " + token);
                sendLoginRequest(userId, token);
            });
        });
    }

    private void sendLoginRequest(String userId, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.123:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        LoginRequest request = new LoginRequest(userId, token);

        apiService.login(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "Connexion réussie !");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish(); // pour ne pas revenir au login
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Erreur connexion : " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
