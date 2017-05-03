package co.edu.poli.seguridad.informacion.esteganografia.utilidades;

import java.awt.image.BufferedImage;

public class UtilidadesGenerales {

    static final int[] MASCARAS_BITS_MENOS_SIGNIFICATIVOS = {1, 256, 65536, 16777216};

    public static int[][] calcularMatrizDePixeles(BufferedImage imagen) {
        int x = imagen.getWidth();
        int y = imagen.getHeight();
        int[][] pixeles = new int[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                pixeles[i][j] = imagen.getRGB(i, j);
            }
        }

        return pixeles;
    }
}
