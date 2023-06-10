package calculator;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static calculator.CalculatorPanel.display;
import static calculator.CalculatorPanel.currentNumber;
import static calculator.CalculatorPanel.resultLabel;

public abstract class ActionCreator {
    static AbstractAction numberAction() {
        return new AbstractAction() {
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
    }
}
