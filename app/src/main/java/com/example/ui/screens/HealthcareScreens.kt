package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.data.repository.MedicaRepository
import com.example.ui.theme.*

// ==========================================
// 1. DOCTOR APPOINTMENT BOOKING SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsScreen(
    doctors: List<Doctor>,
    onDoctorBooked: (DoctorAppointment) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSpecialty by remember { mutableStateOf("All") }
    var bookingDoctor by remember { mutableStateOf<Doctor?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val specialties = listOf("All", "Cardiologist", "Orthopedic & Joint Specialist", "Pulmonologist & Respiratory Specialist", "General Physician & Diabetologist", "Neurologist", "Pediatrician & Child Health")

    val filteredDoctors = remember(doctors, searchQuery, selectedSpecialty) {
        doctors.filter { doc ->
            val matchesQuery = searchQuery.isBlank() || doc.name.contains(searchQuery, ignoreCase = true) || doc.specialty.contains(searchQuery, ignoreCase = true) || doc.hospitalName.contains(searchQuery, ignoreCase = true)
            val matchesSpec = selectedSpecialty == "All" || doc.specialty.contains(selectedSpecialty.split(" ").first(), ignoreCase = true)
            matchesQuery && matchesSpec
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("doctors_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Consult Verified Doctors",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Book instant video consultations or in-clinic visits with top specialists",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search doctor, specialty, hospital...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Specialty Filter Row
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(specialties) { spec ->
                    val isSel = spec == selectedSpecialty
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSel) MedicaNavyPrimary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, if (isSel) MedicaNavyPrimary else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.clickable { selectedSpecialty = spec }
                    ) {
                        Text(
                            text = spec.split("&").first().trim(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Doctors List
        items(filteredDoctors, key = { it.id }) { doc ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MedicaNavyContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = doc.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MedicaNavyPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = doc.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, null, tint = MedicaTealPrimary, modifier = Modifier.size(14.dp))
                            }
                            Text(
                                text = doc.specialty,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MedicaTealDark,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = "${doc.qualification} • ${doc.experienceYears} yrs experience",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = MedicaAmber, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${doc.rating}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Text(
                                text = "(${doc.reviewsCount})",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 9.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🏥 ${doc.hospitalName} (${doc.distanceKm} km)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Next: ${doc.nextAvailableSlot}",
                            style = MaterialTheme.typography.labelSmall.copy(color = MedicaTealPrimary, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Consultation Fee", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp))
                            Text("₹${doc.consultationFee.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MedicaNavyPrimary))
                        }

                        Button(
                            onClick = { bookingDoctor = doc },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Book Appointment", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Appointment Booking Sheet
    bookingDoctor?.let { doc ->
        var selectedDate by remember { mutableStateOf("Today") }
        var selectedTime by remember { mutableStateOf("04:30 PM") }
        var consultType by remember { mutableStateOf("Video Consultation") }
        var patientName by remember { mutableStateOf("Aarav Sharma") }

        AlertDialog(
            onDismissRequest = { bookingDoctor = null },
            title = { Text("Book Doctor Consultation", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(doc.name, fontWeight = FontWeight.Bold, color = MedicaNavyPrimary)
                    Text("${doc.specialty} • ₹${doc.consultationFee.toInt()}", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Consultation Mode:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Video Consultation", "In-Clinic Visit").forEach { m ->
                            val isSel = consultType == m
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) MedicaNavyPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f).clickable { consultType = m }
                            ) {
                                Text(
                                    text = m,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp
                                    ),
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Select Time Slot:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("02:00 PM", "04:30 PM", "06:15 PM").forEach { slot ->
                            val isSel = selectedTime == slot
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) MedicaTealPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f).clickable { selectedTime = slot }
                            ) {
                                Text(
                                    text = slot,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Patient Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val appointment = DoctorAppointment(
                            id = "apt_${(1000..9999).random()}",
                            doctor = doc,
                            patientName = patientName,
                            date = selectedDate,
                            timeSlot = selectedTime,
                            consultationType = consultType,
                            status = "Confirmed",
                            feePaid = doc.consultationFee
                        )
                        MedicaRepository.bookAppointment(appointment)
                        bookingDoctor = null
                        onDoctorBooked(appointment)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary)
                ) {
                    Text("Confirm Booking")
                }
            },
            dismissButton = {
                TextButton(onClick = { bookingDoctor = null }) { Text("Cancel") }
            }
        )
    }
}

// ==========================================
// 2. MEDICINE ORDERING & PHARMACY SCREEN
// ==========================================
@Composable
fun MedicinesScreen(
    medicines: List<Medicine>,
    cartItems: List<CartItem>,
    onAddToCart: (Medicine) -> Unit,
    onUpdateQuantity: (String, Int) -> Unit,
    onCheckoutSuccess: (MedicineOrder) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var showCartSheet by remember { mutableStateOf(false) }

    val categories = listOf("All", "Pain Relief", "Antibiotics", "Respiratory", "Vitamins & Supplements", "Diabetes Care", "Cardiac Care", "First Aid & Wound Care", "Gastro Care")

    val filteredMedicines = remember(medicines, searchQuery, selectedCategory) {
        medicines.filter { med ->
            val matchesQuery = searchQuery.isBlank() || med.name.contains(searchQuery, ignoreCase = true) || med.brand.contains(searchQuery, ignoreCase = true)
            val matchesCat = selectedCategory == "All" || med.category.contains(selectedCategory.split(" ").first(), ignoreCase = true)
            matchesQuery && matchesCat
        }
    }

    val totalAmount = cartItems.sumOf { it.medicine.price * it.quantity }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("medicines_screen"),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Medicines & Pharmacy",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "100% genuine medicines delivered with digital prescription validation",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search medicine, salt, vitamin...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            // Categories
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSel = cat == selectedCategory
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSel) MedicaTealPrimary else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isSel) MedicaTealPrimary else MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.clickable { selectedCategory = cat }
                        ) {
                            Text(
                                text = cat.split("&").first().trim(),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Medicines List
            items(filteredMedicines, key = { it.id }) { med ->
                val inCartItem = cartItems.find { it.medicine.id == med.id }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = med.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "By ${med.brand} • ${med.dosageForm}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            if (med.requiresPrescription) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MedicaNavyContainer
                                ) {
                                    Text(
                                        text = "Rx Required",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MedicaNavyPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = med.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹${med.price.toInt()}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "₹${med.originalPrice.toInt()}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        textDecoration = TextDecoration.LineThrough,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${med.discountPercent}% OFF",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MedicaGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            if (inCartItem != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MedicaTealContainer)
                                        .padding(horizontal = 4.dp)
                                ) {
                                    IconButton(
                                        onClick = { onUpdateQuantity(med.id, -1) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, null, modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "${inCartItem.quantity}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(
                                        onClick = { onUpdateQuantity(med.id, 1) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                                    }
                                }
                            } else {
                                Button(
                                    onClick = { onAddToCart(med) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MedicaTealPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Add to Cart", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Cart Sticky Bar
        if (cartItems.isNotEmpty()) {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                color = MedicaNavyPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${cartItems.sumOf { it.quantity }} Medicines in Cart",
                            style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Total: ₹${totalAmount.toInt()}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MedicaTealLight, fontWeight = FontWeight.Medium)
                        )
                    }

                    Button(
                        onClick = { showCartSheet = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicaTealLight),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Checkout", color = MedicaNavyDark, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, null, tint = MedicaNavyDark, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }

    // Checkout Dialog
    if (showCartSheet) {
        var address by remember { mutableStateOf("Flat 402, Green Glen Layout, Indiranagar, Bengaluru") }

        AlertDialog(
            onDismissRequest = { showCartSheet = false },
            title = { Text("Confirm Medicine Order", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.quantity}x ${item.medicine.name}", fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Text("₹${(item.medicine.price * item.quantity).toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal")
                        Text("₹${totalAmount.toInt()}")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Express Home Delivery")
                        Text("FREE", color = MedicaGreen, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Payable", fontWeight = FontWeight.Bold)
                        Text("₹${totalAmount.toInt()}", fontWeight = FontWeight.ExtraBold, color = MedicaNavyPrimary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Delivery Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val order = MedicaRepository.placeMedicineOrder(cartItems, totalAmount, address)
                        showCartSheet = false
                        onCheckoutSuccess(order)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary)
                ) {
                    Text("Place Order (₹${totalAmount.toInt()})")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCartSheet = false }) { Text("Cancel") }
            }
        )
    }
}

// ==========================================
// 3. LAB TESTS & DIAGNOSTICS SCREEN
// ==========================================
@Composable
fun LabTestsScreen(
    labTests: List<LabTest>,
    onTestBooked: (LabBooking) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var bookingTest by remember { mutableStateOf<LabTest?>(null) }

    val filteredTests = remember(labTests, searchQuery) {
        labTests.filter { test ->
            searchQuery.isBlank() || test.testName.contains(searchQuery, ignoreCase = true) || test.category.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("lab_tests_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Lab Tests & Diagnostics",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "NABL certified diagnostic centers with free home sample collection",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search full body checkup, blood test, diabetes...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Lab Tests List
        items(filteredTests, key = { it.id }) { test ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE0F2FE)
                        ) {
                            Text(
                                text = test.category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF0369A1),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        if (test.homeCollection) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MedicaGreenContainer
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Home, null, tint = MedicaGreen, modifier = Modifier.size(11.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Free Home Collection", color = Color(0xFF065F46), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = test.testName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    )

                    Text(
                        text = "Conducted by ${test.labName} • Reports in ${test.turnaroundHours} hrs",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Included Parameters Chip list
                    Text(
                        text = "Includes: ${test.includedParameters.take(4).joinToString(", ")}${if (test.includedParameters.size > 4) " + more" else ""}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MedicaTealDark)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${test.price.toInt()}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "₹${test.originalPrice.toInt()}",
                                style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Button(
                            onClick = { bookingTest = test },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Book Test", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Booking Dialog
    bookingTest?.let { test ->
        var patientName by remember { mutableStateOf("Aarav Sharma") }
        var address by remember { mutableStateOf("Indiranagar, Bengaluru") }
        var slot by remember { mutableStateOf("07:30 AM (Fasting Sample)") }

        AlertDialog(
            onDismissRequest = { bookingTest = null },
            title = { Text("Book Lab Health Test", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(test.testName, fontWeight = FontWeight.Bold, color = MedicaNavyPrimary)
                    Text("Lab: ${test.labName} • Total: ₹${test.price.toInt()}", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Preparation: ${test.preparation}", style = MaterialTheme.typography.labelSmall.copy(color = MedicaAmber))

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Patient Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Sample Collection Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val booking = LabBooking(
                            id = "lab_${(1000..9999).random()}",
                            test = test,
                            patientName = patientName,
                            date = "Tomorrow",
                            timeSlot = slot,
                            homeCollection = true,
                            address = address,
                            totalAmount = test.price,
                            status = "Phlebotomist Assigned"
                        )
                        MedicaRepository.bookLabTest(booking)
                        bookingTest = null
                        onTestBooked(booking)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary)
                ) {
                    Text("Confirm Lab Booking")
                }
            },
            dismissButton = {
                TextButton(onClick = { bookingTest = null }) { Text("Cancel") }
            }
        )
    }
}
