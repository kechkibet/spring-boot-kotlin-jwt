package com.example.demo.Security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap


@Component
class JwtUtil {

    private val secret = "secret"
    private val validity = 60

    fun generateToken(userDetails: UserDetails): String {
        return createToken(HashMap(), userDetails.username)
    }


    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + (60 * 60000)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact()
    }

    fun <T> extractClaim(token: String, claimsResolver: Claims.() -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }


    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    fun extractUsername(jwt: String): String {
        return extractClaim(jwt, Claims::getSubject)
    }

    fun extractExpiration(jwt: String): Date {
        return extractClaim(jwt, Claims::getExpiration)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun validateToken(jwt: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(jwt)
        return (username == userDetails.username && !isTokenExpired(jwt))
    }
}