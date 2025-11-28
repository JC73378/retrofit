package com.example.partsasign1.Navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.partsasign1.ui.Screen.*
import com.example.partsasign1.viewmodels.*
import com.example.partsasign1.Viewmodels.AutentViewModel

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    val sharedPendingViewModel: PendingViewModel = viewModel()
    val solicitudesVM: SolicitudesViewModel = viewModel()

    NavHost(navController = navController, startDestination = Route.Login.path) {

        composable(Route.Login.path) {
            val autentViewModel: AutentViewModel = viewModel()

            LogScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Dashboard.path) {
                        popUpTo(Route.Login.path) { inclusive = true }
                    }
                },
                viewModel = autentViewModel
            )
        }

        composable(Route.Dashboard.path) {
            DashboardScreen(
                viewModel = viewModel<DashboardViewModel>(),
                onNavigateToSearch    = { navController.navigate(Route.Search.path) },
                onNavigateToScanner   = { navController.navigate(Route.Scanner.path) },
                onNavigateToPending   = { navController.navigate(Route.Pending.path) },
                onNavigateToProfile   = { navController.navigate(Route.Profile.path) },
                onNavigateToMap       = { navController.navigate(Route.Map.path) },
                onNavigateToHistorial = { navController.navigate(Route.Historial.path) },
                solicitudesVM = solicitudesVM
            )
        }

        composable(Route.Search.path) {
            SearchScreen(
                viewModel = viewModel<SearchViewModel>(),
                onRepuestoClick = { id ->
                    navController.navigate("detail/$id")
                },
                onBack = { navController.popBackStack() },
                onCreateSolicitud = { navController.navigate("solicitud") }
            )
        }

        composable("scanner") {
            ScannerScreen(
                onBarcodeScanned = { barcode ->
                    Log.d("SCANNER_RESULT", "Código escaneado: $barcode")

                    println("✅ ¡FUNCIONA! Código: $barcode")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Route.Pending.path) {
            PendingRScreen(
                viewModel = sharedPendingViewModel,
                onRepuestoClick = { id ->
                    navController.navigate("detail/$id")
                },
                onBack = { navController.popBackStack() },
                onNavigateToOTProgramada = { id ->
                    navController.navigate("ot_programada/$id")
                }
            )
        }

        composable(Route.OTProgramada.path) { backStackEntry ->

            val repuestoId = backStackEntry.arguments?.getString("id") ?: ""

            OTProgramadaScreen(
                viewModel = sharedPendingViewModel,
                repuestoId = repuestoId,
                onBack = { navController.popBackStack() },
                onOTGuardada = { id ->
                    sharedPendingViewModel.marcarRecibido(id)
                }
            )
        }

        composable(Route.Profile.path) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(Route.Dashboard.path) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Detail.path) {
            val vm: DetailViewModel = viewModel()
            val id = it.arguments?.getString("id") ?: ""
            LaunchedEffect(id) { vm.loadById(id) }

            RepuestoDetailScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Map.path) {
            LocationMapScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Solicitud.path) {
            SolicitudRepuestoScreen(
                viewModel = solicitudesVM,
                onBack = { navController.popBackStack() },
                onGuardado = {
                    navController.navigate(Route.Historial.path)
                }
            )
        }

        composable(Route.DeleteRequest.path) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val nombreEncoded = backStackEntry.arguments?.getString("nombre") ?: ""
            val nombre = try { java.net.URLDecoder.decode(nombreEncoded, java.nio.charset.StandardCharsets.UTF_8.toString()) } catch (_: Exception) { nombreEncoded }

            DeleteRepuestoScreen(
                repuestoId = id,
                repuestoNombre = nombre,
                onBack = { navController.popBackStack() },
                onGuardado = { s ->
                    solicitudesVM.agregar(s) {
                        navController.navigate(Route.Historial.path)
                    }
                }
            )
        }

        composable(Route.Historial.path) {
            HistorialRepuestosScreen(
                viewModel = solicitudesVM,
                onBack = { navController.popBackStack() }
            )
        }

    }
}
