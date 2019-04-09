class RAM {
    char[][] ram = new char[300][4];// RAM data a 2d array

    void print() {
        System.out.println();
        for (int i = 0; i < 111; i++)
            System.out.print("-");
        System.out.print("RAM");
        for (int i = 0; i < 111; i++)
            System.out.print("-");
        for (int i = 0; i < 300; i++) {
            if (i % 25 == 0) {
                System.out.println();
            }
            if (i < 10)
                System.out.print(" ");
            if (i < 100)
                System.out.print(" ");
            System.out.print(i + ":");
            for (int j = 0; j < 4; j++)
                System.out.print(ram[i][j]);
            System.out.print(" ");
        }
        System.out.println();
        for (int i = 0; i < 225; i++)
            System.out.print("-");
        System.out.println();
    }

    void initialize() {
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 4; j++)
                ram[i][j] = '-';
        }
    }
}
