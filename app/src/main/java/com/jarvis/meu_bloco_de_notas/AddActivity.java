package com.jarvis.meu_bloco_de_notas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AddActivity extends AppCompatActivity {
    EditText title_input, author_input,prices_input;
    Button add_button;
    private CalendarView calendarView;
    private String year_month,selectedDate;
    private String gasto_fixo = "NAO"; // Valor padrão quando "Gasto Fixo" não está marcado
    String receita_ou_despesa = "DESPESA";
    String parcelado_nao_parcelado = "NAO_PARCELADO";
    String yearString;
    private String numero_parcelas = "1";
    private Locale userLocale;
    private Context context;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        prices_input = findViewById(R.id.prices_input);
        add_button = findViewById(R.id.add_button);
        calendarView = findViewById(R.id.calendarView);


        // Inicializar o Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Registrar evento de visualização da página
        logPageViewEvent("AddActivity");


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

        RadioButton parcelarCheckbox = findViewById(R.id.checkbox2);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        TextInputLayout parcelarInputLayout = findViewById(R.id.parcelar_input_layout);

        EditText parcelar_input = findViewById(R.id.parcelar_input);

        parcelar_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método não é necessário para o seu caso, mas precisa ser implementado
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método é chamado quando o texto está sendo alterado
                numero_parcelas = charSequence.toString();
                //Log.d("TAG", "numero_parcelas é " + numero_parcelas);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Este método não é necessário para o seu caso, mas precisa ser implementado
            }
        });

        parcelarCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView2, boolean isChecked2) {
                if (isChecked2) {
                    // Quando o CheckBox "Parcelar" é marcado, torne a opção visível
                    parcelarInputLayout.setVisibility(View.VISIBLE);
                } else {
                    parcelarInputLayout.setVisibility(View.GONE);
                    numero_parcelas = "1";
                }
            }
        });

        /*/
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Log.d(TAG, "changed");
                if (checkedId == R.id.checkbox1) {
                    //Log.d(TAG, "A");
                    parcelarInputLayout.setVisibility(View.GONE);
                    numero_parcelas = "1";
                    gasto_fixo = "NAO";
                }
                if (checkedId == R.id.checkbox2) {
                    //Log.d(TAG, "A");
                    parcelarInputLayout.setVisibility(View.VISIBLE);

                    numero_parcelas = parcelar_input.getText().toString();

                    gasto_fixo = "NAO";
                }
            }
        });
        /*/

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String formattedMonth = String.format("%02d", month + 1);
                String formattedDay = String.format("%02d", dayOfMonth);
                selectedDate = year + "-" + formattedMonth + "-" + formattedDay;
                year_month = year + "-" + formattedMonth;
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);

                if (selectedDate == null || selectedDate.isEmpty()) {
                    // Obtenha o ano, mês e dia da currentDate.
                    Calendar calendar = Calendar.getInstance();

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Note que os meses são base 0, então adicionamos 1.
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                    // Formate o mês e o dia com dois dígitos
                    String formattedMonth = String.format("%02d", month);
                    String formattedDayOfMonth = String.format("%02d", dayOfMonth);

                    selectedDate = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                    year_month = year + "-" + formattedMonth;

                }

                // Remova todas as vírgulas e o prefixo "R$" ( Somente para Brazil )
                //      String priceText = prices_input.getText().toString().replaceAll("[R$,$]", "");
                //     String priceText2 = priceText.replaceAll("[,]", ".");

                String title_2 = title_input.getText().toString().trim();
                String author_2 = author_input.getText().toString().trim();
                String priceText44 = prices_input.getText().toString();
                String year2 = year_month.substring(0, 4);


                // Criando o Hash único
                UUID uuid = UUID.randomUUID();
                String hash = uuid.toString();

                //numero_parcelas = parcelar_input.getText().toString();
                //Log.d("TAG", "numero_parcelas é " + numero_parcelas);
                //Log.d("TAG", "gastoFixo é " + gasto_fixo);

                int numParcelas = Integer.parseInt(numero_parcelas);

                //int numParcelas = 6;

                //Log.d("TAG", String.format("numParcelas é %d", numParcelas)); // Usando String.format() para formatar a string

                // Verifique se o título ou descrição não são vazios
                if (title_2.isEmpty()) {

                    // título está vazio
                    String textToDisplay_tit = AddActivity.this.getString(R.string.translate_preencha_titulo);
                    Toast.makeText(AddActivity.this, textToDisplay_tit, Toast.LENGTH_SHORT).show();
                }
                else if(title_2.isEmpty() && author_2.isEmpty()) {

                    // Ambos título e descrição estão vazios
                    String textToDisplay_tit_desc = AddActivity.this.getString(R.string.translate_preencha_titulo_ou_descricao);
                    Toast.makeText(AddActivity.this, textToDisplay_tit_desc, Toast.LENGTH_SHORT).show();

                } else if (priceText44.isEmpty() || Double.parseDouble(priceText44) == 0.0) {

                    // Verifique se o preço é vazio ou zero
                    String textToDisplay_price = AddActivity.this.getString(R.string.translate_preco_nao_pode_ser_vazio_ou_zero);
                    Toast.makeText(AddActivity.this, textToDisplay_price, Toast.LENGTH_SHORT).show();

                } else if (numero_parcelas.isEmpty() || Double.parseDouble(numero_parcelas) == 0.0) {

                        String textToDisplay_tit = AddActivity.this.getString(R.string.translate_numero_de_parcela_nao_pode_ser_zero);
                        Toast.makeText(AddActivity.this, textToDisplay_tit, Toast.LENGTH_SHORT).show();
                } else if (numParcelas > 1000) {

                        String textToDisplay_tit = AddActivity.this.getString(R.string.translate_o_numero_de_parcela_nao_pode_ser_maior_que_mil);
                        Toast.makeText(AddActivity.this, textToDisplay_tit, Toast.LENGTH_SHORT).show();

                } else {
                    // Certifique-se de que numeros_de_parcela_input seja um número inteiro válido
                    if (numParcelas != 1) {
                        try {
                            parcelado_nao_parcelado = "PARCELADO";
                            // Crie uma cópia da data inicial para evitar modificá-la diretamente
                            Calendar calendar = Calendar.getInstance();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date dataInicial = sdf.parse(selectedDate);
                            calendar.setTime(dataInicial);

                            // Use um loop for para adicionar o livro várias vezes
                            for (int i = 0; i < numParcelas; i++) {
                                // Ajuste o título do livro adicionando a descrição da parcela
                                String titulo = title_2 + " " + (i + 1) + "/" + numParcelas;

                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1; // Note que os meses são base 0, então adicionamos 1.
                                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                                // Formate o mês e o dia com dois dígitos
                                String formattedMonth = String.format("%02d", month);
                                String formattedDayOfMonth = String.format("%02d", dayOfMonth);

                                selectedDate = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                                year_month = year + "-" + formattedMonth;

                                yearString = String.valueOf(year);

                                myDB.addBook(
                                        titulo,
                                        author_2,
                                        priceText44,
                                        selectedDate,
                                        year_month,
                                        yearString,
                                        gasto_fixo,
                                        receita_ou_despesa,
                                        parcelado_nao_parcelado,
                                        hash
                                );

                                // Incremente a data em um mês para a próxima parcela
                                calendar.add(Calendar.MONTH, 1);

                            }
                        } catch (NumberFormatException e) {
                            // Lidar com a situação em que a conversão falhou (entrada inválida)
                            // Por exemplo, mostre uma mensagem de erro ao usuário
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        myDB.addBook(
                                title_2,
                                author_2,
                                priceText44,
                                selectedDate,
                                year_month,
                                year2,
                                gasto_fixo,
                                receita_ou_despesa,
                                parcelado_nao_parcelado,
                                hash
                        );
                    }

                    Intent intent = new Intent(AddActivity.this, AnotacoesSalvas.class);
                    startActivity(intent);
                }
            }
        });
    } // fim Oncreate

    private String capitalizeWords(String text) {
        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        if (result.length() > 0) {
            // Remove espaço extra no final, se houver
            result.setLength(result.length() - 1);
        }
        return result.toString();
    }


    private void logPageViewEvent(String pageName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, pageName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

} // fim AddActivity






