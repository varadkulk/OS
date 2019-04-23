import java.io.*;
import java.util.*;

class OS {
	CPU c = new CPU();// Object for class CPU
	RAM r = new RAM();// Object for class RAM
	static long startTime;
	int pi = 0, si = 3, ti = 0, status[] = new int[30], frame = 0;

	public void exit(BufferedWriter buffer) throws IOException {// End of the execution
		System.out.println("Program Terminated");
		buffer.close();
		// Print execution time in seconds
		System.out.println("Execution Time = " + ((float) ((System.nanoTime()) - startTime) / 1000000000) + " seconds");
		System.exit(0);
	}// exit

	public int random(int r) {
		Random rand = new Random();
		r = rand.nextInt(r);
		return r;
	}// random

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

	public void print(int PTBR) {
		c.print();// Print CPU class
		System.out.print("\nPage Table: " + PTBR);
		r.print();// Print RAM class
	}// print

	public void initializePT(int p) {
		int x = p;
		for (int i = 0; i < 30; i++)
			status[i] = 1;
		status[p] = 0;
		p *= 10;
		for (int i = 0; i < 10; i++) {
			r.ram[p + i][0] = '*';
			r.ram[p + i][1] = ' ';
			while (status[x] == 0)
				x = random(30);
			r.ram[p + i][2] = (char) ((x / 10) + '0');
			r.ram[p + i][3] = (char) ((x % 10) + '0');
			status[x] = 0;
		}
	}// initializePT

	public int returnframe(int rf, int p) {
		int y = ((r.ram[(p * 10) + rf][2] - '0') * 10) + ((r.ram[(p * 10) + rf][3]) - '0');
		return y;
	}

	public void PD(BufferedWriter buffer, int reg, int k, int PTBR) throws IOException {
		int rf = 1;
		int y = returnframe(rf, PTBR) * 10;
		while (r.ram[PTBR * 10 + rf][0] == '$') {
			if (r.ram[y][0] == '$' && returnframe(0, y / 10) == reg) {
				y++;
				while (r.ram[y][k] != '-') {
					buffer.write(r.ram[y][k]);
					k++;
					if (k == 4) {
						k = 0;
						y++;
					}
				}
			} else {
				rf++;
				y = returnframe(rf, PTBR) * 10;
			}
		}
		buffer.newLine();
	}

	public void mos(BufferedReader br, BufferedWriter buffer, int reg, int k, int PTBR) throws IOException {
		if (ti == 0 && si == 1) {
			int y = 0, i;
			for (i = 1; i < 10; i++) {
				y = returnframe(i, PTBR) * 10;
				if (r.ram[y][0] == '$' && returnframe(0, y / 10) == reg) {
					i = 11;
				}
			}
			if (i == 10) {
				r.ram[PTBR * 10 + frame][0] = '$';
				y = returnframe(frame++, PTBR) * 10;
				r.ram[y][0] = '$';
				r.ram[y][1] = ' ';
				r.ram[y][2] = (char) ((reg / 10) + '0');
				r.ram[y][3] = (char) ((reg % 10) + '0');
			}
			y++;
			String st = br.readLine();
			int l = st.length();
			for (int j = 0; j < l; j++) {
				if (y % 10 == 0) {
					r.ram[PTBR * 10 + frame][0] = '$';
					y = returnframe(frame++, PTBR) * 10;
					r.ram[y][0] = '$';
					r.ram[y][1] = ' ';
					r.ram[y][2] = (char) ((reg / 10) + '0');
					r.ram[y][3] = (char) ((reg % 10) + '0');
					y++;
				}
				r.ram[y][k] = st.charAt(j);
				k++;
				if (k == 4) {
					k = 0;
					y++;
				}
				si = 3;
			}
		} else if (ti == 0 && si == 2) {
			PD(buffer, reg, k, PTBR);
			si = 3;
		} else if (ti == 0 && si == 3)
			exit(buffer);
		else if (ti == 2 && si == 1) {
			buffer.write("ERROR:Time Limit Exceeded");
			exit(buffer);
		} else if (ti == 2 && si == 2) {
			PD(buffer, reg, k, PTBR);
			si = 0;
			buffer.write("ERROR:Time Limit Exceeded");
			exit(buffer);
		} else if (ti == 2 && si == 3)
			exit(buffer);
		if (ti == 0 && pi == 1) {
			buffer.write("ERROR:Operation Code Error");
			exit(buffer);
		} else if (ti == 0 && pi == 2) {
			buffer.write("ERROR:Operand Error");
			exit(buffer);
		} else if (ti == 0 && pi == 3) {
			buffer.write("ERROR:Invalid Page Fault");
			exit(buffer);
		} else if (ti == 2 && pi == 1) {
			buffer.write("ERROR:Time Limit Exceeded");
			buffer.newLine();
			buffer.write("ERROR:Operation Code Error");
			exit(buffer);
		} else if (ti == 2 && pi == 2) {
			buffer.write("ERROR:Time Limit Exceeded");
			buffer.newLine();
			buffer.write("ERROR:Operand Error");
			exit(buffer);
		} else if (ti == 2 && pi == 3)
			exit(buffer);// terminate(3);
		else if (ti == 0 && si == 0 && pi == 0)
			exit(buffer);
	}// mos

	public void run(File input, FileWriter output) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(input));// BufferedReader initialize
		BufferedWriter buffer = new BufferedWriter(output);
		String st, s_program_number, s_ni, s_reg, s_o;
		int l, ni = 0, instruction_line = 1, max_instruction_line = 2, program_number, reg, PTBR = random(30);

		c.initialize();// Initialize CPU class
		r.initialize();// Initialize RAM class
		initializePT(PTBR);
		print(PTBR);
		st = br.readLine();// Line 1 stored
		st = st.trim();// Trim sting to remove spaces at the start and the end
		l = st.length();// Calculate length of first line of input file
		// Error checking for $AMJ
		if (st.charAt(0) != '$' || st.charAt(1) != 'A' || st.charAt(2) != 'M' || st.charAt(3) != 'J') {
			System.out.println("ERROR line 1: Start of program should be '$AMJ' ");
			exit(buffer);
		}
		if (check_char_as_num(st.charAt(4)) || check_char_as_num(st.charAt(5)) || check_char_as_num(st.charAt(6))
				|| check_char_as_num(st.charAt(7))) {
			System.out.println("ERROR line 1: Program number should only contain numbers");
			exit(buffer);
		}
		StringBuilder sb_program_number = new StringBuilder();
		for (int i = 4; i <= 7; i++)
			sb_program_number.append(st.charAt(i));
		s_program_number = sb_program_number.toString();
		program_number = Integer.parseInt(s_program_number);
		if (check_char_as_num(st.charAt(8)) || check_char_as_num(st.charAt(9))) {
			System.out.println("ERROR line 1: Program number should only contain numbers");
			exit(buffer);
		}
		StringBuilder sb_ni = new StringBuilder();
		for (int i = 8; i <= 11; i++)
			sb_ni.append(st.charAt(i));
		s_ni = sb_ni.toString();
		ni = Integer.parseInt(s_ni);// number of instructions from input file
		if (ni > 10)
			max_instruction_line++;
		instruction_line++;
		int location = PTBR;
		while (instruction_line <= max_instruction_line) {
			r.ram[(PTBR * 10) + frame][0] = '$';
			location = returnframe(frame++, PTBR) * 10;
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
				print(PTBR);
				location++;
			}
			instruction_line++;
		}
		st = br.readLine();
		st = st.trim();
		l = st.length();
		int z = returnframe(0, PTBR) * 10;
		System.out.println(z);
		for (int i = z; i < (ni + z); i++) {
			if (i % 10 == 0 && i != z) {
				i = returnframe(1, PTBR) * 10;
				z = i;
				ni -= 10;
			}
			for (int j = 0; j < 4; j++)
				c.IR[j] = r.ram[i][j];
			c.IC++;// Instruction counter increment
			print(PTBR);
			if (c.IR[0] == 'H' && c.IR[1] == 'A' && c.IR[2] == 'L' && c.IR[3] == 'T')// HALT condition
			{
				mos(br, buffer, 0, 0, PTBR);
				exit(buffer);
			} else {
				StringBuilder sb_reg = new StringBuilder();
				for (int j = 2; j <= 3; j++) {
					if (!check_char_as_num(c.IR[j]))
						sb_reg.append(c.IR[j]);
					else {
						pi = 2;
						mos(br, buffer, 0, 0, PTBR);
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
					mos(br, buffer, reg, k, PTBR);
				}
				case "PD": {
					si = 2;
					mos(br, buffer, reg, k, PTBR);
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
					int y = 0, x;
					for (x = 1; x < 10; x++) {
						y = returnframe(x, PTBR) * 10;
						if (r.ram[y][0] == '$' && returnframe(0, y / 10) == reg) {
							x = 11;
							for (int j = 0; j < 4; j++)
								c.r[j] = r.ram[y + 1][j];
						}
					}
					break;
				}
				case "SR": {
					int y = 0, x;
					for (x = 1; x < 10; x++) {
						y = returnframe(x, PTBR) * 10;
						if (r.ram[y][0] == '$' && returnframe(0, y / 10) == reg) {
							x = 11;
							for (int j = 0; j < 4; j++)
								r.ram[y+1][j] = c.r[j];
						}
					}
					if (x == 10) {
						r.ram[PTBR * 10 + frame][0] = '$';
						y = returnframe(frame++, PTBR) * 10;
						r.ram[y][0] = '$';
						r.ram[y][1] = ' ';
						r.ram[y][2] = (char) ((reg / 10) + '0');
						r.ram[y][3] = (char) ((reg % 10) + '0');
						y++;
						for (int j = 0; j < 4; j++)
							r.ram[y][j] = c.r[j];
					}
					break;
				}
				case "BT": {
					print(PTBR);
					if (c.t)
						i = reg - 1;
					break;
				}
				default: {
					pi = 1;
					mos(br, buffer, reg, k, PTBR);
				}
				}
			}
		}
	}// run

	public static void main(String[] args) throws IOException {
		OS OS = new OS();// Object for class OS

		PrintStream fileOut = new PrintStream("log.vk");
		System.setOut(fileOut);

		// File input = new File(OS.file_input("input"));// File initialize
		// FileWriter output = new FileWriter(OS.file_input("output"));
		File input = new File("input.vk");// File initialize
		FileWriter output = new FileWriter("output.vk");

		startTime = System.nanoTime();
		OS.run(input, output);
	}// main
}// class