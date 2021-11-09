package com.coyoapp.donations.transaction;

import net.sf.ehcache.CacheManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class TransactionServiceTest {


    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    public void getTransaction(){
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(new Transaction()));
        List<Transaction> transactionList = transactionService.getAllTransaction();
        int size = CacheManager.ALL_CACHE_MANAGERS.get(0)
                .getCache("com.coyoapp.donations.transaction.Transaction").getSize();
        assertThat(size>0);
    }
}