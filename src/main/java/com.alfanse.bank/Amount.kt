package com.alfanse.bank

import java.math.BigDecimal
import java.time.LocalDateTime

data class Amount(
        val amount: BigDecimal,
        val date: LocalDateTime
)

enum class TransactionType {
    CREDIT {
        override fun apply(balance: BigDecimal, amount: BigDecimal): BigDecimal {
            return scale(balance + amount)
        }
    },
    DEBIT {
        override fun apply(balance: BigDecimal, amount: BigDecimal): BigDecimal {
            return scale(balance - amount)
        }
    };

    abstract fun apply(balance: BigDecimal, amount: BigDecimal): BigDecimal

}
