package com.emorae.controllers

fun String.validateKey():Boolean{
    return "[a-zA-Z0-9-_]+".toRegex().matches(this)
}