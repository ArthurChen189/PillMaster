package com.ece452.pillmaster.utils

enum class NavigationPath(val route: String) {
    DASHBOARD("dashboard"),
    HOMEPAGE("homepage"),
    LOGIN("login"),
    SIGNUP("signup"),
    POLICY("policy"),
    CARE_RECEIVER_HOMEPAGE("care_receiver_homepage"),
    CARE_GIVER_HOMEPAGE("care_giver_homepage"),
    PILL_ADD_PAGE("pill_add_page"),
    CAMERA_HOMEPAGE("camera_homepage"),
    CALENDAR("calendar"),
    CARE_GIVER_CONTACT("care_giver_contact"),
    CARE_RECEIVER_CONTACT("care_receiver_contact"),
    CARE_GIVER_USER_CHAT("care_giver_user_chat/receiverId={receiverId}/receiverEmail={receiverEmail}"),
    CARE_RECEIVER_USER_CHAT("care_receiver_user_chat/receiverId={receiverId}/receiverEmail={receiverEmail}"),
    RECEIVER_SETTING("receiver_setting"),
    PILL_MANAGE("pill_manage"),
    CAREGIVER_MANAGE("caregiver_manage"),
    CARE_GIVER_MESSAGE("care_giver_message"),
    HEALTH_BOT_PATH("healt_bot_path")
}