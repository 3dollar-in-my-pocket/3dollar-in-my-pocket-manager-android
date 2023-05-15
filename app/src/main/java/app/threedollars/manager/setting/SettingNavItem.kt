package app.threedollars.manager.setting

sealed class SettingNavItem(var screenRoute: String) {
    object Faq : SettingNavItem("faq")
}