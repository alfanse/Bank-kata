package com.alfanse.bank

import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.text.DecimalFormat



class AccountSummary(val account: Account) {
    val datePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun summary(): String {
        val transactions = account.transactions
        if(transactions.isEmpty()) {
            return rowHeader()
        }

        val rowsOldFirst = transactions.map { transaction ->
            rowTransaction(transaction, datePattern)
        }
        //newest transaction first
        val reversed = rowsOldFirst.reversed()
        return rowHeader() + "\n" + reversed.joinToString("\n")
    }

    /** date       || credit   || debit    || balance */
    private fun rowHeader(): String {
        return java.lang.String.format("%s||%s||%s||%s",
                padRight("date", 12),
                padRight(" credit", 12),
                padRight(" debit", 12),
                padRight(" balance", 12)
        )
    }

    private fun rowTransaction(transaction: Transaction, summaryDatePattern: DateTimeFormatter?): String {
        val amount = transaction.amount
        return java.lang.String.format("%s||%s||%s||%s",
                    padRight(amount.date.format(summaryDatePattern), 12),
                    padLeft(creditAmount(transaction, amount), 12),
                    padLeft(debitAmount(transaction, amount), 12),
                    padLeft(toCurrency(transaction.balance), 12)
            )
        }

    private fun debitAmount(transaction: Transaction, amount: Amount): String {
        var debit = ""
        if (transaction.type == TransactionType.DEBIT) {
            debit = toCurrency(amount.amount)
        }
        return debit

    }

    private fun creditAmount(transaction: Transaction, amount: Amount): String {
        var credit = ""
        if (transaction.type == TransactionType.CREDIT) {
            credit = toCurrency(amount.amount)
        }
        return credit
    }

    private fun toCurrency(amount: BigDecimal): String {
        return DecimalFormat("#0.00").format(amount)
    }

    private fun padLeft(message: String, width: Int): String {
        return padding(width, message) + message
    }

    private fun padRight(message: String, width: Int): String {
        return message + padding(width, message)
    }

    private fun padding(width: Int, message: String): String {
        val padsRequired = width - message.length
        var padding = ""
        for (i in 1..padsRequired) {
            padding += " "
        }
        return padding
    }
}