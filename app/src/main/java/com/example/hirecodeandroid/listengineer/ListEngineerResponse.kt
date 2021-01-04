package com.example.hirecodeandroid.listengineer

import com.example.hirecodeandroid.dataclass.ItemSkillEngineerDataClass
import com.google.gson.annotations.SerializedName

data class ListEngineerResponse(val success: Boolean, val message: String, val data: List<Engineer>) {
    data class Engineer(@SerializedName("en_id") val engineerId: String?,
                        @SerializedName("ac_id") val accountId: String?,
                        @SerializedName("ac_name") val accountName: String?,
                        @SerializedName("ac_email") val accountEmail: String?,
                        @SerializedName("ac_phone") val accountPhone: String?,
                        @SerializedName("en_job_title") val engineerJobTitle: String?,
                        @SerializedName("en_job_type") val engineerJobType: String?,
                        @SerializedName("en_domisili") val engineerDomicilie: String?,
                        @SerializedName("en_deskripsi") val engineerDesc: String?,
                        @SerializedName("en_ft_profil") val engineerProfilePict: String?,
                        @SerializedName("en_created_at") val engineerCreated: String?,
                        @SerializedName("en_update_at") val engineerUpdate: String?,
                        @SerializedName("en_skill") val skillEngineer: List<ItemSkillEngineerDataClass>)
}