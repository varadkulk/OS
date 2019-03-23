import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import sun.plugin2.gluegen.runtime.CPU;

class OS {
    CPU c = new CPU();// Object for class CPU
    RAM r = new RAM();// Object for class RAM
    static long startTime, endTime, totalTime;

    public void exit() {// End of the execution
        double seconds;
        System.out.println("Program Terminated");
        endTime = System.nanoTime();
        totalTime = (endTime - startTime);
        seconds = (double) totalTime / 1_000_000_000.0;// As totalTime is in nano seconds
        System.out.println("Execution Time = " + seconds + " seconds");// Print execution time in seconds
        System.exit(0);
    }// exit

    public void terminate(int x) {
        switch (x) {
        case 0: {
            System.out.println("No error");
            break;
        }
        case 1: {
            System.out.println("ERROR:Out of data");
            break;
        }
        case 2: {
            System.out.println("ERROR:Line Limit Exceeded");
            break;
        }
        case 3: {
            System.out.println("ERROR:Time Limit Exceeded");
            break;
        }
        case 4: {
            System.out.println("ERROR:Operation Code Error");
            break;
        }
        case 5: {
            System.out.println("ERROR:Operand Error");
            break;
        }
        case 6: {
            System.out.println("ERROR:Invalid Page Fault");
            break;
        }
        case 7: {
            System.out.println("ERROR:Time Limit Exceeded");
            System.out.println("ERROR:Operation Code Error");
            break;
        }
        case 8: {
            System.out.println("ERROR:Time Limit Exceeded");
            System.out.println("ERROR:Operand Error");
            break;
        }
        default:
            break;
        }
        exit();
    }// terminate

    public void mos(BufferedReader br, BufferedWriter buffer, int ti, int si, int pi) {
        if (ti == 0 && si == 1) {
        } 
        else if (ti == 0 && si == 2) {
        } 
        else if (ti == 0 && si == 3)
            terminate(0);
        else if (ti == 2 && si == 1)
            terminate(3);
        else if (ti == 2 && si == 2) {
        } 
        else if (ti == 2 && si == 3)
            terminate(0);
        else if (ti == 0 && pi == 1)
            terminate(4);
        else if (ti == 0 && pi == 2)
            terminate(5);
        else if (ti == 0 && pi == 3) {
        } else if (ti == 2 && pi == 1)
            terminate(7);// 3,5
        else if (ti == 2 && pi == 2)
            terminate(8);// 3,6
        else if (ti == 2 && pi == 3)
            terminate(3);
        else if (ti == 0 && si == 0 && pi == 0)
            terminate(0);
    }// mos

    public String file_input(String text) throws IllegalArgumentException {
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

    public boolean check_num_as_char(char x) {
        if (x == '0' || x == '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6' || x == '7' || x == '8'
                || x == '9')
            return false;
        return true;
    }// check_num_as_char

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
        StringBuilder sb_ni = new StringBuilder();
        sb_ni.append(st.charAt(8));
        sb_ni.append(st.charAt(9));
        sb_ni.append(st.charAt(10));
        sb_ni.append(st.charAt(11));
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
            // location+=10;
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
                buffer.close();
                exit();
            } else {
                StringBuilder sb_reg = new StringBuilder();
                sb_reg.append(c.IR[2]);
                sb_reg.append(c.IR[3]);
                s_reg = sb_reg.toString();
                reg = Integer.parseInt(s_reg);
                int k = 0;
                if (c.IR[0] == 'G' && c.IR[1] == 'D') {// GD command
                    st = br.readLine();
                    l = st.length();
                    for (int j = 0; j < l; j++) {
                        r.ram[reg][k] = st.charAt(j);
                        k++;
                        if (k == 4) {
                            k = 0;
                            reg++;
                        }
                    }
                } else {
                    StringBuilder sb_o = new StringBuilder();
                    sb_o.append(c.IR[0]);
                    sb_o.append(c.IR[1]);
                    s_o = sb_o.toString();
                    switch (s_o) {
                    case "PD": {
                        while (r.ram[reg][k] != '-') {
                            buffer.write(r.ram[reg][k]);
                            k++;
                            if (k == 4) {
                                k = 0;
                                reg++;
                            }
                        }
                        buffer.newLine();
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
                    }
                }
            }
        }
        br.close();
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
/*
 * public void Error(File input)throws IOException {
 * 
 * 
 * c.initialize();//Initialize CPU class r.initialize();//Initialize RAM class
 * 
 * BufferedReader br = new BufferedReader(new
 * FileReader(input));//BufferedReader initialize String
 * st,s_program_number,s_ni,s_reg,s_program_number_end; int
 * l,ni=0,instruction_line=1,max_instruction_line=2,program_number=0,reg,
 * program_number_end;
 * 
 * st = br.readLine();//Line 1 stored st=st.trim();//Trim sting to remove spaces
 * at the start and the end l=st.length();//Calculate length of first line of
 * input file
 * 
 * if(l==10)//Check for 1st line {
 * 
 * if
 * (st.charAt(0)!='$'||st.charAt(1)!='A'||st.charAt(2)!='M'||st.charAt(3)!='J')
 * //Error checking for $AMJ {
 * System.out.println("ERROR line 1: Start of program should be '$AMJ' ");
 * exit(); }
 * 
 * if(check_num_as_char(st.charAt(4))||check_num_as_char(st.charAt(5))||
 * check_num_as_char(st.charAt(6))||check_num_as_char(st.charAt(7))) {
 * System.out.println("ERROR line 1: Program number should only contain numbers"
 * ); exit(); }
 * 
 * StringBuilder sb_program_number = new StringBuilder();
 * sb_program_number.append(st.charAt(4));
 * sb_program_number.append(st.charAt(5));
 * sb_program_number.append(st.charAt(6));
 * sb_program_number.append(st.charAt(7));
 * s_program_number=sb_program_number.toString(); program_number =
 * Integer.parseInt(s_program_number);
 * 
 * if(check_num_as_char(st.charAt(8))||check_num_as_char(st.charAt(9))) {
 * System.out.println("ERROR line 1: Program number should only contain numbers"
 * ); exit(); }
 * 
 * StringBuilder sb_ni = new StringBuilder(); sb_ni.append(st.charAt(8));
 * sb_ni.append(st.charAt(9)); s_ni=sb_ni.toString(); ni =
 * Integer.parseInt(s_ni);//number of instructions from input file
 * 
 * if (ni==0) { System.out.println("ERROR line 1: Instrucion number is 0");
 * exit(); } else if (ni>20) {
 * System.out.println("ERROR line 1: Instrucion number is more than 20");
 * exit(); }
 * 
 * if(ni>10) max_instruction_line++; }
 * 
 * else if (l<10) { System.out.println("ERROR line 1: Charaters missing");
 * exit(); }
 * 
 * else { System.out.println("ERROR line 1: Extra Charaters"); exit(); }
 * 
 * instruction_line++;
 * 
 * while(instruction_line<=max_instruction_line)// {
 * 
 * st = br.readLine(); st=st.trim(); l=st.length();
 * 
 * if(l>40) { System.out.println("ERROR line "
 * +instruction_line+": more than 40 characters in one line"); exit(); }
 * 
 * if(l%4!=0) {
 * System.out.println("ERROR line "+instruction_line+": Instruction incomplete"
 * ); exit(); }
 * 
 * if (instruction_line==max_instruction_line ) {
 * 
 * if(instruction_line==3) {
 * 
 * if(((l/4)+10)!=ni) { System.out.println("ERROR line "
 * +instruction_line+": number of instructions not matching to instruction number in line 1"
 * ); exit(); }
 * 
 * }
 * 
 * else if((l/4)!=ni) { System.out.println("ERROR line "
 * +instruction_line+": number of instructions not matching to instruction number in line 1"
 * ); exit(); }
 * 
 * if(st.charAt(l-4)!='H'||st.charAt(l-3)!='A'||st.charAt(l-2)!='L'||st.charAt(l
 * -1)!='T') {
 * System.out.println("ERROR line "+instruction_line+": HALT statement missing"
 * ); exit(); } }
 * 
 * int i=0,k=0;
 * 
 * while(i<l) { for (int x=0;x<4;x++) { if(i<l) { r.ram[k][x]=st.charAt(i); i++;
 * } else x=4; } k++; } instruction_line++; }
 * 
 * st = br.readLine(); st=st.trim(); l=st.length();
 * 
 * if(l!=4||st.charAt(0)!='$'||st.charAt(1)!='D'||st.charAt(2)!='T'||st.charAt(3
 * )!='A') //Error checking for $DTA { System.out.println("ERROR line "
 * +instruction_line+": should contaion only $DTA "); exit(); }
 * 
 * for (int i=0;i<ni;i++) { for(int j=0;j<4;j++) c.IR[j]=r.ram[i][j];
 * c.IC++;//Instruction counter increment
 * 
 * if (c.IR[0]=='H'&&c.IR[1]=='A'&&c.IR[2]=='L'&&c.IR[3]=='T')//HALT condition {
 * 
 * st = br.readLine(); st=st.trim(); l=st.length();
 * 
 * if(l!=8||st.charAt(0)!='$'||st.charAt(1)!='E'||st.charAt(2)!='N'||st.charAt(3
 * )!='D') //Error checking for $DTA {
 * System.out.println("ERROR line "+instruction_line+": should start with $END "
 * ); exit(); }
 * 
 * if(check_num_as_char(st.charAt(4))||check_num_as_char(st.charAt(5))||
 * check_num_as_char(st.charAt(6))||check_num_as_char(st.charAt(7))) {
 * System.out.println("ERROR line "
 * +instruction_line+": Program number should only contain numbers"); exit(); }
 * 
 * StringBuilder sb_program_number_end = new StringBuilder();
 * sb_program_number_end.append(st.charAt(4));
 * sb_program_number_end.append(st.charAt(5));
 * sb_program_number_end.append(st.charAt(6));
 * sb_program_number_end.append(st.charAt(7));
 * s_program_number_end=sb_program_number_end.toString(); program_number_end =
 * Integer.parseInt(s_program_number_end);
 * 
 * if (program_number!=program_number_end) { System.out.println("ERROR line "
 * +instruction_line+": Program number Not matching"); exit(); } } else {
 * 
 * if(check_num_as_char(c.IR[2])||check_num_as_char(c.IR[3])) {
 * System.out.println("Incorrect Operand "); exit(); }
 * 
 * StringBuilder sb_reg = new StringBuilder(); sb_reg.append(c.IR[2]);
 * sb_reg.append(c.IR[3]); s_reg=sb_reg.toString(); reg =
 * Integer.parseInt(s_reg);
 * 
 * int k=0;
 * 
 * if (c.IR[0]=='G'&&c.IR[1]=='D')//GD command { st = br.readLine();
 * l=st.length();
 * 
 * instruction_line++;
 * 
 * for(int j=0;j<l;j++) { if(st.charAt(j)=='-') {
 * System.out.println("Invalid character dectected '-' "); exit(); }
 * 
 * r.ram[reg][k]=st.charAt(j); k++;
 * 
 * if(k==4) { k=0; reg++; } } }
 * 
 * else if
 * ((c.IR[0]=='P'&&c.IR[1]=='D')||(c.IR[0]=='C'||c.IR[0]=='L'||c.IR[0]=='S')&&c.
 * IR[1]=='R');//CR, LR & SR command
 * 
 * else if (c.IR[0]=='B'&&c.IR[1]=='T')//BT command { if(reg<0||reg>=ni) {
 * System.out.println("The instruction is not present "); exit(); } } } }
 * }//Error() method
 */