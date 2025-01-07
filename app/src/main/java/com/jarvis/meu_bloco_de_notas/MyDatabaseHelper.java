package com.jarvis.meu_bloco_de_notas;

//import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; // Classe que contém os métodos de manipulação dos dados no banco, por isso temos que importar
import android.database.sqlite.SQLiteOpenHelper; // Classe responsável pela criação do banco e também responsável pelo versionamento do mesmo.
//import android.icu.text.CaseMap;
import android.widget.Toast;
import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

class MyDatabaseHelper extends SQLiteOpenHelper {  // Primeiro passo então, é herdar todos os métodos da classe SQLiteOpenHelper, para acessar seus métodos.
    // Defina as constantes para nome do banco, versão, tabelas, etc.
    private Context context;
    private static final String DATABASE_NAME = "Banco_De_Dados_Meu_Controle_Financeiro.db";

    //Certifique-se de aumentar o número de versão sempre que fizer alterações no esquema do banco de dados que precisem
    //ser refletidas em versões futuras do aplicativo. Isso permitirá que os usuários atualizem o aplicativo sem perder os dados existentes.
    private static final int DATABASE_VERSION = 2; // Aumente o número da versão

    public static final String TABLE_NAME = "my_library"; //  Nome do Banco de Dados.db
    private static final String COLUMN_ID = "_id"; // id da coluna, que será automaticamente incrementado.
    public  static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PRICES = "prices";
    public static final String COLUMN_DATE = "date"; // Nome da coluna será date
    public static final String COLUMN_YEAR_MONTH = "year_month";  // nome da coluna, será year_month
    public static final String COLUMN_YEAR = "year";  // nome da coluna, será year
    public static final String COLUMN_GASTO_FIXO = "gasto_fixo";
    public static final String COLUMN_HASH = "hash";
    public static final String COLUMN_DESPESA_OU_RECEITA = "Receita_ou_despesa";
    public static final String COLUMN_PARCELADO = "parcelado_ou_nao_parcelado";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Ao estender sua classe SQLiteOpenHelper, o Android Studio obriga o desenvolvedor a implementar dois métodos que são
    // de suma importância para o correto funcionamento da criação do banco de dados:

    // Método onCreate(): é chamado quando a aplicação cria o banco de dados pela primeira vez.
    // Nesse método devem ter todas as diretrizes de criação e população inicial do banco.

    // Método onUpgrade(): é o método responsável por atualizar o banco de dados com alguma informação estrutural
    // que tenha sido alterada. Ele sempre é chamado quando uma atualização é necessária, para não ter nenhum tipo
    // de inconsistência de dados entre o banco existente no aparelho e o novo que a aplicação irá utilizar.

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crie as tabelas conforme necessário
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PRICES + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_YEAR_MONTH + " TEXT, " +
                COLUMN_YEAR + " TEXT, " +
                COLUMN_GASTO_FIXO + " TEXT, " +
                COLUMN_DESPESA_OU_RECEITA + " TEXT, " +
                COLUMN_PARCELADO + " TEXT, " +
                COLUMN_HASH + " TEXT);";
        db.execSQL(query);
    }

    /*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Lide com atualizações do banco, se necessário
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Este trecho de código está excluindo a tabela especificada (TABLE_NAME) se ela existir. Em outras palavras, ele está descartando a tabela inteira e todos os dados dentro dela. Isso é geralmente usado quando você precisa fazer uma atualização de banco de dados que envolve uma mudança significativa na estrutura da tabela ou quando você deseja recriar a tabela do zero. No entanto, isso resultará na perda de todos os dados existentes na tabela.
        onCreate(db); // Após excluir a tabela (se ela existir), este código chama o método onCreate para criar novamente a tabela de acordo com a definição fornecida dentro do método onCreate. Isso efetivamente cria uma nova tabela no banco de dados, substituindo a tabela antiga que foi excluída na etapa anterior.
    }
    /*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Se a versão antiga for menor que 2, adicione todas as novas colunas
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_GASTO_FIXO + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_DESPESA_OU_RECEITA + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PARCELADO + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_HASH + " TEXT;");

        }
    }

    void addBook(String title, String author, String prices, String date, String year_month, String year, String gasto_fixo,String receita_ou_despesa, String parcelado, String hash){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_AUTHOR,author);
        cv.put(COLUMN_PRICES,prices);
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_YEAR_MONTH,year_month);
        cv.put(COLUMN_YEAR,year);
        cv.put(COLUMN_GASTO_FIXO,gasto_fixo);
        cv.put(COLUMN_DESPESA_OU_RECEITA,receita_ou_despesa);
        cv.put(COLUMN_PARCELADO,parcelado);
        cv.put(COLUMN_HASH,hash);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){

            String textToDisplay_falhou = context.getString(R.string.translate_falhou);
            Toast.makeText(context,textToDisplay_falhou,Toast.LENGTH_SHORT).show();
        }
        /*/
        else {
            String textToDisplay_added_sucesso = context.getString(R.string.translate_adicionado_com_sucesso);
            Toast.makeText(context,textToDisplay_added_sucesso,Toast.LENGTH_SHORT).show();
        }
        /*/
    }

    Cursor readAllData(){
            String query = "SELECT * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = null;
            if(db != null){
                cursor = db.rawQuery(query,null);
            }
            return cursor;
        }

    Cursor readDataFilter(String targetDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        //String query = "SELECT * FROM " + TABLE_NAME + " WHERE year_month = ?";
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE year_month = ? ORDER BY date ASC";

        String[] selectionArgs = { targetDate };

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, selectionArgs);
        }
        return cursor;
        //O cursor retornado pelo método readDataFilter contém os resultados da consulta SQL que seleciona todas
        //as colunas da tabela especificada (TABLE_NAME) onde o valor da coluna year_month corresponde ao valor passado como targetDate.

        //O cursor contém os registros que atendem aos critérios de filtragem definidos na consulta SQL. Cada registro
        //do cursor representa uma linha da tabela, e você pode iterar sobre o cursor para acessar os valores de cada coluna desse registro.
    }

    void updateData(String row_id, String title, String author, String prices, String date, String year_month,String year,String gasto_fixo,String receita_ou_despesa, String parcelado, String hash){
        // pages foi mudado para String pages ou int pages etc.. agora double pages

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_AUTHOR,author);
        cv.put(COLUMN_PRICES,prices);
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_YEAR_MONTH,year_month);
        cv.put(COLUMN_YEAR,year);
        cv.put(COLUMN_GASTO_FIXO,gasto_fixo);
        cv.put(COLUMN_DESPESA_OU_RECEITA,receita_ou_despesa);
        cv.put(COLUMN_PARCELADO,parcelado);
        cv.put(COLUMN_HASH,hash);

        long result = db.update(TABLE_NAME,cv,"_id=?",new String[]{row_id});
        if ( result == -1 ){

            //Context context = getApplicationContext();
            String textToDisplay6 = context.getString(R.string.translate_falha_ao_atualizar);

            Toast.makeText(context,textToDisplay6, Toast.LENGTH_SHORT).show();
        }
        /*/
        else{

            String textToDisplay7 = context.getString(R.string.translate_atualizado_com_sucesso);
            Toast.makeText(context,textToDisplay7, Toast.LENGTH_SHORT).show();
        }
        /*/
}
    public void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,"_id=?", new String[]{row_id});
        if ( result == -1){

            String textToDisplay8 = context.getString(R.string.translate_falha_ao_deletar);
            Toast.makeText(context, textToDisplay8, Toast.LENGTH_SHORT).show();
        }
        else{

            String textToDisplay9 = context.getString(R.string.translate_deletado_com_sucesso);
            Toast.makeText(context, textToDisplay9, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isDatabaseVersion1() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.getVersion() == 1;
    }


    public boolean areAllColumnsNullById(long itemId) {

        // Usar essa função para preencher os databases mais antigos,pois tem 22 usuarios utilizando. e está dando erro ao apagar a função.

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"gasto_fixo", "Receita_ou_despesa", "parcelado_ou_nao_parcelado", "hash"};
        String selection = "_id=?";
        String[] selectionArgs = {String.valueOf(itemId)};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean allColumnsNull = true;

        if (cursor.moveToFirst()) {
            for (String columnName : columns) {
                int columnIndex = cursor.getColumnIndex(columnName);
                if (!cursor.isNull(columnIndex)) {
                    allColumnsNull = false;
                    break;
                }
            }
        }

        cursor.close();
        return allColumnsNull;
    }


    public void deleteRowsWithSameHash(String hash) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "hash=?", new String[]{hash});
        if (result == -1) {
            String textToDisplay8 = context.getString(R.string.translate_falha_ao_deletar);
            Toast.makeText(context, textToDisplay8, Toast.LENGTH_SHORT).show();
        } else {
            String textToDisplay9 = context.getString(R.string.translate_deletado_com_sucesso);
            Toast.makeText(context, textToDisplay9, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasDuplicateHash(String hashValue) {
        // pega os hash que estao duplicados e retorna true se achar algum duplicado, isso é para verificar se o item esta parcelado.
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"_id"};
        String selection = "hash=?";
        String[] selectionArgs = {hashValue};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int rowCount = cursor.getCount();
        cursor.close();

        // Se rowCount for maior que 1, existem duplicatas
        return rowCount > 1;
    }

    public String getHashById(String rowId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"hash"}; // Colunas que você deseja recuperar

        Cursor cursor = db.query(TABLE_NAME, columns, "_id=?", new String[]{rowId}, null, null, null);
        String hash = "";

        if (cursor.moveToFirst()) {
            hash = cursor.getString(cursor.getColumnIndex("hash"));
        }

        cursor.close();
        return hash;
    }

    public String getParceladoStatusById(String rowId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"parcelado_ou_nao_parcelado"}; // Coluna que você deseja recuperar

        Cursor cursor = db.query(TABLE_NAME, columns, "_id=?", new String[]{rowId}, null, null, null);
        String parceladoStatus = "";

        if (cursor.moveToFirst()) {
            parceladoStatus = cursor.getString(cursor.getColumnIndex("parcelado_ou_nao_parcelado"));
        }

        cursor.close();
        return parceladoStatus;
    }

    public void deleteRowsWithSameHashAndDate(String hash, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = "hash=? AND date >= ?";
        String[] selectionArgs = {hash, date};

        long result = db.delete(TABLE_NAME, selection, selectionArgs);

        if (result == -1) {
            String textToDisplay8 = context.getString(R.string.translate_falha_ao_deletar);
            Toast.makeText(context, textToDisplay8, Toast.LENGTH_SHORT).show();
        } else {
            String textToDisplay9 = context.getString(R.string.translate_deletado_com_sucesso);
            Toast.makeText(context, textToDisplay9, Toast.LENGTH_SHORT).show();
        }
    }

    public double sumPricesForYearMonth(String yearMonth) {
        SQLiteDatabase db = this.getReadableDatabase();
        double sum = 0;

        // Crie a consulta SQL para somar os preços para o ano e mês especificados
        String query = "SELECT SUM(prices) FROM my_library WHERE year_month = ?";

        Cursor cursor = db.rawQuery(query, new String[]{yearMonth});

        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }
        cursor.close();
        db.close(); // Feche o banco de dados quando terminar de usá-lo

        return sum;
    }

    public double sumPricesForMonth(String yearMonth) {
        SQLiteDatabase db = this.getReadableDatabase();
        double sum2 = 0;

        // Crie a consulta SQL para somar os preços para o ano e mês especificados
        String query = "SELECT SUM(prices) FROM my_library WHERE year_month <= ?";

        Cursor cursor = db.rawQuery(query, new String[]{yearMonth});

        if (cursor.moveToFirst()) {
            sum2 = cursor.getDouble(0);
        }
        cursor.close();
        db.close(); // Feche o banco de dados quando terminar de usá-lo

        return sum2;
    }

    public double sumPricesForYear(String year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double sum_total_year = 0;

        // Crie a consulta SQL para somar os preços para o ano e mês especificados
        String query = "SELECT SUM(prices) FROM my_library WHERE year = ?";

        Cursor cursor = db.rawQuery(query, new String[]{year});

        if (cursor.moveToFirst()) {
            sum_total_year = cursor.getDouble(0);
        }
        cursor.close();
        db.close(); // Feche o banco de dados quando terminar de usá-lo

        return sum_total_year;
    }


    //-------FILTRANDO POR TODOS OS ANOS ------//

    // Método para obter anos distintos
    public List<String> obterAnosDistintos() {
        List<String> anos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obter anos distintos
        String query = "SELECT DISTINCT year FROM my_library ORDER BY year";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String ano = cursor.getString(cursor.getColumnIndex("year"));
            anos.add(ano);
        }
        cursor.close();
        db.close();
        return anos;
    }
    //------- FIM FILTRANDO POR TODOS OS ANOS ------//
}//class MyDatabaseHelper extends SQLiteOpenHelper