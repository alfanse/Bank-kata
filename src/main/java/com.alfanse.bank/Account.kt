package com.alfanse.bank

class Account {

    val transactions: MutableList<Transaction> = mutableListOf()
    var balance = currency(0.0)

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
        synchronized(this) {
            val newBalance = transactionType.apply(balance, amount.amount)
            this.balance = newBalance
            transactions.add(Transaction(amount, transactionType, newBalance))
        }
    }

    fun filterByDebits(): List<Transaction> {
        return transactions.filter { t -> t.type == TransactionType.DEBIT }
    }

    fun filterByCredits(): List<Transaction> {
        return transactions.filter { t -> t.type == TransactionType.CREDIT }
    }
}