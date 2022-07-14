package bg.softuni.fitcom.config;

import bg.softuni.fitcom.web.interceptors.ViewsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ViewsInterceptor viewsInterceptor;

    private final LocaleChangeInterceptor localeChangeInterceptor;

    public WebConfig(ViewsInterceptor requestProcessingTimeInterceptor,
                     LocaleChangeInterceptor localeChangeInterceptor) {
        this.viewsInterceptor = requestProcessingTimeInterceptor;
        this.localeChangeInterceptor = localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(viewsInterceptor);
        registry.addInterceptor(localeChangeInterceptor);
    }
}
