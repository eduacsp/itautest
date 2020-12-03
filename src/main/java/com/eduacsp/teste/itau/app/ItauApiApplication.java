package com.eduacsp.teste.itau.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.eduacsp.teste.itau.model"
})
@EnableJpaRepositories(basePackages = {
        "com.eduacsp.teste.itau.repository"
})
@ComponentScan(basePackages= {"com.eduacsp.teste.itau"})
public class ItauApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItauApiApplication.class, args);
    }


    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean
                = new FilterRegistrationBean<>( new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/movimentacao/*");
        filterRegistrationBean.setName("etagFilter");
        return filterRegistrationBean;
    }

}
