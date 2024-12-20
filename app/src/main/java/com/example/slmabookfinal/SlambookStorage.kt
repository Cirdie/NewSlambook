package com.example.slmabookfinal

object SlambookStorage {
    private val slambooks = mutableListOf<SlambookEntry>()

    fun addSlambook(slambook: SlambookEntry) {
        slambooks.add(slambook)
    }

    fun getSlambooks(): List<SlambookEntry> = slambooks

    fun deleteSlambook(slambook: SlambookEntry) {
        slambooks.remove(slambook)
    }
}
