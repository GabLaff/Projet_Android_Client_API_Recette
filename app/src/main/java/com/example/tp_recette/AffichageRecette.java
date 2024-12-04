package com.example.tp_recette;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AffichageRecette extends AppCompatActivity {

    private TextView txtRecette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_recette);

        txtRecette = findViewById(R.id.txtRecette);
        String recettesData = getIntent().getStringExtra("RECETTES_DATA");

        if (recettesData != null) {
            try {
                JSONArray jsonArray = new JSONArray(recettesData);
                StringBuilder formattedRecettes = new StringBuilder();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject recette = jsonArray.getJSONObject(i);
                    String nom;
                    nom = recette.getString("Nom");
                    String ingredients = recette.getString("Ingredient");

                    formattedRecettes.append("Nom: ").append(nom).append("\n");
                    formattedRecettes.append("Ingrédients: ").append(ingredients).append("\n\n");
                }

                txtRecette.setText(formattedRecettes.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                txtRecette.setText("Erreur lors de l'analyse des données.");
            }
        } else {
            txtRecette.setText("Aucune recette trouvée.");
        }
    }

}
