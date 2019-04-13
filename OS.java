import java.io.*;
import java.util.*;

class OS {
    CPU c = new CPU();// Object for class CPU
    RAM r = new RAM();// Object for class RAM
    static long startTime, endTime, totalTime;
    int pi = 0, si = 3, ti = 0;

    public void exit() {// End of the execution
        double seconds;
        System.out.println("Program Terminated");
        endTime = System.nanoTime();
        totalTime = (endTime - startTime);
        seconds = (double) totalTime / 1_000_000_000.0;// As totalTime is in nano seconds
        System.out.println("Execution Time = " + seconds + " seconds");// Print execution time in seconds
        System.exit(0);
    }// exit

    /*
     * public void terminate(int x) { switch (x) { case 0: { break; } case 1: {
     * System.out.println("ERROR:Out of data"); break; } case 2: {
     * System.out.println("ERROR:Line Limit Exceeded"); break; } } }// terminate
     */

    public void mos(BufferedReader br, BufferedWriter buffer, int reg, int k) throws IOException {
        if (ti == 0 && si == 1) {
            String st = br.readLine();
            int l = st.length();
            for (int j = 0; j < l; j++) {
                r.ram[reg][k] = st.charAt(j);
                k++;
                if (k == 4) {
                    k = 0;
                    reg++;
                }
                si = 3;
            }
        } else if (ti == 0 && si == 2) {
            while (r.ram[reg][k] != '-') {
                buffer.write(r.ram[reg][k]);
                k++;
                if (k == 4) {
                    k = 0;
                    reg++;
                }
            }
            buffer.newLine();
            si = 3;
        } else if (ti == 0 && si == 3) {
            buffer.close();
            exit();
        } else if (ti == 2 && si == 1) {
            System.out.println("ERROR:Time Limit Exceeded");
            exit();
        } else if (ti == 2 && si == 2) {
            while (r.ram[reg][k] != '-') {
                buffer.write(r.ram[reg][k]);
                k++;
                if (k == 4) {
                    k = 0;
                    reg++;
                }
            }
            buffer.newLine();
            si = 0;
            System.out.println("ERROR:Time Limit Exceeded");
            exit();
        } else if (ti == 2 && si == 3) {
            buffer.close();
            exit();
        } else if (ti == 0 && pi == 1) {
            System.out.println("ERROR:Operation Code Error");
            exit();
        } else if (ti == 0 && pi == 2) {
            System.out.println("ERROR:Operand Error");
            exit();
        } else if (ti == 0 && pi == 3) {
            System.out.println("ERROR:Invalid Page Fault");
            exit();
        } else if (ti == 2 && pi == 1) {
            System.out.println("ERROR:Time Limit Exceeded");
            System.out.println("ERROR:Operation Code Error");
            exit();
        } else if (ti == 2 && pi == 2) {
            System.out.println("ERROR:Time Limit Exceeded");
            System.out.println("ERROR:Operand Error");
            exit();
        } else if (ti == 2 && pi == 3)
            exit();// terminate(3);
        else if (ti == 0 && si == 0 && pi == 0)
            exit();
    }// mos

    public String file_input(String text) {
        Scanner sc = new Scanner(System.in);
        OS OS = new OS();// Object for class OS
        boolean c = true;
        String s = "";// Because of stupid errors of initialization
        while (c) {
            System.out.print("Enter " + text + " file: ");
            s = sc.nextLine();
            c = OS.check_file_name(s);
        }
        sc.close();
        return s;
    }// file_input

    public boolean check_file_name(String s) {
        int l = s.length(), i = 0;
        while (i < l) {
            if (s.charAt(i) == '.' && i < l - 1)
                return false;
            i++;
        }
        System.out.println("wrong format!!!");
        return true;
    }// check_file_name

    public boolean check_char_as_num(char x) {
        if (x == '0' || x == '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6' || x == '7' || x == '8'
                || x == '9')
            return false;
        return true;
    }// check_char_as_num

    public void run(File input, FileWriter output) throws IOException {
        c.initialize();// Initialize CPU class
        r.initialize();// Initialize RAM class
        c.print();// Print CPU class
        r.print();// Print RAM class
        BufferedReader br = new BufferedReader(new FileReader(input));// BufferedReader initialize
        BufferedWriter buffer = new BufferedWriter(output);
        String st, s_program_number, s_ni, s_reg, s_o;
        int l, ni = 0, instruction_line = 1, max_instruction_line = 2, program_number, reg;
        st = br.readLine();// Line 1 stored
        st = st.trim();// Trim sting to remove spaces at the start and the end
        l = st.length();// Calculate length of first line of input file

        if (st.charAt(0) != '$' || st.charAt(1) != 'A' || st.charAt(2) != 'M' || st.charAt(3) != 'J')
        // Error checking for $AMJ
        {
            System.out.println("ERROR line 1: Start of program should be '$AMJ' ");
            exit();
        }

        if (check_char_as_num(st.charAt(4)) || check_char_as_num(st.charAt(5)) || check_char_as_num(st.charAt(6))
                || check_char_as_num(st.charAt(7))) {
            System.out.println("ERROR line 1: Program number should only contain numbers");
            exit();
        }

        StringBuilder sb_program_number = new StringBuilder();
        for (int i = 4; i <= 7; i++)
            sb_program_number.append(st.charAt(i));
        s_program_number = sb_program_number.toString();
        program_number = Integer.parseInt(s_program_number);

        if (check_char_as_num(st.charAt(8)) || check_char_as_num(st.charAt(9))) {
            System.out.println("ERROR line 1: Program number should only contain numbers");
            exit();
        }
        StringBuilder sb_ni = new StringBuilder();
        for (int i = 8; i <= 11; i++)
            sb_ni.append(st.charAt(i));
        s_ni = sb_ni.toString();
        ni = Integer.parseInt(s_ni);// number of instructions from input file
        if (ni > 10)
            max_instruction_line++;
        instruction_line++;
        int location = 0;
        while (instruction_line <= max_instruction_line) {
            st = br.readLine();
            st = st.trim();
            l = st.length();
            int i = 0;
            while (i < l) {
                for (int x = 0; x < 4; x++) {
                    if (i < l) {
                        r.ram[location][x] = st.charAt(i);
                        i++;
                    } else
                        x = 4;
                }
                c.print();
                r.print();
                location++;
            }
            instruction_line++;
        }
        st = br.readLine();
        st = st.trim();
        l = st.length();
        for (int i = 0; i < ni; i++) {
            for (int j = 0; j < 4; j++)
                c.IR[j] = r.ram[i][j];
            c.IC++;// Instruction counter increment
            c.print();
            r.print();
            if (c.IR[0] == 'H' && c.IR[1] == 'A' && c.IR[2] == 'L' && c.IR[3] == 'T')// HALT condition
            {
                mos(br, buffer, 0, 0);
                exit();
            } else {
                StringBuilder sb_reg = new StringBuilder();
                for (int j = 2; j <= 3; j++) {
                    if (!check_char_as_num(c.IR[j]))
                        sb_reg.append(c.IR[j]);
                    else {
                        pi = 2;
                        mos(br, buffer, 0, 0);
                    }
                }
                s_reg = sb_reg.toString();
                reg = Integer.parseInt(s_reg);
                int k = 0;
                StringBuilder sb_o = new StringBuilder();
                sb_o.append(c.IR[0]);
                sb_o.append(c.IR[1]);
                s_o = sb_o.toString();
                switch (s_o) {
                case "GD": {
                    si = 1;
                    mos(br, buffer, reg, k);
                }
                case "PD": {
                    si = 2;
                    mos(br, buffer, reg, k);
                    break;
                }
                case "CR": {
                    for (int j = 0; j < 4; j++) {
                        if (c.r[j] == r.ram[reg][j])
                            c.t = true;
                        else {
                            c.t = false;
                            j = 4;
                        }
                    }
                    break;
                }
                case "LR": {
                    for (int j = 0; j < 4; j++)
                        c.r[j] = r.ram[reg][j];
                    break;
                }
                case "SR": {
                    for (int j = 0; j < 4; j++)
                        r.ram[reg][j] = c.r[j];
                    break;
                }
                case "BT": {
                    c.print();
                    r.print();
                    if (c.t)
                        i = reg - 1;
                    break;
                }
                default: {
                    pi = 1;
                    mos(br, buffer, reg, k);
                }
                }

            }
        }
    }// run

    public static void main(String[] args) throws IOException {
        OS OS = new OS();// Object for class OS
        // File input = new File(OS.file_input("input"));// File initialize
        // FileWriter output = new FileWriter(OS.file_input("output"));
        File input = new File("input.vk");// File initialize
        FileWriter output = new FileWriter("output.vk");
        startTime = System.nanoTime();
        OS.run(input, output);
    }// main
}// class