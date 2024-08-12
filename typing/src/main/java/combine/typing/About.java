package combine.typing;

import combine.typing.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class About extends Dialog {

    public About(Frame owner) {
        super(owner, "About", true);
        init();
    }

    private void init() {
        Panel panel = new Panel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(new Label("Runtime version: " + System.getProperty("java.vendor") + " " + System.getProperty("java.runtime.version")));
        panel.add(new Label("PID: " + ProcessHandle.current().pid()));
        add(panel);
        setIconImage(Utils.getImage("/info16.png"));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
    }
}
