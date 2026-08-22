package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.data.repository.MedicaRepository
import com.example.ui.components.AiVerifiedBadge
import com.example.ui.theme.*

// ==========================================
// 1. IN-APP CHAT SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    equipment: EquipmentListing,
    onBackClick: () -> Unit
) {
    val allChats by MedicaRepository.chatMessages.collectAsState()
    val messages = allChats[equipment.id] ?: listOf(
        ChatMessage("m1", equipment.id, equipment.sellerName, "Namaste! The ${equipment.title} is thoroughly sanitized and ready for inspection.", "10:00 AM", false)
    )
    var textInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MedicaNavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(equipment.sellerName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(equipment.sellerName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text("Regarding: ${equipment.title}", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = MedicaTealDark), maxLines = 1)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(tonalElevation = 6.dp, color = MaterialTheme.colorScheme.surface) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Ask about condition, pickup, sanitized check...") },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                MedicaRepository.sendChatMessage(equipment.id, textInput, "Aarav Sharma")
                                textInput = ""
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MedicaNavyPrimary)
                            .size(44.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isMe = msg.isFromUser
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (isMe) 14.dp else 2.dp,
                            bottomEnd = if (isMe) 2.dp else 14.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe) MedicaNavyPrimary else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isMe) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.timestamp,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isMe) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 9.sp
                                ),
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. UNIFIED BOOKINGS & ORDERS SCREEN
// ==========================================
@Composable
fun BookingsScreen(
    appointments: List<DoctorAppointment>,
    orders: List<MedicineOrder>,
    rentals: List<RentalBooking>,
    donations: List<DonationRequest>,
    labBookings: List<LabBooking>
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Doctors (${appointments.size})", "Equipment Rentals (${rentals.size})", "Medicines (${orders.size})", "Lab Tests (${labBookings.size})", "Donations (${donations.size})")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("bookings_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "My Healthcare & Equipment Activity",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "Track appointments, medicine orders, equipment rentals & donation requests",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(10.dp))

            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // TAB 0: DOCTORS
            if (selectedTab == 0) {
                if (appointments.isEmpty()) {
                    item { EmptyStateView("No Doctor Appointments", "Book tele-consultation or in-clinic visits with verified doctors.") }
                } else {
                    items(appointments, key = { it.id }) { apt ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(apt.doctor.name, fontWeight = FontWeight.Bold)
                                    Surface(shape = RoundedCornerShape(6.dp), color = MedicaGreenContainer) {
                                        Text(apt.status, color = Color(0xFF065F46), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text("${apt.doctor.specialty} • ${apt.consultationType}", style = MaterialTheme.typography.bodySmall, color = MedicaTealDark)
                                Text("📅 Date: ${apt.date}, Time: ${apt.timeSlot}", style = MaterialTheme.typography.bodySmall)
                                Text("Patient: ${apt.patientName} • Fee: ₹${apt.feePaid.toInt()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // TAB 1: EQUIPMENT RENTALS
            if (selectedTab == 1) {
                if (rentals.isEmpty()) {
                    item { EmptyStateView("No Active Rentals", "Rent oxygen concentrators, wheelchairs, or ICU beds with refundable security deposits.") }
                } else {
                    items(rentals, key = { it.id }) { rnt ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(rnt.equipment.title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                    Surface(shape = RoundedCornerShape(6.dp), color = MedicaTealContainer) {
                                        Text(rnt.status, color = MedicaTealOnContainer, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text("Rental Duration: ${rnt.totalDays} Days (${rnt.startDate} to ${rnt.endDate})", style = MaterialTheme.typography.bodySmall)
                                Text("Total Paid: ₹${rnt.totalAmount.toInt()} (Includes ₹${rnt.securityDeposit.toInt()} refundable deposit)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MedicaNavyPrimary)
                            }
                        }
                    }
                }
            }

            // TAB 2: MEDICINES
            if (selectedTab == 2) {
                if (orders.isEmpty()) {
                    item { EmptyStateView("No Medicine Orders", "Order genuine prescription and OTC medicines for doorstep delivery.") }
                } else {
                    items(orders, key = { it.id }) { ord ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Order #${ord.id}", fontWeight = FontWeight.Bold)
                                    Surface(shape = RoundedCornerShape(6.dp), color = MedicaNavyContainer) {
                                        Text(ord.status, color = MedicaNavyPrimary, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text("Items: ${ord.items.joinToString(", ") { "${it.quantity}x ${it.medicine.name}" }}", style = MaterialTheme.typography.bodySmall)
                                Text("Delivery: ${ord.deliveryAddress}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Total: ₹${ord.totalAmount.toInt()} • Placed: ${ord.orderedAt}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MedicaTealDark)
                            }
                        }
                    }
                }
            }

            // TAB 3: LAB TESTS
            if (selectedTab == 3) {
                if (labBookings.isEmpty()) {
                    item { EmptyStateView("No Lab Bookings", "Book certified health checkups and blood tests with home collection.") }
                } else {
                    items(labBookings, key = { it.id }) { lab ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(lab.test.testName, fontWeight = FontWeight.Bold)
                                Text("Lab: ${lab.test.labName} • Status: ${lab.status}", style = MaterialTheme.typography.bodySmall, color = MedicaTealDark)
                                Text("📅 Slot: ${lab.date}, ${lab.timeSlot}", style = MaterialTheme.typography.bodySmall)
                                Text("Home Collection Address: ${lab.address}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // TAB 4: DONATIONS
            if (selectedTab == 4) {
                if (donations.isEmpty()) {
                    item { EmptyStateView("No Donation Requests", "Request free equipment donated by caregivers and community partners.") }
                } else {
                    items(donations, key = { it.id }) { don ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(don.equipment.title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                    Surface(shape = RoundedCornerShape(6.dp), color = MedicaGreenContainer) {
                                        Text(don.status, color = Color(0xFF065F46), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text("Requester: ${don.requesterName} (${don.requesterPhone})", style = MaterialTheme.typography.bodySmall)
                                Text("Reason: ${don.purposeDescription}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. ADMIN & MODERATION DASHBOARD SCREEN
// ==========================================
@Composable
fun AdminDashboardScreen(
    stats: PlatformStats,
    reports: List<ListingReport>,
    equipmentList: List<EquipmentListing>,
    onApproveListing: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("admin_dashboard_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Medica Saathi Platform Governance",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = "Real-time ecosystem analytics, AI verification moderation, and trust & safety",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stat Cards Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminStatBox("Active Users", "${stats.totalUsers}", MedicaNavyContainer, MedicaNavyPrimary, Modifier.weight(1f))
                AdminStatBox("Equipment Listed", "${stats.activeListings}", MedicaTealContainer, MedicaTealDark, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminStatBox("Waste Saved", "${stats.wastePreventedKg.toInt()} kg", MedicaGreenContainer, Color(0xFF065F46), Modifier.weight(1f))
                AdminStatBox("Savings Generated", "₹84.5 Lakhs", Color(0xFFFEF3C7), Color(0xFFB45309), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("AI Equipment Verification Queue", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text("Review Gemini AI inspection reports and approve high-confidence listings", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(equipmentList.take(3), key = { it.id }) { eq ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(eq.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("AI Confidence: ${eq.aiConfidence}% • Seller: ${eq.sellerName}", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                    }

                    Button(
                        onClick = { onApproveListing(eq.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicaGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Approve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text("Reported Listings Moderation (${reports.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(reports, key = { it.id }) { rep ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MedicaRed.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(rep.equipmentTitle, fontWeight = FontWeight.Bold)
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFEE2E2)) {
                            Text(rep.status, color = MedicaRed, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp, 1.dp))
                        }
                    }
                    Text("Reason: ${rep.reason}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Reported by: ${rep.reporterName} • ${rep.timestamp}", style = MaterialTheme.typography.labelSmall, color = MedicaTextMuted)
                }
            }
        }
    }
}

// ==========================================
// 4. USER PROFILE & SETTINGS SCREEN
// ==========================================
@Composable
fun ProfileScreen(
    user: UserProfile,
    onRoleSwitchClick: () -> Unit,
    onLocationClick: () -> Unit,
    onAiAssistantClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
    ) {
        item {
            // Profile Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MedicaNavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.take(2).uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(user.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, null, tint = MedicaGreen, modifier = Modifier.size(16.dp))
                        }
                        Text(user.email, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        Text(user.phone, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Environmental Impact Certificate Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MedicaGreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Eco, null, tint = Color(0xFF065F46), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Circular Healthcare Champion", fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                        Text("You have contributed to reducing medical e-waste and supporting patient recovery.", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF065F46)))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items
            ProfileMenuItem("Switch Stakeholder Role", "Active: ${user.role.displayName}", Icons.Default.SwitchAccount, onRoleSwitchClick)
            ProfileMenuItem("Change Default Location", user.location, Icons.Default.LocationOn, onLocationClick)
            ProfileMenuItem("AI Price & Verification Assistant", "Estimate equipment circular value", Icons.Default.AutoAwesome, onAiAssistantClick)
            ProfileMenuItem("Emergency Healthcare Contacts", "Ambulance: 108 • Blood Bank: 104", Icons.Default.Emergency, {})
            ProfileMenuItem("Trust & Safety Policy", "Biomedical verification standards", Icons.Default.Security, {})
        }
    }
}

@Composable
private fun ProfileMenuItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MedicaNavyPrimary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp))
            }
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AdminStatBox(label: String, value: String, bg: Color, fg: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = fg))
            Text(label, style = MaterialTheme.typography.bodySmall.copy(color = fg, fontSize = 11.sp))
        }
    }
}

@Composable
private fun EmptyStateView(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Inbox, null, tint = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.size(56.dp))
        Spacer(modifier = Modifier.height(10.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant), modifier = Modifier.padding(horizontal = 20.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}
