package com.example.transaction.exceptions;

import com.example.transaction.ExceptionRollback;

@ExceptionRollback(rollback = true)
public class CheckedExceptionWithRollbackTrue extends Exception {
}
