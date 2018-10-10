package com.example.josue.cifrado;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ProcesoCifradoZigZag extends Fragment implements View.OnClickListener {

    TextView Nivel;
    TextView TextoDescifrado;
    TextView TextoCifrado;
    Button BotonCifrar;
    Button BotonDecifrar;
    static String Cifrado;
    static String Decifrado;

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
                Toast.makeText(getActivity(), "Aqui va todo el codigo para descifrar",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void RecibirParametros(String textoc, String textod)
    {
        Cifrado = textoc;
        Decifrado = textod;

    }
}


