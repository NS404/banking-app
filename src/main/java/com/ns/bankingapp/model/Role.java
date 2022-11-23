package com.ns.bankingapp.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


public enum Role {
    ADMIN,
    CLIENT,
    TELLER
}
