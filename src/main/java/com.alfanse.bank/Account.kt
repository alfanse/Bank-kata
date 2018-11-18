package com.alfanse.bank

import java.math.BigDecimal

class Account {

    val transactions: MutableList<Transaction> = mutableListOf()
    var balance = BigDecimal(0)

    fun deposit(amount: Amount) {
        transact(TransactionType.CREDIT, amount)
    }

    fun withdraw(amount: Amount) {
        transact(TransactionType.DEBIT, amount)
    }

    fun transactions(): List<Transaction> {
        return transactions
    }

    private fun transact(transactionType: TransactionType, amount: Amount) {
        val newBalance = transactionType.apply(balance, amount)
        this.balance = BigDecimal(newBalance.unscaledValue())
        transactions.add(Transaction(amount, transactionType, newBalance))
    }
}