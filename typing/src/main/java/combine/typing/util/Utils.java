package combine.typing.util;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Utils {

    private Utils() {
    }

    public static Image getImage(String name) {
        return Toolkit.getDefaultToolkit().getImage(Utils.class.getResource(name));
    }

    public static class SystemExit extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    public static class ComponentInvisible extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            e.getWindow().setVisible(false);
        }
    }
}
