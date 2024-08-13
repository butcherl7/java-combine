package combine.java.sc;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class RunGroovyUI extends JFrame {

    private final JFileChooser fc = new JFileChooser();
    private final JTextField textField = new JTextField();
    private final JButton button = new JButton("选择 Groovy 文件");

    public RunGroovyUI() {
        super("Run Groovy Script");
        init();
    }

    private void init() {
        setSize(500, 300);
        setLayout(new FlowLayout());

        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new GroovyFileFilter());

        textField.setToolTipText("请选择文件");
        textField.setColumns(50);
        // textField.setEditable(false);

        button.addActionListener(this::handleAction);

        add(textField);
        add(button);
    }

    private void handleAction(ActionEvent e) {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            textField.setText(file.getAbsolutePath());

            Binding binding = new Binding();
            GroovyShell groovyShell = new GroovyShell(binding);
            try {
                Object result = groovyShell.evaluate(file);
                System.out.println(result);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void createAndShowGui() {
        var frame = new RunGroovyUI();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
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
            return "Groovy 脚本文件";
        }
    }
}
