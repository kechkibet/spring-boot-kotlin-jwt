package com.example.demo.Security

import com.example.demo.Services.MyUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    lateinit var myUserDetailService: MyUserDetailService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val header = request.getHeader("Authorization")

        var username: String? = null
        var jwt: String? = null

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7)
            username = jwtUtil.extractUsername(jwt)
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null && jwt != null) {
            val userDetails = this.myUserDetailService.loadUserByUsername(username)
            //warning, make sure user is not disabled here...
            if (!userDetails.isEnabled)
                throw DisabledException("Account Disabled")
            if (jwtUtil.validateToken(jwt, userDetails)) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                )

                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }

}