package com.akarinti.sapoe.objects

import androidx.annotation.StringDef

@StringDef(
    PrefKey.PREF_KEY_LANGUAGE,
    PrefKey.PREF_KEY_COUNTRY
)
annotation class PrefKey {
    companion object {
        const val PREF_KEY_LANGUAGE = "p_lang"
        const val PREF_KEY_COUNTRY = "p_country"
        const val PREF_KEY_ACCESS_TOKEN = "access_token"
        const val PREF_KEY_AUTH_TOKEN = "auth_token"
        const val PREF_KEY_PROFILE = "p_profile"
        const val PREF_KEY_SELECTED_SCHEDULE = "p_sel_schedule"
        const val PREF_KEY_NAME = "p_name"
        const val PREF_KEY_HAS_REPORT = "p_has_report"
        const val PREF_KEY_SKIP_REPORT = "p_skip_report"
        const val PREF_KEY_TYPE = "p_type"
        const val PREF_KEY_DONE = "p_done"
        const val PREF_KEY_PENDING = "p_pending"
        const val PREF_KEY_PARTS = "p_parts"
        const val PREF_KEY_INFO = "p_info"
        const val PREF_KEY_FINIS = "p_finis"
        const val PREF_KEY_CATEGORY = "p_category"
        const val PREF_KEY_ORDERID = "p_orderid"
        const val PREF_KEY_USER = "p_user"
        const val PREF_KEY_INPROGRESS_PARENT_U = "p_inprogress_parent_u"
        const val PREF_KEY_INPROGRESS_PARENT = "p_inprogress_parent"
        const val PREF_KEY_INPROGRESS_CHILD = "p_child_parent"
        const val PREF_KEY_IMAGE = "p_image"
        const val PREF_KEY_OFFLINE_S_AC_U = "p_off_s_ac_u"
        const val PREF_KEY_OFFLINE_S_NB_U = "p_off_s_nb_u"
        const val PREF_KEY_OFFLINE_S_CL_U = "p_off_s_cl_u"
        const val PREF_KEY_OFFLINE_U_MCDS = "p_off_u_mcds"
        const val PREF_KEY_OFFLINE_U_QC = "p_off_u_qc"
        const val PREF_KEY_OFFLINE_S_AC = "p_off_s_ac"
        const val PREF_KEY_OFFLINE_S_NB = "p_off_s_nb"
        const val PREF_KEY_OFFLINE_S_CL = "p_off_s_cl"
        const val PREF_KEY_TICKET_LIST = "p_ticket_list"
        const val PREF_KEY_ORIGN_SCHEDULE = "p_orign_sch"
        const val PREF_KEY_ORIGN_UNSCHEDULE = "p_orign_unsch"
        const val PREF_KEY_ORIGN_UNSCHEDULE_AUTO = "p_orign_unsch_auto"
        const val PREF_KEY_ARCHIVE_SCHEDULE = "p_archive_sch"
        const val PREF_KEY_ARCHIVE_UNSCHEDULE = "p_archive_unsch"
        const val PREF_KEY_ARCHIVE_UNSCHEDULE_AUTO = "p_archive_unsch_auto"
        const val PREF_KEY_UNSENT_ANSWER = "p_unsent_answer"
    }
}