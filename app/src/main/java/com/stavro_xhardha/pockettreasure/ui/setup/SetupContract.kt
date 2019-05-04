package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.model.Country

interface SetupContract {
    fun onListItemClick(country: Country)
}