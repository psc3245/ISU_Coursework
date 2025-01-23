package library;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Interface representing operations on items in a library.  
 * Items are intended to be ordered lexicographically by title.
 */
public class Item implements Comparable<Item>
{

	/**
	 * Title of this item.
	 */
	private String title;

	/**
	 * Due date for this item.  This value is null when not checked out.
	 */
	private Date dueDate;

	/**
	 * Patron to whom this item is checked out.  This value is null when not checked out.
	 */
	private Patron checkedOutTo;

	/**
	 * Number of times the item has been renewed for the current patron.
	 */
	private int renewalCount;


	/**
	 * Checks out this item to the given patron, if possible.
	 * Does nothing if the item is already checked out.
	 * @param p
	 *   Patron to whom to check out the item
	 * @param now
	 *   current date
	 */
	public void checkOut(Patron p, Date now) {
		if (!isCheckedOut())
		{
			int checkOutDays = 21;

			// use a GregorianCalendar to figure out the Date corresponding to
			// midnight, 21 days from now
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(now);
			cal.add(Calendar.DAY_OF_YEAR, checkOutDays);

			// always set to 11:59:59 pm on the day it's due
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);     

			// convert back to Date object
			dueDate = cal.getTime();

			checkedOutTo = p;      
		}
	}

	/**
	 * Checks in this item.  Does nothing if the item is
	 * not checked out.
	 */
	public void checkIn() {
		if (isCheckedOut())
		{
			checkedOutTo = null;
			dueDate = null;
			renewalCount = 0;
		}
	}

	/**
	 * Renews this item, if possible.  Does nothing if the item is not checked out.
	 * Does nothing if the item is currently overdue.
	 * @param now
	 *   current date
	 */
	public void renew(Date now) {
		if (isCheckedOut() && !isOverdue(now) && renewalCount < 2)
		{
			checkOut(checkedOutTo, dueDate);
			renewalCount += 1;
		}    
	}

	/**
	 * Returns the patron to whom this item is checked out, or null
	 * if the item is not checked out.
	 * @return
	 *   patron to whom this item is checked out, or null
	 */
	public Patron getPatron() {
		return checkedOutTo;
	}

	/**
	 * Returns the fine owed if this item is overdue, or zero
	 * if the item is not currently overdue.
	 * @param now
	 *   current date
	 * @return
	 *   the fine currently owed
	 */
	public double getFine(Date now) {
		if (isCheckedOut() && isOverdue(now))
		{
			// how late is it, in milliseconds
			double elapsed = now.getTime() - dueDate.getTime();

			// how late is it, in days
			int millisPerDay = 24 * 60 * 60 * 1000;
			int daysLate = (int) Math.ceil(elapsed / millisPerDay);

			// compute the fine, 25 cents per day
			return daysLate * .25;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Determines whether this item is currently checked out.
	 * @return
	 *   true if this item is checked out, false otherwise
	 */
	public boolean isCheckedOut() {
		return dueDate != null;
	}

	/**
	 * Determines whether this item is currently overdue.
	 * @param now
	 *   current date
	 * @return
	 *   true if this item is overdue, false otherwise
	 */
	public boolean isOverdue(Date now) {
		if (!isCheckedOut())
		{
			return false;
		}
		return now.after(dueDate);
	}

	/**
	 * Returns the due date for this item, or null if not currently checked out.
	 * @return
	 *   due date for this item, or null
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * Returns the title of this item.
	 * @return
	 *   title of this item
	 */
	public String getTitle() {
		return title;
	}

	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		return 0;
	}








}