package combine.typing;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TypeUtils {

    private static final Robot ROBOT;

    static {
        try {
            ROBOT = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static void typeString(String text) {
        for (char c : text.toCharArray()) {
            typeCharacter(c);
        }
    }

    public static void typeCharacter(char c) {
        switch (c) {
            case 'a', 'A' -> doType(KeyEvent.VK_A, c);
            case 'b', 'B' -> doType(KeyEvent.VK_B, c);
            case 'c', 'C' -> doType(KeyEvent.VK_C, c);
        }
    }

    public static void doType(Robot robot, int... keyCodes) {
        for (int keyCode : keyCodes) {
            robot.keyPress(keyCode);
        }
        for (int keyCode : keyCodes) {
            robot.keyRelease(keyCode);
        }
    }

    public static void doType(int keyCode, char c) {
        boolean isUpper = Character.isUpperCase(c);
        if (isUpper) ROBOT.keyPress(KeyEvent.VK_SHIFT);
        ROBOT.keyPress(keyCode);
        ROBOT.keyRelease(keyCode);
        if (isUpper) ROBOT.keyRelease(KeyEvent.VK_SHIFT);
    }
}

