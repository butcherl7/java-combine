package combine.typing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main extends JFrame {

    private final JTextArea textArea = new JTextArea(10, 50);

    private final JScrollPane areaPane = new JScrollPane(textArea);

    private final JPanel footerPanel = new JPanel();

    private final JLabel label = new JLabel("Delay(ms): ");

    private final JTextField textField = new JTextField(5);

    private final JButton sendButton = new JButton("type");

    private Main() {
        init();
    }

    private void init() {
        textArea.setLineWrap(true);

        textField.setText("2000");
        footerPanel.add(label);
        footerPanel.add(textField);
        footerPanel.add(sendButton);

        JPanel panel = (JPanel) getContentPane();
        panel.add(areaPane, BorderLayout.CENTER);
        panel.add(footerPanel, BorderLayout.PAGE_END);

        sendButton.addActionListener(this::onSend);
    }

    private void onSend(ActionEvent e) {
        String text = textArea.getText();
        if (text == null || text.isBlank()) {
            return;
        }

        Integer delay = runCatch(() -> Integer.parseInt(textField.getText()));

        if (delay != null && delay > 0) {
            sleep(delay);
        }
        TypeUtils.typeString(text);
    }

    private static void createAndShowGUI() {
        /*try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ignored) {
        }*/
        JFrame frame = new Main();
        frame.setTitle("Easy Typing");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(360, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static <T> T runCatch(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return null;
        }
    }

    static void sleep(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}
