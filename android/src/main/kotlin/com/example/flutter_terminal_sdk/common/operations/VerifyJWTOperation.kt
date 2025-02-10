package com.example.flutter_terminal_sdk.common.operations


import android.util.Log
import com.example.flutter_terminal_sdk.common.NearpayProvider
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import com.example.flutter_terminal_sdk.common.status.ResponseHandler
import io.nearpay.terminalsdk.Terminal
import io.nearpay.terminalsdk.data.dto.JWTLoginData
import io.nearpay.terminalsdk.listeners.JWTLoginListener
import io.nearpay.terminalsdk.listeners.failures.JWTLoginFailure


class VerifyJWTOperation(provider: NearpayProvider) : BaseOperation(provider) {
    override fun run(filter: ArgsFilter, response: (Map<String, Any>) -> Unit) {

        val jwt = filter.getString("jwt") ?: return response(
            ResponseHandler.error("MISSING_MOBILE", "jwt is required")
        )

        val loginData = JWTLoginData(
            jwt = jwt,
        )
        provider.terminalSdk?.jwtLogin(loginData, object : JWTLoginListener {

            override fun onJWTLoginSuccess(terminal: Terminal) {


                val resultData = mapOf(
                    "tid" to terminal.terminalUUID,
                    "isReady" to terminal.isTerminalReady(),
                    "terminalUUID" to terminal.terminalUUID,
                    "uuid" to terminal.terminalUUID,
                )
                response(ResponseHandler.success("Login successful: ${terminal.terminalUUID}", resultData))


            }

            override fun onJWTLoginFailure(jwtLoginFailure: JWTLoginFailure) {
                val errorMessage = jwtLoginFailure.toString()
                Log.d("asdf" ,"im here + " + jwtLoginFailure.toString())
                response(ResponseHandler.error("VERIFY_FAILURE", errorMessage))
            }
        })
    }
}