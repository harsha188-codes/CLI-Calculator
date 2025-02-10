import java.util.Scanner;
import java.util.Stack;

public class Application {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the valid arithmetic expression: ");
        String expression = input.nextLine();
        System.out.println("Expression: " + expression);

        boolean isValid = isValidParenthisis(expression) && isValidOperators(expression);

        if (isValid) {
            try {
                String postfix = infixToPostfix(expression);
                double result = evaluatePostfix(postfix);
                System.out.printf("Result: %.2f%n", result);
            } catch (Exception e) {
                System.out.println("Error evaluating expression: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid expression!");
        }
    }

    // Validate operators
    public static boolean isValidOperators(String exp) {
        for (int index = 0; index < exp.length() - 1; index++) {
            char currentChar = exp.charAt(index);
            char nextChar = exp.charAt(index + 1);
            if (isOperator(currentChar) && isOperator(nextChar)) {
                return false;
            }
        }
        return true;
    }

    // Validate parentheses
    public static boolean isValidParenthisis(String exp) {
        Stack<Character> bracketStack = new Stack<>();
        for (char symbol : exp.toCharArray()) {
            if (symbol == '(') {
                bracketStack.push('(');
            } else if (symbol == ')') {
                if (!bracketStack.empty() && bracketStack.peek() == '(') {
                    bracketStack.pop();
                } else {
                    return false;
                }
            } else {
                if (symbol == '[' || symbol == '{' || symbol == ']' || symbol == '}') {
                    return false;
                }
            }
        }
        return bracketStack.empty();
    }

    // Convert infix expression to postfix
    private static String infixToPostfix(String expression) {
        StringBuilder postfixExpression = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        String cleanedExpression = expression.replaceAll("\\s+", "");
        StringBuilder currentNumber = new StringBuilder();

        for (int index = 0; index < cleanedExpression.length(); index++) {
            char currentChar = cleanedExpression.charAt(index);

            if (Character.isDigit(currentChar) || currentChar == '.') {
                currentNumber.append(currentChar);
            } else {
                if (currentNumber.length() > 0) {
                    postfixExpression.append(currentNumber).append(" ");
                    currentNumber = new StringBuilder();
                }

                if (currentChar == '(') {
                    operatorStack.push(currentChar);
                } else if (currentChar == ')') {
                    while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                        postfixExpression.append(operatorStack.pop()).append(" ");
                    }
                    if (!operatorStack.isEmpty()) {
                        operatorStack.pop(); // Remove '('
                    }
                } else if (isOperator(currentChar)) {
                    while (!operatorStack.isEmpty() && operatorStack.peek() != '(' &&
                            getPrecedence(operatorStack.peek()) >= getPrecedence(currentChar)) {
                        postfixExpression.append(operatorStack.pop()).append(" ");
                    }
                    operatorStack.push(currentChar);
                }
            }
        }

        if (currentNumber.length() > 0) {
            postfixExpression.append(currentNumber).append(" ");
        }

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() != '(') {
                postfixExpression.append(operatorStack.pop()).append(" ");
            } else {
                operatorStack.pop();
            }
        }

        return postfixExpression.toString().trim();
    }

    // Evaluate the postfix expression
    private static double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens) {
            if (isOperator(token.charAt(0))) {
                double b = stack.pop();
                double a = stack.pop();
                double result = applyOperator(a, b, token.charAt(0));
                stack.push(result);
            } else {
                stack.push(Double.parseDouble(token));
            }
        }
        return stack.pop();
    }

    // Check if the character is an operator
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Get the precedence of operators
    private static int getPrecedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return -1;
    }

    // Apply operator on operands
    private static double applyOperator(double a, double b, char operator) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            default: throw new UnsupportedOperationException("Invalid operator: " + operator);
        }
    }
}
