package hello.exception;

import hello.exception.resolver.MyHandlerExceptionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * HandlerExceptionResolver
 * -> WebMvcConfigurer(extendHandlerExceptionResolvers(...))
 * -> resolver.add(new 만든 ExceptionResolver())로 등록
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /* //이 메서드를 사용하면 스프링이 기본으로 등록하는 ExceptionResolver가 제거됨 주의!!
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    }*/

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
    }
}
