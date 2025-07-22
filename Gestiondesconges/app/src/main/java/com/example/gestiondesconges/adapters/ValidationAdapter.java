package com.example.gestiondesconges.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondesconges.R;
import com.example.gestiondesconges.models.DemandeConge;

import java.util.List;

public class ValidationAdapter extends RecyclerView.Adapter<ValidationAdapter.ViewHolder> {

    private final List<DemandeConge> demandes;
    private final Context context;

    public ValidationAdapter(Context context, List<DemandeConge> demandes) {
        this.context = context;
        this.demandes = demandes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_validation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DemandeConge demande = demandes.get(position);
        holder.textNomEmploye.setText("Employé : " + demande.type); // On suppose que type contient le nom (à changer si tu as un vrai champ)
        holder.textInfos.setText(demande.type + " | " + demande.dateDebut + " - " + demande.dateFin);

        holder.btnAccepter.setOnClickListener(v -> {
            Toast.makeText(context, "Demande acceptée ✅", Toast.LENGTH_SHORT).show();
            demandes.remove(position);
            notifyItemRemoved(position);
        });

        holder.btnRefuser.setOnClickListener(v -> {
            Toast.makeText(context, "Demande refusée ❌", Toast.LENGTH_SHORT).show();
            demandes.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return demandes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNomEmploye, textInfos;
        Button btnAccepter, btnRefuser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNomEmploye = itemView.findViewById(R.id.textNomEmploye);
            textInfos = itemView.findViewById(R.id.textInfos);
            btnAccepter = itemView.findViewById(R.id.btnAccepter);
            btnRefuser = itemView.findViewById(R.id.btnRefuser);
        }
    }
}
