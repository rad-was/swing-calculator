package calculator;

import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;

public class CalculatorPanel extends JPanel {
    private final JTextField display;
    private String currentNumber = "";

    public CalculatorPanel() {

        Action numberAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentNumber += e.getActionCommand();
                //System.out.println(currentNumber);
                if (display.getText().equals("0")) {
                    display.setText("");
                }
                display.replaceSelection(e.getActionCommand());
            }
        };

        setLayout(new BorderLayout());

        display = new JTextField();
        display.setPreferredSize(new Dimension(100, 50));
        display.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        display.setFont(display.getFont().deriveFont(20f));
        display.setEditable(false);
        display.setHighlighter(null);
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

        JLabel resultLabel = new JLabel();
        resultLabel.setPreferredSize(new Dimension(100, 40));
        resultLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        resultLabel.setHorizontalAlignment(JTextField.RIGHT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(resultLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;

        add(buttonPanel, BorderLayout.SOUTH);

        ArrayList<calculator.Button> numberButtons = new ArrayList<>(List.of(
                new calculator.Button("7", 0, 2),
                new calculator.Button("8", 1, 2),
                new calculator.Button("9", 2, 2),
                new calculator.Button("4", 0, 3),
                new calculator.Button("5", 1, 3),
                new calculator.Button("6", 2, 3),
                new calculator.Button("1", 0, 4),
                new calculator.Button("2", 1, 4),
                new calculator.Button("3", 2, 4),
                new calculator.Button("0", 1, 5)
        ));

        for (calculator.Button b : numberButtons) {
            String text = b.getName();
            JButton button = new JButton(text);
            button.setFont(new Font("Sans Serif", Font.BOLD, 15));

            c.weightx = 0.1;
            c.gridx = b.getX();
            c.gridy = b.getY();
            button.addActionListener(numberAction);
            buttonPanel.add(button, c);

            InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(KeyStroke.getKeyStroke(text), text);
            inputMap.put(KeyStroke.getKeyStroke("NUMPAD" + text), text);
            buttonPanel.getActionMap().put(text, numberAction);
        }

        InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        JButton add = new JButton("+");
        add.setFont(new Font("Sans Serif", Font.BOLD, 15));
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 4;
        add.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                display.replaceSelection("+");
                currentNumber = "";
            }
        });
        buttonPanel.add(add, c);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK), "ShiftPlus");
        buttonPanel.getActionMap().put("ShiftPlus", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add.doClick();
            }
        });

        JButton subtract = new JButton("-");
        subtract.setFont(new Font("Sans Serif", Font.BOLD, 15));
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 3;
        subtract.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                display.replaceSelection("-");
                currentNumber = "";
            }
        });
        buttonPanel.add(subtract, c);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "Minus");
        buttonPanel.getActionMap().put("Minus", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subtract.doClick();
            }
        });

        //JButton multiply = new JButton("×");
        HighlightButton multiply = new HighlightButton("×");
        multiply.setFont(new Font("Sans Serif", Font.BOLD, 15));
        multiply.setHighlight(new Color(157, 233, 134, 64));
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 2;
        multiply.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                display.replaceSelection("×");
                currentNumber = "";
            }
        });
        buttonPanel.add(multiply, c);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.SHIFT_DOWN_MASK), "ShiftEight");
        buttonPanel.getActionMap().put("ShiftEight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiply.doClick();
            }
        });

        HighlightButton divide = new HighlightButton("÷");
        divide.setFont(new Font("Sans Serif", Font.BOLD, 15));
        divide.setHighlight(new Color(157, 233, 134, 64));
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 1;
        divide.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
                display.replaceSelection("÷");
                currentNumber = "";
            }
        });
        buttonPanel.add(divide, c);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "Slash");
        buttonPanel.getActionMap().put("Slash", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                divide.doClick();
            }
        });

        JButton separator = new JButton(".");
        separator.setFont(new Font("Sans Serif", Font.BOLD, 15));
        c.weightx = 0.1;
        c.gridx = 2;
        c.gridy = 5;
        separator.addActionListener(e -> {
            if (!display.getText().isBlank()
                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))
                    && StringUtils.countMatches(currentNumber, ".") == 0) {

                display.replaceSelection(".");
                currentNumber += ".";
            }
        });
        buttonPanel.add(separator, c);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), "Period");
        buttonPanel.getActionMap().put("Period", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                separator.doClick();
            }
        });

        JButton backspace = new JButton("⌫");
        backspace.setFont(new Font("Sans Serif", Font.BOLD, 15));
        c.weightx = 0.1;
        c.gridx = 2;
        c.gridy = 1;
        backspace.addActionListener(e -> {
            if (!display.getText().isBlank() && !display.getText().equals("0")) {
                display.setText(display.getText().substring(0, display.getText().length() - 1));
                currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
            }
        });
        buttonPanel.add(backspace, c);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && e.getID() == KeyEvent.KEY_RELEASED) {
                backspace.doClick();
                return true;
            }
            return false;
        });

        JButton clear = new JButton("C");
        clear.setFont(new Font("Sans Serif", Font.BOLD, 15));
        c.weightx = 0.1;
        c.gridx = 1;
        c.gridy = 1;
        clear.addActionListener(e -> {
            if (!display.getText().isBlank()) {
                display.setText("0");
                currentNumber = "";
            }
        });
        buttonPanel.add(clear, c);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "C");
        buttonPanel.getActionMap().put("C", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear.doClick();
            }
        });

        JButton equal = new JButton("=");
        equal.setFont(new Font("Sans Serif", Font.BOLD, 15));
        c.weightx = 0.1;
        c.gridx = 3;
        c.gridy = 5;
        equal.addActionListener(e -> {
//            if (!display.getText().isBlank()
//                    && Character.isDigit(display.getText().charAt(display.getText().length() - 1))) {
//                // display.replaceSelection("=");
//                resultLabel.setText("= ");
//            }
            if (validOperation(display.getText())) {
                //Expression ex = new Expression(display.getText().substring(0, display.getText().length() - 1));
                Expression ex = new Expression(display.getText());
                double result = ex.calculate();
                DecimalFormat format = new DecimalFormat("#,##########0.##########");
                //display.setText(display.getText() + format.format(result));
                resultLabel.setText("= " + format.format(result));
            }
        });
        buttonPanel.add(equal, c);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "Equals");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Equals");
        buttonPanel.getActionMap().put("Equals", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                equal.doClick();
            }
        });

    }

    private boolean validOperation(String s) {
        if (s.isBlank() || s.isEmpty()) {
            return false;
        }
        if (StringUtils.containsAny(s, "+-÷×")) {
            //String[] numbers = s.split("[+-÷×]");
            return true;
        }
        return false;
    }

    private static Object evaluateExpression(String mathExpression) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        return engine.eval(mathExpression);
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("Calculator");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
        frame.setPreferredSize(new Dimension(320, 350));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new CalculatorPanel());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws ScriptException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
            }
        });
    }


}
