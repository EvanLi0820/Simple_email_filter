import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        File ifile = new File("input.txt");
        File ofile = new File("output.txt");

        Scanner sc = new Scanner(ifile);
        FileWriter printer = new FileWriter(ofile);

        while (sc.hasNextLine()) {
            String emailtext = sc.nextLine();

            // To Lower Case
            emailtext = emailtext.toLowerCase();
            emailtext = emailtext.trim();

            // Replace
            int atpos1 = emailtext.lastIndexOf("@");
            int atpos2 = emailtext.lastIndexOf("_at_");

            if (atpos2 > -1 && atpos1 < atpos2) {
                String pre = emailtext.substring(0, atpos2);
                pre += '@';
                pre += emailtext.substring(atpos2 + 4);
                emailtext = pre;
            }
            int atpos = emailtext.lastIndexOf('@');

            int dotpos2 = emailtext.lastIndexOf("_dot_");

            if (dotpos2 > -1 && atpos < dotpos2) {
                String pre = emailtext.substring(0, dotpos2);
                pre += '.';
                pre += emailtext.substring(dotpos2 + 5);
                emailtext = pre;
            }

            printer.write(emailtext);

            // Check '@'
            if (atpos < 1) {
                printer.write(" <- Missing @ symbol\n");
                continue;
            }

            // Check Address
            String address = emailtext.substring(0, atpos);
            boolean isSymbol = false;
            boolean isAddress = true;
            for (int i = 0; i < address.length(); i++) {
                Character c = address.charAt(i);
                if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                    isSymbol = false;
                    continue;
                }

                if (!isSymbol && (c == '_' || c == '-' || c == '.')) {
                    isSymbol = true;
                    continue;
                }

                isAddress = false;
                break;
            }

            if (!isAddress) {
                printer.write(" <- Invalid address name\n");
                continue;
            }

            // Check domain
            String domain = emailtext.substring(atpos + 1);

            if (domain.charAt(0) != '[') {
                boolean isDomain = true;
                isSymbol = false;
                for (int i = 0; i < domain.length(); i++) {
                    Character c = domain.charAt(i);
                    if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                        isSymbol = false;
                        continue;
                    }

                    if (!isSymbol && c == '.') {
                        isSymbol = true;
                        continue;
                    }

                    isDomain = false;
                    break;
                }

                if (!isDomain) {
                    printer.write(" <- Invalid domain name\n");
                    continue;
                }
            }

            // Check co.nz com.au co.ca com co.us co.uk
            boolean extension = false;

            if (emailtext.substring(emailtext.length() - 6).contentEquals(".co.nz"))
                extension = true;
            if (emailtext.substring(emailtext.length() - 7).contentEquals(".com.au"))
                extension = true;
            if (emailtext.substring(emailtext.length() - 6).contentEquals(".co.ca"))
                extension = true;
            if (emailtext.substring(emailtext.length() - 4).contentEquals(".com"))
                extension = true;
            if (emailtext.substring(emailtext.length() - 6).contentEquals(".co.us"))
                extension = true;
            if (emailtext.substring(emailtext.length() - 6).contentEquals(".co.uk"))
                extension = true;

            int brackstart = emailtext.indexOf('[');
            if (brackstart > 1 && extension) {
                printer.write(" <- Invalid domain name\n");
                continue;
            }

            if (brackstart > 1 && emailtext.charAt(emailtext.length() - 1) == ']' && !extension) {
                String ipaddr = emailtext.substring(brackstart + 1, emailtext.length() - 1);
                String[] addrs = ipaddr.split("\\.");

                if (addrs.length == 4) {
                    boolean errorip = false;
                    try {
                        Integer addr0 = Integer.parseInt(addrs[0]);
                        if (addr0 < 1 || addr0 > 256) {
                            errorip = true;
                        }
                        for (int i = 1; i < addrs.length; i++) {
                            Integer addr = Integer.parseInt(addrs[i]);
                            if (addr < 0 || addr > 256) {
                                errorip = true;
                            }
                        }
                    } catch (Exception e) {
                        errorip = true;
                    }

                    if (!errorip)
                        extension = true;
                }
            }

            if (!extension) {
                printer.write(" <- Invalid extension\n");
                continue;
            }

            printer.write("\n");
        }

        sc.close();
        printer.close();
    }
}
