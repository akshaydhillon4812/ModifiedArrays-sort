import java.util.*;
import java.io.*;
class DualPivot
{
	
	private DualPivot() {}
	private static final int MAX_RUN_COUNT = 67;
/**
  * The maximum length of run in merge sort.
  */
	private static final int MAX_RUN_LENGTH = 33;
/**
  * If the length of an array to be sorted is less than this
  * constant, Quicksort is used in preference to merge sort.
  */
	private static final int QUICKSORT_THRESHOLD = 286;
/**
  * If the length of an array to be sorted is less than this
  * constant, insertion sort is used in preference to Quicksort.
  */
	private static final int INSERTION_SORT_THRESHOLD = 47;
//We are limiting the treshold so as the algorithms uses at most 40mb of memory for creating buckets
	private static final int PSORT_TRESHOLD = 10000000;
  
	public static void HashSort(int[] a, int left, int right, boolean leftmost,int min,int max,int k)
	{
				

		//System.out.println("Hash sort xxxx "+min+" "+max+" "+k);

		int treshold=a.length<<3;
		int range=max-min;
		if(/*range>PSORT_TRESHOLD || */ treshold<range)
		{
			sort(a,left,right,true);
		}

		for(int i=k;i<a.length;i++)
		{
			if(max<a[i])
			{
				max=a[i];

			}
			else if(min>a[i])
			{
				min=a[i];
			}

		} 
		 range=max-min;

		if(/*range>PSORT_TRESHOLD ||*/ treshold<range)
		{
			//System.out.println("sw");
			sort(a,left,right,true);
		}
		else
		{
			//System.out.println("Hash sort called");
			PSort(a,range,min,max);
		} 
	}

	public static void PSort(int[] arr,int range,int min,int max)
	{
		int temp[]=new int[range+1];
		for(int i=0;i<arr.length;i++)
		{
			temp[arr[i]-min]++;
		}
		int k=0; 
		for(int i=0;i<temp.length;i++)
		{
  			while(temp[i]-->0)
  			{
				arr[k++]=i+min;
  			}   

		}
	}

	public static void sort(int[] a, int left, int right,int[] work, int workBase, int workLen) {
  			// Use Quicksort on small arrays
  			if(right - left < QUICKSORT_THRESHOLD) {
   				sort(a, left, right, true);
   				return;
  			}
			int min=a[0];
			int max=a[0];
  			int[] run = new int[MAX_RUN_COUNT + 1];
  			int count = 0;
  			run[0] = left;

  			// Check if the array is nearly sorted
  			for (int k = left; k < right; run[count] = k) {
   				if (a[k] < a[k + 1]) { 
				if(a[k]<min)
				min=a[k];
				// ascending
    					while (++k <= right && a[k - 1] <= a[k]);
					
					if(a[k-1]>max)
						max=a[k-1];
   				} 
				else if (a[k] > a[k + 1]) 
				{ 
					if(a[k]>max)
					max=a[k];
					// descending
    					while (++k <= right && a[k - 1] >= a[k]);
    					if(min>a[k-1])
						min=a[k-1];	
					for (int lo = run[count] - 1, hi = k; ++lo < --hi;) {
     					int t = a[lo];
     					a[lo] = a[hi];
     					a[hi] = t;
    					}
   				} 
				else
				{ 
				// equal
					
    					for (int m = MAX_RUN_LENGTH; ++k <= right && a[k - 1] == a[k];) {
     						if (--m == 0) {
      						sort(a, left, right, true);
      						return;
     						}
    					}
					if(a[k-1]>max)
					max=a[k-1];
					if(a[k-1]<min)
					min=a[k-1];
					
	
   				}

   /*
    * The array is not highly structured,
    * use Quicksort instead of merge sort.
    */
   				if (++count == MAX_RUN_COUNT) {
					HashSort(a,0,a.length-1,true,min,max,k);
					//sort(a, left, right, true);
    					return;
   				}
  			}

  		// Check special cases
	       // Implementation note: variable "right" is increased by 1.
  		if (run[count] == right++) { // The last run contains one element
   			run[++count] = right;
  		} else if (count == 1) { // The array is already sorted
   			return;
  		}

  // Determine alternation base for merge
  	byte odd = 0;
  	for(int n = 1;(n <<= 1) < count; odd ^= 1);

  // Use or create temporary array b for merging
  	int[] b; // temp array; alternates with a
  	int ao, bo; // array offsets from 'left'
  	int blen = right - left; // space needed for b
  	if (work == null || workLen < blen || workBase + blen > work.length) {
   		work = new int[blen];
   		workBase = 0;
  	}
  	if (odd == 0) {
   		System.arraycopy(a, left, work, workBase, blen);
   		b = a;
   		bo = 0;
   		a = work;
   		ao = workBase - left;
  	} else {
   		b = work;
   		ao = 0;
   		bo = workBase - left;
  	}

  // Merging
  	for (int last; count > 1; count = last) {
   		for (int k = (last = 0) + 2; k <= count; k += 2) {
    			int hi = run[k], mi = run[k - 1];
    				for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
     					if (q >= hi || p < mi && a[p + ao] <= a[q + ao]) {
      						b[i + bo] = a[p++ + ao];
     					} else {
      						b[i + bo] = a[q++ + ao];
     					}
    				}
    			run[++last] = hi;
   		}
   		if ((count & 1) != 0) {
    			for (int i = right, lo = run[count - 1]; --i >= lo; b[i + bo] = a[i + ao]);
    			run[++last] = right;
   		}
   		int[] t = a;
   		a = b;
   		b = t;
   		int o = ao;
   		ao = bo;
   		bo = o;
  		}
 	}



private static void sort(int[] a, int left, int right, boolean leftmost) {
  	int length = right - left + 1;
  // Use insertion sort on tiny arrays
  
     if (length < INSERTION_SORT_THRESHOLD) {
   		if (leftmost) {
    	/*
     		* Traditional (without sentinel) insertion sort,
 		  optimized for server VM, is used in case of
     		* the leftmost part.
     	*/
    
	for (int i = left, j = i; i < right; j = ++i) {
     		int ai = a[i + 1];
     		while (ai < a[j]) {
      			a[j + 1] = a[j];
      			if (j-- == left) {
       				break;
      			}
     		}
     		a[j + 1] = ai;
    	}
    } 
    else 
    {
    /*
     * Skip the longest ascending sequence.
     */
    	do {
     		if (left >= right) {
      			return;
     		}
    	   } while (a[++left] >= a[left - 1]);
    /*
     * Every element from adjoining part plays the role
     * of sentinel, therefore this allows us to avoid the
     * left range check on each iteration. Moreover, we use
     * the more optimized algorithm, so called pair insertion
     * sort, which is faster (in the context of Quicksort)
     * than traditional implementation of insertion sort.
     */

    for (int k = left; ++left <= right; k = ++left) {
	     int a1 = a[k], a2 = a[left];
     	     if (a1 < a2) {
      		a2 = a1;
      		a1 = a[left];
     	     }
     	while (a1 < a[--k]) {
      		a[k + 2] = a[k];
     	}
     a[++k + 1] = a1;
     while (a2 < a[--k]) {
      a[k + 1] = a[k];
     }
     a[k + 1] = a2;
    }
    int last = a[right];
    while (last < a[--right]) {
     a[right + 1] = a[right];
    }
    a[right + 1] = last;
   }
   return;
  }
  // Inexpensive approximation of length / 7
  int seventh = (length >> 3) + (length >> 6) + 1;
  /*
   * Sort five evenly spaced elements around (and including) the
   * center element in the range. These elements will be used for
   * pivot selection as described below. The choice for spacing
   * these elements was empirically determined to work well on
   * a wide variety of inputs.
   */
  int e3 = (left + right) >>> 1; // The midpoint
  int e2 = e3 - seventh;
  int e1 = e2 - seventh;
  int e4 = e3 + seventh;
  int e5 = e4 + seventh;
  // Sort these elements using insertion sort
  if (a[e2] < a[e1]) {
   int t = a[e2];
   a[e2] = a[e1];
   a[e1] = t;
  }
  if (a[e3] < a[e2]) {
   int t = a[e3];
   a[e3] = a[e2];
   a[e2] = t;
   if (t < a[e1]) {
    a[e2] = a[e1];
    a[e1] = t;
   }
  }
  if (a[e4] < a[e3]) {
   int t = a[e4];
   a[e4] = a[e3];
   a[e3] = t;
   if (t < a[e2]) {
    a[e3] = a[e2];
    a[e2] = t;
     a[e2] = a[e1];
    if (t < a[e1]) {
     a[e1] = t;
    }
   }
  }
  if (a[e5] < a[e4]) {
   int t = a[e5];
   a[e5] = a[e4];
   a[e4] = t;
   if (t < a[e3]) {
    a[e4] = a[e3];
    a[e3] = t;
    if (t < a[e2]) {
     a[e3] = a[e2];
     a[e2] = t;
     if (t < a[e1]) {
      a[e2] = a[e1];
      a[e1] = t;
     }
    }
   }
  }
  // Pointers
  int less = left; // The index of the first element of center part
  int great = right; // The index before the first element of right part
  if (a[e1] != a[e2] && a[e2] != a[e3] && a[e3] != a[e4] && a[e4] != a[e5]) {
   /*
    * Use the second and fourth of the five sorted elements as pivots.
    * These values are inexpensive approximations of the first and
    * second terciles of the array. Note that pivot1 <= pivot2.
    */
   int pivot1 = a[e2];
   int pivot2 = a[e4];
   /*
    * The first and the last elements to be sorted are moved to the
    * locations formerly occupied by the pivots. When partitioning
    * is complete, the pivots are swapped back into their final
    * positions, and excluded from subsequent sorting.
    */
   a[e2] = a[left];
   a[e4] = a[right];
   /*
    * Skip elements, which are less or greater than pivot values.
    */

   while (a[++less] < pivot1);
   while (a[--great] > pivot2);
   /*
    * Partitioning:
    *
    *   left part           center part                   right part
    * +--------------------------------------------------------------+
    * |  < pivot1  |  pivot1 <= && <= pivot2  |    ?    |  > pivot2  |
    * +--------------------------------------------------------------+
    *               ^                          ^       ^
    *               |                          |       |
    *              less                        k     great
    *
    * Invariants:
    *
    *              all in (left, less)   < pivot1
    *    pivot1 <= all in [less, k)     <= pivot2
    *              all in (great, right) > pivot2
    *
    * Pointer k is the first index of ?-part.
    */
   outer:
    for (int k = less - 1; ++k <= great;) {
     int ak = a[k];
     if (ak < pivot1) { // Move a[k] to left part
      a[k] = a[less];
      /*
       * Here and below we use "a[i] = b; i++;" instead
       * of "a[i++] = b;" due to performance issue.
       */
      a[less] = ak;
      ++less;
     } else if (ak > pivot2) { // Move a[k] to right part
      while (a[great] > pivot2) {
       if (great-- == k) {
        break outer;
       }
      }
      if (a[great] < pivot1) { // a[great] <= pivot2
       a[k] = a[less];
       a[less] = a[great];
       ++less;
      } else { // pivot1 <= a[great] <= pivot2
       a[k] = a[great];
      }
      /*
       * Here and below we use "a[i] = b; i--;" instead
       * of "a[i--] = b;" due to performance issue.
       */
      a[great] = ak;
      --great;
     }
    }
   // Swap pivots into their final positions
   a[left] = a[less - 1];
   a[less - 1] = pivot1;
   a[right] = a[great + 1];
   a[great + 1] = pivot2;
   // Sort left and right parts recursively, excluding known pivots
   sort(a, left, less - 2, leftmost);
   sort(a, great + 2, right, false);
   /*
    * If center part is too large (comprises > 4/7 of the array),
    * swap internal pivot values to ends.
    */
   if (less < e1 && e5 < great) {
    /*
     * Skip elements, which are equal to pivot values.
     */
    while (a[less] == pivot1) {
     ++less;
    }
    while (a[great] == pivot2) {
     --great;
    }
    /*
     * Partitioning:
     *
     *   left part         center part                  right part
     * +----------------------------------------------------------+
     * | == pivot1 |  pivot1 < && < pivot2  |    ?    | == pivot2 |
     * +----------------------------------------------------------+
     *              ^                        ^       ^
     *              |                        |       |
     *             less                      k     great
     *
     * Invariants:
     *
     *              all in (*,  less) == pivot1
     *     pivot1 < all in [less,  k)  < pivot2
     *              all in (great, *) == pivot2
     *
     * Pointer k is the first index of ?-part.
     */
    outer:
     for (int k = less - 1; ++k <= great;) {
      int ak = a[k];
      if (ak == pivot1) { // Move a[k] to left part
       a[k] = a[less];
       a[less] = ak;
       ++less;
      } else if (ak == pivot2) { // Move a[k] to right part
       while (a[great] == pivot2) {
        if (great-- == k) {
         break outer;
        }
       }
       if (a[great] == pivot1) { // a[great] < pivot2
        a[k] = a[less];
        /*
         * Even though a[great] equals to pivot1, the
         * assignment a[less] = pivot1 may be incorrect,
         * if a[great] and pivot1 are floating-point zeros
         * of different signs. Therefore in float and
         * double sorting methods we have to use more
         * accurate assignment a[less] = a[great].
         */
        a[less] = pivot1;
        ++less;
       } else { // pivot1 < a[great] < pivot2
        a[k] = a[great];
       }
       a[great] = ak;
       --great;
      }
     }
   }
   // Sort center part recursively
   sort(a, less, great, false);
  } else { // Partitioning with one pivot
   /*
    * Use the third of the five sorted elements as pivot.
    * This value is inexpensive approximation of the median.
    */
   int pivot = a[e3];
   /*
    * Partitioning degenerates to the traditional 3-way
    * (or "Dutch National Flag") schema:
    *
    *   left part    center part              right part
    * +-------------------------------------------------+
    * |  < pivot  |   == pivot   |     ?    |  > pivot  |
    * +-------------------------------------------------+
    *              ^              ^        ^
    *              |              |        |
    *             less            k      great
    *
    * Invariants:
    *
    *   all in (left, less)   < pivot
    *   all in [less, k)     == pivot
    *   all in (great, right) > pivot
    *
    * Pointer k is the first index of ?-part.
    */
   for (int k = less; k <= great; ++k) {
    if (a[k] == pivot) {
     continue;
    }
    int ak = a[k];
    if (ak < pivot) { // Move a[k] to left part
     a[k] = a[less];
     a[less] = ak;
     ++less;
    } else { // a[k] > pivot - Move a[k] to right part
     while (a[great] > pivot) {
      --great;
     }
     if (a[great] < pivot) { // a[great] <= pivot
      a[k] = a[less];
      a[less] = a[great];
      ++less;
     } else { // a[great] == pivot
      /*
       * Even though a[great] equals to pivot, the
       * assignment a[k] = pivot may be incorrect,
       * if a[great] and pivot are floating-point
       * zeros of different signs. Therefore in float
       * and double sorting methods we have to use
       * more accurate assignment a[k] = a[great].
       */
      a[k] = pivot;
     }
     a[great] = ak;
     --great;
    }
   }
   /*
    * Sort left and right parts recursively.
    * All elements from center part are equal
    * and, therefore, already sorted.
    */
   sort(a, left, less - 1, leftmost);
   sort(a, great + 1, right, false);
  }
}
}
