package com.alfanse.bank;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class AccountIntegrationTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
    void accountActivity() {
        Account account = new Account();
        account.deposit(amount("10-01-2012", 1000 + ""));
        account.deposit(amount("13-01-2012", 2000 + ""));
        account.withdraw(amount("14-01-2012", 500 + ""));

        List<Transaction> transactions = account.transactions();

        Assertions.assertThat(transactions).extracting(Transaction::getBalance).containsExactly(
                new BigDecimal(1000),
                new BigDecimal(3000),
                new BigDecimal(2500)
        );

        String summary = new AccountSummary(account).summary();
        Assertions.assertThat(summary).isEqualTo(
            "date        || credit     || debit      || balance    " + "\n" +
            "14/01/2012  ||            ||     500.00 ||     2500.00" + "\n" +
            "13/01/2012  ||    2000.00 ||            ||     3000.00" + "\n" +
            "10/01/2012  ||    1000.00 ||            ||     1000.00"
        );
    }

    @NotNull
    private Amount amount(String date, String amount) {
        return new Amount(new BigDecimal(amount), when(date));
    }

    @NotNull
    private LocalDateTime when(String when) {
        return LocalDate.parse(when, FORMATTER).atStartOfDay();
    }
}
