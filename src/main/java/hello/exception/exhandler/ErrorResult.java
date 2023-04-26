package hello.exception.exhandler;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 예외가 발생했을 때 API 응답으로 사용할 객체 정의
 * @ExceptionHandler에서 사용할 것.
 */
@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}