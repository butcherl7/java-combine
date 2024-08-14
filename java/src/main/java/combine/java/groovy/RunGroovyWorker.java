package combine.java.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;

@Slf4j
public final class RunGroovyWorker extends SwingWorker<Void, Void> {

    private final File groovyFile;

    private final JTextPane textPane;

    public RunGroovyWorker(File groovyFile, JTextPane textPane) {
        super();
        this.groovyFile = groovyFile;
        this.textPane = textPane;
    }

    @Override
    protected Void doInBackground() {
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
            } catch (BadLocationException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        log.debug("Execution '{}' end.", groovyFile.getName());
        return null;
    }
}
