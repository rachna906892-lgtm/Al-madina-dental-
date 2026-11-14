package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val ClinicLightColorScheme = lightColorScheme(
    primary = EmeraldTealPrimary,
    onPrimary = EmeraldTealOnPrimary,
    primaryContainer = MintSageContainer,
    onPrimaryContainer = DeepPineOnContainer,
    secondary = EucalyptusSecondary,
    onSecondary = EucalyptusOnSecondary,
    secondaryContainer = SoftEucalyptusContainer,
    onSecondaryContainer = DarkEucalyptusOnContainer,
    tertiary = WarmAmberTertiary,
    onTertiary = WarmAmberOnTertiary,
    tertiaryContainer = SoftCreamAmberContainer,
    onTertiaryContainer = DeepBronzeOnContainer,
    background = AlabasterBackground,
    onBackground = DeepSlateInk,
    surface = AlabasterBackground,
    onSurface = DeepSlateInk,
    surfaceVariant = SurfaceBase,
    onSurfaceVariant = MutedSageInk,
    surfaceContainerLowest = SurfaceLowest,
    surfaceContainerLow = SurfaceLow,
    surfaceContainer = SurfaceBase,
    surfaceContainerHigh = SurfaceHigh,
    surfaceContainerHighest = SurfaceHighest,
    outline = OutlineSage,
    outlineVariant = OutlineVariantSage
)

private val ClinicDarkColorScheme = darkColorScheme(
    primary = DarkPrimaryMint,
    onPrimary = DarkOnPrimaryTeal,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondarySage,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiaryAmber,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkBackground,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerLowest = DarkBackground,
    surfaceContainerLow = DarkSurfaceLow,
    surfaceContainer = DarkSurface,
    surfaceContainerHigh = DarkSurfaceHigh,
    surfaceContainerHighest = DarkSurfaceHigh,
    outline = DarkOnSurfaceVariant,
    outlineVariant = DarkOutlineVariant
)

val ClinicShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun AlMadinaDentalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ClinicDarkColorScheme else ClinicLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ClinicTypography,
        shapes = ClinicShapes,
        content = content
    )
}
