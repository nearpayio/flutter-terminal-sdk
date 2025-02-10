package io.nearpay.terminalsdk.data.local

import android.content.Context
import io.nearpay.terminalsdk.data.dto.AuthResponse
import io.nearpay.softpos.core.utils.enums.LoginType
import io.nearpay.terminalsdk.TerminalConnection
import io.nearpay.terminalsdk.TerminalConnectionData
import io.nearpay.terminalsdk.data.dto.Auth
import io.nearpay.terminalsdk.data.dto.ReaderAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

class TerminalSharedPreferences(
    context: Context,
    sharedPreferencesGenerator: IEncryptedPreferencesGenerator
) {

    val json = Json { isLenient = true; ignoreUnknownKeys = true }

    companion object {
        //location
        const val MOCKLOCATION = "mock"
        const val LAT = "lat"
        const val LONG = "long"

        //login
        const val CIG = "cig" //kernel config
        const val HA = "ha" //kernel config hash
        const val ISG = "isG" //is login
        const val CODE = "code" //user pin
        const val HASH = "hash" //admin pin
        const val NETWORK_PACKAGE_NAME = "networkPackageName"
        const val LOGIN_SESSION_UUID = "loginSessionUuid"
        const val LOGIN_TYPE = "loginType"

        //keys
        const val VER = "ver"
        const val IP = "iP"
        const val TA = "tA"
        const val YP = "yP"
        const val AK = "aK"
        const val RO = "rO"
        const val CN = "cn"
        const val RN = "rn"
        const val RT = "rt"

        //user
        const val CURRENT_USER = "currentUser"
        const val USER_NAME = "userName"
        const val USER_MOBILE = "userMobile"
        const val USER_EMAIL = "userEmail"
        const val USER_UUID = "userUUID"
        const val USER_LIST = "userList"
        const val TERMINAL_PROFILE = "terminalProfile"
        const val GENERATED_TERMINAL_UUID = "generatedTerminalUUID"
        const val ACTIVE_USER_UUID_KEY = "ACTIVE_USER_UUID"
        const val ACTIVE_USER_UUID_KEY_REFRESH = "ACTIVE_USER_UUID_REFRESH"

    }

    private val encryptedSharedPreferences =
        sharedPreferencesGenerator.generateEncryptedSharedPreferences(context)

    fun saveUserLogin(authResponse: AuthResponse): Boolean {
        val currentUserUUID = authResponse.user.userUUID
        saveUserToList(authResponse)
        runBlocking {
            encryptedSharedPreferences.edit().apply {
                putString(USER_UUID, currentUserUUID)
            }.apply()
            Timber.d("calling savePosCredential from saveUserLogin")
            savePosCredential(auth = authResponse.auth, uuid = currentUserUUID!!)
        }
        return encryptedSharedPreferences.edit().apply {
            putString(USER_NAME + currentUserUUID, authResponse.user?.name)
            putString(USER_MOBILE + currentUserUUID, authResponse.user?.mobile)
            putString(USER_EMAIL + currentUserUUID, authResponse.user?.email)
        }.commit()
    }

    fun deleteUserFromCache(userUUID: String): Boolean {
        // also delete the user from the list of users
        val userList = getUserList()
        userList.removeIf { it.user?.userUUID == userUUID }
        encryptedSharedPreferences.edit().apply {
            putString(USER_LIST, json.encodeToString(userList))
        }.apply()
        return encryptedSharedPreferences.edit().apply {
            remove(USER_NAME + userUUID)
            remove(USER_MOBILE + userUUID)
            remove(USER_EMAIL + userUUID)
            remove(ACTIVE_USER_UUID_KEY + userUUID)
            remove(ACTIVE_USER_UUID_KEY_REFRESH + userUUID)
            remove(LOGIN_TYPE + userUUID)
            remove(USER_UUID)
        }.commit()
    }


    private fun getCurrentUserUUID(): String? {
        return encryptedSharedPreferences.getString(USER_UUID, null)
    }


     fun setActiveUserUUID(userUUID: String?) {
        return encryptedSharedPreferences.edit().putString(USER_UUID, userUUID).apply()
    }

    fun getUserList(): ArrayList<AuthResponse> {
        return json.decodeFromString(
            encryptedSharedPreferences.getString(
                USER_LIST, "[]"
            ).toString()
        )
    }
    fun saveUserToList(loginUser: AuthResponse): Boolean {
        val userList = getUserList()
        if (userList.isNotEmpty()) {
            with(userList.listIterator()) {
                var userExists = false
                forEach {
                    if (it.user?.mobile == loginUser.user?.mobile || it.user?.email == loginUser.user?.email)
                        userExists = true
                }
                if (!userExists)
                    userList.add(loginUser)
            }
        } else {
            userList.add(loginUser)
        }

        return encryptedSharedPreferences.edit().apply {
            putString(USER_LIST, json.encodeToString(userList))
        }.commit()
    }

    fun saveTerminalsList(userUUID: String, terminalList: List<TerminalConnectionData>): Boolean {
        val terminalListKey = TERMINAL_PROFILE + userUUID
        return encryptedSharedPreferences.edit().apply {
            putString(terminalListKey, json.encodeToString(terminalList))
        }.commit()
    }

    fun checkTerminalExistsInUserProfile(userUUID: String, terminalUUID: String): Boolean {
        val terminalListKey = TERMINAL_PROFILE + userUUID
        val terminalList = encryptedSharedPreferences.getString(terminalListKey, null)?.let {
            json.decodeFromString<ArrayList<TerminalConnectionData>>(it)
        } ?: arrayListOf()
        return terminalList.any { it.tid == terminalUUID }
    }



    fun getUserByUUID(uuid: String): AuthResponse? {
        val userList = getUserList()
        return userList.find { it.user?.userUUID == uuid }
    }

     fun savePosCredential(
        auth: Auth,
        loginType: LoginType = LoginType.OTP,
        uuid: String
    ): Boolean {
        Timber.d("Im inside savePosCredential and auth access is ${auth.access_token} and refresh is ${auth.refresh_token}")
        var UUID: String = if (uuid == "refresh") {
            getCurrentUserUUID() ?: ""
        } else {
            uuid
        }
        Timber.d("the value of UUID is ${UUID.toString()}")
        encryptedSharedPreferences.edit().apply {
            putString(ACTIVE_USER_UUID_KEY + UUID, auth.access_token)
            putString(ACTIVE_USER_UUID_KEY_REFRESH + UUID, auth.refresh_token)
//            putString(RT + currentUserUUID, auth)
//            putString(VER + currentUserUUID, auth.keys.version)
//            putString(IP + currentUserUUID, auth.keys.pin)
//            putString(TA + currentUserUUID, auth.keys.attestation)
//            putString(YP + currentUserUUID, auth.keys.payment)

            putBoolean(ISG, true)
            putInt(LOGIN_TYPE + uuid, loginType.id)
        }.apply()
        Timber.d("uuid from savePosCredential: $uuid and uuid from getCurrentUserUUID: ${getCurrentUserUUID()}")

        Timber.d("Im inside savePosCredential and the new access token is ${encryptedSharedPreferences.getString(ACTIVE_USER_UUID_KEY + UUID, "")}")
        return true
    }

    fun saveActiveTerminalsForUser(userUUID: String?, terminalUUID: String): Boolean {
        // Retrieve existing terminals for the user
        val activeTerminalsKey = "active_terminals_$userUUID"
        val terminalList = encryptedSharedPreferences.getString(activeTerminalsKey, null)?.let {
            json.decodeFromString<ArrayList<String>>(it)
        } ?: arrayListOf()

        // Add the new terminal UUID if it doesn't already exist
        if (!terminalList.contains(terminalUUID)) {
            terminalList.add(terminalUUID)
        }

        // Save the updated list back to SharedPreferences
        return encryptedSharedPreferences.edit().apply {
            putString(activeTerminalsKey, json.encodeToString(terminalList))
        }.commit()
    }
    fun getActiveTerminalsForUser(userUUID: String): List<String> {
        val activeTerminalsKey = "active_terminals_$userUUID"

        val activeTerminals =  encryptedSharedPreferences.getString(activeTerminalsKey, null)?.let {
            json.decodeFromString<ArrayList<String>>(it)
        } ?: emptyList()

        Timber.d("active terminals for user: $activeTerminals")
        return activeTerminals
    }
    fun saveActiveTerminalUUID(terminalUUID: String): Boolean {
        return encryptedSharedPreferences.edit().apply {
            putString(GENERATED_TERMINAL_UUID, terminalUUID)
        }.commit()
    }

//    fun getActiveTerminalUUID(): String? {
//        return encryptedSharedPreferences.getString(GENERATED_TERMINAL_UUID, null)
//    }


    fun saveTerminalCredentials(
        transactionAuth: Auth,
        tid: String
    ): Boolean {
//        val currentUserUUID = getCurrentUserUUID()
//        val keyPrefix = "${currentUserUUID}_$terminalUUID" // Key includes user and terminal UUID
//        // Save the current terminal UUID
//        saveActiveTerminalUUID(terminalUUID)

        // Add the terminal to the list of active terminals for the user
        val currentUserUUID = getCurrentUserUUID()
        saveActiveTerminalsForUser(currentUserUUID, tid)

        return encryptedSharedPreferences.edit().apply {
            putString(CN + tid , transactionAuth.access_token)
            putString(RN + tid , transactionAuth.refresh_token)
        }.commit()
    }

    fun saveReaderToken(readerAuth: ReaderAuth, tid: String): Boolean {
//        val currentUserUUID = getCurrentUserUUID()
//        val keyPrefix = "${currentUserUUID}_$terminalUUID" // Key includes user and terminal UUID
//        Timber.d("currentUserUUID in saveReaderToken: $currentUserUUID")
        return encryptedSharedPreferences.edit().apply {
            putString(RT + tid,  readerAuth.reader.token)
        }.commit()
    }

//    fun saveReaderCoreCredentials(clientKeystore: Int, clientKeystorePassword: String, googleCloudProjectNumber: Long, huaweiSafetyDetectApiKey : String): Boolean {
//        val currentUserUUID = getCurrentUserUUID()
//        val keyPrefix = "${currentUserUUID}_$terminalUUID" // Key includes user and terminal UUID
//        Timber.d("currentUserUUID in saveReaderToken: $currentUserUUID")
//        return encryptedSharedPreferences.edit().apply {
//            putString(RT + keyPrefix + currentUserUUID, readerCoreCredentials.reader.token)
//        }.commit()
//    }

    private fun getNetworkPackageName(): String {
        return encryptedSharedPreferences.getString(NETWORK_PACKAGE_NAME, "").orEmpty()
    }

    private fun getTransactionCredential(tid : String): Auth? {
//        val currentUserUUID = getCurrentUserUUID() ?: return null
//        val activeTerminalUUID = getActiveTerminalUUID() ?: return null

//        val keyPrefix = "${currentUserUUID}_$activeTerminalUUID"
        val accessToken = encryptedSharedPreferences.getString(CN + tid, null)
        val refreshToken = encryptedSharedPreferences.getString(RN + tid, null)

        return if (accessToken != null && refreshToken != null) {
            Auth(access_token = accessToken, refresh_token = refreshToken)
        } else {
            null
        }
    }


    fun getTransactionAccessToken(tid: String) =
        getTransactionCredential(tid)?.access_token.orEmpty()

    fun getTransactionRefreshToken(tid: String) =
        getTransactionCredential(tid)?.refresh_token.orEmpty()

    fun getPosAccessToken() =
        encryptedSharedPreferences.getString(ACTIVE_USER_UUID_KEY + getCurrentUserUUID(), "")
            .orEmpty()

    fun getPosRefreshToken() =
        encryptedSharedPreferences.getString(ACTIVE_USER_UUID_KEY_REFRESH + getCurrentUserUUID(), "")
            .orEmpty()

    fun getReaderToken(tid :String): String {
//        val currentUserUUID = getCurrentUserUUID()
//        val activeTerminalUUID = getActiveTerminalUUID()
//        val keyPrefix = "${currentUserUUID}_$activeTerminalUUID"
        return encryptedSharedPreferences.getString(RT + tid, "")
            .orEmpty()

    }

    fun getLoginType(): LoginType {
        val loginTypeId = encryptedSharedPreferences.getInt(LOGIN_TYPE, -1)
        return if (loginTypeId != -1)
            LoginType.getType(loginTypeId)
        else
            LoginType.getType(encryptedSharedPreferences.getInt(LOGIN_TYPE + getCurrentUserUUID(), -1))
    }


}