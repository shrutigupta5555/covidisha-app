package com.example.covidishaa

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.covidishaa.history.HistoryFragment
import com.example.covidishaa.medicine.MedicineFragment
import com.example.covidishaa.stats.StatsFragment
import com.example.covidishaa.utils.FirebaseUtils
import com.example.covidishaa.vaccine.VaccineFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        setupNavigation()
    }

    override fun onStart() {

        super.onStart()



    }

    override fun onResume() {
        super.onResume()

    }



    // Run the chat server as long as the app is on screen

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.title = "CoviDisha"
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
        } else if(item.itemId == R.id.miProfile){

            val fragment: Fragment = ProfileFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment)
                commit()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val homeFragment = HomeFragment()
        val historyFragment = HistoryFragment()
        val profileFragment = ProfileFragment()
        val vaccineFragment = VaccineFragment()
        val statsFragment = StatsFragment()
        val medicineFragment = MedicineFragment()
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
                    Toast.makeText(this, "History selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.BNMMed -> {
                    setCurrentFragment(medicineFragment)
                    Toast.makeText(this, "Medicine selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.BNMVaccine -> {
                    setCurrentFragment(vaccineFragment)
                    Toast.makeText(this, "Vaccine selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.BNMStats -> {
                    setCurrentFragment(statsFragment)
                    Toast.makeText(this, "Stats selected", Toast.LENGTH_SHORT).show()
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