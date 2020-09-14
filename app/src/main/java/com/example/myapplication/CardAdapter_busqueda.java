package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter_busqueda extends RecyclerView.Adapter<CardAdapter_busqueda.PlanetHolder> {

    private Context context;
    private ArrayList<Planet> planets;

    public CardAdapter_busqueda(Context context, ArrayList<Planet> planets) {
        this.context = context;
        this.planets = planets;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout_buscar, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        Planet planet = planets.get(position);
        holder.bind(planets.get(position));
        holder.setDetails(planet);
    }

    @Override
    public int getItemCount() {
        return planets.size();
    }

    class PlanetHolder extends RecyclerView.ViewHolder {
        private TextView titulon;
        private TextView ubicacionn;
        private TextView folion;
        private TextView mensajen;
        private TextView fechan;
        private TextView text;
        private String colorn;
        private ImageView imagenn;
        private CardView estatus;
        private boolean toque=false ;


        PlanetHolder(View itemView) {
            super(itemView);
            titulon = itemView.findViewById(R.id.folioc);
            ubicacionn = itemView.findViewById(R.id.precioc);
            folion = itemView.findViewById(R.id.tarifac);
            fechan = itemView.findViewById(R.id.asientoc);


        }

        @SuppressLint("ResourceAsColor")
        void setDetails(Planet planet) {
            titulon.setText(planet.gettitulon());
            ubicacionn.setText(planet.getUbicacionn());
            folion.setText(planet.getFolion());
            fechan.setText(planet.getFechan());
            }

        void bind(final Planet employee) {


        }
    }

}

