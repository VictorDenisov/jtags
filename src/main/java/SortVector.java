/**
 * (C) Copyright 1998 by International Business Machines Corporation
 * 
 * Author: Claudio Fleiner <cfl@almaden.ibm.com>
 * 
 * The Software is owned by International Business Machines Corporation or one of
 * its subsidiaries ("IBM") and is copyrighted and licensed, not sold. 
 * 
 * IBM grants you a non-exclusive, non-transferable license to download the
 * Software. Implied licenses are negated.
 * 
 * You may not merge, distribute (for free or for sale) or sublicense the Software;
 * 
 * IBM licenses the Software to you on an "as is" basis, without warranty of any
 * kind. IBM hereby expressly disclaims all warranties or conditions, either
 * express or implied, including, but not limited to, the implied warranties or
 * conditions of merchantability and fitness for a particular purpose. You are
 * solely responsible for determining the appropriateness of using this Software
 * and assume all risks associated with the use of this Software, including but
 * not limited to the risks of program errors, damage to or loss of data, programs
 * or equipment, and unavailability or interruption of operations. Some
 * jurisdictions do not allow for the exclusion or limitation of implied
 * warranties, so the above limitations or exclusions may not apply to you.
 * 
 * IBM will not be liable for any direct damages or for any special, incidental,
 * or indirect damages or for any economic consequential damages (including lost
 * profits or savings), even if IBM has been advised of the possibility of such
 * damages. IBM will not be liable for the loss of, or damage to, your records or
 * data, or any damages claimed by you based on a third party claim. Some
 * jurisdictions do not allow for the exclusion or limitation of incidental or
 * consequential damages, so the above limitations or exclusions may not apply to
 * you.
 */
import java.util.Vector;

public abstract class SortVector extends Vector {
	abstract protected int compare(Object a,Object b);

	/* 
	private void testPrint() {
		String t="{ ";
		for(int i=0;i<elementCount;i++)
			t+=elementData[i]+" ";
		System.out.println(t+"}");
	}
	*/

	private void swap(int l,int h) {
		if(l!=h) { 
			Object o=elementData[l];
			elementData[l]=elementData[h];
			elementData[h]=o;
		}
	}
	private void qsort(int l,int h) {
		if(l>=h) return;
		if(l+1==h) {
			if(compare(elementData[l],elementData[h])>0) swap(l,h);
			return;
		}
		int m=(l+h)/2;
		Object s=elementData[m];
		int ll=l;
		int hh=h;
		while(hh>ll) {
			while(ll<h && compare(elementData[ll],s)<=0) ll++;
			while(hh>l && compare(s,elementData[hh])<=0) hh--;
			if(hh>ll) {
				swap(ll,hh);
				ll++;
				hh--;
			} 
		}
		if(h==hh) {
			swap(m,h);
			qsort(l,h-1);
		} else if(l==ll) {
			swap(l,m);
			qsort(l+1,h);
		} else {
			qsort(l,hh);
			qsort(ll,h);
		}
	}

	public void sort() {
		qsort(0,elementCount-1);
	}
}

