package com.example.tp_recette;

import static com.example.tp_recette.R.layout.activity_main;

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

public class MainActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

    }

    public void onLogin(View view) {
        // Récupération des valeurs des EditText
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        login(email,password);
    }

    public void login(String email, String password){
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:5500/v1/app/service-api/login";
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
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Login: Erreur dans onFailure", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    // Analysez le JSON pour récupérer le token
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String token = jsonResponse.getString("token"); // Remplacez "token" par le nom exact de la clé

                        // Créez un Intent pour passer à l'activité suivante
                        Intent monIntent = new Intent(MainActivity.this, EntrerIngredient.class);
                        monIntent.putExtra("TOKEN", token); // Ajoutez le token à l'intent

                        startActivity(monIntent);

                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Login: Succès " + responseData, Toast.LENGTH_SHORT).show());
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Erreur lors de l'analyse de la réponse", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Login: Erreur", Toast.LENGTH_SHORT).show());
                }
            }

        });
    }

    private void setWidget() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
    }

    public void onRegister(View view) {
        Intent monIntent = new Intent(MainActivity.this, Register.class);

        startActivity(monIntent);
    }
}