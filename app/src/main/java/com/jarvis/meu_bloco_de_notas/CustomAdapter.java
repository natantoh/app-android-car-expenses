package com.jarvis.meu_bloco_de_notas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
//import android.content.Context;
//import android.widget.ArrayAdapter;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    Activity activity;
    private ArrayList book_id, book_title, book_author, prices, book_date;

    CustomAdapter(Activity activity, Context context, ArrayList book_id, ArrayList book_title, ArrayList book_author, ArrayList prices, ArrayList book_date){
        this.activity = activity;
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.prices = prices;
        this.book_date = book_date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from((context));
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // O método onBindViewHolder é um método importante em uma classe que estende RecyclerView.Adapter no Android.
        // Ele é usado para associar os dados a uma visão (item) específica dentro do RecyclerView.
        // Quando você rola a lista exibida no RecyclerView, novas visualizações (itens) são criadas ou recicladas para exibir os novos dados.
        // O onBindViewHolder é chamado toda vez que um item precisa ser exibido na tela ou atualizado conforme a rolagem.
        // Aqui está uma descrição das principais tarefas realizadas no método onBindViewHolder:

        //O parâmetro int position no método onBindViewHolder indica a posição do item na lista de dados que está sendo associado à visualização (item) atual do RecyclerView.
        // Cada item na lista de dados tem uma posição única, começando de 0 para o primeiro item, 1 para o segundo item e assim por diante.

        // Quando o método onBindViewHolder é chamado, ele indica que uma determinada visualização está sendo criada ou atualizada na tela,
        // e o parâmetro position indica qual item da lista de dados está sendo exibido naquela visualização específica.

        // Usar o position é crucial para associar os dados corretos a cada item exibido. Isso permite que você acesse os valores relevantes do
        // modelo de dados (por exemplo, o título, o autor ou o preço de um livro) na posição correspondente na lista e os atribua aos elementos visuais da visualização.

        holder.book_id_txt.setText(String.valueOf(book_id.get(position)));
        holder.book_title_txt.setText(String.valueOf(book_title.get(position)));
        holder.book_author_txt.setText(String.valueOf(book_author.get(position)));
         //DESCOMENTAR EM CASO DE ERRO DOS OUTROS ITEMS holder.prices_txt.setText(String.valueOf(prices.get(position)  ));
        // DESCOMENTAR DEPOIS holder.book_date_txt.setText(String.valueOf(book_date.get(position)));

        // Obtém a localização atual do dispositivo
        //Locale locale = getResources().getConfiguration().locale;
        //Locale locale = context.getResources().getConfiguration().locale;

        // Verifica se a localização é do Brasil
      //  if (locale.equals(new Locale("pt", "BR"))) {
            // Se for do Brasil, substitua ponto por vírgula
           // String prices_Formatado_virgula = String.valueOf(prices.get(position)).replace(".", ",");
          // holder.prices_txt.setText(prices_Formatado_virgula);
       // } else {
          //  String prices_Formatado_virgula = String.valueOf(prices.get(position)).replace(",", ".");
          //  holder.prices_txt.setText(prices_Formatado_virgula);
       // }

        //String prices = String.valueOf(prices.get(position));
        String prices_Formatado_virgula = String.valueOf(prices.get(position));
        double valor = Double.parseDouble(prices_Formatado_virgula);

        // Obtenha a localização atual ou a localização desejada
        Locale locale = Locale.getDefault(); // Localização padrão do dispositivo

        // Crie um formato de número com base na localização
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        // Para forçar a substituição de vírgulas por pontos em números decimais
        if (numberFormat instanceof DecimalFormat) {
            //((DecimalFormat) numberFormat).applyPattern("#.##"); // Padrão que substitui a vírgula pelo ponto
            ((DecimalFormat) numberFormat).applyPattern("#0.00"); // Padrão que inclui duas casas decimais
        }
        String valorFormatado = numberFormat.format(valor);

        //holder.prices_txt.setText(prices_Formatado_virgula);
        holder.prices_txt.setText(valorFormatado);

        //-------------------------INICIO SETANDO A DATA----------------------------------
        // Supondo que book_date.get(position) seja uma String no formato "2023-09-15"
        String dataNoFormatoOriginal = String.valueOf(book_date.get(position));
        try {
            // Formate a data no formato desejado para exibir para o usuario. assim na tela fica apresentando no formato 22/set/2023  enquando no banco de dados fica 2023-09-22
            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoDesejado = new SimpleDateFormat("dd/MMM/yyyy");

            Date data = formatoOriginal.parse(dataNoFormatoOriginal);
            String dataFormatada = formatoDesejado.format(data);

            // Agora, você pode definir o texto no TextView
            holder.book_date_txt.setText(dataFormatada);
        } catch (ParseException e) {
            // Lidar com exceções de formatação de data, se necessário
            e.printStackTrace();
        }
        //-------------------------FIM SETANDO A DATA----------------------------------



        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Passe os valores como extras para a UpdateActivity
                Intent intent = new Intent(context,UpdateActivity.class);
                intent.putExtra("id",String.valueOf(book_id.get(position)));
                intent.putExtra("title",String.valueOf(book_title.get(position)));
                intent.putExtra("author",String.valueOf(book_author.get(position)));
                intent.putExtra("prices",String.valueOf(prices.get(position)));
                intent.putExtra("book_date",String.valueOf(book_date.get(position)));

                //intent.putExtra("prices", (double) prices.get(position));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {

        return book_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView book_id_txt, book_title_txt, book_author_txt, prices_txt,book_date_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.book_id_txt);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            prices_txt = itemView.findViewById(R.id.prices_txt);
            book_date_txt = itemView.findViewById(R.id.book_date_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}