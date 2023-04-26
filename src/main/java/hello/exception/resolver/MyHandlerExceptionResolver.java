package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * [HandlerExceptionResolver]
 * 스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고,
 * 동작을 새로 정의할 수 있는 방법을 제공.
 * 컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면
 * HandlerExceptionResolver 를 사용하면 됨. 줄여서 ExceptionResolver.
 *
 * [ExceptioinResolver 활용]
 * 예외 상태 코드 변환 -> response.sendError(xxx) -> 스프링 부트 기본 설정 /templates/error/xxx.html 호출됨
 * 뷰 템플릿 처리 -> ModelAndView에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 제공
 * API 응답 처리 -> response.getWriter().print("exception")처럼 바디에 직접 데이터 넣기 가능(JSON도 가능)
 *
 * [ExceptionResolver vs 스프링 제공 Exception]
 * 직접 ExceptionResolver를 구현하는 것은 상당히 복잡
 * 이 후 강의에서 스프링이 제공하는 ExceptionResolver 들을 알아봄
 */
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        //handler: 핸들러(컨트롤러) 정보, Exception ex: 핸들러(컨트롤러)에서 발생한 발생한 예외


        try{

            //WAS까지 sendError됨
            //스프링 부트 BasicErrorController 제공 오류 페이지와 application/json 값을 받게 됨
            if(ex instanceof IllegalArgumentException){
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,ex.getMessage());

                //ExceptionResolver가 ModelAndView를 반환하는 이유
                //마치 try, catch를 하듯, Exception을 처리해서 정상 흐름처럼 변경하는 것이 목적

                //ExceptionResolver 적용 전 : preHandle > 헨들러 > postHandle(X) > afterCompletion
                //ExceptionResolver 적용 후 : preHandle > 헨들러 > ExceptionResolver(상태코드 변환+mv반환) -> afterCompletion

                //HandlerExceptionResolver 반환 값에 따른 DispatcherServlet 동작 방식
                //1. new ModelAndView(빈 mv): 뷰를 렌더링 하지 않고 정상 흐름으로 서블릿 리턴
                //2. ModelAndView 지정: 뷰를 렌더링
                //3. null: 다음 ExceptionResolver를 찾아 실행, 없으면 기존 발생 예외를 서블릿 밖으로 던짐
                return new ModelAndView();
            }

             //Exception을 HandlerExceptionResolver 내에서 모두 처리
             //WAS로 넘기지 않음 (WAS 입장에서 정상 호출)
             //BasicErrorController를 이용하지 않음
            if(ex instanceof UserException){
                log.info("UserException resolver to 400");

                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if("application/json".equals(acceptHeader)){
                    //JSON에 담을 값 Map으로 처리
                    Map<String,Object> errorResult = new HashMap<>();
                    errorResult.put("ex",ex.getClass());
                    errorResult.put("message",ex.getMessage());

                    //JSON으로 변환시킬 Map을 String으로 변환
                    String result= objectMapper.writeValueAsString(errorResult);

                    //응답 헤더 설정
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");

                    //response body에 JSON으로 변환시킬 String 값 담은
                    response.getWriter().write(result);

                    return new ModelAndView();
                }else{

                    //TEXT/HTML
                    return new ModelAndView("error/4xx");
                }
            }
        }catch (IOException e){
            log.error("resolver ex",e);
        }
        return null;
    }
}
