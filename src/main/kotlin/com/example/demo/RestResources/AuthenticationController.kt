package com.example.demo.RestResources

import com.example.demo.Security.JwtUtil
import com.example.demo.Services.MyUserDetailService
import com.example.demo.Security.models.AuthenticationRequest
import com.example.demo.Security.models.AuthenticationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import javax.validation.Valid

@RestController
@RequestMapping("/security")
class AuthenticationController {

    @Autowired
    private lateinit var myUserDetailService: MyUserDetailService

    @Autowired
    private lateinit var jwtUtil: JwtUtil



    @Autowired
    lateinit var authenticationManager:AuthenticationManager


    @PostMapping("/authenticate")
    fun authenticate(@Valid @RequestBody authenticationRequest:AuthenticationRequest):ResponseEntity<AuthenticationResponse>{
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(authenticationRequest.username, authenticationRequest.password))
        }catch (e:BadCredentialsException){
            throw Exception("Incorrect username or password")
        }
        val userDetails = myUserDetailService.loadUserByUsername(authenticationRequest.username)

        val jwtString = jwtUtil.generateToken(userDetails)

        return ResponseEntity.ok().body(AuthenticationResponse(jwtString))
    }
}