package com.example.josue.cifrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;


public class ProcesoSDES extends Fragment implements View.OnClickListener {


    TextView KeyCifrado;
    TextView KeyDescifrado;
    Button BotonCifrar;
    Button BotonDecifrar;
    static String RutaArchivoCifrado;
    String Key;
    String Ruta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_sde, container, false);

        KeyCifrado = (TextView) view.findViewById(R.id.txtKey);
        KeyDescifrado = (TextView) view.findViewById(R.id.txtKeyDescifrado);
        BotonDecifrar = (Button) view.findViewById(R.id.btnDescifrarSDES);
        BotonCifrar.setOnClickListener(this);
        BotonDecifrar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        String Auxiliar = KeyCifrado.getText().toString();
        if(Auxiliar.equals("") == false) {
            Key = Auxiliar;
        }else
        {
            Toast.makeText(getActivity(), "Debe de Ingresar una Llave para Poder Continuar",Toast.LENGTH_SHORT).show();
        }

        switch (view.getId())
        {
            case R.id.btnCifrarSDES:

                break;

            case R.id.btnDescifrarSDES:

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

                        //Aqui se envia a Descifrar y a Escribir
                        CifradoZigZag Envio = new CifradoZigZag();
                        //Aqui debe de recibir el texto decifrado que se va aenviar a escribir
                        File ArchivoCifrado = new File(RutaArchivoCifrado);
                        String TextoCifrado = Envio.LeerArchivo(ArchivoCifrado);

                        Toast.makeText(getActivity(),"Se ha Decifrado el Archivo Correctamente", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNegativeButton("Cancel", null);


                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }

    }


}

