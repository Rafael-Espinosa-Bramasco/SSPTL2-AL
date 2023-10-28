/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package App;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rafael
 */
public class AL extends javax.swing.JFrame {

    /**
     * Creates new form AL
     */
    public AL() {
        initComponents();
        this.input = new ArrayList<>();
        this.tokenArray = new ArrayList<>();
        this.tokenLinesArray = new ArrayList<>();
        
        this.tableModel = (DefaultTableModel) this.tokensTable.getModel();
        
        this.newToken = new String();
        
        this.isFilled = false;
    }
    
    // Variables
        // Booleans
        boolean isFilled;
    
        // Strings
        String newToken;
    
        // Arrays
        ArrayList<Character> input;
        ArrayList<String> tokenArray;
        ArrayList<ArrayList> tokenLinesArray;
        ArrayList<ArrayList> LinesOfCode;
        ArrayList<ArrayList<Integer>> KEYS;
    
        // Table Model
        DefaultTableModel tableModel;
    
    // Functions
    private void addToTable(String token){
        ArrayList<String> tokenData = this.tokenFilter(token);
        
        this.tableModel.addRow(new Object[]{token, tokenData.get(0), tokenData.get(1)});
    }
    
    private boolean isIdentifier(char c){
        switch(Character.toLowerCase(c)){
            case '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'ñ', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' -> {return true;}
            default -> {return false;}
        }
    }
    
    private boolean isAlpha(char c){
        switch(Character.toLowerCase(c)){
            case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'ñ', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' -> {return true;}
            default -> {return false;}
        }
    }
    
    private boolean isPreprocessor(char c){
        return c == '#';
    }
    
    private boolean isNumeric(char c){
        switch(c){
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {return true;}
            default -> {return false;}
        }
    }
    
    private boolean isOperator(char c){
        switch(c){
            case ',', '.', '+', '-', '*', '/', '%' -> {return true;}
            case '{', '}', '[', ']', '(', ')', '<', '>', '=', '!', '&', '|' -> {return true;}
            default -> {return false;}
        }
    }
    
    private boolean isIgnorable(char c){
        switch(c){
            case ' ', '\t' -> {return true;}
            default -> {return false;}
        }
    }
    
    private boolean isSemiColon(char c){
        return c == ';';
    }
    
    private boolean isSpace(char c){
        return c == ' ';
    }
    
    private boolean isString(char c){
        return c == '"';
    }
    
    private boolean isJump(char c){
        return c == '\n';
    }
    
    private void clearComments(){
        for(int i = 0; i < this.input.size() ; i++){
            if(!this.input.isEmpty() && this.input.get(i) == '/' && (this.input.size() - 1 >= i + 1) && this.input.get(i + 1) == '/'){
                while(!this.input.isEmpty() && this.input.get(i) != '\n'){
                    this.input.remove(i);
                }
            }
        }
    }

    private void startP(){
        this.clearComments();
        
        while(!this.input.isEmpty()){
            
            char act = this.input.get(0);
            
            if(this.isIdentifier(act)){
                this.pI();
            }
            else if(this.isOperator(act)){
                this.pO();
            }
            else if(this.isPreprocessor(act)){
                this.pP();
            }
            else if(this.isNumeric(act)){
                this.pN();
            }
            else if(this.isString(act)){
                this.pS();
            }
            else if(this.isIgnorable(act)){
                this.input.remove(0);
            }
            else if(this.isSemiColon(act)){
                this.input.remove(0);
                this.tokenArray.add(";");
            }
            else if(this.isJump(act)){
                this.input.remove(0);
                this.tokenLinesArray.add(this.tokenArray);
                this.tokenArray = new ArrayList();
            }
        }
        
        if(!this.tokenArray.isEmpty()){
            this.tokenLinesArray.add(this.tokenArray);
            this.tokenArray = new ArrayList();
        }
        
        for(int i = 0; i < this.tokenLinesArray.size() ; i++){
            for(int j = 0 ; j < this.tokenLinesArray.get(i).size() ; j++){
                this.addToTable(this.tokenLinesArray.get(i).get(j).toString());
            }
        }
        
        this.LinesOfCode = (ArrayList) this.tokenLinesArray.clone();
        this.tokenLinesArray.clear();
    }
    
    private void pS(){
        this.newToken += '"';
        this.input.remove(0);
        
        while(!this.input.isEmpty() && this.input.get(0) != '"'){
            this.newToken += this.input.get(0);
            this.input.remove(0);
        }
        
        if(!this.input.isEmpty() && this.input.get(0) == '"'){
            this.newToken += '"';
            this.input.remove(0);
        }
        
        this.tokenArray.add(newToken);
        this.newToken = new String();
    }
    
    private void pN(){
        
        this.newToken += this.input.get(0);
        this.input.remove(0);
        
        while(!this.input.isEmpty() && this.isNumeric(this.input.get(0))){
            this.newToken += this.input.get(0);
            this.input.remove(0);
        }
        
        if(this.input.get(0) == '.'){
            this.newToken += '.';
            this.input.remove(0);
            
            while(!this.input.isEmpty() && this.isNumeric(this.input.get(0))){
                this.newToken += this.input.get(0);
                this.input.remove(0);
            }
        }
        
        this.tokenArray.add(newToken);
        this.newToken = new String();
    }
    
    private void pP(){
        this.input.remove(0);
        this.newToken += '#';
        
        while(!this.input.isEmpty() && this.isAlpha(this.input.get(0))){
            this.newToken += this.input.get(0);
            this.input.remove(0);
        }
        
        if(this.newToken.length() > 0){
            this.tokenArray.add(newToken);
            this.newToken = new String();
        }
    }
    
    private void pO(){
        
        if(this.input.get(0) == '<'){
            this.newToken += '<';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '!'){
            this.newToken += '!';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '&'){
            this.newToken += '&';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '&'){
                this.newToken += '&';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '>'){
            this.newToken += '>';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '+'){
            this.newToken += '+';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else if (!this.input.isEmpty() && this.input.get(0) == '+'){
                this.newToken += '+';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
            else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '-'){
            this.newToken += '-';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else if (!this.input.isEmpty() && this.input.get(0) == '-'){
                this.newToken += '-';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
            else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '='){
            this.newToken += '=';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '.'){
            this.tokenArray.add(".");
            this.input.remove(0);
        }
        else if(this.input.get(0) == '{'){
            this.tokenArray.add("{");
            this.input.remove(0);
        }
        else if(this.input.get(0) == '}'){
            this.tokenArray.add("}");
            this.input.remove(0);
        }
        else if(this.input.get(0) == '('){
            this.tokenArray.add("(");
            this.input.remove(0);
        }
        else if(this.input.get(0) == ')'){
            this.tokenArray.add(")");
            this.input.remove(0);
        }
        else if(this.input.get(0) == '['){
            this.tokenArray.add("[");
            this.input.remove(0);
        }
        else if(this.input.get(0) == ']'){
            this.tokenArray.add("]");
            this.input.remove(0);
        }
        else if(this.input.get(0) == ','){
            this.tokenArray.add(",");
            this.input.remove(0);
        }
        else if(this.input.get(0) == '^'){
            this.tokenArray.add("^");
            this.input.remove(0);
        }
        else if(this.input.get(0) == '*'){
            this.newToken += '*';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '/'){
            this.newToken += '/';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else if(!this.input.isEmpty() && this.input.get(0) == '/'){
                this.newToken += '/';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
        else if(this.input.get(0) == '%'){
            this.newToken += '%';
            this.input.remove(0);
            
            if(!this.input.isEmpty() && this.input.get(0) == '='){
                this.newToken += '=';
                this.input.remove(0);
                this.tokenArray.add(newToken);
                this.newToken = new String();
            } else{
                this.tokenArray.add(newToken);
                this.newToken = new String();
            }
        }
    }
    
    private void pI(){
        this.newToken += this.input.get(0);
        this.input.remove(0);
        
        while(!this.input.isEmpty() && !this.isSpace(this.input.get(0)) && !this.isOperator(this.input.get(0)) && !this.isSemiColon(this.input.get(0))){
            this.newToken += this.input.get(0);
            this.input.remove(0);
        }
        
        if(!this.input.isEmpty() && this.isSpace(this.input.get(0))){
            this.input.remove(0);
        }
        
        if(this.newToken.length() > 0){
            this.tokenArray.add(this.newToken);
            this.newToken = new String();
        }
    }

    //Function to identify the type of the token received
    private ArrayList<String> tokenFilter(String token){
        ArrayList<String> result = new ArrayList<>();
        
        int i=0;
        boolean numberFlag = true;
        
        String numberType = "Numero ";
        String type = "1"; //asuming its a number
        
        if(token.charAt(0)=='"' || token.charAt(token.length()-1)=='"'){result.add("Cadena ".concat(token)); result.add("2"); return result;}
        
        if(token.charAt(0)=='.'){numberFlag = false;}
        // This while loop identifies numbers
        while(numberFlag && i<token.length()){ 
            switch(token.charAt(i)){
                case '0', '1', '2', '3', '4', '5', '6', '7', '9' -> {}
                case '.' -> {numberType = numberType.concat("Real "); type = "3";}
                default -> {numberFlag = false;}
            }
            i++;
        }
        
        if(numberFlag){result.add(numberType.concat(token)); result.add(type); return result;}
        
        if(token.charAt(0) == '#'){
            result.add("Directiva del Preprocesador");
            result.add("4");
            return result;
        }
        
        switch(token){
            
            case "int", "void", "float", "string", "char" -> {result.add("Palabra reservada ".concat(token)); result.add("5");}
            case "+", "-", "+=", "-=", "++", "--" -> {result.add("Operador suma"); result.add("6");}
            case "*", "/", "*=", "/=" -> {result.add("Operador de Multiplicacion"); result.add("7");}
            case "%", "%=" -> {result.add("Operador de Modulo"); result.add("8");}
            case "//" -> {result.add("Operador de Division Entera"); result.add("9");}
            case "^" -> {result.add("Operador de Potencia"); result.add("10");}
            case "<", "<=", ">", ">=" -> {result.add("Operador Relacional"); result.add("11");}
            case "||" -> {result.add("Operador Or"); result.add("12");}
            case "|" -> {result.add("Operador sobre Bits Or"); result.add("13");}
            case "&&" -> {result.add("Operador And"); result.add("14");}
            case "&" -> {result.add("Operador sobre Bits And"); result.add("15");}
            case "!" -> {result.add("Operador Not"); result.add("16");}
            case "==", "!=" -> {result.add("Operador Igualdad"); result.add("17");}
            case ";" -> {result.add("Punto y coma"); result.add("0");}
            case "," -> {result.add("Coma"); result.add("18");}
            case "(" -> {result.add("Parentesis de Apertura"); result.add("19");}
            case ")" -> {result.add("Parentesis de Cierre"); result.add("20");}
            case "{" -> {result.add("Llave de Apertura"); result.add("21");}
            case "}" -> {result.add("Llave de Cierre"); result.add("22");}
            case "[" -> {result.add("Parentesis cuadrado de apertura"); result.add("23");}
            case "]" -> {result.add("Parentesis cuadrado de cierre"); result.add("24");}
            case "=" -> {result.add("Signo de Asignacion"); result.add("25");}
            case "if" -> {result.add("Palabra reservada if"); result.add("26");}
            case "while" -> {result.add("Palabra reservada while"); result.add("27");}
            case "for" -> {result.add("Palabra reservada for"); result.add("28");}
            case "do" -> {result.add("Palabra reservada do"); result.add("29");}
            case "return" -> {result.add("Palabra reservada return"); result.add("30");}
            case "else" -> {result.add("Palabra reservada else"); result.add("31");}
            case "$" -> {result.add("Palabra reservada $"); result.add("32");}
            case "." -> {result.add("Operador ."); result.add("33");}
            default -> {result.add("Identificador ".concat(token)); result.add("34");}
        }
        
        return result;
    }
    
    private boolean isType(String T){
        switch(T){
            case "int", "void", "char", "string", "float" -> {return true;}
        }
        
        return false;
    }
    
    private boolean isBlock(String T){
        switch(T){
            case "if", "while" -> {return true;}
        }
        
        return false;
    }
    
    private boolean isLetter(char c){
        int x = (int) c;
        return ((x >= 97 && x <= 122) || (x >= 65 && x <= 90));
    }
    private boolean isNum(char c){
        int x = (int) c;
        return (x >= 48 && x <= 57);
    }
    private boolean isOpenPar(char c){
        return c == '(';
    }
    private boolean isClosePar(char c){
        return c == ')';
    }
    private boolean isOpenPar(String s){
        return "(".equals(s);
    }
    private boolean isClosePar(String s){
        return ")".equals(s);
    }
    private boolean isOP(String s){
        switch(s){
            case "+","-","*","/" -> {return true;}
            default -> {return false;}
        }
    }
    private boolean isLOP(String s){
        switch(s){
            case "&&","||","!",">","<",">=","<=","==","!=" -> {return true;}
            default -> {return false;}
        }
    }
    
    private boolean isSC(String T){
        return ";".equals(T);
    }
    
    private boolean isNumber(String num){
        for(int i = 0 ; i < num.length() ; i++){
            if(!isNum(num.charAt(i))){
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isFloat(String num){
        if(num.indexOf('.') == -1){
            return false;
        }
        
        int p = 0;
        for(int i = 0 ; i < num.length() ; i++){
            if('.' == num.charAt(i)){
                p = i;
                break;
            }else if(!isNum(num.charAt(i))){
                return false;
            }
        }
        
        for(p = p ; p < num.length() ; p++){
            if(!isNum(num.charAt(p))){
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isIdentifier(String id){
        return isLetter(id.charAt(0)) && !isReserved(id);
    }
    
    private boolean isReserved(String T){
        switch(T){
            case "int", "void", "float", "string", "char", "if", "while", "for", "do", "return", "else" -> {return true;}
        }
        return false;
    }
    
    private boolean startSyntacticAnalysis(){
        ArrayList<Integer> LINES;
        HashMap<ArrayList<Integer>, String> TOKENS = new HashMap<>();
        KEYS = new ArrayList<>();
        
        for(int i = 0; i < this.LinesOfCode.size() ; i++){
            for(int j = 0 ; j < this.LinesOfCode.get(i).size() ; j++){
                LINES = new ArrayList<>();
                LINES.add(i+1);
                LINES.add(j+1);
                TOKENS.put(LINES, (String) LinesOfCode.get(i).get(j));
                KEYS.add(LINES);
            }
        }
        
        return PROGRAM(TOKENS);
    }
    
    private void showErrorMessage(String ET,HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        addSAMessage("Token: ");
        addSAMessage(TOKENS_LINES.get(KEYS.get(0)));
        addSAMessage(" not expected.\n");
        String Line = String.valueOf(KEYS.get(0).get(0));
        String TN = String.valueOf(KEYS.get(0).get(1));
        addSAMessage("At Line ".concat(Line).concat(", Line Token Number ").concat(TN).concat(".\n"));
        
        boolean flag = true;
        int spaces = 0;
        for(int i = 0 ; i < LinesOfCode.get(KEYS.get(0).get(0) - 1).size() ; i++){
            addSAMessage(((String) LinesOfCode.get(KEYS.get(0).get(0) - 1).get(i)) + ' ');
            if(((String) LinesOfCode.get(KEYS.get(0).get(0) - 1).get(i)).compareTo(TOKENS_LINES.get(KEYS.get(0)))==0){
                flag = false;
                spaces++;
            }
            if(flag){
                spaces += ((String) LinesOfCode.get(KEYS.get(0).get(0) - 1).get(i)).length();
                spaces++;
            }
        }
        addSAMessage("\n");
        for(int i = 0 ; i < spaces ; i++){
            addSAMessage("-");
        }
        addSAMessage("^\n");
        addSAMessage("Expected: ".concat(ET).concat(".\n"));
        addSAMessage("\n");
    }
    
    // Funciones de Gramatica
    private boolean PROGRAM(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        return STATEMENTS(TOKENS_LINES);
    }
    
    private boolean STATEMENTS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        boolean S = STATEMENT(TOKENS_LINES);
        if(!S){
            return false;
        }
        boolean MS = MORE_STATEMENTS(TOKENS_LINES);
        return S && MS;
    }
    
    private boolean STATEMENT(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }

        // check decl or block
        boolean F;
        if(isBlock(TOKENS_LINES.get(KEYS.get(0)))){
            F = BLOCK(TOKENS_LINES);
        }else{
            F = DECLARATION(TOKENS_LINES);
        }
        
        return F;
    }
    
    private boolean MORE_STATEMENTS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            return true;
        }
        
        boolean S = STATEMENT(TOKENS_LINES);
        if(!S){
            return false;
        }
        boolean MS = MORE_STATEMENTS(TOKENS_LINES);
        
        return S && MS;
    }
    
    private boolean DECLARATION(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        boolean x = false;
        
        if(isType(TOKENS_LINES.get(KEYS.get(0)))){
            if(! (x = INIT(TOKENS_LINES))){
                return false;
            }
        }else if(isIdentifier(TOKENS_LINES.get(KEYS.get(0))) && !isReserved(TOKENS_LINES.get(KEYS.get(0)))){
            if(! (x = CALL(TOKENS_LINES))){
                return false;
            }
        }
        
        return x;
    }
    
    private boolean INIT(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        // Check Type
        if(isType(TOKENS_LINES.get(KEYS.get(0))) && !TOKENS_LINES.isEmpty()){
            TYPE(TOKENS_LINES);
        }else{
            return false;
        }
        
        // Check ID
        if(isIdentifier(TOKENS_LINES.get(KEYS.get(0))) && !isReserved(TOKENS_LINES.get(KEYS.get(0))) && !TOKENS_LINES.isEmpty()){
            ID(TOKENS_LINES);
        }else{
            return false;
        }
        
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        // REST...
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "=", ",", "(" -> {
                return INIT_TYPE(TOKENS_LINES);
            }
            default -> {
                return false;
            }
        }
    }
    
    private boolean INIT_TYPE(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "=" -> {
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                if(!EXPRESSION(TOKENS_LINES)){
                    return false;
                }
                
                // semicolon
                if(";".equals(TOKENS_LINES.get(KEYS.get(0)))){
                    TOKENS_LINES.remove(KEYS.get(0));
                    KEYS.remove(0);
                    
                    return true;
                }else{
                    showErrorMessage(";",TOKENS_LINES);
                    return false;
                }
            }
            case "," -> {
                return MORE_INITS(TOKENS_LINES);
            }
            case "(" -> {
                
                HashMap<ArrayList<Integer>, String> INIT_PARAMS_HASH = new HashMap<>();
                
                INIT_PARAMS_HASH.put(KEYS.get(0), "(");
                
                TOKENS_LINES.remove(KEYS.get(0));
                
                int openPar = 1;
                int keyIndex = 1;
                while(openPar > 0 && keyIndex < KEYS.size()){
                    if("(".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                        openPar++;
                    }else if(")".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                        openPar--;
                    }
                    
                    INIT_PARAMS_HASH.put(KEYS.get(keyIndex), TOKENS_LINES.get(KEYS.get(keyIndex)));
                    TOKENS_LINES.remove(KEYS.get(keyIndex));
                    keyIndex++;
                }
                
                if(openPar >= 1){
                    showErrorMessage(")",TOKENS_LINES);
                    return false;
                }else{
                    if(INIT_PARAMS(INIT_PARAMS_HASH)){
                        
                        if(!("{".equals(TOKENS_LINES.get(KEYS.get(0))))){
                            showErrorMessage("{",TOKENS_LINES);
                            return false;
                        }
                        
                        HashMap<ArrayList<Integer>, String> FSTATEMENTS_HASH = new HashMap<>();
                        
                        FSTATEMENTS_HASH.put(KEYS.get(0), "{");
                
                        TOKENS_LINES.remove(KEYS.get(0));
                        
                        int openKey = 1;
                        int keyIndex2 = 1;
                        while(openKey > 0 && keyIndex2 < KEYS.size()){
                            if("{".equals(TOKENS_LINES.get(KEYS.get(keyIndex2)))){
                                openKey++;
                            }else if("}".equals(TOKENS_LINES.get(KEYS.get(keyIndex2)))){
                                openKey--;
                            }

                            FSTATEMENTS_HASH.put(KEYS.get(keyIndex2), TOKENS_LINES.get(KEYS.get(keyIndex2)));
                            TOKENS_LINES.remove(KEYS.get(keyIndex2));
                            keyIndex2++;
                        }
                        
                        if(openKey >= 1){
                            showErrorMessage("}",TOKENS_LINES);
                            return false;
                        }else{
                            return FSTATEMENTS(FSTATEMENTS_HASH);
                        }
                    }else{
                        return false;
                    }
                }
            }
            default -> {
                return false;
            }
        }
    }
    
    private boolean INIT_PARAMS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        TOKENS_LINES.remove(KEYS.get(0));
        KEYS.remove(0);

        int S = TOKENS_LINES.size() - 1;
        TOKENS_LINES.remove(KEYS.get(S));
        KEYS.remove(S);
        
        if(TYPE(TOKENS_LINES)){
            if(ID(TOKENS_LINES)){
                return MORE_PARAMS(TOKENS_LINES);
            }
            return false;
        }
        
        return false;
    }
    
    private boolean MORE_PARAMS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            return true;
        }
        
        if(",".equals(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            if(TYPE(TOKENS_LINES)){
                if(ID(TOKENS_LINES)){
                    return MORE_PARAMS(TOKENS_LINES);
                }
                return false;
            }
            return false;
        }
        
        showErrorMessage(",",TOKENS_LINES);
        return false;
    }
    
    private boolean FSTATEMENTS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        TOKENS_LINES.remove(KEYS.get(0));
        KEYS.remove(0);

        int S = TOKENS_LINES.size() - 1;
        TOKENS_LINES.remove(KEYS.get(S));
        KEYS.remove(S);
        
        boolean x,y;
        
        x = FSTATEMENT(TOKENS_LINES);
        if(!x){
            return false;
        }
        y = MORE_FSTATEMENTS(TOKENS_LINES);
        
        return x && y;
    }
    
    private boolean FSTATEMENT(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }

        // check decl or block
        boolean F;
        if(isBlock(TOKENS_LINES.get(KEYS.get(0)))){
            F = BLOCK(TOKENS_LINES);
        }else{
            F = FDECLARATION(TOKENS_LINES);
        }
        
        return F;
    }
    
    private boolean FDECLARATION(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        boolean x = false;
        
        if(isType(TOKENS_LINES.get(KEYS.get(0)))){
            if(! (x = FINIT(TOKENS_LINES))){
                return false;
            }
        }else if(isIdentifier(TOKENS_LINES.get(KEYS.get(0))) && !isReserved(TOKENS_LINES.get(KEYS.get(0)))){
            if(! (x = CALL(TOKENS_LINES))){
                return false;
            }
        }else if("return".equals(TOKENS_LINES.get(KEYS.get(0)))){
            if(! (x = RET(TOKENS_LINES))){
                return false;
            }
        }
        
        return x;
    }
    
    private boolean FINIT(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        // Check Type
        if(isType(TOKENS_LINES.get(KEYS.get(0))) && !TOKENS_LINES.isEmpty()){
            TYPE(TOKENS_LINES);
        }else{
            showErrorMessage("int or float or string or char",TOKENS_LINES);
            return false;
        }
        
        // Check ID
        if(isIdentifier(TOKENS_LINES.get(KEYS.get(0))) && !isReserved(TOKENS_LINES.get(KEYS.get(0))) && !TOKENS_LINES.isEmpty()){
            ID(TOKENS_LINES);
        }else{
            showErrorMessage("Identifier",TOKENS_LINES);
            return false;
        }
        
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        // REST...
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "=", ",", "(" -> {
                return FINIT_TYPE(TOKENS_LINES);
            }
            default -> {
                return false;
            }
        }
    }
    
    private boolean FINIT_TYPE(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "=" -> {
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                if(!EXPRESSION(TOKENS_LINES)){
                    return false;
                }
                
                // semicolon
                if(";".equals(TOKENS_LINES.get(KEYS.get(0)))){
                    TOKENS_LINES.remove(KEYS.get(0));
                    KEYS.remove(0);
                    
                    return true;
                }else{
                    showErrorMessage(";",TOKENS_LINES);
                    return false;
                }
            }
            case "," -> {
                return MORE_INITS(TOKENS_LINES);
            }default -> {
                return false;
            }
        }
    }
    
    private boolean RET(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if("return".equals(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            boolean x = EXPRESSION(TOKENS_LINES);
            if(x){
                if(isSC(TOKENS_LINES.get(KEYS.get(0)))){
                    TOKENS_LINES.remove(KEYS.get(0));
                    KEYS.remove(0);
                    
                    return true;
                }
                showErrorMessage(";",TOKENS_LINES);
                return false;
            }else{
                showErrorMessage("Valid logic or mathematic expression",TOKENS_LINES);
                return false;
            }
        }
        showErrorMessage("return",TOKENS_LINES);
        return false;
    }
    
    private boolean MORE_FSTATEMENTS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            return true;
        }
        
        boolean x,y;
        
        x = FSTATEMENT(TOKENS_LINES);
        if(!x){
            return false;
        }
        y = MORE_FSTATEMENTS(TOKENS_LINES);
        
        return x && y;
    }
    
    private boolean MORE_INITS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isSC(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            return true;
        }
        
        if(",".equals(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            if(TYPE(TOKENS_LINES)){
                if(ID(TOKENS_LINES)){
                    return MORE_INITS(TOKENS_LINES);
                }
                return false;
            }
            return false;
        }
        showErrorMessage(",",TOKENS_LINES);
        return false;
    }
    
    private boolean CALL(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        boolean x,y;
        
        x = ID(TOKENS_LINES);
        if(!x){
            return false;
        }
        y = MORE_CALL(TOKENS_LINES);
        
        return x && y;
    }
    
    private boolean MORE_CALL(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "=" -> {
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                if(!EXPRESSION(TOKENS_LINES)){
                    return false;
                }
                
                // semicolon
                if(";".equals(TOKENS_LINES.get(KEYS.get(0)))){
                    TOKENS_LINES.remove(KEYS.get(0));
                    KEYS.remove(0);
                    
                    return true;
                }else{
                    showErrorMessage(";",TOKENS_LINES);
                    return false;
                }
            }
            case "(" -> {
                
                HashMap<ArrayList<Integer>, String> INIT_PARAMS_HASH = new HashMap<>();
                
                INIT_PARAMS_HASH.put(KEYS.get(0), "(");
                
                TOKENS_LINES.remove(KEYS.get(0));
                
                int openPar = 1;
                int keyIndex = 1;
                while(openPar > 0 && keyIndex < KEYS.size()){
                    if("(".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                        openPar++;
                    }else if(")".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                        openPar--;
                    }
                    
                    INIT_PARAMS_HASH.put(KEYS.get(keyIndex), TOKENS_LINES.get(KEYS.get(keyIndex)));
                    TOKENS_LINES.remove(KEYS.get(keyIndex));
                    keyIndex++;
                }
                
                if(openPar >= 1){
                    showErrorMessage(")",TOKENS_LINES);
                    return false;
                }else{
                    if(CALL_PARAMS(INIT_PARAMS_HASH)){
                        if(isSC(TOKENS_LINES.get(KEYS.get(0)))){
                            TOKENS_LINES.remove(KEYS.get(0));
                            KEYS.remove(0);
                            
                            return true;
                        }
                    }
                    return false;
                }
            }default -> {
                return false;
            }
        }
    }
    
    private boolean CALL_PARAMS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            return true;
        }
        
        TOKENS_LINES.remove(KEYS.get(0));
        KEYS.remove(0);

        int S = TOKENS_LINES.size() - 1;
        TOKENS_LINES.remove(KEYS.get(S));
        KEYS.remove(S);
        
        boolean x,y;
        
        if(isIdentifier(TOKENS_LINES.get(KEYS.get(0)))){
            x = ID(TOKENS_LINES);
        }else{
            x = MN(TOKENS_LINES);
        }
        
        y = MORE_CALLP(TOKENS_LINES);
        
        return x && y;
    }
    
    private boolean MORE_CALLP(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            return true;
        }
        
        boolean x,y;
        
        if(!",".equals(TOKENS_LINES.get(KEYS.get(0)))){
            showErrorMessage(",",TOKENS_LINES);
            return false;
        }
        
        if(isIdentifier(TOKENS_LINES.get(KEYS.get(0)))){
            x = ID(TOKENS_LINES);
        }else{
            x = MN(TOKENS_LINES);
        }
        
        y = MORE_CALLP(TOKENS_LINES);
        
        return x && y;
    }
    
    private boolean BLOCK(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "if" -> {
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                switch(TOKENS_LINES.get(KEYS.get(0))){
                    case "(" -> {
                        HashMap<ArrayList<Integer>, String> INIT_PARAMS_HASH = new HashMap<>();

                        INIT_PARAMS_HASH.put(KEYS.get(0), "(");

                        TOKENS_LINES.remove(KEYS.get(0));

                        int openPar = 1;
                        int keyIndex = 1;
                        while(openPar > 0 && keyIndex < KEYS.size()){
                            if("(".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                                openPar++;
                            }else if(")".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                                openPar--;
                            }

                            INIT_PARAMS_HASH.put(KEYS.get(keyIndex), TOKENS_LINES.get(KEYS.get(keyIndex)));
                            TOKENS_LINES.remove(KEYS.get(keyIndex));
                            keyIndex++;
                        }

                        if(openPar >= 1){
                            showErrorMessage(")",TOKENS_LINES);
                            return false;
                        }else{
                            INIT_PARAMS_HASH.remove(KEYS.get(0));
                            KEYS.remove(0);

                            int S = INIT_PARAMS_HASH.size() - 1;
                            INIT_PARAMS_HASH.remove(KEYS.get(S));
                            KEYS.remove(S);
                            
                            if(LE(INIT_PARAMS_HASH)){
                                if(!("{".equals(TOKENS_LINES.get(KEYS.get(0))))){
                                    showErrorMessage("}",TOKENS_LINES);
                                    return false;
                                }

                                HashMap<ArrayList<Integer>, String> FSTATEMENTS_HASH = new HashMap<>();

                                FSTATEMENTS_HASH.put(KEYS.get(0), "{");

                                TOKENS_LINES.remove(KEYS.get(0));

                                int openKey = 1;
                                int keyIndex2 = 1;
                                while(openKey > 0 && keyIndex2 < KEYS.size()){
                                    if("{".equals(TOKENS_LINES.get(KEYS.get(keyIndex2)))){
                                        openKey++;
                                    }else if("}".equals(TOKENS_LINES.get(KEYS.get(keyIndex2)))){
                                        openKey--;
                                    }

                                    FSTATEMENTS_HASH.put(KEYS.get(keyIndex2), TOKENS_LINES.get(KEYS.get(keyIndex2)));
                                    TOKENS_LINES.remove(KEYS.get(keyIndex2));
                                    keyIndex2++;
                                }

                                if(openKey >= 1){
                                    showErrorMessage("}",TOKENS_LINES);
                                    return false;
                                }else{
                                    if(BSTATEMENTS(FSTATEMENTS_HASH)){
                                        if("else".equals(TOKENS_LINES.get(KEYS.get(0)))){
                                            return ELSE(TOKENS_LINES);
                                        }else{
                                            return true;
                                        }
                                    }
                                }
                            }
                            return false;
                        }
                    }default -> {
                        showErrorMessage("(",TOKENS_LINES);
                        return false;
                    }
                }
            }
            case "while" -> {
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                switch(TOKENS_LINES.get(KEYS.get(0))){
                    case "(" -> {
                        HashMap<ArrayList<Integer>, String> INIT_PARAMS_HASH = new HashMap<>();

                        INIT_PARAMS_HASH.put(KEYS.get(0), "(");

                        TOKENS_LINES.remove(KEYS.get(0));

                        int openPar = 1;
                        int keyIndex = 1;
                        while(openPar > 0 && keyIndex < KEYS.size()){
                            if("(".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                                openPar++;
                            }else if(")".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                                openPar--;
                            }

                            INIT_PARAMS_HASH.put(KEYS.get(keyIndex), TOKENS_LINES.get(KEYS.get(keyIndex)));
                            TOKENS_LINES.remove(KEYS.get(keyIndex));
                            keyIndex++;
                        }

                        if(openPar >= 1){
                            showErrorMessage(")",TOKENS_LINES);
                            return false;
                        }else{
                            INIT_PARAMS_HASH.remove(KEYS.get(0));
                            KEYS.remove(0);

                            int S = INIT_PARAMS_HASH.size() - 1;
                            INIT_PARAMS_HASH.remove(KEYS.get(S));
                            KEYS.remove(S);
                            if(LE(INIT_PARAMS_HASH)){
                                if(!("{".equals(TOKENS_LINES.get(KEYS.get(0))))){
                                    showErrorMessage("{",TOKENS_LINES);
                                    return false;
                                }

                                HashMap<ArrayList<Integer>, String> FSTATEMENTS_HASH = new HashMap<>();

                                FSTATEMENTS_HASH.put(KEYS.get(0), "{");

                                TOKENS_LINES.remove(KEYS.get(0));

                                int openKey = 1;
                                int keyIndex2 = 1;
                                while(openKey > 0 && keyIndex2 < KEYS.size()){
                                    if("{".equals(TOKENS_LINES.get(KEYS.get(keyIndex2)))){
                                        openKey++;
                                    }else if("}".equals(TOKENS_LINES.get(KEYS.get(keyIndex2)))){
                                        openKey--;
                                    }

                                    FSTATEMENTS_HASH.put(KEYS.get(keyIndex2), TOKENS_LINES.get(KEYS.get(keyIndex2)));
                                    TOKENS_LINES.remove(KEYS.get(keyIndex2));
                                    keyIndex2++;
                                }

                                if(openKey >= 1){
                                    showErrorMessage("}",TOKENS_LINES);
                                    return false;
                                }else{
                                    return BSTATEMENTS(FSTATEMENTS_HASH);
                                }
                            }
                            showErrorMessage("Valid logic expression",TOKENS_LINES);
                            return false;
                        }
                    }default -> {
                        showErrorMessage("(",TOKENS_LINES);
                        return false;
                    }
                }
            }
        }
        
        return false;
    }
    
    private boolean ELSE(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "else" -> {
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                switch(TOKENS_LINES.get(KEYS.get(0))){
                    case "{" -> {
                        HashMap<ArrayList<Integer>, String> INIT_PARAMS_HASH = new HashMap<>();

                        INIT_PARAMS_HASH.put(KEYS.get(0), "{");

                        TOKENS_LINES.remove(KEYS.get(0));

                        int openPar = 1;
                        int keyIndex = 1;
                        while(openPar > 0 && keyIndex < KEYS.size()){
                            if("{".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                                openPar++;
                            }else if("}".equals(TOKENS_LINES.get(KEYS.get(keyIndex)))){
                                openPar--;
                            }

                            INIT_PARAMS_HASH.put(KEYS.get(keyIndex), TOKENS_LINES.get(KEYS.get(keyIndex)));
                            TOKENS_LINES.remove(KEYS.get(keyIndex));
                            keyIndex++;
                        }

                        if(openPar >= 1){
                            showErrorMessage(")",TOKENS_LINES);
                            return false;
                        }else{
                            return BSTATEMENTS(INIT_PARAMS_HASH);
                        }
                    }default -> {showErrorMessage("{",TOKENS_LINES);return false;}
                }
            }
        }
        showErrorMessage("possibly else",TOKENS_LINES);
        return false;
    }
    
    private boolean BSTATEMENTS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        TOKENS_LINES.remove(KEYS.get(0));
        KEYS.remove(0);

        int S = TOKENS_LINES.size() - 1;
        TOKENS_LINES.remove(KEYS.get(S));
        KEYS.remove(S);
        
        
        boolean SS = BSTATEMENT(TOKENS_LINES);
        if(!SS){
            return false;
        }
        boolean MS = MORE_BSTATEMENTS(TOKENS_LINES);
        return SS && MS;
    }
    
    private boolean BSTATEMENT(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }

        // check decl or block
        boolean F;
        if(isBlock(TOKENS_LINES.get(KEYS.get(0)))){
            F = BLOCK(TOKENS_LINES);
        }else{
            F = BDECLARATION(TOKENS_LINES);
        }
        
        return F;
    }
    
    private boolean MORE_BSTATEMENTS(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            return true;
        }
        
        boolean S = BSTATEMENT(TOKENS_LINES);
        if(!S){
            return false;
        }
        boolean MS = MORE_BSTATEMENTS(TOKENS_LINES);
        
        return S && MS;
    }
    
    private boolean BDECLARATION(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        boolean x = false;
        
        if(isType(TOKENS_LINES.get(KEYS.get(0)))){
            if(! (x = BINIT(TOKENS_LINES))){
                showErrorMessage("int or float or string or char",TOKENS_LINES);
                return false;
            }
        }else if(isIdentifier(TOKENS_LINES.get(KEYS.get(0))) && !isReserved(TOKENS_LINES.get(KEYS.get(0)))){
            if(! (x = CALL(TOKENS_LINES))){
                showErrorMessage("Identifier",TOKENS_LINES);
                return false;
            }
        }
        
        return x;
    }
    
    private boolean BINIT(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        // Check Type
        if(isType(TOKENS_LINES.get(KEYS.get(0))) && !TOKENS_LINES.isEmpty()){
            TYPE(TOKENS_LINES);
        }else{
            showErrorMessage("int or float or string or char",TOKENS_LINES);
            return false;
        }
        
        // Check ID
        if(isIdentifier(TOKENS_LINES.get(KEYS.get(0))) && !isReserved(TOKENS_LINES.get(KEYS.get(0))) && !TOKENS_LINES.isEmpty()){
            ID(TOKENS_LINES);
        }else{
            showErrorMessage("Identifier",TOKENS_LINES);
            return false;
        }
        
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        // REST...
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "=", ","  -> {
                return BINIT_TYPE(TOKENS_LINES);
            }
            default -> {
                showErrorMessage("= or ,",TOKENS_LINES);
                return false;
            }
        }
    }
    
    private boolean BINIT_TYPE(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(TOKENS_LINES.isEmpty()){
            showErrorMessage("TOKENS",TOKENS_LINES);
            return false;
        }
        
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "=" -> {
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                if(!EXPRESSION(TOKENS_LINES)){
                    showErrorMessage("Valid logic or mathematic expression",TOKENS_LINES);
                    return false;
                }
                
                // semicolon
                if(";".equals(TOKENS_LINES.get(KEYS.get(0)))){
                    TOKENS_LINES.remove(KEYS.get(0));
                    KEYS.remove(0);
                    
                    return true;
                }else{
                    showErrorMessage(";",TOKENS_LINES);
                    return false;
                }
            }
            case "," -> {
                return MORE_INITS(TOKENS_LINES);
            }default -> {
                showErrorMessage(", or =",TOKENS_LINES);
                return false;
            }
        }
    }
    
    private boolean TYPE(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isType(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            return true;
        }
        showErrorMessage("int or float or string or char",TOKENS_LINES);
        return false;
    }
    
    private boolean ID(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isIdentifier(TOKENS_LINES.get(KEYS.get(0))) && !isReserved(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            return true;
        }
        showErrorMessage("Identifier",TOKENS_LINES);
        return false;
    }
    
    private boolean EXPRESSION(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        boolean x = false;
        boolean y = false;
        
        HashMap<ArrayList<Integer>, String> STATE = (HashMap<ArrayList<Integer>, String>) TOKENS_LINES.clone();
        HashMap<ArrayList<Integer>, String> STATE1 = (HashMap<ArrayList<Integer>, String>) STATE.clone();
        HashMap<ArrayList<Integer>, String> STATE2 = (HashMap<ArrayList<Integer>, String>) STATE.clone();
        
        x = ME(STATE1);
        
        if(x){
            TOKENS_LINES.clear();
            
            for(int i = 0 ; i < STATE1.size(); i++){
                TOKENS_LINES.put(KEYS.get(i), STATE1.get(KEYS.get(i)));
            }
            
        }else{
            y = LE(STATE2);
            if(y){
                TOKENS_LINES.clear();
            
                for(int i = 0 ; i < STATE2.size(); i++){
                    TOKENS_LINES.put(KEYS.get(i), STATE2.get(KEYS.get(i)));
                }
            }
        }
        
        return x || y;
    }
    ///////////////////////////////////////////////////////////////////////////
    private boolean LE(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        boolean T = false;
        boolean RE = false;
        
        if(!TOKENS_LINES.isEmpty() && (isNumber(TOKENS_LINES.get(KEYS.get(0))) || isIdentifier(TOKENS_LINES.get(KEYS.get(0))))){
            T = this.LT(TOKENS_LINES);
            RE = this.LRE(TOKENS_LINES);
        }else if(isOpenPar(TOKENS_LINES.get(KEYS.get(0)))){
            HashMap<ArrayList<Integer>, String> termArray = new HashMap<>();
                
            int numPar = 1;
            int keysIndex = 1;
            TOKENS_LINES.remove(KEYS.get(0));
            termArray.put(KEYS.get(0), "(");
            while(numPar > 0 && keysIndex < KEYS.size()){
                if(isOpenPar(TOKENS_LINES.get(KEYS.get(keysIndex)))){
                    numPar++;
                }else if(isClosePar(TOKENS_LINES.get(KEYS.get(keysIndex)))){
                    numPar--;
                }
                
                termArray.put(KEYS.get(keysIndex), TOKENS_LINES.get(KEYS.get(keysIndex)));
                TOKENS_LINES.remove(KEYS.get(keysIndex));
            }

            if(numPar >= 1){
                showErrorMessage(")",TOKENS_LINES);
                return false;
            }else{
                T = this.LT(termArray);
                RE = this.LRE(TOKENS_LINES);
                return T && RE;
            }
        }
        
        return RE && T;
    }
    
    private boolean LRE(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        boolean T = false;
        boolean RE = false;
        
        if(TOKENS_LINES.isEmpty() || isSC(TOKENS_LINES.get(KEYS.get(0)))){
            return true;
        }
        else if(TOKENS_LINES.size() >= 2 && isLOP(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            if(!TOKENS_LINES.isEmpty() && (isNumber(TOKENS_LINES.get(KEYS.get(0))) || isIdentifier(TOKENS_LINES.get(KEYS.get(0)))) || isFloat(TOKENS_LINES.get(KEYS.get(0)))){
                T = this.LT(TOKENS_LINES);
                RE = this.LRE(TOKENS_LINES);
            }else if(isOpenPar(TOKENS_LINES.get(KEYS.get(0)))){
                HashMap<ArrayList<Integer>, String> termArray = new HashMap<>();
                
                int numPar = 1;
                int keysIndex = 1;
                TOKENS_LINES.remove(KEYS.get(0));
                termArray.put(KEYS.get(0), "(");
                while(numPar > 0 && keysIndex < KEYS.size()){
                    if(isOpenPar(TOKENS_LINES.get(KEYS.get(keysIndex)))){
                        numPar++;
                    }else if(isClosePar(TOKENS_LINES.get(KEYS.get(keysIndex)))){
                        numPar--;
                    }

                    termArray.put(KEYS.get(keysIndex), TOKENS_LINES.get(KEYS.get(keysIndex)));
                    TOKENS_LINES.remove(KEYS.get(keysIndex));
                }

                if(numPar >= 1){
                    showErrorMessage(")",TOKENS_LINES);
                    return false;
                }else{
                    T = this.LT(termArray);
                    RE = this.LRE(TOKENS_LINES);
                    return T && RE;
                }
            }
            
            return T && RE;
        }else{
            showErrorMessage("Logical Operator (like && or || or ! or < or >...",TOKENS_LINES);
            return false;
        }
    }
    
    private boolean LT(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isOpenPar(TOKENS_LINES.get(KEYS.get(0)))){
            if(TOKENS_LINES.size() >= 3){
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                int s = TOKENS_LINES.size() - 1;
                TOKENS_LINES.remove(KEYS.get(s));
                KEYS.remove(s);
            
                return this.LE(TOKENS_LINES);
            }
            showErrorMessage("At least one TOKEN between parenthesis",TOKENS_LINES);
            return false;
            
        }else{
            if(isNumber(TOKENS_LINES.get(KEYS.get(0))) || isFloat(TOKENS_LINES.get(KEYS.get(0)))){
                if(this.LN(TOKENS_LINES)){
                    return true;
                }
            }else if(isIdentifier(TOKENS_LINES.get(KEYS.get(0)))){
                if(this.LID(TOKENS_LINES)){
                    return true;
                }
            }
            
            return LCONST(TOKENS_LINES);
        }
    }
    
    private boolean LN(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isNumber(TOKENS_LINES.get(KEYS.get(0))) || isFloat(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            return true;
        }
        showErrorMessage("Number",TOKENS_LINES);
        return false;
    }
    
    private boolean LID(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isIdentifier(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            return true;
        }
        
        showErrorMessage("Identifier",TOKENS_LINES);
        return false;
    }
    
    private boolean LCONST(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        switch(TOKENS_LINES.get(KEYS.get(0))){
            case "true", "false" -> {
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                return true;
            }
        }
        showErrorMessage("Logic constant true or false",TOKENS_LINES);
        return false;
    }
    
    private boolean ME(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        boolean T = false;
        boolean RE = false;
        
        if(!TOKENS_LINES.isEmpty() && (isNumber(TOKENS_LINES.get(KEYS.get(0))) || isIdentifier(TOKENS_LINES.get(KEYS.get(0))))){
            T = this.MT(TOKENS_LINES);
            RE = this.MRE(TOKENS_LINES);
        }else if(isOpenPar(TOKENS_LINES.get(KEYS.get(0)))){
            HashMap<ArrayList<Integer>, String> termArray = new HashMap<>();
                
            int numPar = 1;
            int keysIndex = 1;
            TOKENS_LINES.remove(KEYS.get(0));
            termArray.put(KEYS.get(0), "(");
            while(numPar > 0 && keysIndex < KEYS.size()){
                if(isOpenPar(TOKENS_LINES.get(KEYS.get(keysIndex)))){
                    numPar++;
                }else if(isClosePar(TOKENS_LINES.get(KEYS.get(keysIndex)))){
                    numPar--;
                }
                
                termArray.put(KEYS.get(keysIndex), TOKENS_LINES.get(KEYS.get(keysIndex)));
                TOKENS_LINES.remove(KEYS.get(keysIndex));
            }

            if(numPar >= 1){
                showErrorMessage(")",TOKENS_LINES);
                return false;
            }else{
                T = this.MT(termArray);
                RE = this.MRE(TOKENS_LINES);
                return T && RE;
            }
        }
        
        return RE && T;
    }
    
    private boolean MRE(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        boolean T = false;
        boolean RE = false;
        
        if(TOKENS_LINES.isEmpty() || isSC(TOKENS_LINES.get(KEYS.get(0)))){
            return true;
        }
        else if(TOKENS_LINES.size() >= 2 && isOP(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            if(!TOKENS_LINES.isEmpty() && (isNumber(TOKENS_LINES.get(KEYS.get(0))) || isIdentifier(TOKENS_LINES.get(KEYS.get(0))))){
                T = this.MT(TOKENS_LINES);
                RE = this.MRE(TOKENS_LINES);
            }else if(isOpenPar(TOKENS_LINES.get(KEYS.get(0)))){
                HashMap<ArrayList<Integer>, String> termArray = new HashMap<>();
                
                int numPar = 1;
                int keysIndex = 1;
                TOKENS_LINES.remove(KEYS.get(0));
                termArray.put(KEYS.get(0), "(");
                while(numPar > 0 && keysIndex < KEYS.size()){
                    if(isOpenPar(TOKENS_LINES.get(KEYS.get(keysIndex)))){
                        numPar++;
                    }else if(isClosePar(TOKENS_LINES.get(KEYS.get(keysIndex)))){
                        numPar--;
                    }

                    termArray.put(KEYS.get(keysIndex), TOKENS_LINES.get(KEYS.get(keysIndex)));
                    TOKENS_LINES.remove(KEYS.get(keysIndex));
                }

                if(numPar >= 1){
                    showErrorMessage(")",TOKENS_LINES);
                    return false;
                }else{
                    T = this.MT(termArray);
                    RE = this.MRE(TOKENS_LINES);
                    return T && RE;
                }
            }
            
            return T && RE;
        }else{
             showErrorMessage("Mathematic operator or ;",TOKENS_LINES);
            return false;
        }
    }
    
    private boolean MT(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isOpenPar(TOKENS_LINES.get(KEYS.get(0)))){
            if(TOKENS_LINES.size() >= 3){
                TOKENS_LINES.remove(KEYS.get(0));
                KEYS.remove(0);
                
                int s = TOKENS_LINES.size() - 1;
                TOKENS_LINES.remove(KEYS.get(s));
                KEYS.remove(s);
            
                return this.ME(TOKENS_LINES);
            }
            showErrorMessage("Tokens between parenthesis",TOKENS_LINES);
            return false;
            
        }else{
            if(isNumber(TOKENS_LINES.get(KEYS.get(0)))){
                if(this.MN(TOKENS_LINES)){
                    return true;
                }
            }else if(isIdentifier(TOKENS_LINES.get(KEYS.get(0)))){
                if(this.MID(TOKENS_LINES)){
                    return true;
                }
            }
            showErrorMessage("Number or Identifier",TOKENS_LINES);
            return false;
        }
    }
    
    private boolean MN(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isNumber(TOKENS_LINES.get(KEYS.get(0))) || isFloat(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            return true;
        }
        showErrorMessage("Number",TOKENS_LINES);
        return false;
    }
    
    private boolean MID(HashMap<ArrayList<Integer>, String> TOKENS_LINES){
        if(isIdentifier(TOKENS_LINES.get(KEYS.get(0)))){
            TOKENS_LINES.remove(KEYS.get(0));
            KEYS.remove(0);
            
            return true;
        }
        showErrorMessage("Identifier",TOKENS_LINES);
        return false;
    }
    
    private void addSAMessage(String m){
        this.SAMessages.setText(this.SAMessages.getText().concat(m));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        StatusPanel = new javax.swing.JScrollPane();
        inputCode = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tokensTable = new javax.swing.JTable();
        analizeBTN = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        SAMessages = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador Lexico");
        setMinimumSize(new java.awt.Dimension(1080, 720));

        inputCode.setColumns(20);
        inputCode.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        inputCode.setRows(5);
        StatusPanel.setViewportView(inputCode);

        tokensTable.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tokensTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Lexema", "Token", "ID"
            }
        ));
        tokensTable.setShowGrid(true);
        tokensTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tokensTable);

        analizeBTN.setText("Analizar");
        analizeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analizeBTNActionPerformed(evt);
            }
        });

        jLabel1.setText("Código");

        SAMessages.setColumns(20);
        SAMessages.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        SAMessages.setRows(5);
        jScrollPane3.setViewportView(SAMessages);
        SAMessages.setEditable(false);

        jLabel2.setText("Tabla Análisis Léxico");

        jLabel3.setText("Análisis Sintáctico");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(analizeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StatusPanel)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(analizeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 7, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void analizeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analizeBTNActionPerformed
        // TODO add your handling code here:

        this.input.clear();
        this.SAMessages.setText("");

        String text = this.inputCode.getText();

        for(int i = 0; i < text.length() ; i++){
            this.input.add(text.charAt(i));
        }

        if(isFilled){
            // delete table
            while(this.tableModel.getRowCount() > 0){
                this.tableModel.removeRow(0);
            }
        }

        this.startP();

        this.isFilled = true;
        
        // AS
        boolean SAR = this.startSyntacticAnalysis();
        
        if(SAR){
            addSAMessage("Compilation Complete Without Errors!.");
        }
    }//GEN-LAST:event_analizeBTNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea SAMessages;
    private javax.swing.JScrollPane StatusPanel;
    private javax.swing.JButton analizeBTN;
    private javax.swing.JTextArea inputCode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tokensTable;
    // End of variables declaration//GEN-END:variables
}
