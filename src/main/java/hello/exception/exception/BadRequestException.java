package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * [스프링 부트가 기본으로 제공하는 ExceptionResolver 종류]
 * 1. ExceptionHandlerExceptionResolver
 *     - @ExceptionHandler 을 처리.
 *     - API 예외 처리는 대부분 이 기능으로 해결
 *     - 조금 뒤에 자세히 설명한다.
 * 2. ResponseStatusExceptionResolver [이 클래스와 관련한 resolver ]
 *     - HTTP 상태 코드를 지정.
 *     - a. @ResponseStatus(code = HttpStatus.NOT_FOUND) 처리
 *     - b. ResponseStatusException 처리
 * 3. DefaultHandlerExceptionResolver
 *     - 스프링 내부 기본 예외를 처리
 */
//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException{
    //@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason="잘못된 요청 오류")
    //BadRequestException 예외가 컨트롤러 밖으로 넘어가면
    //ResponseStatusExceptionResolver 예외가 해당 애노테이션을 확인하여
    //code값과 reason(message)를 담음

    //@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
    //reason을 MessageSource에서 찾는 기능 제공함

    //ResponseStatusExceptionResolver 코드를 확인해보면
    //결국 response.sendError(statusCode,resolvedReason)을 호출하는 것을 확인 가능
    //sendError를 호출했기 때문에 WAS에서 다시 오류 페이지(/error)를 내부 요청하게 됨
}
