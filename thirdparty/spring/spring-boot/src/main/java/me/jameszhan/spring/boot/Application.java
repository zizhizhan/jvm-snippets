package me.jameszhan.spring.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

@SpringBootApplication
@ComponentScan("me.jameszhan.spring")
public class Application {

    public static void main(String[] args)  {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            System.out.println("JvmName: " + jvmName);
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

            System.out.println("List dependency jars:");
            Enumeration<URL> urls = Application.class.getClassLoader().getResources("META-INF/");
            while (urls.hasMoreElements()) {
                System.out.println(urls.nextElement());
            }

            System.out.println("MainClass: " + getMainApplicationClass());
        };
    }

    private Class<?> getMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                System.out.println();
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
