package com.app.at_seguranca

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.android.billingclient.api.*
import com.app.at_seguranca.adapter.ArquivoAdapter
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_notas.*
import kotlinx.android.synthetic.main.fragment_cadastro.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeActivity : AppCompatActivity(),
    BillingClientStateListener,
    SkuDetailsResponseListener,
    PurchasesUpdatedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var arquivoAdapter: ArquivoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var clienteInApp: BillingClient
    private var currentSku = "android.test.purchased"
    private var mapSku = HashMap<String, SkuDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        lateinit var mAdView : AdView
        MobileAds.initialize(this) {
            val adView = AdView(this)
            adView.adSize = AdSize.BANNER
            adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
            mAdView = findViewById(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
        }
        buttonPremium.setOnClickListener {
            processGoogleInApp()
            adView.isVisible = false
            buttonPremium.isGone = true
        }
        clienteInApp = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
            clienteInApp.startConnection(this)



        buttonCriarLista.setOnClickListener{
            startActivity(Intent(this, NotasActivity::class.java))
        }

//        viewManager = LinearLayoutManager(this)
//        arquivoAdapter = ArquivoAdapter(ArrayList<File>(obterNotas()))
//        recyclerView = findViewById<RecyclerView>(R.id.recView).apply {
//            setHasFixedSize(true)
//            layoutManager = viewManager
//            adapter = arquivoAdapter
//        }


    }
    override fun onDestroy() {
        clienteInApp.endConnection()
        super.onDestroy()
    }

    override fun onBillingSetupFinished(p0: BillingResult) {
        if(p0?.responseCode ==
            BillingClient.BillingResponseCode.OK){
            Log.d("COMPRA>>","Serviço InApp conectado")
            val skuList = arrayListOf(currentSku)
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(
                BillingClient.SkuType.INAPP)
            clienteInApp.querySkuDetailsAsync(params.build(), this)
        }
    }

    override fun onBillingServiceDisconnected() {
        Log.d("COMPRA>>","Serviço InApp desconectado")
    }

    override fun onSkuDetailsResponse(p0: BillingResult, p1: MutableList<SkuDetails>?) {
        if(p0?.responseCode ==
            BillingClient.BillingResponseCode.OK){
            mapSku.clear()
            p1?.forEach{
                    t ->
                val preco = t.price
                val descricao = t.description
                mapSku[t.sku] = t
                Log.d("COMPRA>>",
                    "Produto Disponivel ($preco): $descricao")
            }
        }
    }
    fun processGoogleInApp(){
        val skuDetails = mapSku[currentSku]
        val purchaseParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails!!).build()
        clienteInApp.launchBillingFlow(this,purchaseParams)
    }
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase>?) {
        if (billingResult.responseCode ==
            BillingClient.BillingResponseCode.OK &&
            purchases != null) {
            for (purchase in purchases) {
                GlobalScope.launch(Dispatchers.IO) {
                    handlePurchase(purchase)
                }
            }
        } else if (billingResult.responseCode ==
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Log.d("COMPRA>>","Produto já foi comprado")
        } else if (billingResult.responseCode ==
            BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d("COMPRA>>","Usuário cancelou a compra")
        } else {
            Log.d("COMPRA>>",
                "Código de erro desconhecido: ${billingResult.responseCode}")
        }
    }
    suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState === Purchase.PurchaseState.PURCHASED)
        {
            Log.d("COMPRA>>","Produto obtido com sucesso")
            adView.isVisible = false
            buttonPremium.isGone = true
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult = withContext(Dispatchers.IO) {
                    clienteInApp.acknowledgePurchase(
                        acknowledgePurchaseParams.build())
                }
            }
        }
    }


}