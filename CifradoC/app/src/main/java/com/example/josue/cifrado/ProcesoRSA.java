package com.example.josue.cifrado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ProcesoRSA extends Fragment implements View.OnClickListener {

    TextView KeyCifrado1;
    TextView KeyCifrado2;

    TextView KeyDescifrado1;
    TextView KeyDescifrado2;

    Button BotonCifrar;
    Button BotonDecifrar;

    static String RutaArchivoCifrado;
    String Ruta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_proceso_r, container, false);

        KeyCifrado1 = (TextView) view.findViewById(R.id.txtKey1);
        KeyCifrado2 = (TextView) view.findViewById(R.id.txtKey2);

        KeyDescifrado1 = (TextView) view.findViewById(R.id.txtKeyDescifrado1);
        KeyDescifrado2 = (TextView) view.findViewById(R.id.txtKeyDescifrado2);

        BotonDecifrar = (Button) view.findViewById(R.id.btnDescifrarRSA);
        BotonCifrar = (Button) view.findViewById(R.id.btnCifrarRSA);
        BotonCifrar.setOnClickListener(this);
        BotonDecifrar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnCifrarRSA:

                String Auxiliar1 = KeyCifrado1.getText().toString();
                String Auxiliar2 =  KeyCifrado2.getText().toString();
                if(Auxiliar1.equals("")|| Auxiliar2.equals(""))
                {
                    KeyCifrado1.setText("");
                    KeyCifrado2.setText("");
                    Toast.makeText(getActivity(), "Se Necesitan dos claves para poder continuar", Toast.LENGTH_SHORT).show();
                }
                else if(ValidarPrimo(Integer.parseInt(Auxiliar1)) == false || ValidarPrimo(Integer.parseInt(Auxiliar2)) == false )
                {
                    Toast.makeText(getActivity(), "Alguno de los dos numeros o los dos NO son primos, porfavor ingrese numeros primos", Toast.LENGTH_SHORT).show();
                }
                else {
                    String Auxiliar = KeyCifrado1.getText().toString();
                    BigInteger Key1 = BigInteger.valueOf(Integer.valueOf(Auxiliar));
                    Auxiliar = KeyCifrado2.getText().toString();
                    BigInteger Key2 = BigInteger.valueOf(Integer.valueOf(Auxiliar));
                    RSA Envio = new RSA();
                    Envio.RecibirLlaves(Key1, Key2);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new RSA()).commit();
                }
                break;

            case R.id.btnDescifrarRSA:

                String Auxiliarc1 = KeyDescifrado1.getText().toString();
                String Auxiliarc2 =  KeyDescifrado2.getText().toString();
                if(Auxiliarc1.equals("")|| Auxiliarc2.equals(""))
                {
                    KeyDescifrado1.setText("");
                    KeyDescifrado2.setText("");
                    Toast.makeText(getActivity(), "Se Necesitan dos claves para poder continuar", Toast.LENGTH_SHORT).show();
                }
                else if(ValidarPrimo(Integer.parseInt(Auxiliarc1)) == false || ValidarPrimo(Integer.parseInt(Auxiliarc2)) == false )
                {
                    Toast.makeText(getActivity(), "Alguno de los dos numeros o los dos NO son primos, porfavor ingrese numeros primos", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Aqui va todo el codigo para decifrar
                    final RSA ExtraerDatos = new RSA();

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
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            File ArchivoCifrado = new File(RutaArchivoCifrado);
                            char[] TextoaDescifrar = null;
                            try {
                                TextoaDescifrar = RSA.Lectura(ArchivoCifrado);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String Auxiliar = KeyDescifrado1.getText().toString();
                            BigInteger Key1 = BigInteger.valueOf(Integer.valueOf(Auxiliar));
                            Auxiliar = KeyDescifrado2.getText().toString();
                            BigInteger Key2 = BigInteger.valueOf(Integer.valueOf(Auxiliar));

                            StringBuilder TextoparaEscribir = new StringBuilder();
                            TextoparaEscribir = ExtraerDatos.Descifrar(Key1, Key2, TextoaDescifrar);
                            ExtraerDatos.EscribirDecifrado(TextoparaEscribir, Ruta);
                            Toast.makeText(getActivity(), "Se ha Decifrado el Archivo Correctamente", Toast.LENGTH_SHORT).show();

                        }
                    });

                    builder.setNegativeButton("Cancel", null);


                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
        }
    }

    public void RecibirParametros(String RutaArchivoCifrado)
    {
        this.RutaArchivoCifrado = RutaArchivoCifrado;
    }

    public boolean ValidarPrimo(int Numero)
    {
        if(Numero%2 == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
