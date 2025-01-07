package com.jarvis.meu_bloco_de_notas;

//import android.app.Activity;
import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
//import java.util.Locale;

public class CustomAdapterYears extends RecyclerView.Adapter<CustomAdapterYears.MyViewHolder> {
    private Context context;
    private List<String> total_years;
    private List<String> total_sum_for_every_years;

    public CustomAdapterYears(Context context, List<String> total_years, List<String> total_sum_for_every_years) {
        this.context = context;
        this.total_years = total_years;
        this.total_sum_for_every_years = total_sum_for_every_years;
    }

    // Métodos necessários do RecyclerView.Adapter

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item da lista e cria um ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_total_every_years, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Vincula os dados aos elementos de interface do usuário
        String ano = total_years.get(position);
        String resultado = total_sum_for_every_years.get(position);

        holder.textViewAno.setText(ano);
        holder.textViewResultado.setText(String.valueOf(resultado));
    }

    @Override
    public int getItemCount() {
        return total_years.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAno;
        TextView textViewResultado;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewAno = itemView.findViewById(R.id.year_txt);
            textViewResultado = itemView.findViewById(R.id.prices_total_year_txt);
        }
    }
}
