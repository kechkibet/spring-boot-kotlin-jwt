package com.example.demo.Security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDetailService: UserDetailsService {





    override fun loadUserByUsername(username: String?): UserDetails {
        return User("kech","kibet", listOf(SimpleGrantedAuthority("USER")))
    }

}