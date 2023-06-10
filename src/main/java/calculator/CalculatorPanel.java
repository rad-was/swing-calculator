package calculator;

import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class CalculatorPanel extends JPanel {
    static final JTextField display = new JTextField("0");
    static final JLabel resultLabel = new JLabel();
    static String currentNumber = "";

    public CalculatorPanel() {
        setLayout(new BorderLayout());

        display.setPreferredSize(new Dimension(100, 50));
        display.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        display.setFont(display.getFont().deriveFont(20f));
        display.setEditable(false);
        display.setHighlighter(null);
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

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

        final int[] numberButtonsGridX = {1, 0, 1, 2, 0, 1, 2, 0, 1, 2};
        final int[] numberButtonsGridY = {5, 4, 4, 4, 3, 3, 3, 2, 2, 2};

        for (int i = 0; i < 10; ++i) {
            ButtonCreator.createNumericButton(
                    String.valueOf(i), buttonPanel, gbc, numberButtonsGridX[i], numberButtonsGridY[i]);
        }

        InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        createAddButton(buttonPanel, gbc, inputMap);
        createSubtractButton(buttonPanel, gbc, inputMap);
        createMultiplyButton(buttonPanel, gbc, inputMap);
        createDivideButton(buttonPanel, gbc, inputMap);
        createSeparatorButton(buttonPanel, gbc, inputMap);
        createBackspaceButton(buttonPanel, gbc);
        createClearButton(buttonPanel, gbc, inputMap);
        createEqualButton(buttonPanel, gbc, inputMap);
    }

    private void createEqualButton(JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
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

    private void createClearButton(JPanel buttonPanel, GridBagConstraints gbc, InputMap inputMap) {
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
            if (result.equals("NaN")) {
                resultLabel.setText("");
            } else {
                resultLabel.setText("");
                display.setText(result);
                currentNumber = result;
            }
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
}
