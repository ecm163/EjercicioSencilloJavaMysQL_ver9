/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import vista.Vista;
import fechaHora.FechaHora;
//import static vista.Vista.radioBotonEdad;
//import static vista.Vista.radioBotonId;
//import static vista.Vista.radioBotonNombre;
//Las siguientes librerías se agregande forma manual. Escribiendolas.
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.javafx.tk.Toolkit;
import static com.sun.javafx.tk.Toolkit.getToolkit;
import controlador.Controlador;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vista.Info;
import vista.MasInfo;

/**
 *
 * @author Emilio
 */
public class Modelo {

    Connection cc;
    Connection cn = Conexion();
    
    public static boolean loginValidado;
    
    Vista view = new Vista();
    public static String atributo = "Id";
    public static String valor = "";
    
    public static boolean bandera;

    public Connection Conexion() {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            cc = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema", "root", "");
            System.out.println("Hecha la conexión con éxito.");

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
        return cc;

    }

    public void mostrartabla(String valor) {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Edad");
        modelo.addColumn("Correo");

        view.tabla_datos.setModel(modelo);

        //String sql = "SELECT * FROM usuario";
        String sql = "";
        if (valor.equals("")) {
            sql = "SELECT * FROM usuario";
        } else {
            sql = "SELECT * FROM usuario WHERE " + atributo + " = '" + valor + "'";
        }

        String datos[] = new String[5];
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {

                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                modelo.addRow(datos);

            }
            view.tabla_datos.setModel(modelo);

            if (modelo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "La búsqueda no ha tenido éxito.");
            }

        } catch (SQLException ex) {
            System.out.println("Se ha producido una excepción en la función mostrartabla()");
        }

    }

    public void limpiar() {
        
        view.txt_nombre.setText("");
        view.txt_apellidos.setText("");
        view.txt_edad.setText("");
        view.txt_correo.setText("");
        view.txt_buscar.setText("");
        
        view.botonModificar.setEnabled(true);
        view.botonActualizar.setEnabled(false);
        
        mostrartabla("");
            
    }

    public void Guardar() {

        try {

            PreparedStatement pps = cn.prepareStatement("INSERT INTO usuario(Nombre, Apellidos, Edad, Correo) VALUES(?, ?, ?, ?)");
            pps.setString(1, view.txt_nombre.getText().trim());
            pps.setString(2, view.txt_apellidos.getText().trim());
            pps.setString(3, view.txt_edad.getText().trim());
            pps.setString(4, view.txt_correo.getText().trim());
            pps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Datos guardados.");
            mostrartabla("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error.");
        }

    }

    public void Modificar() {

        int fila = view.tabla_datos.getSelectedRow();
        if (fila >= 0) {

            view.txt_buscar.setText(view.tabla_datos.getValueAt(fila, 0).toString());
            view.txt_nombre.setText(view.tabla_datos.getValueAt(fila, 1).toString());
            view.txt_apellidos.setText(view.tabla_datos.getValueAt(fila, 2).toString());
            view.txt_edad.setText(view.tabla_datos.getValueAt(fila, 3).toString());
            view.txt_correo.setText(view.tabla_datos.getValueAt(fila, 4).toString());
            
            view.botonModificar.setEnabled(false);
            view.botonActualizar.setEnabled(true);
            
        } else {
            JOptionPane.showMessageDialog(null, "Fila no seleccionada.");
        }

    }

    public void Atualizar() {

        try {
            PreparedStatement pps = cn.prepareStatement("UPDATE usuario SET Nombre = '" + view.txt_nombre.getText()
                    + "' ,Apellidos = '" + view.txt_apellidos.getText() + "' ,Edad = '" + view.txt_edad.getText()
                    + "' ,Correo ='" + view.txt_correo.getText() + "' WHERE Id = " + view.txt_buscar.getText() + "");
            pps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Datos Actualizados.");
            limpiar();
            //mostrartabla("");

        } catch (SQLException ex) {
            System.out.println("Error en Actualizar()");
        }

    }

    public void Eliminar() {

        int fila = view.tabla_datos.getSelectedRow();
        String valor = view.tabla_datos.getValueAt(fila, 0).toString();
        if (fila >= 0) {

            try {
                PreparedStatement pps = cn.prepareStatement("DELETE FROM usuario WHERE Id = '" + valor + "'");
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Dato eliminado.");
                mostrartabla("");
            } catch (SQLException ex) {
                System.out.println("Error en Eliminar()");
            }

        } else {

            JOptionPane.showMessageDialog(null, "La tabla está vacía.");

        }

    }

    //El método Buscar será diferente a la versión anterior hecha con radio-buttons.
    public void Buscar() {

        atributo = view.combo.getSelectedItem().toString();
        mostrartabla(view.txt_buscar.getText());

    }

    public void agregarItem() {

        view.combo.addItem("Id");
        view.combo.addItem("Nombre");
        view.combo.addItem("Apellidos");
        view.combo.addItem("Edad");
        view.combo.addItem("Correo");

    }

    public void GenerarDocumentoPDF() {

        Document documento = new Document();
        FechaHora fechaYhora = new FechaHora();

        try {

            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Informe_Tabla_Usuarios.pdf"));
            //Introducimos la imagen:
            //Cuando trabajamos con NetBeans los paquetes se guardan siempre en un directorio llamado src.
            Image header = Image.getInstance("src/img/Cabecera.png");
            //El largo de la imagen de 650 px y la escala de visualización máxima 1000.
            header.scaleToFit(650, 1000);
            //Alineamos la imagen y mediante el método Chunk alineamos al centro.
            header.setAlignment(Chunk.ALIGN_CENTER);
            //Vamos a comenzar a darle formato a nuestro texto:
            //Alineamos:
            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            //Añadimos texto (parrafo).
            parrafo.add("\n\nDocumento creado a las " + fechaYhora.Hora() + " del "
                    + fechaYhora.Fecha() + ".");
            parrafo.add("\nFormato creado por Emilio®. \n\n");
            //Damos formato.
            parrafo.setFont(FontFactory.getFont("Tahoma", 18, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY));
            parrafo.add("Alumnos registrados.\n\n");
            documento.open();
            //Para agregar la imagen y el parrafo creado.
            documento.add(header);
            documento.add(parrafo);

            PdfPTable tabla_pdf = new PdfPTable(5);

            //A continuación le damos un titulo a cada columna.
            tabla_pdf.setHeaderRows(1);
            tabla_pdf.addCell("\n Id\n ");
            tabla_pdf.addCell("\n  Nombre\n ");
            tabla_pdf.addCell("\n    Apellidos\n ");
            tabla_pdf.addCell("\nEdad\n ");
            tabla_pdf.addCell("\n           Correo\n ");

            //Para establecer el ancho de las columnas. Las medidas se pueden sacar por tanteo.
            float[] medidaCeldas = {1.5f, 3f, 4.5f, 1.75f, 9.5f};
            tabla_pdf.setWidths(medidaCeldas);

            //A continuación conectamos con la base de datos y damos instrucciones a nuestra base de datos:
            try {

                PreparedStatement ppsPDF = cn.prepareStatement("SELECT * FROM usuario");
                ResultSet rsPDF = ppsPDF.executeQuery();

                if (rsPDF.next()) { //Si la tabla no está vacía.

                    do {

                        //rsPDF.getString(x)Recupera el valor de la columna x en la fila actual.
                        tabla_pdf.addCell(rsPDF.getString(1));
                        tabla_pdf.addCell(rsPDF.getString(2));
                        tabla_pdf.addCell(rsPDF.getString(3));
                        tabla_pdf.addCell(rsPDF.getString(4));
                        tabla_pdf.addCell(rsPDF.getString(5));

                    } while (rsPDF.next()); //Se ejecutará siempre que haya información en la BBDD.

                    //Se añade al documento PDF la tabla.
                    documento.add(tabla_pdf);

                }

            } catch (Exception e) {

                System.out.println("No se pudo dar la instrucción a la base de datos correctamente.");
                JOptionPane.showMessageDialog(null, "No se pudo dar la instrucción a la base de datos correctamente.");

            }

            documento.close();//Abrimos previamente el documento con documento.open() y ahora hay que cerrarlo.
            JOptionPane.showMessageDialog(null, "Informe PDF creado.");

        } catch (Exception e) {

            System.out.println("No se pudo generar el documento PDF.\nSi tiene un lector de PDF abierto cierrelo,\n"
                    + "quizás ese sea el problema.");
            JOptionPane.showMessageDialog(null, "\n   No se pudo generar el documento PDF.    \n\n"
                    + "------------------------------------------\n"
                    + "Si tiene un lector de PDF abierto cierrelo,    \n"
                    + "quizás ese sea el problema.    \n"
                    + "------------------------------------------\n\n");

        }

    }

    public void GenerarInforme_Jasper() {

        try {

//            Conexion con = new Conexion();
//            Connection conn = con.getConexion();
//            Connection cc;
//            Connection cn = Conexion();
            JasperReport informe = null;
            String path = "src\\informes\\InformeUsuarios.jasper";

            //Hacemos un mapeo para que se pueda filtrar la información a mostrar.
//            Map parametro = new HashMap();
//            parametro.put("Id", 3); //Búsca la fila con Id = 3.
            informe = (JasperReport) JRLoader.loadObjectFromFile(path);
            //Rellenar el informe (o reporte).
            JasperPrint jprint = JasperFillManager.fillReport(informe, null, cn);
            //Si optamos por la opción de filtrar con un parámetro:
//            JasperPrint jprint = JasperFillManager.fillReport(informe, parametro, conn);
            //Activar la vista del informe.
            JasperViewer vistaInforme = new JasperViewer(jprint, false);
            //Para que se cierre al hacer clic en la x de cerrar.
            vistaInforme.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            //Ahora instrucción para mostrar como visible este reporte.
            vistaInforme.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void ActivarDesactivar() {

        if (bandera == false) {

            view.botonGuardar.setEnabled(false);
            view.botonLimpiar.setEnabled(false);
            view.botonModificar.setEnabled(false);

            view.botonActualizar.setEnabled(false);
            view.botonEliminar.setEnabled(false);

            view.botonBuscar.setEnabled(false);
            view.botonMostrarTabla.setEnabled(false);
            view.botonGenerarInforme.setEnabled(false);

            view.txt_nombre.setEnabled(false);
            view.txt_apellidos.setEnabled(false);
            view.txt_edad.setEnabled(false);
            view.txt_correo.setEnabled(false);
            view.txt_buscar.setEnabled(false);
            
            view.combo.setEnabled(false);
            
            view.jScrollPane3.setVisible(false);
            view.jScrollPane3.setEnabled(false);
            view.tabla_datos.setVisible(false);
            view.tabla_datos.setEnabled(false);                         
           
        }
        
        if (bandera == true) {

            view.botonGuardar.setEnabled(true);
            view.botonLimpiar.setEnabled(true);
            view.botonModificar.setEnabled(true);

            view.botonActualizar.setEnabled(false);
            view.botonEliminar.setEnabled(true);

            view.botonBuscar.setEnabled(true);
            view.botonMostrarTabla.setEnabled(true);
            view.botonGenerarInforme.setEnabled(true);

            view.txt_nombre.setEnabled(true);
            view.txt_apellidos.setEnabled(true);
            view.txt_edad.setEnabled(true);
            view.txt_correo.setEnabled(true);
            view.txt_buscar.setEnabled(true);
            
            view.combo.setEnabled(true);

            view.jScrollPane3.setVisible(true);
            view.jScrollPane3.setEnabled(true);
            view.tabla_datos.setVisible(true);
            view.tabla_datos.setEnabled(true); 
            
            mostrartabla("");
                       

        }
                
        
        bandera = !bandera;

    }
    
    public boolean Login(){
        
        String usuario = vista.Login.txt_usuario.getText();
        String password = vista.Login.txt_password.getText();
        
        if(usuario.equals("usuario") && password.equals("123")){
            
            //JOptionPane.showMessageDialog(null, "Nombre de usuario o contraseña correctos.");
            loginValidado = true;
            
        }
        
        else{
            
            //JOptionPane.showMessageDialog(null, "Nombre de usuario o contraseña no correctos."); 
            Controlador.flagLogin = 1;
            loginValidado = false;
            
        }
        
        return loginValidado; 
        
    }
    
    public void CambiarImg(){
        
        //El código está en el Frame Form dentro de la ActionPerformed del botón Guardar
        //llamada botonCambiarImgActionPerformed().
        
    }
    
    public void VerInfo(){
        
        Info inst = new Info();
        view.Escritorio.add(inst);
        inst.show();
        
    }
    
    public void VerMasInfo(){
        
        //El código está en el JInternal Form 'Info'      
        
    }
    
}
