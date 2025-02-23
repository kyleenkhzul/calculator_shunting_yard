import java.util.Stack;

public class Calculator {

    
    // This function is a helper function to maintain order of operations. Multiplication/division have a higher precedence than addition/subtraction.
    // All other digits are treated normally.  
    public static int getPrecedence(char op) {
        switch (op) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            default: return 0;
        }
    }

    // This function takes a String expression and transforms it into a postfix expression. 
    public static String infixToPostfix(String expression) {
        StringBuilder output = new StringBuilder();  // string to output the digits and operators
        Stack<Character> stack = new Stack<>();   // stack to handle push/pop of operators
        StringBuilder numberBuffer = new StringBuilder();  // handles processing multidigit numbers

        for (char token : expression.toCharArray()) {
            if (Character.isDigit(token)) { // checks if its a digit
                numberBuffer.append(token); // add it to the numberBuffer
            } else {
                if (numberBuffer.length() > 0) {  // if it is a multidigit number
                    output.append(numberBuffer).append(" ");  // add it to the output
                    numberBuffer.setLength(0); // set the length of numberBuffer to 0
                }
                if (token == '(') {
                    stack.push(token); // pushes left parantheses
                } else if (token == ')') {  
                    while (!stack.isEmpty() && stack.peek() != '(') { // while the stack is not empty and the next is not a left
                        output.append(stack.pop()).append(" "); // pop it and add a space 
                    }
                    stack.pop(); // Pop the left parenthesis
                } else if (getPrecedence(token) > 0) { // Operator check
                    while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(token)) { // while the stack is not empty and the next operator has greater precendence than the current operator
                        output.append(stack.pop()).append(" ");  // add it to the output and pop it from the stack
                    }
                    stack.push(token); // add the operator to the stack
                }
            }
        }
        if (numberBuffer.length() > 0) {
            output.append(numberBuffer).append(" ");  // add any remaining numbers to the output
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" "); // add remaining operators
        }

        return output.toString().trim(); // return postfix expression 
    }

    // This function evaluates the Postfix expression 
    public static int evaluatePostfix(String postfix) {
        Stack<Integer> stack = new Stack<>(); //create a new stack of integers
        for (String token : postfix.split(" ")) {    // add a space in between each token
            if (token.matches("\\d+")) {  // if the token consists only of digits
                stack.push(Integer.parseInt(token));  // push it to the stack
            } else {
                if (stack.size() < 2) throw new IllegalArgumentException("Invalid expression"); // if stack size is 2 then expression is invalid
                int b = stack.pop();  // assigns the first element in the stack to B because of LIFO and pops it from the stack
                int a = stack.pop();  // assigns second element in the stack to A because of LIFO and pops it from the stack 
                switch (token.charAt(0)) {  // checks which operator
                    case '+': stack.push(a + b); break;  // conducts operations
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/': stack.push(a / b); break;
                }
            }
        }
        return stack.pop();  // returns final answer operation
    }

    // helper method to call static methods
    public int evaluate(String expression) {
        return evaluatePostfix(infixToPostfix(expression));  
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        // Prints out the number 7
        System.out.println(calculator.evaluate("2 + 5"));
    
        // Prints out the number 33
        System.out.println(calculator.evaluate("3 + 6 * 5"));
    
        // Prints out the number 20
        System.out.println(calculator.evaluate("4 * (2 + 3)"));
    
        // Prints out the number 2
        System.out.println(calculator.evaluate("(7 + 9) / 8"));
    }
}