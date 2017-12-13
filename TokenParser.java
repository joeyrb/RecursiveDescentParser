/**
*	Authors:	Joey Brown
*				Collin Chick
*	Assignment:	PA#3
*	Date:		11/6/2017
*	Description:
*		Class file that implements a recursive decent parser to check for valid expressions.
*
**/
public class TokenParser {
    private String source;
    private String[] tokens = {};
    private int currentChar = 0;

    /**
     * The constructor for a TokenParser object.
     */
    TokenParser(String source) {
        this.source = source;
    }

    public boolean Parse() {
        // This is just test code, this function will be changed/removed.
        return Expr();
    }

    /**
     * Determines whether the next group of tokens is a valid Expression nonterminal. An Expression nonterminal
     * is defined by the following EBNF statement:
     *      <term> { <addop> <term> }
     *
     * @return True if the next group of tokens is a valid Expression nonterminal, false otherwise.
     */
    private boolean Expr() {
        // Check if reading is still in bounds
        try {
            consumeWhitespace();
        }
        catch (EndOfSourceException e) {
            return false;
        }

        // Check if valid Expression
        try {
            if (!Term())
                return false;
            consumeWhitespace();
            
            // either term and eol or term addop term ...
            while(AddOp()) {
                consume();
                consumeWhitespace();

                if (!Term()) {
                    return false;
                }

                consumeWhitespace();
            }

            return true;
        }
        catch (EndOfSourceException e) {
            return true;
        }
    }

   /**
     * Determines whether the next group of tokens is a valid Term nonterminal. A Term nonterminal
     * is defined by the following EBNF statement:
     *      <factor> { <mulop> <factor> }
     *
     * @return True if the next group of tokens is a valid Term nonterminal, false otherwise.
     */
    private boolean Term() {
        // Check if reading is still in bounds
        try {
            consumeWhitespace();
        }
        catch (EndOfSourceException e) {
            return false;
        }

        try {
            if (!Factor()) {
                return false;
            }

            consumeWhitespace();

            while(MulOp()) {
                consume();
                consumeWhitespace();

                if (!Factor()) {
                    return false;
                }

                consumeWhitespace();
            }

            return true;
        }
        catch (EndOfSourceException e) {
            return true;
        }
    }

    /**
     * Determines whether the next group of tokens is a valid Term nonterminal. A Term nonterminal
     * is defined by the following EBNF statement:
     *      <integer> | <float> | <id> | '(' <expr> ')' | [-] <factor>
     *
     * @return True if the next group of tokens is a valid Term nonterminal, false otherwise.
     */
    private boolean Factor() throws EndOfSourceException {
        try {
            int curr = currentChar;
        	if (Integer()) {
        		return true;
            }
            else
                currentChar = curr;
        	
            if (Float()) {
        		return true;
            }
            else 
                currentChar = curr;

        	if (Id()) {
        		return true;
            }
            else 
                currentChar = curr;

        	if (peek() == '('){
                consume();
        		if (Expr()) {
                    if( peek() == ')') {
                        consume();
                        return true;
                    }
                    else
                        return false;
        		}
        		else{
        			return false;
                }
        	}
        	else if (peek() == '-') {
                consume();
                if(Factor())
                    return true;
                else
                    return false;
        	}
        	else 
        		return false;
        }
        catch (EndOfSourceException e) {
            throw new EndOfSourceException();
        }
    }

    /**
     * This function determines whether the next group of tokens is a valid Integer nonterminal. An Integer nonterminal
     * is defined by the following EBNF statement:
     *      <digit> { <digit> }
     *
     * From the other statements in the EBNF grammar, an integer can be followed by either a '.' (in the case of a Float
     * nonterminal) or a whitespace character.
     *
     * @return True if the next group of tokens is a valid Integer nonterminal, false otherwise.
     */
    private boolean Integer() {
        try {
            consumeWhitespace();
        }
        catch (EndOfSourceException e) {
            return false;
        }

        try {
            while (true) {
                if (Digit())
                    consume();
                else {
                    char nextChar = peek();
                    return Character.isWhitespace(nextChar) || nextChar == '.';
                }
            }
        }
        catch (EndOfSourceException e) {
            return true;
        }
    }

    /**
     * This function determines whether the next group of tokens is a valid Float nonterminal.  A Float nonterminal is
     * defined by the following EBNF statement:
     *      <integer> . <integer>
     *
     * @return True if the next group of tokens is a valid Float nonterminal, false otherwise.
     */
    private boolean Float() {
        try {
            consumeWhitespace();
        }
        catch (EndOfSourceException e) {
            return false;
        }

        try {
            if (!Integer())
                return false;

            if (peek() == '.')
                consume();
            else
                return false;

            if (!Integer())
                return false;
        }
        catch (EndOfSourceException e) {
            return false;
        }

        return true;
    }

    /**
     * This function determines whether the next group of tokens is a valid Id nonterminal. An Id nonterminal is
     * defined by the following EBNF statement:
     *      <letter> { <letter> | <digit> }
     *
     * @return True if the next group of tokens is a valid Id nonterminal, false otherwise.
     */
    private boolean Id() {
        try {
            consumeWhitespace();
        }
        catch (EndOfSourceException e) {
            return false;
        }

        try {
            if (Letter())
                consume();
            else
                return false;

            while (true) {
                if (!Letter() && !Digit()) {
                    return true;
                }
                consume();
            }
        }
        catch (EndOfSourceException e) {
            return true;
        }
    }

    /**
     * This function determines whether a character is a Letter terminal. A Letter is either an uppercase or lowercase
     * letter, or the underscore ('_') character.
     *
     * @throws EndOfSourceException if the currentChar index is not within the source string.
     * @return True if the character is a Letter, false otherwise.
     */
    private boolean Letter() throws EndOfSourceException {
        try {
            return source.charAt(currentChar) >= 'a' && source.charAt(currentChar) <= 'z' ||
                   source.charAt(currentChar) >= 'A' && source.charAt(currentChar) <= 'Z' ||
                   source.charAt(currentChar) == '_';
        }
        catch (IndexOutOfBoundsException e) {
            throw new EndOfSourceException();
        }
    }

    /**
     * This function determines whether or not a character is a digit (0-9).
     *
     * @throws EndOfSourceException if the currentChar index is not within the source string.
     * @return True if the character is a digit, false otherwise.
     */
    private boolean Digit() throws EndOfSourceException {
        try {
            return Character.isDigit(source.charAt(currentChar));
        }
        catch (IndexOutOfBoundsException e) {
            throw new EndOfSourceException();
        }
    }

    /**
     * This function determines whether or not an operator of the same precedence as an addition operator is valid. The
     * two operators supported in this language are + and -.
     *
     * @throws EndOfSourceException if the currentChar index is not within the source string.
     * @return True if the character is a valid AddOp terminal, false otherwise.
     */
    private boolean AddOp() throws EndOfSourceException {
        try {
            return source.charAt(currentChar) == '+' || source.charAt(currentChar) == '-';
        }
        catch (IndexOutOfBoundsException e) {
            throw new EndOfSourceException();
        }
    }

    /**
     * This function determines whether or not an operator of the same precedence as a multiplication operator is valid.
     * The three operators supported in this language are *, /, and %.
     *
     * @throws EndOfSourceException if the currentChar index is not within the source string.
     * @return True if the operator is valid, false otherwise.
     */
    private boolean MulOp() throws EndOfSourceException {
        try {
            return source.charAt(currentChar) == '*' || source.charAt(currentChar) == '/' ||
                   source.charAt(currentChar) == '%';
        }
        catch (IndexOutOfBoundsException e) {
            throw new EndOfSourceException();
        }
    }

    /**
     * This function consumes characters in the source string until it hits a non-whitespace character. (consumes spaces,
     * tabs, newline characters, etc.)
     *
     * @throws EndOfSourceException if the end of the source string is reached.
     * @return The next character in the source string.
     */
    private void consumeWhitespace() throws EndOfSourceException {
        char nextChar;

        nextChar = peek();
        while (Character.isWhitespace(nextChar) &&  currentChar < source.length()) {
            consume();
            nextChar = peek();
        }
    }

    /**
     * This function returns the next character in the source string, incrementing the "currentChar" index.
     *
     * @throws EndOfSourceException if the end of the source string is reached.
     * @return The next character in the source string.
     */
    private char consume() throws EndOfSourceException {
        if (currentChar >= source.length())
            throw new EndOfSourceException();

        currentChar++;
        return source.charAt(currentChar - 1);
    }

    /**
     * This function returns the next character in the source string without incrementing the "currentChar" index.
     *
     * @throws EndOfSourceException if the end of the source string is reached.
     * @return The next character in the source string.
     */
    private char peek() throws EndOfSourceException {
        if (currentChar >= source.length())
            throw new EndOfSourceException();

        return source.charAt(currentChar);
    }
}

class EndOfSourceException extends Exception {
    public EndOfSourceException() {
        super();
    }
}