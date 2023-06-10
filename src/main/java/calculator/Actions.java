package calculator;

import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Expression;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static calculator.CalculatorPanel.display;
import static calculator.CalculatorPanel.currentNumber;
import static calculator.CalculatorPanel.resultLabel;

public abstract class Actions {
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

    static AbstractAction click(JButton button) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick();
                System.out.println("log: \"" + button.getText() + "\" button clicked, current number: " + currentNumber);
            }
        };
    }

    static AbstractAction genericAction(String s) {
        String text = display.getText();
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!text.isBlank() &&
                        (Character.isDigit(text.charAt(text.length() - 1)) ||
                                text.charAt(text.length() - 1) == ')' ||
                                text.charAt(text.length() - 1) == '%')) {

                    addResultToDisplayIfPresent();
                    display.replaceSelection(s);
                    currentNumber = "";
                }
            }
        };
    }

    static AbstractAction parenthesesAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!display.getText().isBlank()) {
                    if (display.getText().equals("0")) {
                        display.setText("(");
                    } else {
                        if (StringUtils.countMatches(display.getText(), "(") > StringUtils.countMatches(display.getText(), ")")) {
                            display.setText(display.getText() + ")");
                        } else {
                            display.setText(display.getText() + "(");
                        }
                    }
                    currentNumber = "";
                }
            }
        };
    }

    static AbstractAction separatorAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!display.getText().isBlank()
                        && Character.isDigit(display.getText().charAt(display.getText().length() - 1))
                        && StringUtils.countMatches(currentNumber, ".") == 0
                        && (resultLabel.getText().isEmpty()
                        || StringUtils.countMatches(resultLabel.getText().substring(2), ".") == 0)) {

                    addResultToDisplayIfPresent();
                    display.replaceSelection(".");
                    currentNumber += ".";
                }
            }
        };
    }

    static AbstractAction backspaceAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        };
    }

    static AbstractAction clearAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!display.getText().isBlank()) {
                    display.setText("0");
                    resultLabel.setText("");
                    currentNumber = "";
                }
            }
        };
    }

    static AbstractAction equalAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = display.getText();
                if (!s.isBlank() && !StringUtils.containsAny(s.substring(s.length() - 1), "+-÷×")) {
                    if (s.charAt(s.length() - 1) == '.') {
                        s = s.substring(0, s.length() - 1);
                        display.setText(s);
                    }
                    if (StringUtils.countMatches(s, "(") > StringUtils.countMatches(s, ")")) {
                        s = s + ")";
                        display.setText(s);
                    }
                    Expression ex = new Expression(s);
                    double result = ex.calculate();
                    DecimalFormat format = new DecimalFormat("#,##########0.##########");
                    resultLabel.setText("= " + format.format(result));
                }
            }
        };
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

    private static void addResultToDisplayIfPresent() {
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
}
