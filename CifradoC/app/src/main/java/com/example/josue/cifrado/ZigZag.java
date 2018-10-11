package com.example.josue.cifrado;

public class ZigZag {

    private char[] Texto;
    private int[] niveles;
    private Riel[] Rieles;
    private static int Nivel;


    public ZigZag(String Texto, int Nivel) {
        this.Texto = Rellenar(Texto, Nivel).toCharArray();
        niveles = ObtenerTamañosOla(Nivel);
        this.Nivel = Nivel;
        Rieles = new Riel[Nivel];
    }

    public String Cifrar()
    {
        String TextoCifrado = "";

        int cantExtremos = Texto.length/niveles[Nivel];
        int cantMedios = cantExtremos*2;

        // Representa el caracter por el cual comienza la fila
        int paso = 0;

        // SE OBTIENEN LA PRIMERA FILA (CRESTAS DE LAS OLAS)
        TextoCifrado += ObtenerExtremos(paso, cantExtremos);
        paso++;

        if (Nivel > 2)
        {
            while (paso < Nivel-1)
            {
                TextoCifrado += ObtenerMedios(paso, cantMedios, Nivel-paso);
                paso++;
            }
        }

        // SE OBTIENEN LA ULTIMA FILA (VALLES DE LAS OLAS)
        TextoCifrado += ObtenerExtremos(paso, cantExtremos);

        return TextoCifrado;
    }


    public String Descifrar()
    {
        int cantExtremos = this.Texto.length/niveles[Nivel];
        int cantMedios = cantExtremos*2;

        // SE ASIGNAN LOS ESPACIOS PARA DESCIFRAR
        Riel crestas = new Riel(cantExtremos);
        Rieles[0] = crestas;

        for(int i = 1; i < (Nivel-1); i++)
        {
            Riel temp = new Riel(cantMedios);
            Rieles[i] = temp;
        }

        Riel valles = new Riel(cantExtremos);
        Rieles[Nivel-1] = valles;

        // SE COMIENZAN A RELLENAR LOS RIELES
        for (int i = 0; i < cantExtremos; i++)
        {
            Rieles[0].Riel[i] = this.Texto[i];
        }

        int cont = 0;
        int cAcumulado = cantExtremos;
        for (int i = 1; i < (Nivel-1); i++)
        {
            while (cont < cantMedios)
            {
                Rieles[i].Riel[cont] = this.Texto[cAcumulado];
                cAcumulado++;
                cont++;
            }
            cont = 0;
        }


        cont = 0;
        for (int i = this.Texto.length - cantExtremos; i < this.Texto.length; i++)
        {
            Rieles[Nivel-1].Riel[cont] = this.Texto[i];
            cont++;
        }

        String Descifrado = "";

        int pasoExtremos = 0;
        int pasoMedios = 0;
        for (int i = 0; i < cantExtremos; i++)
        {
            Descifrado += Character.toString(Rieles[0].Riel[pasoExtremos]);

            for (int a = 1; a < (Nivel-1); a++)
                Descifrado += Character.toString(Rieles[a].Riel[pasoMedios]);

            pasoMedios++;

            Descifrado += Character.toString(Rieles[(Nivel-1)].Riel[pasoExtremos]);
            pasoExtremos++;

            for (int a = (Nivel-2); a > 0; a--)
                Descifrado += Character.toString(Rieles[a].Riel[pasoMedios]);

            pasoMedios++;
        }

        return Descifrado;
    }



    private String ObtenerExtremos (int caracter, int cantExtremos)
    {
        String SeccionTexto = "";

        while (SeccionTexto.length() < cantExtremos)
        {
            SeccionTexto += Character.toString(Texto[caracter]);
            caracter += niveles[Nivel];
        }

        return SeccionTexto;
    }

    private String ObtenerMedios (int caracter, int cantMedios, int NivelSecundario)
    {
        String SeccionTexto = "";
        int Nivel1 = NivelSecundario;
        int Nivel2 = (Nivel+1)-NivelSecundario;

        while (SeccionTexto.length() < cantMedios)
        {
            SeccionTexto += Character.toString(Texto[caracter]);
            caracter += niveles[Nivel1];

            SeccionTexto += Character.toString(Texto[caracter]);
            caracter += niveles[Nivel2];
        }

        return SeccionTexto;
    }

    // RELLENA EL TEXTO CON espacios PARA QUE LAS OLAS SEAN EXACTAS
    private String Rellenar (String Texto, int Nivel)
    {
        int tOla = (Nivel*2)-2;
        int cantOla = Texto.length()/tOla +1;
        int CantRellenar = (cantOla*tOla)-Texto.length();

        for(int i = 0; i < CantRellenar; i++)
            Texto += " ";

        return Texto;
    }

    // OBTIENE EL TAMAÑO DE LAS OLAS EN TODOS LOS NIVELES (POSICION DE DISTINTOS CARACTERES)
    private int[] ObtenerTamañosOla (int Nivel)
    {
        int[] tamaños = new int[Nivel + 2];

        tamaños[0] = 0;
        tamaños[1] = 0;

        for (int i = 2; i < Nivel + 2; i++)
            tamaños[i] = (i*2)-2;

        return tamaños;
    }


}

