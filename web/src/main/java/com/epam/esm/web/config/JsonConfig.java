package com.epam.esm.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.epam.esm.web.controller")
@EnableWebMvc
public class JsonConfig implements WebMvcConfigurer {

    //	@Bean
//	public Jackson2ObjectMapperBuilder jacksonBuilder() {
//		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//		builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//		return builder;
//	}
    @Bean
    public MappingJackson2HttpMessageConverter jsonMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }
}