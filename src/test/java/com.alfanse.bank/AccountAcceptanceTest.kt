package com.alfanse.bank

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.annotations.NotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays.asList

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountAcceptanceTest {

    val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    private val account  = Account()
    private val accountSummary = AccountSummary(account)

    @BeforeEach
    internal fun setUp() {
        account.transactions.clear()
    }

    /**
     * Given a client makes:
     * a deposit of 1000 on 10-01-2012
     * a deposit of 2000 on 13-01-2012
     * a withdrawal of 500 on 14-01-2012
     *
     * When she prints her bank statement
     *
     * Then she would see
     * date       || credit   || debit    || balance
     * 14/01/2012 ||          || 500.00   || 2500.00
     * 13/01/2012 || 2000.00  ||          || 3000.00
     * 10/01/2012 || 1000.00  ||          || 1000.00
     */
    @Test
    fun `multiple types of account activity + statement`() {

        account.deposit(amount("10-01-2012", "1000"))
        account.deposit(amount("13-01-2012", "2000"))
        account.withdraw(amount("14-01-2012", "500"))

        val transactions = account.transactions()

        assertThat(transactions.map { t->t.balance }).isEqualTo(asList(
                        currency(1000.00),
                        currency(3000.00),
                        currency(2500.00)))

        val summary = accountSummary.summary()
        Assertions.assertThat(summary).isEqualTo(
            "date        || credit     || debit      || balance    " + "\n" +
            "14/01/2012  ||            ||     500.00 ||     2500.00" + "\n" +
            "13/01/2012  ||    2000.00 ||            ||     3000.00" + "\n" +
            "10/01/2012  ||    1000.00 ||            ||     1000.00"
        )
    }

    @Nested
    inner class Filtering {
        val credit1 = amount("10-01-2012", "1001")
        val debit1 = amount("11-01-2012", "501")
        val credit2 = amount("12-01-2012", "1002")
        val debit2 = amount("13-01-2012", "502")

        @BeforeEach
        internal fun setUp() {
            //given transactions
            account.deposit(credit1)
            account.withdraw(debit1)
            account.deposit(credit2)
            account.withdraw(debit2)
        }

        @Test
        fun `filtering by deposits`() {
            assertThat(account.filterByCredits().map(Transaction::amount)).isEqualTo(asList(credit1, credit2))
        }

        @Test
        fun `filtering by withdrawls`() {
            assertThat(account.filterByDebits().map(Transaction::amount)).isEqualTo(asList(debit1, debit2))
        }
    }


    @NotNull
    private fun amount(date: String, amount: String): Amount  {
        return Amount(BigDecimal(amount), date(date))
    }

    @NotNull
    private fun date(date: String): LocalDateTime = LocalDate.parse(date, FORMATTER).atStartOfDay()

}
