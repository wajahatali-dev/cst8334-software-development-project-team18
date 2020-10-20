package com.tutor.radar.dto

data class User(
    var id: String? = null,
    var name: String? = null,
    var address: String? = null,
    var institute: String? = null,
    var city: String? = null,
    var province: String? = null,
    var country: String? = null,
    var email: String? = null,
    var password: String? = null,
    var role: String? = null,
    var studentNumber: String? = null,
    var experienceYear : Int = 0,
    var specialCode: String? = null,
    var gender: String?=null,
    var subject: String?=null
)