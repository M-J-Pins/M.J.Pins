package com.example.quickwallet.presentation.navigation

sealed class Screen(val route: String, val title: String) {
    sealed class AuthScreens(route: String, title: String) : Screen(route, title) {
        object AuthStart : AuthScreens("auth-start", "AuthStart")
        object AuthPhoneRequest : AuthScreens("auth-phone-request", "AuthPhoneRequest")
        object AuthRequest : AuthScreens("auth-request", "AuthRequest")
    }

    sealed class QuickWallet(route: String, title: String) : Screen(route, title) {
        object QuickCards : QuickWallet("quick-cards", "QuickCards")
        object MyCards : QuickWallet("my-cards", "MyCards")
        object Wallet : QuickWallet("wallet", "Wallet")
        object AddCard : QuickWallet("add-card", "AddCard")
    }
}

val bottomNavigationScreens =
    listOf(
        Screen.QuickWallet.MyCards,
        Screen.QuickWallet.QuickCards,
        Screen.QuickWallet.Wallet
    )