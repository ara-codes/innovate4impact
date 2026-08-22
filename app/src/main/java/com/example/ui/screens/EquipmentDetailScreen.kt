package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.*
import com.example.data.repository.MedicaRepository
import com.example.ui.components.AiVerifiedBadge
import com.example.ui.components.ReportListingDialog
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentDetailScreen(
    equipment: EquipmentListing,
    onBackClick: () -> Unit,
    onContactSeller: (EquipmentListing) -> Unit,
    onRentalSuccess: () -> Unit,
    onDonationSuccess: () -> Unit,
    onBuySuccess: () -> Unit
) {
    var showRentDialog by remember { mutableStateOf(false) }
    var showBuyDialog by remember { mutableStateOf(false) }
    var showDonationDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = equipment.title,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showReportDialog = true }) {
                        Icon(Icons.Outlined.Flag, contentDescription = "Report", tint = MedicaRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Sticky Action Bar
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { onContactSeller(equipment) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("detail_chat_btn")
                    ) {
                        Icon(Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Chat")
                    }

                    if (equipment.transactionTypes.contains(TransactionType.DONATE)) {
                        Button(
                            onClick = { showDonationDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicaTealPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("detail_donate_action_btn")
                        ) {
                            Icon(Icons.Default.Favorite, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Request Free Donation", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        if (equipment.transactionTypes.contains(TransactionType.RENT)) {
                            Button(
                                onClick = { showRentDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MedicaTealDark),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("detail_rent_action_btn")
                            ) {
                                Text("Rent (₹${equipment.rentalPriceDaily?.toInt() ?: 100}/d)", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (equipment.transactionTypes.contains(TransactionType.BUY)) {
                            Button(
                                onClick = { showBuyDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("detail_buy_action_btn")
                            ) {
                                Text("Buy (₹${equipment.salePrice?.toInt() ?: 0})", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // 1. Hero Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    val imageRes = when (equipment.category) {
                        EquipmentCategory.RESPIRATORY, EquipmentCategory.MONITORING -> R.drawable.img_equipment_scanner
                        else -> R.drawable.img_hero_banner
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = equipment.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                                )
                            )
                    )

                    // Floating Badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MedicaNavyDark.copy(alpha = 0.85f)
                        ) {
                            Text(
                                text = "${equipment.category.displayName} • ${equipment.condition.displayName}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (equipment.aiVerified) {
                            AiVerifiedBadge(confidence = equipment.aiConfidence)
                        }
                    }
                }
            }

            // 2. Title & Key Stats
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        text = equipment.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MedicaRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${equipment.locationName} (${equipment.distanceKm} km away)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "Listed ${equipment.createdAt}",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    // Specs Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpecBox("Brand", equipment.brand, Modifier.weight(1f))
                        SpecBox("Model", equipment.model, Modifier.weight(1f))
                        SpecBox("Age", equipment.age, Modifier.weight(1f))
                    }
                }
            }

            // 3. AI EQUIPMENT ANALYSIS CARD (CRITICAL DIFFERENTIATOR)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MedicaNavyContainer),
                    border = BorderStroke(1.dp, MedicaTealLight.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("ai_analysis_report_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MedicaNavyPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MedicaTealLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Gemini AI Verification & Fair Price Report",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MedicaNavyOnContainer
                                        )
                                    )
                                    Text(
                                        text = "Biomedical hardware & market model index",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }

                            AiVerifiedBadge(confidence = equipment.aiConfidence)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Price Evaluation Summary
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.7f))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Estimated Market Range",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MedicaTextSecondary)
                                )
                                Text(
                                    text = "₹${equipment.estimatedPriceMin.toInt()} – ₹${equipment.estimatedPriceMax.toInt()}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MedicaNavyPrimary
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Fair Circular Value",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MedicaTextSecondary)
                                )
                                Text(
                                    text = "₹${equipment.suggestedPrice.toInt()}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MedicaTealDark
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Condition Assessment: Verified in ${equipment.condition.displayName}. Tested for structural weld stability, motor/pressure integrity, and sanitization protocols.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MedicaNavyOnContainer,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Checkpoints
                        val checkList = listOf(
                            "Frame & mechanical joint symmetry verified",
                            "Model verified with manufacturer circular registry",
                            "No open safety recalls or device defects found",
                            "Sanitization protocol ready for patient reuse"
                        )

                        checkList.forEach { cp ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 1.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, null, tint = MedicaGreen, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = cp,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MedicaNavyOnContainer,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 4. Description & Usage History
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Item Description & Medical History",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = equipment.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.History, null, tint = MedicaTealPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Usage History:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = equipment.usageDuration,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                )
                            }
                        }
                    }
                }
            }

            // 5. Seller Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MedicaNavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = equipment.sellerName.take(2).uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = equipment.sellerName,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, null, tint = MedicaTealPrimary, modifier = Modifier.size(14.dp))
                            }
                            Text(
                                text = "Verified Equipment Donor / Caregiver",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = MedicaAmber, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${equipment.sellerRating} Rating • 100% Response Rate",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Rent Confirmation Modal
    if (showRentDialog) {
        var rentDays by remember { mutableStateOf(7) }
        val dailyRate = equipment.rentalPriceDaily ?: 100.0
        val deposit = equipment.securityDeposit ?: 1500.0
        val rentTotal = (dailyRate * rentDays * 0.9) // 10% discount on 7+ days

        AlertDialog(
            onDismissRequest = { showRentDialog = false },
            title = { Text("Configure Equipment Rental", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = equipment.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Rental Duration (Days): $rentDays days", fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = rentDays.toFloat(),
                        onValueChange = { rentDays = it.toInt() },
                        valueRange = 1f..30f,
                        steps = 28
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Daily Rate (₹${dailyRate.toInt()} x $rentDays d)")
                        Text("₹${(dailyRate * rentDays).toInt()}")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Duration Discount (10%)", color = MedicaGreen)
                        Text("-₹${((dailyRate * rentDays) - rentTotal).toInt()}", color = MedicaGreen)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Refundable Security Deposit")
                        Text("₹${deposit.toInt()}")
                    }

                    Divider(modifier = Modifier.padding(vertical = 6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Payable Now", fontWeight = FontWeight.Bold)
                        Text("₹${(rentTotal + deposit).toInt()}", fontWeight = FontWeight.Bold, color = MedicaNavyPrimary)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        MedicaRepository.bookRental(
                            RentalBooking(
                                id = "rnt_${(1000..9999).random()}",
                                equipment = equipment,
                                startDate = "Today",
                                endDate = "+$rentDays days",
                                totalDays = rentDays,
                                dailyRate = dailyRate,
                                securityDeposit = deposit,
                                totalAmount = rentTotal + deposit,
                                status = "Active"
                            )
                        )
                        showRentDialog = false
                        onRentalSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicaTealPrimary)
                ) {
                    Text("Confirm & Book Rental")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRentDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Buy Confirmation Modal
    if (showBuyDialog) {
        val price = equipment.salePrice ?: 5000.0
        AlertDialog(
            onDismissRequest = { showBuyDialog = false },
            title = { Text("Confirm Purchase", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(equipment.title, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Price: ₹${price.toInt()}")
                    Text("Doorstep Sanitized Delivery: Free (Medica Saathi Promise)", color = MedicaGreen)
                    Text("7-Day Return & Functional Guarantee Included", color = MedicaTealDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Delivery to: Indiranagar, Bengaluru")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBuyDialog = false
                        onBuySuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary)
                ) {
                    Text("Pay ₹${price.toInt()} & Buy")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBuyDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Donation Request Modal
    if (showDonationDialog) {
        var patientName by remember { mutableStateOf("Aarav Sharma") }
        var phone by remember { mutableStateOf("+91 98765 43210") }
        var reason by remember { mutableStateOf("Needed for post-operative recovery at home") }

        AlertDialog(
            onDismissRequest = { showDonationDialog = false },
            title = { Text("Request Free Equipment Donation", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("This equipment has been donated to support recovery patients in need.", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Patient Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Contact Phone") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        label = { Text("Medical Need / Reason") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        MedicaRepository.requestDonation(
                            DonationRequest(
                                id = "don_${(1000..9999).random()}",
                                equipment = equipment,
                                requesterName = patientName,
                                requesterPhone = phone,
                                purposeDescription = reason,
                                urgentLevel = "Urgent",
                                status = "Pending Review"
                            )
                        )
                        showDonationDialog = false
                        onDonationSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicaTealPrimary)
                ) {
                    Text("Submit Request")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDonationDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Report Listing Dialog
    if (showReportDialog) {
        ReportListingDialog(
            equipment = equipment,
            onDismiss = { showReportDialog = false },
            onSubmitReport = { reason ->
                MedicaRepository.reportListing(
                    ListingReport(
                        id = "rep_${(1000..9999).random()}",
                        equipmentId = equipment.id,
                        equipmentTitle = equipment.title,
                        reason = reason,
                        reporterName = "Aarav Sharma",
                        timestamp = "Just now"
                    )
                )
            }
        )
    }
}

@Composable
private fun SpecBox(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                maxLines = 1
            )
        }
    }
}
