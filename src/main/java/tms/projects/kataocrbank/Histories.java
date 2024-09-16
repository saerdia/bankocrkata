/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tms.projects.kataocrbank;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static tms.projects.kataocrbank.KataOCRbank.baseControleNumbers;
import static tms.projects.kataocrbank.KataOCRbank.correctAccount;
import static tms.projects.kataocrbank.KataOCRbank.getAccounts;
import static tms.projects.kataocrbank.KataOCRbank.initBaseControle;
import static tms.projects.kataocrbank.KataOCRbank.readFileToBinaryArray;
import static tms.projects.kataocrbank.KataOCRbank.validCheckSum;

/**
 *
 * @author Serigne Saer DIA A1047782
 */
public class Histories {

    public static String hostoriy1(String scannedFilePath) throws IOException {

        baseControleNumbers = initBaseControle();
        List<String[][]> binaryArrayAccountNumbers = readFileToBinaryArray(scannedFilePath);
        List<Account> accounts = getAccounts(binaryArrayAccountNumbers);

        String output = "";
        for (Account account : accounts) {
            output = output + account.getAccountNumber().trim() + "\n";
        }
        output = output.trim();
        System.out.println(output);
        return output;
    }

    public static String hostoriy3(String scannedFilePath) throws IOException {

        baseControleNumbers = initBaseControle();
        List<String[][]> binaryArrayAccountNumbers = readFileToBinaryArray(scannedFilePath);
        List<Account> accounts = getAccounts(binaryArrayAccountNumbers);

        String output = "";
        for (Account account : accounts) {
            Account validedAccount = account;
            validedAccount = validCheckSum(account);
            output = output + (validedAccount.getAccountNumber() + " " + validedAccount.getControlVal()).trim() + "\n";
        }
        output = output.trim();
        System.out.println(output);
        return output;
    }

    public static String hostoriy4(String scannedFilePath) throws IOException {

        baseControleNumbers = initBaseControle();
        List<String[][]> binaryArrayAccountNumbers = readFileToBinaryArray(scannedFilePath);
        List<Account> accounts = getAccounts(binaryArrayAccountNumbers);

        String output = "";
        for (Account account : accounts) {
            Account validedAccount = account;
            validedAccount = validCheckSum(account);
            if (validedAccount.getControlVal().equals("")) {
                output = output + (validedAccount.getAccountNumber() + " " + validedAccount.getControlVal() + " ");
            } else {
                validedAccount = correctAccount(validedAccount);
                output = output + (validedAccount.getAccountNumber() + " " + validedAccount.getControlVal() + " ");
                if (validedAccount.getControlVal().equals("AMB")) {
                    List<String> ambivalentAccounts = validedAccount.getCorrectedAccounts().stream()
                            .map(data -> data.getAccountNumber())
                            .collect(Collectors.toList());
                    output = output + (ambivalentAccounts);
                }
            }
            output = output + "\n";
        }
        output = output.trim();
        System.out.println(output);
        return output;
    }
}
