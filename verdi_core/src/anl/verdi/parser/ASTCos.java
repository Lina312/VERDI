/* Generated By:JJTree: Do not edit this line. ASTCos.java */

package anl.verdi.parser;

import anl.verdi.formula.IllegalFormulaException;
import anl.verdi.util.DoubleFunction;
import anl.verdi.util.FormulaArray;

public class ASTCos extends SimpleNode {

	static class Cos implements DoubleFunction {
		public double apply(double val) {
			return Math.cos(val);
		}
	}

  public ASTCos(int id) {
    super(id);
  }

  public ASTCos(Parser p, int id) {
    super(p, id);
  }

	/**
	 * Evaluates this Node.
	 *
	 * @param frame
	 * @return the result of the evaluation.
	 */
	@Override
	public FormulaArray evaluate(Frame frame) throws IllegalFormulaException {
		return jjtGetChild(0).evaluate(frame).foreach(new Cos());
	}
}
