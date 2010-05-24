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

import java.util.*;
import java.io.*;

/**
 * The information used for one tag.
 * The token is only valid while reading a java file and deleted afterwards
 * (to allow the garbage collector to work)
 */
public class Tag {
        public String tag;
        public String file;
        public String cmd;

        public Tag() { }

        public Tag(String n,String f,String c) {
                tag=n;
                file=f;
                cmd=c;
        }

        public Tag(String s) {
            StringTokenizer st=new StringTokenizer(s,"\t");
            tag=st.nextToken();
            file=st.nextToken();
            cmd=st.nextToken();
            cmd=cmd.substring(0,cmd.length()-2);
        }

        public String toString() {
                return tag+'\t'+file+'\t'+cmd+";\"";
        }
}
