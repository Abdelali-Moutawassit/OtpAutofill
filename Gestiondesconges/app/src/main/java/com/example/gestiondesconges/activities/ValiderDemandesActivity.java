package com.example.gestiondesconges.activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondesconges.R;
import com.example.gestiondesconges.adapters.ValidationAdapter;
import com.example.gestiondesconges.models.DemandeConge;

import java.util.ArrayList;
import java.util.List;

public class ValiderDemandesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ValidationAdapter adapter;
    private List<DemandeConge> demandes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valider_demandes);

        recyclerView = findViewById(R.id.recyclerViewValidation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Données fictives à valider
        demandes = new ArrayList<>();
        demandes.add(new DemandeConge("Meriam Ait Sghir", "10/07/2024", "15/07/2024", "En attente"));
        demandes.add(new DemandeConge("Khalid El Idrissi", "20/07/2024", "22/07/2024", "En attente"));

        adapter = new ValidationAdapter(this, demandes);
        recyclerView.setAdapter(adapter);
    }
}
