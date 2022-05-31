package com.example.letschat.model

class Users {
    var name : String? = null
    var email : String? = null
    var UID : String? = null

    constructor(){}

    constructor(name: String?, email: String?, UID: String?) {
        this.name = name
        this.email = email
        this.UID = UID
    }
}