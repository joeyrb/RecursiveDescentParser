/**
*	Authors:	Joey Brown
*				Collin Chick
*	Assignment:	PA#3
*	Date:		11/6/2017
*	Description:
*		Main class that tests whether or not the input is a valid expression. Accepts the tag -t 
*		to output tokens.
**/
// Import Java Scanner class to read command line input
import java.util.Scanner;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser 
{
	public static void main(String[] Args)
	{	
		boolean tokenize = false;
		if(Args.length == 1 && Args[0].equals("-t")){
				tokenize = true;
		}
		evalLoop(tokenize);
	}

	// Returns true if input is not empty
	public static boolean Continue(String s) {
		return !(s.isEmpty());
	}

	// Loops through evaluating expressions from user input until input is empty
	public static void evalLoop(boolean tknze){
		// scanner object to read command-line input
		Scanner scanner = new Scanner(System.in);
		String s;	// input from user stored as string

		do {
			// prompt for user input
			System.out.print("\nEnter expression: ");

			// use scanner to get input as a string
			s = scanner.nextLine();

			TokenParser parser = new TokenParser(s);

			boolean parseResult = parser.Parse();
			if (!Continue(s)) {
				System.out.println("(end of input)");
				break;
			}
			else if (parseResult)
				System.out.printf("\"%s\" is a valid expression\n", s);
			else
				System.out.printf("\"%s\" is a not valid expression\n", s);
			if (tknze) {
				Tokenize(s);
			}
		} while(Continue(s));
	}

	// Tokenizes the Strings passed in and prints them in a comma separated list
	public static void Tokenize(String s){
		// I have no clue how to ignore/remove just one " " character...
		StringTokenizer st = new StringTokenizer(s,"(\\W*[^ ]*)(\\S+\\b)(\\S+[/*()+-]?)(\\W[^ ]*)", true);
		System.out.printf("tokens: ");
		while(st.hasMoreTokens()) {
			System.out.printf(st.nextToken());
			if(st.hasMoreTokens())
				System.out.printf(",");
		}
		System.out.println();
	}
}