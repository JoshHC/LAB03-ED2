package com.example.josue.cifrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProcesoCifradoZigZag extends Fragment implements View.OnClickListener {

    TextView Nivel;
    TextView TextoDescifrado;
    TextView TextoCifrado;
    Button BotonCifrar;
    Button BotonDecifrar;
    static String Cifrado;
    static String RutaArchivoCifrado;
<<<<<<< HEAD
=======
    static int nivel;
>>>>>>> 3b318a5bf2ed6e954a959cff72235d686d70396b
    //Ruta del Archivo
    String Ruta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_proceso_cifrado_zig_zag, container, false);

        Nivel = (TextView) view.findViewById(R.id.txtNivel);
        TextoDescifrado = (TextView) view.findViewById(R.id.txtTextoDescifrado);
        TextoCifrado = (TextView) view.findViewById(R.id.txtTextoaDescifrar);
        BotonCifrar = (Button) view.findViewById(R.id.btnCifrar);
        BotonDecifrar = (Button) view.findViewById(R.id.btnDescifrar);
        TextoCifrado.setEnabled(false);
        TextoCifrado.setFocusable(false);
        TextoDescifrado.setEnabled(false);
        TextoDescifrado.setFocusable(false);

        BotonCifrar.setOnClickListener(this);
        BotonDecifrar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        CifradoZigZag Datos = new CifradoZigZag();
        String Auxiliar = Nivel.getText().toString();
        nivel = Integer.parseInt(Auxiliar);

        switch (view.getId())
        {
            case R.id.btnCifrar:

                if(Auxiliar.equals("1"))
                {

                    Toast.makeText(getActivity(), "ERROR El Nivel no Puede estar comprendido entre 0-1 ",Toast.LENGTH_SHORT).show();
                    Nivel.setText(" ");

                }else if (Auxiliar.equals("0"))
                {

                    Toast.makeText(getActivity(), "ERROR El Nivel no Puede estar comprendido entre 0-1 ",Toast.LENGTH_SHORT).show();
                    Nivel.setText(" ");

                }else if (Auxiliar.equals(""))
                {
                    Toast.makeText(getActivity(), "Debe de Ingresar un Nivel para poder continuar",Toast.LENGTH_SHORT).show();
                }
                else {
                    Datos.RecibirParametros(TextoCifrado.getText().toString(), Integer.parseInt(Nivel.getText().toString()));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CifradoZigZag()).commit();
                    TextoCifrado.setText(Cifrado);
                    TextoCifrado.setEnabled(true);
                    TextoCifrado.setFocusable(true);

<<<<<<< HEAD
=======

>>>>>>> 3b318a5bf2ed6e954a959cff72235d686d70396b
                    /*
                    if(Decifrado!= null) {
                        if (Decifrado.equals("") == false) {
                            TextoDescifrado.setText(Decifrado);
                            TextoDescifrado.setEnabled(true);
                            TextoDescifrado.setFocusable(true);
                        }
<<<<<<< HEAD

                    }*/
=======
                    }
                    */
>>>>>>> 3b318a5bf2ed6e954a959cff72235d686d70396b
                }

                break;

            case R.id.btnDescifrar:

                //Aqui va todo el codigo para decifrar
                final  CifradoZigZag ExtraerDatos = new CifradoZigZag();

                //Esto es para Que seleccione la Ruta donde desea guardar el archivo decifrado
                //Se Extraen los datos Leidos de la Estructura para fijarlos en la Actividad
                List<String> Lista = ExtraerDatos.EnviarNombres();

                final String[] ListaNombres = new String[Lista.size()];
                int contador = 0;
                for(String i: Lista)
                {
                    ListaNombres[contador]= i;
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
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int item)
                            {
                                Toast.makeText(getActivity(),"Has Elegido Guardar tu Archivo Decifrado en: " + ListaNombres[item], Toast.LENGTH_SHORT).show();
                                //Se envian los datos a los Fragments
                                ExtraerDatos.RecibirRuta(ListaNombres[item]);
                                Ruta = ListaNombres[item];
                            }
                        });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("Cancel", null);


                AlertDialog dialog = builder.create();
                dialog.show();

                //Aqui se envia a Descifrar y a Escribir
                CifradoZigZag Envio = new CifradoZigZag();
                //Aqui debe de recibir el texto decifrado que se va aenviar a escribir
                File ArchivoCifrado = new File(RutaArchivoCifrado);
                String TextoCifrado = Envio.LeerArchivo(ArchivoCifrado);

                ZigZag DescifradoZigZag = new ZigZag(TextoCifrado, nivel);
                String TextoDecifrado = DescifradoZigZag.Descifrar();

                Envio.EscribirDecifrado(TextoDecifrado,Ruta);

                break;
        }

    }

    public void RecibirParametros(String textoc, String RutaArchivoCifrado)
    {
        Cifrado = textoc;
<<<<<<< HEAD
        RutaArchivoCifrado = RutaArchivoCifrado;

=======
        this.RutaArchivoCifrado = RutaArchivoCifrado;
>>>>>>> 3b318a5bf2ed6e954a959cff72235d686d70396b
    }


}


