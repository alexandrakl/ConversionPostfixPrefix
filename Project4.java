import java.util.Stack;
import java.util.Scanner;

public class Project4 {

	/**
	 * Create constructor for printing
	 */
	public Project4() {
		Scanner sc = new Scanner(System.in);
		String input = "";

		// make a loop and exit if user presses "ENTER"
		while (true) {
			System.out.println("Please enter your Infix expression: ");

			input = sc.nextLine();
			System.out.println("");

			// if no input entered exit loop
			if (input.length() == 0) {
				System.exit(0);
			}
			
			if (checkInput(input)) {
				System.out.println("The input is wrong.");
				System.out.println("Enter the correct expression.");
			} else {
				System.out.println(convertPostfix(input));
				System.out.println(convertPrefix(input));
			}

		

		}

	}

	/**
	 * call constructor in main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {

		// instantiate the constructor
		Project4 p4 = new Project4();
	}

	/**
	 * Checks for wrong input.
	 * @param input
	 * @return
	 */
	public boolean checkInput(String input) {
		int parenthesisCounter = 0;
		int operandCounter = 0;
		int operatorCounter = 0;

		char inputArray[] = input.toCharArray();
		for (int i = 0; i < inputArray.length; ++i) {
			char c = input.charAt(i);

			if (c == ')') {
				parenthesisCounter++;
			} else if (c == '(') {
				parenthesisCounter--;
			}

			if (isOperatorWithoutParanthesis(c)) {
				operatorCounter++;
			} else if (isOperand(c)) {
				operandCounter++;
			}
		}

		if (parenthesisCounter != 0 || operatorCounter >= operandCounter) {
			return true;
		}

		return false;
	}

	/**
	 * Converts infix equation to a postfix equation
	 * 
	 * @param input
	 * @return
	 */
	public String convertPostfix(String input) {

		// instantiate postfix Stack
		Stack<Character> postfix = new Stack<Character>();

		// initialize return String
		String resultPostfix = "";

		// Create array to store the input string
		char inputArray[] = input.toCharArray();

		// loop through array
		for (int i = 0; i < inputArray.length; ++i) {

			char c = input.charAt(i);

			if (isOperand(c)) {
				resultPostfix += c;
			}

			// if left parenthesis always push
			else if (c == '(') {
				postfix.push(c);
			}

			// if closing parenthesis pop
			else if (c == ')' && postfix.peek() != '(') {

				while (!postfix.isEmpty() && postfix.peek() != '(') {
					resultPostfix += postfix.pop();
				}
				postfix.pop();
			}

			else {

				while (true) {

					// if higher precedence then previous element push
					if (!postfix.isEmpty() && (isHigherPrecedence(postfix, c))) {
						postfix.push(c);
						break;
					}

					// if lower precedence then pop
					else if (!postfix.isEmpty() && (isLowerPrecedence(postfix, c))) {
						resultPostfix += postfix.pop();
					}

					// if same precedence
					else if (!postfix.isEmpty() && (isEqualPrecedence(postfix, c))) {
						resultPostfix += postfix.pop();
					} else {
						postfix.push(c);
						// result+=c;
						break;
					}

				}
			}
		}

		// if elements left in stack pop and store in result String
		while (!postfix.isEmpty()) {
			resultPostfix += postfix.pop();
		}

		return resultPostfix;
	}

	/**
	 * Convert infix equation to Prefix equation
	 * 
	 * @param input
	 * @return
	 */
	public String convertPrefix(String input) {

		String resultPrefix = "";
		char op = (char) 0;
		String rightOperand = "";
		String leftOperand = "";

		Stack<String> operands = new Stack<String>();
		Stack<Character> operators = new Stack<Character>();

		char inputArray[] = input.toCharArray();

		// loop through array
		for (int i = 0; i < inputArray.length; ++i) {

			char c = input.charAt(i);

			// push operands to operand stack
			if (isOperand(c)) {
				operands.push(String.valueOf(c));

			} else {

				// if left parenthesis always push
				if (c == '(') {
					operators.push(c);
				}

				// if closing parenthesis pop until opening parenthesis
				else if (c == ')' && operators.peek() != '(') {
					while (operators.peek() != '(') {
						op = operators.pop();
						rightOperand = operands.pop();
						leftOperand = operands.pop();
						operands.push(op + leftOperand + rightOperand);
					}
					operators.pop();
				}

				// if higher precedence then previous element push
				else if ((!operators.isEmpty()) && (isHigherPrecedence(operators, c))) {
					operators.push(c);
				}

				// if same or lower precedence
				else if ((!operators.isEmpty())
						&& (isEqualPrecedence(operators, c) || isLowerPrecedence(operators, c))) {
					operators.push(c);
					op = operators.pop();
					rightOperand = operands.pop();
					leftOperand = operands.pop();
					operands.push(op + leftOperand + rightOperand);

				} else {
					operators.push(c);
				}
			}
		}

		while (!operators.isEmpty()) {

			String r = "";
			String l = "";
			String s = String.valueOf(operators.pop());
			r = operands.pop();
			l = operands.pop();

			s += l;
			s += r;
			operands.push(s);
		}

		// loop through operand stack
		for (String oper : operands) {
			resultPrefix += oper;
		}
		return resultPrefix;
	}

	/**
	 * all lower letters
	 * 
	 * @param c
	 * @return
	 */
	public boolean isOperand(char c) {
		return ((c >= 97 && c <= 122) || (c >= 48 && c <= 57) || (c >= 65 && c <= 90));
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean isOperator(char c) {
		if (c == ')' || c == '+' || c == '-' || c == '*' || c == '/' || c == '(') {
			return true;
		}
		return false;
	}
	
	public boolean isOperatorWithoutParanthesis(char c) {
		if (c == '+' || c == '-' || c == '*' || c == '/') {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param postfix
	 * @return
	 */
	public boolean isPreviousOperator(Stack<Character> postfix) {
		if (postfix.peek() == '(' || postfix.peek() == '+' || postfix.peek() == '-' || postfix.peek() == '*'
				|| postfix.peek() == '/' || postfix.peek() == '%') {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param postfix
	 * @param c
	 * @return
	 */
	public boolean isLowerPrecedence(Stack<Character> postfix, char c) {
		return (c == '+' || c == '-') && (postfix.peek() == '*' || postfix.peek() == '/' || postfix.peek() == '%');
	}

	/**
	 * 
	 * @param postfix
	 * @param c
	 * @return
	 */
	public boolean isEqualPrecedence(Stack<Character> postfix, char c) {
		return (c == postfix.peek() || (c == '+' && postfix.peek() == '-') || ((c == '-' && postfix.peek() == '+'))
				|| ((c == '*' || c == '/') && postfix.peek() == '%')
				|| (c == '%' && (postfix.peek() == '*' || postfix.peek() == '/')));
	}

	/**
	 * 
	 * @param postfix
	 * @param c
	 * @return
	 */
	public boolean isHigherPrecedence(Stack<Character> postfix, char c) {
		return (c == '*' || c == '/' || c == '%') && (postfix.peek() == '+' || postfix.peek() == '-');
	}

}
