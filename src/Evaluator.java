import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Evaluator {
    public static double evaluate(char[] expression) {
        // Parse expression with Shunting-Yard algorithm by Edsger Dijkstra.
        Stack<String> operators = new Stack<>();
        Queue<String> output = new LinkedList<>();

        for (int i = 0; i < expression.length; i++) {
            // Stop parsing at the equals sign.
            if (expression[i] == '=') {
                break;
            }

            // While char is '0'-'9' or '.', add to token, then push all to output.
            if (Character.isDigit(expression[i]) || expression[i] == '.') {
                String token = "";
                int decCount = 0;

                while (i < expression.length && (Character.isDigit(expression[i]) || expression[i] == '.')) {
                    if (expression[i] == '.') decCount++;
                    token += expression[i];
                    i++;
                }

                // Throw error if last digit is decimal point.
                if (expression[i-1] == '.') {
                    throw new IllegalArgumentException("Invalid expression - Number ends with '.'");
                }
                // Throw error if token has multiple decimal points.
                if (decCount > 1) {
                    throw new IllegalArgumentException("Invalid expression - Number has more than one '.'");
                }

                output.add(token);
                i--;
                continue;
            }

            if (expression[i] == '(') {
                // Check for implicit multiplication before parentheses.
                if (i != 0 && Character.isDigit(expression[i-1])) {
                    operators.add("*");
                }

                // Evaluate everything within the parenthesis and push result to output.
                i++;
                String token = "";

                while (expression[i] != ')') {
                    token += expression[i];
                    i++;
                }

                output.add(evaluate(token.toCharArray()) + "");

                // Check for implicit multiplication after parentheses.
                if (i + 1 < expression.length && (Character.isDigit(expression[i+1]) || expression[i+1] == '.')) {
                    operators.add("*");
                }
                continue;
            }

            if (expression[i] == '+') {
                if (!operators.isEmpty()) {
                    String o = operators.peek();
                    while (o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("^")) {
                        output.add(operators.pop());
                        if (operators.isEmpty()) break;
                        o = operators.peek();
                    }
                }
                operators.push("+");
                continue;
            }

            if (expression[i] == '-') {
                if (i == 0 || expression[i-1] == '(' || expression[i-1] == '+'|| expression[i-1] == '-'
                        || expression[i-1] == '*' || expression[i-1] == '/' || expression[i-1] == '^') {
                    operators.push("*");
                    output.add("-1");
                } else {
                    if (!operators.isEmpty()) {
                        String o = operators.peek();
                        while (o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("^")) {
                            output.add(operators.pop());
                            if (operators.isEmpty()) break;
                            o = operators.peek();
                        }
                    }
                    operators.push("-");
                }
                continue;
            }

            if (expression[i] == '*') {
                if (!operators.isEmpty()) {
                    String o = operators.peek();
                    while (o.equals("*") || o.equals("/") || o.equals("^")) {
                        output.add(operators.pop());
                        if (operators.isEmpty()) break;
                        o = operators.peek();
                    }
                }
                operators.push("*");
                continue;
            }

            if (expression[i] == '/') {
                if (!operators.isEmpty()) {
                    String o = operators.peek();
                    while (o.equals("*") || o.equals("/") || o.equals("^")) {
                        output.add(operators.pop());
                        if (operators.isEmpty()) break;
                        o = operators.peek();
                    }
                }
                operators.push("/");
                continue;
            }

            if (expression[i] == '^') {
                operators.push("^");
                continue;
            }

            // Functions.
            // sin, cos, tan, cot, ln, log
            // If char is a letter, get three-char function name and check if valid.
            if (Character.isLetter(expression[i])) {
                // Throw error if there is not enough room for a function name.
                if (expression.length - i < 3) {
                    throw new IllegalArgumentException("Invalid Input - Incorrect function name at end of expression.");
                }

                String token = "" + expression[i] + expression[i+1] + expression[i+2];
                switch (token.toLowerCase()) {
                    case "sin":
                        operators.push("sin");
                        break;
                    case "cos":
                        operators.push("cos");
                        break;
                    case "tan":
                        operators.push("tan");
                        break;
                    case "cot":
                        operators.push("cot");
                        break;
                    case "log":
                        operators.push("log");
                        break;
                    case "ln(":
                        operators.push("ln");
                        i--; // ln is shorter than the rest, so offset i.
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid Input - Incorrect function name.");
                }
                i += 3; // Move i to after the function name.
                continue;
            }

            throw new IllegalArgumentException("Invalid Input - Incorrect character in expression.");
        }

        while (!operators.isEmpty()) output.add(operators.pop());

        return 0;
    }
}
