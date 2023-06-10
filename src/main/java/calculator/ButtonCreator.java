package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import static calculator.CalculatorPanel.buttonPanel;
import static calculator.CalculatorPanel.currentNumber;

public abstract class ButtonCreator {
    static void createNumericButton(String name, GridBagConstraints gbc, int gridx, int gridy) {
        JButton button = new JButton(name);
        button.setFont(new Font("Sans Serif", Font.PLAIN, 17));
        gbc.weightx = 0.1;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        button.addActionListener(Actions.numberAction());
        buttonPanel.add(button, gbc);

        InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(name), name);
        inputMap.put(KeyStroke.getKeyStroke("NUMPAD" + name), name);
        buttonPanel.getActionMap().put(name, Actions.click(button));
    }

    static void createOperationButton(String name, Font font, GridBagConstraints gbc, int gridx, int gridy,
                                      int key, boolean shiftDown, String actionMapName) {

        JButton button = createButton(name, font, gbc, gridx, gridy);
        addToActionMap(button, key, shiftDown, actionMapName);
    }

    @SuppressWarnings("SameParameterValue")
    static void createOperationButton(String name, Font font, GridBagConstraints gbc, int gridx, int gridy,
                                      int key1, int key2, boolean shiftDown, String actionMapName) {

        JButton button = createButton(name, font, gbc, gridx, gridy);
        addToActionMap(button, key1, key2, shiftDown, actionMapName);
    }

    static void createBackspaceButton(Font font, GridBagConstraints gbc) {

        JButton button = createButton("⌫", font, gbc, 2, 1);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && e.getID() == KeyEvent.KEY_RELEASED) {
                button.doClick();
                System.out.println("log: \"" + button.getText() + "\" button clicked, current number: " + currentNumber);
                return true;
            }
            return false;
        });
    }

    private static JButton createButton(String name, Font font, GridBagConstraints gbc, int gridx, int gridy) {
        JButton button = new JButton(name);
        button.setFont(font);
        gbc.weightx = 0.1;
        gbc.gridx = gridx;
        gbc.gridy = gridy;

        switch (name) {
            case "÷", "×", "-", "+" -> button.addActionListener(Actions.genericAction(name));
            case "( )" -> button.addActionListener(Actions.parenthesesAction());
            case "." -> button.addActionListener(Actions.separatorAction());
            case "⌫" -> button.addActionListener(Actions.backspaceAction());
            case "C" -> button.addActionListener(Actions.clearAction());
            case "=" -> button.addActionListener(Actions.equalAction());
        }
        buttonPanel.add(button, gbc);
        return button;
    }

    private static void addToActionMap(JButton button, int key, boolean shiftDown, String name) {
        InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (shiftDown) {
            inputMap.put(KeyStroke.getKeyStroke(key, KeyEvent.SHIFT_DOWN_MASK), name);
        } else {
            inputMap.put(KeyStroke.getKeyStroke(key, 0), name);
        }
        buttonPanel.getActionMap().put(name, Actions.click(button));
    }

    private static void addToActionMap(JButton button, int key1, int key2, boolean shiftDown, String name) {
        InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (shiftDown) {
            inputMap.put(KeyStroke.getKeyStroke(key1, KeyEvent.SHIFT_DOWN_MASK), name);
            inputMap.put(KeyStroke.getKeyStroke(key2, KeyEvent.SHIFT_DOWN_MASK), name);
        } else {
            inputMap.put(KeyStroke.getKeyStroke(key1, 0), name);
            inputMap.put(KeyStroke.getKeyStroke(key2, 0), name);
        }
        buttonPanel.getActionMap().put(name, Actions.click(button));
    }
}
