package principal;

import modelo.Modelo;
import vista.Vista;
import vista.Login;
import static modelo.Modelo.loginValidado;
import fechaHora.FechaHora;

import controlador.Controlador;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import vista.Info;


//Las siguientes librerías se agregande forma manual. Escribiendolas.

public class Principal {

    //public static String valor_inicio = "";

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Error: "+e);
        }
        
        //boolean loginValido;
        
        Login login = new Login();
        Modelo modelo = new Modelo(/*valor_inicio*/);
        Vista vista = new Vista();
        //Info info = new Info();
        Controlador control = new Controlador(modelo, vista, login);
               
        control.iniciar();
            

    }

}
