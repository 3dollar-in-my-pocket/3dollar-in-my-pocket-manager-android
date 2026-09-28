package app.threedollars.manager.feature.storemanagement.components.coupon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray10
import app.threedollars.common.ui.Gray20
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray60
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.Green100
import app.threedollars.common.ui.GreenOp50
import app.threedollars.common.ui.White
import app.threedollars.domain.dto.CouponDto
import app.threedollars.domain.dto.CouponStatus

@Composable
internal fun CouponCard(
    coupon: CouponDto,
    onCloseClick: (CouponDto) -> Unit,
) {
    val isActive = coupon.status == CouponStatus.ACTIVE
    val isEnded = coupon.status == CouponStatus.ENDED
    val borderColor = when (coupon.status) {
        CouponStatus.ACTIVE -> Green
        CouponStatus.STOPPED -> GreenOp50
        else -> Gray40
    }
    val bottomColor = if (isEnded) Gray10 else Green100
    val issuedColor = if (isEnded) Gray60 else Green
    val shape = RoundedCornerShape(16.dp)

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (isActive) Modifier.shadow(8.dp, shape, ambientColor = Green, spotColor = Green) else Modifier)
                .clip(shape)
                .background(White)
                .border(1.dp, borderColor, shape),
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    CouponStatusBadge(status = coupon.status)
                    Spacer(modifier = Modifier.weight(1f))
                    if (isActive) {
                        Text(
                            text = "발급 중지하기 ›",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Gray50,
                            modifier = Modifier.clickable { onCloseClick(coupon) },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = coupon.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray95,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "사용 기간  |  ${CouponFormatter.formatPeriod(coupon.validityPeriod.startDateTime, coupon.validityPeriod.endDateTime)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray60,
                )
            }
            DashedDivider(color = if (isEnded) Gray20 else GreenOp50)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bottomColor)
                    .padding(vertical = 16.dp),
            ) {
                CouponMetric(modifier = Modifier.weight(1f), title = "발급 가능 수량", value = coupon.maxIssuableCount, valueColor = Gray60)
                CouponMetric(modifier = Modifier.weight(1f), title = "발급된 수량", value = coupon.currentIssuedCount, valueColor = issuedColor)
                CouponMetric(modifier = Modifier.weight(1f), title = "사용 완료 수량", value = coupon.currentUsedCount, valueColor = Gray60)
            }
        }
        if (isActive) {
            Text(
                text = "* 최대 1개의 쿠폰만 만들 수 있습니다.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Gray50,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp),
            )
        }
    }
}

@Composable
private fun CouponStatusBadge(status: CouponStatus) {
    val (text, background, textColor) = when (status) {
        CouponStatus.ACTIVE -> Triple("발급 중", Green, White)
        CouponStatus.STOPPED -> Triple("발급 중지 / 사용 가능", Green100, Green)
        else -> Triple("사용 종료", Gray50, White)
    }
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier
            .background(background, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
private fun CouponMetric(modifier: Modifier, title: String, value: Int, valueColor: Color) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Gray60)
        Text(text = "$value", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}

@Composable
private fun DashedDivider(color: Color) {
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = size.height,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f),
        )
    }
}
