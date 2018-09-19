package com.corphish.nightlight.engine.kcal

import com.corphish.nightlight.helpers.RootUtils
import java.io.File

/*
 * KCAL implementation for newer kernels (starting with v4.4)\
 * This implementation was first seen in kernels in devices with SDM845, hence the name.
 */
class SDM845KCALManager : KCALAbstraction {

    // File paths
    private val KCAL_RED = "/sys/module/msm_drm/parameters/kcal_red"
    private val KCAL_GREEN = "/sys/module/msm_drm/parameters/kcal_green"
    private val KCAL_BLUE = "/sys/module/msm_drm/parameters/kcal_blue"

    /**
     * A function to determine whether the implementation is supported by device
     */
    override fun isSupported(): Boolean =
            (File(KCAL_RED).exists() || RootUtils.doesFileExist(KCAL_RED)) && (File(KCAL_GREEN).exists() || RootUtils.doesFileExist(KCAL_GREEN)) && (File(KCAL_BLUE).exists() || RootUtils.doesFileExist(KCAL_BLUE))
    /**
     * A function to turn on KCAL
     */
    override fun turnOn() {
        // KCAL is always on?
    }

    /**
     * A function to adjust KCAL colors
     */
    override fun setColors(red: Int, green: Int, blue: Int) {
        RootUtils.writeToMultipleFilesAtOnce(
                listOf(red.toString(), green.toString(), blue.toString()),
                listOf(KCAL_RED, KCAL_GREEN, KCAL_BLUE)
        )
    }

    /**
     * A function get current KCAL color readings
     */
    override fun getColorReadings(): IntArray {
        return intArrayOf(
                RootUtils.readOneLine(KCAL_RED).toInt(),
                RootUtils.readOneLine(KCAL_GREEN).toInt(),
                RootUtils.readOneLine(KCAL_BLUE).toInt()
        )
    }
}