package com.example.transaction;

import com.example.entity.Country;
import com.example.repository.CountryRepository;
import com.example.transaction.exceptions.CheckedExceptionWithRollbackTrue;
import com.example.transaction.exceptions.MyCheckedException;
import com.example.transaction.exceptions.MyUncheckedException;
import com.example.transaction.exceptions.UncheckedExceptionWithRollbackFalse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalBean {
    @Autowired
    private CountryRepository countryRepository;

    @Transactional
    public void doSomeStuffWithUncheckedException(Country c, boolean throwException) {
        //countryRepository.save(new Country(3L, "123", "123"));
        countryRepository.save(c);
        if (throwException) {
            throw new MyUncheckedException();
        }
    }

    @Transactional
    public void doSomeStuffWithCheckedException(Country c, boolean throwException) throws MyCheckedException {
        countryRepository.save(c);
        //countryRepository.save(new Country(4L, "1234", "1234"));
        if (throwException) {
            throw new MyCheckedException();
        }
    }

    @Transactional
    public void doSomeStuffWithUncheckedExceptionWithRollbackFalse(Country c, boolean throwException) {
        countryRepository.save(c);
        if (throwException) {
            throw new UncheckedExceptionWithRollbackFalse();
        }
    }

    @Transactional
    public void doSomeStuffWithCheckedExceptionWithRollbackTrue(Country c, boolean throwException) throws CheckedExceptionWithRollbackTrue {
        countryRepository.save(c);
        if (throwException) {
            throw new CheckedExceptionWithRollbackTrue();
        }
    }

}
