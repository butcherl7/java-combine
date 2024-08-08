package combine.typing.util;

import java.awt.*;

public class Utils {

    private Utils() {
    }

    public static Image getImage(String name) {
        return Toolkit.getDefaultToolkit().getImage(Utils.class.getResource(name));
    }
}
