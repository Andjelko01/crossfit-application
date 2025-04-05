package com.crossfit.crossfitapplication.application.config

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakConfig(
    @Value("\${keycloak.auth-server-url}") private val serverUrl: String,
    @Value("\${keycloak.realm}") private val realm: String,
    @Value("\${spring.security.oauth2.client.registration.keycloak.client-id}") private val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.keycloak.client-secret}") private val clientSecret: String,
) {
    @Bean
    fun keycloak(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .build()
    }

    @Bean
    fun realmResource(keycloak: Keycloak): RealmResource {
        return keycloak.realm(realm)
    }
}
