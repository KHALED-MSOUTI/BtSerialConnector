package com.oetech.bt_serial_connector

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.oetech.bt_connection_agent.BluetoothConnector

class MainActivity : AppCompatActivity() {

    private lateinit var macAddress: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        macAddress = "00:00:00:00:00:00" // change to your mac address

    }


    private fun initializeBluetoothConnector(){
        BluetoothConnector.initialize(macAddress, this)
        BluetoothConnector.setOnMessageReceivedListener{
            // do what ever you want with received message
        }
        BluetoothConnector.setOnMessageSentListener{
            // do what ever you want with sent message
        }
        BluetoothConnector.setOnErrorListener{
            // do what ever you want with error
        }
        BluetoothConnector.connectDevice(macAddress)
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothConnector.close()
    }
}