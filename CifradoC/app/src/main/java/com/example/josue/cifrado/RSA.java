package com.example.josue.cifrado;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RSA extends Fragment implements AdapterView.OnItemClickListener {


    private List<String> NombresArchivos;
    private List<String> RutasArchivos;
    static private String DirectorioRaiz;
    private TextView CarpetaActual;
    ListView Lista;
    static String ruta;
    static String Carpeta;
    static String ArchivoT;

    static BigInteger KeyCifrado1;
    static BigInteger KeyDescifrado1;
    static BigInteger KeyCifrado2;
    static BigInteger KeyDescifrado2;

    static BigInteger n;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_r, container, false);

        CarpetaActual = (TextView) view.findViewById(R.id.RutaRSA);
        Lista = (ListView) view.findViewById(R.id.ListaRSA);

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
    public static char[] Lectura(File Archivo) throws IOException
    {

        if(Archivo.exists()==true)
        {
            FileInputStream Lector = new FileInputStream(Archivo);
            InputStreamReader LecturarArchivoC = new InputStreamReader(Lector, "Windows-1252");
            BufferedReader LeerArchivoC = new BufferedReader(LecturarArchivoC);

            String Texto = "";
            FileReader LecturaArchivo;
            LecturaArchivo = new FileReader(Archivo);
            BufferedReader LeerArchivo = new BufferedReader(LecturaArchivo);

            String Linea="";
            String SiguienteLinea="";
            Linea = LeerArchivo.readLine();

            char Simbolo = (char)65533;

            while(Linea != null)
            {
                SiguienteLinea = LeerArchivo.readLine();
                Texto += Linea;

                if (SiguienteLinea != null)
                    Texto += "@";

                Linea = SiguienteLinea;
            }
            if(Texto.contains(String.valueOf(Simbolo)))
            {
                Texto = "";
                Linea = "";
                SiguienteLinea = "";
                Linea = LeerArchivoC.readLine();
                while(Linea != null)
                {
                    SiguienteLinea = LeerArchivoC.readLine();
                    Texto += Linea;

                    if (SiguienteLinea != null)
                        Texto += "@";

                    Linea = SiguienteLinea;
                }

            }
            LecturarArchivoC.close();
            Lector.close();
            LecturaArchivo.close();
            LeerArchivo.close();

            char[] TextoEnvio;

            final String UTF8_BOM = "\uFEFF";
            if(Texto.contains(UTF8_BOM)) {
                String[] Auxiliar = Texto.split(UTF8_BOM);
                TextoEnvio = Auxiliar[1].toCharArray();
            }
            else
            {
                TextoEnvio = Texto.toCharArray();
            }

            return  TextoEnvio;
        }
        else
        {
            return "-1".toCharArray();
        }
    }

    public static char[] LecturaEspecial(File Archivo) throws IOException
    {

        if(Archivo.exists()==true)
        {
            FileInputStream Lector = new FileInputStream(Archivo);
            InputStreamReader LecturarArchivoC = new InputStreamReader(Lector);
            BufferedReader LeerArchivoC = new BufferedReader(LecturarArchivoC);

            String Texto = "";

            String Linea="";
            String SiguienteLinea="";
                Linea = LeerArchivoC.readLine();
                while(Linea != null)
                {
                    SiguienteLinea = LeerArchivoC.readLine();
                    Texto += Linea;

                    if (SiguienteLinea != null)
                        Texto += "@";

                    Linea = SiguienteLinea;
                }
            LecturarArchivoC.close();
            Lector.close();
            char [] TextoEnvio = Texto.toCharArray();

            return  TextoEnvio;

            }
        else
        {
            return "-1".toCharArray();
        }
    }

    //Metodo en donde se escribe el Archivo
    private void Escribir(StringBuilder Cadena)
    {
        MainActivity P = new MainActivity();
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();
        String Ruta = "";
        File ArchivoNuevo = new File(ArchivoT);
        String Formato = "/"+ArchivoNuevo.getName().replace(".txt",".RSA");

        if(Carpeta == null)
        {
            Toast.makeText(getActivity(), "No hay Ningun Archivo para Escribir Aún", Toast.LENGTH_SHORT).show();
        }
        else {
            for (File item : ListadeArchivos) {
                if (item.toString().contains(Carpeta) == true) {
                    Ruta = item.getAbsolutePath();
                    Ruta = Ruta + Formato;
                    this.ruta = Ruta;
                }
            }

            File Archivo = new File(Ruta);
            try {
                FileWriter Escribir = new FileWriter(Archivo);
                BufferedWriter bw = new BufferedWriter(Escribir);
                bw.write(String.valueOf(Cadena));
                bw.close();
                Escribir.close();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Metodo en donde se escribe el Archivo
    public void EscribirDecifrado(StringBuilder Cadena, String RutaAux) {

        MainActivity P = new MainActivity();
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();

        File ArchivoNuevo = new File(ArchivoT);
        File Auxiliar = new File(RutaAux);
        String Formato = "/" + ArchivoNuevo.getName().replace(".txt", ".RSA");
        String Aux = "Decifrado";
        String Ruta = Formato.replace(".RSA", Aux) + ".RSA";
        String AuxT = "";
        String RutaC = "";
        //String TextoaReemplazar = Auxiliar.getName();
        //RutaAux = RutaAux.replace(TextoaReemplazar, Ruta);

        for (File item : ListadeArchivos) {
            String itemaux = item.toString();
            if (itemaux.contains(Carpeta) == true) {
                AuxT = item.getAbsolutePath();
                RutaC = AuxT + Ruta;
                break;
            }
        }


        File Archivo = new File(RutaC);
        try {
            FileWriter Escribir = new FileWriter(Archivo);
            BufferedWriter bw = new BufferedWriter(Escribir);
            char[] Texto = Cadena.toString().toCharArray();
            String Textoparaescribir = "";
            for(int i = 0; i<Cadena.length(); i++)
            {
                if(Texto[i] == '@')
                {
                    Textoparaescribir = Textoparaescribir + "\r\n";
                }
                else {

                    Textoparaescribir = Textoparaescribir + Texto[i];
                }
            }
            bw.write(Textoparaescribir);
            bw.close();
            Escribir.close();
        } catch (IOException ex) {
            Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
        }
    }


    //Metodo que recibe la ruta para guardar el archivo.
    public String RecibirRuta(String carpeta)
    {
        Carpeta = carpeta;
        return Carpeta;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        final File archivo = new File(RutasArchivos.get(i));
        if(archivo.isFile())
        {
            if (archivo.getName().endsWith(".txt"))
            {
                AlertDialog.Builder Dialogo = new AlertDialog.Builder(getActivity());
                Dialogo.setTitle("Importante");
                Dialogo.setMessage("¿Desea Aplicar El Metodo de Cifrado RSA?");
                Dialogo.setCancelable(false);
                Dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        char[] TextoaCifrar = null;
                        try {
                            TextoaCifrar = Lectura(archivo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        StringBuilder TextoparaEscribir = new StringBuilder();
                        ArchivoT = archivo.toString();
                        TextoparaEscribir = Cifrar(KeyCifrado1,KeyCifrado2,TextoaCifrar);
                        Escribir(TextoparaEscribir);
                        ProcesoRSA Envio = new ProcesoRSA();
                        Envio.RecibirParametros(ruta);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new ProcesoRSA()).commit();
                        Toast.makeText(getActivity(), "El Texto se Ha Cifrado Correctamente ",Toast.LENGTH_SHORT).show();

                    }
                });
                Dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new ProcesoRSA()).commit();
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

    public StringBuilder Cifrar(BigInteger p, BigInteger q, char[] Texto)
    {
        n = p.multiply(q);
        BigInteger Fi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = new BigInteger("0");
        BigInteger d = new BigInteger("0");
        BigInteger C = new BigInteger("0");
        BigInteger N = new BigInteger("0");
        StringBuilder TextoCifrado = new StringBuilder();

        //Funcion para elegir e, numero primero mayor de 17 hasta Fi
        for(int i = 17; i< Fi.intValue(); i++)
        {
            if (Coprimo(i, Fi.intValue()) == true)
            {
                e = BigInteger.valueOf(i);
                break;
            }
        }

        //para d sacar el inverso multiplicativo 97mod2088 osea emodFi
        //e = new BigInteger("97");
        d = e.modInverse(Fi);
        //Armar claves publica y privada clave privada (n,d) Clave publica(n,e)
        String PublicKey = "("+n.toString()+","+e.toString()+")";
        String PrivateKey = "("+n.toString()+","+d.toString()+")";

        WritePublicKey(PublicKey);
        WritePrivateKey(PrivateKey);

        //Cifrando todo el Texto
        for(int i = 0; i< Texto.length; i++)
        {
            BigInteger M = BigInteger.valueOf(Texto[i]);
            C = (M.pow(e.intValue())).mod(n);
            char caracter = (char)C.intValue();
            TextoCifrado = TextoCifrado.append(String.valueOf(caracter));
        }

        return TextoCifrado;
    }


    public boolean Coprimo (int Dato, int Fi)
    {
        int divisor=2;
        boolean rta=true;

        while ((divisor<=Fi/2) || (divisor<=Dato/2))
        {
            if(((Fi % divisor)==0)&&((Dato % divisor)==0))
                return (rta=false);
            divisor++;
        }
        return rta;
    }


    public StringBuilder Descifrar(BigInteger d, BigInteger n, char[] Texto)
    {
        //Descifrando todo el Texto
        BigInteger N = new BigInteger("0");
        StringBuilder TextoCifrado = new StringBuilder();

        for(int i = 0; i< Texto.length; i++)
        {
            BigInteger C = BigInteger.valueOf(Texto[i]);
            N = (C.pow(d.intValue())).mod(n);
            char caracter = (char)N.intValue();
            TextoCifrado = TextoCifrado.append(String.valueOf(caracter));
        }

        return TextoCifrado;
    }

    public void RecibirLlaves(BigInteger key1, BigInteger key2)
    {
        KeyCifrado1 = key1;
        KeyCifrado2 = key2;
    }

    public void WritePublicKey(String Cadena)
    {
            MainActivity P = new MainActivity();
            File directorioactual = new File(DirectorioRaiz);
            File[] ListadeArchivos = directorioactual.listFiles();
            String Ruta = "";
            File ArchivoNuevo = new File(ArchivoT);
            String Nombre = ArchivoNuevo.getName().replace(".txt",".RSA");
            Nombre = Nombre.replace(".RSA","PublicKey.RSA");
            String Formato = "/"+ Nombre;

            if(Carpeta == null)
            {
                Toast.makeText(getActivity(), "No hay Ningun Archivo para Escribir Aún", Toast.LENGTH_SHORT).show();
            }
            else {
                for (File item : ListadeArchivos) {
                    if (item.toString().contains(Carpeta) == true) {
                        Ruta = item.getAbsolutePath();
                        Ruta = Ruta + Formato;
                        this.ruta = Ruta;
                    }
                }

                File Archivo = new File(Ruta);
                try {
                    FileWriter Escribir = new FileWriter(Archivo);
                    BufferedWriter bw = new BufferedWriter(Escribir);
                    bw.write(String.valueOf(Cadena));
                    bw.close();
                    Escribir.close();
                } catch (IOException ex) {
                    Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
                }
            }

    }

    public void WritePrivateKey(String Cadena)
    {
        MainActivity P = new MainActivity();
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();
        String Ruta = "";
        File ArchivoNuevo = new File(ArchivoT);
        String Nombre = ArchivoNuevo.getName().replace(".txt",".RSA");
        Nombre = Nombre.replace(".RSA","PrivateKey.RSA");
        String Formato = "/"+ Nombre;

        if(Carpeta == null)
        {
            Toast.makeText(getActivity(), "No hay Ningun Archivo para Escribir Aún", Toast.LENGTH_SHORT).show();
        }
        else {
            for (File item : ListadeArchivos) {
                if (item.toString().contains(Carpeta) == true) {
                    Ruta = item.getAbsolutePath();
                    Ruta = Ruta + Formato;
                    this.ruta = Ruta;
                }
            }

            File Archivo = new File(Ruta);
            try {
                FileWriter Escribir = new FileWriter(Archivo);
                BufferedWriter bw = new BufferedWriter(Escribir);
                bw.write(String.valueOf(Cadena));
                bw.close();
                Escribir.close();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
