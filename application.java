package org.example;

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
}
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

