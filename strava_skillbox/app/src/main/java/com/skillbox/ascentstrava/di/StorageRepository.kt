package com.skillbox.ascentstrava.di

interface StorageRepository {

    fun addFlagAfterFirstEntry()
    fun isFirstEntry(): Boolean
}