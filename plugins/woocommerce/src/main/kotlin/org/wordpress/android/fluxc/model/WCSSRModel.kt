package org.wordpress.android.fluxc.model

import org.wordpress.android.fluxc.persistence.entity.SSREntity

data class WCSSRModel(
    val remoteSiteId: Int,
    val environment: String? = null,
    val database: String? = null,
    val activePlugins: String? = null,
    val theme: String? = null,
    val settings: String? = null,
    val security: String? = null,
    val pages: String? = null
) {
    fun mapToEntity(): SSREntity {
        return SSREntity(
                id = id,
                localSiteId = localSiteId,
                environment = environment,
                database = database,
                activePlugins = activePlugins,
                theme = theme,
                settings = settings,
                security = security,
                pages = pages
        )
    }
}
