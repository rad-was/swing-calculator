package calculator;

import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class CalculatorPanel extends JPanel {
    private final JTextField display;
    private final JLabel resultLabel;
    private String currentNumber = "";

    public CalculatorPanel() {
        setLayout(new BorderLayout());

        display = new JTextField("0");
        display.setPreferredSize(new Dimension(100, 50));
        display.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        display.setFont(display.getFont().deriveFont(20f));
        display.setEditable(false);
        display.setHighlighter(null);
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

        resultLabel = new JLabel();
        resultLabel.setPreferredSize(new Dimension(100, 40));
        resultLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        resultLabel.setHorizontalAlignment(JTextField.RIGHT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(resultLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 10;

        add(buttonPanel, BorderLayout.SOUTH);

        Action numberAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentNumber += e.getActionCommand();
                if (display.getText().equals("0")) {
                    display.setText("");
                }
                if (!resultLabel.getText().isBlank()) {
                    resultLabel.setText("");
                }
                display.replaceSelection(e.getActionCommand());
            }
        };

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

        for (Button b : numberButtons) {
            String text = b.name();
            JButton button = new JButton(text);
            button.setFont(new Font("Sans Serif", Font.PLAIN, 17));

            gbc.weightx = 0.1;
            gbc.gridx = b.x();
            gbc.gridy = b.y();
            button.addActionListener(numberAction);
            buttonPanel.add(button, gbc);

            InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(KeyStroke.getKeyStroke(text), text);
            inputMap.put(KeyStroke.getKeyStroke("NUMPAD" + text), text);
            buttonPanel.getActionMap().put(text, numberAction);
        }

        InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        createAddButton(buttonPanel, gbc, inputMap);
        createSubtractButton(buttonPanel, gbc, inputMap);
        createMultiplyButton(buttonPanel, gbc, inputMap);
        createDivideButton(buttonPanel, gbc, inputMap);
        createSeparatorButton(buttonPanel, gbc, inputMap);
        createBackspaceButton(buttonPanel, gbc);
        createClearButton(resultLabel, buttonPanel, gbc, inputMap);
        createEqualButton(resultLabel, buttonPanel, gbc, inputMap);
    }

    private void createEqualButton(JLabel resultLabel, JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
        JButton equal = new JButton("=");
        equal.setFont(new Font("Sans Serif", Font.BOLD, 17));
        gbc.weightx = 0.1;
        gbc.gridx = 3;
        gbc.gridy = 5;
        equal.addActionListener(e -> {
            if (validOperation(display.getText())) {
                Expression ex = new Expression(display.getText());
                double result = ex.calculate();
                DecimalFormat format = new DecimalFormat("#,##########0.##########");
                resultLabel.setText("= " + format.format(result));
            }
        });
        buttonPanel.add(equal, gbc);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "Equals");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Equals");
        buttonPanel.getActionMap().put("Equals", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                equal.doClick();
            }
        });
    }

    private void createClearButton(JLabel resultLabel, JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
        JButton clear = new JButton("C");
        clear.setFont(new Font("Sans Serif", Font.PLAIN, 17));
        gbc.weightx = 0.1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        clear.addActionListener(e -> {
            if (!display.getText().isBlank()) {
                display.setText("0");
                resultLabel.setText("");
                currentNumber = "";
            }
        });
        buttonPanel.add(clear, gbc);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "C");
        buttonPanel.getActionMap().put("C", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear.doClick();
            }
        });
    }

    private void createBackspaceButton(JPanel buttonPanel, GridBagConstraints gbc) {
        JButton backspace = new JButton("⌫");
        backspace.setFont(new Font("Sans Serif", Font.PLAIN, 17));
        gbc.weightx = 0.1;
        gbc.gridx = 2;
        gbc.gridy = 1;
        backspace.addActionListener(e -> {
            if (!display.getText().isBlank() && !display.getText().equals("0")) {

                if (!resultLabel.getText().isBlank()) {
                    resultLabel.setText("");
                }
                String oldDisplay = display.getText();
                display.setText(display.getText().substring(0, display.getText().length() - 1));
                if (display.getText().isBlank()) {
                    display.setText("0");
                    currentNumber = "";
                } else {
                    if (!Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                        currentNumber = "";
                    } else {
                        if (!Character.isDigit(oldDisplay.charAt(oldDisplay.length() - 1))) {
                            currentNumber = extractLastNumber(display.getText());
                        } else {
                            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
                        }
                    }
                    // UGLY BUT SEEMS TO BE WORKING FOR NOW ?
                }
            }
        });
        buttonPanel.add(backspace, gbc);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && e.getID() == KeyEvent.KEY_RELEASED) {
                backspace.doClick();
                return true;
            }
            return false;
        });
    }

    private void createSeparatorButton(JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
        JButton separator = new JButton(".");
        separator.setFont(new Font("Sans Serif", Font.BOLD, 17));
        gbc.weightx = 0.1;
        gbc.gridx = 2;
        gbc.gridy = 5;
        separator.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))
                    && StringUtils.countMatches(currentNumber, ".") == 0
                    && (resultLabel.getText().isEmpty()
                    || StringUtils.countMatches(resultLabel.getText().substring(2), ".") == 0)) {

                addResultToDisplayIfPresent();
                display.replaceSelection(".");
                currentNumber += ".";
            }
        });
        buttonPanel.add(separator, gbc);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), "Period");
        buttonPanel.getActionMap().put("Period", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                separator.doClick();
            }
        });
    }

    private void createDivideButton(JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
        JButton divide = new JButton("÷");
        divide.setFont(new Font("Sans Serif", Font.BOLD, 17));
        gbc.weightx = 0.1;
        gbc.gridx = 3;
        gbc.gridy = 1;
        divide.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {

                addResultToDisplayIfPresent();
                display.replaceSelection("÷");
                currentNumber = "";
            }
        });
        buttonPanel.add(divide, gbc);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "Slash");
        buttonPanel.getActionMap().put("Slash", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                divide.doClick();
            }
        });
    }

    private void createMultiplyButton(JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
        JButton multiply = new JButton("×");
        multiply.setFont(new Font("Sans Serif", Font.BOLD, 17));
        gbc.weightx = 0.1;
        gbc.gridx = 3;
        gbc.gridy = 2;
        multiply.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {

                addResultToDisplayIfPresent();
                display.replaceSelection("×");
                currentNumber = "";
            }
        });
        buttonPanel.add(multiply, gbc);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.SHIFT_DOWN_MASK), "ShiftEight");
        buttonPanel.getActionMap().put("ShiftEight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiply.doClick();
            }
        });
    }

    private void createSubtractButton(JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
        JButton subtract = new JButton("-");
        subtract.setFont(new Font("Sans Serif", Font.BOLD, 17));
        gbc.weightx = 0.1;
        gbc.gridx = 3;
        gbc.gridy = 3;
        subtract.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {

                addResultToDisplayIfPresent();
                display.replaceSelection("-");
                currentNumber = "";
            }
        });
        buttonPanel.add(subtract, gbc);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "Minus");
        buttonPanel.getActionMap().put("Minus", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subtract.doClick();
            }
        });
    }

    private void createAddButton(JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
        JButton add = new JButton("+");
        add.setFont(new Font("Sans Serif", Font.BOLD, 17));
        gbc.weightx = 0.1;
        gbc.gridx = 3;
        gbc.gridy = 4;
        add.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {

                addResultToDisplayIfPresent();
                display.replaceSelection("+");
                currentNumber = "";
            }
        });
        buttonPanel.add(add, gbc);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK), "ShiftPlus");
        buttonPanel.getActionMap().put("ShiftPlus", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add.doClick();
            }
        });
    }

    private void addResultToDisplayIfPresent() {
        if (!resultLabel.getText().isBlank()) {
            String result = resultLabel.getText().substring(2); // to get just the number
            resultLabel.setText("");
            display.setText(result);
            currentNumber = result;
        }
    }

    private static String extractLastNumber(String input) {
        Pattern pattern = Pattern.compile("[\\d.]+$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    private boolean validOperation(String s) {
        if (s.isBlank() || s.isEmpty() || !Character.isDigit(s.charAt(s.length() - 1))) {
            return false;
        }
        return StringUtils.containsAny(s, "+-÷×");
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("Calculator");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
        frame.setPreferredSize(new Dimension(320, 360));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new CalculatorPanel());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public record Button(String name, int x, int y) { }

    public static void main(String[] args) {
        EventQueue.invokeLater(CalculatorPanel::createAndShowUI);
    }
}
