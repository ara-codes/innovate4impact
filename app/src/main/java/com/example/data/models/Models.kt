package com.example.data.models

enum class UserRole(val displayName: String, val badgeColorHex: Long) {
    PATIENT("Patient / User", 0xFF0F3D64),
    EQUIPMENT_SELLER("Equipment Owner / Seller", 0xFF008080),
    HEALTHCARE_PROVIDER("Healthcare Provider", 0xFF0284C7),
    ADMIN("Admin / Moderator", 0xFF7C3AED)
}

data class UserProfile(
    val id: String = "u_001",
    val name: String = "Dr. Vikram Sethi",
    val email: String = "vikram.sethi@gmail.com",
    val phone: String = "+91 98450 12345",
    val role: UserRole = UserRole.PATIENT,
    val location: String = "Koramangala, Bengaluru",
    val avatarUrl: String = "",
    val isVerified: Boolean = true
)

enum class EquipmentCategory(val displayName: String, val iconName: String) {
    ALL("All", "Category"),
    MOBILITY("Mobility", "Accessible"),
    RESPIRATORY("Respiratory", "Air"),
    HOME_CARE("Home Care", "Home"),
    HOSPITAL_EQUIPMENT("Hospital Beds", "Bed"),
    MONITORING("Monitoring", "Speed"),
    REHABILITATION("Rehabilitation", "FitnessCenter"),
    OTHER("Other", "MoreHoriz")
}

enum class TransactionType(val displayName: String) {
    BUY("Buy"),
    RENT("Rent"),
    DONATE("Donate")
}

enum class EquipmentCondition(val displayName: String) {
    NEW("Brand New"),
    LIKE_NEW("Like New"),
    GOOD("Good Condition"),
    USED("Fair / Used")
}

data class EquipmentListing(
    val id: String,
    val title: String,
    val category: EquipmentCategory,
    val brand: String,
    val model: String,
    val condition: EquipmentCondition,
    val age: String,
    val usageDuration: String,
    val description: String,
    val imageUrl: String = "",
    val drawableResName: String = "img_hero_banner",
    val transactionTypes: List<TransactionType> = listOf(TransactionType.BUY, TransactionType.RENT),
    val salePrice: Double? = null,
    val rentalPriceDaily: Double? = null,
    val rentalPriceWeekly: Double? = null,
    val securityDeposit: Double? = null,
    val locationName: String = "Koramangala, Bengaluru",
    val distanceKm: Double = 2.4,
    val aiVerified: Boolean = true,
    val aiConfidence: Int = 94,
    val estimatedPriceMin: Double = 8000.0,
    val estimatedPriceMax: Double = 10000.0,
    val suggestedPrice: Double = 9000.0,
    val sellerId: String = "s_101",
    val sellerName: String = "Aarav Sharma",
    val sellerRating: Double = 4.8,
    val isAvailable: Boolean = true,
    val createdAt: String = "2 hours ago",
    val isReported: Boolean = false
)

data class AiAnalysisResult(
    val equipmentIdentified: String,
    val confidence: Int,
    val conditionAssessment: String,
    val estimatedMinPrice: Double,
    val estimatedMaxPrice: Double,
    val suggestedListingPrice: Double,
    val isVerified: Boolean,
    val checkPoints: List<String>,
    val safetyNotice: String = "AI marketplace trust estimate. Review details before purchasing."
)

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val qualification: String,
    val experienceYears: Int,
    val rating: Double,
    val reviewsCount: Int,
    val consultationFee: Double,
    val hospitalName: String,
    val location: String,
    val distanceKm: Double,
    val nextAvailableSlot: String,
    val consultationModes: List<String> = listOf("Video Consultation", "In-Clinic Visit")
)

data class DoctorAppointment(
    val id: String,
    val doctor: Doctor,
    val patientName: String,
    val date: String,
    val timeSlot: String,
    val consultationType: String,
    val status: String = "Confirmed",
    val feePaid: Double
)

data class Medicine(
    val id: String,
    val name: String,
    val brand: String,
    val category: String,
    val dosageForm: String, // Tablet, Syrup, Inhaler
    val price: Double,
    val originalPrice: Double,
    val discountPercent: Int = 15,
    val requiresPrescription: Boolean = false,
    val inStock: Boolean = true,
    val description: String
)

data class CartItem(
    val medicine: Medicine,
    val quantity: Int = 1
)

data class MedicineOrder(
    val id: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val deliveryAddress: String,
    val status: String = "Processing",
    val orderedAt: String = "Today, 10:30 AM",
    val trackingStage: Int = 2 // 1: Placed, 2: Pharmacy Packed, 3: Out for Delivery, 4: Delivered
)

data class LabTest(
    val id: String,
    val testName: String,
    val labName: String,
    val category: String,
    val price: Double,
    val originalPrice: Double,
    val turnaroundHours: Int,
    val homeCollection: Boolean = true,
    val rating: Double,
    val includedParameters: List<String>,
    val preparation: String
)

data class LabBooking(
    val id: String,
    val test: LabTest,
    val patientName: String,
    val date: String,
    val timeSlot: String,
    val homeCollection: Boolean,
    val address: String,
    val totalAmount: Double,
    val status: String = "Confirmed"
)

data class RentalBooking(
    val id: String,
    val equipment: EquipmentListing,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val dailyRate: Double,
    val securityDeposit: Double,
    val totalAmount: Double,
    val status: String = "Active" // Pending, Active, Completed, Cancelled
)

data class DonationRequest(
    val id: String,
    val equipment: EquipmentListing,
    val requesterName: String,
    val requesterPhone: String,
    val purposeDescription: String,
    val urgentLevel: String = "High",
    val requestDate: String = "Today",
    val status: String = "Pending Review" // Pending Review, Approved, Dispatched, Delivered
)

data class ChatMessage(
    val id: String,
    val equipmentId: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isFromUser: Boolean
)

data class ListingReport(
    val id: String,
    val equipmentId: String,
    val equipmentTitle: String,
    val reason: String,
    val reporterName: String,
    val timestamp: String,
    val status: String = "Pending Moderator Review"
)

data class PlatformStats(
    val totalUsers: Int = 12450,
    val activeListings: Int = 890,
    val equipmentRentals: Int = 4120,
    val equipmentSales: Int = 3280,
    val donationsCompleted: Int = 1560,
    val doctorAppointments: Int = 9840,
    val wastePreventedKg: Double = 4280.0,
    val savingsGeneratedInr: Double = 8450000.0,
    val pendingVerifications: Int = 14,
    val reportedListings: Int = 3
)
