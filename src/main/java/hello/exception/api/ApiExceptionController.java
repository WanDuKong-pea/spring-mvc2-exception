package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * 단순히 회원을 조회하는 기능을 가진 예외 발생 RestController.
 * 예외 테스트를 위해 URL에 전달된 id의 값이 ex이면 예외가 발생하도록 코드를 심어둠
 */
@Slf4j
@RestController
public class ApiExceptionController {
    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        //HandlerExceptionResolver 강의에서 사용할 오류 값
        //PostMan 실행 시 500 에러 처리됨
        //기대하는 것: WAS까지 예외가 넘어가더라도 500 에러 코드(default)가 아닌
                    //예외에 따라 다른 상태코드로 처리
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        //HandlerExceptionResolver 강의에서 사용할 예외 발생 값
        //기대하는 것: WAS까지 예외가 넘어가서 처리되지 않고,
                    //ExceptionResolver에서 예외처리가 끝나는 것.
        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }


    @Data
    @AllArgsConstructor //Lombok 애노테이션, 클래스의 모든 필드를 인자로 받는 생성자 자동 생성
    static class MemberDto {
        private String memberId;
        private String name;
    }

    //스프링 부트 제공 ExceptionResolver 강의에서 사용할 예외 발생 값
    //기대하는 것: 스프링이 제공하는 "ResponseStatusException"이 동작하는 것
    //실행: http://localhost:8080/api/response-status-ex1?message=
                        //message= (properties에서 on_param으로 설정 했기 때문에 작성)
    //WAS까지 넘어갔기 떄문에 BasicErrorController에서 제공하는 json과 /error페이지 반환
    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1(){
        throw new BadRequestException();
    }

    //스프링 부트 제공 ExceptionResolver 강의에서 사용할 예외 발생 값
    //@ResponseStatus 는 개발자가 직접 변경할 수 없는 예외에는 적용할 수 없음
    //추가로 애노테이션을 사용하기 때문에 조건에 따라 동적으로 변경하는 것도 어려움.
    //이때는 ResponseStatusException 예외를 사용
    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2(){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad",
                new IllegalArgumentException());
    }

    //스프링 부트 제공 ExceptionResolver 강의에서 사용할 예외 발생 값
    //DefaultHandlerExceptionResolver : 스프링 내부 발생 스프링 예외 해결
    //sendError로 문제를 해결 WAS까지 예외를 보냄 -> BasicExceptionController 제공 오류 페이지, json
    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        //대표적인 내부 발생 스프링 예외 -> TypeMismatchException
        //DefaultHandlerExceptionResolver는
        //TypeMismatchException이 발생했을 때 500에러가 나지 않고 400에러로 변경함

        //http://localhost:8080/api/default-handler-ex?data=hello&message=
        //Integer data 에 문자를 입력하면 내부에서 TypeMismatchException 이 발생
        return "ok";
    }

}

