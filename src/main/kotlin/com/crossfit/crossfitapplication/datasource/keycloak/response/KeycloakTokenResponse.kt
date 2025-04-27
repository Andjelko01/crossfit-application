package com.crossfit.crossfitapplication.datasource.keycloak.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,

    @JsonProperty("refresh_token")
    val refreshToken: String,

    @JsonProperty("expires_in")
    val expiresIn: Int,
)
