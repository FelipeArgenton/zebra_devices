package br.com.srssistemas.zebra


import android.content.*
import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.EventChannel.StreamHandler
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class ZebraPlugin: FlutterPlugin, MethodCallHandler {
  private lateinit var channel : MethodChannel
  private lateinit var scanChannel: EventChannel
  private lateinit var context: Context
  private val SCAN_CHANNEL = "br.com.srssistemas/scan"
  private val CHANNEL = "br.com.srssistemas/zebra"

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext

    channel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL)
    channel.setMethodCallHandler(this)

    scanChannel = EventChannel(flutterPluginBinding.binaryMessenger, SCAN_CHANNEL)

    scanChannel.setStreamHandler(object: StreamHandler {
      private var scanBroadcast : BroadcastReceiver? = null
      override fun onListen(arguments: Any?, events: EventSink?) {
        scanBroadcast = createBroadcastReceiver(events)
        val scanIntent = IntentFilter()
        scanIntent.addAction(Helpers.SCAN_ACTION)
        scanIntent.addAction(Helpers.RETURN_ACTION_COMMAND)
        scanIntent.addCategory(Helpers.INTENT_CATEGORY_DEFAULT)
        context.registerReceiver(scanBroadcast, scanIntent)
      }

      override fun onCancel(arguments: Any?) {
        context.unregisterReceiver(scanBroadcast)
        scanBroadcast = null
      }
    })
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if(call.method == "createProfile"){
      val profileName = call.arguments.toString()
      createProfile(profileName)
    }else{
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
    scanChannel.setStreamHandler(null)
  }

  private fun createBroadcastReceiver(events: EventSink?) : BroadcastReceiver {
    return object: BroadcastReceiver(){
      override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action.equals(Helpers.SCAN_ACTION)){
          var scanData = intent?.getStringExtra(Helpers.DATA_STRING_COMMAND)
          println(scanData)
          events?.success(scanData)
        }
        else{
          events?.success("Nothing")
        }
      }
    }
  }

  private fun createProfile(profileName: String){
    registerProfileName(profileName)
    val mainProprieties = configureMainProprieties(profileName)
    mainProprieties.putBundle(Helpers.PLUGIN_CONFIG, enableBarcodePlugin())
    mainProprieties.putParcelableArray(Helpers.APP_LIST, arrayOf(configureAppProprieties()))
    registerProfileConfigurations(mainProprieties)

    //Register Plugin - One at a time
    mainProprieties.remove(Helpers.PLUGIN_CONFIG)
    mainProprieties.putBundle(Helpers.PLUGIN_CONFIG, enableIntentPlugin())
    registerProfileConfigurations(mainProprieties)
  }

  private fun registerProfileName(profileName: String){
    val profileNameIntent = Intent()
    profileNameIntent.action = Helpers.SEND_ACTION_COMMAND
    profileNameIntent.putExtra(Helpers.CREATE_PROFILE_COMMAND, profileName)
    context.sendBroadcast(profileNameIntent)
  }

  private fun configureMainProprieties(profileName: String): Bundle {
    val mainProprieties = Bundle()
    mainProprieties.putString(Helpers.PROFILE_NAME, profileName)
    mainProprieties.putString(Helpers.PROFILE_ENABLED, "true")
    mainProprieties.putString(Helpers.CONFIG_MODE, "UPDATE")
    return mainProprieties
  }

  private fun enableBarcodePlugin(): Bundle {
    val barcodeProprieties = Bundle()
    barcodeProprieties.putString(Helpers.PLUGIN_NAME, "BARCODE")
    barcodeProprieties.putString(Helpers.RESET_CONFIG, "true")
    barcodeProprieties.putBundle(Helpers.PARAM_LIST, configureBarcodeProprieties())
    return barcodeProprieties
  }

  private fun configureBarcodeProprieties() : Bundle{
    val barcodeProprieties = Bundle()
    return barcodeProprieties
  }

  private fun configureAppProprieties() : Bundle{
    val appProprieties = Bundle()
    appProprieties.putString(Helpers.PACKAGE_NAME, context.packageName)
    appProprieties.putStringArray(Helpers.ACTIVITY_LIST, arrayOf("*"))
    return appProprieties
  }

  private fun registerProfileConfigurations(profileConfig: Bundle){
    val registerProfileConfigIntent = Intent()
    registerProfileConfigIntent.action = Helpers.SEND_ACTION_COMMAND
    registerProfileConfigIntent.putExtra(Helpers.SEND_SET_CONFIG_COMMAND, profileConfig)
    context.sendBroadcast(registerProfileConfigIntent)
  }

  private fun enableIntentPlugin() : Bundle{
    val intentProprieties = Bundle()
    intentProprieties.putString(Helpers.PLUGIN_NAME, "INTENT")
    intentProprieties.putString(Helpers.RESET_CONFIG, "true")
    intentProprieties.putBundle(Helpers.PARAM_LIST, configureIntentPlugin())
    return intentProprieties
  }

  private fun configureIntentPlugin():Bundle{
    val intentProprieties = Bundle()
    intentProprieties.putString(Helpers.INTENT_OUTPUT_ENABLE, "true")
    intentProprieties.putString(Helpers.INTENT_ACTION, Helpers.SCAN_ACTION)
    intentProprieties.putString(Helpers.INTENT_DELIVERY, Helpers.INTENT_DELIVERY_BROADCAST)
    return intentProprieties
  }


}


