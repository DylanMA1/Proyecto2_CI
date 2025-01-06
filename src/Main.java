import java_cup.runtime.Symbol;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import JFLEX.Lexer;
import CUP.sym;
import jflex.exceptions.SilentExit;

/**
 * Clase principal que realiza el análisis léxico y sintáctico de un archivo de texto
 * utilizando un lexer generado por JFlex y un parser generado por CUP.
 */
public class Main {

    // Tabla de símbolos
    private static final List<SymbolTableEntry> symbolTable = new ArrayList<>();

    /**
     * Método principal que controla la ejecución del programa.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) throws SilentExit {

        InputStream originalIn = System.in;

        String lexerFilePath = "src/JFLEX/Lexer.jflex";  // Ruta del archivo Lexer.jflex
        String parserFilePath = "src/CUP/Parser.cup";    // Ruta del archivo Parser.cup

        generarLexer(lexerFilePath);
        generarParser(parserFilePath);

        System.setIn(originalIn);

        Scanner scanner = new Scanner(System.in);
        String archivo;

        while (true) {
            System.out.print("Por favor, ingrese la ruta del archivo: ");
            archivo = scanner.nextLine();

            if (Files.exists(Paths.get(archivo)) && Files.isReadable(Paths.get(archivo))) {
                break;
            } else {
                System.err.println("La ruta proporcionada no es válida o el archivo no es accesible. Inténtelo nuevamente.");
            }
        }

        String archivoSalida = "salida.txt";

        try (FileReader reader = new FileReader(archivo);
             BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida, false))) {

            MultiOutputStream multiOut = new MultiOutputStream(System.out, writer);
            PrintStream printStream = new PrintStream(multiOut);
            System.setOut(printStream);

            Lexer lexer = new Lexer(reader);
            Symbol token;

            while (true) {
                token = lexer.next_token();
                if (token.sym != 0) {
                    String tokenName = sym.terminalNames[token.sym];
                    int linea = lexer.getLine() + 1;
                    int columna = lexer.getColumn() + 1;

                    String tipo = getTokenType(token.sym);

                    System.out.println("Token: " + tokenName + ", Tipo: " + tipo + ", Línea: " + linea + ", Columna: " + columna);

                    // Agregar token a la tabla de símbolos
                    addToSymbolTable(token, tokenName, tipo, linea, columna);

                } else {
                    System.out.println("Análisis terminado: se alcanzó el final del archivo.");
                    break;
                }
            }

            System.out.println("\nTabla de símbolos:");
            for (SymbolTableEntry entry : symbolTable) {
                System.out.println(entry);
            }

        } catch (IOException e) {
            System.err.println("Error al leer o escribir archivos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error durante el análisis léxico: " + e.getMessage());
        }
    }

    /**
     * Retorna el tipo asociado a un token según su símbolo.
     *
     * @param sym El símbolo del token.
     * @return El tipo asociado o "N/A" si no aplica.
     */
    private static String getTokenType(int sym) {
        switch (sym) {
            case CUP.sym.INTEGER:
                return "int";
            case CUP.sym.FLOAT:
                return "float";
            case CUP.sym.BOOL:
                return "bool";
            case CUP.sym.CHAR:
                return "char";
            case CUP.sym.STRING:
                return "string";
            case CUP.sym.IDENTIFIER:
                return "variable";
            case CUP.sym.MAIN:
                return "main";
            default:
                return "Sin tipo";
        }
    }

    /**
     * Agrega un token a la tabla de símbolos.
     *
     * @param token El token identificado.
     * @param tokenName El nombre del token.
     * @param tipo El tipo asociado (si es aplicable, ej. variables o funciones).
     * @param linea La línea donde se encuentra el token.
     * @param columna La columna donde se encuentra el token.
     */
    private static void addToSymbolTable(Symbol token, String tokenName, String tipo, int linea, int columna) {
        SymbolTableEntry entry = new SymbolTableEntry(tokenName, token.value, tipo, linea, columna);
        symbolTable.add(entry);
    }

    public static void generarParser(String inputFile) {
        try {
            String[] archivoEntrada = {
                    "-destdir", "src/CUP",
                    "-parser", "Parser",
                    "-symbols", "sym",
                    inputFile
            };

            java_cup.Main.main(archivoEntrada);
            System.out.println("Parser generado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al generar el parser: " + e.getMessage());
        }
    }

    public static void generarLexer(String inputFile) throws SilentExit {
        String[] archivoEntrada = { inputFile };
        jflex.Main.generate(archivoEntrada);
        System.out.println("Lexer generado exitosamente.");
    }
}

/**
 * Clase para representar una entrada en la tabla de símbolos.
 */
class SymbolTableEntry {
    private final String name;
    private final Object value;
    private final String type; // Tipo asociado, ej. int, float, función
    private final int line;
    private final int column;

    public SymbolTableEntry(String name, Object value, String type, int line, int column) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return "Símbolo: " + name +
                ", Valor: " + value +
                ", Tipo: " + type +
                ", Línea: " + line +
                ", Columna: " + column;
    }
}

/**
 * Clase para redirigir la salida a múltiples flujos.
 */
class MultiOutputStream extends OutputStream {
    private final OutputStream console;
    private final Writer fileWriter;

    public MultiOutputStream(OutputStream console, Writer fileWriter) {
        this.console = console;
        this.fileWriter = fileWriter;
    }

    @Override
    public void write(int b) throws IOException {
        console.write(b);
        fileWriter.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        console.write(b, off, len);
        fileWriter.write(new String(b, off, len));
    }

    @Override
    public void flush() throws IOException {
        console.flush();
        fileWriter.flush();
    }

    @Override
    public void close() throws IOException {
        console.close();
        fileWriter.close();
    }
}