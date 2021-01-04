package com.example.hirecodeandroid.listengineer

import com.example.hirecodeandroid.dataclass.ItemSkillEngineerDataClass

data class ListEngineerModel(
                        val engineerId: String?,
                        val accountId: String?,
                        val accountName: String?,
                        val accountEmail: String?,
                        val accountPhone: String?,
                        val engineerJobTitle: String?,
                        val engineerJobType: String?,
                        val engineerDomicilie: String?,
                        val engineerDesc: String?,
                        val engineerProfilePict: String?,
                        val engineerCreated: String?,
                        val engineerUpdate: String?,
                        val skillEngineer: List<ItemSkillEngineerDataClass?>)