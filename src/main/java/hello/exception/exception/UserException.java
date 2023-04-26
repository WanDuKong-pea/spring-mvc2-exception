package hello.exception.exception;

/**
 * [사용자 정의 예외]
 * ExceptionResolver를 사용하여
 * WAS까지 예외를 보내지 않기 위한 예시에 사용할 것
 */
public class UserException extends RuntimeException{

    public UserException(){
        super();
    }

    public UserException(String message){
        super(message);
    }

    public UserException(String message, Throwable cause){
        super(message,cause);
    }

    public UserException(Throwable cause){
        super(cause);
    }

    protected UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
