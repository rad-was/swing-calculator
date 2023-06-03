package calculator;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MyFrame extends JFrame {
    MyFrame() {
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final URL imageResource = Main.class.getClassLoader().getResource("icon.png");
        final Image image = defaultToolkit.getImage(imageResource);

        final Taskbar taskbar = Taskbar.getTaskbar();

        try {
            taskbar.setIconImage(image);
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }

        this.setIconImage(image);

        //some default JFrame things
        this.setVisible(true);
        this.setTitle("Calculator");
        this.setSize(420, 420);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
