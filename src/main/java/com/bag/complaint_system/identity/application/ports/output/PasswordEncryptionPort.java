package com.bag.complaint_system.identity.application.ports.output;

public interface PasswordEncryptionPort {

    String encryptPassword(String password);

    boolean checkPassword(String password, String encryptedPassword);
}
