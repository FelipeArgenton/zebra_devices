package br.com.srssistemas.zebra

import android.content.Context
import android.content.Intent
import android.os.Bundle

class Helpers() {
    companion object {
        //Internal Commands
        const val SCAN_ACTION = "br.com.srssistemas.SCAN"

        //DataWedge Commands
        const val SEND_ACTION_COMMAND = "com.symbol.datawedge.api.ACTION"
        const val CREATE_PROFILE_COMMAND = "com.symbol.datawedge.api.CREATE_PROFILE"
        const val SEND_SET_CONFIG_COMMAND = "com.symbol.datawedge.api.SET_CONFIG"
        const val RETURN_ACTION_COMMAND = "com.symbol.datawedge.api.RESULT_ACTION"
        const val DATA_STRING_COMMAND = "com.symbol.datawedge.data_string"

        //Params
        const val PROFILE_NAME = "PROFILE_NAME"
        const val PROFILE_ENABLED = "PROFILE_ENABLED"
        const val CONFIG_MODE = "CONFIG_MODE"
        const val PLUGIN_NAME = "PLUGIN_NAME"
        const val RESET_CONFIG = "RESET_CONFIG"
        const val PLUGIN_CONFIG = "PLUGIN_CONFIG"
        const val PARAM_LIST = "PARAM_LIST"
        const val PACKAGE_NAME = "PACKAGE_NAME"
        const val ACTIVITY_LIST = "ACTIVITY_LIST"
        const val APP_LIST = "APP_LIST"

        //Barcode - Params
        const val SCANNER_SELECTION = "scanner_selection"
        const val SCANNER_INPUT_ENABLED = "scanner_input_enabled"

        //Intent - Params
        const val INTENT_OUTPUT_ENABLE = "intent_output_enabled"
        const val INTENT_ACTION = "intent_action"
        const val INTENT_DELIVERY = "intent_delivery"
        const val INTENT_DELIVERY_BROADCAST = "2"

        //Android - Commands
        const val INTENT_CATEGORY_DEFAULT = "android.intent.category.DEFAULT"
    }




}