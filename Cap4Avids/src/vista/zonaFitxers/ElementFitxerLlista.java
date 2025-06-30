package vista.zonaFitxers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.nio.file.Path;

public class ElementFitxerLlista extends JPanel {
    private final File file;
    private Path otherPath;
    private final int id;
    private final JLabel filePathLabel;
    private final JLabel otherPathLabel;
    private final JProgressBar progressBar;
    private final JButton removeButton;
    private final JButton showTreeButton;

    public ElementFitxerLlista(File file, Path otherPath, int id) {
        this.file = file;
        this.otherPath = otherPath;
        this.id = id;

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        // Panel de rutas
        JPanel pathsPanel = new JPanel(new GridLayout(2, 1));
        filePathLabel = new JLabel(file.getAbsolutePath());
        otherPathLabel = new JLabel(otherPath.toString());
        pathsPanel.add(filePathLabel);
        pathsPanel.add(otherPathLabel);
        add(pathsPanel, BorderLayout.CENTER);

        // Barra de progreso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);

        // Botones
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        removeButton = new JButton("Cancelar");
        showTreeButton = new JButton("Mostrar arbre");
        buttonsPanel.add(removeButton);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(showTreeButton);
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

        // Si cabe entero
        if (fm.stringWidth(fullPath) <= availWidth) {
            label.setText(fullPath);
            return;
        }

        // Búsqueda binaria para el prefijo máximo
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

    public int getProgress() {
        return progressBar.getValue();
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
        if (value >= progressBar.getMaximum()) {
            removeButton.setEnabled(false);
        }
    }

    public void addRemoveListener(ActionListener listener) {
        removeButton.addActionListener(listener);
    }

    public void addShowTreeListener(ActionListener listener) {
        showTreeButton.addActionListener(listener);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            Container parent = getParent();
            if (parent != null) {
                setPreferredSize(new Dimension(parent.getWidth(), getPreferredSize().height));
                revalidate();
                repaint();
            }
        });
    }
}
