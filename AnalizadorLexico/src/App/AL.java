/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package App;

import java.util.ArrayList;
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputCode = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tokensTable = new javax.swing.JTable();
        analizeBTN = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador Lexico");
        setResizable(false);

        inputCode.setColumns(20);
        inputCode.setRows(5);
        jScrollPane1.setViewportView(inputCode);

        tokensTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Lexema", "Token", "ID"
            }
        ));
        tokensTable.setShowGrid(true);
        jScrollPane2.setViewportView(tokensTable);

        analizeBTN.setText("Analizar");
        analizeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analizeBTNActionPerformed(evt);
            }
        });

        jLabel1.setText("Código");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(analizeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(analizeBTN)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void analizeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analizeBTNActionPerformed
        // TODO add your handling code here:
        
        this.input.clear();
        
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
    private javax.swing.JButton analizeBTN;
    private javax.swing.JTextArea inputCode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tokensTable;
    // End of variables declaration//GEN-END:variables
}
