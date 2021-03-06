package com.corphish.nightlight.data

/**
 * Created by Avinaba on 10/4/2017.
 * Constants
 */

object Constants {
    /**
     * SharedPreference keys
     */
    const val PREF_MASTER_SWITCH = "switch"
    const val PREF_FORCE_SWITCH = "force_switch"
    const val PREF_AUTO_SWITCH = "auto_switch"
    const val PREF_SUN_SWITCH = "sun_switch"
    const val PREF_START_TIME = "start_time"
    const val PREF_END_TIME = "end_time"
    const val PREF_LAST_START_TIME = "last_start_time"
    const val PREF_LAST_END_TIME = "last_end_time"
    const val LAST_LOC_LONGITUDE = "last_longitude"
    const val LAST_LOC_LATITUDE = "last_latitude"
    const val COMPATIBILITY_TEST = "compatibility_test"
    const val KCAL_PRESERVE_SWITCH = "kcal_preserve_switch"
    const val KCAL_PRESERVE_VAL = "kcal_preserve_const val"
    const val PREF_BOOT_MODE = "boot_mode"
    const val PREF_LAST_BOOT_RES = "last_boot_result"
    const val PREF_BOOT_DELAY = "boot_delay"
    const val PREF_CUR_APPLY_TYPE = "cur_apply_type"
    const val PREF_CUR_APPLY_EN = "cur_apply_switch"
    const val PREF_CUR_PROF_MODE = "cur_profile_mode"
    const val PREF_CUR_PROF_VAL = "cur_profile_settings"
    const val PREF_KCAL_BACKUP_EVERY_TIME = "kcal_backup_everytime"
        const val PREF_DARK_HOURS_ENABLE = "dark_hours_enable"
    const val PREF_DARK_HOURS_START = "dark_hours_start"
    const val PREF_LIGHT_THEME = "light_theme"
    const val PREF_THEME_CHANGE_EVENT = "theme_change_temp"
    const val PREF_SET_ON_BOOT = "set_on_boot"
    const val PREF_ICON_SHAPE = "icon_shape_v2"
    const val PREF_WIND_DOWN = "wind_down"
    const val PREF_SERVICE_STATE = "foreground_service_running"
    const val PREF_DISABLE_IN_LOCK_SCREEN = "disable_in_lock_screen"
    const val PREF_DISABLED_BY_LOCK_SCREEN = "disabled_by_lock_screen"
    const val PREF_FADE_ENABLED = "fade_enabled"
    const val PREF_FADE_POLL_RATE_MINS = "fade_poll_rate"

    /**
     * Fixed const values (default, max const values)
     * For max color intensities, values may seem minimum and vice versa
     */
    const val DEFAULT_START_TIME = "00:00"
    const val DEFAULT_END_TIME = "06:00"
    const val DEFAULT_LONGITUDE = "0.00"
    const val DEFAULT_LATITUDE = "0.00"
    const val DEFAULT_KCAL_VALUES = "256 256 256"
    const val DEFAULT_BOOT_DELAY = 20
    const val DEFAULT_LIGHT_THEME = false
    const val DEFAULT_SET_ON_BOOT = true
    const val DEFAULT_ICON_SHAPE = "0"
    const val DEFAULT_WIND_DOWN = false

    /**
     * Night light setting modes
     */
    const val NL_SETTING_MODE_TEMP = 0
    const val NL_SETTING_MODE_MANUAL = 1
    const val NL_SETTING_MODE_GRAYS_SCALE = 2

    /**
     * Apply types
     */
    const val APPLY_TYPE_PROFILE = 1
    const val APPLY_TYPE_NON_PROFILE = 0

    /**
     * Tasker error in-app communication msg
     */
    const val TASKER_ERROR_STATUS = "tasker_error"
    const val TASKER_SETTING = "tasker_setting"

    /**
     * Color control prefs and arrays.
     */
    const val PREF_SETTING_MODE = "nl_setting_mode"
    const val PREF_RED_COLOR = "color_red"
    const val PREF_GREEN_COLOR = "color_green"
    const val PREF_BLUE_COLOR = "color_blue"
    const val PREF_COLOR_TEMP = "color_temp"

    const val DEFAULT_BLUE_COLOR = 166
    const val DEFAULT_GREEN_COLOR = 205
    const val DEFAULT_RED_COLOR = 255
    const val DEFAULT_COLOR_TEMP = 4000

    /**
     * Profile version
     */
    const val PROFILE_API_VERSION = 1

    /**
     * Icon shapes
     */
    const val ICON_SHAPE_CIRCLE = 0
    const val ICON_SHAPE_SQUARE = 1
    const val ICON_SHAPE_ROUNDED_SQUARE = 2
    const val ICON_SHAPE_TEARDROP = 3

    /**
     * Profile create options
     */
    const val MODE_CREATE = 0
    const val MODE_EDIT = 1

    /**
     * Profile migration data keys
     */
    const val PROFILE_DATA_PRESENT = "dataPresent"
    const val PROFILE_DATA_NAME = "name"
    const val PROFILE_DATA_SETTING_ENABLED = "settingEnabled"
    const val PROFILE_DATA_SETTING_MODE = "settingMode"
    const val PROFILE_DATA_SETTING = "setting"
    const val PROFILE_MODE = "profileMode"

    const val FRESH_START = "fresh_start"

    /**
     * Foreground service actions
     */
    const val ACTION_START = "start"
    const val ACTION_STOP = "stop"

    /**
     * Automation section simulation
     */
    const val SIMULATE_AUTOMATION_SECTION = "sim_auto_section"

    /**
     * Support color picking.
     */
    const val COLOR_PICKER_MODE = "color_picker"

    /**
     * Index for updating automation routine.
     */
    const val ROUTINE_UPDATE_INDEX = "routineUpdateIndex"
}
