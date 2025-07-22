package com.example.gestiondesconges.models;

public class DemandeConge {
    public String type;
    public String dateDebut;
    public String dateFin;
    public String status;

    public DemandeConge(String type, String dateDebut, String dateFin, String status) {
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.status = status;
    }
}
