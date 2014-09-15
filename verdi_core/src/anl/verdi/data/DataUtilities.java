package anl.verdi.data;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import anl.verdi.core.VerdiConstants;
import anl.verdi.util.ArrayFactory;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DataUtilities {

	public static final int NO_LAYER = -1;
	public static double BADVAL3 = VerdiConstants.BADVAL3; 
	public static double AMISS3 = VerdiConstants.AMISS3; 


	public static class MinMax {

		private double min, max;

		public MinMax(double min, double max) {
			this.max = max;
			this.min = min;
		}


		public double getMax() {
			return max;
		}

		public double getMin() {
			return min;
		}
	}

	public static class MinMaxPoint {

		private Set<Point> minPoints = new HashSet<Point>();
		private Set<Point> maxPoints = new HashSet<Point>();

		private MinMax minMax = new MinMax(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

		void testPoint(int x, int y, double val) {
			if (val <= BADVAL3 || val <= AMISS3)	// 2014 changed from == AMISS3
				return;
			
			if (val < minMax.min) {
				minMax.min = val;
				minPoints.clear();
				minPoints.add(new Point(x, y));
			} else if (val == minMax.min) {
				minPoints.add(new Point(x, y));
			}

			if (val > minMax.max) {
				minMax.max = val;
				maxPoints.clear();
				maxPoints.add(new Point(x, y));
			} else if (val == minMax.max) {
				maxPoints.add(new Point(x, y));
			}
		}

		public Set<Point> getMinPoints() {
			return minPoints;
		}

		public Set<Point> getMaxPoints() {
			return maxPoints;
		}

		public double getMin() {
			return minMax.min;
		}

		public double getMax() {
			return minMax.max;
		}
	}

	/**
	 * Gets the minimum value contained by the specified DataFrame.
	 *
	 * @param frame the DataFrame to get the value from
	 * @return the minimum value contained by the specified  DataFrame.
	 */
	public static MinMax minMax(DataFrame frame) {
		return minMax(frame.getArray());
	}

	private static MinMax minMax(Array array) {
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;

		for (IndexIterator iter = array.getIndexIterator(); iter.hasNext();) {
			double val = iter.getDoubleNext();
			
			if (val <= BADVAL3 || val <= AMISS3)	// 2014 changed from == AMISS3
				continue;
			
			min = Math.min(min, val);
			max = Math.max(max, val);
		}

		return new MinMax(min, max);
	}

	/**
	 * Gets the min max of the frame data for the specified time
	 * step and x value over all the layers and y range.
	 *
	 * @param frame    the data
	 * @param timeStep the timestep
	 * @param x        the x value
	 * @return the min max of the frame data for the specified time
	 *         step and x value over all the layers and y range.
	 * @throws InvalidRangeException if the timestep or x value is out of range.
	 */
	public static MinMax minMaxTX(DataFrame frame, int timeStep, int x) throws InvalidRangeException {
		if (frame.getShape().length != 4) throw new InvalidRangeException("Frame rank does not equal 4");
		Axes<DataFrameAxis> axes = frame.getAxes();
		ucar.ma2.Range[] ranges = new ucar.ma2.Range[4];
		ranges[axes.getTimeAxis().getArrayIndex()] = new ucar.ma2.Range(timeStep, timeStep);
		DataFrameAxis layerAxis = axes.getZAxis();
		ranges[layerAxis.getArrayIndex()] = new ucar.ma2.Range(0, layerAxis.getExtent() - 1);
		DataFrameAxis xAxis = axes.getXAxis();
		ranges[xAxis.getArrayIndex()] = new ucar.ma2.Range(x, x);
		DataFrameAxis yAxis = axes.getYAxis();
		ranges[yAxis.getArrayIndex()] = new ucar.ma2.Range(0, yAxis.getExtent() - 1);
		Array array = frame.getArray().section(Arrays.asList(ranges));
		return minMax(array);
	}

	/**
	 * Gets the min max of the frame data for the specified time
	 * step and y value over all the layers and x range.
	 *
	 * @param frame    the data
	 * @param timeStep the timestep
	 * @param y        the y value
	 * @return the min max of the frame data for the specified time
	 *         step and x value over all the layers and y range.
	 * @throws InvalidRangeException if the timestep or x value is out of range.
	 */
	public static MinMax minMaxTY(DataFrame frame, int timeStep, int y) throws InvalidRangeException {
		if (frame.getShape().length != 4) throw new InvalidRangeException("Frame rank does not equal 4");
		Axes<DataFrameAxis> axes = frame.getAxes();
		ucar.ma2.Range[] ranges = new ucar.ma2.Range[4];
		ranges[axes.getTimeAxis().getArrayIndex()] = new ucar.ma2.Range(timeStep, timeStep);
		DataFrameAxis layerAxis = axes.getZAxis();
		ranges[layerAxis.getArrayIndex()] = new ucar.ma2.Range(0, layerAxis.getExtent() - 1);
		DataFrameAxis xAxis = axes.getXAxis();
		ranges[xAxis.getArrayIndex()] = new ucar.ma2.Range(0, xAxis.getExtent() - 1);
		DataFrameAxis yAxis = axes.getYAxis();
		ranges[yAxis.getArrayIndex()] = new ucar.ma2.Range(y, y);
		Array array = frame.getArray().section(Arrays.asList(ranges));
		return minMax(array);
	}

	public static MinMax minMax(DataFrame frame, int timeStep, int layer) throws InvalidRangeException {
		if (frame.getShape().length != 4) throw new InvalidRangeException("Frame rank does not equal 4");
		Axes<DataFrameAxis> axes = frame.getAxes();
		ucar.ma2.Range[] ranges = new ucar.ma2.Range[4];
		ranges[axes.getTimeAxis().getArrayIndex()] = new ucar.ma2.Range(timeStep, timeStep);
		ranges[axes.getZAxis().getArrayIndex()] = new ucar.ma2.Range(layer, layer);
		DataFrameAxis xAxis = axes.getXAxis();
		ranges[xAxis.getArrayIndex()] = new ucar.ma2.Range(0, xAxis.getExtent() - 1);
		DataFrameAxis yAxis = axes.getYAxis();
		ranges[yAxis.getArrayIndex()] = new ucar.ma2.Range(0, yAxis.getExtent() - 1);
		Array array = frame.getArray().section(Arrays.asList(ranges));
		return minMax(array);
	}
	
	public static MinMaxPoint minMaxTYPoint(DataFrame frame, int timestep, int y) throws InvalidRangeException {
		if (frame.getShape().length != 4) throw new InvalidRangeException("Frame rank does not equal 4");
		Axes<DataFrameAxis> axes = frame.getAxes();
		DataFrameIndex index = frame.getIndex();
		index.set(timestep, 0, 0, y);
		MinMaxPoint point = new MinMaxPoint();
		for (int z = 0, zLimit = axes.getZAxis().getExtent(); z < zLimit; z++) {
			index.setLayer(z);
			for (int x = 0, xLimit = axes.getXAxis().getExtent(); x < xLimit; x++) {
				index.setXY(x, y);
				point.testPoint(x, y, frame.getDouble(index));
			}
		}
		return point;
	}

	// assumes X, Y exist but not necessarily the layer
	public static MinMaxPoint minMaxPoint(DataFrame frame, int timestep) throws InvalidRangeException {
		Axes<DataFrameAxis> axes = frame.getAxes();
		if (axes.getTimeAxis() == null) throw new InvalidRangeException("Time axis is missing");
		DataFrameIndex index = frame.getIndex();
		index.setTime(timestep);
		MinMaxPoint point = new MinMaxPoint();
		if (axes.getZAxis() == null) {
			for (int y = 0, yLimit = axes.getYAxis().getExtent(); y < yLimit; y++) {
				for (int x = 0, xLimit = axes.getXAxis().getExtent(); x < xLimit; x++) {
					index.setXY(x, y);
					point.testPoint(x, y, frame.getDouble(index));
				}
			}
		} else {
			for (int z = 0, zLimit = axes.getZAxis().getExtent(); z < zLimit; z++) {
				index.setLayer(z);
				for (int y = 0, yLimit = axes.getYAxis().getExtent(); y < yLimit; y++) {
					for (int x = 0, xLimit = axes.getXAxis().getExtent(); x < xLimit; x++) {
						index.setXY(x, y);
						point.testPoint(x, y, frame.getDouble(index));
					}
				}
			}
		}
		return point;
	}

	public static MinMaxPoint minMaxTXPoint(DataFrame frame, int timestep, int x) throws InvalidRangeException {
		if (frame.getShape().length != 4) throw new InvalidRangeException("Frame rank does not equal 4");
		Axes<DataFrameAxis> axes = frame.getAxes();
		DataFrameIndex index = frame.getIndex();
		index.set(timestep, 0, x, 0);
		MinMaxPoint point = new MinMaxPoint();
		for (int z = 0, zLimit = axes.getZAxis().getExtent(); z < zLimit; z++) {
			index.setLayer(z);
			for (int y = 0, yLimit = axes.getYAxis().getExtent(); y < yLimit; y++) {
				index.setXY(x, y);
				point.testPoint(x, y, frame.getDouble(index));
			}
		}

		return point;
	}

	/**
	 * Gets the min max point at the specified time step and layer over all
	 * the x,y cells.
	 *
	 * @param frame    the frame containing the data
	 * @param timeStep the timestep
	 * @param layer    the layer
	 * @return the min max point at the specified time step and layer over all
	 *         the x,y cells.
	 * @throws InvalidRangeException if the frame's rank does not equal four.
	 */
	public static MinMaxPoint minMaxTLPoint(DataFrame frame, int timeStep, int layer) throws InvalidRangeException {
		if (frame.getShape().length != 4) throw new InvalidRangeException("Frame rank does not equal 4");
		Axes<DataFrameAxis> axes = frame.getAxes();
		DataFrameIndex index = frame.getIndex();
		index.set(timeStep, layer, 0, 0);
		MinMaxPoint point = new MinMaxPoint();
		for (int x = 0, xLimit = axes.getXAxis().getExtent(); x < xLimit; x++) {
			for (int y = 0, yLimit = axes.getYAxis().getExtent(); y < yLimit; y++) {
				index.setXY(x, y);
				point.testPoint(x, y, frame.getDouble(index));
			}
		}

		return point;
	}

	/**
	 * Gets the min max for the specified layer over the frame's entire
	 * time range.
	 *
	 * @param frame the frame
	 * @param layer the index of the layer whose min max is desired
	 * @return the min max for the specified layer over the frame's entire
	 *         time range.
	 * @throws InvalidRangeException if the range is invalid.
	 */
	public static MinMax minMaxForTimeLayer(DataFrame frame, int layer) throws InvalidRangeException {
		Axes<DataFrameAxis> axes = frame.getAxes();
		if (axes.getZAxis() == null) throw new InvalidRangeException("Z-Axis is missing");
		ucar.ma2.Range[] ranges = new ucar.ma2.Range[4];
		int rangeCount = 1;
		ranges[axes.getZAxis().getArrayIndex()] = new ucar.ma2.Range(layer, layer);
		DataFrameAxis timeAxis = axes.getTimeAxis();
		if (timeAxis != null) {
			rangeCount++;
			ranges[timeAxis.getArrayIndex()] = new ucar.ma2.Range(0, timeAxis.getExtent() - 1);
		}
		DataFrameAxis xAxis = axes.getXAxis();
		if (xAxis != null) {
			rangeCount++;
			ranges[xAxis.getArrayIndex()] = new ucar.ma2.Range(0, xAxis.getExtent() - 1);
		}
		DataFrameAxis yAxis = axes.getYAxis();
		if (yAxis != null) {
			rangeCount++;
			ranges[yAxis.getArrayIndex()] = new ucar.ma2.Range(0, yAxis.getExtent() - 1);
		}


		ucar.ma2.Range[] validRanges = new ucar.ma2.Range[rangeCount];
		System.arraycopy(ranges, 0, validRanges, 0, rangeCount);

		Array array = frame.getArray().section(Arrays.asList(validRanges));
		return minMax(array);
	}

	/**
	 * Gets the min max value from the DataFrame at the specified time step.
	 * The range for the other axes will be their existing range.
	 *
	 * @param frame    the frame whose min max we want
	 * @param timeStep the timestep for the min max
	 * @return the min max value from the DataFrame at the specified time step.
	 *         The range for the other axes will be their existing range.
	 * @throws InvalidRangeException if the range is invalid.
	 */
	public static MinMax minMax(DataFrame frame, int timeStep) throws InvalidRangeException {
		Axes<DataFrameAxis> axes = frame.getAxes();
		if (axes.getTimeAxis() == null) throw new InvalidRangeException("Time Axis is missing");

		ucar.ma2.Range[] ranges = new ucar.ma2.Range[4];
		int rangeCount = 1;
		ranges[axes.getTimeAxis().getArrayIndex()] = new ucar.ma2.Range(timeStep, timeStep);
		DataFrameAxis layerAxis = axes.getZAxis();
		if (layerAxis != null) {
			rangeCount++;
			ranges[layerAxis.getArrayIndex()] = new ucar.ma2.Range(0, layerAxis.getExtent() - 1);
		}
		DataFrameAxis xAxis = axes.getXAxis();
		if (xAxis != null) {
			rangeCount++;
			ranges[xAxis.getArrayIndex()] = new ucar.ma2.Range(0, xAxis.getExtent() - 1);
		}
		DataFrameAxis yAxis = axes.getYAxis();
		if (yAxis != null) {
			rangeCount++;
			ranges[yAxis.getArrayIndex()] = new ucar.ma2.Range(0, yAxis.getExtent() - 1);
		}


		ucar.ma2.Range[] validRanges = new ucar.ma2.Range[rangeCount];
		System.arraycopy(ranges, 0, validRanges, 0, rangeCount);

		Array array = frame.getArray().section(Arrays.asList(validRanges));
		return minMax(array);
	}

	public static DataFrame createDataFrame(DataFrame frame) {
		DataFrameBuilder builder = new DataFrameBuilder();
		builder.addDataset(frame.getDataset());
		builder.setVariable(frame.getVariable());
		builder.setArray(ArrayFactory.createDoubleArray(frame.getArray().getShape()));
		Axes<DataFrameAxis> axes = frame.getAxes();
		if (axes.getTimeAxis() != null)
			builder.addAxis(DataFrameAxis.createDataFrameAxis(axes.getTimeAxis(), axes.getTimeAxis().getArrayIndex()));
		if (axes.getZAxis() != null)
			builder.addAxis(DataFrameAxis.createDataFrameAxis(axes.getZAxis(), axes.getZAxis().getArrayIndex()));
		if (axes.getXAxis() != null)
			builder.addAxis(DataFrameAxis.createDataFrameAxis(axes.getXAxis(), axes.getXAxis().getArrayIndex()));
		if (axes.getYAxis() != null)
			builder.addAxis(DataFrameAxis.createDataFrameAxis(axes.getYAxis(), axes.getYAxis().getArrayIndex()));
		return builder.createDataFrame();
	}

	/**
	 * Normalizes the xFrame and yFrame data into normalized wind vectors 
	 * i.e., uwind / max_uwind
	 * 
	 * @param xFrame
	 * @param yFrame
	 * @return
	 */
	public static DataFrame[] unitVectorTransform(DataFrame xFrame, DataFrame yFrame) {
		DataFrame newX = createDataFrame(xFrame);
		DataFrame newY = createDataFrame(yFrame);
//		IndexIterator newXIter = newX.getArray().getIndexIteratorFast();	// deprecated in NetCDF library
//		IndexIterator newYIter = newY.getArray().getIndexIteratorFast();	// deprecated in NetCDF library
		IndexIterator newXIter = newX.getArray().getIndexIterator();
		IndexIterator newYIter = newY.getArray().getIndexIterator();

		Array xArray = xFrame.getArray();
		Array yArray = yFrame.getArray();
		IndexIterator xIterMax = xArray.getIndexIterator();
		IndexIterator yIterMax = yArray.getIndexIterator();
		double maxUVal = 0;
		double maxVVal = 0;
		while (xIterMax.hasNext()) {
			double xVal = xIterMax.getDoubleNext();
			double yVal = yIterMax.getDoubleNext();
			maxUVal = Math.max(maxUVal, Math.abs(xVal));
			maxVVal = Math.max(maxVVal, Math.abs(yVal));
		}
		IndexIterator xIter = xArray.getIndexIterator();
		IndexIterator yIter = yArray.getIndexIterator();
		while (xIter.hasNext()) {
			double xVal = xIter.getDoubleNext();
			double yVal = yIter.getDoubleNext();
//			double mag = Math.sqrt(xVal * xVal + yVal * yVal);
			newXIter.setDoubleNext(xVal / maxUVal * .5);
			newYIter.setDoubleNext(yVal / maxVVal * .5);
		}

		return new DataFrame[]{newX, newY};
	}
}
