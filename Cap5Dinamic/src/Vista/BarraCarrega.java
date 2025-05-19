package Vista;

import Model.Dades;
import controlador.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class BarraCarrega extends JPanel {
    private String titol;
    private JProgressBar progressBar;
    private JButton cancel;
    private int id;

    private boolean hasEnd = false;
    private boolean actiu = false;

    private Dades dades;

    static{
        UIManager.put("ProgressBar.repaintInterval", 20);
        UIManager.put("ProgressBar.cycleTime", 1000);
    }
    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public BarraCarrega(String titol, int id) {
        super();
        this.dades = Main.getInstance().getDades();
        this.id = id;
        this.titol = titol;

        init();
    }

    private void init() {

        this.setLayout(new BorderLayout(10, 15));
        JLabel label = new JLabel(titol);
        this.add(label, BorderLayout.WEST);


        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setForeground(Color.GREEN);
        progressBar.setMaximumSize(new Dimension(progressBar.getPreferredSize().width, progressBar.getPreferredSize().height));
        this.add(progressBar, BorderLayout.CENTER);


        cancel = new JButton("Cancelar");
        cancel.addActionListener(e -> {
            System.err.println("Cancel" + id);
            Main.getInstance().aturar(id);
        });
        this.add(cancel, BorderLayout.EAST);
        this.setVisible(true);
    }

    public void iniciar(){
        actiu = true;
    }

    public void tick(){
        if(!actiu)return;
        if(hasEnd){
            progressBar.setValue(progressBar.getMaximum());
            return;
        }
        int n = progressBar.getValue() + 3;
        if(n>progressBar.getMaximum()){
            n = progressBar.getMinimum();
        }
        progressBar.setValue(n);
    }

    public void end(){
        hasEnd = true;
        progressBar.setValue(progressBar.getMaximum());
        cancel.setEnabled(false);
        Thread.startVirtualThread(this::esperarEliminar);
    }

    private void esperarEliminar(){
        try {
            Thread.sleep(3000);
            Main.getInstance().getFinestra().aturar(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
