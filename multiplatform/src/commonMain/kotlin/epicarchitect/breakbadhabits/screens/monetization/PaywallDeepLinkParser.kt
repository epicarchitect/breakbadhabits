package epicarchitect.breakbadhabits.screens.monetization

import epicarchitect.breakbadhabits.screens.root.RootRoute

fun parsePaywallDeepLink(rawSource: String?): RootRoute.Paywall? =
    rawSource
        ?.takeIf { it.isNotBlank() }
        ?.let(RootRoute::Paywall)
