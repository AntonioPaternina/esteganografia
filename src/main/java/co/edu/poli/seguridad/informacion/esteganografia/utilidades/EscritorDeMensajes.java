package co.edu.poli.seguridad.informacion.esteganografia.utilidades;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Conversion;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static co.edu.poli.seguridad.informacion.esteganografia.utilidades.UtilidadesGenerales
        .MASCARAS_BITS_MENOS_SIGNIFICATIVOS;


public class EscritorDeMensajes {

    public static final String FORMATO_IMAGEN = "png";
    public static final int BITS_LONGITUD_MENSAJE = 32;
    public static final int COLUMNA_PARA_LONGITUD_MENSAJE = 0;
    public static final int COLUMNA_INICIAL_PARA_MENSAJE = 1;
    public static final int BITS_EN_BYTE = 8;
    public static final String SUFIJO_ARCHIVO_MODIFICADO = "-modificado";

    private BufferedImage imagenOriginal;
    private File archivoOriginal;
    private BufferedImage imagenModificada;
    private File archivoModificado;
    private int pixeles[][];
    private char caracteresMensaje[];
    private boolean caracterEnBinario[];
    private int anchoImagen;
    private int altoImagen;
    private int longitudMensaje;
    private boolean longitudMensajeEnBinario[];

    public void ocultarMensajeEnImagen(String rutaImagen, String mensaje) throws IOException, ImageReadException,
            ImageWriteException {
        cargarImagenEnMemoria(rutaImagen);

        guardarLongitudDelMensajeEnImagen(mensaje);

        guardarMensajeEnImagen(mensaje);

        actualizarImagenEnDisco();
    }

    private void actualizarImagenEnDisco() throws IOException, ImageWriteException {
        Sanselan.writeImage(this.imagenModificada, this.archivoModificado, ImageFormat.IMAGE_FORMAT_PNG, null);
    }

    private void guardarMensajeEnImagen(String mensaje) {
        this.caracteresMensaje = mensaje.toCharArray();

        ocultarMensajeEnPixeles();
    }

    private void guardarLongitudDelMensajeEnImagen(String mensaje) {
        calcularLongitudEnBinario(mensaje);

        ocultarLongitudEnPixeles();
    }

    private void cargarImagenEnMemoria(String rutaImagen) throws IOException, ImageReadException {
        this.archivoOriginal = new File(rutaImagen);
        String ruta = this.archivoOriginal.getPath();
        this.archivoModificado = new File(FilenameUtils.getFullPath(ruta) + FilenameUtils.getBaseName(ruta) +
                SUFIJO_ARCHIVO_MODIFICADO + "." + FORMATO_IMAGEN);

        this.imagenOriginal = Sanselan.getBufferedImage(this.archivoOriginal);

        this.imagenModificada = new BufferedImage(this.imagenOriginal.getWidth(), this.imagenOriginal.getHeight(),
                BufferedImage.TYPE_4BYTE_ABGR);
        this.anchoImagen = this.imagenModificada.getWidth();
        this.altoImagen = this.imagenModificada.getHeight();

        this.pixeles = UtilidadesGenerales.calcularMatrizDePixeles(this.imagenOriginal);
    }

    private void ocultarMensajeEnPixeles() {

        int indiceCaracter = 0;
        int indiceBitDeCaracter = 0;
        this.caracterEnBinario = codificarCaracterEnBinario((int) this.caracteresMensaje[indiceCaracter]);

        for (int i = COLUMNA_INICIAL_PARA_MENSAJE; i < this.anchoImagen && indiceCaracter < this
                .caracteresMensaje.length; i++) {
            for (int j = 0; j < this.altoImagen && indiceCaracter < this
                    .caracteresMensaje.length; j++) {
                for (int k = 0; k < MASCARAS_BITS_MENOS_SIGNIFICATIVOS.length && indiceCaracter < this
                        .caracteresMensaje.length; k++) {

                    int mascara = MASCARAS_BITS_MENOS_SIGNIFICATIVOS[k];

                    boolean bit = caracterEnBinario[indiceBitDeCaracter++];
                    settearBitEnPixel(bit, i, j, mascara);

                    if (indiceBitDeCaracter == BITS_EN_BYTE) {
                        indiceBitDeCaracter = 0;
                        indiceCaracter++;
                        if (indiceCaracter < this.caracteresMensaje.length) {
                            this.caracterEnBinario = codificarCaracterEnBinario((int) this
                                    .caracteresMensaje[indiceCaracter]);
                        }
                    }
                }
            }
        }

        actualizarMatrizPixeles();
    }

    private void settearBitEnPixel(boolean bit, int i, int j, int mascara) {
        if (bit == false) {
            this.pixeles[i][j] = this.pixeles[i][j] & ~mascara;
        } else if (bit == true) {
            this.pixeles[i][j] = this.pixeles[i][j] | mascara;
        }
    }

    private void actualizarMatrizPixeles() {
        for (int i = 0; i < this.anchoImagen; i++) {
            for (int j = 0; j < this.altoImagen; j++) {
                int nuevoPixel = this.pixeles[i][j];
                this.imagenModificada.setRGB(i, j, nuevoPixel);
            }
        }
    }

    private boolean[] codificarCaracterEnBinario(int caracter) {
        boolean[] caracterEnBinario = new boolean[BITS_EN_BYTE];

        Conversion.intToBinary(caracter, 0, caracterEnBinario, 0, BITS_EN_BYTE);

        return caracterEnBinario;
    }

    private void ocultarLongitudEnPixeles() {
        int numeroPixelesPorModificar = BITS_LONGITUD_MENSAJE / MASCARAS_BITS_MENOS_SIGNIFICATIVOS.length;
        int indiceBitEnMensaje = 0;

        for (int j = 0; (j < numeroPixelesPorModificar && indiceBitEnMensaje < BITS_LONGITUD_MENSAJE); j++) {
            for (int k = 0; k < MASCARAS_BITS_MENOS_SIGNIFICATIVOS.length; k++) {

                boolean bit = this.longitudMensajeEnBinario[indiceBitEnMensaje];
                int mascaraBitsPorModificar = MASCARAS_BITS_MENOS_SIGNIFICATIVOS[k];

                settearBitEnPixel(bit, COLUMNA_PARA_LONGITUD_MENSAJE, j, mascaraBitsPorModificar);

                indiceBitEnMensaje++;
            }
        }
    }

    private void calcularLongitudEnBinario(String mensaje) {
        this.longitudMensaje = mensaje.length();
        this.longitudMensajeEnBinario = new boolean[BITS_LONGITUD_MENSAJE];
        Conversion.intToBinary(this.longitudMensaje, 0, this.longitudMensajeEnBinario, 0, BITS_LONGITUD_MENSAJE);
    }
}
