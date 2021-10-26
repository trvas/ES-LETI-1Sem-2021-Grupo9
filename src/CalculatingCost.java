import java.util.Scanner;

public class CalculatingCost {

	private int horas;
	private int custo;

	public CalculatingCost(int horas, int custo) {
		super();
		this.horas = horas;
		this.custo = custo;
	}

	public CalculatingCost() {

		this.custo = 20;

	}

	public int getHoras() {

		Scanner ler = new Scanner(System.in);

		System.out.printf("Informa o número de horas que trabalhas-te:\n");
		horas = ler.nextInt();

		ler.close();

		return horas * getCusto();

	}

	public void setHoras(int horas) {
		this.horas = horas;
	}

	public int getCusto() {
		return custo;
	}

	public void setCusto(int custo) {
		this.custo = custo;
	}

	@Override
	public String toString() {
		return "CalculatingCost [horas=" + horas + ", custo=" + custo + "]";
	}

	public static void main(String[] args) {

		CalculatingCost c1 = new CalculatingCost();
		System.out.println(" O custo total será de " + c1.getHoras());

	}

}
