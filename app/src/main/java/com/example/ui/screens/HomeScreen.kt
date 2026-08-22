package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.components.AiVerifiedBadge
import com.example.ui.components.CircularEconomyBanner
import com.example.ui.components.EquipmentItemCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    user: UserProfile,
    equipmentList: List<EquipmentListing>,
    appointments: List<DoctorAppointment>,
    orders: List<MedicineOrder>,
    rentals: List<RentalBooking>,
    labBookings: List<LabBooking>,
    onNavigateToMarketplace: () -> Unit,
    onNavigateToDoctors: () -> Unit,
    onNavigateToMedicines: () -> Unit,
    onNavigateToLabs: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onEquipmentClick: (EquipmentListing) -> Unit,
    onListEquipmentClick: () -> Unit,
    onAiAssistantClick: () -> Unit,
    onLocationClick: () -> Unit
) {
    val nearbyEquipment = remember(equipmentList) { equipmentList.take(4) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_dashboard_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. GREETING & LOCATION HEADER
        item {
            Card(
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = MedicaNavyPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Good morning,",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MedicaNavyDark,
                            border = BorderStroke(1.dp, MedicaTealLight.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onLocationClick() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MedicaTealLight,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = user.location,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // AI Quick Assistant Callout
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAiAssistantClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MedicaTealLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Estimate fair price & verify equipment using Gemini AI",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MedicaTealLight,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. QUICK ACTIONS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DashboardActionItem(
                        icon = Icons.Default.MedicalServices,
                        label = "Book\nDoctor",
                        containerColor = MedicaNavyContainer,
                        iconTint = MedicaNavyPrimary,
                        onClick = onNavigateToDoctors
                    )

                    DashboardActionItem(
                        icon = Icons.Default.LocalPharmacy,
                        label = "Order\nMedicine",
                        containerColor = MedicaTealContainer,
                        iconTint = MedicaTealDark,
                        onClick = onNavigateToMedicines
                    )

                    DashboardActionItem(
                        icon = Icons.Default.Biotech,
                        label = "Book\nLab Test",
                        containerColor = Color(0xFFE0F2FE),
                        iconTint = Color(0xFF0284C7),
                        onClick = onNavigateToLabs
                    )

                    DashboardActionItem(
                        icon = Icons.Default.Autorenew,
                        label = "Find\nEquipment",
                        containerColor = Color(0xFFFEF3C7),
                        iconTint = Color(0xFFB45309),
                        onClick = onNavigateToMarketplace
                    )

                    DashboardActionItem(
                        icon = Icons.Default.AddCircle,
                        label = "List\nItem",
                        containerColor = Color(0xFFD1FAE5),
                        iconTint = Color(0xFF047857),
                        onClick = onListEquipmentClick
                    )
                }
            }
        }

        // 3. UPCOMING APPOINTMENTS & ACTIVE ORDERS
        if (appointments.isNotEmpty() || orders.isNotEmpty() || rentals.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Upcoming & Active Care",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )

                        TextButton(onClick = onNavigateToBookings) {
                            Text("View All", fontWeight = FontWeight.Bold, color = MedicaTealPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Doctor Appointment Card
                    appointments.firstOrNull()?.let { apt ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable { onNavigateToBookings() }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MedicaNavyContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.VideoCameraFront,
                                        null,
                                        tint = MedicaNavyPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = apt.doctor.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "${apt.doctor.specialty} • ${apt.consultationType}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    )
                                    Text(
                                        text = "📅 ${apt.date}, ${apt.timeSlot}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MedicaTealDark,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MedicaGreenContainer
                                ) {
                                    Text(
                                        text = apt.status,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF065F46),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Medicine Delivery Card
                    orders.firstOrNull()?.let { ord ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToBookings() }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MedicaTealContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.LocalShipping,
                                        null,
                                        tint = MedicaTealDark,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Order #${ord.id} (${ord.items.size} Medicines)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Total ₹${ord.totalAmount.toInt()} • Placed ${ord.orderedAt}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    )
                                    Text(
                                        text = "🚚 ${ord.status} (Delivery in ~45 mins)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MedicaBlueAccent,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. CIRCULAR BANNER
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                CircularEconomyBanner(onExploreClick = onNavigateToMarketplace)
            }
        }

        // 5. NEARBY VERIFIED EQUIPMENT SECTION
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Nearby Verified Equipment",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "Within 5 km of ${user.location.split(",").firstOrNull() ?: "you"}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    TextButton(onClick = onNavigateToMarketplace) {
                        Text("View Marketplace", fontWeight = FontWeight.Bold, color = MedicaTealPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    nearbyEquipment.forEach { eq ->
                        EquipmentItemCard(
                            equipment = eq,
                            onCardClick = { onEquipmentClick(eq) },
                            onBuyClick = { onEquipmentClick(eq) },
                            onRentClick = { onEquipmentClick(eq) },
                            onDonateClick = { onEquipmentClick(eq) },
                            onContactSeller = { onEquipmentClick(eq) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    containerColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 2.dp)
            .testTag("action_${label.replace("\n", "_").lowercase()}")
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        )
    }
}
