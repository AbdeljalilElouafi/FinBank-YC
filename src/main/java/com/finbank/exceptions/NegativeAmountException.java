package main.java.com.finbank.exceptions;


public class NegativeAmountException extends Exception {
    public NegativeAmountException(String message) {
        super(message);
    }
}

