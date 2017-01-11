package com.l1p.interop.dfsp.directory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a wrapper on a map of Accounts
 * 
 * TODO: resolve possible issue with using the name Account vs User as it might
 * create issues in future URIs as well as general confusion to programmers
 *
 * Created by Bryan on 8/17/2016.
 */
public class ValidateAccountException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidateAccountException(){
        super();
    }

    public ValidateAccountException(String message){
        super(message);
    }

}
