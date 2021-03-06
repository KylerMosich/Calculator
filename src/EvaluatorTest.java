import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {

    @Test
    void evaluateTest() {
        // Single-operator arithmetic.
        assertEquals(7, Evaluator.evaluate("7".toCharArray()));
        assertEquals(5, Evaluator.evaluate("2+3".toCharArray()));
        assertEquals(-1, Evaluator.evaluate("4-5".toCharArray()));
        assertEquals(12, Evaluator.evaluate("6*2".toCharArray()));
        assertEquals(2.5, Evaluator.evaluate("5/2".toCharArray()));
        assertEquals(-5, Evaluator.evaluate("-5".toCharArray()));
        assertEquals(49, Evaluator.evaluate("7^2".toCharArray()));
        assertEquals(15, Evaluator.evaluate("3(5)".toCharArray()));

        // Multiple-operator arithmetic.
        assertEquals(-7, Evaluator.evaluate("-1*7".toCharArray()));
        assertEquals(25, Evaluator.evaluate("5(3+2)".toCharArray()));

        // Functions.
        assertEquals(0, Evaluator.evaluate("sin(0)".toCharArray()), 0.0001);
        assertEquals(1, Evaluator.evaluate("cos(0)".toCharArray()), 0.0001);
        assertEquals(0, Evaluator.evaluate("tan(0)".toCharArray()), 0.0001);
        assertEquals(0, Evaluator.evaluate("cot(π/2)".toCharArray()), 0.0001);
        assertEquals(0, Evaluator.evaluate("ln(1)".toCharArray()), 0.0001);
        assertEquals(0, Evaluator.evaluate("log(1)".toCharArray()), 0.0001);

        // The Omni-Test.
        assertEquals(-7.55, Evaluator.evaluate("-5.78+-(4-2.23)+sin(0)*cos(1)/(1+tan(2*ln(-3+2*(1.23+99.111))))".toCharArray()), 0.0001);

        // Exceptions.
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("1.0.4".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("1.+7".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("3+*4".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("17/".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("fdjhd".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("5+((3)".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("7/5)".toCharArray()));

        String max = String.format("%.0f", Math.pow(2, 53));
        assertThrows(ArithmeticException.class, () -> Evaluator.evaluate("1/0".toCharArray()));
        assertThrows(ArithmeticException.class, () -> Evaluator.evaluate(("9" + max).toCharArray()));
        assertThrows(ArithmeticException.class, () -> Evaluator.evaluate((max + "+" + max).toCharArray()));
        assertThrows(ArithmeticException.class, () -> Evaluator.evaluate(("-" + max + "-" + max).toCharArray()));
        assertThrows(ArithmeticException.class, () -> Evaluator.evaluate((max + "*" + max).toCharArray()));
        assertThrows(ArithmeticException.class, () -> Evaluator.evaluate((max + "/" + 0.5).toCharArray()));
        assertThrows(ArithmeticException.class, () -> Evaluator.evaluate((max + "^" + max).toCharArray()));
    }
}