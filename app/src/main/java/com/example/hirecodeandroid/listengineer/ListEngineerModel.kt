package com.example.hirecodeandroid.listengineer

import com.example.hirecodeandroid.dataclass.ItemSkillEngineerDataClass

class ListEngineerModel(val engineerId: String?,
                        val accountId: String?,
                        val accountName: String?,
                        val engineerJobTitle: String?,
                        val engineerJobType: String?,
                        val engineerDomicilie: String?,
                        val engineerProfilePict: String?,
                        val skillEngineer: List<ItemSkillEngineerDataClass?>)