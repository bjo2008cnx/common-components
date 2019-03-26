package com.github.common.components.auth.client;

import com.github.common.components.web.filter.RepeatableReadFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 示例Filter 配置
 * RereadFilter需要配置在AuthFilter之前
 */
@Configuration
public class FilterConfigExample {

    public static final String API = "/api/*";

    @Bean
    public Filter rereadFilter() {
        return new RepeatableReadFilter();
    }

    @Bean
    public Filter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean1() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(rereadFilter());
        filterRegistrationBean.addUrlPatterns(API);
        filterRegistrationBean.setOrder(10);//order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean2() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(authFilter());
        filterRegistrationBean.addUrlPatterns(API);
        filterRegistrationBean.setOrder(20);
        return filterRegistrationBean;
    }
}