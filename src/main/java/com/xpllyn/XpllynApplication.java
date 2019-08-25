package com.xpllyn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.xpllyn.mapper")
public class XpllynApplication {

    public static void main(String[] args) {
        SpringApplication.run(XpllynApplication.class, args);
    }

}
