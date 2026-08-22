package com.example.ui.screens

import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.EquipmentCategory
import com.example.data.models.EquipmentListing
import com.example.data.models.TransactionType
import com.example.ui.components.AiVerifiedBadge
import com.example.ui.components.CircularEconomyBanner
import com.example.ui.components.EquipmentItemCard
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    equipmentList: List<List<EquipmentListing>>,
    onExploreHealthcare: () -> Unit,
    onExploreMarketplace: () -> Unit,
    onNavigateToDoctors: () -> Unit,
    onNavigateToMedicines: () -> Unit,
    onNavigateToLabs: () -> Unit,
    onEquipmentClick: (EquipmentListing) -> Unit,
    onListEquipmentClick: () -> Unit,
    onAiAssistantClick: () -> Unit
) {
    val flatListings = remember(equipmentList) { equipmentList.flatten() }
    val featuredEquipment = remember(flatListings) { flatListings.take(4) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("landing_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. HERO SECTION
        item {
            Card(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MedicaNavyDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("landing_hero_section")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    // Tagline Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MedicaTealDark,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MedicaTealLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Affordable • Accessible • AI-Verified Healthcare",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Text(
                        text = "Healthcare,\nAll in One Place.",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 28.sp,
                            lineHeight = 34.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Book doctors, order medicines, book lab tests, and buy, rent, sell or donate medical equipment — powered by AI.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFCBD5E1),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hero Mockup Visual with AI Verification Badge
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.img_hero_banner),
                                contentDescription = "Medica Saathi Integrated Health Ecosystem",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Floating AI Verified Pill on Hero
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black.copy(alpha = 0.75f),
                                border = BorderStroke(1.dp, MedicaGreen),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = MedicaGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Column {
                                        Text(
                                            text = "Gemini AI Verified Marketplace",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        )
                                        Text(
                                            text = "Certified Biomedical Condition & Fair Pricing",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color(0xFFA7F3D0),
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // CTA Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onExploreHealthcare,
                            colors = ButtonDefaults.buttonColors(containerColor = MedicaTealLight),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_explore_healthcare_btn")
                        ) {
                            Text(
                                text = "Explore Healthcare",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MedicaNavyDark,
                                    fontSize = 13.sp
                                )
                            )
                        }

                        OutlinedButton(
                            onClick = onExploreMarketplace,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_explore_marketplace_btn")
                        ) {
                            Text(
                                text = "Equipment Hub",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // 2. FOUR SERVICE CARDS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Comprehensive Healthcare Services",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 17.sp
                    )
                )
                Text(
                    text = "Integrated medical care, pharmacy, diagnostics and circular equipment",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ServiceCard(
                        title = "Doctor\nConsultation",
                        subtitle = "Verified Specialists",
                        icon = Icons.Default.MedicalServices,
                        badgeText = "Video & In-Clinic",
                        containerColor = MedicaNavyContainer,
                        iconTint = MedicaNavyPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToDoctors
                    )

                    ServiceCard(
                        title = "Medicines &\nPharmacy",
                        subtitle = "Genuine & Fast Delivery",
                        icon = Icons.Default.LocalPharmacy,
                        badgeText = "Up to 20% OFF",
                        containerColor = MedicaTealContainer,
                        iconTint = MedicaTealDark,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToMedicines
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ServiceCard(
                        title = "Lab Tests &\nDiagnostics",
                        subtitle = "Free Home Collection",
                        icon = Icons.Default.Biotech,
                        badgeText = "64+ Parameters",
                        containerColor = Color(0xFFE0F2FE),
                        iconTint = Color(0xFF0369A1),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToLabs
                    )

                    ServiceCard(
                        title = "Medical\nEquipment",
                        subtitle = "Circular Marketplace",
                        icon = Icons.Default.Autorenew,
                        badgeText = "AI-Verified Hub",
                        containerColor = Color(0xFFFEF3C7),
                        iconTint = Color(0xFFB45309),
                        modifier = Modifier.weight(1f),
                        onClick = onExploreMarketplace
                    )
                }
            }
        }

        // 3. CIRCULAR ECONOMY SHOWCASE
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                CircularEconomyBanner(onExploreClick = onExploreMarketplace)
            }
        }

        // 4. POPULAR EQUIPMENT CAROUSEL
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
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
                            text = "Featured Equipment",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "Wheelchairs, oxygen concentrators, hospital beds & more",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    TextButton(onClick = onExploreMarketplace) {
                        Text("Browse All", fontWeight = FontWeight.Bold, color = MedicaTealPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Equipment Showcase Categories Pills
                val quickPills = listOf(
                    "♿ Wheelchair", "💨 Oxygen Concentrator", "🛏️ Hospital Bed",
                    "🚶 Walker", "🩼 Crutches", "🫁 Nebulizer", "📊 BP Monitor"
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quickPills) { pill ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.clickable { onExploreMarketplace() }
                        ) {
                            Text(
                                text = pill,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Featured Listing Cards
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    featuredEquipment.forEach { eq ->
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

        // 5. WHY MEDICA SAATHI IMPACT STATS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "Why Medica Saathi?",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "Building a sustainable, affordable healthcare circular economy in India",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ImpactStatCard(
                        title = "Lower Costs",
                        value = "60–75%",
                        subtitle = "Savings on verified used/rental equipment",
                        icon = Icons.Default.Savings,
                        containerColor = MedicaGreenContainer,
                        contentColor = Color(0xFF065F46),
                        modifier = Modifier.weight(1f)
                    )

                    ImpactStatCard(
                        title = "Less Waste",
                        value = "4,280 kg",
                        subtitle = "Medical hardware salvaged & reused",
                        icon = Icons.Default.Recycling,
                        containerColor = MedicaTealContainer,
                        contentColor = MedicaTealOnContainer,
                        modifier = Modifier.weight(1f)
                    )

                    ImpactStatCard(
                        title = "Accessibility",
                        value = "100%",
                        subtitle = "Integrated healthcare in one platform",
                        icon = Icons.Default.HealthAndSafety,
                        containerColor = MedicaNavyContainer,
                        contentColor = MedicaNavyPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // AI Price Assistant Banner Trigger
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MedicaTealLight.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAiAssistantClick() }
                        .testTag("landing_ai_price_assistant_card")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MedicaTealPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Have Unused Equipment at Home?",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Try our AI Price & Verification Assistant to evaluate fair resale, rental, or donation value instantly.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MedicaTealPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badgeText: String,
    containerColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("service_card_${title.replace("\n", "_").lowercase()}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = iconTint,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 13.sp,
                    lineHeight = 16.sp
                )
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF475569),
                    fontSize = 10.sp
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ImpactStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = contentColor,
                    fontSize = 15.sp
                )
            )

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    fontSize = 11.sp
                )
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = contentColor.copy(alpha = 0.8f),
                    fontSize = 9.sp,
                    lineHeight = 11.sp
                ),
                maxLines = 2
            )
        }
    }
}
