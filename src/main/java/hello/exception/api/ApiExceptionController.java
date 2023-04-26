package hello.exception.api;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    /*
    "Postman"으로 테스트
    HTTP "Header"에 "Accept"가 "application/json"인 것을 꼭 확인

    [테스트 결과]
    정상: API로 JSON 형식으로 데이터가 정상 반환.
    오류 발생: 우리가 미리 만들어둔 오류 페이지 HTML이 반환.
    기대하는 바: 클라이언트는 정상 요청이든, 오류 요청이든 JSON이 반환되기를 기대.
               웹 브라우저가 아닌 이상 HTML을 직접 받아서 할 수 있는 것은 별로 없음.

    [문제 해결]
    오류 페이지 컨트롤러도 JSON 응답을 할 수 있도록 수정-> ErrorPageController API 응답 추가.
    */
}

