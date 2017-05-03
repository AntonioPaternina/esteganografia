package co.edu.poli.seguridad.informacion.esteganografia;

import co.edu.poli.seguridad.informacion.esteganografia.utilidades.EscritorDeMensajes;
import co.edu.poli.seguridad.informacion.esteganografia.utilidades.LectorDeMensajes;

import java.io.IOException;

public class MainCLI {
    public static void main(String[] args) throws IOException {
        EscritorDeMensajes escritorDeMensajes = new EscritorDeMensajes();

        escritorDeMensajes.ocultarMensajeEnImagen("C:\\Users\\acpat\\Documents\\projects\\poli\\seguridad-de-la" +
                "-informacion\\test.png", "mensaje de prueba");

        LectorDeMensajes lectorDeMensajes = new LectorDeMensajes();

        String mensajeOculto = lectorDeMensajes.leerMensajeOculto
                ("C:\\Users\\acpat\\Documents\\projects\\poli\\seguridad-de-la" +
                "-informacion\\test.png");
        System.out.println(mensajeOculto);
    }
}
