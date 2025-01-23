package library;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A Book is a library item that can be checked out for 21 days and renewed at most twice.
 * If overdue, the fine is .25 per day.
 */
public class Book extends Item
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
	 * Constructs a book with the given title.
	 * @param givenTitle
	 */
	public Book(String givenTitle)
	{
		title = givenTitle;
		dueDate = null;
		checkedOutTo = null;
		renewalCount = 0;
	}


	public void checkOut(Patron p, Date now)
	{
		super.checkOut(p, now);
	}

	public void checkIn()
	{
		super.checkIn();
	}

	public void renew(Date now)
	{
		super.renew(now);
	}

	@Override
	public double getFine(Date now)
	{
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


	public boolean isOverdue(Date now)
	{
		return super.isOverdue(now);
	}

	@Override
	public int compareTo(Item other)
	{
		return title.compareTo(other.getTitle());
	}


	public String getTitle()
	{
		return super.getTitle();
	}


	public boolean isCheckedOut()
	{
		return super.isCheckedOut();
	}


	public Date getDueDate()
	{
		return super.getDueDate();
	}


	public Patron getPatron()
	{
		return super.getPatron();
	}

}
