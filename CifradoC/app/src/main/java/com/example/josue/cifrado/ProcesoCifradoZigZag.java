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
    static String Decifrado;
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
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 041a608e7cf2efc9219842e8a0d6db622bd861b8
        if(Auxiliar.equals("") == false) {
            nivel = Integer.parseInt(Auxiliar);
        }else
        {
            Toast.makeText(getActivity(), "Debe de Ingresar un Nivel para Poder Continuar",Toast.LENGTH_SHORT).show();
        }
<<<<<<< HEAD
=======
=======
>>>>>>> 29e6ef10f275a22cea8410b6414d1d03334887ee
>>>>>>> 041a608e7cf2efc9219842e8a0d6db622bd861b8

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


                    if(Decifrado!= null) {
                        if (Decifrado.equals("") == false) {
                            TextoDescifrado.setText(Decifrado);
                            TextoDescifrado.setEnabled(true);
                            TextoDescifrado.setFocusable(true);
                        }
                    }
                }

                break;

            case R.id.btnDescifrar:

<<<<<<< HEAD
                    //Aqui va todo el codigo para decifrar
                    final  CifradoZigZag ExtraerDatos = new CifradoZigZag();
=======
<<<<<<< HEAD
                    //Aqui va todo el codigo para decifrar
                    final  CifradoZigZag ExtraerDatos = new CifradoZigZag();
=======
                if(Auxiliar.equals(""))
                {
                    Toast.makeText(getActivity(),"Debe de Ingresar el Nivel de Descifrado", Toast.LENGTH_SHORT).show();

                }
                else
                {
                //Aqui va todo el codigo para decifrar
                final  CifradoZigZag ExtraerDatos = new CifradoZigZag();
>>>>>>> 29e6ef10f275a22cea8410b6414d1d03334887ee
>>>>>>> 041a608e7cf2efc9219842e8a0d6db622bd861b8

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
<<<<<<< HEAD

                                }
                            });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Aqui se envia a Descifrar y a Escribir
                            CifradoZigZag Envio = new CifradoZigZag();
                            //Aqui debe de recibir el texto decifrado que se va aenviar a escribir
                            File ArchivoCifrado = new File(RutaArchivoCifrado);
                            String TextoCifrado = Envio.LeerArchivo(ArchivoCifrado);

                            ZigZag DescifradoZigZag = new ZigZag(nivel, TextoCifrado);
                            String TextoDecifrado = DescifradoZigZag.Descifrar();

                            TextoDescifrado.setText(TextoDecifrado);
                            Envio.EscribirDecifrado(TextoDecifrado,Ruta);
                            Toast.makeText(getActivity(),"Se ha Decifrado el Archivo Correctamente", Toast.LENGTH_SHORT).show();

                        }
                    });

                    builder.setNegativeButton("Cancel", null);


                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;
                }
=======

                                }
                            });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Aqui se envia a Descifrar y a Escribir
                            CifradoZigZag Envio = new CifradoZigZag();
                            //Aqui debe de recibir el texto decifrado que se va aenviar a escribir
                            File ArchivoCifrado = new File(RutaArchivoCifrado);
                            String TextoCifrado = Envio.LeerArchivo(ArchivoCifrado);

                            ZigZag DescifradoZigZag = new ZigZag(nivel, TextoCifrado);
                            String TextoDecifrado = DescifradoZigZag.Descifrar();

                            TextoDescifrado.setText(TextoDecifrado);
                            Envio.EscribirDecifrado(TextoDecifrado,Ruta);
                            Toast.makeText(getActivity(),"Se ha Decifrado el Archivo Correctamente", Toast.LENGTH_SHORT).show();

<<<<<<< HEAD
                        }
                    });
=======
                AlertDialog dialog = builder.create();
                dialog.show();
<<<<<<< HEAD

>>>>>>> 29e6ef10f275a22cea8410b6414d1d03334887ee

                    builder.setNegativeButton("Cancel", null);

<<<<<<< HEAD

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;
                }
=======
                ZigZag DescifradoZigZag = new ZigZag(nivel, TextoCifrado);
                String TextoDecifrado = DescifradoZigZag.Descifrar();

                TextoDescifrado.setText(TextoDecifrado);
                Envio.EscribirDecifrado(TextoDecifrado,RutaArchivoCifrado);
                Toast.makeText(getActivity(),"Se ha Decifrado el Archivo Correctamente", Toast.LENGTH_SHORT).show();

=======
            /*
                //Aqui se envia a Descifrar y a Escribir
                CifradoZigZag Envio = new CifradoZigZag();
                //Aqui debe de recibir el texto decifrado que se va aenviar a escribir
                String TextoDecifrado = "";
                //TextoDecifrado = FunciondeDecifrado();
                Envio.EscribirDecifrado(TextoDecifrado,Ruta);
                */
>>>>>>> 7e9e9be46adbbf409582bc57241e4cec348bd2d0
                break;
>>>>>>> 29e6ef10f275a22cea8410b6414d1d03334887ee
        }
>>>>>>> 041a608e7cf2efc9219842e8a0d6db622bd861b8
        }


    public void RecibirParametros(String textoc, String textod)
    {
        Cifrado = textoc;
        Decifrado = textod;

    }


}



