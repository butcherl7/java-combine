package combine.typing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main extends Frame {

    private static final TextArea textArea = new TextArea("", 10, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);

    private static final TextField textField = new TextField("2000", 5);

    private static final Label label = new Label("Delay(ms):");

    private static final Button button = new Button("✍");

    private static final CheckboxMenuItem alwaysOnTopMenu = new CheckboxMenuItem("Always On Top", false);

    private static final long pid = ProcessHandle.current().pid();

    private Main() {
        init();
    }

    private void init() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon64.png")));

        setLayout(new BorderLayout());
        setMenuBar();

        var footerPanel = new Panel();
        footerPanel.add(label);
        footerPanel.add(textField);
        footerPanel.add(button);

        add(textArea, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.PAGE_END);

        addListener();
    }

    private void setMenuBar() {
        Menu menu1 = new Menu("Options");
        menu1.add(alwaysOnTopMenu);

        // MenuItem pidItem = new MenuItem("PID: " + ProcessHandle.current().pid());
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.addActionListener(this::onAbout);

        Menu menu2 = new Menu("Help");
        menu2.add(aboutItem);

        MenuBar menuBar = new MenuBar();
        menuBar.add(menu1);
        menuBar.add(menu2);
        setMenuBar(menuBar);
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
        Panel panel = new Panel();
        BoxLayout panelLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(panelLayout);
        panel.add(new Label("Runtime version: " + System.getProperty("java.vendor") + " " + System.getProperty("java.runtime.version")));
        panel.add(new Label("PID: " + pid));

        Dialog dialog = new Dialog(this, "About", true);
        dialog.add(panel);
        dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/info16.png")));
        dialog.addWindowListener(new WindowLDispose());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void componentEnabled(boolean enabled) {
        button.setEnabled(enabled);
        textArea.setEnabled(enabled);
        textField.setEnabled(enabled);
    }

    private static void createAndShowGUI() {
        var frame = new Main();
        frame.addWindowListener(new WindowLExit());
        frame.setTitle("Easy Typing");
        frame.setAlwaysOnTop(alwaysOnTopMenu.getState());
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
