package combine.typing;

import combine.typing.util.TypeUtils;
import combine.typing.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main extends Frame {

    private CheckboxMenuItem alwaysOnTopMenu;

    private TextArea textArea;

    private TextField textField;

    private Button button;

    private About about;

    private Main() {
        init();
    }

    private void init() {
        alwaysOnTopMenu = new CheckboxMenuItem("Always On Top", false);
        textArea = new TextArea("", 10, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
        textField = new TextField("2000", 5);
        button = new Button("✍");
        about = new About(this);

        about.setSize(300, 150);
        about.setLocationRelativeTo(this);

        setIconImage(Utils.getImage("/icon64.png"));
        setLayout(new BorderLayout());

        var footerPanel = new Panel();
        footerPanel.add(new Label("Delay(ms):"));
        footerPanel.add(textField);
        footerPanel.add(button);

        add(textArea, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.PAGE_END);

        addListener();
        setMenuBar();
        setFrame();
    }

    private void setMenuBar() {
        Menu menu1 = new Menu("Options");
        menu1.add(alwaysOnTopMenu);

        MenuItem aboutItem = new MenuItem("About");
        aboutItem.addActionListener(this::onAbout);

        Menu menu2 = new Menu("Help");
        menu2.add(aboutItem);

        MenuBar menuBar = new MenuBar();
        menuBar.add(menu1);
        menuBar.add(menu2);
        setMenuBar(menuBar);
    }

    private void setFrame() {
        setTitle("Easy Typing");
        setMinimumSize(textArea.getSize());
        addWindowListener(new WindowLExit());
        setAlwaysOnTop(alwaysOnTopMenu.getState());
    }

    private void addListener() {
        button.addActionListener(this::onSend);
        alwaysOnTopMenu.addItemListener(this::onTop);
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

    private void onAbout(ActionEvent e) {
        about.setVisible(true);
    }

    private void componentEnabled(boolean enabled) {
        button.setEnabled(enabled);
        textArea.setEnabled(enabled);
        textField.setEnabled(enabled);
    }

    private static void createAndShowGUI() {
        var frame = new Main();
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
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

    private static class WindowLDispose extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            e.getWindow().dispose();
        }
    }

    private static class WindowLExit extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
