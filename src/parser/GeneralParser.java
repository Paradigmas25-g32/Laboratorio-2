package parser;

/*
 * Esta clase modela los atributos y metodos comunes a todos los distintos tipos de
 * parser existentes en la aplicacion
 * */
public abstract class GeneralParser {

    /**
     * Método abstracto que deben implementar las clases hijas
     * Este método se encarga de parsear una fuente dedatos y convertirla a un tipo T.
     *
     * @param source La fuente a parsear, que puede ser una cadena de caracteres o la ruta del archivo
     * @return un objeto de tipo T que contiene la información parseada
     * */
    public abstract Object parser(String source) throws Exception;

    /**
     * Método helper para loguear errores durante el parseo.
     *
     * @param message El mensaje de error
     * @param e La excepción que causó el error
     */
    protected void logError(String message, Exception e) {
        System.err.println(
            "[" + this.getClass().getSimpleName() + "] ERROR: " + message
        );
        if (e != null) {
            e.printStackTrace();
        }
    }
}
