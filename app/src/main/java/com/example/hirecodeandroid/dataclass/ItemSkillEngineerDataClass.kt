package com.example.hirecodeandroid.dataclass

import com.google.gson.annotations.SerializedName

data class ItemSkillEngineerDataClass(@SerializedName("sk_id") val skillId: String?,
                                      @SerializedName("en_id") val engineerId: String?,
                                      @SerializedName("sk_nama_skill") val skillName: String?)