package com.alfanse.bank

import java.math.BigDecimal

abstract class Transaction(val amount: Amount, startBalance: BigDecimal) {

    val balance: BigDecimal = recordNewBalance(startBalance)

    abstract fun type(): TransactionType

    fun isCredit(): Boolean {
        return TransactionType.CREDIT == type()
    }

    fun isDebit(): Boolean {
        return TransactionType.DEBIT == type()
    }

    private fun recordNewBalance(balance: BigDecimal): BigDecimal {
        return type().apply(balance, amount.amount)
    }
}

class Credit (amount: Amount, startBalance: BigDecimal) : Transaction(amount, startBalance) {
    override fun type(): TransactionType {
        return TransactionType.CREDIT
    }
}

class Debit (amount: Amount, startBalance: BigDecimal) : Transaction(amount, startBalance) {
    override fun type(): TransactionType {
        return TransactionType.DEBIT
    }
}