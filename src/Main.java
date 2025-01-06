import java_cup.runtime.Symbol;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import JFLEX.Lexer;
import CUP.sym;
import jflex.exceptions.SilentExit;

/**
 * Clase principal que realiza el análisis léxico de un archivo de texto
 * utilizando un lexer generado por JFlex y un parser generado por CUP.
 *
 * Funcionalidad:
 * - Genera automáticamente el lexer (a partir de un archivo .jflex).
 * - Genera automáticamente el parser (a partir de un archivo .cup).
 * - Solicita al usuario la ruta de un archivo de entrada.
 * - Realiza un análisis léxico sobre el archivo proporcionado.
 * - Escribe los resultados (tokens, tipo, línea, columna) en un archivo de salida.
 */
public class Main {

    /**
     * Método principal que controla la ejecución del programa.
     *
     * @param args Argumentos de línea de comandos.
     *             Si no se proporciona un archivo, solicita la ruta al usuario.
     */
    public static void main(String[] args) throws SilentExit {

        // Guardar una referencia al flujo de entrada original
        InputStream originalIn = System.in;

        // Rutas de los archivos de configuración para JFlex y CUP
        String lexerFilePath = "src/JFLEX/Lexer.jflex";  // Ruta del archivo Lexer.jflex
        String parserFilePath = "src/CUP/Parser.cup";    // Ruta del archivo Parser.cup

        // Generar el lexer y el parser utilizando JFlex y CUP
        generarLexer(lexerFilePath);
        generarParser(parserFilePath);

        // Restaurar el flujo original de entrada tras ejecutar las funciones generadoras
        System.setIn(originalIn);

        // Instancia para leer entradas desde la consola
        Scanner scanner = new Scanner(System.in);
        String archivo;

        // Bucle para solicitar al usuario la ruta del archivo de entrada
        while (true) {
            System.out.print("Por favor, ingrese la ruta del archivo: ");
            archivo = scanner.nextLine();

            // Validar si el archivo existe y es accesible
            if (Files.exists(Paths.get(archivo)) && Files.isReadable(Paths.get(archivo))) {
                break; // Salir del bucle si la ruta es válida
            } else {
                System.err.println("La ruta proporcionada no es válida o el archivo no es accesible. Inténtelo nuevamente.");
            }
        }

        // Nombre del archivo donde se guardarán los resultados del análisis léxico
        String archivoSalida = "salida.txt";

        // Bloque try-with-resources para manejar correctamente los recursos de entrada y salida
        try (FileReader reader = new FileReader(archivo);
             BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida, false))) {

            // Crear una instancia del lexer configurado con el archivo de entrada
            Lexer lexer = new Lexer(reader);

            Symbol token; // Variable para almacenar el token actual

            // Iniciar el análisis léxico
            while (true) {
                token = lexer.next_token(); // Obtener el siguiente token

                // Verificar si el token no es EOF
                if (token.sym != 0) {
                    // Obtener el nombre del token y su posición en el archivo
                    String tokenName = sym.terminalNames[token.sym];
                    int linea = lexer.getLine() + 1; // Línea (ajustada a índice 1)
                    int columna = lexer.getColumn() + 1; // Columna (ajustada a índice 1)

                    // Mostrar el token en la consola
                    System.out.println("Token: " + tokenName + ", Tipo: " + token.sym + ", Línea: " + linea + ", Columna: " + columna);

                    // Escribir el token en el archivo de salida
                    writer.write("Token: " + tokenName + ", Tipo: " + token.sym + ", Línea: " + linea + ", Columna: " + columna + "\n");

                } else {
                    // Si se alcanza el final del archivo (EOF), finalizar el análisis
                    System.out.println("Análisis terminado: se alcanzó el final del archivo.");
                    break;
                }
            }
        } catch (IOException e) {
            // Manejar errores relacionados con la lectura/escritura de archivos
            System.err.println("Error al leer o escribir archivos: " + e.getMessage());
        } catch (Exception e) {
            // Manejar errores generales durante el análisis léxico
            System.err.println("Error durante el análisis léxico: " + e.getMessage());
        }
    }

    /**
     * Genera el parser (análisis sintáctico) utilizando CUP.
     *
     * @param inputFile La ruta al archivo Parser.cup.
     */
    public static void generarParser(String inputFile) {
        try {
            // Configuración de los argumentos para CUP
            String[] archivoEntrada = {
                    "-destdir", "src/CUP",    // Directorio destino para el parser generado
                    "-parser", "Parser",     // Nombre de la clase del parser
                    "-symbols", "sym",       // Nombre de la clase para los símbolos
                    inputFile                // Ruta del archivo .cup
            };

            // Generar el parser utilizando CUP
            java_cup.Main.main(archivoEntrada);

            // Indicar éxito en la generación
            System.out.println("Parser generado exitosamente.");
        } catch (Exception e) {
            // Manejar errores durante la generación del parser
            System.err.println("Error al generar el parser: " + e.getMessage());
        }
    }

    /**
     * Genera el lexer (análisis léxico) utilizando JFlex.
     *
     * @param inputFile La ruta al archivo Lexer.jflex.
     * @throws SilentExit Excepción que puede lanzar JFlex si ocurre un error.
     */
    public static void generarLexer(String inputFile) throws SilentExit {
        // Configuración de los argumentos para JFlex
        String[] archivoEntrada = { inputFile };

        // Generar el lexer utilizando JFlex
        jflex.Main.generate(archivoEntrada);

        // Indicar éxito en la generación
        System.out.println("Lexer generado exitosamente.");
    }
}