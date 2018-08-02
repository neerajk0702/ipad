package com.apitechnosoft.ipad;

/**
 * @author AST Inc.
 */
public class FNSortOrdering extends FNObject {

	public static final Object EOCompareAscending = "EOCompareAscending";
	public static final Object EOCompareDescending = "EOCompareDescending";
	public static final Object EOCompareCaseInsensitiveAscending = "EOCompareCaseInsensitiveAscending";
	public static final Object EOCompareCaseInsensitiveDescending = "EOCompareCaseInsensitiveDescending";
	private static final long serialVersionUID = 1L;
	protected String key ;
	protected Object selector; /* maybe we want to use other objects */

	/**
	 * <p>
	 * This method is used to set key on which sorting is done and selector
	 * means operation on array e.g. ascending or descending. etc.
	 * </p>
	 * <p>
	 * <p>
	 * <p>
	 * 
	 * <pre>
	 * suppose a class person which has a variable firstName.
	 * and you want to sort arraylist containing person object
	 * on the basis of firstName in ascending order
	 * then you will pass the key as firstName and
	 * FNSortOrdering.EOCompareAscending as selector or _sel
	 * FNSortOrdering("firstName", FNSortOrdering.EOCompareAscending);
	 * </pre>
	 *
	 * @param _key
	 *            on the basis of which sorting is to be done.
	 * @param _sel
	 *            selector ascending,descending.
	 * @see {@link FNSortOrdering#selector()}
	 * @since Foundation 1.0
	 */
	public FNSortOrdering(final String _key, final Object _sel) {
		this.key = _key;
		this.selector = _sel;
	}

	/**
	 * Convenience function to create an array of sort orderings.
	 * <p>
	 * Example:<br>
	 *
	 * <pre>
	 * fs.setSortOrderings(FNSortOrdering.create(&quot;name&quot;, &quot;ASC&quot;));
	 * </pre>
	 *
	 * @param _key
	 *            - the key to sort on
	 * @param _op
	 *            - the operation
	 * @return an array of FNSortOrdering's suitable for use in a fetchspec
	 */

	/**
	 * Returns the KVC key to sort on, eg 'lastModified' or 'firstname'.
	 */
	public String key() {
		return this.key;
	}

	public boolean isKeyBased() {
		return this.isNonEmptyStr(this.key);
	}

	/**
	 * Returns the selector to use for the sort. There are four predefined
	 * selectors:
	 * <ul>
	 * <li>EOCompareAscending
	 * <li>EOCompareDescending
	 * <li>EOCompareCaseInsensitiveAscending
	 * <li>EOCompareCaseInsensitiveDescending
	 * </ul>
	 *
	 * @return an Object representing the selector which is used for a sort
	 */
	public Object selector() {
		return this.selector;
	}

	/* description */

	/**
	 * Returns an SQL-like string representation of the sort ordering.
	 * <p>
	 * Example:<br>
	 * <p>
	 * <p>
	 * <p>
	 * 
	 * <pre>
	 * name ASC
	 * </pre>
	 */
	public String stringRepresentation() {
		String sr = this.key;

		if (this.selector.equals(FNSortOrdering.EOCompareCaseInsensitiveAscending)) {
			sr += " IASC";
		} else if (this.selector.equals(FNSortOrdering.EOCompareCaseInsensitiveDescending)) {
			sr += " IDESC";
		} else if (this.selector.equals(FNSortOrdering.EOCompareAscending)) {
			sr += " ASC";
		} else if (this.selector.equals(FNSortOrdering.EOCompareDescending)) {
			sr += " DESC";
		} else {
			sr += this.selector;
		}

		return sr;
	}

}
