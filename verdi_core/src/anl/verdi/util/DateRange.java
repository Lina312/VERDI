package anl.verdi.util;

import java.util.GregorianCalendar;

/**
 * @author Nick Collier
* @version $Revision$ $Date$
*/
public class DateRange {

	private long start, end;

	public DateRange(GregorianCalendar gregorianCalendar, GregorianCalendar gregorianCalendar2) {
System.out.println("in constructor with 2 GregorianCalendar objects in DateRange");
		this.end = gregorianCalendar2.getTimeInMillis();
		this.start = gregorianCalendar.getTimeInMillis();
	}

	public DateRange(long start, long end) {
		this.start = start;
		this.end = end;
System.out.println("in alternate constructor for DateRange");
	}


	public long getEnd() {
		return end;
	}

	public long getStart() {
		return start;
	}

	public DateRange overlap(DateRange other) {
		if (start >= other.start && end <= other.end) {
			// this is within other
			return new DateRange(start, end);
		}

		if (other.start >= this.start && other.end <= this.end) {
			// other is within this
			return new DateRange(other.start,  other.end);
		}

		if (start >= other.start && start <= other.end && end > other.end) {
			// overlaps between this start and other end
			return new DateRange(start, other.end);
		}

		if (start <= other.start && end >= other.start && end < other.end) {
			// overlaps between other.start and this.end
			return new DateRange(other.start, this.end);
		}

		return null;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof DateRange)) return false;
		DateRange other = (DateRange) obj;
		return other.start == this.start && other.end == this.end;
	}
}
