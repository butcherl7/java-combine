package combine.java.groovy;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.ExecutionException;

@Slf4j
public class RunGroovyUI extends JFrame {

    private final JFileChooser fc = new JFileChooser();
    private final JTextField textField = new JTextField();
    private final JButton button = new JButton("选择 Groovy 文件");

    private final JTextPane textPane = new JTextPane();
    private final JScrollPane textAreaPane = new JScrollPane(textPane);

    public RunGroovyUI() {
        super("Run Groovy Script");
        init();
    }

    private void init() {
        JPanel contentPane = (JPanel) getContentPane();

        File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();

        fc.setAcceptAllFileFilterUsed(false);
        fc.setCurrentDirectory(homeDirectory);
        fc.addChoosableFileFilter(new GroovyFileFilter());

        textField.setToolTipText("请选择文件");
        textField.setColumns(30);
        textField.setEditable(false);

        textPane.setPreferredSize(new Dimension(0, 200));
        textPane.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(textField);
        panel.add(button);

        contentPane.add(panel, BorderLayout.PAGE_START);
        contentPane.add(textAreaPane, BorderLayout.CENTER);

        button.addActionListener(this::handleAction);
    }

    private void handleAction(ActionEvent e) {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            textField.setText(file.getAbsolutePath());
            RunGroovyWorker groovyWorker = new RunGroovyWorker(file, textPane);
            groovyWorker.execute();
            try {
                log.debug(groovyWorker.get());
            } catch (InterruptedException | ExecutionException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    private static void createAndShowGui() {
        var frame = new RunGroovyUI();
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        /*try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ignored) {
        }*/
        SwingUtilities.invokeLater(RunGroovyUI::createAndShowGui);
    }

    static class GroovyFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            return f.getName().endsWith(".groovy");
        }

        @Override
        public String getDescription() {
            return "Groovy Source File (.groovy)";
        }
    }
}
