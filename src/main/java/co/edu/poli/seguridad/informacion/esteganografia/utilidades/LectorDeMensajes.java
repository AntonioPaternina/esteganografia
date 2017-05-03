package co.edu.poli.seguridad.informacion.esteganografia.utilidades;

import org.apache.commons.lang3.Conversion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;

import static co.edu.poli.seguridad.informacion.esteganografia.utilidades.EscritorDeMensajes.*;
import static co.edu.poli.seguridad.informacion.esteganografia.utilidades.UtilidadesGenerales
        .MASCARAS_BITS_MENOS_SIGNIFICATIVOS;


public class LectorDeMensajes {

    private BufferedImage imagen;

    private int pixeles[][];

    private int longitudMensaje;
    private StringBuilder mensajeEnTextoPlano = new StringBuilder();

    public String leerMensajeOculto(String rutaImagen) throws IOException {
        cargarImagenEnMemoria(rutaImagen);

        leerLongitudMensajeOculto();

        leerMensajeEnBitsModificados();

        return mensajeEnTextoPlano.toString();
    }

    private void cargarImagenEnMemoria(String rutaImagen) throws IOException {
        imagen = ImageIO.read(new File(rutaImagen));

        pixeles = UtilidadesGenerales.calcularMatrizDePixeles(imagen);
    }

    private void leerMensajeEnBitsModificados() {
        boolean[] caracterTemp = new boolean[BITS_EN_BYTE];
        int contadorByteTemp = 0;

        for (int i = FILA_INICIAL_PARA_MENSAJE; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                for (int k = 0; k < MASCARAS_BITS_MENOS_SIGNIFICATIVOS.length; k++) {
                    if (mensajeEnTextoPlano.length() < longitudMensaje) {
                        caracterTemp = leerBit(caracterTemp, pixeles[i][j], MASCARAS_BITS_MENOS_SIGNIFICATIVOS[k],
                                contadorByteTemp);

                        contadorByteTemp++;
                        if (contadorByteTemp == BITS_EN_BYTE) {
                            mensajeEnTextoPlano.append((char) Conversion.binaryToInt(caracterTemp, 0, 0,
                                    0, BITS_EN_BYTE));
                            contadorByteTemp = 0;
                            caracterTemp = new boolean[BITS_EN_BYTE];
                        }
                    }
                }
            }
        }
    }

    private boolean[] leerBit(boolean[] caracterTemp, int pixel, int mascaraDeModificacion, int indice) {
        if ((pixel & mascaraDeModificacion) != 0) {
            caracterTemp[indice] = true;
        } else {
            caracterTemp[indice] = false;
        }
        return caracterTemp;
    }

    private void leerLongitudMensajeOculto() {
        boolean[] longitudMensaje = obtenerLongitudMensaje();
        this.longitudMensaje = Conversion.binaryToInt(longitudMensaje, 0, 0, 0, longitudMensaje.length);
    }

    private boolean[] obtenerLongitudMensaje() {
        boolean[] longitudDelMensaje = new boolean[BITS_LONGITUD_MENSAJE];
        int indiceBitLongitudMensaje = 0;
        int numeroPixelesParaLongitudDelMensaje = BITS_LONGITUD_MENSAJE / MASCARAS_BITS_MENOS_SIGNIFICATIVOS.length;

        for (int j = 0; (j < numeroPixelesParaLongitudDelMensaje && indiceBitLongitudMensaje < BITS_LONGITUD_MENSAJE)
                ; j++) {
            for (int k = 0; k < MASCARAS_BITS_MENOS_SIGNIFICATIVOS.length; k++) {

                int mascaraDeModificacion = MASCARAS_BITS_MENOS_SIGNIFICATIVOS[k];
                int pixel = pixeles[FILA_PARA_LONGITUD_MENSAJE][j];

                if ((pixel & mascaraDeModificacion) != 0) {
                    longitudDelMensaje[indiceBitLongitudMensaje] = true;
                } else {
                    longitudDelMensaje[indiceBitLongitudMensaje] = false;
                }

                indiceBitLongitudMensaje++;
            }
        }
        return longitudDelMensaje;
    }

    public String getMensajeEnTextoPlano() {
        return mensajeEnTextoPlano.toString();
    }
}
