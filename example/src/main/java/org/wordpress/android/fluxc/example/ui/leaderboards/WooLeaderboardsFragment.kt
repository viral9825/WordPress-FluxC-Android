package org.wordpress.android.fluxc.example.ui.leaderboards

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_woo_leaderboards.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wordpress.android.fluxc.Dispatcher
import org.wordpress.android.fluxc.example.R
import org.wordpress.android.fluxc.example.prependToLog
import org.wordpress.android.fluxc.example.ui.StoreSelectorDialog
import org.wordpress.android.fluxc.example.utils.toggleSiteDependentButtons
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.model.leaderboards.WCTopPerformerProductModel
import org.wordpress.android.fluxc.store.WCLeaderboardsStore
import org.wordpress.android.fluxc.store.WooCommerceStore
import javax.inject.Inject

class WooLeaderboardsFragment : Fragment(), StoreSelectorDialog.Listener {
    @Inject internal lateinit var dispatcher: Dispatcher
    @Inject internal lateinit var wooCommerceStore: WooCommerceStore
    @Inject internal lateinit var wcLeaderboardsStore: WCLeaderboardsStore

    private var selectedPos: Int = -1
    private var selectedSite: SiteModel? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_woo_leaderboards, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leaderboards_select_site.setOnClickListener(::onLeaderboardsSelectSiteButtonClicked)
        fetch_product_leaderboards.setOnClickListener(::onFetchProductsLeaderboardsClicked)
    }

    private fun onLeaderboardsSelectSiteButtonClicked(view: View) {
        fragmentManager?.let { fm ->
            StoreSelectorDialog.newInstance(this, selectedPos)
                    .show(fm, "StoreSelectorDialog")
        }
    }

    private fun onFetchProductsLeaderboardsClicked(view: View) {
        coroutineScope.launch {
            try {
                takeAsyncRequestWithValidSite { wcLeaderboardsStore.fetchProductLeaderboards(it) }
                        ?.model
                        ?.let { logLeaderboardResponse(it) }
                        ?: prependToLog("Couldn't fetch Products Leaderboards.")
            } catch (ex: Exception) {
                prependToLog("Couldn't fetch Products Leaderboards. Error: ${ex.message}")
            }
        }
    }

    override fun onSiteSelected(site: SiteModel, pos: Int) {
        selectedSite = site
        selectedPos = pos
        buttonContainer.toggleSiteDependentButtons()
        leaderboards_selected_site.text = site.name ?: site.displayName
    }

    private fun logLeaderboardResponse(model: List<WCTopPerformerProductModel>) {
        model.forEach {
            prependToLog("  Top Performer Product name: ${it.product.name ?: "Product name not available"}")
            prependToLog("  Top Performer currency: ${it.currency ?: "Currency not available"}")
            prependToLog("  Top Performer quantity: ${it.quantity ?: "Quantity not available"}")
            prependToLog("  Top Performer total: ${it.total ?: "total not available"}")
            prependToLog("  Top Performer id: ${it.id ?: "ID not available"}")
            prependToLog("  --------- Product ---------")
        }
        prependToLog("========== Top Performers =========")
    }

    private suspend inline fun <T> takeAsyncRequestWithValidSite(crossinline action: suspend (SiteModel) -> T) =
            selectedSite?.let {
                withContext(Dispatchers.Default) {
                    action(it)
                }
            }
}
