package ru.yarsu.web.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.http4k.core.cookie.Cookie
import java.util.Date

class JwtTools(private val secretKey: String, private val organizationName: String, private val tokenExpiration: Long) {
    private val algorithm = Algorithm.HMAC512(secretKey)

    fun createToken(login: String?): String {
        val now = Date()
        val expirationDate = Date(now.time + tokenExpiration)

        val builder: JWTCreator.Builder = JWT.create()
            .withSubject(login)
            .withExpiresAt(expirationDate)
            .withIssuer(organizationName)

        return builder.sign(algorithm)
    }

    fun getLogin(token: Cookie): String? {
        return try {
            val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer(organizationName)
                .build()
            val decodedJWT: DecodedJWT = verifier.verify(token.value)
            val expirationDate = decodedJWT.expiresAt
            val dateNow = Date()
            if (expirationDate != null && expirationDate.after(dateNow)) {
                return decodedJWT.subject
            } else {
                null
            }
        } catch (e: JWTVerificationException) {
            e.addSuppressed(e)
            null
        }
    }
}
