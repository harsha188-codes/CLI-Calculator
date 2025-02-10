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
