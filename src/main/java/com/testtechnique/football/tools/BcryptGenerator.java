/**
 * Utilitaire CLI pour générer des hashes BCrypt au format Spring Delegating ({bcrypt}).
 * <p>
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */

package com.testtechnique.football.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utility CLI to generate BCrypt hashes.
 * Usage:
 *   mvn -q -Ptools -Dpassword=Admin#2025! exec:java -Dexec.mainClass=tools.com.testtechnique.football.BcryptGenerator
 */
public class BcryptGenerator {
    public static void main(String[] args) {
        String pwd = System.getProperty("password");
    //    String pwd = "Ines26031986*";
        if (pwd == null || pwd.isBlank()) {
            System.err.println("Usage: -Dpassword=<cleartext>");
            System.exit(1);
        }
        PasswordEncoder enc = new BCryptPasswordEncoder(10);
        String hash = enc.encode(pwd);
        System.out.println("{bcrypt}" + hash);
    }
}
