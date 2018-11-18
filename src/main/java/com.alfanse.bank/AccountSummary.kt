package com.alfanse.bank

import java.lang.String.format
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.text.DecimalFormat

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
                    padLeft(creditAmount(transaction, amount), 10),
                    padLeft(debitAmount(transaction, amount), 10),
                    padLeft(toCurrency(transaction.balance), 11)
            )
        }

    private fun dateWhen(amount: Amount) = dateFormat.format(amount.date)

    private fun debitAmount(transaction: Transaction, amount: Amount): String {
        return toCurrencyWhenType(transaction, amount, TransactionType.DEBIT)
    }

    private fun creditAmount(transaction: Transaction, amount: Amount): String {
        return toCurrencyWhenType(transaction, amount, TransactionType.CREDIT)
    }

    private fun toCurrencyWhenType(transaction: Transaction, amount: Amount, requiredType: TransactionType): String {
        var display = ""
        if (transaction.type == requiredType) {
            display = toCurrency(amount.amount)
        }
        return display
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