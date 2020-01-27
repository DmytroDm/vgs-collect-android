package com.verygoodsecurity.vgscollect.core

/**
 *
 * Define type of Vault for VGSCollect to communicate with.
 * Please note, sensitive data cannot be used in the Sandbox environment.
 * Therefore, it’s a risk-free and stress-free environment to test out VGS.
 * You can easily modify your settings or even wipe the whole vault if you’d like.
 *
 * @param rawValue Unique identifier.
 *
 * @version 1.0.0
 */
enum class Environment(val rawValue:String) {

    /**
     *  Sandbox Environment using sandbox Test Vault
     */
    SANDBOX("sandbox"),

    /**
     *  Live Environment using sandbox Live Vault
     */
    LIVE("live")
}