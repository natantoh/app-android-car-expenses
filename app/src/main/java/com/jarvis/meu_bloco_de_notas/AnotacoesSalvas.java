package com.jarvis.meu_bloco_de_notas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import com.google.firebase.analytics.FirebaseAnalytics;
public class AnotacoesSalvas extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabaseHelper myDB;
    private Context context;
    ArrayList<String> book_id, book_title, book_author,book_pages,book_date,book_year_month;
    CustomAdapter customAdapter;
    private TextView txtMonth;
    private Calendar currentCalendar;
    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;

    //--------------CRIANDO O MENU E AÇÃO DE CLIQUE ------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.filtro_gastos_por_ano) {
            Intent intent = new Intent(AnotacoesSalvas.this,GastosPorAno.class);
            startActivity(intent);
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //--------------FIM DA CRIAÇÃO  DO MENU E AÇÃO DE CLIQUE ---------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotacoes_salvas);

        txtMonth = findViewById(R.id.txtMonth);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnBefore = findViewById(R.id.btnBefore);
        currentCalendar = Calendar.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        logPageViewEvent("AnotacoesSalvas");

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////////////////////////////// INICIO ANUNCIO//////////////////////////////////////////
                MobileAds.initialize(AnotacoesSalvas.this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                });

                AdRequest adRequest = new AdRequest.Builder().build();

                String adUnitId = BuildConfig.AD_UNIT_ID; // Pega o ID do anúncio conforme o build
                InterstitialAd.load(AnotacoesSalvas.this,adUnitId, adRequest,

                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                int randomIndex2 = 2;
                                if(randomIndex2 == 2)
                                {
                                    mInterstitialAd.show(AnotacoesSalvas.this);
                                }
                                //Log.i(TAG, "onAdLoaded");
                            }
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                //Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });
                /////////////////////////////// FIM ANUNCIO//////////////////////////////////////////
                Intent intent = new Intent(AnotacoesSalvas.this,AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(AnotacoesSalvas.this);

        // Obter a data atual
        Date currentDate = currentCalendar.getTime();
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH) + 1; // Os meses em Calendar são baseados em zero, então somamos 1

        // Formate o mês e o dia com dois dígitos
        String formattedMonth2 = String.format("%02d", month);
        String year_monthh = String.format(Locale.getDefault(), "%d-%s", year, formattedMonth2);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String monthText = sdf.format(currentDate);

        //System.out.println("O valor é: " + sdf);
        //Toast.makeText(AnotacoesSalvas.this,"TESTANDO.",Toast.LENGTH_SHORT).show();
        //Toast.makeText(AnotacoesSalvas.this, "Variavel sdf é: " + sdf, Toast.LENGTH_SHORT).show();
        //Toast.makeText(AnotacoesSalvas.this, "Variavel sdf é: " + sdf, Toast.LENGTH_SHORT).show();
        //Toast.makeText(AnotacoesSalvas.this, "Variavel year_monthh eh: " + year_monthh, Toast.LENGTH_SHORT).show();
        //Toast.makeText(AnotacoesSalvas.this, "Variavel year eh: " + year, Toast.LENGTH_SHORT).show();
        //Toast.makeText(AnotacoesSalvas.this, "Variavel monthText eh: " + monthText, Toast.LENGTH_SHORT).show();
        /*/
        // Inserir o valor da variável em uma mensagem de log
        Log.d("TAG", "O valor de year é " + year);
        Log.d("TAG", "O valor de month é " + month);
        Log.d("TAG", "O valor de year_monthh é " + year_monthh);
        Log.d("TAG", "O valor de formattedMonth é " + formattedMonth2);
        Log.d("TAG", "O valor de monthText é " + monthText);
        /*/

        // Converter a primeira letra para maiúscula
        monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1);
        txtMonth.setText(monthText);

        // Limpar os dados do adaptador, definindo uma lista vazia
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        book_date = new ArrayList<>();
        book_year_month = new ArrayList<>();

        customAdapter = new CustomAdapter(AnotacoesSalvas.this,AnotacoesSalvas.this, book_id, book_title,book_author,book_pages,book_date);
        recyclerView.setAdapter(customAdapter);

        storeDataInArrays_using_filter(year_monthh);

        // Formate o mês e o dia com dois dígitos
        String formattedMonth4 = String.format("%02d", month);

        // Formatar a data no formato desejado (ano-mês)
        String year_month = String.format(Locale.getDefault(), "%d-%s", year, formattedMonth4);

        //-----------------------------------------------------------------//
        double totalSum = myDB.sumPricesForYearMonth(year_month);
        if(totalSum == 0){
            TextView resultTextView3 = findViewById(R.id.result_total_apenas_do_mes);
            Context context = getApplicationContext();
            String textToDisplay = context.getString(R.string.translate_total_deste_mes);
            resultTextView3.setText(textToDisplay);
        }else {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String formattedtotalSum = decimalFormat.format(totalSum);
            TextView resultTextView3 = findViewById(R.id.result_total_apenas_do_mes);

            Context context = getApplicationContext();
            String textToDisplay = context.getString(R.string.translate_total_deste_mes_2);

            // Concatenando e definindo o texto no TextView
            String concatenatedText = textToDisplay + " " + formattedtotalSum;
            resultTextView3.setText(concatenatedText);
        }
        double totalSum2 = myDB.sumPricesForMonth(year_month);
        if(totalSum2 == 0){
           TextView resultTextView4 = findViewById(R.id.resultTextView);
           Context context = getApplicationContext();
           String textToDisplay2 = context.getString(R.string.translate_total_ate_aqui);
           resultTextView4.setText(textToDisplay2);
        }else {
            DecimalFormat decimalFormat2 = new DecimalFormat("#.00");
            String formattedtotalSum2 = decimalFormat2.format(totalSum2);
            TextView resultTextView4 = findViewById(R.id.resultTextView);

            Context context = getApplicationContext();
            String textToDisplay2 = context.getString(R.string.translate_total_ate_aqui_2);

            // Concatenando e definindo o texto no TextView
            String concatenatedText2 = textToDisplay2 + " " + formattedtotalSum2;
            resultTextView4.setText(concatenatedText2);
        }
        //-----------------------------------------------------------------//

        // customAdapter é responsável por mostrar no recyclerView os itens:
        customAdapter = new CustomAdapter(AnotacoesSalvas.this,AnotacoesSalvas.this, book_id, book_title,book_author,book_pages,book_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AnotacoesSalvas.this));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, 1);
                Date currentDate = currentCalendar.getTime();
                int year = currentCalendar.get(Calendar.YEAR);
                int month = currentCalendar.get(Calendar.MONTH) + 1; // Os meses em Calendar são baseados em zero, então somamos 1

                // Formate o mês e o dia com dois dígitos
                String formattedMonth4 = String.format("%02d", month);
                String year_month = String.format(Locale.getDefault(), "%d-%s", year, formattedMonth4);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                String monthText = sdf.format(currentDate);


                // Converter a primeira letra para maiúscula
                monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1);
                txtMonth.setText(monthText);

                // Limpar os dados do adaptador, definindo uma lista vazia
                book_id = new ArrayList<>();
                book_title = new ArrayList<>();
                book_author = new ArrayList<>();
                book_pages = new ArrayList<>();
                book_date = new ArrayList<>();
                book_year_month = new ArrayList<>();

                customAdapter = new CustomAdapter(AnotacoesSalvas.this,AnotacoesSalvas.this, book_id, book_title,book_author,book_pages,book_date);
                recyclerView.setAdapter(customAdapter);

                storeDataInArrays_using_filter(year_month);

                //-----------------------------------------------------------------//
                double totalSum = myDB.sumPricesForYearMonth(year_month);
                if(totalSum == 0){

                    TextView resultTextView3 = findViewById(R.id.result_total_apenas_do_mes);
                    Context context = getApplicationContext();
                    String textToDisplay = context.getString(R.string.translate_total_deste_mes);
                    resultTextView3.setText(textToDisplay);
                }else {
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    String formattedtotalSum = decimalFormat.format(totalSum);
                    TextView resultTextView3 = findViewById(R.id.result_total_apenas_do_mes);

                    Context context = getApplicationContext();
                    String textToDisplay = context.getString(R.string.translate_total_deste_mes_2);

                    // Concatenando e definindo o texto no TextView
                    String concatenatedText = textToDisplay + " " + formattedtotalSum;
                    resultTextView3.setText(concatenatedText);
                }
                double totalSum2 = myDB.sumPricesForMonth(year_month);
                if(totalSum2 == 0){
                    TextView resultTextView4 = findViewById(R.id.resultTextView);
                    Context context = getApplicationContext();
                    String textToDisplay2 = context.getString(R.string.translate_total_ate_aqui);
                    resultTextView4.setText(textToDisplay2);
                }else {
                    DecimalFormat decimalFormat2 = new DecimalFormat("#.00");
                    String formattedtotalSum2 = decimalFormat2.format(totalSum2);
                    TextView resultTextView4 = findViewById(R.id.resultTextView);

                    Context context = getApplicationContext();
                    String textToDisplay2 = context.getString(R.string.translate_total_ate_aqui_2);

                    // Concatenando e definindo o texto no TextView
                    String concatenatedText2 = textToDisplay2 + " " + formattedtotalSum2;
                    resultTextView4.setText(concatenatedText2);
                }
                //-----------------------------------------------------------------//
                // customAdapter é responsável por mostrar no recyclerView os itens:
                customAdapter = new CustomAdapter(AnotacoesSalvas.this,AnotacoesSalvas.this, book_id, book_title,book_author,book_pages,book_date);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AnotacoesSalvas.this));
            }
        });

        btnBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentCalendar.add(Calendar.MONTH, -1);
                Date currentDate = currentCalendar.getTime();

                int year = currentCalendar.get(Calendar.YEAR);
                int month = currentCalendar.get(Calendar.MONTH) + 1;

                // Formate o mês e o dia com dois dígitos
                String formattedMonth5 = String.format("%02d", month);
                String year_month = String.format(Locale.getDefault(), "%d-%s", year, formattedMonth5);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                String monthText = sdf.format(currentDate);
                monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1);

                txtMonth.setText(monthText);

                // Limpar os dados do adaptador, definindo uma lista vazia
                book_id = new ArrayList<>();
                book_title = new ArrayList<>();
                book_author = new ArrayList<>();
                book_pages = new ArrayList<>();
                book_date = new ArrayList<>();
                book_year_month = new ArrayList<>();

                customAdapter = new CustomAdapter(AnotacoesSalvas.this,AnotacoesSalvas.this, book_id, book_title,book_author,book_pages,book_date);
                recyclerView.setAdapter(customAdapter);

                storeDataInArrays_using_filter(year_month);

                //-----------------------------------------------------------------//
                double totalSum = myDB.sumPricesForYearMonth(year_month);
                if(totalSum == 0){
                    TextView resultTextView3 = findViewById(R.id.result_total_apenas_do_mes);
                    Context context = getApplicationContext();
                    String textToDisplay = context.getString(R.string.translate_total_deste_mes);
                    resultTextView3.setText(textToDisplay);
                }else {
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    String formattedtotalSum = decimalFormat.format(totalSum);
                    TextView resultTextView3 = findViewById(R.id.result_total_apenas_do_mes);

                    Context context = getApplicationContext();
                    String textToDisplay = context.getString(R.string.translate_total_deste_mes_2);

                    // Concatenando e definindo o texto no TextView
                    String concatenatedText = textToDisplay + " " + formattedtotalSum;
                    resultTextView3.setText(concatenatedText);
                }
                double totalSum2 = myDB.sumPricesForMonth(year_month);
                if(totalSum2 == 0){
                    TextView resultTextView4 = findViewById(R.id.resultTextView);
                    Context context = getApplicationContext();
                    String textToDisplay2 = context.getString(R.string.translate_total_ate_aqui);
                    resultTextView4.setText(textToDisplay2);
                }else {
                    DecimalFormat decimalFormat2 = new DecimalFormat("#.00");
                    String formattedtotalSum2 = decimalFormat2.format(totalSum2);
                    TextView resultTextView4 = findViewById(R.id.resultTextView);

                    Context context = getApplicationContext();
                    String textToDisplay2 = context.getString(R.string.translate_total_ate_aqui_2);

                    // Concatenando e definindo o texto no TextView
                    String concatenatedText2 = textToDisplay2 + " " + formattedtotalSum2;
                    resultTextView4.setText(concatenatedText2);
                }
                //-----------------------------------------------------------------//
                // customAdapter é responsável por mostrar no recyclerView os itens:
                customAdapter = new CustomAdapter(AnotacoesSalvas.this,AnotacoesSalvas.this, book_id, book_title,book_author,book_pages,book_date);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AnotacoesSalvas.this));
            }
        });

    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }
        void storeDataInArrays(){
            Cursor cursor = myDB.readAllData();
            if(cursor.getCount() == 0 ){
                Toast.makeText(this,"no data.",Toast.LENGTH_SHORT).show();
            }else{
                while(cursor.moveToNext()){
                    book_id.add(cursor.getString(0)); // Vai adicionando ao Array  book_id = new ArrayList<>();
                    book_title.add(cursor.getString(1));
                    book_author.add(cursor.getString(2));
                    book_pages.add(cursor.getString(3));
                    book_date.add(cursor.getString(4));
                    //book_year_month.add(cursor.getString(5));
                }
            }
        }

    void storeDataInArrays_using_filter(String year_month){
        Cursor cursor = myDB.readDataFilter(year_month);

        // Limpar os dados do adaptador, definindo uma lista vazia
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        book_date = new ArrayList<>();
        book_year_month = new ArrayList<>();

        if(cursor.getCount() == 0 ){
            //Toast.makeText(this,"no data.",Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                book_id.add(cursor.getString(0)); // Vai adicionando ao Array  book_id = new ArrayList<>();
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
                book_date.add(cursor.getString(4));
                //book_year_month.add(cursor.getString(5));
            }
        }
    }

    private void logPageViewEvent(String pageName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, pageName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
} // public class AnotacoesSalvas extends AppCompatActivity








