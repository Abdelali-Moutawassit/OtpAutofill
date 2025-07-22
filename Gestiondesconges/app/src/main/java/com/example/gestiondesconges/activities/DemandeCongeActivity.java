package com.example.gestiondesconges.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gestiondesconges.R;
import java.util.Calendar;

public class DemandeCongeActivity extends AppCompatActivity {

    private Spinner spinnerType;
    private EditText editTextDateDebut, editTextDateFin, editTextMotif;
    private Button btnEnvoyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_conge);

        // Initialisation
        spinnerType = findViewById(R.id.spinnerType);
        editTextDateDebut = findViewById(R.id.editTextDateDebut);
        editTextDateFin = findViewById(R.id.editTextDateFin);
        editTextMotif = findViewById(R.id.editTextMotif);
        btnEnvoyer = findViewById(R.id.btnEnvoyer);

        // Remplir les types de congé
        String[] types = {"Annuel", "Maladie", "Exceptionnel", "Sans solde"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        // Date pickers
        editTextDateDebut.setOnClickListener(v -> showDatePicker(editTextDateDebut));
        editTextDateFin.setOnClickListener(v -> showDatePicker(editTextDateFin));

        // Envoyer
        btnEnvoyer.setOnClickListener(v -> envoyerDemande());
    }

    private void showDatePicker(EditText target) {
        Calendar calendar = Calendar.getInstance();
        int annee = calendar.get(Calendar.YEAR);
        int mois = calendar.get(Calendar.MONTH);
        int jour = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
            target.setText(date);
        }, annee, mois, jour);

        dialog.show();
    }

    private void envoyerDemande() {
        String type = spinnerType.getSelectedItem().toString();
        String dateDebut = editTextDateDebut.getText().toString().trim();
        String dateFin = editTextDateFin.getText().toString().trim();
        String motif = editTextMotif.getText().toString().trim();

        if (TextUtils.isEmpty(dateDebut) || TextUtils.isEmpty(dateFin) || TextUtils.isEmpty(motif)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Envoi de la demande à un backend ou stockage local
        Toast.makeText(this, "Demande envoyée avec succès ✅", Toast.LENGTH_LONG).show();
        finish(); // retour à l'accueil
    }
}
