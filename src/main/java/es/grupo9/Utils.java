package es.grupo9;

public class Utils{


    public static int getCusto(int horas, int custo) {
        return horas * custo;
    }

    // custo default = 20
    public static int getCusto(int horas) {
        getCusto(horas, 20);
    }

    @Override
    public String toString() {
        return "CalculatingCost [horas=" + horas + ", custo=" + custo + "]";
    }








    public static void utils(String[] args) {
    }
}