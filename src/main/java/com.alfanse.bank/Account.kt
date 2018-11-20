package com.alfanse.bank

class Account {

    val transactions: MutableList<Transaction> = mutableListOf()

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

    fun transactions(): List<Transaction> {
        return transactions
    }

    fun getWithdrawals(): List<Transaction> {
        return transactions.filter(Transaction::isDebit)
    }

    fun getDeposits(): List<Transaction> {
        return transactions.filter(Transaction::isCredit)
    }
}