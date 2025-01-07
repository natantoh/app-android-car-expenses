package com.jarvis.meu_bloco_de_notas;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
//import android.text.format.DateUtils;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//import java.util.UUID;
import com.google.firebase.analytics.FirebaseAnalytics;

public class UpdateActivity extends AppCompatActivity {
    EditText title_input,author_input,prices_input;
    Button update_button,delete_button;
    String id, title, author,prices,date,parcelado_nao_parcelado,hash;
    String receita_ou_despesa = "DESPESA";
    private String year_month,selectedDate;
    private String gasto_fixo = "NAO"; // Valor padrão quando "Gasto Fixo" não está marcado
    private FirebaseAnalytics mFirebaseAnalytics;
    private CalendarView calendarView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        // Inicializar o Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Registrar evento de visualização da página
        logPageViewEvent("UpdateActivity");

        title_input = findViewById(R.id.title_input2);
        author_input = findViewById(R.id.author_input2);
        prices_input = findViewById(R.id.prices_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        calendarView2 = findViewById(R.id.calendarView2);

        // Inclua o TextWatcher para formatar o preço
        //prices_input.addTextChangedListener(new CurrencyTextWatcher(prices_input));

        calendarView2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Ação a ser executada quando o usuário selecionar uma data no calendário
                String formattedMonth = String.format("%02d", month + 1); // Formata o mês com dois dígitos
                String formattedDay = String.format("%02d", dayOfMonth); // Formata o dia com dois dígitos
                selectedDate = year + "-" + formattedMonth + "-" + formattedDay;
                year_month = year + "-" + formattedMonth;
                // Chame um método para inserir 'selectedDate' no banco de dados SQLite
            }
        });

        // Primeiro chamamos isso
        getAndSetIntentData();

        // Formatar a letra em tempo real para deixar sempre as iniciais Maiúsculas-----------------
        title_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Este método é chamado antes de o texto ser alterado.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Este método é chamado quando o texto está sendo alterado.
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                StringBuilder formattedText = new StringBuilder();
                boolean capitalizeNext = true;

                for (char c : inputText.toCharArray()) {
                    if (Character.isWhitespace(c)) {
                        capitalizeNext = true;
                        formattedText.append(c);
                    } else if (capitalizeNext) {
                        formattedText.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        formattedText.append(c);
                    }
                }

                if (!inputText.equals(formattedText.toString())) {
                    // Evita recursão infinita ao definir o texto capitalizado
                    title_input.setText(formattedText.toString());
                    title_input.setSelection(formattedText.length());
                }
            }

        });
        //--------- FIM-----------------------------------------------------------------------------

        // Formatar a letra em tempo real para deixar sempre as iniciais Maiúsculas-----------------
        author_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Este método é chamado antes de o texto ser alterado.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Este método é chamado quando o texto está sendo alterado.
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                StringBuilder formattedText = new StringBuilder();
                boolean capitalizeNext = true;

                for (char c : inputText.toCharArray()) {
                    if (Character.isWhitespace(c)) {
                        capitalizeNext = true;
                        formattedText.append(c);
                    } else if (capitalizeNext) {
                        formattedText.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        formattedText.append(c);
                    }
                }

                if (!inputText.equals(formattedText.toString())) {
                    // Evita recursão infinita ao definir o texto capitalizado
                    author_input.setText(formattedText.toString());
                    author_input.setSelection(formattedText.length());
                }
            }

        });
        //--------- FIM-----------------------------------------------------------------------------

        //Definir o título da barra de ação após o método getAndSetIntentData
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);

                if (selectedDate == null || selectedDate.isEmpty()) {
                    // Obtenha o ano, mês e dia da currentDate.
                    Calendar calendar = Calendar.getInstance();

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Note que os meses são base 0, então adicionamos 1.
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                    // Formate o mês e o dia com dois dígitos
                    String formattedMonth = String.format("%02d", month);
                    String formattedDayOfMonth = String.format("%02d", dayOfMonth);
                    // Atualize selectedDate e year_month
                    selectedDate = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                    year_month = year + "-" + formattedMonth;
                }

                title = title_input.getText().toString().trim();
                author = author_input.getText().toString().trim();
                // FUNCIONAL prices = prices_input.getText().toString().trim();

                //---------INICIO FORMATAÇÃO DOS VALORES-----------------------------------------------------------------
                String prices_Formatado_virgula = prices_input.getText().toString();

                //---------FIM FORMATAÇÃO DOS VALORES-----------------------------------------------------------------

                String year2 = year_month.substring(0, 4);

                // Verifique se o título ou descrição não são vazios
                if (title.isEmpty() && author.isEmpty()) {

                    // Ambos título e descrição estão vazios
                    String textToDisplay_tit_desc = UpdateActivity.this.getString(R.string.translate_preencha_titulo_ou_descricao);
                    Toast.makeText(UpdateActivity.this,textToDisplay_tit_desc,Toast.LENGTH_SHORT).show();

                } else if (prices_Formatado_virgula.isEmpty() || Double.parseDouble(prices_Formatado_virgula) == 0.0) {

                    // Verifique se o preço é vazio ou zero
                    String textToDisplay_price = UpdateActivity.this.getString(R.string.translate_preco_nao_pode_ser_vazio_ou_zero);
                    Toast.makeText(UpdateActivity.this,textToDisplay_price,Toast.LENGTH_SHORT).show();

                } else {
                    myDB.updateData(id,
                            title,
                            author,
                            prices_Formatado_virgula,
                            selectedDate,
                            year_month,
                            year2,
                            gasto_fixo,
                            receita_ou_despesa,
                            parcelado_nao_parcelado,
                            hash
                    );
                    // Se clicar no botão add_button deve ir para página que contém todas as anotações
                    Intent intent = new Intent(UpdateActivity.this,AnotacoesSalvas.class);
                    startActivity(intent);
                }
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);

                boolean hasDuplicates = myDB.hasDuplicateHash(hash);

                boolean check_database_version = myDB.isDatabaseVersion1();

                long itemId = Long.parseLong(id); // Converte a String para um long
                boolean allColumnsNull = myDB.areAllColumnsNullById(itemId);

                //Log.d("TAG", "O valor de allColumnsNull é " + allColumnsNull);
                //Log.d("TAG", "O valor de check_database_version é " + check_database_version);

                if (allColumnsNull) {
                    confirmDialog();
                } else {
                    if (hasDuplicates) {
                        showCustomDialog();
                    } else {
                        confirmDialog();
                    }
                }
            }
        });
    }
    void getAndSetIntentData(){
        if( getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("author") && getIntent().hasExtra("prices") && getIntent().hasExtra("book_date") ){

            // Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            prices = getIntent().getStringExtra("prices");
            selectedDate = getIntent().getStringExtra("book_date");

            //Log.d("TAG", "O valor de date de update activty linha 123 eh  " + selectedDate);

            // Setting Intent Data
            title_input.setText(title);
            author_input.setText(author);
            prices_input.setText(String.valueOf(prices)); // Certifique-se de que esta linha esteja correta


            String[] dateParts = selectedDate.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Lembre-se que o mês em Java começa em 0 (janeiro é 0)
            int day = Integer.parseInt(dateParts[2]);

            int month2 = month + 1;

            String formattedMonth = String.format("%02d", month2);
            year_month = year + "-" + formattedMonth;

            calendarView2 = findViewById(R.id.calendarView2);

            // Criar um objeto Calendar e definir a data nele
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            // Definir a data no calendarView2
            calendarView2.setDate(calendar.getTimeInMillis());

            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);


            long itemId = Long.parseLong(id); // Converte a String para um long
            boolean allColumnsNull = myDB.areAllColumnsNullById(itemId);

            //Log.d("TAG", "O valor de allColumnsNull é " + allColumnsNull);

            if (allColumnsNull) {  // SOLUCAO TEMPORARIA
                // Se todas as colunas forem 1 é por que estão no database antigo. essa é uma solução temporária.
                hash = "old40ol0-old7-4old-o647-25oo78480o6o";
                parcelado_nao_parcelado = "NAO_PARCELADO";
            }else{
                hash = myDB.getHashById(id);
                parcelado_nao_parcelado = myDB.getParceladoStatusById(id);
            }
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }

    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DELETE");

        Context context = getApplicationContext();
        String textToDisplay = context.getString(R.string.translate_esta_certo_que_voce_quer_deletar);
        builder.setMessage(textToDisplay);


        Context context_yes = getApplicationContext();
        String textToDisplay_yes = context_yes.getString(R.string.translate_sim);

        builder.setPositiveButton(textToDisplay_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });

        Context context_not = getApplicationContext();
        String textToDisplay_not = context_not.getString(R.string.translate_nao);

        builder.setNegativeButton(textToDisplay_not, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        Button buttonOnlyThis = dialogView.findViewById(R.id.button_only_this);
        Button buttonAll = dialogView.findViewById(R.id.button_all);
        Button buttonThisAndFuture = dialogView.findViewById(R.id.button_this_and_future);

        buttonOnlyThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para APENAS ESTA
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
                dialog.dismiss();
            }
        });

        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para TODAS
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteRowsWithSameHash(hash);
                finish();
                dialog.dismiss();
            }
        });

        buttonThisAndFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para ESTA E PRÓXIMAS
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteRowsWithSameHashAndDate(hash,selectedDate);
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void logPageViewEvent(String pageName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, pageName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}