package com.zebone.platform.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zebone.platform.modules.utils.CustomDataFormat;
import com.zebone.platform.modules.web.mvc.spring.view.FreeMarkerView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Optional;
import java.util.Properties;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    @Primary
    public ObjectMapper objectMapper (){
        return Jackson2ObjectMapperBuilder
                .json()
                .dateFormat(new CustomDataFormat("yyyyMMddHHmmss"))
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(ConfigurableEnvironment environment, FreeMarkerViewResolver fmvr){
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates");
        freeMarkerConfigurer.setDefaultEncoding("UTF-8");
        Optional<PropertySource<?>> source = environment.getPropertySources()
                .stream().filter(pt -> "freemarker.properties".equals(pt.getName())).findFirst();
        freeMarkerConfigurer.setFreemarkerSettings(source.isPresent()?(Properties)source.get().getSource():null);

        fmvr.setViewClass(FreeMarkerView.class);
        fmvr.setOrder(1);
        return freeMarkerConfigurer;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //设置默认返回头的Content-Type为json；默认已经添加了MediaType:xml、json，
        //使用默认转换器时，可以xxx?format=json或者xml来返回目标格式
        configurer.ignoreAcceptHeader(true)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .favorParameter(true);
    }

}
