package com.list.nevrytime.config;

import com.list.nevrytime.security.jwt.AuthenticationFilter;
import com.list.nevrytime.security.jwt.JwtAccessDeniedHandler;
import com.list.nevrytime.security.jwt.JwtAuthenticationEntryPoint;
import com.list.nevrytime.security.jwt.JwtAuthenticationFilter;
import com.list.nevrytime.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtService jwtService;
    private AuthenticationFilter authenticationFilter;
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          AuthenticationFilter authenticationFilter,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler,
                          JwtService jwtService) {

        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.authenticationFilter = authenticationFilter;
        this.jwtService = jwtService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }

//    // 이미지를 출력할 때는 spring 에게 해당 경로를 허락해줘야 함
//    @Configuration
//    public class WebMvcConfig implements WebMvcConfigurer {
//
//        // todo: 허락해줄 경로를 설정해줌. 앞에 file:/// 를 붙여줌
//        String PermittedPath = "file:///" + System.getProperty("user.dir") + "/src/main/resources/static/";
//
//        @Override
//        public void addResourceHandlers(ResourceHandlerRegistry registry) {
//            registry.addResourceHandler("/**") // PermittedPath 경로들/+a
//                    .addResourceLocations(PermittedPath); // 로컬에 저장된 파일을 읽어 올 root 경로 설정
//        }
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationFilter, JwtAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/", "/**").permitAll()
                .antMatchers("**exception**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .disable()
                .headers()
                .frameOptions()
                .disable();

        return http.build();
    }
}
