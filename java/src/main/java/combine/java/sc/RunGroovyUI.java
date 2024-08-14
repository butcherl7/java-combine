package combine.java.sc;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

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

            var result = "";
            var doc = textPane.getStyledDocument();
            var attributeSet = new SimpleAttributeSet();

            Binding binding = new Binding();
            GroovyShell groovyShell = new GroovyShell(binding);
            try {
                result = String.valueOf(groovyShell.evaluate(file));
                StyleConstants.setForeground(attributeSet, Color.BLUE);
            } catch (Exception ex) {
                result = ex.getMessage();
                StyleConstants.setForeground(attributeSet, Color.RED);
            } finally {
                try {
                    doc.insertString(doc.getLength(), result, attributeSet);
                } catch (BadLocationException ignored) {
                }
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
