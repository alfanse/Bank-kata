package com.alfanse.bank

import java.lang.String.format
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

private val summaryFormat = "%s || %s || %s || %s"

private val currencyFormat = DecimalFormat("#0.00")

private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

class AccountSummary(val account: Account) {

    fun summary(): String {
        val transactions = account.transactions
        if(transactions.isEmpty()) {
            return headerRow()
        }

        return headerRow() + "\n" + transactions.reversed()
                .joinToString("\n") { summaryRow(it) }
    }

    /** date       || credit   || debit    || balance */
    private fun headerRow(): String {
        return format(summaryFormat,
                padRight("date", 11),
                padRight("credit", 10),
                padRight("debit", 10),
                padRight("balance", 11)
        )
    }

    private fun summaryRow(transaction: Transaction): String {
        val amount = transaction.amount
        return format(summaryFormat,
                    padRight(dateWhen(amount), 11),
                    padLeft(creditAmount(transaction), 10),
                    padLeft(debitAmount(transaction), 10),
                    padLeft(toCurrency(transaction.balance), 11)
            )
        }

    private fun dateWhen(amount: Amount) = dateFormat.format(amount.date)

    private fun debitAmount(transaction: Transaction): String {
        return toCurrencyWhenType(transaction, TransactionType.DEBIT)
    }

    private fun creditAmount(transaction: Transaction): String {
        return toCurrencyWhenType(transaction, TransactionType.CREDIT)
    }

    private fun toCurrencyWhenType(transaction: Transaction, requiredType: TransactionType): String {
        if (transaction.type() == requiredType) {
            return toCurrency(transaction.amount)
        }
        return ""
    }

    private fun toCurrency(amount: Amount): String {
        return toCurrency(amount.amount)
    }

    private fun toCurrency(amount: BigDecimal): String {
        return currencyFormat.format(amount)
    }

    private fun padLeft(message: String, width: Int): String {
        return pad(width, message) + message
    }

    private fun padRight(message: String, width: Int): String {
        return message + pad(width, message)
    }

    private fun pad(width: Int, message: String): String {
        val padsRequired = width - message.length
        var padding = ""
        for (i in 1..padsRequired) {
            padding += " "
        }
        return padding
    }
}