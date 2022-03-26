package com.example.covidishaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.covidishaa.bluetooth.ChatServer
import com.example.covidishaa.utils.FirebaseUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("fuckthis", FirebaseUtils.firebaseAuth.currentUser.toString()+"-=-=-=-=-home=-=")
        setupNavigation()
    }

    // Run the chat server as long as the app is on screen
    override fun onStart() {
        super.onStart()
        ChatServer.startServer(application)

        val user: FirebaseUser? = FirebaseUtils.firebaseAuth.currentUser
        Log.i("fuckthis", """${user.toString()}-----------------""")
        if (user == null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
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

    private fun setupNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)



        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {



                R.id.BNMHistory -> {

                    Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.BNMProfile -> {
                    Toast.makeText(this, "History selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}