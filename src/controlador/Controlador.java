package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import modelo.Modelo;
import vista.Vista;
import vista.Login;
import vista.Info;
import vista.MasInfo;
import static vista.Vista.grupoRadioBotones;

//import static vista.Vista.radioBotonEdad;
//import static vista.Vista.radioBotonId;
//import static vista.Vista.radioBotonNombre;
//Las siguientes librerías se agregande forma manual. Escribiendolas.
/**
 *
 * @author Emilio
 */
public class Controlador implements ActionListener {

    private Modelo modelo;
    private Vista vista;
    private Login login;    
    private Info info;
        
    public static int flagLogin = 0;

    public Controlador(Modelo modelo, Vista vista, Login login) {

        this.modelo = modelo;
        this.vista = vista;
        this.login = login;

        this.vista.botonGuardar.addActionListener(this);
        this.vista.botonLimpiar.addActionListener(this);
        this.vista.botonModificar.addActionListener(this);

        this.vista.botonActualizar.addActionListener(this);
        this.vista.botonEliminar.addActionListener(this);

        this.vista.botonBuscar.addActionListener(this);
        this.vista.botonMostrarTabla.addActionListener(this);
        this.vista.botonGenerarInforme.addActionListener(this);

        this.vista.botonActivar.addActionListener(this);
        this.login.botonLogin.addActionListener(this);
        
        this.vista.botonCambiarImg.addActionListener(this);
        
        this.vista.botonVerInfo.addActionListener(this);
        
        
    }

    public void iniciar() {

        JOptionPane.showMessageDialog(null, "El nombre de usuario y contraseñas son:\n\n"
                + "USUARIO:             usuario\n" 
                + "CONTRASEÑA:    123\n\n"
                + "Esto es sólo un ejercicio práctico, por eso se da el usuario y contraseña.\n\n");
        
        do {

            login.setVisible(true);

            if (flagLogin == 1) {
                JOptionPane.showMessageDialog(login, "Nombre de usuario o contraseña incorrectos.");
                flagLogin = 0;
            }

        } while (!modelo.loginValidado);

        login.txt_usuario.setText("");
        login.txt_usuario.setText("");
        login.setVisible(false);

        vista.setTitle("Sistema con MVC.");
        vista.pack();
        vista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        modelo.agregarItem();
        modelo.mostrartabla("");
        modelo.bandera = false;
        modelo.ActivarDesactivar();
        //m.MostrarUsuarios();
        //m.BloquearUsuario();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (vista.botonGuardar == e.getSource()) {

            try {

                modelo.Guardar();
                modelo.mostrartabla("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido guardar los datos.");

            }

        }

        if (vista.botonLimpiar == e.getSource()) {

            try {

                modelo.limpiar();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido limpiar los campos.");

            }

        }

        if (vista.botonModificar == e.getSource()) {

            try {

                modelo.Modificar();
                modelo.mostrartabla("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido actualizar los datos.");

            }

        }

        if (vista.botonActualizar == e.getSource()) {

            try {

                modelo.Atualizar();
                modelo.mostrartabla("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido limpiar los campos.");

            }

        }

        if (vista.botonEliminar == e.getSource()) {

            try {

                modelo.Eliminar();
                modelo.mostrartabla("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido eliminar los datos.");

            }

        }

        if (vista.botonBuscar == e.getSource()) {

            try {

                modelo.Buscar();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido eliminar los datos.");

            }

        }

        if (vista.botonMostrarTabla == e.getSource()) {

            try {

                modelo.mostrartabla("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido eliminar los datos.");

            }

        }

        if (vista.botonGenerarInforme == e.getSource()) {

            try {

                modelo.GenerarInforme_Jasper();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido generar el documento PDF.");

            }

        }

        if (vista.botonActivar == e.getSource()) {

            try {

                modelo.ActivarDesactivar();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido generar el documento PDF.");

            }

        }

        if (login.botonLogin == e.getSource()) {

            try {

                modelo.Login();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido hacer el login.");

            }

        }
        
        if (vista.botonVerInfo == e.getSource()) {

            try {

                modelo.VerInfo();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha podido hacer el login.");

            }

        }
        
        

    }

}
