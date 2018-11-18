package com.alfanse.bank

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

internal class AccountSummaryTest {
    val account = Account()
    val accountSummary = AccountSummary(account)

    private val headerTxt = "date        || credit     || debit      || balance    "

    @Test
    fun summaryNotTransactions() {
        val summary = accountSummary.summary()
        Assertions.assertThat(summary).isEqualTo(headerTxt)
    }

    @Test
    fun summaryAfterCredit() {
        val date = LocalDateTime.of(1234, 11, 21, 21, 59)
        account.deposit(Amount(BigDecimal("123.45"), date))

        val summary = accountSummary.summary()

        val rows = summary.split("\n")
        Assertions.assertThat(rows).hasSize(2)
        Assertions.assertThat(rows.get(0)).isEqualTo(headerTxt)
        Assertions.assertThat(rows.get(1)).isEqualTo("21/11/1234  ||     123.45 ||            ||      123.45")
    }

    @Test
    fun summaryAfterDebit() {
        val date = LocalDateTime.of(1234, 11, 21, 21, 59)
        account.withdraw(Amount(BigDecimal("123.45"), date))

        val summary = accountSummary.summary()

        val rows = summary.split("\n")
        Assertions.assertThat(rows).hasSize(2)
        Assertions.assertThat(rows.get(0)).isEqualTo(headerTxt)
        Assertions.assertThat(rows.get(1)).isEqualTo("21/11/1234  ||            ||     123.45 ||     -123.45")
    }
}