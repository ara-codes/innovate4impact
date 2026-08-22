package com.example.data.repository

import com.example.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object MedicaRepository {

    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "u_001",
            name = "Aarav Sharma",
            email = "aarav.sharma@medicasaathi.in",
            phone = "+91 98765 43210",
            role = UserRole.PATIENT,
            location = "Indiranagar, Bengaluru",
            isVerified = true
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    fun updateRole(newRole: UserRole) {
        _currentUser.value = _currentUser.value.copy(role = newRole)
    }

    fun updateLocation(newLocation: String) {
        _currentUser.value = _currentUser.value.copy(location = newLocation)
    }

    // Equipment Listings Data Flow
    private val initialListings = listOf(
        EquipmentListing(
            id = "eq_001",
            title = "Karma Ergonomic Lightweight Wheelchair",
            category = EquipmentCategory.MOBILITY,
            brand = "Karma Healthcare",
            model = "Ergo 125",
            condition = EquipmentCondition.LIKE_NEW,
            age = "6 Months",
            usageDuration = "Used for 3 months post-surgery",
            description = "Ergonomic pressure-relief seat with flip-back armrests and quick-release rear wheels. Perfectly maintained, thoroughly sanitized, and inspected by biomedical technician.",
            drawableResName = "img_hero_banner",
            transactionTypes = listOf(TransactionType.BUY, TransactionType.RENT),
            salePrice = 8500.0,
            rentalPriceDaily = 120.0,
            rentalPriceWeekly = 750.0,
            securityDeposit = 2000.0,
            locationName = "Indiranagar, Bengaluru",
            distanceKm = 1.8,
            aiVerified = true,
            aiConfidence = 96,
            estimatedPriceMin = 7800.0,
            estimatedPriceMax = 9400.0,
            suggestedPrice = 8500.0,
            sellerId = "s_201",
            sellerName = "Dr. Rajesh Kulkarni",
            sellerRating = 4.9,
            createdAt = "1 hr ago"
        ),
        EquipmentListing(
            id = "eq_002",
            title = "Philips Respironics EverFlo 5L Oxygen Concentrator",
            category = EquipmentCategory.RESPIRATORY,
            brand = "Philips",
            model = "EverFlo OPI",
            condition = EquipmentCondition.LIKE_NEW,
            age = "1 Year",
            usageDuration = "480 runtime hours",
            description = "High purity 93% ± 3% medical grade oxygen concentrator. Equipped with Oxygen Percentage Indicator (OPI), fresh filters, nasal cannula, and humidifier bottle.",
            drawableResName = "img_equipment_scanner",
            transactionTypes = listOf(TransactionType.BUY, TransactionType.RENT),
            salePrice = 34500.0,
            rentalPriceDaily = 450.0,
            rentalPriceWeekly = 2800.0,
            securityDeposit = 6000.0,
            locationName = "Koramangala, Bengaluru",
            distanceKm = 3.2,
            aiVerified = true,
            aiConfidence = 98,
            estimatedPriceMin = 32000.0,
            estimatedPriceMax = 37000.0,
            suggestedPrice = 34500.0,
            sellerId = "s_202",
            sellerName = "Sneha Hegde",
            sellerRating = 4.8,
            createdAt = "3 hrs ago"
        ),
        EquipmentListing(
            id = "eq_003",
            title = "Electric 3-Function ICU Hospital Bed with Remote",
            category = EquipmentCategory.HOSPITAL_EQUIPMENT,
            brand = "Paramount Bed",
            model = "A5 Series Motorized",
            condition = EquipmentCondition.GOOD,
            age = "1.5 Years",
            usageDuration = "Home patient recovery for 8 months",
            description = "Smooth motorized backrest, knee-rest, and height adjustment. Includes fold-down aluminum side rails, waterproof medical mattress, and IV pole attachment.",
            drawableResName = "img_hero_banner",
            transactionTypes = listOf(TransactionType.BUY, TransactionType.RENT),
            salePrice = 28000.0,
            rentalPriceDaily = 380.0,
            rentalPriceWeekly = 2400.0,
            securityDeposit = 5000.0,
            locationName = "HSR Layout, Bengaluru",
            distanceKm = 4.5,
            aiVerified = true,
            aiConfidence = 93,
            estimatedPriceMin = 26000.0,
            estimatedPriceMax = 31000.0,
            suggestedPrice = 28000.0,
            sellerId = "s_203",
            sellerName = "Vikram Menon",
            sellerRating = 4.7,
            createdAt = "5 hrs ago"
        ),
        EquipmentListing(
            id = "eq_004",
            title = "Vissco Folding Mobility Walker with Wheels",
            category = EquipmentCategory.MOBILITY,
            brand = "Vissco",
            model = "Zip-Glide 0902",
            condition = EquipmentCondition.LIKE_NEW,
            age = "4 Months",
            usageDuration = "Used indoors for 3 weeks",
            description = "Height adjustable reciprocal walker with dual front rubber wheels. Light aluminum frame folds with single push button. Donated for elderly patients in need.",
            drawableResName = "img_hero_banner",
            transactionTypes = listOf(TransactionType.DONATE),
            salePrice = 0.0,
            rentalPriceDaily = 0.0,
            securityDeposit = 0.0,
            locationName = "Jayanagar, Bengaluru",
            distanceKm = 2.9,
            aiVerified = true,
            aiConfidence = 97,
            estimatedPriceMin = 0.0,
            estimatedPriceMax = 0.0,
            suggestedPrice = 0.0,
            sellerId = "s_204",
            sellerName = "Meera Nambiar (Donation Care)",
            sellerRating = 5.0,
            createdAt = "Just now"
        ),
        EquipmentListing(
            id = "eq_005",
            title = "Omron Ultra-Quiet Compressor Nebulizer",
            category = EquipmentCategory.RESPIRATORY,
            brand = "Omron",
            model = "NE-C28P",
            condition = EquipmentCondition.LIKE_NEW,
            age = "5 Months",
            usageDuration = "Used sparingly during monsoon",
            description = "High efficiency Virtual Valve Technology (V.V.T.) ensures minimal medication waste. Complete with adult and child silicone masks and extra air filters.",
            drawableResName = "img_equipment_scanner",
            transactionTypes = listOf(TransactionType.BUY, TransactionType.RENT),
            salePrice = 1450.0,
            rentalPriceDaily = 40.0,
            rentalPriceWeekly = 220.0,
            securityDeposit = 500.0,
            locationName = "Whitefield, Bengaluru",
            distanceKm = 6.1,
            aiVerified = true,
            aiConfidence = 95,
            estimatedPriceMin = 1300.0,
            estimatedPriceMax = 1700.0,
            suggestedPrice = 1450.0,
            sellerId = "s_205",
            sellerName = "Pooja Deshmukh",
            sellerRating = 4.9,
            createdAt = "Yesterday"
        ),
        EquipmentListing(
            id = "eq_006",
            title = "ResMed AirSense 10 AutoSet BiPAP / CPAP Machine",
            category = EquipmentCategory.RESPIRATORY,
            brand = "ResMed",
            model = "AirSense 10 3G",
            condition = EquipmentCondition.GOOD,
            age = "2 Years",
            usageDuration = "650 hours, fully serviced",
            description = "Premium auto-adjusting pressure device with built-in HumidAir heated humidifier, ClimateLineAir heated tubing, and AirFit N20 nasal mask. Full biomedical data report included.",
            drawableResName = "img_equipment_scanner",
            transactionTypes = listOf(TransactionType.BUY, TransactionType.RENT),
            salePrice = 38000.0,
            rentalPriceDaily = 500.0,
            rentalPriceWeekly = 3200.0,
            securityDeposit = 7000.0,
            locationName = "Malleshwaram, Bengaluru",
            distanceKm = 5.3,
            aiVerified = true,
            aiConfidence = 92,
            estimatedPriceMin = 36000.0,
            estimatedPriceMax = 42000.0,
            suggestedPrice = 38000.0,
            sellerId = "s_206",
            sellerName = "Kavita Reddy",
            sellerRating = 4.6,
            createdAt = "1 day ago"
        ),
        EquipmentListing(
            id = "eq_007",
            title = "Medical Anti-Decubitus Alternating Pressure Air Mattress",
            category = EquipmentCategory.HOME_CARE,
            brand = "Narang Medical",
            model = "Bubble Air Cell 880",
            condition = EquipmentCondition.LIKE_NEW,
            age = "3 Months",
            usageDuration = "Used for 1 month",
            description = "Pressure sore relief mattress with low-noise automated motor pump and adjustable pressure dial. Donated to assist bedridden recovery patients.",
            drawableResName = "img_hero_banner",
            transactionTypes = listOf(TransactionType.DONATE),
            salePrice = 0.0,
            rentalPriceDaily = 0.0,
            securityDeposit = 0.0,
            locationName = "BTM Layout, Bengaluru",
            distanceKm = 3.8,
            aiVerified = true,
            aiConfidence = 96,
            estimatedPriceMin = 0.0,
            estimatedPriceMax = 0.0,
            suggestedPrice = 0.0,
            sellerId = "s_207",
            sellerName = "Ananya Trust Health",
            sellerRating = 5.0,
            createdAt = "2 days ago"
        ),
        EquipmentListing(
            id = "eq_008",
            title = "Universal Underarm Aluminum Crutches (Pair)",
            category = EquipmentCategory.REHABILITATION,
            brand = "Tynor Orthotics",
            model = "L-01 Adult",
            condition = EquipmentCondition.LIKE_NEW,
            age = "2 Months",
            usageDuration = "Used 2 weeks for ankle sprain",
            description = "High strength anodized aluminum alloy tubes with soft underarm cushioning and non-slip rubber tips. Dual push-button height adjustments.",
            drawableResName = "img_hero_banner",
            transactionTypes = listOf(TransactionType.BUY, TransactionType.DONATE),
            salePrice = 550.0,
            rentalPriceDaily = 20.0,
            rentalPriceWeekly = 100.0,
            securityDeposit = 200.0,
            locationName = "Indiranagar, Bengaluru",
            distanceKm = 1.2,
            aiVerified = true,
            aiConfidence = 99,
            estimatedPriceMin = 500.0,
            estimatedPriceMax = 700.0,
            suggestedPrice = 550.0,
            sellerId = "s_208",
            sellerName = "Rohan Verma",
            sellerRating = 4.8,
            createdAt = "3 days ago"
        ),
        EquipmentListing(
            id = "eq_009",
            title = "Omron HEM-7120 Blood Pressure Monitor with Smart Cuff",
            category = EquipmentCategory.MONITORING,
            brand = "Omron",
            model = "HEM-7120 Intellisense",
            condition = EquipmentCondition.LIKE_NEW,
            age = "6 Months",
            usageDuration = "Rarely used",
            description = "Accurate oscillometric BP and pulse rate measurement with hypertension indicator and body movement detector. Calibrated and tested.",
            drawableResName = "img_equipment_scanner",
            transactionTypes = listOf(TransactionType.BUY),
            salePrice = 1350.0,
            rentalPriceDaily = 30.0,
            rentalPriceWeekly = 150.0,
            securityDeposit = 400.0,
            locationName = "MG Road, Bengaluru",
            distanceKm = 2.1,
            aiVerified = true,
            aiConfidence = 97,
            estimatedPriceMin = 1200.0,
            estimatedPriceMax = 1550.0,
            suggestedPrice = 1350.0,
            sellerId = "s_209",
            sellerName = "Prakash Joshi",
            sellerRating = 4.9,
            createdAt = "4 days ago"
        ),
        EquipmentListing(
            id = "eq_010",
            title = "Suction Apparatus Machine Portable 15L/min",
            category = EquipmentCategory.HOME_CARE,
            brand = "Apex Medical",
            model = "VacPro Care",
            condition = EquipmentCondition.GOOD,
            age = "1 Year",
            usageDuration = "Used post-tracheostomy care",
            description = "Compact oil-free piston pump suction machine with autoclavable collection jar and bacterial overflow filter. Ideal for tracheostomy home care.",
            drawableResName = "img_equipment_scanner",
            transactionTypes = listOf(TransactionType.BUY, TransactionType.RENT),
            salePrice = 6200.0,
            rentalPriceDaily = 110.0,
            rentalPriceWeekly = 650.0,
            securityDeposit = 1500.0,
            locationName = "Rajajinagar, Bengaluru",
            distanceKm = 7.4,
            aiVerified = true,
            aiConfidence = 91,
            estimatedPriceMin = 5800.0,
            estimatedPriceMax = 7200.0,
            suggestedPrice = 6200.0,
            sellerId = "s_210",
            sellerName = "Dr. Anita Rao",
            sellerRating = 4.8,
            createdAt = "5 days ago"
        )
    )

    private val _equipmentListings = MutableStateFlow(initialListings)
    val equipmentListings: StateFlow<List<EquipmentListing>> = _equipmentListings.asStateFlow()

    fun addEquipmentListing(listing: EquipmentListing) {
        _equipmentListings.value = listOf(listing) + _equipmentListings.value
        _platformStats.value = _platformStats.value.copy(
            activeListings = _platformStats.value.activeListings + 1
        )
    }

    // Doctors Data
    private val doctorsList = listOf(
        Doctor(
            id = "doc_001",
            name = "Dr. Nandita Sen",
            specialty = "Cardiologist",
            qualification = "MD, DM (Cardiology) - AIIMS",
            experienceYears = 14,
            rating = 4.9,
            reviewsCount = 428,
            consultationFee = 800.0,
            hospitalName = "Manipal Heart Center",
            location = "Indiranagar, Bengaluru",
            distanceKm = 1.5,
            nextAvailableSlot = "Today, 4:30 PM"
        ),
        Doctor(
            id = "doc_002",
            name = "Dr. Sandeep Bannerjee",
            specialty = "Orthopedic & Joint Specialist",
            qualification = "MS (Ortho), Fellowship in Joint Replacement",
            experienceYears = 18,
            rating = 4.8,
            reviewsCount = 560,
            consultationFee = 900.0,
            hospitalName = "Apollo Ortho & Trauma Clinic",
            location = "Koramangala, Bengaluru",
            distanceKm = 2.7,
            nextAvailableSlot = "Tomorrow, 10:00 AM"
        ),
        Doctor(
            id = "doc_003",
            name = "Dr. Priya Nair",
            specialty = "Pulmonologist & Respiratory Specialist",
            qualification = "MD (Chest Medicine), DNB",
            experienceYears = 11,
            rating = 4.9,
            reviewsCount = 310,
            consultationFee = 750.0,
            hospitalName = "Fortis Pulmonary Center",
            location = "HSR Layout, Bengaluru",
            distanceKm = 3.8,
            nextAvailableSlot = "Today, 6:00 PM"
        ),
        Doctor(
            id = "doc_004",
            name = "Dr. Arvind Subramanian",
            specialty = "General Physician & Diabetologist",
            qualification = "MBBS, MD (Internal Medicine)",
            experienceYears = 16,
            rating = 4.9,
            reviewsCount = 820,
            consultationFee = 500.0,
            hospitalName = "Medica Family Clinic",
            location = "Jayanagar, Bengaluru",
            distanceKm = 2.2,
            nextAvailableSlot = "Today, 2:00 PM"
        ),
        Doctor(
            id = "doc_005",
            name = "Dr. Shalini Mukhopadhyay",
            specialty = "Neurologist",
            qualification = "MD, DM (Neurology) - NIMHANS",
            experienceYears = 13,
            rating = 4.7,
            reviewsCount = 290,
            consultationFee = 1100.0,
            hospitalName = "Brain & Spine Institute",
            location = "Richmond Town, Bengaluru",
            distanceKm = 4.1,
            nextAvailableSlot = "Wed, 11:30 AM"
        ),
        Doctor(
            id = "doc_006",
            name = "Dr. Deepak Mehta",
            specialty = "Pediatrician & Child Health",
            qualification = "MD (Pediatrics), DCH",
            experienceYears = 9,
            rating = 4.9,
            reviewsCount = 410,
            consultationFee = 650.0,
            hospitalName = "Aster CMI Kids Clinic",
            location = "Indiranagar, Bengaluru",
            distanceKm = 1.9,
            nextAvailableSlot = "Today, 5:15 PM"
        )
    )

    private val _doctors = MutableStateFlow(doctorsList)
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    // Medicines Catalog
    private val medicinesList = listOf(
        Medicine(
            id = "med_001",
            name = "Dolo 650mg Paracetamol",
            brand = "Micro Labs",
            category = "Pain Relief",
            dosageForm = "Strip of 15 Tablets",
            price = 32.0,
            originalPrice = 36.0,
            discountPercent = 11,
            requiresPrescription = false,
            description = "Proven fast relief for fever, headache, body aches, and post-vaccination discomfort."
        ),
        Medicine(
            id = "med_002",
            name = "Augmentin 625 Duo Tablet",
            brand = "GlaxoSmithKline",
            category = "Antibiotics",
            dosageForm = "Strip of 10 Tablets",
            price = 185.0,
            originalPrice = 210.0,
            discountPercent = 12,
            requiresPrescription = true,
            description = "Amoxicillin and Potassium Clavulanate for respiratory and bacterial infections. Prescription verified."
        ),
        Medicine(
            id = "med_003",
            name = "Budecort 200 Inhaler 200 MDI",
            brand = "Cipla Respiratory",
            category = "Respiratory",
            dosageForm = "Inhaler Canister",
            price = 395.0,
            originalPrice = 460.0,
            discountPercent = 14,
            requiresPrescription = true,
            description = "Budesonide inhalation aerosol for maintenance treatment of asthma and chronic bronchospasm."
        ),
        Medicine(
            id = "med_004",
            name = "Becosules Performance Multi-Vitamin",
            brand = "Pfizer",
            category = "Vitamins & Supplements",
            dosageForm = "Bottle of 30 Capsules",
            price = 145.0,
            originalPrice = 175.0,
            discountPercent = 17,
            requiresPrescription = false,
            description = "Vitamin B-Complex enriched with Vitamin C and Zinc to boost immune response and cellular recovery."
        ),
        Medicine(
            id = "med_005",
            name = "Glycomet-GP 1 Tablet PR",
            brand = "USV Pharma",
            category = "Diabetes Care",
            dosageForm = "Strip of 15 Tablets",
            price = 122.0,
            originalPrice = 140.0,
            discountPercent = 13,
            requiresPrescription = true,
            description = "Glimepiride + Metformin for glycemic control in Type 2 Diabetes Mellitus."
        ),
        Medicine(
            id = "med_006",
            name = "Volini Pain Relief Gel (75g)",
            brand = "Sun Pharma",
            category = "Pain Relief",
            dosageForm = "Ointment Tube",
            price = 195.0,
            originalPrice = 230.0,
            discountPercent = 15,
            requiresPrescription = false,
            description = "Fast-absorbing micro-emulsion formula with Diclofenac and Methyl Salicylate for muscle and joint pain."
        ),
        Medicine(
            id = "med_007",
            name = "Ecosprin 75mg Gastro-Resistant",
            brand = "USV Pharma",
            category = "Cardiac Care",
            dosageForm = "Strip of 14 Tablets",
            price = 18.0,
            originalPrice = 22.0,
            discountPercent = 18,
            requiresPrescription = true,
            description = "Low-dose Aspirin blood thinner for cardiovascular protection and prevention of clot formation."
        ),
        Medicine(
            id = "med_008",
            name = "Betadine 10% Microbicidal Solution (100ml)",
            brand = "Win-Medicare",
            category = "First Aid & Wound Care",
            dosageForm = "Antiseptic Liquid Bottle",
            price = 135.0,
            originalPrice = 150.0,
            discountPercent = 10,
            requiresPrescription = false,
            description = "Povidone-Iodine antiseptic for cleansing cuts, wounds, burns, and post-surgical dressings."
        ),
        Medicine(
            id = "med_009",
            name = "Pan 40 Gastro-Resistant Tablet",
            brand = "Alkem Laboratories",
            category = "Gastro Care",
            dosageForm = "Strip of 15 Tablets",
            price = 148.0,
            originalPrice = 170.0,
            discountPercent = 13,
            requiresPrescription = true,
            description = "Pantoprazole sodium for acid reflux, GERD, and hyperacidity relief."
        ),
        Medicine(
            id = "med_010",
            name = "Shelcal 500 Calcium + Vitamin D3",
            brand = "Torrent Pharma",
            category = "Vitamins & Supplements",
            dosageForm = "Strip of 15 Tablets",
            price = 125.0,
            originalPrice = 145.0,
            discountPercent = 14,
            requiresPrescription = false,
            description = "High bioavailability calcium supplement with Cholecalciferol for bone density and osteoporosis prevention."
        )
    )

    private val _medicines = MutableStateFlow(medicinesList)
    val medicines: StateFlow<List<Medicine>> = _medicines.asStateFlow()

    // Lab Tests Catalog
    private val labTestsList = listOf(
        LabTest(
            id = "lab_001",
            testName = "Comprehensive Full Body Health Checkup (64 Parameters)",
            labName = "Thyrocare / Metropolis Diagnostics",
            category = "Full Body Packages",
            price = 1499.0,
            originalPrice = 3200.0,
            turnaroundHours = 24,
            homeCollection = true,
            rating = 4.9,
            includedParameters = listOf(
                "Complete Hemogram (CBC)",
                "Liver Function Test (LFT)",
                "Kidney Function Test (KFT)",
                "Lipid Profile (Cholesterol)",
                "Fasting Blood Sugar (FBS)",
                "Thyroid Profile (T3, T4, TSH)",
                "Vitamin D3 & B12 Levels",
                "Urine Routine Analysis"
            ),
            preparation = "10–12 hours overnight fasting mandatory. Free home sample collection included."
        ),
        LabTest(
            id = "lab_002",
            testName = "HbA1c & Fasting Glucose Glycemic Profile",
            labName = "Dr. Lal PathLabs",
            category = "Diabetes",
            price = 450.0,
            originalPrice = 750.0,
            turnaroundHours = 12,
            homeCollection = true,
            rating = 4.8,
            includedParameters = listOf(
                "Glycated Hemoglobin (HbA1c)",
                "Estimated Average Glucose (eAG)",
                "Plasma Fasting Blood Glucose"
            ),
            preparation = "8–10 hours fasting required. Reports delivered digitally via WhatsApp & App."
        ),
        LabTest(
            id = "lab_003",
            testName = "Complete Blood Count (CBC) with ESR & Platelets",
            labName = "Apollo Diagnostics",
            category = "Blood Tests",
            price = 280.0,
            originalPrice = 400.0,
            turnaroundHours = 8,
            homeCollection = true,
            rating = 4.9,
            includedParameters = listOf(
                "Hemoglobin",
                "RBC & WBC Total Count",
                "Platelet Count",
                "Differential Leucocyte Count (DLC)",
                "Erythrocyte Sedimentation Rate (ESR)"
            ),
            preparation = "No special fasting required. Same-day digital report."
        ),
        LabTest(
            id = "lab_004",
            testName = "Advanced Thyroid Function Profile (Total T3, T4 & Ultrasensitive TSH)",
            labName = "Medall Healthcare",
            category = "Thyroid",
            price = 399.0,
            originalPrice = 650.0,
            turnaroundHours = 12,
            homeCollection = true,
            rating = 4.7,
            includedParameters = listOf(
                "Total Triiodothyronine (T3)",
                "Total Thyroxine (T4)",
                "Thyroid Stimulating Hormone (TSH Ultrasensitive)"
            ),
            preparation = "Morning fasting sample recommended."
        ),
        LabTest(
            id = "lab_005",
            testName = "Cardiac Risk Profile & Lipid Screen",
            labName = "Suburban Diagnostics",
            category = "Heart",
            price = 699.0,
            originalPrice = 1100.0,
            turnaroundHours = 18,
            homeCollection = true,
            rating = 4.8,
            includedParameters = listOf(
                "Total Cholesterol",
                "HDL (Good) Cholesterol",
                "LDL (Bad) Cholesterol",
                "Triglycerides",
                "VLDL",
                "TC/HDL Cholesterol Ratio",
                "High-Sensitivity CRP (hs-CRP)"
            ),
            preparation = "12 hours strict fasting required."
        )
    )

    private val _labTests = MutableStateFlow(labTestsList)
    val labTests: StateFlow<List<LabTest>> = _labTests.asStateFlow()

    // Active User State: Cart, Bookings, Orders, Chats, Admin Moderation
    private val _cart = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(medicinesList[0], 2), // Dolo 650
            CartItem(medicinesList[3], 1)  // Becosules
        )
    )
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    fun addToCart(medicine: Medicine) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.medicine.id == medicine.id }
        if (index >= 0) {
            current[index] = current[index].copy(quantity = current[index].quantity + 1)
        } else {
            current.add(CartItem(medicine, 1))
        }
        _cart.value = current
    }

    fun updateCartQuantity(medicineId: String, delta: Int) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.medicine.id == medicineId }
        if (index >= 0) {
            val newQty = current[index].quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = newQty)
            }
        }
        _cart.value = current
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // Doctor Appointments
    private val _appointments = MutableStateFlow<List<DoctorAppointment>>(
        listOf(
            DoctorAppointment(
                id = "apt_101",
                doctor = doctorsList[0],
                patientName = "Aarav Sharma",
                date = "Tomorrow",
                timeSlot = "04:30 PM",
                consultationType = "Video Consultation",
                status = "Confirmed",
                feePaid = 800.0
            )
        )
    )
    val appointments: StateFlow<List<DoctorAppointment>> = _appointments.asStateFlow()

    fun bookAppointment(appointment: DoctorAppointment) {
        _appointments.value = listOf(appointment) + _appointments.value
        _platformStats.value = _platformStats.value.copy(
            doctorAppointments = _platformStats.value.doctorAppointments + 1
        )
    }

    // Medicine Orders
    private val _medicineOrders = MutableStateFlow<List<MedicineOrder>>(
        listOf(
            MedicineOrder(
                id = "ord_5521",
                items = listOf(CartItem(medicinesList[0], 2), CartItem(medicinesList[5], 1)),
                totalAmount = 259.0,
                deliveryAddress = "Flat 402, Green Glen Layout, Indiranagar, Bengaluru",
                status = "Out for Delivery",
                orderedAt = "Today, 09:15 AM",
                trackingStage = 3
            )
        )
    )
    val medicineOrders: StateFlow<List<MedicineOrder>> = _medicineOrders.asStateFlow()

    fun placeMedicineOrder(items: List<CartItem>, total: Double, address: String): MedicineOrder {
        val newOrder = MedicineOrder(
            id = "ord_${(1000..9999).random()}",
            items = items,
            totalAmount = total,
            deliveryAddress = address,
            status = "Placed & Verified",
            orderedAt = "Just now",
            trackingStage = 1
        )
        _medicineOrders.value = listOf(newOrder) + _medicineOrders.value
        clearCart()
        return newOrder
    }

    // Lab Bookings
    private val _labBookings = MutableStateFlow<List<LabBooking>>(
        listOf(
            LabBooking(
                id = "lab_b_901",
                test = labTestsList[0],
                patientName = "Aarav Sharma",
                date = "24 Aug 2026",
                timeSlot = "07:30 AM (Fasting)",
                homeCollection = true,
                address = "Indiranagar, Bengaluru",
                totalAmount = 1499.0,
                status = "Phlebotomist Assigned"
            )
        )
    )
    val labBookings: StateFlow<List<LabBooking>> = _labBookings.asStateFlow()

    fun bookLabTest(booking: LabBooking) {
        _labBookings.value = listOf(booking) + _labBookings.value
    }

    // Equipment Rentals
    private val _rentals = MutableStateFlow<List<RentalBooking>>(
        listOf(
            RentalBooking(
                id = "rnt_801",
                equipment = initialListings[0], // Karma Wheelchair
                startDate = "20 Aug 2026",
                endDate = "27 Aug 2026",
                totalDays = 7,
                dailyRate = 120.0,
                securityDeposit = 2000.0,
                totalAmount = 750.0,
                status = "Active"
            )
        )
    )
    val rentals: StateFlow<List<RentalBooking>> = _rentals.asStateFlow()

    fun bookRental(rental: RentalBooking) {
        _rentals.value = listOf(rental) + _rentals.value
        _platformStats.value = _platformStats.value.copy(
            equipmentRentals = _platformStats.value.equipmentRentals + 1,
            wastePreventedKg = _platformStats.value.wastePreventedKg + 14.5
        )
    }

    // Donation Requests
    private val _donations = MutableStateFlow<List<DonationRequest>>(
        listOf(
            DonationRequest(
                id = "don_301",
                equipment = initialListings[3], // Vissco Walker
                requesterName = "Suresh Patel",
                requesterPhone = "+91 94480 88219",
                purposeDescription = "Needed for my 78-year-old mother recovering from hip fracture surgery.",
                urgentLevel = "Urgent",
                requestDate = "Today, 11:00 AM",
                status = "Approved for Pickup"
            )
        )
    )
    val donations: StateFlow<List<DonationRequest>> = _donations.asStateFlow()

    fun requestDonation(request: DonationRequest) {
        _donations.value = listOf(request) + _donations.value
        _platformStats.value = _platformStats.value.copy(
            donationsCompleted = _platformStats.value.donationsCompleted + 1,
            wastePreventedKg = _platformStats.value.wastePreventedKg + 4.2
        )
    }

    // In-App Chat Messages
    private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "eq_001" to listOf(
                ChatMessage("m1", "eq_001", "Dr. Rajesh Kulkarni", "Namaste! The wheelchair is sanitized and available for instant dispatch or pickup.", "10:00 AM", false),
                ChatMessage("m2", "eq_001", "Aarav Sharma", "Hello Dr. Rajesh, does it come with the original seat cushion?", "10:05 AM", true),
                ChatMessage("m3", "eq_001", "Dr. Rajesh Kulkarni", "Yes, pristine original ergonomic memory-foam cushion and footrest are included.", "10:07 AM", false)
            ),
            "eq_002" to listOf(
                ChatMessage("m4", "eq_002", "Sneha Hegde", "Hello! The EverFlo 5L concentrator has fresh HEPA filters and comes with 2 unopened cannula kits.", "09:30 AM", false)
            )
        )
    )
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

    fun sendChatMessage(equipmentId: String, text: String, senderName: String) {
        val currentMap = _chatMessages.value.toMutableMap()
        val currentList = currentMap[equipmentId]?.toMutableList() ?: mutableListOf()
        currentList.add(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                equipmentId = equipmentId,
                senderName = senderName,
                text = text,
                timestamp = "Just now",
                isFromUser = true
            )
        )
        currentMap[equipmentId] = currentList
        _chatMessages.value = currentMap
    }

    // Reports and Platform Stats
    private val _reports = MutableStateFlow<List<ListingReport>>(
        listOf(
            ListingReport(
                id = "rep_101",
                equipmentId = "eq_006",
                equipmentTitle = "ResMed AirSense 10",
                reason = "Seller description needed clarification on mask size",
                reporterName = "Verified User",
                timestamp = "Yesterday",
                status = "Reviewed & Cleared"
            )
        )
    )
    val reports: StateFlow<List<ListingReport>> = _reports.asStateFlow()

    fun reportListing(report: ListingReport) {
        _reports.value = listOf(report) + _reports.value
        _platformStats.value = _platformStats.value.copy(
            reportedListings = _platformStats.value.reportedListings + 1
        )
    }

    private val _platformStats = MutableStateFlow(PlatformStats())
    val platformStats: StateFlow<PlatformStats> = _platformStats.asStateFlow()

    fun approveAiVerification(listingId: String) {
        val current = _equipmentListings.value.toMutableList()
        val index = current.indexOfFirst { it.id == listingId }
        if (index >= 0) {
            current[index] = current[index].copy(aiVerified = true, aiConfidence = 98)
            _equipmentListings.value = current
        }
    }
}
