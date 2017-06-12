package com.example.richa.kuromasu3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Game extends AppCompatActivity {

//  int[][] matrizInicial = {{1,2,0}, {1, 1, 1}};
    private int[][] matrizInicial;  // 0 y los valores de las casillas no tocables
    private int[][] matrizEstado;   // N's y B's con los numeros de las casillas no tocables
    private char[][] matrizSavedMap; // matriz que viene desde el save
    private Button[][] botones;
    private int rows =2, columns =3, levelId;
    private LinearLayout layoutPrincipal;
    private Button btnRestartGame, btnSaveGame;
    private int screenWidth, screeHeight;
    private String gameMode;


    private int linearLayoutWidth;

    //gris=0, negro=1, blanco=2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        IniDisplay();
        //BUTTONS
        btnRestartGame = (Button) findViewById(R.id.btnRestartGame);
        btnSaveGame = (Button) findViewById(R.id.btnSaveGame);
        btnSaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveGame();
            }
        });

        //TABLERO
        gameMode = GetMode();
        Log.d("MODE", gameMode);
        rows = GetRowns();
        columns = GetColumns();
        levelId = GetMapId();

        matrizEstado = new int[rows][columns];
        matrizSavedMap = new char[rows][columns];
        botones = new Button[rows][columns];


        matrizInicial = GetInitialMap();
        matrizEstado = matrizInicial;
        if(gameMode.equals("CONTINUE")){
            LoadSavedGame("save0"+levelId);
            matrizEstado = GetCurrentSavedMap();
        }
        Log.d("MATRIZINICIAL","matrizInicial");
        PrintMatrizInt(matrizInicial);
        Log.d("MATRIZINICIAL","matrizEstado");
        PrintMatrizInt(matrizEstado);
        Log.d("MATRIZINICIAL","matrizSave");
        PrintMatrizChar(matrizSavedMap);


//

        layoutPrincipal=(LinearLayout) this.findViewById(R.id.lLTablero);

        CrearTablero();
        PintarTablero();

    }
    private void IniDisplay(){
        // DISPLAY
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screeHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }
    private String GetMode(){
        Intent in = getIntent();
        return in.getStringExtra("MODE");
    }
    private int GetColumns(){
        Intent in = getIntent();
        return in.getIntExtra("MapaColumnas", 99 );
    }
    private int GetRowns(){
        Intent in = getIntent();
        return in.getIntExtra("MapaFilas", 99 );
    }
    private int GetMapId(){
        Intent in = getIntent();
        return in.getIntExtra("LevelId", 99 );
    }
    private int[][] GetInitialMap(){
        Intent in = getIntent();
        int[][] inMatrix = null;
        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("InitialMap");
        if(objectArray!=null){
            inMatrix = new int[objectArray.length][];
            for(int i=0;i<objectArray.length;i++){
                inMatrix[i]=(int[]) objectArray[i];
            }
        }
        return inMatrix;
    }
    private char[][] GetSavedMap(){
        char[][] inMatrix = null;
        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("SaveMap");
        if(objectArray!=null){
            inMatrix = new char[objectArray.length][];
            for(int i=0;i<objectArray.length;i++){
                inMatrix[i]=(char[]) objectArray[i];
                Log.d("CHAR",inMatrix[i] +"");
            }
        }
        return inMatrix;
    }

    private int[][] GetCurrentSavedMap(){
        int[][] tmp = new int[rows][columns];
        for (int i = 0; i< rows; i++){
            for (int j = 0; j< columns; j++){
                if(matrizSavedMap[i][j] == 'N'){
                    tmp[i][j] = 1;
                }else if(matrizSavedMap[i][j] == 'B'){
                    tmp[i][j] = 2;
                }else{
                    tmp[i][j] = matrizInicial[i][j];
                }
            }
        }
        Log.d("matrizestado","matrizestado");
        PrintMatrizInt(tmp);
        return tmp;
    }
    //Parsea arreglo de chars
    private void SaveGame() {

        String content="";
        String line="";
        for (int i = 0; i< rows; i++){
            line="";
            for (int j = 0; j < columns; j++){
                line += matrizSavedMap[i][j] + " ";
            }
            line+= System.getProperty("line.separator");
            content += line;
        }
        Log.d("CONTENT",content);
        write("save0"+levelId,content);
    }

    public Boolean write(String fname, String fcontent){
        try {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kuromasu/saves/";
//            String fpath = fname+".txt";
//
//            new File(path).mkdir();
//            File file = new File(path + fpath);
//            if (!file.exists()) {
//                file.createNewFile();
//            }
            String fpath = "/sdcard/"+fname+".txt";
            File file = new File(fpath);
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess","Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void PrintMatrizInt(int[][] m){
        String line ="";
        for (int i = 0; i< rows; i++){
            for (int j = 0; j < columns; j++){
                line += m[i][j] + ",";
            }
            line += System.lineSeparator();
        }
        Log.d("INT[][]", line);
    }

    private void PrintMatrizChar(char[][] m){
        String line= "";
        for (int i = 0; i< rows; i++){
            //line ="";
            for (int j = 0; j < columns; j++){
                line += m[i][j] + ",";
            }
            line += System.lineSeparator();
        }
        Log.d("CHAR[][]", line);
    }

    private void ChangeButtonState(int k, int p, Button b){
        if(matrizEstado[k][p]==0){
            b.setBackgroundColor(Color.BLACK);
            matrizEstado[k][p]++;
            matrizSavedMap[k][p]='N'; // NEGRO
        }
        else if(matrizEstado[k][p]==1){
            b.setBackgroundColor(Color.WHITE);
            matrizEstado[k][p]++;
            matrizSavedMap[k][p]='B'; // BLANCO
        }
        else {
            b.setBackgroundColor(Color.GRAY); // GRIS
            matrizEstado[k][p]=0;
        }
    }

    private void ComprobarEstadoJuego(){
        if(comprobar(matrizEstado)) {
            if(Arrays.deepEquals(matrizInicial, matrizEstado)){
                Toast toast1 = Toast.makeText(getApplicationContext(),"Felicitaciones", Toast.LENGTH_SHORT);
                toast1.show();
                System.out.println("correcto");
            }

            else {
                Toast toast1 =Toast.makeText(getApplicationContext(),"Incorrecto", Toast.LENGTH_SHORT);
                toast1.show();
            }

            try {
                Pista(matrizInicial, matrizEstado);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private  void ButtonPressed(){

        if(Arrays.deepEquals(matrizInicial, matrizEstado)){
            Toast toast1 =Toast.makeText(getApplicationContext(),"Felicitaciones", Toast.LENGTH_SHORT);
            toast1.show();
            System.out.println("correcto");
        }
        else System.out.println("Incorrecto");

        ComprobarEstadoJuego();

    }

    private void CrearTablero(){
        for (int i = 0; i< rows; i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            //Y POR CADA COLUMNA UNA VISTA QUE PUEDE SER UN BUTTON Y REPRESENTA EL ESPACIO
            for(int j = 0; j< columns; j++){
                final int p = j;
                final int k = i;
                final Button b= new Button(this);
                botones[i][j] = b;
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChangeButtonState(k,p, b);
                        //ButtonPressed();
                        PrintMatrizChar(matrizSavedMap);
                    }
                });

                int buttonSize = screenWidth/ columns - 5;
                b.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, buttonSize));
                // y se añade al layout
                layout.addView(b);
          }
            layoutPrincipal.addView(layout);
        }
    }

    private void PintarTablero(){
        for (int i = 0; i< rows; i++){
            for(int j = 0; j< columns; j++) {
                if (matrizInicial[i][j]== 0) {
                    if (matrizEstado[i][j] == 1) {
                        botones[i][j].setBackgroundColor(Color.BLACK);

                    } else if (matrizEstado[i][j] == 2) {
                        botones[i][j].setBackgroundColor(Color.WHITE);
                    }
                }else{
                      botones[i][j].setEnabled(false);
                      botones[i][j].setText(String.valueOf(matrizEstado[i][j]));
                  }
            }
        }
    }

    //Termina el Main Activity

    //Comprueba matrizEstado completa o no, true si es completa
    private boolean comprobar(int matriz2[][]) {
        for (int i = 0; i < 2; i++) {    // El primer índice recorre las rows.
            for (int j = 0; j < 3; j++) {    // El segundo índice recorre las columns.
                // Procesamos cada elemento de la matrizEstado.
                if (matriz2[i][j]==0) return false;
            }

        }
        return true;
    }

    private void Pista(int mat[][], int resp[][]) throws InterruptedException {
        for (int i = 0; i < mat.length; i++) {    // El primer índice recorre las rows.
            for (int j = 0; j < mat[0].length; j++) {    // El segundo índice recorre las columns.
                // Procesamos cada elemento de la matrizEstado.
                if( resp[i][j]!=mat[i][j]){
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Debes corregir la celda ubicada en la fila "+i+"columna"+j+"gracias", Toast.LENGTH_SHORT);
                    toast1.show();
                    Thread.sleep(3000);
                }
            }
        }
    }

    public void LoadSavedGame(String text){
        String savedLevel = read(text);

        if(savedLevel != null){
            Log.e("READ", "Saved Game cargado" );
            //btnGameContinue.setEnabled(true);
            ParseSavedGame(savedLevel);
        }else{
            Log.e("READ", "Saved Game no existente" );
            //btnGameContinue.setEnabled(false);
        }

    }

    public String read(String fname){

        BufferedReader br = null;
        String response = null;

        try {

            StringBuffer output = new StringBuffer();
            String fpath = "/sdcard/"+fname+".txt";

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +System.getProperty("line.separator"));
            }
            response = output.toString();

            //Log.d("READ", response);
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
        return response;
    }


    public void ParseSavedGame(String savedLevel){
        String[] lines = savedLevel.split("\n|\n ");
        // eje y
        int j =0;
        for (String line : lines) {
            //eje x
            int counterX =0;
            String[] r = line.split("\\s");
            for (int i =0; i< r.length; i++){
                char[] chars = r[i].toCharArray();
                matrizSavedMap[j][counterX++] = chars[0];
            }
            j++;
            if(j > rows){break;}
        }
    }

}
