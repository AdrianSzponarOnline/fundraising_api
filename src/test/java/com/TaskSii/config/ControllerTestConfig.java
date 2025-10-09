package com.TaskSii.config;

import com.TaskSii.model.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@TestConfiguration
public class ControllerTestConfig implements WebMvcConfigurer {

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if ("testuser".equals(username)) {
                    User user = new User();
                    user.setUser_id(1L);
                    user.setEmail("test@test.com");
                    user.setPassword("password");
                    user.setEnabled(true);
                    
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(user.getEmail())
                            .password(user.getPassword())
                            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_OWNER")))
                            .build();
                }
                throw new UsernameNotFoundException("User not found");
            }
        };
    }

    @Bean
    @Primary
    public TestAuthenticationPrincipalResolver testAuthenticationPrincipalResolver() {
        return new TestAuthenticationPrincipalResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(testAuthenticationPrincipalResolver());
    }
}
