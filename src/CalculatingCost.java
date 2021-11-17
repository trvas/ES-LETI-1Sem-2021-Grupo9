
/**Date : November  15-2021
* @author Paulo.Palma
* @version 1.0
*/

/**
 * Classe do calculo do custo das horas
 *
 */

public class CalculatingCost {

	private int horas;
	private int custo;

	/**
	 * Construtor da classe
	 *
	 */

	public CalculatingCost(int horas, int custo) {
		super();
		this.horas = horas;
		this.custo = custo;
	}

	/**
	 * Segundo Contrutor 
	 *
	 */
	public CalculatingCost() {

		this.custo = 20;

	}

	/**
	 * Este metodo recebe como parametro as horas e devolve o valor do trabalho
	 * 
	 * @param horas
	 * @return devolve o valor das horas 
	 */

	public int getHoras(int horas) {

		return horas * getCusto();

	}

	public void setHoras(int horas) {
		this.horas = horas;
	}

	/**
	 * metodo do obter o custo
	 *
	 */

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
		System.out.println(" O custo total será de " + c1.getHoras(20));

	}

}
