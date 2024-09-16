/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package tms.projects.kataocrbank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author A1047782
 */
public class KataOCRbank {

    public static List<Number> baseControleNumbers;

    public static void main(String[] args) throws IOException {
        Histories.hostoriy4("src/main/resources/scanned_file");
    }

    public static List<String[][]> readFileToBinaryArray(String filePath) throws IOException {

        List<String[][]> accountListBinaryFormat = new ArrayList<>();

        BufferedReader reader = null;
        try {

            String[][] fileBinaryValues = new String[9][27];
            int segmentLineNumber = 0;

            String line;
            reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    String binaryValue;
                    if (line.charAt(i) == ' ') {
                        binaryValue = "0";
                    } else {
                        binaryValue = "1";
                    }
                    fileBinaryValues[segmentLineNumber][i] = binaryValue;
                    //System.out.print(binaryValue);
                }
                //System.out.println();
                segmentLineNumber++;
            }

            String numberSequence = "";
            for (int k = 0; k < segmentLineNumber; k = k + 3) {
                for (int j = 0; j < 27; j++) {
                    for (int i = k; i < k + 3; i++) {
                        numberSequence = numberSequence + fileBinaryValues[i][j];
                    }
                    if (numberSequence.length() == 81) {
                        accountListBinaryFormat.add(convertBinaryStringTo2DStringArray(numberSequence));
                        //System.out.println(numberSequence);
                        numberSequence = "";
                    }
                }
            }

//            for (String[][] account : accountListBinaryFormat) {
//                System.out.println(Arrays.deepToString(account));
//            }
            return accountListBinaryFormat;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return null;
    }

    public static String[][] convertBinaryStringTo2DStringArray(String binaryString) {
        String[][] result = new String[9][9];
        for (int i = 0; i < binaryString.length(); i++) {
            int row = i / 9;
            int col = i % 9;
            result[row][col] = String.valueOf(binaryString.charAt(i));
        }
        return result;
    }

    //history 1
    public static List<Account> getAccounts(List<String[][]> accountListBinaryFormat) {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < accountListBinaryFormat.size(); i++) {
            accounts.add(setupAccount(accountListBinaryFormat.get(i)));
        }
        return accounts;
    }

    //history 2 et 3
    public static Account validCheckSum(Account account) {
        if (account.getAccountNumber().contains("?")) {
            account.setControlVal("ILL");
            return account;
        }

        int checksum = 0;
        for (int i = 0; i < account.getReadNumbers().size(); i++) {
            int multiple = account.getReadNumbers().size() - i;
            checksum += Integer.parseInt(account.getReadNumbers().get(i).getIntVal()) * multiple;
        }
        if (checksum % 11 != 0) {
            account.setControlVal("ERR");
        }
        return account;
    }

    //history 4
    public static Account correctAccount(Account account) {
        List<Account> correctedAccounts = new ArrayList();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String[][] accountBinaryFormatNew = accountNumbersToBinary(account);
                String toReplace = accountBinaryFormatNew[i][j];
                if (toReplace.equals("0")) {
                    accountBinaryFormatNew[i][j] = "1";
                } else {
                    accountBinaryFormatNew[i][j] = "0";
                }
                //System.out.println(Arrays.deepToString(accountBinaryFormatNew));

                Account accountNew = setupAccount(accountBinaryFormatNew);
                //System.out.println(accountNew.getAccountNumber());

                accountNew = validCheckSum(accountNew);
                if (accountNew.getControlVal().equals("")) {
                    correctedAccounts.add(accountNew);
                }
            }
        }
        account.setCorrectedAccounts(correctedAccounts);
        if (correctedAccounts.isEmpty()) {
            account.setControlVal("ILL");
        } else if (correctedAccounts.size() == 1) {
            account.setControlVal("");
            account.setAccountNumber(correctedAccounts.get(0).getAccountNumber());
        } else {
            account.setControlVal("AMB");
        }
        return account;
    }

    public static List<Number> initBaseControle() {
        List<Number> numbers = new ArrayList<>();
        numbers.add(new Number("000000011", "1"));
        numbers.add(new Number("001111010", "2"));
        numbers.add(new Number("000111011", "3"));
        numbers.add(new Number("010010011", "4"));
        numbers.add(new Number("010111001", "5"));
        numbers.add(new Number("011111001", "6"));
        numbers.add(new Number("000100011", "7"));
        numbers.add(new Number("011111011", "8"));
        numbers.add(new Number("010111011", "9"));
        numbers.add(new Number("011101011", "0"));
        return numbers;
    }

    public static Account setupAccount(String[][] accountBinaryFormat) {
        List<Number> numbers = new ArrayList<>();
        String accountNumber = "";
        for (int j = 0; j < 9; j++) {
            String binaryString = String.join("", accountBinaryFormat[j]);

            Number matchedNumber = baseControleNumbers.stream()
                    .filter(number -> number.getBinaryVal().equals(binaryString))
                    .findFirst()
                    .orElse(new Number(binaryString, "?"));
            numbers.add(matchedNumber);
            accountNumber = accountNumber + matchedNumber.getIntVal();
        }
        Account account = new Account();
        account.setReadNumbers(numbers);
        account.setAccountNumber(accountNumber);
        account.setControlVal("");
        return account;
    }

    public static String[][] accountNumbersToBinary(Account account) {
        String[][] binaryValues = new String[9][9];
        for (int i = 0; i < account.getReadNumbers().size(); i++) {
            for (int j = 0; j < account.getReadNumbers().get(i).getBinaryVal().length(); j++) {
                binaryValues[i][j] = "" + account.getReadNumbers().get(i).getBinaryVal().charAt(j);
            }
        }
        return binaryValues;
    }

    public static String formatOutput(String chaine) {
        if (chaine != null && chaine.endsWith("\n")) {
            return chaine.substring(0, chaine.length() - 1);
        }
        return chaine;
    }

}

class Account {

    private List<Number> readNumbers;
    private String accountNumber;
    private String controlVal;
    private List<Account> correctedAccounts;

    public List<Number> getReadNumbers() {
        return readNumbers;
    }

    public void setReadNumbers(List<Number> readNumbers) {
        this.readNumbers = readNumbers;
    }

    public String getControlVal() {
        return controlVal;
    }

    public void setControlVal(String controlVal) {
        this.controlVal = controlVal;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<Account> getCorrectedAccounts() {
        return correctedAccounts;
    }

    public void setCorrectedAccounts(List<Account> correctedAccounts) {
        this.correctedAccounts = correctedAccounts;
    }

}

class Number {

    private String binaryVal;
    private String intVal;

    public Number() {
    }

    public Number(String binaryVal, String intVal) {
        this.binaryVal = binaryVal;
        this.intVal = intVal;
    }

    public String getBinaryVal() {
        return binaryVal;
    }

    public void setBinaryVal(String binaryVal) {
        this.binaryVal = binaryVal;
    }

    public String getIntVal() {
        return intVal;
    }

    public void setIntVal(String intVal) {
        this.intVal = intVal;
    }
}
