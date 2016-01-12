package com.haskins.cloudtrailviewer.thirdparty;

import org.jfree.data.Range;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * A {@link XYDataset} implementation that presents a window(subset) of the
 * items in an underlying dataset.  The index of the first "visible"
 * item can be modified, which provides a means of "sliding" through
 * the items in the underlying series dataset.
 */

public class SlidingXYDataset extends AbstractXYDataset
implements XYDataset, DatasetChangeListener {

  /** The underlying dataset. */
  private XYDataset underlying;

  /** The index of the first item. */
  private int firstItemIndex;

  /** The maximum number of items to present in the window. */
  private int windowCount;

  /** Storage for the series offset from the items at 0.
   *  This list must be kept in sync with the seriesList. */
  private int[] seriesOffset;


  /**
   * Creates a new <code>SlidingXYDataset</code> class that
   * applies a dynamically updateable sliding through the underlying
   * dataset.
   * 
   * @param underlying  the underlying dataset (<code>null</code> not
   * permitted).
   * @param firstItemIndex the first item index
   * @param windowCount the window count
   */
  public SlidingXYDataset(XYDataset underlying, int firstItemIndex, 
      int windowCount) {
    this.underlying = underlying;
    this.underlying.addChangeListener(this);
    this.windowCount = windowCount;
    this.firstItemIndex = firstItemIndex;
    this.seriesOffset = new int[getSeriesCount()];
    for (int i = 0; i <  seriesOffset.length; i++) {
      seriesOffset[i] = getOffSet(i);
    }
  }

  /**
   * Gets the off set of the series from the left most item
   * in the dataset. This assumes the domain(X) values to
   * be in equal intervals.
   * 
   * @param series the series index
   * 
   * @return the off set
   */
  private int getOffSet(int series) {
    double firstX = 
      DatasetUtilities.findMinimumDomainValue(this.underlying).doubleValue();
    double seriesFirstX = getFirstXValue(series);
    double diff = getInterval();
    int offset = (int) ((seriesFirstX - firstX) / diff);
    return offset;
  }

  /**
   * Gets the interval.
   * It is the difference between to consecutive
   * x values.
   * 
   * This assumes the domain(X) values to
   * be in equal intervals.
   * 
   * 
   * @return the interval
   */
  private double getInterval() {
    double second = this.underlying.getXValue(0, 1);
    double first = getFirstXValue(0);
    return second - first;
  }

  /**
   * Gets the length of the underlying dataset.
   * 
   * @return the length
   */
  private int getLength() {
    Range range = DatasetUtilities.findDomainBounds(this.underlying);
    double length = range.getLength();
    int items = (int) (length / getInterval());
    return items;
  }

  /**
   * Gets the first x value in the series.
   * 
   * @param i the i
   * 
   * @return the first x value
   */
  private double getFirstXValue(int i) {
    return this.underlying.getXValue(i, 0);
  }

  /**
   * Returns the underlying dataset that was supplied to the constructor.
   * 
   * @return The underlying dataset (never <code>null</code>).
   */
  public XYDataset getUnderlyingDataset() {
    return this.underlying;
  }

  /**
   * Returns the index of the first visible item.
   * 
   * @return The index.
   * 
   * @see #setFirstItemIndex(int)
   */
  public int getFirstItemIndex() {
    return this.firstItemIndex;
  }


  /**
   * Sets the index of the first item that should be used from the
   * underlying dataset, and sends a {@link DatasetChangeEvent} to all
   * registered listeners.
   * 
   * @param first  the index.
   * 
   * @see #getFirstItemIndex()
   */
  public void setFirstItemIndex(int first) {
    int lastIndex = first + windowCount - 1;
    int length = getLength();
    if (first < 0 
        || lastIndex >= length) {
      throw new IllegalArgumentException("Invalid index." + first);
    }
    this.firstItemIndex = first;
    fireDatasetChanged();
  }

  /**
   * Returns the number of items in the specified series to be displayed in
   * the window.
   * 
   * @param series  the series index (in the range <code>0</code> to
   * <code>getSeriesCount() - 1</code>).
   * 
   * @return The item count.
   * 
   * @throws IllegalArgumentException if <code>series</code> is not in the
   * specified range.
   */
  public int getItemCount(int series) {

    if ((series < 0) || (series >= getSeriesCount())) {
      throw new IllegalArgumentException("Series index out of bounds");
    }
    int itemCount = 0;

    int seriesItemCount = this.underlying.getItemCount(series);
    int offset = seriesOffset[series];
    int seriesEndIndex = offset + seriesItemCount;

    int windowEndIndex = firstItemIndex + windowCount;

    if (firstItemIndex >= seriesEndIndex) {
      return 0;
    }  
    if (firstItemIndex >= offset) {
      itemCount = windowCount;
    }    
    else {
      if (windowEndIndex > offset) {
        itemCount = windowEndIndex - offset;
      }
    }
    return itemCount;
  }

  /**
   * Returns the x-value for an item within a series using the
   * offsets relative positions in the underlying dataset.
   * 
   * @param series  the series index (in the range <code>0</code> to
   * <code>getSeriesCount() - 1</code>).
   * @param item  the item index (in the range <code>0</code> to
   * <code>getItemCount(series)</code>).
   * 
   * @return The x-value.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>series</code> is not
   * within the specified range.
   * 
   * @see #getX(int, int)
   */
  public double getXValue(int series, int item) {
    int newIndex = getItemIndex(series, item);
    double value = this.underlying.getXValue(series, newIndex);
    return value;
  }

  /**
   * Gets the corresponding item index in the underlying
   * dataset.
   * 
   * @param series the series
   * @param item the item in the window
   * 
   * @return the item index
   */
  private int getItemIndex(int series, int item) {
    int newIndex = item + firstItemIndex - seriesOffset[series];
    int seriesLastItemIndex = this.underlying.getItemCount(series) - 1;

    if (newIndex >= seriesLastItemIndex) {
      newIndex = seriesLastItemIndex;
    }
    else {
      int index = item + firstItemIndex;
      int offset = seriesOffset[series];
      if (firstItemIndex >= offset) {
        newIndex = index - offset;
      } 
      else {
        newIndex = item;
      }
    }
    return newIndex;
  }

  /**
   * Returns the x-value for an item within a series using the
   * offsets relative positions in the underlying dataset.
   * 
   * @param series  the series index (in the range <code>0</code> to
   * <code>getSeriesCount() - 1</code>).
   * @param item  the item index (in the range <code>0</code> to
   * <code>getItemCount(series)</code>).
   * 
   * @return The x-value.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>item</code> is not
   * within the specified range.
   * 
   * @see #getXValue(int, int)
   */
  public Number getX(int series, int item) {
    return new Double(getXValue(series, item));
  }

  /**
   * Returns the y-value for an item within a series using the
   * offsets relative positions in the underlying dataset.
   * 
   * @param series  the series index (in the range <code>0</code> to
   * <code>getSeriesCount() - 1</code>).
   * @param item  the item index (in the range <code>0</code> to
   * <code>getItemCount(series)</code>).
   * 
   * @return The y-value.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>series</code> is not
   * within the specified range.
   * 
   * @see #getY(int, int)
   */
  public double getYValue(int series, int item) {
    int newIndex = getItemIndex(series, item);
    return this.underlying.getYValue(series, newIndex);
  }

  /**
   * Returns the y-value for an item within a series using the
   * offsets relative positions in the underlying dataset.
   * 
   * @param series  the series index (in the range <code>0</code> to
   * <code>getSeriesCount() - 1</code>).
   * @param item  the item index (in the range <code>0</code> to
   * <code>getItemCount(series)</code>).
   * 
   * @return The y-value.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>series</code> is not
   * within the specified range.
   * 
   * @see #getY(int, int)
   */
  public Number getY(int series, int item) {
    return new Double(getYValue(series, item));
  }

  /**
   * Returns the number of series in the dataset.
   * 
   * @return The series count.
   */
  public int getSeriesCount() {
    return this.underlying.getSeriesCount();
  }

  /**
   * Returns the key for a series.
   * 
   * @param series  the series index (in the range <code>0</code> to
   * <code>getSeriesCount() - 1</code>).
   * 
   * @return The key for the series.
   * 
   * @throws IllegalArgumentException if <code>series</code> is not in the
   * specified range.
   */
  public Comparable getSeriesKey(int series) {
    if ((series < 0) || (series >= getSeriesCount())) {
      throw new IllegalArgumentException("Series index out of bounds");
    }
    return underlying.getSeriesKey(series);
  }    

  /**
   * Receives notification of an dataset change event.
   * 
   * @param event  information about the event.
   */
  public void datasetChanged(DatasetChangeEvent event) {
    this.fireDatasetChanged();
  }

  /**
   * Tests this <code>SlidingXYDataset</code> instance for equality with an
   * arbitrary object.  This method returns <code>true</code> if and only if:
   * <ul>
   * <li><code>obj</code> is not <code>null</code>;</li>
   * <li><code>obj</code> is an instance of
   * <code>SlidingXYDataset</code>;</li>
   * <li>both datasets have the same number of series, each containing
   * exactly the same values, window and firstItemIndex.</li>
   * </ul>
   * 
   * @param obj  the object (<code>null</code> permitted).
   * 
   * @return A boolean.
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SlidingXYDataset)) {
      return false;
    }
    SlidingXYDataset that = (SlidingXYDataset) obj;
    if (this.firstItemIndex != that.firstItemIndex) {
      return false;
    }
    if (this.windowCount != that.windowCount) {
      return false;
    }
    if (!this.underlying.equals(that.underlying)) {
      return false;
    }
    return true;
  }

  /**
   * Returns a hash code for this instance.
   * 
   * @return A hash code.
   */
  public int hashCode() {
    int result;
    result = this.underlying.hashCode();
    result = 29 * result + this.firstItemIndex;
    result = 17 * result + this.windowCount;
    return result;
  }
}

