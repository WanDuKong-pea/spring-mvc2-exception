package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 스프링이 아닌 순수 서블릿 컨테이너가 예외를 처리하는 방법
 * 1. Exception(예외)
 * 2. response.sendError(HTTP 상태코드, 오류 메시지)
 */
@Slf4j
@Controller
public class ServletExController {

    /**
     * 1.Exception(예외)
     * tomcat이 기본으로 제공하는 오류 화면을 볼 수 있음
     * HTTP Status 500 - Internal Server Error
     */
    @GetMapping("/error-ex")
    public void errorEx(){
        throw new RuntimeException("예외 발생!");
    }

    /**
     * 2. response.sendError(HTTP 상태코드, 오류 메시지)
     * 서블릿 컨테이너가 응답 전에 response에 sendError()가 호출되었는지 확인
     * 호출되었다면 설정한 오류 코드에 맞추어 기본 오류 페이지를 보임
     */
    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException{
        response.sendError(404,"404오류!");
    }

    /**
     * 2. response.sendError(HTTP 상태코드, 오류 메시지)
     * 서블릿 컨테이너가 응답 전에 response에 sendError()가 호출되었는지 확인
     * 호출되었다면 설정한 오류 코드에 맞추어 기본 오류 페이지를 보임
     */
    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException{
        response.sendError(500);
    }

    //(서블릿 예외 처리 시작 Commit) 정리
    //서블릿 컨테이너가 제공하는 기본 예외 처리 화면은 사용자가 보기에 불편
    //의미 있는 오류 화면을 제공해보자
}
