package calculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalculatorPanel extends JPanel {
    static final JTextField display = new JTextField("0");
    static final JLabel resultLabel = new JLabel();
    static String currentNumber = "";
    static final JPanel buttonPanel = new JPanel();

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

        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 10;
        add(buttonPanel, BorderLayout.SOUTH);

        final int[] numberButtonsGridX = {1, 0, 1, 2, 0, 1, 2, 0, 1, 2};
        final int[] numberButtonsGridY = {5, 4, 4, 4, 3, 3, 3, 2, 2, 2};

        for (int i = 0; i < 10; ++i) {
            ButtonCreator.createNumericButton(
                    String.valueOf(i), gbc, numberButtonsGridX[i], numberButtonsGridY[i]);
        }

        Font plain = new Font("Sans Serif", Font.PLAIN, 17);
        Font bold = new Font("Sans Serif", Font.BOLD, 17);

//        ButtonCreator.createOperationButton("( )", plain, gbc, 0, 1, KeyEvent.VK_9, KeyEvent.VK_0, true, "ShiftNineZero");
        ButtonCreator.createOperationButton("+", bold, gbc, 3, 4, KeyEvent.VK_EQUALS, true, "ShiftPlus");
        ButtonCreator.createOperationButton("-", bold, gbc, 3, 3, KeyEvent.VK_MINUS, false, "Minus");
        ButtonCreator.createOperationButton("×", bold, gbc, 3, 2, KeyEvent.VK_8, true, "ShiftEight");
        ButtonCreator.createOperationButton("÷", bold, gbc, 3, 1, KeyEvent.VK_SLASH, false, "Slash");
        ButtonCreator.createOperationButton(".", bold, gbc, 2, 5, KeyEvent.VK_PERIOD, false, "Period");
        ButtonCreator.createOperationButton("C", plain, gbc, 1, 1, KeyEvent.VK_C, false, "C");
        ButtonCreator.createOperationButton("=", plain, gbc, 3, 5, KeyEvent.VK_EQUALS, KeyEvent.VK_ENTER, false, "Equals");
        ButtonCreator.createBackspaceButton(plain, gbc);
    }
}
