package calculator;

import javax.swing.*;
import java.awt.*;

public abstract class ButtonCreator {
    public static void createNumericButton(String name, JPanel buttonPanel, GridBagConstraints gbc, int gridx, int gridy) {
        JButton button = new JButton(name);
        button.setFont(new Font("Sans Serif", Font.PLAIN, 17));

        gbc.weightx = 0.1;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        button.addActionListener(ActionCreator.numberAction());
        buttonPanel.add(button, gbc);

        InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(name), name);
        inputMap.put(KeyStroke.getKeyStroke("NUMPAD" + name), name);
        buttonPanel.getActionMap().put(name, ActionCreator.numberAction());
    }
}
