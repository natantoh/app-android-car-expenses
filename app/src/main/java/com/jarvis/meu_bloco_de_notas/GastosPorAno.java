package com.jarvis.meu_bloco_de_notas;

//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
import android.widget.TextView;
//import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
//import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.List;
import java.util.Locale;
//import com.google.firebase.analytics.FirebaseAnalytics;

public class GastosPorAno extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabaseHelper myDB;
    private Context context;
    private TextView txtMonth;
    private Calendar currentCalendar;
    private FirebaseAnalytics mFirebaseAnalytics;

   // Locale locale = context.getResources().getConfiguration().getLocales().get(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_por_ano);

        // Inicializar o Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Registrar evento de visualização da página
        logPageViewEvent("GastosPorAno");

        myDB = new MyDatabaseHelper(GastosPorAno.this);
        List<String> total_years = myDB.obterAnosDistintos();
        //Log.d("TAG", "O valor de total_years é:  " + total_years);


        /*/
        List<Double> vector_total_sum_for_every_years = new ArrayList<>();

        for (String year : total_years) {

            double totalGastoPorAno = myDB.sumPricesForYear(year);
            vector_total_sum_for_every_years.add(totalGastoPorAno);
        }
        /*/

        //Log.d("TAG", "O valor de resultados é:  " + vector_total_sum_for_every_years);

        // Converta os valores de ponto para vírgula


        //List<String> vector_total_sum_for_every_years_comma = new ArrayList<>();

        //Locale locale = context.getResources().getConfiguration().locale;

        /*/
        for (Double valor : vector_total_sum_for_every_years) {
              String valorComVirgula = String.format("%.2f", valor).replace(".", ",");
              vector_total_sum_for_every_years_comma.add(valorComVirgula);
        }
        /*/



        List<Double> vector_total_sum_for_every_years = new ArrayList<>();
        List<String> stringArray = new ArrayList<>();

        for (String year : total_years) {
            double totalGastoPorAno = myDB.sumPricesForYear(year);
            vector_total_sum_for_every_years.add(totalGastoPorAno);

            // Obtenha a localização atual ou a localização desejada
            Locale locale = Locale.getDefault(); // Localização padrão do dispositivo

            // Crie um formato de número com base na localização
            NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

            // Para forçar a substituição de vírgulas por pontos em números decimais
            if (numberFormat instanceof DecimalFormat) {
                //((DecimalFormat) numberFormat).applyPattern("#.##"); // Padrão que substitui a vírgula pelo ponto
                ((DecimalFormat) numberFormat).applyPattern("#0.00"); // Padrão que inclui duas casas decimais
            }
            // Formate o número e adicione à lista de strings
            String formattedValue = numberFormat.format(totalGastoPorAno);
            stringArray.add(formattedValue);
        }

            // Suponha que você tenha as listas total_years e total_sum_for_every_years preenchidas.
            CustomAdapterYears adapter = new CustomAdapterYears(context, total_years, stringArray);
            RecyclerView recyclerView = findViewById(R.id.recyclerViewYear);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use o layout manager apropriado.

    }

    private void logPageViewEvent(String pageName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, pageName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}








