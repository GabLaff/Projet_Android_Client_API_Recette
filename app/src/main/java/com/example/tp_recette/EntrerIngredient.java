package com.example.tp_recette;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EntrerIngredient extends AppCompatActivity {

    private EditText txtIncredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entrer_ingredient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();
    }

    public void Afficher_Recette(String ingredient) {
        OkHttpClient client = new OkHttpClient();

        // Récupération du token
        Intent intent = getIntent();
        String token = intent.getStringExtra("TOKEN");

        // Construire l'URL avec le paramètre "ingredient"
        String url = "http://10.0.2.2:5500/v1/app/service-api/" + token;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("ingredient", ingredient);
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(EntrerIngredient.this, "Ingredient: Erreur dans onFailure", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    Log.d("Response Data", responseData);


                    runOnUiThread(() -> {
                        // Créer un Intent pour démarrer AffichageRecette
                        Intent intent = new Intent(EntrerIngredient.this, AffichageRecette.class);
                        intent.putExtra("RECETTES_DATA", responseData); // Passer les données des recettes avec la bonne clé
                        startActivity(intent); // Démarrer l'activité
                    });
                } else {
                    // Log pour afficher le code d'erreur et le message
                    Log.e("Afficher_Recette", "Code de réponse: " + response.code() + ", Message: " + response.message());
                    runOnUiThread(() -> Toast.makeText(EntrerIngredient.this, "Erreur lors de l'obtention des recettes", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
    private void Affiher_Liste_Recette() {
        OkHttpClient client = new OkHttpClient();

        // Récupération du token
        Intent intent = getIntent();
        String token = intent.getStringExtra("TOKEN");

        // Construire l'URL avec le paramètre "ingredient"
        String url = "http://10.0.2.2:5500/v1/app/service-api/recettes/" + token;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(EntrerIngredient.this, "Erreur: Erreur dans onFailure", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    Log.d("Response Data", responseData); // Ajoutez cette ligne pour voir la réponse


                    runOnUiThread(() -> {
                        // Créer un Intent pour démarrer AffichageRecette
                        Intent intent = new Intent(EntrerIngredient.this, AffichageRecette.class);
                        intent.putExtra("RECETTES_DATA", responseData); // Passer les données des recettes avec la bonne clé
                        startActivity(intent); // Démarrer l'activité
                    });
                } else {
                    // Log pour afficher le code d'erreur et le message
                    Log.e("Afficher_Recette", "Code de réponse: " + response.code() + ", Message: " + response.message());
                    runOnUiThread(() -> Toast.makeText(EntrerIngredient.this, "Erreur lors de l'obtention des recettes", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void setWidget() {
        txtIncredient = findViewById(R.id.txtIncredient);
    }

    public void onAfficherRecette(View view) {
        // Récupération des valeurs des EditText
        String ingredient = txtIncredient.getText().toString();
        Afficher_Recette(ingredient);
    }

    public void onAfficherListeRecettes(View view) {
        Affiher_Liste_Recette();
    }


}
//gab@gmail.com


