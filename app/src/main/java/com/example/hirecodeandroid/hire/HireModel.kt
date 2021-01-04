package com.example.hirecodeandroid.hire

data class HireModel(
    val hireId: String?,
    val engineerId: String?,
    val projectId: String?,
    val hirePrice: Long?,
    val hireMessage: String?,
    val hireStatus: String?,
    val hireDateConfirm: String?,
    val hireCreated: String?,
    val companyName: String?,
    val projectName: String?,
    val projectDeadline: String?
)