package gov.faa.notam.developerportal.configuration;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Email template processor configuration.
 */
@Configuration
@Setter
public class EmailTemplateConfiguration {
    /**
     * Path to the mail templates directory.
     */
    @Value("${mail.templates.path}")
    private String mailTemplatesPath;

    /**
     * The template resolver bean.
     *
     * @param applicationContext - application context.
     * @return the resolver.
     */
    @Bean
    public ITemplateResolver thymeleafTemplateResolver(ApplicationContext applicationContext) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix(mailTemplatesPath);
        resolver.setSuffix("");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    /**
     * The template engine bean.
     *
     * @param applicationContext - application context.
     * @return the engine.
     */
    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine(ApplicationContext applicationContext) {
        ITemplateResolver templateResolver = thymeleafTemplateResolver(applicationContext);
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
