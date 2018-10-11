package com.example.josue.cifrado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CifradoZigZag extends Fragment implements OnItemClickListener {

    private List<String> NombresArchivos;
    private List<String> RutasArchivos;
    private String DirectorioRaiz;
    private TextView CarpetaActual;
    ListView Lista;
    static String TextoParaDecifrar;
    static int NiveldeDecifrado;
    static String Carpeta;
    static String ArchivoT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cifrado_zig_zag, container, false);

        CarpetaActual = (TextView) view.findViewById(R.id.RutaZigZag);
        Lista = (ListView) view.findViewById(R.id.ListaZigZag);

        DirectorioRaiz = Environment.getExternalStorageDirectory().getPath();
        Lista.setOnItemClickListener(this);
        VerDirectorio(DirectorioRaiz);
        return view;
    }


    //Metodo en donde se recuperan los Directorios validos para mostrarlos
    private void VerDirectorio (String rutadirectorio)
    {
        NombresArchivos = new ArrayList<String>();
        RutasArchivos = new ArrayList<String>();
        int count = 0;
        File directorioactual = new File(rutadirectorio);
        File[] ListadeArchivos = directorioactual.listFiles();

        if (!rutadirectorio.equals(DirectorioRaiz))
        {
            NombresArchivos.add("../");
            RutasArchivos.add(directorioactual.getParent());
            count = 1;
        }

        for (File archivo:ListadeArchivos){
            RutasArchivos.add(archivo.getPath());
        }

        Collections.sort(RutasArchivos,String.CASE_INSENSITIVE_ORDER);

        for (int i = count; i<RutasArchivos.size(); i++){
            File archivo = new File (RutasArchivos.get(i));

            if(archivo.isFile())
            {
                NombresArchivos.add(archivo.getName());
            }
            else
            {
                NombresArchivos.add("/" + archivo.getName());
            }
        }

        if(ListadeArchivos.length<1)
        {
            NombresArchivos.add("No hay ningun archivo");
            RutasArchivos.add(rutadirectorio);
        }

        final ArchivoAdapter Adaptador = new ArchivoAdapter(getActivity(), (ArrayList<String>) NombresArchivos);
        Lista.setAdapter(Adaptador);
    }

    //Metodo en donde se lee el Archivo
    private String LeerArchivo (File Archivo)
    {
        String Texto = "";
        if(Archivo.exists()==true)
        {
            FileReader LecturaArchivo;
            try {
                LecturaArchivo = new FileReader(Archivo);
                BufferedReader LeerArchivo = new BufferedReader(LecturaArchivo);
                String Linea="";
                String SiguienteLinea="";
                Linea = LeerArchivo.readLine();
                while(Linea != null)
                {
                    SiguienteLinea = LeerArchivo.readLine();
                    Texto += Linea;

                    if (SiguienteLinea != null)
                        Texto += "λ";

                    Linea = SiguienteLinea;
                }
                LecturaArchivo.close();
                LeerArchivo.close();

                return Texto;
            } catch (IOException e) {
                Toast.makeText(getActivity(), "ERROR El Archivo no se puede Leer!",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "ERROR El Archivo no Existe!",Toast.LENGTH_SHORT).show();
        }

        return "-1";
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        final File archivo = new File(RutasArchivos.get(i));
        if(archivo.isFile())
        {
            if (archivo.getName().endsWith(".txt"))
            {
                AlertDialog.Builder Dialogo = new AlertDialog.Builder(getActivity());
                Dialogo.setTitle("Importante");
                Dialogo.setMessage("¿Desea Aplicar El Metodo de Cifrado Zig Zag?");
                Dialogo.setCancelable(false);
                Dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        TextoParaDecifrar = LeerArchivo(archivo);
                        ZigZag CifradoZigZag = new ZigZag(TextoParaDecifrar,NiveldeDecifrado);
                        ArchivoT = archivo.toString();
                        String TextoCifrado = CifradoZigZag.Cifrar();
                        Escribir(TextoCifrado);

                        ProcesoCifradoZigZag Envio = new ProcesoCifradoZigZag();
                        Envio.RecibirParametros(TextoCifrado, "");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new ProcesoCifradoZigZag()).commit();
                        Toast.makeText(getActivity(), "El Texto se Ha Codificado Correctamente ",Toast.LENGTH_SHORT).show();

                    }
                });
                Dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                Dialogo.show();

            }
            else
            {
                Toast.makeText(getActivity(), "Has Seleccionado El Archivo: "+archivo.getName(),Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            VerDirectorio(RutasArchivos.get(i));
        }
    }

    public void RecibirParametros(String texto, int nivel)
    {
        TextoParaDecifrar = texto;
        NiveldeDecifrado = nivel;
    }


    //Metodo en donde se envian los nombres de los directorios validos
    public List<String> EnviarNombres()
    {
        DirectorioRaiz = Environment.getExternalStorageDirectory().getPath();
        NombresArchivos = new ArrayList<String>();
        RutasArchivos = new ArrayList<String>();
        int count = 0;
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();

        if (!DirectorioRaiz.equals(DirectorioRaiz)) {
            NombresArchivos.add("../");
            RutasArchivos.add(directorioactual.getParent());
            count = 1;
        }

        for (File archivo : ListadeArchivos) {
            RutasArchivos.add(archivo.getPath());
        }
        Collections.sort(RutasArchivos, String.CASE_INSENSITIVE_ORDER);

        for (int i = count; i < RutasArchivos.size(); i++) {
            File archivo = new File(RutasArchivos.get(i));

            if (archivo.isFile()) {
                NombresArchivos.add(archivo.getName());
            } else {
                NombresArchivos.add("/" + archivo.getName());
            }
        }

        if (ListadeArchivos.length < 1) {
            NombresArchivos.add("No hay ningun archivo");
            RutasArchivos.add(DirectorioRaiz);
        }

        return NombresArchivos;
    }

    //Metodo que recibe la ruta para guardar el archivo.
    public String RecibirRuta(String carpeta)
    {
        Carpeta = carpeta;
        return Carpeta;
    }

    //Metodo en donde se escribe el Archivo
    private void Escribir(String Cadena)
    {
        MainActivity P = new MainActivity();
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();
        String Ruta = "";
        File ArchivoNuevo = new File(ArchivoT);
        String Formato = "/"+ArchivoNuevo.getName().replace(".txt",".cif");

        if(Carpeta == null)
        {
            Toast.makeText(getActivity(), "No hay Ningun Archivo para Escribir Aún", Toast.LENGTH_SHORT).show();
        }
        else {
            for (File item : ListadeArchivos) {
                if (item.toString().contains(Carpeta) == true) {
                    Ruta = item.getAbsolutePath();
                    Ruta = Ruta + Formato;
                }
            }

            File Archivo = new File(Ruta);
            try {
                FileWriter Escribir = new FileWriter(Archivo);
                BufferedWriter bw = new BufferedWriter(Escribir);
                bw.write(Cadena);
                bw.close();
                Escribir.close();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Metodo en donde se escribe el Archivo
     public void EscribirDecifrado(String Cadena, String RutaAux)
    {
        MainActivity P = new MainActivity();
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();
        String Ruta = "";
        File ArchivoNuevo = new File(RutaAux);
        String Formato = "/"+ArchivoNuevo.getName().replace(".txt",".cif");

        if(Carpeta == null)
        {
            Toast.makeText(getActivity(), "No hay Ningun Archivo para Escribir Aún", Toast.LENGTH_SHORT).show();
        }
        else {
            for (File item : ListadeArchivos) {
                if (item.toString().contains(Carpeta) == true) {
                    Ruta = item.getAbsolutePath();
                    Ruta = Ruta + Formato;
                }
            }

            File Archivo = new File(Ruta);
            try {
                FileWriter Escribir = new FileWriter(Archivo);
                BufferedWriter bw = new BufferedWriter(Escribir);
                bw.write(Cadena);
                bw.close();
                Escribir.close();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
