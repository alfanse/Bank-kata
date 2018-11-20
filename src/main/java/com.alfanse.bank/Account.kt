package com.alfanse.bank

import com.alfanse.bank.TransactionType.CREDIT
import com.alfanse.bank.TransactionType.DEBIT

class Account {

    val transactions: MutableList<Transaction> = mutableListOf()
    var balance = currency(0.0)

    fun deposit(amount: Amount) {
        transact(CREDIT, amount)
    }

    fun withdraw(amount: Amount) {
        transact(DEBIT, amount)
    }

    fun transactions(): List<Transaction> {
        return transactions
    }

    private fun transact(transactionType: TransactionType, amount: Amount) {
        synchronized(this) {
            val newBalance = transactionType.apply(balance, amount.amount)
            this.balance = newBalance
            transactions.add(Transaction(amount, transactionType, newBalance))
        }
    }

    fun getWithdrawals(): List<Transaction> {
        return transactions.filter { t -> t.type == DEBIT }
    }

    fun getDeposits(): List<Transaction> {
        return transactions.filter { t -> t.type == CREDIT }
    }
}