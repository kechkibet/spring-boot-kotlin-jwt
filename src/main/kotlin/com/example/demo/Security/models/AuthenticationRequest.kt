package com.example.demo.Security.models

import org.jetbrains.annotations.NotNull

data class AuthenticationRequest(@NotNull val username:String, @NotNull val password:String)