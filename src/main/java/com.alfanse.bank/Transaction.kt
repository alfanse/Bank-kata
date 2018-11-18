package com.alfanse.bank

import java.math.BigDecimal
import java.time.LocalDateTime


data class Transaction(
        val amount: Amount,
        val type: TransactionType,
        val balance: BigDecimal
)


data class Amount(
        val amount: BigDecimal,
        val date: LocalDateTime
)


enum class TransactionType {
    CREDIT {
        override fun apply(left: BigDecimal, right: Amount): BigDecimal {
            return left + right.amount
        }
    },
    DEBIT {
        override fun apply(left: BigDecimal, right: Amount): BigDecimal {
            return left - right.amount
        }
    };

    abstract fun apply(left: BigDecimal, right: Amount): BigDecimal
}
