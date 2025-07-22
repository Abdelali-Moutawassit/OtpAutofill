package com.example.gestiondesconges.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestiondesconges.R;

public class ProfilActivity extends AppCompatActivity {

    private ImageView imageProfile;
    private TextView textNom, textEmail, textPoste, textDepartement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Liaison des vues
        imageProfile = findViewById(R.id.imageProfile);
        textNom = findViewById(R.id.textNom);
        textEmail = findViewById(R.id.textEmail);
        textPoste = findViewById(R.id.textPoste);
        textDepartement = findViewById(R.id.textDepartement);

        // Données fictives (à remplacer avec données utilisateurs réelles)
        textNom.setText("Nom : Meriam Ait Sghir");
        textEmail.setText("Email : meriam@adria.ma");
        textPoste.setText("Poste : Développeuse mobile");
        textDepartement.setText("Département : IT");

        // Tu peux charger une vraie image plus tard avec Glide/Picasso
    }
}
