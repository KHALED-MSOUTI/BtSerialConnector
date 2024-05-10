package com.oetech.bt_connection_agent

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BluetoothConnector {

    companion object {
        private var bluetoothManager: BluetoothManager = BluetoothManager.instance!!
        private lateinit var deviceInterface: SimpleBluetoothDeviceInterface
        private lateinit var context: Context

        private var onMessageReceivedListener: SimpleBluetoothDeviceInterface.OnMessageReceivedListener? =
            null
        private var onMessageSentListener: SimpleBluetoothDeviceInterface.OnMessageSentListener? =
            null
        private var onErrorListener: SimpleBluetoothDeviceInterface.OnErrorListener? = null


        fun setOnMessageReceivedListener(runnable: Runnable) {
            getOnMessageReceivedListener(runnable)
        }

        fun setOnMessageSentListener(runnable: Runnable) {
            getOnMessageReceivedListener(runnable)
        }

        fun setOnErrorListener(runnable: Runnable) {
            getOnErrorListener(runnable)
        }

        fun initialize(mac: String, context: Context) {
            bluetoothManager.openSerialDevice(mac)
            this.context = context

        }

         fun connectDevice(macAddress: String) {
            bluetoothManager.openSerialDevice(macAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ connectedDevice: BluetoothSerialDevice ->
                    this.onConnected(
                        connectedDevice
                    )
                },
                    { error: Throwable ->
                        run {
                            Toast.makeText(
                                context,
                                error.toString(),
                                Toast.LENGTH_SHORT
                            ).show().also {
                                bluetoothManager.closeDevice(macAddress)
                                Log.e("bluetooth", "disconnect")
                            }
                        }
                    })
                .run { Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show() }
        }

        private fun onConnected(connectedDevice: BluetoothSerialDevice) {
            try {
                // You are now connected to this device!
                // Here you may want to retain an instance to your device:
                connectedDevice.let {
                    deviceInterface = it.toSimpleDeviceInterface()
                }

                // Listen to bluetooth events
                deviceInterface.apply {
                    setListeners(
                        onMessageReceivedListener,
                        onMessageSentListener,
                        onErrorListener
                    )
                }
                // to send a data for device
                //deviceInterface.sendMessage("Hello world!")
            } catch (e: Exception) {
                Log.e("My Bluetooth App", "error: ")

            }
        }

        fun sendMessage(message: String) {
            deviceInterface.sendMessage(message)
        }

        fun close() {
            bluetoothManager.close()
        }

        private fun getOnMessageReceivedListener(runnable: Runnable):  SimpleBluetoothDeviceInterface.OnMessageReceivedListener {
            return object :  SimpleBluetoothDeviceInterface.OnMessageReceivedListener {
                override fun onMessageReceived(message: String) {
                    runnable.run()
                }
            }
        }

        private fun getOnMessageSentListener(runnable: Runnable):  SimpleBluetoothDeviceInterface.OnMessageSentListener {
            return object :  SimpleBluetoothDeviceInterface.OnMessageSentListener {
                override fun onMessageSent(message: String) {
                    runnable.run()
                }
            }
        }

        private fun getOnErrorListener(runnable: Runnable):  SimpleBluetoothDeviceInterface.OnErrorListener {
            return object :  SimpleBluetoothDeviceInterface.OnErrorListener {
                override fun onError(error: Throwable) {
                    runnable.run()
                }
            }
        }
    }
}