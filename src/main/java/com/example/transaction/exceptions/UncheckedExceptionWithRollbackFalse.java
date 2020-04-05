package com.example.transaction.exceptions;

import com.example.transaction.ExceptionRollback;

@ExceptionRollback(rollback = false)
public class UncheckedExceptionWithRollbackFalse extends RuntimeException {
}
