package sentinel.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import co.rexiox.sentinel.ui.resources.Res
import co.rexiox.sentinel.ui.resources.inter_bold
import co.rexiox.sentinel.ui.resources.inter_medium
import co.rexiox.sentinel.ui.resources.inter_regular
import co.rexiox.sentinel.ui.resources.inter_semibold

internal val SentinelFont
    @Composable
    get() = FontFamily(
        Font(Res.font.inter_regular, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semibold, FontWeight.SemiBold),
        Font(Res.font.inter_bold, FontWeight.Bold)
    )