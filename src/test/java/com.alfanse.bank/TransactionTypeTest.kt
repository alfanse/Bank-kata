package com.alfanse.bank

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal

internal class TransactionTypeTest {

    @ParameterizedTest
    @CsvSource(
            "0, 1, 1",
            "1, 1.11, 2.11",
            "99.99, 99.99, 199.98",
            "-99, 1, -98",
            "-99.99, 111.11, 11.12"
    )
    fun credit(aaa: String, bbb: String, expected: String) {
        val actual = TransactionType.CREDIT.apply(BigDecimal(aaa), BigDecimal(bbb))
        assertThat(actual).isEqualTo(currency(expected))
    }

    @ParameterizedTest
    @CsvSource(
            "0, 1, -1",
            "100, 1, 99.00",
            "111.11, 99.99, 11.12"
    )
    fun debit(aaa: String, bbb: String, expected: String) {
        val actual = TransactionType.DEBIT.apply(BigDecimal(aaa), BigDecimal(bbb))
        assertThat(actual).isEqualTo(currency(expected))
    }

    @Test
    fun creditWithBothInputsScaled() {
        val actual = TransactionType.CREDIT.apply(currency(1.99), currency(99.99))
        assertThat(actual).isEqualTo(currency(101.98))
    }

    @Test
    fun creditWithScaledBalance() {
        val actual = TransactionType.CREDIT.apply(currency(1.99), rawBigDouble(99.99))
        assertThat(actual).isEqualTo(currency(101.98))
    }

    @Test
    fun creditWithRawBoth() {
        val actual = TransactionType.CREDIT.apply(rawBigDouble(1.99), rawBigDouble(99.99))
        assertThat(actual).isEqualTo(currency(101.98))
    }

    @Test
    fun debitWithBothInputsScaled() {
        val actual = TransactionType.DEBIT.apply(currency(100.11), currency(99.99))
        assertThat(actual).isEqualTo(currency(0.12))
    }

    @Test
    fun debitWithScaledBalance() {
        val actual = TransactionType.DEBIT.apply(currency(100.11), rawBigDouble(99.99))
        assertThat(actual).isEqualTo(currency(0.12))
    }

    @Test
    fun debitWithRawBoth() {
        val actual = TransactionType.DEBIT.apply(rawBigDouble(100.11), rawBigDouble(99.99))
        assertThat(actual).isEqualTo(currency(0.12))
    }

    private fun rawBigDouble(d: Double) = BigDecimal(d)
}