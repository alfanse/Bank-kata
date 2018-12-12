package com.alfanse.bank

import java.math.BigDecimal

class Account {

    val transactions: Transactions = Transactions()
    var currentBalance = currency(0.0)

    fun deposit(amount: Amount) {
        synchronized(this) {
            val credit = Credit(amount, currentBalance)
            currentBalance = credit.balance
            transactions.add(credit)
        }
    }

    fun withdraw(amount: Amount) {
        synchronized(this) {
            val debit = Debit(amount, currentBalance)
            currentBalance = debit.balance
            transactions.add(debit)
        }
    }

    fun balanceHistory(): List<BigDecimal> {
        return transactions.balances()
    }

    fun getWithdrawals(): List<Transaction> {
        return transactions.filter(Transaction::isDebit)
    }

    fun getDeposits(): List<Transaction> {
        return transactions.filter(Transaction::isCredit)
    }

    fun reset() {
        transactions.clear()
        currentBalance = currency(0.0)
    }
}