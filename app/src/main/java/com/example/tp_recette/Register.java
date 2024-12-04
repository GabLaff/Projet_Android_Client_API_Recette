package com.example.tp_recette;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();
    }

    public void onRegister(View view) {
        // Récupération des valeurs des EditText
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        register(email,password);

    }

    public void register(String email, String password){
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:5500/v1/app/service-api/register";
        //String url = "http://localhost:5500/v1/app/service-api/login";

        //Créer un objet Json
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        }catch (Exception e){
            e.printStackTrace();
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Register.this, "Login: Erreur dans onFailure", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    Intent monIntent = new Intent(Register.this, MainActivity.class);
                    startActivity(monIntent);
                    runOnUiThread(() ->Toast.makeText(Register.this, "Register: Succes " + responseData, Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() ->Toast.makeText(Register.this, "Login: Erreur", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


    private void setWidget() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPasswordRegister);
    }
}