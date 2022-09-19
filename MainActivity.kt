package com.evvoiot.socketconnectiondemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket

//import io.socket.client.Socket

class MainActivity : AppCompatActivity() {

    private var btnConnectSocket: Button? = null
    private var btnDisconnectSocket: Button? = null
    private var btnReceiveSocketData: Button? = null
    private var mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnConnectSocket = findViewById(R.id.btnConnectSocket)
        btnDisconnectSocket = findViewById(R.id.btnDisconnectSocket)
        btnReceiveSocketData = findViewById(R.id.btnReceiveSocketData)

        example1()
    }

    private fun example1() {
        val app: SocketInstance = application as SocketInstance
        mSocket = app.getMSocket()
//connecting socket
        mSocket?.connect()
        val options = IO.Options()
        options.reconnection = true //reconnection
        options.forceNew = true

        if (mSocket?.connected() == true) {
            Toast.makeText(this, "Socket is connected", Toast.LENGTH_SHORT).show()
        }

        emitEvents()

        checkSocketDisconnectedORReconnected()
    }

    private fun checkSocketDisconnectedORReconnected() {
        //Disconnect
        mSocket?.on(Socket.EVENT_DISCONNECT) {
            runOnUiThread {
                Toast.makeText(this, "Socket Disconnected", Toast.LENGTH_SHORT).show()
            }
        }

        //Reconnect
        mSocket?.on(Socket.EVENT_RECONNECT) {
            mSocket?.connect()
            runOnUiThread {
                Toast.makeText(this, "Socket Reconnected", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun emitEvents() {
        mSocket?.on("sendCallToClientagjqtiwwrwlbhvredpou") { args ->
            if (args[0] != null) {
                val data = args[0]
                Log.e("output", data.toString())
                runOnUiThread {
                    Toast.makeText(this, "Data received from socket", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun disconnectSocket() {
        mSocket?.disconnect()
        mSocket?.off("sendCallToClientagjqtiwwrwlbhvredpou")
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectSocket()
    }
}