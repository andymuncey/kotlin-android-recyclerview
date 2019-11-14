package com.tinyappco.shoplist

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceFragmentCompat
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.preference.PreferenceFragmentCompat


class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)
    }


}
