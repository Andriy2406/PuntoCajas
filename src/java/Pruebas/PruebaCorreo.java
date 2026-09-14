package Pruebas;
import Controlador.EnviarCorreo;

public class PruebaCorreo {

    public static void main(String[] args) {

        EnviarCorreo enviarCorreo =
                new EnviarCorreo();

        boolean enviado =
                enviarCorreo.enviarCodigo(
                        "nelsonocampo08.11@gmail.com",
                        "483921"
                );

        if (enviado) {

            System.out.println(
                    "PRUEBA EXITOSA: El correo fue enviado."
            );

        } else {

            System.out.println(
                    "PRUEBA FALLIDA: No se pudo enviar el correo."
            );
        }
    }
}

