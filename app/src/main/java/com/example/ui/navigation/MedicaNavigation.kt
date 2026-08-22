package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.models.*
import com.example.data.repository.MedicaRepository
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Landing : Screen("landing", "Explore", Icons.Default.Explore)
    object Home : Screen("home", "Dashboard", Icons.Default.Dashboard)
    object Marketplace : Screen("marketplace", "Equipment", Icons.Default.Autorenew)
    object Doctors : Screen("doctors", "Doctors", Icons.Default.MedicalServices)
    object Medicines : Screen("medicines", "Pharmacy", Icons.Default.LocalPharmacy)
    object Labs : Screen("labs", "Lab Tests", Icons.Default.Biotech)
    object ListEquipment : Screen("list_equipment", "List Equipment", Icons.Default.AddCircle)
    object EquipmentDetail : Screen("equipment_detail/{id}", "Details", Icons.Default.Info)
    object Chat : Screen("chat/{id}", "Chat", Icons.Default.Chat)
    object Bookings : Screen("bookings", "Bookings", Icons.Default.EventNote)
    object Admin : Screen("admin", "Admin", Icons.Default.AdminPanelSettings)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicaSaathiApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentUser by MedicaRepository.currentUser.collectAsState()
    val equipmentList by MedicaRepository.equipmentListings.collectAsState()
    val doctors by MedicaRepository.doctors.collectAsState()
    val medicines by MedicaRepository.medicines.collectAsState()
    val labTests by MedicaRepository.labTests.collectAsState()
    val cartItems by MedicaRepository.cart.collectAsState()
    val appointments by MedicaRepository.appointments.collectAsState()
    val orders by MedicaRepository.medicineOrders.collectAsState()
    val rentals by MedicaRepository.rentals.collectAsState()
    val donations by MedicaRepository.donations.collectAsState()
    val labBookings by MedicaRepository.labBookings.collectAsState()
    val platformStats by MedicaRepository.platformStats.collectAsState()
    val reports by MedicaRepository.reports.collectAsState()

    var showLocationDialog by remember { mutableStateOf(false) }
    var showRoleDialog by remember { mutableStateOf(false) }
    var showAiPriceDialog by remember { mutableStateOf(false) }

    val bottomNavItems = listOf(
        Screen.Landing,
        Screen.Marketplace,
        Screen.Doctors,
        Screen.Medicines,
        Screen.Bookings,
        if (currentUser.role == UserRole.ADMIN) Screen.Admin else Screen.Profile
    )

    val showBottomBar = currentRoute in listOf(
        Screen.Landing.route,
        Screen.Home.route,
        Screen.Marketplace.route,
        Screen.Doctors.route,
        Screen.Medicines.route,
        Screen.Labs.route,
        Screen.Bookings.route,
        Screen.Admin.route,
        Screen.Profile.route
    )

    Scaffold(
        topBar = {
            if (showBottomBar) {
                MedicaTopAppBar(
                    title = "Medica Saathi",
                    currentLocation = currentUser.location,
                    currentRole = currentUser.role,
                    cartItemCount = cartItems.sumOf { it.quantity },
                    onLocationClick = { showLocationDialog = true },
                    onRoleClick = { showRoleDialog = true },
                    onCartClick = { navController.navigate(Screen.Medicines.route) },
                    onNotificationClick = { navController.navigate(Screen.Bookings.route) },
                    onSearchClick = { navController.navigate(Screen.Marketplace.route) }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(Screen.Landing.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    tint = if (isSelected) MedicaNavyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MedicaNavyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MedicaNavyContainer
                            ),
                            modifier = Modifier.testTag("nav_${screen.route}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Landing.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Landing / Explore Screen
            composable(Screen.Landing.route) {
                LandingScreen(
                    equipmentList = listOf(equipmentList),
                    onExploreHealthcare = { navController.navigate(Screen.Home.route) },
                    onExploreMarketplace = { navController.navigate(Screen.Marketplace.route) },
                    onNavigateToDoctors = { navController.navigate(Screen.Doctors.route) },
                    onNavigateToMedicines = { navController.navigate(Screen.Medicines.route) },
                    onNavigateToLabs = { navController.navigate(Screen.Labs.route) },
                    onEquipmentClick = { eq -> navController.navigate("equipment_detail/${eq.id}") },
                    onListEquipmentClick = { navController.navigate(Screen.ListEquipment.route) },
                    onAiAssistantClick = { showAiPriceDialog = true }
                )
            }

            // User Dashboard Screen
            composable(Screen.Home.route) {
                HomeScreen(
                    user = currentUser,
                    equipmentList = equipmentList,
                    appointments = appointments,
                    orders = orders,
                    rentals = rentals,
                    labBookings = labBookings,
                    onNavigateToMarketplace = { navController.navigate(Screen.Marketplace.route) },
                    onNavigateToDoctors = { navController.navigate(Screen.Doctors.route) },
                    onNavigateToMedicines = { navController.navigate(Screen.Medicines.route) },
                    onNavigateToLabs = { navController.navigate(Screen.Labs.route) },
                    onNavigateToBookings = { navController.navigate(Screen.Bookings.route) },
                    onEquipmentClick = { eq -> navController.navigate("equipment_detail/${eq.id}") },
                    onListEquipmentClick = { navController.navigate(Screen.ListEquipment.route) },
                    onAiAssistantClick = { showAiPriceDialog = true },
                    onLocationClick = { showLocationDialog = true }
                )
            }

            // Equipment Marketplace
            composable(Screen.Marketplace.route) {
                MarketplaceScreen(
                    equipmentList = equipmentList,
                    currentLocation = currentUser.location,
                    onEquipmentClick = { eq -> navController.navigate("equipment_detail/${eq.id}") },
                    onListEquipmentClick = { navController.navigate(Screen.ListEquipment.route) },
                    onAiAssistantClick = { showAiPriceDialog = true }
                )
            }

            // Doctors Screen
            composable(Screen.Doctors.route) {
                DoctorsScreen(
                    doctors = doctors,
                    onDoctorBooked = { navController.navigate(Screen.Bookings.route) }
                )
            }

            // Medicines Pharmacy Screen
            composable(Screen.Medicines.route) {
                MedicinesScreen(
                    medicines = medicines,
                    cartItems = cartItems,
                    onAddToCart = { med -> MedicaRepository.addToCart(med) },
                    onUpdateQuantity = { id, delta -> MedicaRepository.updateCartQuantity(id, delta) },
                    onCheckoutSuccess = { navController.navigate(Screen.Bookings.route) }
                )
            }

            // Lab Tests Screen
            composable(Screen.Labs.route) {
                LabTestsScreen(
                    labTests = labTests,
                    onTestBooked = { navController.navigate(Screen.Bookings.route) }
                )
            }

            // List Equipment Wizard
            composable(Screen.ListEquipment.route) {
                ListEquipmentWizardScreen(
                    onBackClick = { navController.popBackStack() },
                    onListingPublished = { listing ->
                        navController.navigate("equipment_detail/${listing.id}") {
                            popUpTo(Screen.Marketplace.route)
                        }
                    }
                )
            }

            // Equipment Detail Screen
            composable(
                route = Screen.EquipmentDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val eq = equipmentList.find { it.id == id } ?: equipmentList.first()

                EquipmentDetailScreen(
                    equipment = eq,
                    onBackClick = { navController.popBackStack() },
                    onContactSeller = { item -> navController.navigate("chat/${item.id}") },
                    onRentalSuccess = { navController.navigate(Screen.Bookings.route) },
                    onDonationSuccess = { navController.navigate(Screen.Bookings.route) },
                    onBuySuccess = { navController.navigate(Screen.Bookings.route) }
                )
            }

            // In-App Chat
            composable(
                route = Screen.Chat.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val eq = equipmentList.find { it.id == id } ?: equipmentList.first()

                ChatScreen(
                    equipment = eq,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Bookings Screen
            composable(Screen.Bookings.route) {
                BookingsScreen(
                    appointments = appointments,
                    orders = orders,
                    rentals = rentals,
                    donations = donations,
                    labBookings = labBookings
                )
            }

            // Admin Dashboard Screen
            composable(Screen.Admin.route) {
                AdminDashboardScreen(
                    stats = platformStats,
                    reports = reports,
                    equipmentList = equipmentList,
                    onApproveListing = { id -> MedicaRepository.approveAiVerification(id) }
                )
            }

            // Profile Screen
            composable(Screen.Profile.route) {
                ProfileScreen(
                    user = currentUser,
                    onRoleSwitchClick = { showRoleDialog = true },
                    onLocationClick = { showLocationDialog = true },
                    onAiAssistantClick = { showAiPriceDialog = true }
                )
            }
        }
    }

    // Modal Overlays
    if (showLocationDialog) {
        LocationSelectorDialog(
            currentLocation = currentUser.location,
            onDismiss = { showLocationDialog = false },
            onSelectLocation = { newLoc -> MedicaRepository.updateLocation(newLoc) }
        )
    }

    if (showRoleDialog) {
        RoleSwitcherDialog(
            currentRole = currentUser.role,
            onDismiss = { showRoleDialog = false },
            onSelectRole = { newRole ->
                MedicaRepository.updateRole(newRole)
                if (newRole == UserRole.ADMIN) {
                    navController.navigate(Screen.Admin.route)
                }
            }
        )
    }

    if (showAiPriceDialog) {
        AiPriceAssistantDialog(
            onDismiss = { showAiPriceDialog = false }
        )
    }
}
