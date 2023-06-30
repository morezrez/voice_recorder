package morezrez.vcrecorder.ui

import android.R.attr
import android.R.id.toggle
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import app.rive.runtime.kotlin.core.Rive
import dagger.hilt.android.AndroidEntryPoint
import morezrez.vcrecorder.R
import morezrez.vcrecorder.databinding.ActivityMainBinding
import morezrez.vcrecorder.utils.DrawerLocker


@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,DrawerLocker {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var openDrawer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);



        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        Rive.init(applicationContext)

        binding.icMenu.setOnClickListener {
            if (!openDrawer) {
                openDrawer()
            } else if (openDrawer) {
                closeDrawer()
            }
        }

        binding.linearNavigationFile.setOnClickListener {
            if (navHostFragment.navController.currentDestination?.id == R.id.voiceListFragment) {
                closeDrawer()
            } else {
                navigateToListFragment()
                closeDrawer()
            }
        }

        binding.linearNavigationApps.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("bazaar://collection?slug=by_author&aid=628367061984")
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
        }
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
        openDrawer = true
    }

    fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        openDrawer = false
    }

    fun navigateToListFragment() {
        val action =
            ListeningFragmentDirections.actionListeningFragmentToVoiceListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun onBackPressed() {
        if (!openDrawer) {
            super.onBackPressed()
        } else if (openDrawer) {
            closeDrawer()
        }

    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode =
            if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        binding.drawerLayout.setDrawerLockMode(lockMode)
            }
}