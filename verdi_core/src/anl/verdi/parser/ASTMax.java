/* Generated By:JJTree: Do not edit this line. ASTMax.java */

package anl.verdi.parser;

import ucar.ma2.Array;
import ucar.ma2.Index;
import anl.verdi.formula.IllegalFormulaException;
import anl.verdi.util.FormulaArray;

public class ASTMax extends SimpleNode {
  public ASTMax(int id) {
    super(id);
  }

  public ASTMax(Parser p, int id) {
    super(p, id);
  }

	/**
	 * Evaluates this Node.
	 * 
	 * NOTE: Changed the calculation from domain-based max to maximum value over time steps (within a layer)
	 * for each individual cell in the domain. Q.H. 12/06/2010
	 *
	 * @param frame
	 * @return the result of the evaluation.
	 */
	@Override
	public FormulaArray evaluate(Frame frame) throws IllegalFormulaException {
		Array array = jjtGetChild(0).evaluate(frame).getArray();
		Array maxA = calcMax(array);

		return new FormulaArray(maxA, false);
	}
	
	private Array calcMax(Array array) {
		Array maxA = array.copy();
		int rank = array.getRank();

		int[] shape = array.getShape();
		
		if (rank != 4) return array;
		
		if (rank == 4 && shape[0] == 1) return array; // NOTE: only one time step
		
		if (rank == 4) {
			Index index = array.getIndex();

			for (int i = 0; i < shape[2]; i++) {
				for (int j = 0; j < shape[3]; j++) {
					for (int k = 0; k < shape[1]; k++) {
						double max = Double.NEGATIVE_INFINITY;
						
						for (int t = 0; t < shape[0]; t++) {
							index.set(t, k, i, j);
							double val = array.getDouble(index);
							if (val > max) max = val;
						}
						
						for (int t = 0; t < shape[0]; t++) {
							index.set(t, k, i, j);
							maxA.setDouble(index, max);
						}
					}
				}
			}
		}
		
		return maxA;
	}
}
