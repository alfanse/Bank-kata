package com.alfanse.bank

import java.math.BigDecimal
import java.math.RoundingMode


fun currency(s: String) = scale(BigDecimal(s))

fun currency(d: Double) = scale(BigDecimal(d))

fun scale(amount: BigDecimal): BigDecimal {
    return amount.setScale(2, RoundingMode.HALF_UP)
}
