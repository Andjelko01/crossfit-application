package com.crossfit.crossfitapplication.datasource.keycloak.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
class KeycloakProperties {
    var registration: Registration = Registration()
    var provider: Provider = Provider()

    class Registration {
        var keycloak: Keycloak = Keycloak()

        class Keycloak {
            var clientId: String = ""
            var clientSecret: String = ""
            var authorizationGrantType: String = ""
            var scope: String = ""
        }
    }

    class Provider {
        var keycloak: Keycloak = Keycloak()

        class Keycloak {
            var issuerUri: String = ""
        }
    }
}


