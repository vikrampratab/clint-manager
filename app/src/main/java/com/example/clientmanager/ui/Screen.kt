package com.example.clientmanager.ui

sealed class Screen(val route: String) {
    object ClientList : Screen("client_list")
    object AddClient : Screen("add_client")
    object ClientDetail : Screen("client_detail/{clientId}") {
        fun createRoute(clientId: Long) = "client_detail/$clientId"
    }
    object AddVisit : Screen("add_visit/{clientId}") {
        fun createRoute(clientId: Long) = "add_visit/$clientId"
    }
    object EditVisit : Screen("edit_visit/{clientId}/{visitId}") {
        fun createRoute(clientId: Long, visitId: Long) = "edit_visit/$clientId/$visitId"
    }
    object Dashboard : Screen("dashboard")
}
