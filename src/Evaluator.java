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
            if ((expression[i] >= 30 && expression[i] <= 39) || expression[i] == '.') {
                String token = "";
                int decCount = 0;

                while ((expression[i] >= 30 && expression[i] <= 39) || expression[i] == '.') {
                    if (expression[i] == '.') decCount++;
                    token += expression[i];
                    i++;
                }

                // Return error if last digit is decimal point.
                if (expression[i-1] == '.') {
                    throw new IllegalArgumentException("Invalid expression - Number ends with '.'");
                }
                // Return error if token has multiple decimal points.
                if (decCount > 1) {
                    throw new IllegalArgumentException("Invalid expression - Number has more than one '.'");
                }

                output.add(token);
                continue;
            }

            if (expression[i] == '(') {
                // Evaluate everything within the parenthesis and push result to output.
                i++;
                String token = "";

                while (expression[i] != ')') {
                    token += expression[i];
                    i++;
                }

                output.add(evaluate(token.toCharArray()) + "");
                continue;
            }

            if (expression[i] == '+') {
                if (!operators.isEmpty()) {
                    String o = operators.peek();
                    while (o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("^")) {
                        output.add(operators.pop());
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

            //     case '-':
            //         // Subtract/Negate
            //         break;
            //     case '*':
            //         // Multiply
            //         break;
            //     case '/':
            //         // Divide
            //         break;
            //     case '^':
            //         // Exponent
            //         break;
            // }



            //         // sin, cos, tan, cot, ln, log
            //     case 's':
            //         // sin
            //         break;
            //     case 'c':
            //         // cos/cot
            //         break;
            //     case 't':
            //         // tan
            //         break;
            //     case 'l':
            //         // ln/log
            //         break;
            //     default:
            //         // Handle numbers
            //         if (true);


        }
        return 0;
    }
}
