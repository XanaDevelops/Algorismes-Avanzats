package vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorMatriu extends JDialog implements ActionListener {

    public EditorMatriu(JFrame parent) {
        super(parent, "Editar Matriu", ModalityType.APPLICATION_MODAL);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setSize(700, 500);
        this.setAlwaysOnTop(true);
        JLabel lblMatriu = new JLabel("Matriu");
        this.add(lblMatriu);
        this.setVisible(true);
    }










    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
