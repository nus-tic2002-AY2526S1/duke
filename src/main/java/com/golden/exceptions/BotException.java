package com.golden.exceptions;

public class BotException extends Exception{
    private final ErrorCode code;

    public BotException(ErrorCode code){
        super((String) null);
        this.code = code;
    }

    public BotException(ErrorCode code, String detail){
        super(detail.trim());
        this.code = code;
    }

    public ErrorCode code(){
        return code;
    }

    public String getErrorMessage() {
        String base = code.message();
        String detail = super.getMessage();
        if (detail == null || detail.isBlank()) {
            return base;
        }
        // Add a separator only if the base message doesn't already end suitably
        char last = base.charAt(base.length() - 1);
        boolean isEndWithPunct = last == '.' || last == '!' || last == '?';
        return isEndWithPunct ? (base + " " + detail) : (base + ": " + detail);
    }

    @Override
    public String toString() {
        // Useful for logs
        // can be called by: System.out.println(e)
        return "["+ code + "] " + getErrorMessage();
    }

}

