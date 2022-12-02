package com.ns.bankingapp.security;

import com.ns.bankingapp.model.Role;
import com.ns.bankingapp.security.filter.CustomAuthenticationFilter;
import com.ns.bankingapp.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().antMatchers("/api/login/**", "/api/token/refresh/**").permitAll();
        http.authorizeHttpRequests().antMatchers("/api/transactions/{clientId}").hasAnyAuthority((Role.CLIENT.toString()), Role.TELLER.toString());
        http.authorizeHttpRequests().antMatchers("/api/cards/{clientId}").hasAnyAuthority(Role.CLIENT.toString(), Role.TELLER.toString());
        http.authorizeHttpRequests().antMatchers("/api/accounts/{clientId}").hasAnyAuthority(Role.CLIENT.toString(), Role.TELLER.toString());
        http.authorizeHttpRequests().antMatchers("/api/user/teller/**").hasAuthority(Role.ADMIN.toString());
        http.authorizeHttpRequests().antMatchers("/api/user/client/**").hasAuthority(Role.TELLER.toString());
        http.authorizeHttpRequests().antMatchers("/api/request/**").hasAuthority(Role.CLIENT.toString());
        http.authorizeHttpRequests().antMatchers("/api/approve/**").hasAuthority(Role.TELLER.toString());
        http.authorizeHttpRequests().antMatchers("/api/disapprove/**").hasAuthority(Role.TELLER.toString());
        http.authorizeHttpRequests().antMatchers("/api/transfer").hasAuthority(Role.CLIENT.toString());
        http.authorizeHttpRequests().antMatchers("/api/transactions/**").hasAuthority(Role.TELLER.toString());
        http.authorizeHttpRequests().antMatchers("/api/cards/**").hasAuthority(Role.TELLER.toString());
        http.authorizeHttpRequests().antMatchers("/api/accounts/**").hasAuthority(Role.TELLER.toString());


        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
