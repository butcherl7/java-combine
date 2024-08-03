package combine.typing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main extends Frame {

    private final TextArea textArea = new TextArea("", 10, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);

    private final Label label = new Label("Delay(ms):");

    private final TextField textField = new TextField("2000", 5);

    private final Button sendButton = new Button("✍");

    private Main() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        Image icon64 = toolkit.getImage(getClass().getResource("/icon64.png"));
        Image icon128 = toolkit.getImage(getClass().getResource("/icon128.png"));
        List<Image> icons = List.of(icon64, icon128);
        setIconImages(icons);

        var footerPanel = new Panel();
        footerPanel.add(label);
        footerPanel.add(textField);
        footerPanel.add(sendButton);

        add(textArea, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.PAGE_END);

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
        var frame = new Main();


        frame.setTitle("Easy Typing");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();
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
