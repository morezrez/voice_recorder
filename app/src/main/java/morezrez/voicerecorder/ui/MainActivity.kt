package morezrez.voicerecorder.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import app.rive.runtime.kotlin.core.Rive
import dagger.hilt.android.AndroidEntryPoint
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import morezrez.voicerecorder.R
import morezrez.voicerecorder.databinding.ActivityMainBinding
import morezrez.voicerecorder.dialogs.DialogRate
import morezrez.voicerecorder.utils.DrawerLocker


@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,DrawerLocker {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var openDrawer: Boolean = false
    val TAPSELL_KEY = ""
    val context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        Rive.init(applicationContext)

        setUpTapsell()

        requestStandardBanner1()
        requestStandardBanner2()
        requestStandardBanner3()
        requestStandardBanner4()

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

        binding.linearNavigationRateUs.setOnClickListener {
            DialogRate().show(this.supportFragmentManager, "my tag")
        }
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
        openDrawer = true
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        openDrawer = false
    }

    private fun navigateToListFragment() {
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

    fun setUpTapsell(){

        TapsellPlus.initialize(this, TAPSELL_KEY,
            object : TapsellPlusInitListener {
                override fun onInitializeSuccess(adNetworks: AdNetworks) {
                    Log.d("onInitializeSuccess", adNetworks.name)
                }

                override fun onInitializeFailed(
                    adNetworks: AdNetworks,
                    adNetworkError: AdNetworkError
                ) {
                    Log.e(
                        "onInitializeFailed",
                        "ad network: " + adNetworks.name + ", error: " + adNetworkError.errorMessage
                    )
                }
            })
    }

    private fun requestStandardBanner1(){

        TapsellPlus.requestStandardBannerAd(
            context, "61309fd471ee512d24e5020a",
            TapsellPlusBannerType.BANNER_320x50,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)

                    //Ad is ready to show
                    //Put the ad's responseId to your responseId variable
                    val standardBannerResponseId = tapsellPlusAdModel.responseId

                    TapsellPlus.showStandardBannerAd(context, standardBannerResponseId,
                        findViewById<ViewGroup>(morezrez.voicerecorder.R.id.standardBanner),
                        object : AdShowListener() {
                            override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                                super.onOpened(tapsellPlusAdModel)
                            }

                            override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                                super.onError(tapsellPlusErrorModel)
                            }
                        })

                }

                override fun error(message: String) {}
            })

    }

    private fun requestStandardBanner2(){

        TapsellPlus.requestStandardBannerAd(
            context, "61608691b2c8056d868b64a9",
            TapsellPlusBannerType.BANNER_320x50,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)

                    //Ad is ready to show
                    //Put the ad's responseId to your responseId variable
                    val standardBannerResponseId = tapsellPlusAdModel.responseId

                    TapsellPlus.showStandardBannerAd(context, standardBannerResponseId,
                        findViewById<ViewGroup>(morezrez.voicerecorder.R.id.standardBanner2),
                        object : AdShowListener() {
                            override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                                super.onOpened(tapsellPlusAdModel)
                            }

                            override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                                super.onError(tapsellPlusErrorModel)
                            }
                        })

                }

                override fun error(message: String) {}
            })

    }

    private fun requestStandardBanner3(){

        TapsellPlus.requestStandardBannerAd(
            context, "64c7981f0554e77c6ad672d9",
            TapsellPlusBannerType.BANNER_320x50,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)

                    //Ad is ready to show
                    //Put the ad's responseId to your responseId variable
                    val standardBannerResponseId = tapsellPlusAdModel.responseId

                    TapsellPlus.showStandardBannerAd(context, standardBannerResponseId,
                        findViewById<ViewGroup>(morezrez.voicerecorder.R.id.standardBanner3),
                        object : AdShowListener() {
                            override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                                super.onOpened(tapsellPlusAdModel)
                            }

                            override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                                super.onError(tapsellPlusErrorModel)
                            }
                        })

                }

                override fun error(message: String) {}
            })

    }

    private fun requestStandardBanner4(){

        TapsellPlus.requestStandardBannerAd(
            context, "64c7988ee834a7751310b249",
            TapsellPlusBannerType.BANNER_320x50,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)

                    //Ad is ready to show
                    //Put the ad's responseId to your responseId variable
                    val standardBannerResponseId = tapsellPlusAdModel.responseId

                    TapsellPlus.showStandardBannerAd(context, standardBannerResponseId,
                        findViewById<ViewGroup>(morezrez.voicerecorder.R.id.standardBanner4),
                        object : AdShowListener() {
                            override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                                super.onOpened(tapsellPlusAdModel)
                            }

                            override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                                super.onError(tapsellPlusErrorModel)
                            }
                        })

                }

                override fun error(message: String) {}
            })

    }

}