package combine.typing.util;

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

    /**
     * {@code '} {@code "} not supported.
     *
     * @param c Character
     */
    public static void typeCharacter(char c) {
        switch (c) {
            case 'a', 'A' -> doType(KeyEvent.VK_A, c);
            case 'b', 'B' -> doType(KeyEvent.VK_B, c);
            case 'c', 'C' -> doType(KeyEvent.VK_C, c);
            case 'd', 'D' -> doType(KeyEvent.VK_D, c);
            case 'e', 'E' -> doType(KeyEvent.VK_E, c);
            case 'f', 'F' -> doType(KeyEvent.VK_F, c);
            case 'g', 'G' -> doType(KeyEvent.VK_G, c);
            case 'h', 'H' -> doType(KeyEvent.VK_H, c);
            case 'i', 'I' -> doType(KeyEvent.VK_I, c);
            case 'j', 'J' -> doType(KeyEvent.VK_J, c);
            case 'k', 'K' -> doType(KeyEvent.VK_K, c);
            case 'l', 'L' -> doType(KeyEvent.VK_L, c);
            case 'm', 'M' -> doType(KeyEvent.VK_M, c);
            case 'n', 'N' -> doType(KeyEvent.VK_N, c);
            case 'o', 'O' -> doType(KeyEvent.VK_O, c);
            case 'p', 'P' -> doType(KeyEvent.VK_P, c);
            case 'q', 'Q' -> doType(KeyEvent.VK_Q, c);
            case 'r', 'R' -> doType(KeyEvent.VK_R, c);
            case 's', 'S' -> doType(KeyEvent.VK_S, c);
            case 't', 'T' -> doType(KeyEvent.VK_T, c);
            case 'u', 'U' -> doType(KeyEvent.VK_U, c);
            case 'v', 'V' -> doType(KeyEvent.VK_V, c);
            case 'w', 'W' -> doType(KeyEvent.VK_W, c);
            case 'x', 'X' -> doType(KeyEvent.VK_X, c);
            case 'y', 'Y' -> doType(KeyEvent.VK_Y, c);
            case 'z', 'Z' -> doType(KeyEvent.VK_Z, c);

            case '1' -> doType(KeyEvent.VK_1);
            case '2' -> doType(KeyEvent.VK_2);
            case '3' -> doType(KeyEvent.VK_3);
            case '4' -> doType(KeyEvent.VK_4);
            case '5' -> doType(KeyEvent.VK_5);
            case '6' -> doType(KeyEvent.VK_6);
            case '7' -> doType(KeyEvent.VK_7);
            case '8' -> doType(KeyEvent.VK_8);
            case '9' -> doType(KeyEvent.VK_9);
            case '0' -> doType(KeyEvent.VK_0);

            case '!' -> doShiftType(KeyEvent.VK_1);
            case '@' -> doShiftType(KeyEvent.VK_2);
            case '#' -> doShiftType(KeyEvent.VK_3);
            case '$' -> doShiftType(KeyEvent.VK_4);
            case '%' -> doShiftType(KeyEvent.VK_5);
            case '^' -> doShiftType(KeyEvent.VK_6);
            case '&' -> doShiftType(KeyEvent.VK_7);
            case '*' -> doShiftType(KeyEvent.VK_8);
            case '(' -> doShiftType(KeyEvent.VK_9);
            case ')' -> doShiftType(KeyEvent.VK_0);

            case '-' -> doType(KeyEvent.VK_MINUS);
            case '=' -> doType(KeyEvent.VK_EQUALS);
            case '_' -> doShiftType(KeyEvent.VK_MINUS);
            case '+' -> doShiftType(KeyEvent.VK_EQUALS);

            case '[' -> doType(KeyEvent.VK_OPEN_BRACKET);
            case ']' -> doType(KeyEvent.VK_CLOSE_BRACKET);
            case '{' -> doShiftType(KeyEvent.VK_OPEN_BRACKET);
            case '}' -> doShiftType(KeyEvent.VK_CLOSE_BRACKET);

            case ';' -> doType(KeyEvent.VK_SEMICOLON);
            case ':' -> doShiftType(KeyEvent.VK_SEMICOLON);

            case ',' -> doType(KeyEvent.VK_COMMA);
            case '.' -> doType(KeyEvent.VK_PERIOD);
            case '/' -> doType(KeyEvent.VK_SLASH);
            case '<' -> doShiftType(KeyEvent.VK_COMMA);
            case '>' -> doShiftType(KeyEvent.VK_PERIOD);
            case '?' -> doShiftType(KeyEvent.VK_SLASH);

            case '\n' -> doShiftType(KeyEvent.VK_ENTER);
            case ' ' -> doShiftType(KeyEvent.VK_SPACE);
            case '\t' -> doShiftType(KeyEvent.VK_TAB);

            default -> System.out.println(c + " not supported.");
        }
    }

    static void doShiftType(int keyCode) {
        ROBOT.keyPress(KeyEvent.VK_SHIFT);
        ROBOT.keyPress(keyCode);
        ROBOT.keyRelease(keyCode);
        ROBOT.keyRelease(KeyEvent.VK_SHIFT);
    }

    static void doType(int keyCode) {
        ROBOT.keyPress(keyCode);
        ROBOT.keyRelease(keyCode);
    }

    static void doType(int keyCode, char c) {
        if (Character.isUpperCase(c)) {
            doShiftType(keyCode);
            return;
        }
        ROBOT.keyPress(keyCode);
        ROBOT.keyRelease(keyCode);
    }
}

