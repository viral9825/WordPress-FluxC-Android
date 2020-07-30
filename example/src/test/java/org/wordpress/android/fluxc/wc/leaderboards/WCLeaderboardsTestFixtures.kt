package org.wordpress.android.fluxc.wc.leaderboards

import com.google.gson.Gson
import org.wordpress.android.fluxc.UnitTestUtils
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.model.WCProductModel
import org.wordpress.android.fluxc.model.leaderboards.WCTopPerformerProductModel
import org.wordpress.android.fluxc.network.rest.wpcom.wc.leaderboards.LeaderboardsApiResponse

object WCLeaderboardsTestFixtures {
    val stubSite = SiteModel().apply { id = 321 }

    fun generateSampleProductList() = listOf(
            "wc/top-performer-product-1.json",
            "wc/top-performer-product-2.json",
            "wc/top-performer-product-3.json",
            "wc/top-performer-product-2.json",
            "wc/top-performer-product-1.json"
    )
            .map { fileName ->
                UnitTestUtils.getStringFromResourceFile(this.javaClass, fileName)
                        ?.let { Gson().fromJson(it, WCProductModel::class.java) }
            }

    fun generateSampleShippingLabelApiResponse() =
            UnitTestUtils
                    .getStringFromResourceFile(this.javaClass, "wc/leaderboards-response-example.json")
                    ?.run { Gson().fromJson(this, Array<LeaderboardsApiResponse>::class.java) }

    val stubbedTopPerformersList by lazy {
        listOf(
                WCTopPerformerProductModel(
                        "info 0",
                        "currency 0",
                        0,
                        0.0,
                        321
                ),
                WCTopPerformerProductModel(
                        "info 1",
                        "currency 1",
                        1,
                        1.0,
                        321
                ),
                WCTopPerformerProductModel(
                        "info 2",
                        "currency 2",
                        2,
                        2.0,
                        321
                )
        )
    }
}
