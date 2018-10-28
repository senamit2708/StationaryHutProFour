package com.example.senamit.stationaryhutpro.activities


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.senamit.stationaryhutpro.R
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class StationaryMainPage : AppCompatActivity() {

    private val TAG = SignInActivity::class.java.simpleName

    private var mViewModel: ProductCartViewModel? = null
    private var count = 0



    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stationary_main_page)
//        TypefaceUtil.overrideFont(applicationContext, "SERIF", "fonts/Rubik-Regular.ttf") // font from assets: "assets/fonts/Roboto-Regular.ttf


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
//        supportActionBar?.run { setDisplayHomeAsUpEnabled(true)


//        setHomeButtonEnabled(true)}

        val host: NavHostFragment =
                supportFragmentManager
                        .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController

        setupActionBar(navController)
        setupNavigationMenu(navController)

        mViewModel = ViewModelProviders.of(this).get(ProductCartViewModel::class.java)
    }

    private fun setupActionBar(navController: NavController) {
        drawerLayout = findViewById(R.id.drawer_layout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

    }
    private fun setupNavigationMenu(navController: NavController) {
        findViewById<NavigationView>(R.id.nav_view)?.let { navigationView ->
            NavigationUI.setupWithNavController(navigationView, navController)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(drawerLayout,
                Navigation.findNavController(this, R.id.my_nav_host_fragment))

        onBackPressed()
        return true

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_empty, menu)

        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
                Navigation.findNavController(this, R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)

    }



    fun hideSoftKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun hideKeyboard(activity: Activity) {
        val inputManager = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun getUserId():String{
        if (FirebaseAuth.getInstance().uid == null){
            return FirebaseAuth.getInstance().uid.toString()
        }else{
            return "null"
        }
    }



}
