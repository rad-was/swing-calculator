package calculator;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class Calculator extends JFrame {
    Calculator() {
        super("Calculator");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
//        setSize(280, 400);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        ArrayList<String> buttons = new ArrayList<>(List.of(
                "C", "⌫", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ",", "="
        ));

        ArrayList<Button> numberButtons = new ArrayList<>(List.of(
                new Button("7", 0, 2),
                new Button("8", 1, 2),
                new Button("9", 2, 2),
                new Button("4", 0, 3),
                new Button("5", 1, 3),
                new Button("6", 2, 3),
                new Button("1", 0, 4),
                new Button("2", 1, 4),
                new Button("3", 2, 4),
                new Button("0", 1, 5)
        ));


        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton button;

        JTextField display = new JTextField("0");
        display.setEditable(false);

        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(display.getFont().deriveFont(26f));
        display.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;
        add(display, c);

        c.ipady = 29;       //reset to default
        c.gridwidth = 1;

        Action numberAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.replaceSelection(e.getActionCommand());
            }
        };

        for (Button b : numberButtons) {
            button = new JButton(b.getName());
            c.weightx = 0.1;
            c.gridx = b.getX();
            c.gridy = b.getY();
            button.addActionListener(e -> {
                if (display.getText().equals("0")) {
                    display.setText(b.getName());
                } else {
                    display.setText(display.getText() + b.getName());
                }
            });
//            button.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    display.replaceSelection("+");
//                }
//            });
            add(button, c);

            InputMap inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(KeyStroke.getKeyStroke(button.getName()), button.getName());
            inputMap.put(KeyStroke.getKeyStroke("NUMPAD" + button.getName()), button.getName());
            button.getActionMap().put(button.getName(), numberAction);

        }

//        new Button("C", 1, 1),
//        new Button("⌫", 2, 1),
//        new Button("÷", 3, 1),
//        new Button("×", 3, 2),
//        new Button("-", 3, 3),
//        new Button("+", 3, 4),
//        new Button(",", 2, 5),
//        new Button("=", 3, 5)

        JButton clear = new JButton("C");
        c.weightx = 0.1;
        c.gridx = 1;
        c.gridy = 1;
        clear.addActionListener(e -> display.setText("0"));
        add(clear, c);

        JButton backspace = new JButton("⌫");
        c.weightx = 0.1;
        c.gridx = 2;
        c.gridy = 1;
        backspace.addActionListener(e -> {
            if (display.getText().length() == 1 && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                display.setText("0");
            } else if (!display.getText().isEmpty() && !display.getText().equals("0")) {
                display.setText(display.getText().substring(0, display.getText().length() - 1));
            }
        });
        add(backspace, c);

        JButton division = new JButton("÷");
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 1;
        division.addActionListener(e -> {
            if (!display.getText().isBlank() && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                display.setText(display.getText() + "÷");
            }
        });
        division.setBackground(Color.orange);
        division.setOpaque(true);
        division.setBorderPainted(false);
        add(division, c);

        JButton multiplication = new JButton("×");
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 2;
        add(multiplication, c);

        JButton subtraction = new JButton("-");
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 3;
        add(subtraction, c);

        JButton addition = new JButton("+");
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 4;
        addition.addActionListener(e -> {
            if (!display.getText().isBlank() && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                display.setText(display.getText() + "+");
            }
        });
        // Create an Action for the addition button
        // Create an Action for the addition button
        Action additionAction = new AbstractAction("Addition Button Action") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setText(display.getText() + "+");
                display.requestFocusInWindow();
            }
        };

        addition.addActionListener(e -> {
            if (!display.getText().isBlank() && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                display.setText(display.getText() + "+");
            }
        });

        // Add key binding for "+" key to activate the addition button
        InputMap inputMap = getRootPane().getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("+"), "AdditionButton");
        //inputMap.put(KeyStroke.getKeyStroke("+"), "AdditionButton");
        actionMap.put("AdditionButton", additionAction);
        add(addition, c);

        JButton comma = new JButton(",");
        c.weightx = 0.1;
        c.gridx = 2;
        c.gridy = 5;
        add(comma, c);

        JButton equal = new JButton("=");
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 5;
        add(equal, c);


        getRootPane().setDefaultButton(equal);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }
}
