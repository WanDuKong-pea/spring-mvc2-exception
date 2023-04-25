package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 서블릿 오류 페이지를 등록하는 방법
 * [전반적인 이번 강의 흐름]
 * ex) ServletExController(/error-500) 500 예외 발생-> errorPage500() -> ErrorPageController(/error-page/500) -> view(templates/error-page/500)
 */
@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    /**
     * 앞서 ServletExController 에서 설정한 예외들로 오류 페이지 처리 예시를 작성했음
     */
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        //response.sendError(404): errorPage404 호출
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND,"/error-page/404");
        //response.sendError(500): errorPage500 호출
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/error-page/500");
        //RuntimeException 또는 그 자식 타입의 예외: errorException 호출
        //500 예외가 서버 내부 발생 오류라는 뜻을 포함하고 있기 때문에 500 에러 페이지로 처리함
        ErrorPage errorException = new ErrorPage(RuntimeException.class,"/error-page/500");
        //에러 페이지 등록
        factory.addErrorPages(errorPage404,errorPage500,errorException);
    }
}
