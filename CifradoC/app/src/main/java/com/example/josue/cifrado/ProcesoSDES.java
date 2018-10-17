package com.example.josue.cifrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProcesoSDES extends Fragment implements View.OnClickListener{

    TextView KeyCifrado;
    TextView KeyDescifrado;
    Button BotonCifrar;
    Button BotonDecifrar;
    static String RutaArchivoCifrado;
    String Ruta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_sde, container, false);

        KeyCifrado = (TextView) view.findViewById(R.id.txtKey);
        KeyDescifrado = (TextView) view.findViewById(R.id.txtKeyDescifrado);
        BotonDecifrar = (Button) view.findViewById(R.id.btnDescifrarSDES);
        BotonCifrar = (Button) view.findViewById(R.id.btnCifrarSDES);
        BotonCifrar.setOnClickListener(this);
        BotonDecifrar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        SDES Proceso = new SDES();

        switch (view.getId()) {
            case R.id.btnCifrarSDES:

                final String Auxiliar = KeyCifrado.getText().toString();
                if (Auxiliar.equals("") == false) {
                    Proceso.RecibirLlaves(Auxiliar);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new SDES()).commit();
                } else if (Auxiliar.equals("") == true) {
                    Toast.makeText(getActivity(), "Debe de Ingresar una Llave para Poder Continuar", Toast.LENGTH_SHORT).show();
                } else if (Auxiliar.length() < 10) {
                    Toast.makeText(getActivity(), "La Clave de Cifrado debe tener al menos 10 Bits", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.btnDescifrarSDES:

                final String Auxiliarc = KeyDescifrado.getText().toString();
                KeyDescifrado.setText("");
                if (Auxiliarc.equals("") == false) {
                    //Aqui va todo el codigo para decifrar
                    final SDES ExtraerDatos = new SDES();

                    //Esto es para Que seleccione la Ruta donde desea guardar el archivo decifrado
                    //Se Extraen los datos Leidos de la Estructura para fijarlos en la Actividad
                    List<String> Lista = ExtraerDatos.EnviarNombres();

                    final String[] ListaNombres = new String[Lista.size()];
                    int contador = 0;
                    for (String i : Lista) {
                        ListaNombres[contador] = i;
                        contador++;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Selecciona la Ruta para Guardar tu Archivo Decifrado");

                    int checkedItem = 1; // cow
                    builder.setSingleChoiceItems(ListaNombres, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // user checked an item
                        }
                    });

                    builder.setItems(ListaNombres,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    Toast.makeText(getActivity(), "Has Elegido Guardar tu Archivo Decifrado en: " + ListaNombres[item], Toast.LENGTH_SHORT).show();
                                    //Se envian los datos a los Fragments
                                    ExtraerDatos.RecibirRuta(ListaNombres[item]);
                                    Ruta = ListaNombres[item];

                                }
                            });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            int [] CadenaInt = new int[10];
                            char[] cadenaChar = Auxiliarc.toCharArray();

                            for (int i = 0; i < 10; i++)
                            {
                                CadenaInt[i] = cadenaChar[i];

                                if(CadenaInt[i] == 48)
                                    CadenaInt[i] = 0;
                                else
                                    CadenaInt[i] = 1;
                            }

                            String TextoparaEscribir = "";
                            //Aqui se envia a Descifrar y a Escribir
                            SDES Envio = new SDES();
                            Envio.GenerarLlaves(CadenaInt);
                            //Aqui debe de recibir el texto decifrado que se va aenviar a escribir
                            File ArchivoCifrado = new File(RutaArchivoCifrado);
                            char[] TextoaDescifrar = new char[0];
                            try {
                                TextoaDescifrar = Envio.Lectura(ArchivoCifrado);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            TextoparaEscribir = "";
                            for(int i = 0; i< TextoaDescifrar.length; i++)
                            {
                                char letraDesifrada = Envio.DescifrarSDES(TextoaDescifrar[i]);
                                TextoparaEscribir = TextoparaEscribir + String.valueOf(letraDesifrada);
                            }

                            Envio.EscribirDecifrado(TextoparaEscribir,Ruta);
                            Toast.makeText(getActivity(), "Se ha Decifrado el Archivo Correctamente", Toast.LENGTH_SHORT).show();

                        }
                    });

                    builder.setNegativeButton("Cancel", null);


                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;
                } else if (Auxiliarc.equals("") == true) {
                    Toast.makeText(getActivity(), "Debe de Ingresar una Llave para Poder Continuar", Toast.LENGTH_SHORT).show();
                } else if (Auxiliarc.length() < 10) {
                    Toast.makeText(getActivity(), "La Clave de Descifrado debe tener al menos 10 Bits", Toast.LENGTH_SHORT).show();
                }

        }
    }


    public void RecibirParametros(String RutaArchivoCifrado)
    {
        this.RutaArchivoCifrado = RutaArchivoCifrado;
    }


}

