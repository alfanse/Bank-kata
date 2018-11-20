package com.alfanse.bank

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

import java.util.Collections.nCopies
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors.newFixedThreadPool
import java.util.concurrent.Future

internal class AccountConcurrencyTest {

    val account = Account()

    private val deposit = 1.99

    private val withdraw = 1.11

    @Test
    fun `concurrent transactions`(){
        val noOfTransactions = 100_000

        concurrently(noOfTransactions, Callable {
            credit()
            debit()
        })

        assertThat(account.balance).isEqualTo(
            currency(deposit * noOfTransactions - withdraw *noOfTransactions)
        )
    }

    fun credit(): Boolean {
        account.deposit(Amount(BigDecimal(deposit), LocalDateTime.now()))
        return true
    }

    fun debit(): Boolean {
        account.withdraw(Amount(BigDecimal(withdraw), LocalDateTime.now()))
        return true
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    private fun concurrently(noOfTransactions: Int, task: Callable<Boolean>) {
        val tasks = nCopies<Callable<Boolean>>(noOfTransactions, task)

        val futures = newFixedThreadPool(100).invokeAll(tasks)

        assertThat<Future<Boolean>>(futures).hasSize(noOfTransactions)
        flushExceptions(futures)
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    private fun flushExceptions(futures: List<Future<Boolean>>) {
        for (future in futures) {
            future.get()
        }
    }
}