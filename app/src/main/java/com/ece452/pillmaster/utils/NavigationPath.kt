package com.ece452.pillmaster.utils

enum class NavigationPath(val route: String) {
    DASHBOARD("dashboard"),
    HOMEPAGE("homepage"),
    LOGIN("login"),
    SIGNUP("signup"),
    CARE_RECEIVER_HOMEPAGE("care_receiver_homepage"),
    CARE_GIVER_HOMEPAGE("care_giver_homepage"),
    PILL_ADD_PAGE("pill_add_page"),
    CAMERA_HOMEPAGE("camera_homepage"),
    CALENDAR("calendar"),
    MESSAGE("message"),
    RECEIVER_SETTING("receiver_setting"),
    PILL_MANAGE("pill_manage"),
    CAREGIVER_MANAGE("caregiver_manage"),
    CARE_GIVER_MESSAGE("care_giver_message"),
    HEALTH_BOT_PATH("healt_bot_path")
}