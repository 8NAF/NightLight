package com.corphish.nightlight.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ShortcutManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import com.corphish.nightlight.BuildConfig
import com.corphish.nightlight.R

import com.corphish.nightlight.data.Constants
import com.corphish.nightlight.databinding.ActivitySplashBinding
import com.corphish.nightlight.engine.Core
import com.corphish.nightlight.engine.ForegroundServiceManager
import com.corphish.nightlight.engine.KCALManager
import com.corphish.nightlight.helpers.PreferenceHelper
import com.corphish.nightlight.helpers.RootUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*
 * Declare the shortcut intent strings and id
 */
const val SHORTCUT_INTENT_STRING_NL_TOGGLE = "android.intent.action.NL_TOGGLE"
const val SHORTCUT_ID_TOGGLE = "toggle"

const val TASKER_PLUGIN_INTENT = "com.twofortyfouram.locale.intent.action.EDIT_SETTING"
const val TASKER_INTENT_RQC = 100

class StartActivity : AppCompatActivity() {

    private var checkBypass = 7

    // ViewBinding
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (handleIntent())
            finish()
        else if (!handleTaskerIntent()) {
            if (!PreferenceHelper.getBoolean(this, Constants.COMPATIBILITY_TEST))
                checkCompatibility()
            else
                switchToMain()
        }

        binding.splashContainer.setOnClickListener {
            checkBypass--
            if (checkBypass == 0) switchToMain()
        }
    }

    /**
     * Handle the incoming intent of shortcut
     * Returns true if shortcut was handled, false otherwise
     */
    private fun handleIntent(): Boolean {
        val shortcutID: String

        if (intent.action != null) {
            when (intent.action) {
                SHORTCUT_INTENT_STRING_NL_TOGGLE -> {
                    shortcutID = SHORTCUT_ID_TOGGLE
                    doToggle()
                }
                else -> {
                    shortcutID = ""
                }
            }
        } else
            return false

        if (shortcutID.isEmpty()) return false


        /*
         * On Android 7.0 or below, bail out from now
         * This is because app shortcuts are not supported by default in those android versions
         * It however is supported in 3rd party launchers like nova launcher.
         * As android API guidelines suggest to reportShortcutUsed(), but that can be done only on API 25
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return true
        else {
            val shortcutManager = this.getSystemService(ShortcutManager::class.java)
            shortcutManager!!.reportShortcutUsed(shortcutID)
        }

        return true
    }

    private fun handleTaskerIntent(): Boolean {
        if (TASKER_PLUGIN_INTENT != intent.action) return false
        // Check if master switch is enabled
        // If enabled redirect to ProfilesActivity
        // Otherwise redirect to MainActivity with appropriate intent message
        // MainActivity will then use this message to show a prompt to user to enable night light
        val masterSwitchEnabled = PreferenceHelper.getBoolean(this, Constants.PREF_MASTER_SWITCH)
        val intent: Intent

        intent = Intent(this, if (masterSwitchEnabled) ProfilesActivity::class.java else MasterSwitchActivity::class.java)
        intent.putExtra(Constants.TASKER_ERROR_STATUS, !masterSwitchEnabled)
        startActivityForResult(intent, TASKER_INTENT_RQC)

        return true
    }

    /**
     * Actual night light toggling happens here
     */
    private fun doToggle() {
        val state = !PreferenceHelper.getBoolean(this, Constants.PREF_FORCE_SWITCH)
        val masterSwitch = PreferenceHelper.getBoolean(this, Constants.PREF_MASTER_SWITCH)

        /*
         * If state is on, while masterSwitch is off, turn on masterSwitch as well
         */
        if (state && !masterSwitch)
            PreferenceHelper.putBoolean(this, Constants.PREF_MASTER_SWITCH, true)

        Core.applyNightModeAsync(state, this)
    }

    private fun showAlertDialog(caption: Int, msg: Int) {
        if (isFinishing || isDestroyed) return
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppThemeCircular))
        builder.setTitle(caption)
        builder.setMessage(msg)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> finish() }
        builder.show()
    }

    /**
     * Performs compatibility checks during startup.
     */
    private fun checkCompatibility() {
        var rootAccessAvailable: Boolean
        var kcalSupported: Boolean

        GlobalScope.launch(Dispatchers.IO) {
            rootAccessAvailable = RootUtils.rootAccess
            kcalSupported = KCALManager.isKCALAvailable

            GlobalScope.launch(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if (!rootAccessAvailable) {
                    showAlertDialog(R.string.no_root_access, R.string.no_root_desc)
                    binding.alertPlaceholder.visibility = View.VISIBLE
                } else if (!kcalSupported) {
                    showAlertDialog(R.string.no_kcal, R.string.no_kcal_desc)
                    binding.alertPlaceholder.visibility = View.VISIBLE
                } else {
                    PreferenceHelper.putBoolean(applicationContext, Constants.COMPATIBILITY_TEST, true)
                    switchToMain()
                }

                if (BuildConfig.DEBUG) {
                    switchToMain()
                }
            }
        }
    }

    private fun switchToMain() {
        // Start the foreground service
        ForegroundServiceManager.startForegroundService(this)

        val intent = Intent(this, MasterSwitchActivity::class.java)
        intent.putExtra(Constants.FRESH_START, true)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != TASKER_INTENT_RQC) return
        setResult(RESULT_OK, data)
        finish()
    }
}
