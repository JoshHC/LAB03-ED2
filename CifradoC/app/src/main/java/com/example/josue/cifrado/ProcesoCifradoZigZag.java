package com.example.josue.cifrado;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ProcesoCifradoZigZag extends Fragment implements View.OnClickListener {

    TextView Nivel;
    TextView TextoDescifrado;
    TextView TextoaDecifrar;
    Button BotonCifrar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_proceso_cifrado_zig_zag, container, false);

        Nivel = (TextView) view.findViewById(R.id.txtNivel);
        TextoDescifrado = (TextView) view.findViewById(R.id.txtTextoDescifrado);
        TextoaDecifrar = (TextView) view.findViewById(R.id.txtTextoaDescifrar);
        BotonCifrar = (Button) view.findViewById(R.id.btnCifrar);


        BotonCifrar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        CifradoZigZag Datos = new CifradoZigZag();

        String Auxiliar = Nivel.getText().toString();

        if(Auxiliar.equals("1"))
        {

            Toast.makeText(getActivity(), "ERROR El Nivel no Puede estar comprendido entre 0-1 ",Toast.LENGTH_SHORT).show();
            Nivel.setText(" ");

        }else if (Auxiliar.equals("0"))
        {

            Toast.makeText(getActivity(), "ERROR El Nivel no Puede estar comprendido entre 0-1 ",Toast.LENGTH_SHORT).show();
            Nivel.setText(" ");

        }else {
            Datos.RecibirParametros(TextoaDecifrar.getText().toString(), Integer.parseInt(Nivel.getText().toString()));
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CifradoZigZag()).commit();
        }
    }
    
}


