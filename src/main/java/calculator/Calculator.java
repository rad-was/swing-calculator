package calculator;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Calculator extends JFrame {
    Calculator() {
        super("Calculator");
        setVisible(true);
        setSize(300, 400);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final URL imageResource = Calculator.class.getClassLoader().getResource("icon.png");
        final Image image = defaultToolkit.getImage(imageResource);
        final Taskbar taskbar = Taskbar.getTaskbar();
        try {
            taskbar.setIconImage(image);
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }
        setIconImage(image);


        JTextField equationTextField = new JTextField();
        equationTextField.setBounds(50, 30, 200, 30);
        add(equationTextField);

        JButton solve = new JButton("Solve");
        solve.setBounds(100, 200, 100, 32);
        add(solve);

        solve.addActionListener(e -> {
            String input = equationTextField.getText();
            if (input.matches("\\d+\\+\\d+")) {
                int plusIndex = input.indexOf("+");
                equationTextField.setText(input + "="
                        + (Integer.parseInt(input.substring(0, plusIndex))
                        + Integer.parseInt(input.substring(plusIndex + 1))));
            }
        });

        getRootPane().setDefaultButton(solve);
    }
}
