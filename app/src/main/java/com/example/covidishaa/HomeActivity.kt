package com.example.covidishaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.covidishaa.bluetooth.ChatServer
import com.example.covidishaa.utils.FirebaseUtils

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Run the chat server as long as the app is on screen
    override fun onStart() {
        super.onStart()
        ChatServer.startServer(application)
    }

    override fun onStop() {
        super.onStop()
        ChatServer.stopServer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miLogOut){


            //logout user
            FirebaseUtils.firebaseAuth.signOut()
            val logoutIntent = Intent(this, MainActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}