package combine.java.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;

public final class RunGroovyWorker extends SwingWorker<String, Void> {

    private final File groovyFile;

    private final JTextPane textPane;

    public RunGroovyWorker(File groovyFile, JTextPane textPane) {
        super();
        this.groovyFile = groovyFile;
        this.textPane = textPane;
    }

    @Override
    protected String doInBackground() {
        var result = "";
        var doc = textPane.getStyledDocument();
        var attributeSet = new SimpleAttributeSet();

        Binding binding = new Binding();
        GroovyShell groovyShell = new GroovyShell(binding);
        try {
            result = String.valueOf(groovyShell.evaluate(groovyFile));
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
        return groovyFile.getName() + " execution end.";
    }
}
