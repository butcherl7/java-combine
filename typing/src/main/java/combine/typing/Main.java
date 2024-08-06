package combine.typing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main extends Frame {

    private static final TextArea textArea = new TextArea("", 10, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);

    private static final TextField textField = new TextField("2000", 5);

    private static final Label label = new Label("Delay(ms):");

    private static final Button button = new Button("✍");

    private static final Checkbox checkbox = new Checkbox("Always On Top", true);

    private Main() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setIcon();

        var footerPanel = new Panel();
        footerPanel.add(checkbox);
        footerPanel.add(label);
        footerPanel.add(textField);
        footerPanel.add(button);

        add(textArea, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.PAGE_END);

        addListener();
    }

    private void setIcon() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image icon64 = toolkit.getImage(getClass().getResource("/icon64.png"));
        Image icon128 = toolkit.getImage(getClass().getResource("/icon128.png"));
        List<Image> icons = List.of(icon64, icon128);
        setIconImages(icons);
    }

    private void addListener() {
        button.addActionListener(this::onSend);
        checkbox.addItemListener(this::onTop);
    }

    // 事件处理...

    private void onSend(ActionEvent e) {
        String text = textArea.getText();
        if (text == null || text.isBlank()) {
            return;
        }

        Integer delay = runCatch(() -> Integer.parseInt(textField.getText()));

        componentEnabled(false);

        if (delay != null && delay > 0) {
            sleep(delay);
        }

        TypeUtils.typeString(text);
        componentEnabled(true);
    }

    private void onTop(ItemEvent e) {
        boolean onTop = e.getStateChange() == ItemEvent.SELECTED;
        setAlwaysOnTop(onTop);
    }

    private void componentEnabled(boolean enabled) {
        button.setEnabled(enabled);
        textArea.setEnabled(enabled);
        textField.setEnabled(enabled);
    }

    private static void createAndShowGUI() {
        var frame = new Main();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setTitle("Easy Typing");
        frame.setAlwaysOnTop(checkbox.getState());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(textArea.getSize());
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
