package com.celtica.doctor;

import java.util.ArrayList;

public class ExpandableItem {
    public String titre;
    public ArrayList<String> liste;

    public ExpandableItem(String titre, ArrayList<String> liste) {
        this.titre = titre;
        this.liste = liste;
    }
}
