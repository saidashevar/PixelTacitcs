package com.saidashevar.ptgame.exception.game;

@SuppressWarnings("serial")
public class TooManyCardsInHandException extends Exception {
    private String message;

    public TooManyCardsInHandException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
