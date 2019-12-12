package CodigoFuente;

import javax.swing.JOptionPane;

public class Principal {

    public static void main(String[] args) {
        double x = Double.parseDouble(JOptionPane.showInputDialog("Ingrese un número real"));
        JOptionPane.showMessageDialog(null, x);
        String y;
        y = JOptionPane.showInputDialog("Ingrese una cadena");
        JOptionPane.showMessageDialog(null, y);
    }
}
