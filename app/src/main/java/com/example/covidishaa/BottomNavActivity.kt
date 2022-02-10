package com.example.covidishaa

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.covidishaa.bluetooth.ChatServer
import com.example.covidishaa.utils.FirebaseUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        setupNavigation()
    }

    // Run the chat server as long as the app is on screen

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

        val homeFragment = HomeFragment()
        val historyFragment = HistoryFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.BNMhome -> {
                    setCurrentFragment(homeFragment)
                    Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
                    true
                }


                R.id.BNMHistory -> {
                    setCurrentFragment(historyFragment)
                    Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.BNMProfile -> {
                    setCurrentFragment(profileFragment)
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