package com.example.demo.Security

import com.example.demo.Services.MyUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class SecurityConfigurer : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtFilter: JwtFilter

    @Autowired
    lateinit var myUserDetailService: MyUserDetailService



    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.run{
            userDetailsService(myUserDetailService)
        }
    }

    override fun configure(http: HttpSecurity?) {
        http?.run {
            csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/security/authenticate").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}