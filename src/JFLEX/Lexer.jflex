package JFLEX;

// Importaciones necesarias
import java_cup.runtime.Symbol;
import CUP.sym;

// Asignacion de reglas
%%
%public
%cup
%class Lexer
%unicode
%line
%column

// Metodos necesarios
%{

private boolean imprimirErrores = true;

public void desactivarImpresionErrores() {
    imprimirErrores = false;
}

public void activarImpresionErrores() {
    imprimirErrores = true;
}

public int getLine() {
    return yyline;
}

public int getColumn() {
    return yycolumn;
}
%}
%%

[$%&/] { /* Ignorar si no están dentro de una cadena */ }
// Literales
[0-9]+ { return new Symbol(sym.INT_LITERAL, yyline + 1, yycolumn + 1, Integer.parseInt(yytext())); }
[0-9]+\.[0-9]+ { return new Symbol(sym.FLOAT_LITERAL, yyline + 1, yycolumn + 1, Float.parseFloat(yytext())); }
"true"|"false" { return new Symbol(sym.BOOL_LITERAL, yyline + 1, yycolumn + 1, Boolean.parseBoolean(yytext())); }
'[^']' { return new Symbol(sym.CHAR_LITERAL, yyline + 1, yycolumn + 1, yytext().charAt(1)); }
"\"([^\"\\\\]|\\\\.)*\"" { return new Symbol(sym.STRING_LITERAL, yyline + 1, yycolumn + 1, yytext().substring(1, yytext().length() - 1)); }

// Comentarios
"#".* { /* Comentario de una línea: ignorar */ }
"\\_"([^\\_]|(\\_([^\\_]|\\n)*\\_))*"_/" { /* Ignorar comentario multilínea */ }

// Ignorar espacios y tabulaciones
[ \t]+ { /* Ignorar */ }

// Detectar fin de línea y avanzar a la siguiente
[\r\n]+ { /* Saltar a la siguiente línea */ }

// Procedimiento Main
"_verano_" { return new Symbol(sym.MAIN, yyline + 1, yycolumn + 1); }

// Palabras reservadas
"rodolfo" { return new Symbol(sym.INTEGER, yyline + 1, yycolumn + 1); }
"bromista" { return new Symbol(sym.FLOAT, yyline + 1, yycolumn + 1); }
"trueno" { return new Symbol(sym.BOOL, yyline + 1, yycolumn + 1); }
"cupido" { return new Symbol(sym.CHAR, yyline + 1, yycolumn + 1); }
"cometa" { return new Symbol(sym.STRING, yyline + 1, yycolumn + 1); }

"abrecuento" { return new Symbol(sym.OPEN_BLOCK, yyline + 1, yycolumn + 1); }
"cierracuento" { return new Symbol(sym.CLOSE_BLOCK, yyline + 1, yycolumn + 1); }
"abreempaque" { return new Symbol(sym.OPEN_BRACKET, yyline + 1, yycolumn + 1); }
"cierraempaque" { return new Symbol(sym.CLOSE_BRACKET, yyline + 1, yycolumn + 1); }
"abreregalo" { return new Symbol(sym.OPEN_PAREN, yyline + 1, yycolumn + 1); }
"cierraregalo" { return new Symbol(sym.CLOSE_PAREN, yyline + 1, yycolumn + 1); }
"entrega" { return new Symbol(sym.ASSIGN, yyline + 1, yycolumn + 1); }
"finregalo" { return new Symbol(sym.END_STATEMENT, yyline + 1, yycolumn + 1); }

// Operadores aritméticos
"navidad" { return new Symbol(sym.PLUS, yyline + 1, yycolumn + 1); }
"intercambio" { return new Symbol(sym.MINUS, yyline + 1, yycolumn + 1); }
"reyes" { return new Symbol(sym.DIVIDE, yyline + 1, yycolumn + 1); }
"nochebuena" { return new Symbol(sym.MULTIPLY, yyline + 1, yycolumn + 1); }
"magos" { return new Symbol(sym.MODULO, yyline + 1, yycolumn + 1); }
"adviento" { return new Symbol(sym.POWER, yyline + 1, yycolumn + 1); }

// Operadores unarios
"quien" { return new Symbol(sym.INCREMENT, yyline + 1, yycolumn + 1); }
"grinch" { return new Symbol(sym.DECREMENT, yyline + 1, yycolumn + 1); }

// Operadores relacionales
"snowball" { return new Symbol(sym.LESS_THAN, yyline + 1, yycolumn + 1); }
"evergreen" { return new Symbol(sym.LESS_EQUAL, yyline + 1, yycolumn + 1); }
"minstix" { return new Symbol(sym.GREATER_THAN, yyline + 1, yycolumn + 1); }
"upatree" { return new Symbol(sym.GREATER_EQUAL, yyline + 1, yycolumn + 1); }
"mary" { return new Symbol(sym.EQUAL, yyline + 1, yycolumn + 1); }
"openslae" { return new Symbol(sym.NOT_EQUAL, yyline + 1, yycolumn + 1); }

// Operadores lógicos
"melchor" { return new Symbol(sym.AND, yyline + 1, yycolumn + 1); }
"gaspar" { return new Symbol(sym.OR, yyline + 1, yycolumn + 1); }
"baltazar" { return new Symbol(sym.NOT, yyline + 1, yycolumn + 1); }

// Estructuras de control
"elfo" { return new Symbol(sym.IF, yyline + 1, yycolumn + 1); }
"hada" { return new Symbol(sym.ELSE, yyline + 1, yycolumn + 1); }
"envuelve" { return new Symbol(sym.WHILE, yyline + 1, yycolumn + 1); }
"duende" { return new Symbol(sym.FOR, yyline + 1, yycolumn + 1); }
"varios" { return new Symbol(sym.SWITCH, yyline + 1, yycolumn + 1); }
"historia" { return new Symbol(sym.CASE, yyline + 1, yycolumn + 1); }
"ultimo" { return new Symbol(sym.DEFAULT, yyline + 1, yycolumn + 1); }
"corta" { return new Symbol(sym.BREAK, yyline + 1, yycolumn + 1); }
"envia" { return new Symbol(sym.RETURN, yyline + 1, yycolumn + 1); }
"sigue" { return new Symbol(sym.COLON, yyline + 1, yycolumn + 1); }

// Funciones I/O
"narra" { return new Symbol(sym.PRINT, yyline + 1, yycolumn + 1); }
"escucha" { return new Symbol(sym.READ, yyline + 1, yycolumn + 1); }

// Cadenas
\"[^\"]*\" {return new Symbol(sym.STRING_LITERAL, yyline + 1, yycolumn + 1, yytext().substring(1, yytext().length() - 1));}

// Literales
[0-9]+ { return new Symbol(sym.INT_LITERAL, yyline + 1, yycolumn + 1, Integer.parseInt(yytext())); }
[0-9]+\.[0-9]+ { return new Symbol(sym.FLOAT_LITERAL, yyline + 1, yycolumn + 1, Float.parseFloat(yytext())); }
"true"|"false" { return new Symbol(sym.BOOL_LITERAL, yyline + 1, yycolumn + 1, Boolean.parseBoolean(yytext())); }
'[^']' { return new Symbol(sym.CHAR_LITERAL, yyline + 1, yycolumn + 1, yytext().charAt(1)); }
"\".*?\"" { return new Symbol(sym.STRING_LITERAL, yyline + 1, yycolumn + 1, yytext().substring(1, yytext().length() - 1)); }

// Identificadores
_([a-zA-Z0-9]+)_ { return new Symbol(sym.IDENTIFIER, yyline + 1, yycolumn + 1, yytext()); }

// Coma como separador
"," { return new Symbol(sym.COMMA, yyline + 1, yycolumn + 1); }

// Llamadas a funciones o variables
// [a-zA-Z][a-zA-Z0-9_]* { return new Symbol(sym.FUNCTION_OR_VARIABLE, yyline + 1, yycolumn + 1, yytext()); }

// Manejo de palabras no reconocidas como un solo error
[a-zA-Z][a-zA-Z0-9_]* {
    if (imprimirErrores) {
        System.err.println("Error léxico: \"" + yytext() + "\" en línea " + (yyline + 1) + ", columna " + (yycolumn + 1));
    }
}

// Manejo de errores léxicos
. {
    if (imprimirErrores) {
        System.err.println("Error léxico: " + yytext() + " en línea " + (yyline + 1) + ", columna " + (yycolumn + 1));
    }
}