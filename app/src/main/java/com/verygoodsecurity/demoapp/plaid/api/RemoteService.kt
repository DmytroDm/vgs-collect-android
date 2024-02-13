package com.verygoodsecurity.demoapp.plaid.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.deserializers.StringDeserializer
import com.github.kittinunf.fuel.coroutines.awaitResponse
import com.github.kittinunf.fuel.coroutines.awaitString
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import java.nio.charset.Charset


object RemoteService {

    private const val BASE_URL = ""

    private const val LINK_TOKEN_PATH = "/api/create_link_token"
    private const val EXCHANGE_PUBLIC_TOKEN_PATH = "/api/exchange_public_token"
    private const val DATA_PATH = "/api/data"

    //Creates a Link token and return it
    suspend fun getLinkToken(bin: String, havePlaidAcc: Boolean): String? {
        return try {

            val response = Fuel.post(BASE_URL + LINK_TOKEN_PATH)
                .body(
                    JSONObject().apply {
                        put("bin", bin)
                        put("havePlaidAcc", havePlaidAcc)
                    }.toString()
                )
                .header("Content-Type", "application/json")
                .awaitString()
            JSONObject(response).getString("link_token")
        } catch (e: Exception) {
            null
        }
    }

    // Exchanges the public token from Plaid Link for an access token
    suspend fun getBalanceData(publicToken: String): String {
        val accessToken = getAccessToken(publicToken)
        return Fuel.get(BASE_URL + DATA_PATH)
            .header("Content-Type", "application/json")
            .header("access_token", accessToken)
            .awaitString()
    }

    // Fetches balance data using the Node client library for Plaid
    private suspend fun getAccessToken(publicToken: String): String {
        val response = Fuel.post(BASE_URL + EXCHANGE_PUBLIC_TOKEN_PATH)
            .body(
                JSONObject().apply {
                    put("public_token", publicToken)
                }.toString()
            )
            .header("Content-Type", "application/json")
            .awaitResponse(StringDeserializer(Charset.defaultCharset()), Dispatchers.IO)
        return (response.second.headers["access_token"] as ArrayList<String>)[0]
    }
}