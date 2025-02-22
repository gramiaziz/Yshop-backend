package myshop.shop.Config;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import myshop.shop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import lombok.AllArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;
    @Bean
    public UserDetailsService userDetailsService(){
        return userService;
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies and credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> {
                    httpForm.loginProcessingUrl("/req/login")
                            .successHandler((request, response, authentication) -> {
                                System.out.println("Login successful");
                               /* response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("Login successful");*/
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"message\": \"Login successful\", \"username\": \"" + authentication.getName() + "\"}");
                                response.getWriter().flush();

                            })
                            .failureHandler((request, response, exception) -> {
                                System.out.println("Login failed: " + exception.getMessage());
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("Login failed: " + exception.getMessage());
                            });
                })
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/req/signup", "/req/login","/products/getall").permitAll();
                    registry.requestMatchers("/user/me","/user/username","/logout").authenticated();
                    registry.requestMatchers("/admin/**").hasRole("ADMIN");
                    registry.anyRequest().authenticated();
                })

                .logout(logout -> logout
                        .logoutUrl("/req/logout") // Custom logout endpoint
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\": \"Logout successful\"}");
                            response.getWriter().flush();
                            response.addCookie(new Cookie("JWT", null));
                        })
                        .invalidateHttpSession(true) // Invalidate session
                        .deleteCookies("JSESSIONID") // Clear cookies
                        .permitAll()
                )
                .build();
    }


}
