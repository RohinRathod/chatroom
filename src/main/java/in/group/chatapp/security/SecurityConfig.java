package in.group.chatapp.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import in.group.chatapp.service.UserService;

import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	public UserService userService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(authz -> authz
	            .requestMatchers("/register", "/login", "/js/**", "/css/**", "/images/**").permitAll()
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .requestMatchers("/user/**").hasRole("USER")
	            .anyRequest().authenticated())
	        .formLogin(login -> login
	            .loginPage("/login")
	            .defaultSuccessUrl("/chat", true)
	            .failureUrl("/login?error=true")
	            .permitAll())
	        .logout(logout -> logout
	            .logoutSuccessUrl("/login?logout=true")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	            .permitAll());

	    return http.build();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return username -> {
			Optional<in.group.chatapp.Entity.User> user = userService.findByUsernameObj(username);
			if (user.isEmpty()) {
				throw new UsernameNotFoundException("User not found");
			}
			return new User(user.get().getUsername(), user.get().getPassword(),
					AuthorityUtils.createAuthorityList(user.get().getRole()));
		};
	}

}