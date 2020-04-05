package com.example;

import com.example.entity.Country;
import com.example.repository.CountryRepository;
import com.example.transaction.exceptions.CheckedExceptionWithRollbackTrue;
import com.example.transaction.exceptions.MyCheckedException;
import com.example.transaction.exceptions.MyUncheckedException;
import com.example.transaction.TransactionalBean;
import com.example.transaction.exceptions.UncheckedExceptionWithRollbackFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionalBeanTest {
    @Autowired
    private TransactionalBean transactionalBean;
    @Autowired
    private CountryRepository countryRepository;
    private Country country = new Country(1L, "1", "1");

    @Test
    public void testRollbackUncheckedException() {
        countryRepository.deleteAll();
        assertEquals(0, countryRepository.count());
        try {
            transactionalBean.doSomeStuffWithUncheckedException(country, true);
        } catch (MyUncheckedException e) {
            //do nothing
        }
        assertEquals(0, countryRepository.count());
        System.out.println("testRollbackUncheckedException: " + countryRepository.count());
    }

    @Test
    public void testRollbackCheckedException() {
        countryRepository.deleteAll();
        assertEquals(0, countryRepository.count());
        try {
            transactionalBean.doSomeStuffWithCheckedException(country, true);
        } catch (MyCheckedException e) {
            //do nothing
        }
        assertEquals(1, countryRepository.count());
        System.out.println("testRollbackCheckedException: " + countryRepository.count());
    }

    @Test
    public void testRollbackCheckedExceptionWithRollbackTrue() {
        countryRepository.deleteAll();
        assertEquals(0, countryRepository.count());
        try {
            transactionalBean.doSomeStuffWithCheckedExceptionWithRollbackTrue(country, true);
        } catch (CheckedExceptionWithRollbackTrue checkedExceptionWithRollbackTrue) {
            //do nothing
        }
        assertEquals(0, countryRepository.count());
        System.out.println("testRollbackCheckedExceptionWithRollbackTrue: " + countryRepository.count());
    }

    @Test
    public void testRollbackUncheckedExceptionWithRollbackFalse() {
        countryRepository.deleteAll();
        assertEquals(0, countryRepository.count());
        try {
            transactionalBean.doSomeStuffWithUncheckedExceptionWithRollbackFalse(country, true);
        } catch (UncheckedExceptionWithRollbackFalse e) {
            //do nothing
        }
        assertEquals(1, countryRepository.count());
        System.out.println("testRollbackUncheckedExceptionWithRollbackFalse: " + countryRepository.count());
    }
}
