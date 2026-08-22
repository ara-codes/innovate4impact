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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.components.EquipmentItemCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    equipmentList: List<EquipmentListing>,
    currentLocation: String,
    onEquipmentClick: (EquipmentListing) -> Unit,
    onListEquipmentClick: () -> Unit,
    onAiAssistantClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(EquipmentCategory.ALL) }
    var selectedTransactionType by remember { mutableStateOf<TransactionType?>(null) }
    var selectedCondition by remember { mutableStateOf<EquipmentCondition?>(null) }
    var maxDistanceKm by remember { mutableStateOf(25.0) }
    var sortBy by remember { mutableStateOf("AI Recommended") }
    var showFilterSheet by remember { mutableStateOf(false) }

    val filteredListings = remember(
        equipmentList, searchQuery, selectedCategory, selectedTransactionType, selectedCondition, maxDistanceKm, sortBy
    ) {
        var list = equipmentList.filter { item ->
            val matchesQuery = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.brand.contains(searchQuery, ignoreCase = true) ||
                    item.category.displayName.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == EquipmentCategory.ALL || item.category == selectedCategory

            val matchesTransaction = selectedTransactionType == null || item.transactionTypes.contains(selectedTransactionType)

            val matchesCondition = selectedCondition == null || item.condition == selectedCondition

            val matchesDistance = item.distanceKm <= maxDistanceKm

            matchesQuery && matchesCategory && matchesTransaction && matchesCondition && matchesDistance
        }

        when (sortBy) {
            "Distance" -> list.sortedBy { it.distanceKm }
            "Price: Low to High" -> list.sortedBy { it.salePrice ?: it.rentalPriceDaily ?: 0.0 }
            "Price: High to Low" -> list.sortedByDescending { it.salePrice ?: it.rentalPriceDaily ?: 0.0 }
            "Recently Listed" -> list // already in order
            else -> list.sortedByDescending { it.aiConfidence }
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onListEquipmentClick,
                containerColor = MedicaNavyPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("List Equipment", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .testTag("list_equipment_fab")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .testTag("marketplace_screen"),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Medical Equipment Marketplace",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 19.sp
                                )
                            )
                            Text(
                                text = "Buy, sell, rent or donate verified medical equipment near you",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        IconButton(
                            onClick = onAiAssistantClick,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MedicaNavyContainer)
                                .size(36.dp)
                                .testTag("marketplace_ai_assistant_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Valuation",
                                tint = MedicaNavyPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search wheelchair, oxygen concentrator, hospital bed...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("equipment_search_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Transaction Type Tabs (All, Buy, Rent, Donate)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val isAll = selectedTransactionType == null
                        FilterChip(
                            selected = isAll,
                            onClick = { selectedTransactionType = null },
                            label = { Text("All Listings (${equipmentList.size})", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MedicaNavyPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        TransactionType.values().forEach { tType ->
                            val isSel = selectedTransactionType == tType
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedTransactionType = if (isSel) null else tType },
                                label = { Text(tType.displayName, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = when (tType) {
                                        TransactionType.DONATE -> MedicaGreen
                                        TransactionType.RENT -> MedicaTealPrimary
                                        TransactionType.BUY -> MedicaNavyPrimary
                                    },
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Categories Horizontal Carousel
            item {
                Column(modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(EquipmentCategory.values()) { cat ->
                            val isSel = cat == selectedCategory
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSel) MedicaNavyPrimary else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSel) MedicaNavyPrimary else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { selectedCategory = cat }
                            ) {
                                Text(
                                    text = cat.displayName,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Distance & Sort Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filteredListings.size} Equipment Found",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Quick Distance Filter
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    maxDistanceKm = when (maxDistanceKm) {
                                        2.5 -> 5.0
                                        5.0 -> 10.0
                                        10.0 -> 25.0
                                        else -> 2.5
                                    }
                                }
                        ) {
                            Text(
                                text = "Radius: ${if (maxDistanceKm >= 25.0) "All" else "${maxDistanceKm.toInt()} km"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Quick Sort Pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    sortBy = when (sortBy) {
                                        "AI Recommended" -> "Distance"
                                        "Distance" -> "Price: Low to High"
                                        "Price: Low to High" -> "Price: High to Low"
                                        else -> "AI Recommended"
                                    }
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Sort, null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = sortBy.split(":").first(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Results List
            if (filteredListings.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No equipment matches your filters",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Try clearing filters or expanding your search radius",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                searchQuery = ""
                                selectedCategory = EquipmentCategory.ALL
                                selectedTransactionType = null
                                selectedCondition = null
                                maxDistanceKm = 25.0
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicaNavyPrimary)
                        ) {
                            Text("Reset All Filters")
                        }
                    }
                }
            } else {
                items(filteredListings, key = { it.id }) { item ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        EquipmentItemCard(
                            equipment = item,
                            onCardClick = { onEquipmentClick(item) },
                            onBuyClick = { onEquipmentClick(item) },
                            onRentClick = { onEquipmentClick(item) },
                            onDonateClick = { onEquipmentClick(item) },
                            onContactSeller = { onEquipmentClick(item) }
                        )
                    }
                }
            }
        }
    }
}
