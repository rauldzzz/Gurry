package com.example.gurry.ui.theme

interface Routable {
    val route: String
}

object HomePage: Routable {
    override val route: String = "main/homepage"
}

object Account: Routable {
    override val route: String = "main/account"
}

object Stable: Routable {
    override val route: String = "main/stable"
}

object Market: Routable {
    override val route: String = "main/market"
}