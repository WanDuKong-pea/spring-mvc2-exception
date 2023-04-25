package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * error 발생(ServletExController) 시 WebServerCustomizer에서 등록한 errorPage path와
 * 에러 처리 뷰를 연결하는 controller
 */
@Slf4j
@Controller
public class ErrorPageController {
    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = //예외
            "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = //예외 타입
            "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = //오류 메시지
            "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = //클라이언트 요청 URI
            "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = //오류 발생한 서블릿 이름
            "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = //HTTP 상태코드
            "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    /**
     * WAS는 오류페이지를 단순히 다시 요청만 하는 것이 아니라 오류 정보를
     * request의 attribute에 추가해서 넘겨줌
     * 필요시 오류 페이지에서 전달된 오류 정보 사용 가능
     * @param request
     */
    public void printErrorInfo(HttpServletRequest request){
        log.info("ERROR_EXCEPTION: ex=", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE)); //ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
        log.info("ERROR_REQUEST_URI: {}",request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));

        //[서블릿 예외처리_필터, 인터셉터: SpringMVC2_login에서 추가 커밋 확인하기]
        //DispatcherType: 필터나 인터셉터에서 검증 체크를 진행할 때 WAS가 예외처리 페이지 처리를 위해 재요청하는 요청이라도
        // 필터,인터셉터가 다시 중복으로 검증 체크를 진행하지 않도록 DispatcherType을 이용할 수 있음
        //request.getDispatcherType = ERROR: exception으로 인한 예외 처리 페이지 재요청 시
        //request.getDispatcherType = REQUEST: 정상 요청일 시
        log.info("dispatchType={}",request.getDispatcherType());
    }

}
