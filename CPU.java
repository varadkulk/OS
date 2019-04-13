class CPU {
    char[] r = new char[4];// General register
    boolean t;// Toggle
    int IC;// Instruction Counter
    char[] IR = new char[4];// Instruction Register

    void print() {
        for (int i = 0; i < 111; i++)
            System.out.print("-");
        System.out.print("CPU");
        for (int i = 0; i < 111; i++)
            System.out.print("-");
        System.out.print("\nRegister: ");
        for (int i = 0; i < 4; i++)
            System.out.print(r[i]);
        System.out.print("\nToggle: " + t + "\nInstruction Counter: " + IC + "\nInstruction Register: ");
        for (int i = 0; i < 4; i++)
            System.out.print(IR[i]);
        System.out.println();
        for (int i = 0; i < 225; i++)
            System.out.print("-");
    }

    void initialize() {
        IC = 0;
        for (int i = 0; i < 4; i++)
            r[i] = '-';
        for (int i = 0; i < 4; i++)
            IR[i] = '-';
    }
}
