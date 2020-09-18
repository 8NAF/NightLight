package com.corphish.nightlight.activities

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.corphish.nightlight.BuildConfig
import com.corphish.nightlight.R
import com.corphish.nightlight.activities.base.BaseActivity
import com.corphish.nightlight.design.ThemeUtils
import com.corphish.nightlight.engine.KCALManager
import com.corphish.nightlight.engine.ProfilesManager
import com.corphish.nightlight.helpers.ExternalLink
import com.corphish.widgets.ktx.dialogs.MessageAlertDialog
import com.corphish.widgets.ktx.dialogs.SingleChoiceAlertDialog
import com.corphish.widgets.ktx.dialogs.properties.IconProperties

private const val TITLE_TAG = "settingsActivityTitle"

class SettingsActivity : BaseActivity(),
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ThemeUtils.getAppTheme(this))
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, HeaderFragment())
                    .commit()
        } else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
            setActionBarTitle(title.toString())
        }

        useCustomActionBar()
        setActionBarTitle(R.string.title_activity_settings)

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setActionBarTitle(R.string.title_activity_settings)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) {
            return true
        }

        return super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(
            caller: PreferenceFragmentCompat,
            pref: Preference
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
                classLoader,
                pref.fragment
        ).apply {
            arguments = args
            setTargetFragment(caller, 0)
        }


        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit()

        title = pref.title
        setActionBarTitle(pref.title.toString())

        return true
    }

    /**
     * This groups the settings available.
     */
    class HeaderFragment : PreferenceFragmentCompat() {
        // Profiles manager
        private lateinit var profilesManager: ProfilesManager

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.header_preferences, rootKey)

            // Update profile count
            updateProfileCount()

            // Show driver info
            findPreference<Preference>("kcal_driver")?.summary = KCALManager.implementation.getImplementationName()

            // Show app version
            findPreference<Preference>("about")?.summary = BuildConfig.VERSION_NAME

            // Show FAQ
            findPreference<Preference>("faq")?.setOnPreferenceClickListener {
                ExternalLink.open(requireContext(), "https://github.com/corphish/NightLight/blob/master/notes/usage.md")
                true
            }

            // Show appreciation fragment
            findPreference<Preference>("show_support")?.setOnPreferenceClickListener {
                val background = ContextCompat.getDrawable(requireContext(), ThemeUtils.getThemeIconShape(requireContext()))

                SingleChoiceAlertDialog(requireContext()).apply {
                    titleResId = R.string.show_support
                    messageResId = R.string.support_desc
                    dismissOnChoiceSelection = false
                    animationResourceLayout = R.raw.appreciate
                    iconProperties = IconProperties(
                            iconColor = if (ThemeUtils.isLightTheme(requireContext())) Color.WHITE else Color.BLACK,
                            backgroundDrawable = background
                    )
                    choiceList = listOf(
                            SingleChoiceAlertDialog.ChoiceItem(
                                    titleResId = R.string.rate,
                                    iconResId = R.drawable.ic_star,
                                    action = {
                                        ExternalLink.open(
                                                requireContext(),
                                                "market://details?id=com.corphish.nightlight." + (if (BuildConfig.FLAVOR != "generic") "donate" else "generic")
                                        )
                                    }),
                            SingleChoiceAlertDialog.ChoiceItem(
                                    titleResId = R.string.translate,
                                    iconResId = R.drawable.ic_translate,
                                    action = {
                                        ExternalLink.open(
                                                requireContext(),
                                                "https://github.com/corphish/NightLight/blob/master/notes/translate.md"
                                        )
                                    }),
                            SingleChoiceAlertDialog.ChoiceItem(
                                    titleResId = R.string.get_donate,
                                    iconResId = R.drawable.ic_money,
                                    action = {
                                        ExternalLink.open(
                                                requireContext(),
                                                "https://paypal.me/corphish"
                                        )
                                    })
                    )
                }.show()

                true
            }

            // Pro version
            findPreference<Preference>("pro_version")?.setOnPreferenceClickListener {
                MessageAlertDialog(requireContext()).apply {
                    titleResId = R.string.pro_long_title
                    messageResId = R.string.pro_desc
                    positiveButtonProperties = MessageAlertDialog.ButtonProperties(
                            buttonTitleResId = android.R.string.ok,
                            dismissDialogOnButtonClick = true,
                            buttonAction = {
                                ExternalLink.open(requireContext(), "market://details?id=com.corphish.nightlight.donate")
                            }
                    )
                    negativeButtonProperties = MessageAlertDialog.ButtonProperties(
                            buttonTitleResId = android.R.string.cancel,
                            dismissDialogOnButtonClick = true
                    )
                }.show()

                true
            }
        }

        /**
         * Updates profile summary with its count.
         */
        private fun updateProfileCount() {
            // One time init
            if (!this::profilesManager.isInitialized) {
                profilesManager = ProfilesManager(requireContext())
            }

            // Populate count
            profilesManager.loadProfiles()
            val count = profilesManager.profilesList.size

            findPreference<Preference>("profiles")?.summary =
                    requireContext().resources.getQuantityString(R.plurals.profile_count, count, count)
        }

        override fun onResume() {
            super.onResume()

            updateProfileCount()
        }
    }

    override fun onResume() {
        super.onResume()

        // Supply the resume callback to all child fragments
        for (f in supportFragmentManager.fragments) {
            f.onResume()
        }
    }
}