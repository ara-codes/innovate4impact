package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ai.GeminiEquipmentService
import com.example.data.models.*
import com.example.data.repository.MedicaRepository
import com.example.ui.components.AiVerifiedBadge
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEquipmentWizardScreen(
    onBackClick: () -> Unit,
    onListingPublished: (EquipmentListing) -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    val totalSteps = 5

    // Form State
    var selectedPreset by remember { mutableStateOf("Oxygen Concentrator (5L)") }
    var title by remember { mutableStateOf("Philips EverFlo 5L Oxygen Concentrator") }
    var category by remember { mutableStateOf(EquipmentCategory.RESPIRATORY) }
    var brand by remember { mutableStateOf("Philips Respironics") }
    var model by remember { mutableStateOf("EverFlo 1020014") }
    var condition by remember { mutableStateOf(EquipmentCondition.LIKE_NEW) }
    var age by remember { mutableStateOf("8 Months") }
    var usageDuration by remember { mutableStateOf("Used for 300 hours during post-COVID recovery") }
    var description by remember { mutableStateOf("Pristine condition oxygen concentrator. Fully sanitized, original power cord and replacement filter included.") }
    
    var offerSale by remember { mutableStateOf(true) }
    var salePriceText by remember { mutableStateOf("32000") }
    
    var offerRent by remember { mutableStateOf(true) }
    var rentDailyText by remember { mutableStateOf("450") }
    var rentWeeklyText by remember { mutableStateOf("2600") }
    var securityDepositText by remember { mutableStateOf("5000") }
    
    var offerDonate by remember { mutableStateOf(false) }
    var locationName by remember { mutableStateOf("Indiranagar, Bengaluru") }

    // AI Analysis State
    var isAnalyzing by remember { mutableStateOf(false) }
    var aiAnalysisResult by remember { mutableStateOf<AiAnalysisResult?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "List Medical Equipment",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Step $currentStep of $totalSteps: ${getStepTitle(currentStep)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 1) currentStep-- else onBackClick()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(0.8f)
                        ) {
                            Text("Previous")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Button(
                        onClick = {
                            if (currentStep == 3) {
                                // Trigger AI Analysis
                                isAnalyzing = true
                                scope.launch {
                                    val res = GeminiEquipmentService.analyzeEquipment(
                                        title = title,
                                        category = category.displayName,
                                        brand = brand,
                                        model = model,
                                        condition = condition,
                                        ageYears = age
                                    )
                                    aiAnalysisResult = res
                                    isAnalyzing = false
                                    currentStep++
                                }
                            } else if (currentStep < totalSteps) {
                                currentStep++
                            } else {
                                // Final Publish
                                val txTypes = mutableListOf<TransactionType>()
                                if (offerDonate) txTypes.add(TransactionType.DONATE)
                                else {
                                    if (offerSale) txTypes.add(TransactionType.BUY)
                                    if (offerRent) txTypes.add(TransactionType.RENT)
                                }
                                if (txTypes.isEmpty()) txTypes.add(TransactionType.BUY)

                                val newListing = EquipmentListing(
                                    id = "eq_${(1000..9999).random()}",
                                    title = title,
                                    category = category,
                                    brand = brand,
                                    model = model,
                                    condition = condition,
                                    age = age,
                                    usageDuration = usageDuration,
                                    description = description,
                                    transactionTypes = txTypes,
                                    salePrice = if (offerDonate) 0.0 else salePriceText.toDoubleOrNull() ?: 5000.0,
                                    rentalPriceDaily = if (offerDonate) null else rentDailyText.toDoubleOrNull(),
                                    rentalPriceWeekly = if (offerDonate) null else rentWeeklyText.toDoubleOrNull(),
                                    securityDeposit = if (offerDonate) null else securityDepositText.toDoubleOrNull(),
                                    locationName = locationName,
                                    distanceKm = 1.4,
                                    aiVerified = true,
                                    aiConfidence = aiAnalysisResult?.confidence ?: 95,
                                    estimatedPriceMin = aiAnalysisResult?.estimatedMinPrice ?: 6000.0,
                                    estimatedPriceMax = aiAnalysisResult?.estimatedMaxPrice ?: 8000.0,
                                    suggestedPrice = aiAnalysisResult?.suggestedListingPrice ?: 7000.0,
                                    sellerId = "u_001",
                                    sellerName = "Aarav Sharma (You)",
                                    sellerRating = 5.0,
                                    createdAt = "Just now"
                                )

                                MedicaRepository.addEquipmentListing(newListing)
                                onListingPublished(newListing)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp)
                            .testTag("wizard_next_btn")
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Running AI Vision...")
                        } else {
                            Text(
                                text = if (currentStep == totalSteps) "Publish to Marketplace" else if (currentStep == 3) "Run AI Verification" else "Continue",
                                fontWeight = FontWeight.Bold
                            )
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 30.dp)
        ) {
            // Linear Progress Indicator
            item {
                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MedicaTealPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // STEP 1: UPLOAD PHOTOS & PRESETS
            if (currentStep == 1) {
                item {
                    Text(
                        text = "1. Equipment Media & Visual Inspection",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Upload high-res photos or select a template for rapid AI scanning.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Preset Quick Pickers
                    Text("Quick Test Equipment Presets:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))

                    val presets = listOf(
                        "Oxygen Concentrator (5L)" to EquipmentCategory.RESPIRATORY,
                        "Ergonomic Lightweight Wheelchair" to EquipmentCategory.MOBILITY,
                        "Electric 3-Function Hospital Bed" to EquipmentCategory.HOSPITAL_EQUIPMENT,
                        "BiPAP / CPAP AutoSet" to EquipmentCategory.RESPIRATORY,
                        "Folding Mobility Walker" to EquipmentCategory.MOBILITY
                    )

                    presets.forEach { (pName, pCat) ->
                        val isSel = selectedPreset == pName
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) MedicaNavyContainer else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isSel) MedicaNavyPrimary else MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPreset = pName
                                    title = pName
                                    category = pCat
                                    when (pName) {
                                        "Oxygen Concentrator (5L)" -> {
                                            brand = "Philips"
                                            model = "EverFlo OPI"
                                            salePriceText = "34000"
                                            rentDailyText = "450"
                                        }
                                        "Ergonomic Lightweight Wheelchair" -> {
                                            brand = "Karma Healthcare"
                                            model = "Ergo 125"
                                            salePriceText = "8500"
                                            rentDailyText = "120"
                                        }
                                        "Electric 3-Function Hospital Bed" -> {
                                            brand = "Paramount Bed"
                                            model = "A5 Series"
                                            salePriceText = "28000"
                                            rentDailyText = "380"
                                        }
                                        "BiPAP / CPAP AutoSet" -> {
                                            brand = "ResMed"
                                            model = "AirSense 10"
                                            salePriceText = "38000"
                                            rentDailyText = "500"
                                        }
                                        "Folding Mobility Walker" -> {
                                            brand = "Vissco"
                                            model = "ZipGlide"
                                            salePriceText = "0"
                                            offerDonate = true
                                        }
                                    }
                                }
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isSel) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (isSel) MedicaNavyPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = pName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Media Card
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.img_equipment_scanner),
                                contentDescription = "Equipment Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.Black.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("4 Images Attached", color = Color.White, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }

            // STEP 2: EQUIPMENT SPECS & DETAILS
            if (currentStep == 2) {
                item {
                    Text(
                        text = "2. Device Details & Condition",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Listing Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = brand,
                            onValueChange = { brand = it },
                            label = { Text("Brand / Manufacturer") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = model,
                            onValueChange = { model = it },
                            label = { Text("Model No.") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("Age (e.g. 6 Months)") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = usageDuration,
                            onValueChange = { usageDuration = it },
                            label = { Text("Usage Hours / History") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Device Physical Condition:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        EquipmentCondition.values().forEach { cond ->
                            val isSel = cond == condition
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) MedicaNavyPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { condition = cond }
                            ) {
                                Text(
                                    text = cond.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description & Inclusions") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        maxLines = 3
                    )
                }
            }

            // STEP 3: TRANSACTION & PRICING
            if (currentStep == 3) {
                item {
                    Text(
                        text = "3. Resale, Rental & Donation Options",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Choose whether to sell, rent, or donate this equipment.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Donate Toggle
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (offerDonate) MedicaGreenContainer else MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, if (offerDonate) MedicaGreen else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = offerDonate,
                                onCheckedChange = {
                                    offerDonate = it
                                    if (it) {
                                        offerSale = false
                                        offerRent = false
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Donate to Patient in Need", fontWeight = FontWeight.Bold)
                                Text("List as 100% free community recovery donation", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                            }
                        }
                    }

                    if (!offerDonate) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // Sell Option
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = offerSale, onCheckedChange = { offerSale = it })
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Offer for One-Time Sale", fontWeight = FontWeight.Bold)
                                }
                                if (offerSale) {
                                    OutlinedTextField(
                                        value = salePriceText,
                                        onValueChange = { salePriceText = it },
                                        label = { Text("Selling Price (₹)") },
                                        leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp)) },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Rent Option
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = offerRent, onCheckedChange = { offerRent = it })
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Offer for Flexible Rental", fontWeight = FontWeight.Bold)
                                }
                                if (offerRent) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        OutlinedTextField(
                                            value = rentDailyText,
                                            onValueChange = { rentDailyText = it },
                                            label = { Text("Daily (₹)") },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        OutlinedTextField(
                                            value = rentWeeklyText,
                                            onValueChange = { rentWeeklyText = it },
                                            label = { Text("Weekly (₹)") },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        OutlinedTextField(
                                            value = securityDepositText,
                                            onValueChange = { securityDepositText = it },
                                            label = { Text("Deposit (₹)") },
                                            modifier = Modifier.weight(1.2f),
                                            singleLine = true
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // STEP 4: AI ANALYSIS & VERIFICATION RESULT
            if (currentStep == 4) {
                item {
                    Text(
                        text = "4. AI Verification & Valuation Report",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Gemini AI has analyzed your listing against biomedical benchmarks.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    aiAnalysisResult?.let { res ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MedicaNavyContainer),
                            border = BorderStroke(1.dp, MedicaTealLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Verified, null, tint = MedicaGreen, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("AI Verification: Passed", fontWeight = FontWeight.Bold, color = MedicaNavyOnContainer)
                                    }
                                    AiVerifiedBadge(confidence = res.confidence)
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = res.conditionAssessment,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MedicaNavyOnContainer,
                                        fontSize = 12.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.8f))
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("AI Estimated Fair Value", style = MaterialTheme.typography.labelSmall)
                                        Text("₹${res.estimatedMinPrice.toInt()} – ₹${res.estimatedMaxPrice.toInt()}", fontWeight = FontWeight.Bold, color = MedicaNavyPrimary)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Suggested Listing Price", style = MaterialTheme.typography.labelSmall)
                                        Text("₹${res.suggestedListingPrice.toInt()}", fontWeight = FontWeight.ExtraBold, color = MedicaTealDark)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                res.checkPoints.forEach { cp ->
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 1.dp)) {
                                        Icon(Icons.Default.Check, null, tint = MedicaGreen, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(cp, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // STEP 5: PREVIEW & PUBLISH
            if (currentStep == 5) {
                item {
                    Text(
                        text = "5. Review & Publish",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Your listing is verified and ready to be discovered by nearby patients.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                AiVerifiedBadge(confidence = aiAnalysisResult?.confidence ?: 96)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Brand: $brand • Condition: ${condition.displayName}", style = MaterialTheme.typography.bodySmall)
                            Text("Location: $locationName", style = MaterialTheme.typography.bodySmall, color = MedicaTealDark)

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            if (offerDonate) {
                                Text("Transaction Mode: Free Community Donation", fontWeight = FontWeight.Bold, color = MedicaGreen)
                            } else {
                                if (offerSale) Text("Sale Price: ₹$salePriceText", fontWeight = FontWeight.Bold, color = MedicaNavyPrimary)
                                if (offerRent) Text("Rental: ₹$rentDailyText/day, ₹$rentWeeklyText/week (Deposit: ₹$securityDepositText)", fontWeight = FontWeight.Bold, color = MedicaTealPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getStepTitle(step: Int): String = when (step) {
    1 -> "Photos & Inspection"
    2 -> "Equipment Specs"
    3 -> "Pricing & Terms"
    4 -> "AI Verification Report"
    5 -> "Preview & Submit"
    else -> "Listing Wizard"
}
