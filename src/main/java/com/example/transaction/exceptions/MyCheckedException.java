package com.example.transaction.exceptions;

import com.example.transaction.ExceptionRollback;

//@ApplicationException(rollback = true)
@ExceptionRollback
public class MyCheckedException extends Exception {
}
