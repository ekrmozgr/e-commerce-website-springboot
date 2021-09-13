package com.example.springproject;

import com.example.springproject.ManagedBeans.LoginBean;
import com.sun.faces.config.ConfigureListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.faces.bean.ManagedProperty;
import javax.faces.webapp.FacesServlet;
import java.util.Collection;

@SpringBootApplication
@Configuration
@ComponentScan("com.example")
@EntityScan("com.example")
@EnableJpaRepositories("com.example")
public class SpringProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProjectApplication.class, args);
    }

    //JSF Configration Başlangıc
    @Bean
    public ServletRegistrationBean<FacesServlet> facesServletRegistraiton() {
        ServletRegistrationBean<FacesServlet> registration = new ServletRegistrationBean<FacesServlet>(new FacesServlet(),
                new String[] { "*.xhtml" });
        registration.setName("Faces Servlet");
        registration.setLoadOnStartup(1);

        return registration;
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());

            //Primefacesin ücretsiz temalarından bootstrap örneği yaptık değiştirebilirsiniz
            //servletContext.setInitParameter("primefaces.THEME", "bootstrap");
            //Primefaces client browser tarafında kontrol edilebilme örneğin textbox 10 karakter olmalı vs..
            servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", Boolean.TRUE.toString());

            servletContext.setInitParameter("javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL",Boolean.TRUE.toString());
            //Xhtml sayfalarında commentlerin parse edilmemesi.
            servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", Boolean.TRUE.toString());
            //primefaces icon set için
            //servletContext.setInitParameter("primefaces.FONT_AWESOME", Boolean.TRUE.toString());

            servletContext.setInitParameter("primefaces.UPLOADER","commons");

            servletContext.addFilter("PrimeFaces FileUpload Filter",org.primefaces.webapp.filter.FileUploadFilter.class).addMappingForServletNames(null,false,"Faces Servlet");


            String[] urlPatterns = {"/basket.xhtml","/","/create_user.xhtml","/edit_user.xhtml","/user_settings.xhtml",
                                    "/shopping_settings.xhtml","/create_category.xhtml","/create_product.xhtml",
                                    "/edit_category.xhtml","/edit_product.xhtml","/edit_user.xhtml",
                                    "/account.xhtml","/orders.xhtml","/order_details.xhtml"};
            servletContext.addFilter("AuthFilter",AuthorizationFilter.class).addMappingForUrlPatterns(null,false,
                    urlPatterns);

        };
    }


    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
        return new ServletListenerRegistrationBean<ConfigureListener>(new ConfigureListener());
    }
    //JSF Configration Sonu

}
