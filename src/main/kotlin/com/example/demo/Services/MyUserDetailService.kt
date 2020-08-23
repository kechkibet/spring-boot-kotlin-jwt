package com.example.demo.Services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDetailService: UserDetailsService {





    override fun loadUserByUsername(username: String): UserDetails {
        val userMap = mapOf<String, User>(
                "kech" to User(
                        "kech",
                        "kibet",
                        false,
                        true,
                        true,
                        true,
                        listOf(SimpleGrantedAuthority("USER"))
                ),
                "faith" to User(
                        "faith",
                        "1234",
                        false,
                        true,
                        true,
                        true,
                        listOf(SimpleGrantedAuthority("USER"))
                )
        )


        return userMap[username] ?: error("No User Found")
    }

}