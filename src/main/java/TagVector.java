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
import java.io.*;

/**
 * Implements a Vector of tags that can be sorted
 */

public class TagVector extends SortVector {

    protected int compare(Object a,Object b) {
        Tag ta=(Tag)a;
        Tag tb=(Tag)b;
        try {
            return ta.tag.compareTo(tb.tag);
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("a="+a);
            System.err.println("b="+b);
        }
        return 1;
    }

    static private String toSearch(String s) {
        int i=-2;
        while((i=s.indexOf('\\',i+2))>=0) 
            s=s.substring(0,i)+'\\'+s.substring(i);
        i=-2;
        while((i=s.indexOf('$',i+2))>=0) 
            s=s.substring(0,i)+'\\'+s.substring(i);
        i=-2;
        while((i=s.indexOf('/',i+2))>=0) 
            s=s.substring(0,i)+'\\'+s.substring(i);
        i=-2;
        while((i=s.indexOf('?',i+2))>=0) 
            s=s.substring(0,i)+'\\'+s.substring(i);
        i=-2;
        while((i=s.indexOf('^',i+2))>=0) 
            s=s.substring(0,i)+'\\'+s.substring(i);

        while((i=s.indexOf('\t'))>=0) 
            s=s.substring(0,i)+"\\t"+s.substring(i+1);
        while((i=s.indexOf('\r'))>=0) 
            s=s.substring(0,i)+"\\r"+s.substring(i+1);
        while((i=s.indexOf('\n'))>=0) 
            s=s.substring(0,i)+"\\n"+s.substring(i+1);
        if(JTags.searchBackwards)
            return "?^"+s+"$?";
        else
            return "/^"+s+"$/";
    }

    public void deleteFile(String f) {
        int j=0;
        for(int i=0;i<elementCount;i++)
            if(!((Tag)elementData[i]).file.equals(f)) 
                elementData[j++]=elementData[i];
        elementCount=j;
    }
}
