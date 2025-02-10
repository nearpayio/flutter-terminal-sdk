package io.nearpay.terminalsdk.data.local

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

interface IEncryptedPreferencesGenerator {
    fun generateEncryptedSharedPreferences(context: Context): SharedPreferences
}

object EncryptedPreferencesGenerator : IEncryptedPreferencesGenerator {
    override fun generateEncryptedSharedPreferences(context: Context): SharedPreferences {
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT.or(KeyProperties.PURPOSE_DECRYPT)
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs_file",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}