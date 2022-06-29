package app.threedollars.manager

sealed class SettingNavItem(var screenRoute: String) {
    object PrivacyPolicy : SettingNavItem("privacy_policy")
    object Faq : SettingNavItem("faq")
}