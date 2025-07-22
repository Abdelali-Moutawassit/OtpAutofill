package com.example.gestiondesconges.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gestiondesconges.R;
import com.example.gestiondesconges.models.DemandeConge;

import java.util.List;

public class DemandeAdapter extends RecyclerView.Adapter<DemandeAdapter.ViewHolder> {

    private final List<DemandeConge> demandes;

    public DemandeAdapter(List<DemandeConge> demandes) {
        this.demandes = demandes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demande, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DemandeConge d = demandes.get(position);
        holder.textType.setText(d.type);
        holder.textDates.setText(d.dateDebut + " - " + d.dateFin);
        holder.textStatus.setText(d.status);

        // Couleur selon le statut
        switch (d.status.toLowerCase()) {
            case "accepté":
                holder.textStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "refusé":
                holder.textStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                holder.textStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return demandes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textType, textDates, textStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.textType);
            textDates = itemView.findViewById(R.id.textDates);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }
}
