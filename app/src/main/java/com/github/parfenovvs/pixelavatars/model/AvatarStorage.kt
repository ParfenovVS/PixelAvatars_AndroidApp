package com.github.parfenovvs.pixelavatars.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class AvatarStorage(context: Context) {

    private val prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(PREFS, MODE_PRIVATE)
    }

    fun saveAvatar(spriteType: SpriteType, seed: String) {
        val url = "$BASE_URL${spriteType.toValue()}/$seed.svg"
        val avatars = getAvatars().toMutableSet()
        avatars.add(url)
        prefs.edit()
            .putStringSet(KEY_AVATARS, avatars)
            .apply()
    }

    fun getAvatars(): Set<String> = prefs.getStringSet(KEY_AVATARS, null) ?: emptySet()

    private companion object {
        const val BASE_URL = "https://avatars.dicebear.com/v2/"
        const val PREFS = "prefs"
        const val KEY_AVATARS = "avatars"
    }

}