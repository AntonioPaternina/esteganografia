package co.edu.poli.seguridad.informacion.esteganografia.cli;

import co.edu.poli.seguridad.informacion.esteganografia.utilidades.EscritorDeMensajes;
import co.edu.poli.seguridad.informacion.esteganografia.utilidades.LectorDeMensajes;
import org.apache.commons.cli.*;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class InterfazCLI {

    private static Logger logger = LoggerFactory.getLogger(InterfazCLI.class.getName());

    public static void main(String args[]) throws ParseException, IOException, ImageWriteException, ImageReadException {
        Options opciones = construirOpciones();
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = commandLineParser.parse(opciones, args);

        validarArgumentos(opciones, commandLine);

        procesarComando(commandLine);
    }

    private static void procesarComando(CommandLine commandLine) throws IOException, ImageWriteException,
            ImageReadException {
        String rutaImagen = commandLine.getOptionValue("i");
        if (commandLine.hasOption("o")) {
            String mensaje = commandLine.getOptionValue("m");

            EscritorDeMensajes escritorDeMensajes = new EscritorDeMensajes();
            escritorDeMensajes.ocultarMensajeEnImagen(rutaImagen, mensaje);

            logger.info("se ha ocultado el mensaje correctamente");
        }

        if (commandLine.hasOption("l")) {
            LectorDeMensajes lectorDeMensajes = new LectorDeMensajes();
            String mensaje = lectorDeMensajes.leerMensajeOculto(rutaImagen);

            logger.info("el mensaje oculto es:");
            logger.info(mensaje);
        }
    }

    private static void validarArgumentos(Options opciones, CommandLine commandLine) {
        if (!commandLine.hasOption("o") && !commandLine.hasOption("l")) {
            imprimirAyuda(opciones, 80, "Ayuda Utilidad Esteganografia", "Fin de la ayuda",
                    5, 3, true, System.out);
            return;
        }

        if (!commandLine.hasOption("i")) {
            imprimirAyuda(opciones, 80, "Ayuda Utilidad Esteganografia", "Fin de la ayuda",
                    5, 3, true, System.out);
            return;
        }

        if (commandLine.hasOption("o") && (!commandLine.hasOption("m"))) {
            imprimirAyuda(opciones, 80, "Ayuda Utilidad Esteganografia", "Fin de la ayuda",
                    5, 3, true, System.out);
            return;
        }

    }

    public static Options construirOpciones() {
        final Options opciones = new Options();
        opciones.addOption("o", "ocultar", false, "ocultar un mensaje en una imagen")
                .addOption("l", "leer", false, "leer un mensaje oculto en una imagen")
                .addOption("i", "imagen", true, "ruta de la imagen")
                .addOption("m", "mensaje", true, "mensaje a ocultar");

        return opciones;
    }

    public static void imprimirAyuda(final Options opciones, final int anchoFila, final String encabezado, final
    String pie, final int espaciosAntesDeLaOpcion, final int espaciosAntesDeLaDescripcionDeLaOpcion, final boolean
                                             mostrarUso,
                                     final OutputStream out) {
        final String commandLineSyntax = "java esteganografia.jar";
        final PrintWriter writer = new PrintWriter(out);
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(
                writer,
                anchoFila,
                commandLineSyntax,
                encabezado,
                opciones,
                espaciosAntesDeLaOpcion,
                espaciosAntesDeLaDescripcionDeLaOpcion,
                pie,
                mostrarUso);
        writer.flush();
    }
}
