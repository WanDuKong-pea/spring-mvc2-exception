package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * [@ExceptionHandler 예외 처리 방법]
 * @ExceptionHandler 애노테이션을 선언하고 해당 컨트롤러에서 처리하고 싶은 예외를 지정
 * 해당 컨틀롤러에서 예외가 발생하면 이 메서드가 호출됨
 * = (@ExceptionHandler 애노테이션은 해당 컨트롤러에서만 적용되며, 다른 컨트롤러에서 발생한 예외는 처리할 수 없음)
 * = 동일한 예외를 각 컨트롤러 (ex-회원, 상품) 마다 다르게 처리하고 싶은 때 사용하기 가능해짐
 * 참고로 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있음
 *
 * 다음과 같이 다양한 예외를 한번에 처리할 수도 있음
 * @ExceptionHandler({AException.class, BException.class})
 */
@Slf4j
@RestController
public class ApiExceptionV2Controller {
    /**
     * [@ExceptionHandler(IllegalArgumentException.class)]
     * IllegalArgumentException 또는 그 하위 자식 클래스 모두 처리 가능
     * - 자식예외 호출은 부모예외처리() , 자식예외처리() 둘다 대상.
     * - 그런데 둘 중 더 자세한 것이 우선권을 가지므로 자식예외처리() 가 호출
     * - 부모예외가 호출은 부모예외처리() 만 호출 대상이 되므로 부모예외처리() 가 호출
     *
     * [실행흐름]
     * 1. 컨트롤러를 호출한 결과 IllegalArgumentException 예외가 컨트롤러 밖으로 던져짐.
     * 2. 예외가 발생했으로 ExceptionResolver 가 작동한다. 가장 우선순위가 높은ExceptionHandlerExceptionResolver 가 실행됨.
     * 3. ExceptionHandlerExceptionResolver 는 해당 컨트롤러에 IllegalArgumentException 을 처리할 수 있는 @ExceptionHandler 가 있는지 확인.
     * 4. illegalExHandle() 를 실행. @RestController 이므로 illegalExHandle() 에도
     * @ResponseBody 가 적용. 따라서 HTTP 컨버터가 사용되고, 응답이 다음과 같은 JSON으로 반환됨.
     * 5. @ResponseStatus(HttpStatus.BAD_REQUEST) 를 지정했으므로 HTTP 상태 코드 400으로 응답함.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);

        //예외 처리 객체로 반환 시킴. JSON으로 반환됨
        return new ErrorResult("BAD", e.getMessage());
    }

    /**
     * @ExceptionHandler 에 예외를 생략할 수 있음.
     * 생략하면 메서드 파라미터의 예외가 지정됨
     *
     * ResponseEntity를 반환(HTTP 메시지 바디에 직접 응담_HTTP 컨버터 사용됨)하면
     * HTTP 응답 코드를 프로그래밍해서 동적으로 변경 가능,
     * 앞서 살펴본 @ResponseStatus 는 애노테이션이므로 HTTP 응답 코드를 동적으로 변경 불가.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception (최상위 사용 가능)
     * 따로 @Exception 설정 안한 Exception의 자식 예외들이 적용됨
     * -> 구체적일 수록 우선순위가 높기 때문
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }


    /* //ModelAndView를 사용해서 오류 화면 응답하는데 사용할 수도 있음
    @ExceptionHandler(ViewException.class)
    public ModelAndView ex(ViewException e) {
        log.info("exception e", e);
        return new ModelAndView("error");
    }*/

    /**
     * 예외를 발생 시킬 매핑
     * 위에 지정한 @ExceptionHandler 는 이 콘트롤러에서 발생한 오류에만 사용 가능
     */
    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello " + id);
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
