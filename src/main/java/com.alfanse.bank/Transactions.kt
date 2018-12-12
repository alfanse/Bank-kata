package com.alfanse.bank

import java.math.BigDecimal

class Transactions {
    val transactionsList: MutableList<Transaction> = mutableListOf()

    fun add(transaction: Transaction): Transactions {
        transactionsList.add(transaction)
        return this
    }

    fun isEmpty(): Boolean {
        return transactionsList.isEmpty()
    }

    fun clear() {
        transactionsList.clear()
    }

    fun reversed(): List<Transaction> {
        return transactionsList.reversed()
    }

    fun filter(filterFn: (Transaction) -> Boolean): List<Transaction> {
        return transactionsList.filter(filterFn)
    }

    fun balances(): List<BigDecimal> {
        return transactionsList.map { t->t.balance }
    }
}
