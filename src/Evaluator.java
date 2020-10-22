import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Evaluator {
    /**
     * Evaluate infix expression and return the result.
     * @param expression the infix expression to be evaluated.
     * @return a double containing the final result.
     */
    public static double evaluate(char[] expression) {
        Queue<String> output = shunt(expression);
        return evalRpn(output);
    }

    /**
     * Evaluate postfix expression and return the result.
     * @param output the postfix expression to be evaluated.
     * @return a double containing the final result.
     */
    private static double evalRpn(Queue<String> output) {
        Stack<Double> numbers = new Stack<>();
        final double MAX = Math.pow(2, 53); // Highest integer that can be stored with full precision in a double.

        // Handle each element in the output until it is empty.
        while (!output.isEmpty()) {
            String element = output.remove();

            // If the element is a valid double within the legal range, push to stack.
            // Otherwise, check which operator it is.
            try {
                numbers.push(Double.parseDouble(element));
                if (Math.abs(numbers.peek()) >= MAX)
                    throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
            } catch (NumberFormatException e) {
                double x;
                double y;
                // Handle operator.
                switch (element) {
                    case "+":
                        try {
                            numbers.push(numbers.pop() + numbers.pop());
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "-":
                        try {
                            x = numbers.pop();
                            y = numbers.pop();
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        numbers.push(y - x);
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "*":
                        try {
                            numbers.push(numbers.pop() * numbers.pop());
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "/":
                        try {
                            x = numbers.pop();
                            y = numbers.pop();
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        numbers.push(y / x);
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "^":
                        try {
                            x = numbers.pop();
                            y = numbers.pop();
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        numbers.push(Math.pow(y, x));
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "sin":
                        try {
                            numbers.push(Math.sin(numbers.pop()));
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "cos":
                        try {
                            numbers.push(Math.cos(numbers.pop()));
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "tan":
                        try {
                            numbers.push(Math.tan(numbers.pop()));
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "cot":
                        try {
                            x = numbers.pop();
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        numbers.push(Math.cos(x) / Math.sin(x));
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "log":
                        try {
                            numbers.push(Math.log10(numbers.pop()));
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                    case "ln":
                        try {
                            numbers.push(Math.log(numbers.pop()));
                        } catch (EmptyStackException ex) {
                            throw new IllegalArgumentException("Invalid Input - Operator without operands.");
                        }
                        if (Math.abs(numbers.peek()) >= MAX)
                            throw new ArithmeticException("Invalid Input - Expression becomes too large or undefined.");
                        break;
                }
            }
        }

        // Throw exception if there are too many numbers in stack.
        if (numbers.size() > 1)
            throw new IllegalArgumentException("Invalid Input - Operand without operator.");

        return numbers.pop();
    }

    /**
     * Convert infix expression to postfix expression.
     * @param expression the infix expression to be converted.
     * @return a Queue of Strings representing each token of the postfix expression.
     */
    private static Queue<String> shunt(char[] expression) {
        // Parse expression with (modified) Shunting-Yard algorithm by Edsger Dijkstra.
        Stack<String> operators = new Stack<>();
        Queue<String> output = new LinkedList<>();

        for (int i = 0; i < expression.length; i++) {
            // Stop parsing at the equals sign.
            if (expression[i] == '=') {
                break;
            }

            // Throw exception if ) is found before (.
            if (expression[i] == ')') {
                throw new IllegalArgumentException("Invalid Input - ')' without matching '('");
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
                if (expression[i - 1] == '.') {
                    throw new IllegalArgumentException("Invalid Input - Number ends with '.'");
                }
                // Throw error if token has multiple decimal points.
                if (decCount > 1) {
                    throw new IllegalArgumentException("Invalid Input - Number has more than one '.'");
                }

                output.add(token);
                i--;
                continue;
            }

            if (expression[i] == '(') {
                // Check for implicit multiplication before parentheses.
                if (i != 0 && Character.isDigit(expression[i - 1])) {
                    operators.add("*");
                }

                // Evaluate everything within the parenthesis and push result to output.
                i++;
                String token = "";
                int parenCount = 1;

                while (true) {
                    try {
                        if (expression[i] == '(') parenCount++;
                        else if (expression[i] == ')') parenCount--;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Throw exception if whole expression is parsed without finding ).
                        throw new IllegalArgumentException("Invalid Input - '(' without matching ')'");
                    }

                    // Stop iterating when all ( are matched with ).
                    if (parenCount == 0) break;

                    token += expression[i];
                    i++;
                }

                // Add solution to sub-expression in place of the parentheses.
                output.add(evaluate(token.toCharArray()) + "");

                // Check for implicit multiplication after parentheses.
                if (i + 1 < expression.length && (Character.isDigit(expression[i + 1]) || expression[i + 1] == '.')) {
                    operators.add("*");
                }
                continue;
            }

            // Handle addition.
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

            // Handle subtraction/unary negation.
            if (expression[i] == '-') {
                // Check for unary negation, then treat it as multiplying by -1.
                if (i == 0 || expression[i - 1] == '(' || expression[i - 1] == '+' || expression[i - 1] == '-'
                        || expression[i - 1] == '*' || expression[i - 1] == '/' || expression[i - 1] == '^') {
                    operators.push("*");
                    output.add("-1");
                } else { // Handle subtraction.
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

            // Handle multiplication.
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

            // Handle division.
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

            // Handle exponents.
            if (expression[i] == '^') {
                operators.push("^");
                continue;
            }

            // Functions //

            // If char is a letter, get three-char function name and check if valid.
            if (Character.isLetter(expression[i])) {
                // Throw error if there is not enough room for a function name.
                if (expression.length - i < 3) {
                    throw new IllegalArgumentException("Invalid Input - Incorrect function name at end of expression."
                    + " Valid functions are sin(), cos(), tan(), cot(), log(), and ln().");
                }

                String token = "" + expression[i] + expression[i + 1] + expression[i + 2];
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
                        throw new IllegalArgumentException("Invalid Input - Incorrect function name."
                        + " Valid functions are sin(), cos(), tan(), cot(), log(), and ln().");
                }
                i += 2; // Move i to after the function name.
                continue;
            }

            // If character is still not handled, is illegal.
            throw new IllegalArgumentException("Invalid Input - Incorrect character in expression.");
        }

        // Move all remaining operators to output, then return.
        while (!operators.isEmpty()) output.add(operators.pop());

        return output;
    }
}
