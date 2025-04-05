package com.crossfit.crossfitapplication.application.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http { // Koristimo Kotlin invoke DSL za HttpSecurity
            authorizeHttpRequests { // Lambda DSL za autorizaciju
                // 1. Dozvoli specifične javne putanje prvo
                authorize("/member/**", permitAll)
                authorize("/login", permitAll) // Dozvola za pristup login putanji (ako se koristi loginPage)
                authorize("/error", permitAll) // Dozvola za Spring Boot default error stranicu

                // Primer: Dozvola za statičke resurse ako ih imate
                // authorize("/css/**", permitAll)
                // authorize("/js/**", permitAll)
                // authorize("/images/**", permitAll)
                // authorize("/", permitAll) // Dozvola za korensku putanju (homepage)

                // 2. Za sve ostale zahteve, zahtevaj autentifikaciju
                authorize(anyRequest, authenticated)
            }
            oauth2Login { // Konfiguracija OAuth2 Login-a
                // Definiše putanju koja inicira OAuth2 login tok preusmeravanjem na provajdera.
                // Ako želite podrazumevano ponašanje (obično /oauth2/authorization/{registrationId}),
                // možete ukloniti ovu liniju ili ceo `oauth2Login` blok ako koristite samo podrazumevane vrednosti.
                // loginPage = "/login"
            }
            csrf { // Novi, preporučeni način za konfiguraciju CSRF-a (lambda DSL)
                // UPOZORENJE: Onemogućavanje CSRF-a može biti bezbednosni rizik.
                // Uradite ovo samo ako ste sigurni da je potrebno za vašu aplikaciju
                // (npr. stateless API) ili ako CSRF zaštitu rešavate na drugi način.
                disable()
            }
        }
        return http.build()
    }
}
