package com.example.gestiondesconges.activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondesconges.R;
import com.example.gestiondesconges.adapters.DemandeAdapter;
import com.example.gestiondesconges.models.DemandeConge;

import java.util.ArrayList;
import java.util.List;

public class HistoriqueActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DemandeAdapter adapter;
    private List<DemandeConge> listeDemandes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        recyclerView = findViewById(R.id.recyclerViewHistorique);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Données fictives
        listeDemandes = new ArrayList<>();
        listeDemandes.add(new DemandeConge("Congé Annuel", "10/07/2024", "15/07/2024", "Accepté"));
        listeDemandes.add(new DemandeConge("Congé Maladie", "20/07/2024", "22/07/2024", "En attente"));
        listeDemandes.add(new DemandeConge("Exceptionnel", "01/08/2024", "02/08/2024", "Refusé"));

        adapter = new DemandeAdapter(listeDemandes);
        recyclerView.setAdapter(adapter);
    }
}
