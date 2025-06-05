package com.ensim.vialibre.data.utils

import android.content.Context
import com.ensim.vialibre.R

object StaticStrings {
    lateinit var criteresNom: List<String>
    lateinit var criteresAfficher: List<String>

    fun init(context: Context) {
        criteresNom = context.resources.getStringArray(R.array.liste_criteres_noms).toList()
        criteresAfficher = context.resources.getStringArray(R.array.liste_criteres_afficher).toList()
    }
}
