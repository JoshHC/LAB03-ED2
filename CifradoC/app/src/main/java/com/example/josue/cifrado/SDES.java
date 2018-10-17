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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SDES extends Fragment implements OnItemClickListener {

    static String [][] S0 = {{"01","00","11","10"},{"11","10","01","00"},{"00","10","01","11"}, {"11","01","11","10"}};
    static String [][] S1 = {{"00","01","10","11"},{"10","00","01","11"},{"11","00","01","00"}, {"10","01","00","11"}};
    private static int[] K1 = new int [8];
    private static int[] K2 = new int [8];
    private List<String> NombresArchivos;
    private List<String> RutasArchivos;
    static private String DirectorioRaiz;
    private TextView CarpetaActual;
    ListView Lista;
    static String TextoParaDecifrar;
    static int NiveldeDecifrado;
    static String ruta;
    static String Carpeta;
    static String ArchivoT;
    static int [] CadenaInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sdes, container, false);

        CarpetaActual = (TextView) view.findViewById(R.id.RutaSDES);
        Lista = (ListView) view.findViewById(R.id.ListaSDES);

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
                    Texto += "λ";

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
                        Texto += "λ";

                    Linea = SiguienteLinea;
                }

            }
            LecturaArchivo.close();
            LeerArchivo.close();
            LecturarArchivoC.close();
            Lector.close();

            return Texto.toCharArray();
        }
        else
        {
            return "-1".toCharArray();
        }
    }

    //Metodo en donde se escribe el Archivo
    private void Escribir(String Cadena)
    {
        MainActivity P = new MainActivity();
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();
        String Ruta = "";
        File ArchivoNuevo = new File(ArchivoT);
        String Formato = "/"+ArchivoNuevo.getName().replace(".txt",".SDES");

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
                bw.write(Cadena);
                bw.close();
                Escribir.close();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
            }
        }
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
                Dialogo.setMessage("¿Desea Aplicar El Metodo de Cifrado SDES?");
                Dialogo.setCancelable(false);
                Dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {

                        char[] TextoaCifrar = new char[0];
                        try {
                            TextoaCifrar = Lectura(archivo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String TextoparaEscribir = "";
                        GenerarLlaves(CadenaInt);
                        ArchivoT = archivo.toString();
                            for (int i = 0; i < TextoaCifrar.length; i++) {
                                char letraCifrada = CifrarSDES(TextoaCifrar[i]);
                                TextoparaEscribir = TextoparaEscribir + String.valueOf(letraCifrada);
                            }
                         Escribir(TextoparaEscribir);
                            ProcesoSDES Envio = new ProcesoSDES();
                        Envio.RecibirParametros(ruta);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new ProcesoSDES()).commit();
                        Toast.makeText(getActivity(), "El Texto se Ha Cifrado Correctamente ",Toast.LENGTH_SHORT).show();

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

    //Metodo en donde se escribe el Archivo
    public void EscribirDecifrado(String Cadena, String RutaAux) {

        MainActivity P = new MainActivity();
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();

        File ArchivoNuevo = new File(ArchivoT);
        File Auxiliar = new File(RutaAux);
        String Formato = "/" + ArchivoNuevo.getName().replace(".txt", ".SDES");
        String Aux = " Decifrado";
        String Ruta = Formato.replace(".SDES", Aux) + ".SDES";
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
            bw.write(Cadena);
            bw.close();
            Escribir.close();
        } catch (IOException ex) {
            Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
        }
    }

    public static int[] RellenarCeros(char Letra)
    {
        int numero = Letra;
        String Aux = Integer.toBinaryString(numero);
        char[] Arrayderelleno = Aux.toCharArray();
        int [] ArrayRellenado = new int[8];

        if(Arrayderelleno.length < 8)
        {

            int Cantidad = 8 - Aux.length();
            String ceros = "";

            for(int i = 0; i< Cantidad; i++)
            {
                ceros = ceros + "0";
            }

            Aux = ceros+Aux;
            Arrayderelleno = Aux.toCharArray();

            for(int i = 0; i < Arrayderelleno.length; i++)
            {
                ArrayRellenado[i] = Arrayderelleno[i];

                if(ArrayRellenado[i] == 48)
                    ArrayRellenado[i] = 0;
                else
                    ArrayRellenado[i] = 1;
            }

        }else
        {
            for(int i = 0; i< Arrayderelleno.length; i++)
            {
                ArrayRellenado[i] = Arrayderelleno[i];

                if(ArrayRellenado[i] == 48)
                    ArrayRellenado[i] = 0;
                else
                    ArrayRellenado[i] = 1;
            }
        }


        return ArrayRellenado;
    }


    public static void GenerarLlaves(int [] Entrada)
    {
        int[] temp = new int[10];
        temp = P10(Entrada);

        int[] Parte1 = new int[5];
        int[] Parte2 = new int[5];

        for (int i = 0; i < 5; i++)
        {
            Parte1[i] = temp[i];
            Parte2[i] = temp[i+5];
        }

        Parte1 = LS1(Parte1);
        Parte2 = LS1(Parte2);

        for(int i = 0 ; i < 5; i++)
            temp[i] = Parte1[i];

        for(int i = 5; i < 10; i++)
            temp[i] = Parte2[i-5];

        K1 = P8(temp);

        Parte1 = LS2(Parte1);
        Parte2 = LS2(Parte2);

        for(int i = 0 ; i < 5; i++)
            temp[i] = Parte1[i];

        for(int i = 5; i < 10; i++)
            temp[i] = Parte2[i-5];

        K2 = P8(temp);
    }


    public static char CifrarSDES(char Letra)
    {
        int[] temp = new int[8];
        temp = RellenarCeros(Letra);

        temp = PInicial(temp);

        int[] Externo1 = new int[4];
        int[] Externo2 = new int[4];

        for (int i = 0; i < 4; i++)
        {
            Externo1[i] = temp[i];
            Externo2[i] = temp[i+4];
        }

        int[] TempInterno = new int[8];

        TempInterno = EP(Externo2);
        TempInterno = XOR(TempInterno, K1);

        int[] SBox = new int[4];

        SBox = SBoxes(TempInterno);
        SBox = P4(SBox);

        Externo1 = XOR(Externo1, SBox);

        // SE DAN VUELTA EXTERNO1 Y EXTERNO2 (PRESENTACION)

        temp = new int[8];
        TempInterno = new int[8];

        TempInterno = EP(Externo1);
        TempInterno = XOR(TempInterno, K2);

        SBox = new int[4];

        SBox = SBoxes(TempInterno);
        SBox = P4(SBox);

        Externo2 = XOR(Externo2, SBox);

        for (int i = 0; i < 4; i++)
            temp[i] = Externo2[i];

        for (int i = 4; i < 8; i++)
            temp[i] = Externo1[i-4];

        int[] cifrado = new int[8];
        cifrado = IPInverso(temp);

        String texto = "";

        for (int i = 0; i < 8; i++)
            texto += cifrado[i];

        int numero = Integer.parseInt(texto,2);
        char letraCifrado = (char) numero;

        return letraCifrado;
    }

    public static char DescifrarSDES(char Letra)
    {
        int[] temp = new int[8];
        temp = RellenarCeros(Letra);

        temp = PInicial(temp);

        int[] Externo1 = new int[4];
        int[] Externo2 = new int[4];

        for (int i = 0; i < 4; i++)
        {
            Externo1[i] = temp[i];
            Externo2[i] = temp[i+4];
        }

        int[] TempInterno = new int[8];

        TempInterno = EP(Externo2);
        TempInterno = XOR(TempInterno, K2);

        int[] SBox = new int[4];

        SBox = SBoxes(TempInterno);
        SBox = P4(SBox);

        Externo1 = XOR(Externo1, SBox);

        // SE DAN VUELTA EXTERNO1 Y EXTERNO2 (PRESENTACION)

        temp = new int[8];
        TempInterno = new int[8];

        TempInterno = EP(Externo1);
        TempInterno = XOR(TempInterno, K1);

        SBox = new int[4];

        SBox = SBoxes(TempInterno);
        SBox = P4(SBox);

        Externo2 = XOR(Externo2, SBox);

        for (int i = 0; i < 4; i++)
            temp[i] = Externo2[i];

        for (int i = 4; i < 8; i++)
            temp[i] = Externo1[i-4];

        int[] cifrado = new int[8];
        cifrado = IPInverso(temp);

        String texto = "";

        for (int i = 0; i < 8; i++)
            texto += cifrado[i];

        int numero = Integer.parseInt(texto,2);
        char letraDescifrado = (char) numero;

        return letraDescifrado;
    }


    public static int[] PInicial(int[] Entrada)
    {
        Constantes Datos = new Constantes();
        int [] ArregloAuxiliar = new int[Entrada.length];


        for(int i = 0; i< Datos.PermutacionInicial.length; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.PermutacionInicial[i]];
        }

        return ArregloAuxiliar;
    }

    public static int[] P10(int[] Entrada)
    {
        Constantes Datos = new Constantes();
        int [] ArregloAuxiliar = new int[Entrada.length];


        for(int i = 0; i< Datos.P10.length; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.P10[i]];
        }

        return ArregloAuxiliar;
    }

    public static int[] P8(int[] Entrada)
    {
        Constantes Datos = new Constantes();
        int [] ArregloAuxiliar = new int[Datos.P8.length];


        for(int i = 0; i< Datos.P8.length; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.P8[i]];
        }

        return ArregloAuxiliar;
    }

    public static int[] LS1(int[] Cadena)
    {
        int temp = Cadena[0];

        for (int i = 0; i < 4; i++)
            Cadena[i] = Cadena[i+1];

        Cadena[4] = temp;

        return Cadena;
    }

    public static int[] LS2(int[] Cadena)
    {
        int temp1 = Cadena[0];
        int temp2 = Cadena[1];

        for (int i = 0; i < 3; i++)
            Cadena[i] = Cadena[i+2];

        Cadena[3] = temp1;
        Cadena[4] = temp2;

        return Cadena;
    }

    public static int [] P4(int[] Entrada)
    {
        Constantes Datos = new Constantes();
        int [] ArregloAuxiliar = new int[Entrada.length];


        for(int i = 0; i< Datos.P4.length; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.P4[i]];
        }

        return ArregloAuxiliar;
    }

    public static int[] EP(int[] Entrada)
    {
        Constantes Datos = new Constantes();
        int [] ArregloAuxiliar = new int[Datos.EP.length];


        for(int i = 0; i<3; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.EP[i]];
        }

        for(int i = 3; i<=7; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.EP[i]];
        }

        return ArregloAuxiliar;
    }

    public static int[] IPInverso(int[] Entrada)
    {
        Constantes Datos = new Constantes();
        int [] ArregloAuxiliar = new int[Entrada.length];


        for(int i = 0; i < 8; i++)
        {
            ArregloAuxiliar[Datos.PermutacionInicial[i]] = Entrada[i];
        }

        return ArregloAuxiliar;
    }

    private static int[] XOR (int[] Comparador1, int[] Comparador2)
    {
        int[] XOR = new int[Comparador1.length];

        for (int i = 0; i < Comparador1.length; i++)
        {
            if(Comparador1[i] == Comparador2[i])
                XOR[i] = 0;
            else
                XOR[i] = 1;
        }

        return XOR;
    }

    public static int[] SBoxes(int [] Arreglo)
    {
        String Cadena;
        int[] Auxiliar = new int[4];
        String [] S0box = new String [2];
        String [] S1box = new String [2];

        String Aux = "";

        Aux = String.valueOf(Arreglo[0]);
        Aux = Aux + String.valueOf(Arreglo[3]);
        S0box[0] = Aux;
        Aux = "";
        Aux = String.valueOf(Arreglo[1]);
        Aux = Aux + String.valueOf(Arreglo[2]);
        S0box[1] = Aux;

        Aux = String.valueOf(Arreglo[4]);
        Aux = Aux + String.valueOf(Arreglo[7]);
        S1box[0] = Aux;
        Aux = "";
        Aux = String.valueOf(Arreglo[5]);
        Aux = Aux + String.valueOf(Arreglo[6]);
        S1box[1] = Aux;


        int Fila = Integer.valueOf(S0box[0],2);
        int Columna = Integer.valueOf(S0box[1],2);

        Cadena = S0[Fila][Columna];

        Fila = Integer.valueOf(S1box[0],2);
        Columna = Integer.valueOf(S1box[1],2);

        Cadena = Cadena + S1[Fila][Columna];

        char[] cadenaSeparada = Cadena.toCharArray();
        for (int i = 0; i < cadenaSeparada.length; i++)
        {
            Auxiliar[i] = cadenaSeparada[i];

            if(Auxiliar[i] == 48)
                Auxiliar[i] = 0;
            else
                Auxiliar[i] = 1;
        }

        return Auxiliar;
    }


    public String BinarioaString(int [] texto)
    {

        String Auxiliar = texto.toString();
        int TextoBinario = Integer.valueOf(Auxiliar,2);
        String Letra = String.valueOf((char) TextoBinario);

        return Letra;
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

    public void RecibirLlaves(String Llave)
    {

        CadenaInt = new int[10];
        char[] cadenaChar = Llave.toCharArray();

        for (int i = 0; i < 10; i++)
        {
            CadenaInt[i] = cadenaChar[i];

            if(CadenaInt[i] == 48)
                CadenaInt[i] = 0;
            else
                CadenaInt[i] = 1;
        }
    }

}
