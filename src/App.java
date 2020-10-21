import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class App {
    public static void main(String[] args) {
        // Concatenate all command line arguments into String.
        String input = "";
        for (String arg : args) input += arg;
        input = input.replaceAll("\\s",""); // Remove whitespace.
        char[] expression = input.toCharArray();

        // Print expression solution.
        try {
            System.out.println(Evaluator.evaluate(expression));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("An unknown error occurred!"
                    + " Please report this to Kyler Mosich before he gets a bad grade on this assignment.");
        }
    }
}
