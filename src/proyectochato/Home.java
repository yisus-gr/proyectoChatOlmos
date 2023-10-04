/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyectochato;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author gueva
 */
public class Home extends javax.swing.JFrame {
    private String alias;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 2099;
   
    private Socket socket;
    private DataInputStream entradaServidor;
    private DataOutputStream salidaCliente;
    private String[] listaUsuarios;
    
    /**
     * Creates new form Home
     */
    
    public void setAlias(String name){
        this.alias = name;
        jLabel1.setText(name);
        setTitle(name);
        enviarAliasAlServidor(alias);
    }
    public String getAlias(){
        return this.alias;
    }
    
    
    
    public Home() {
        try {
            UIManager.setLookAndFeel(getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cerrarSocket();
            }
        });
        
        
        
        try{
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            entradaServidor = new DataInputStream(socket.getInputStream());
            salidaCliente = new DataOutputStream(socket.getOutputStream());
                       
            Thread recibirMensajes = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            String mensaje = entradaServidor.readUTF();
                            if (mensaje.charAt(0) == 'm' || mensaje.charAt(0) == 'p'){
                                mostrarMensaje(mensaje);
                            } else { // si el mensaje no empieza con m, entonces es una lista de usuarios
                                listaUsuarios = mensaje.split(",");
                                jComboBox1.removeAllItems();
                                jComboBox1.addItem("Chat Principal");
                                for (String usuario : listaUsuarios) {
                                    if(!usuario.equals(alias)){
                                       jComboBox1.addItem(usuario); 
                                    }
                                }
                                mostrarMensaje(mensaje);
                            }
                            
                        }
                    } catch (IOException e) {
                        mostrarMensaje("Error al recibir mensajes del servidor: " + e.getMessage());
                    }
                }
            });
            recibirMensajes.start();
        } catch(Exception e){

        } 
        initComponents();
    }
    
    private void enviarAliasAlServidor(String alias) {
        try {
            salidaCliente.writeUTF(alias);
        } catch (IOException e) {
            mostrarMensaje("Error al enviar el alias al servidor: " + e.getMessage());
        }
    }

    
    
    private void cerrarSocket() {
        try {
            if (socket != null && !socket.isClosed()) {     
            salidaCliente.writeUTF("DESCONECTAR");
                socket.close();
            }
        } catch (IOException e) {
            mostrarMensaje("Error al cerrar el socket: " + e.getMessage());
            }
    }
    
    
    
    private void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                txtaConversation.append(mensaje + "\n");
            }
        });
    } 
    
    private void enviarMensaje() {
        String mensajeUsuario = txtfMessage.getText();
        if (!mensajeUsuario.isEmpty()) {
            if(jComboBox1.getSelectedIndex()==0){
                try {
                    String mensaje = "m^" + alias + "@" + socket.getLocalAddress().getHostAddress() + "^-^" + mensajeUsuario + "^";
                    //String mensaje = "m^" + alias + "^-^" + mensajeUsuario + "^";
                    salidaCliente.writeUTF(mensaje);
                    txtfMessage.setText("");
                    } catch (IOException e) {
                        mostrarMensaje("Error al enviar mensaje al servidor: " + e.getMessage());
                    }
            } else{
                try{
                    String mensaje = "p^" + alias + "^" + jComboBox1.getSelectedItem().toString() + "^" + mensajeUsuario;
                    salidaCliente.writeUTF(mensaje);
                    txtfMessage.setText("");
                    mostrarMensaje(mensaje);
                } catch(IOException e){
                    mostrarMensaje("Error al enviar mensaje al servidor: " + e.getMessage());
                }
                
                
            }
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtfMessage = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtaConversation = new javax.swing.JTextArea();
        btnEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(this.getName());

        txtaConversation.setEditable(false);
        txtaConversation.setColumns(1);
        txtaConversation.setRows(5);
        txtaConversation.setAutoscrolls(false);
        jScrollPane1.setViewportView(txtaConversation);

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jLabel1.setText("o envia un archivo");

        jButton1.setText("Elegir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("jLabel2");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chat Principal" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtfMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addGap(23, 23, 23)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEnviar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1)
                    .addComponent(jLabel2))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        
        enviarMensaje();
        
        
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser jFileChooser = new JFileChooser();
        String ruta = "";
        
        int respuesta = jFileChooser.showOpenDialog(this);
        
        if(respuesta == JFileChooser.APPROVE_OPTION){
            ruta = jFileChooser.getSelectedFile().getPath();
        }
        jLabel2.setText(ruta);
            
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        txtaConversation.setText("");
    }//GEN-LAST:event_jComboBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);              
                
            }
        });
        
        
       
        
    }
    
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtaConversation;
    private javax.swing.JTextField txtfMessage;
    // End of variables declaration//GEN-END:variables
}
