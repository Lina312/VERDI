/* Generated By:JJTree: Do not edit this line. ASTNLevels.java */

package anl.verdi.parser;

import anl.verdi.formula.IllegalFormulaException;
import anl.verdi.util.FormulaArray;

public class ASTNLevels extends SimpleNode {

	public FormulaArray levels;

  public ASTNLevels(int id) {
    super(id);
  }

  public ASTNLevels(Parser p, int id) {
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
		return levels;
	}

	/**
	 * Performs any node level preprocessing prior
	 * to any evaluation. This should only be called
	 * once on the tree. 
	 *
	 * @param frame
	 */
	@Override
	public void preprocess(Frame frame) throws IllegalFormulaException {
		 levels = new FormulaArray(frame.getLevelCount());
	}

}
