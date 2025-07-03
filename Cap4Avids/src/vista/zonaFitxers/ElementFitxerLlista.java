package vista.zonaFitxers;

import control.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.nio.file.Path;

public class ElementFitxerLlista extends JPanel {
    private final File file;
    private final int id;
    private final JLabel filePathLabel;
    private final JLabel otherPathLabel;
    private final JProgressBar progressBar;
    private Path otherPath;

    private boolean actiu = false;
    private boolean haFinalitzat = false;

    static {
        UIManager.put("ProgressBar.repaintInterval", 20);
        UIManager.put("ProgressBar.cycleTime", 1000);
    }

    public ElementFitxerLlista(File file, Path otherPath, int id) {
        this.file = file;
        this.otherPath = otherPath;
        this.id = id;

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel de rutas
        JPanel pathsPanel = new JPanel(new GridLayout(2, 1));
        filePathLabel = new JLabel(file.getAbsolutePath());
        otherPathLabel = new JLabel(otherPath.toString());
        pathsPanel.add(filePathLabel);
        pathsPanel.add(otherPathLabel);
        add(pathsPanel, BorderLayout.CENTER);

        // Barra de progreso
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setForeground(Color.GREEN);
        add(progressBar, BorderLayout.SOUTH);

        // Botones
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(Box.createVerticalStrut(5));
        add(buttonsPanel, BorderLayout.EAST);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                truncateLabel(filePathLabel, file.getAbsolutePath());
                truncateLabel(otherPathLabel, otherPath.toString());
            }
        });
    }

    private void truncateLabel(JLabel label, String fullPath) {
        String fileName = new File(fullPath).getName();
        FontMetrics fm = label.getFontMetrics(label.getFont());
        int availWidth = label.getWidth();
        String ell = "...";

        if (fm.stringWidth(fullPath) <= availWidth) {
            label.setText(fullPath);
            return;
        }

        int low = 0, high = fullPath.length(), mid;
        String best = ell + fileName;
        while (low <= high) {
            mid = (low + high) / 2;
            String prefix = fullPath.substring(0, mid);
            String text = prefix + ell + fileName;
            if (fm.stringWidth(text) <= availWidth) {
                best = text;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        label.setText(best);
    }

    public File getFile() {
        return file;
    }

    public Path getOtherPath() {
        return otherPath;
    }

    public void setOtherPath(Path otherPath) {
        this.otherPath = otherPath;
        otherPathLabel.setText(otherPath.toString());
    }

    public int getId() {
        return id;
    }

    public void iniciarAnimacio() {
        actiu = true;
        haFinalitzat = false;
    }

    public void finalizar() {
        actiu = false;
        haFinalitzat = true;
        progressBar.setValue(progressBar.getMaximum());
    }

    public void tick() {
        if (!actiu || haFinalitzat) return;

        int n = progressBar.getValue() + 4;
        if (n > progressBar.getMaximum()) n = progressBar.getMinimum();
        progressBar.setValue(n);
        Main.instance.getFinestra().actualitzar();
    }
}
