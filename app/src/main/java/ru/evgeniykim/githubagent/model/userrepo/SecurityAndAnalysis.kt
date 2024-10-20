package ru.evgeniykim.githubagent.model.userrepo

data class SecurityAndAnalysis(
    val advanced_security: AdvancedSecurity,
    val secret_scanning: SecretScanning,
    val secret_scanning_non_provider_patterns: SecretScanningNonProviderPatterns,
    val secret_scanning_push_protection: SecretScanningPushProtection
)