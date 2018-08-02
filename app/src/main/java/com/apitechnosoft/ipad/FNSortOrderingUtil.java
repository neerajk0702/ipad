package com.apitechnosoft.ipad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author AST Inc.
 */
public class FNSortOrderingUtil {

	private FNSortOrderingUtil() {
	}/* do not allow construction */

	/**
	 * <p>
	 * this method is used to create array of FNSortOrderings to sort the
	 * objects in asc desc etc order.
	 * </p>
	 *
	 * @param _key
	 *            on which sorting is to be done
	 * @param _op
	 *            operation by which sorting is to be done.
	 * @since Foundation 1.0
	 */
	public static FNSortOrdering[] create(final String _key, final String _op) {
		String sel;
		if (_op == null) {
			return parse("+" + _key);
		}
		switch (_op) {
			case "DESC":
				sel = "-";
				break;
			case "IASC":
				sel = "%";
				break;
			case "IDESC":
				sel = "#";
				break;
			case "ASC":
			default:
				sel = "+";
				break;
		}
		return parse(sel + _key);
	}

	/**
	 * Parse orderings from a simple string syntax, eg:
	 * <code>name,-balance</code>
	 *
	 * @param _text
	 *            - the text to parse
	 * @return an array of sort orderings
	 */
	public static FNSortOrdering[] parse(final String _text) {
		return (FNSortOrdering[]) parseKey(_text);
	}

	/**
	 * Sorts a list based on the given sort orderings. This performs an "inline"
	 * sort, that is, it modifies the List which is passed in.
	 *
	 * @param _list
	 *            - the List of KVC objects to be sorted
	 * @param _sos
	 *            - an array of FNSortOrdering's which specify the sort
	 */
	@SuppressWarnings("unchecked")
	public static void sort(final List<?> _list, final FNSortOrdering[] _sos) {
		if (_list == null || _sos == null || _sos.length == 0) {
			return;
		}
		Collections.sort(_list, new FNSortOrderingComparator(_sos));
	}

	/**
	 * Sorts a Collection based on the given sort orderings. This first creates
	 * a list using the given object and then performs the inline
	 * FNSortOrdering.sort().
	 * <p>
	 * This method always returns a fresh List and never reuses the Collection
	 * which is passed in.
	 *
	 * @param _col
	 *            - the Collection of KVC objects to be sorted
	 * @param _sos
	 *            - an array of FNSortOrdering's which specify the sort
	 * @return a List which contains the objects sorted by the given criteria
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List sortedList(final Collection<?> _col, FNSortOrdering[] _sos) {
		if (_col == null) {
			return null;
		}
		if (_sos == null || _sos.length == 0) {
			return new ArrayList(_col); /* always return a copy! */
		}

		ArrayList<Object> result = new ArrayList<>(_col.size());
		result.addAll(_col);
		sort(result, _sos);
		return result;
	}

	/**
	 * Scans an array of sort-orderings for the first sort ordering which sorts
	 * on the specified key.
	 */
	public static FNSortOrdering firstSortOrderingWithKey(final FNSortOrdering[] _sos, final String _key) {
		if (_sos == null || _key == null) {
			return null;
		}

		for (int i = 0; i < _sos.length; i++) {
			if (_key.equals(_sos[i].key())) {
				return _sos[i];
			}
		}
		return null; /* not found */
	}

	/* searching */

	/**
	 * Scans an array of sort-orderings for the first sort ordering which sorts
	 * on the specified key and then returns the selector of this sort ordering.
	 */
	public static Object firstSelectorForKey(FNSortOrdering[] _sos, String _key) {
		if (_sos == null || _key == null) {
			return null;
		}

		for (FNSortOrdering _so : _sos) {
			if (_key.equals(_so.key())) {
				return _so.selector();
			}
		}
		return null; /* not found */
	}

	public static Object parseKey(String _text) {
		if (_text == null) {
			return null;
		}
		if (_text.length() == 0) {
			return new FNSortOrdering[0];
		}

		final List<FNSortOrdering> orderings = new ArrayList<>(4);
		for (String arg : _text.split(",")) {
			arg = arg.trim();
			if (arg.length() == 0) {
				continue;
			}
			Object sel;
			String key;
			switch (arg.charAt(0)) {
				case '-':
					sel = FNSortOrdering.EOCompareDescending;
					key = arg.substring(1);
					break;
				case '+':
					sel = FNSortOrdering.EOCompareCaseInsensitiveAscending;
					key = arg.substring(1);
					break;
				case '#':
					sel = FNSortOrdering.EOCompareAscending;
					key = arg.substring(1);
					break;
				case '%':
					sel = FNSortOrdering.EOCompareCaseInsensitiveDescending;
					key = arg.substring(1);
					break;
				default:
					sel = FNSortOrdering.EOCompareCaseInsensitiveAscending;
					key = arg;
			}

			orderings.add(new FNSortOrdering(key, sel));
		}
		return orderings.toArray(new FNSortOrdering[orderings.size()]);
	}

	/**
	 * Sorts a Collection based on the single sort ordering. This first creates
	 * a list using the given object and then performs the inline
	 * FNSortOrdering.sort().
	 * <p>
	 * This method always returns a fresh List and never reuses the Collection
	 * which is passed in.
	 *
	 * @param _col
	 *            - the Collection of KVC objects to be sorted
	 * @return a List which contains the objects sorted by the given criteria
	 */
	@SuppressWarnings("rawtypes")
	public List sortedList(final Collection<?> _col) {
		if (_col == null) {
			return null;
		}
		return sortedList(_col, new FNSortOrdering[] {});
	}

}

@SuppressWarnings("rawtypes")
class FNSortOrderingComparator extends FNObject implements Comparator {
	private static final long serialVersionUID = 1L;
	protected FNSortOrdering[] sortOrderings;

	public FNSortOrderingComparator(FNSortOrdering[] _orderings) {
		this.sortOrderings = _orderings.clone();
	}

	/**
	 * <p>
	 * To compare two objects.
	 * </p>
	 *
	 * @param _obj1
	 *            object1.
	 * @param _obj2
	 *            object2.
	 * @return int
	 * @since Foundation 1.0
	 */
	@SuppressWarnings("unchecked")
	public int compare(Object _obj1, Object _obj2) {
		//  : this needs a unit test
		for (FNSortOrdering sortOrdering : this.sortOrderings) {
			String key = sortOrdering.key();
			Object sel = sortOrdering.selector();
			int result;

			Object v1 = ((FNObject) _obj1).valueForKeyPath(key);
			Object v2 = ((FNObject) _obj2).valueForKeyPath(key);

			boolean isAsc = (sel.equals(FNSortOrdering.EOCompareAscending) || sel.equals(FNSortOrdering.EOCompareCaseInsensitiveAscending));

			if (v1 == v2) {
				result = 0 /* same */;
			} else if (v1 == null) {
				result = isAsc ? -1 : 1;
			} else if (v2 == null) {
				result = isAsc ? 1 : -1;
			} else {
				if (sel.equals(FNSortOrdering.EOCompareCaseInsensitiveAscending) || sel.equals(FNSortOrdering.EOCompareCaseInsensitiveDescending)) {
					if (v1 instanceof String) {
						v1 = ((String) v1).toLowerCase();
					}
					if (v2 instanceof String) {
						v2 = ((String) v2).toLowerCase();
					}
				}

				boolean isV1Num = v1 instanceof Number;
				boolean isV2Str = v2 instanceof String;

				/* special hacks to improve SOPE compatibility */
				if (isV1Num && isV2Str) {
					/*
					 * This is useful in combination with property lists where
					 * numbers are sometimes parsed as numbers or strings
					 * depending on the syntax. But not so in SOPE.
					 */
					v1 = v1.toString();
				} else if (v1 instanceof String && (!isV2Str && v2 instanceof Number)) {
					v2 = v2.toString();
				}

				/* do the compare */
				Comparable c1 = (Comparable) v1;
				result = c1.compareTo(v2);

				if (!isAsc) {
					result = -result;
				}
			}

			if (result != 0 /* same */) {
				return result;
			}
		}
		return 0 /* same */;
	}
}
