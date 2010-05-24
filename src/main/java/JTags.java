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
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import java.io.*;

/**
 * Main JTags class
 */
public class JTags {
    static public boolean searchBackwards=false;
    static public boolean addClasses=true;
    static public boolean addMethods=true;
    static public boolean addConstants=true;
    static public boolean addMembers=true;
    static public boolean addFile=true;
    static public boolean addPrivate=false;
    static public boolean addDefault=true;

    static public boolean append=false;
    static public boolean sorting=true;
    static public boolean verbose=false;
    static public boolean patterns=false;

    static public String tagfile="tags";
    static private boolean gottagfile=false;

    static private TagVector tags=new TagVector();

    static public long lastTagFileModification=0;
    static public boolean changed=false;


    static private JavaParser parser;

    static private void readTagFile() {
        if(gottagfile) abort("there cannot be two tag files!");
        gottagfile=true;
        try {
            File ft=new File(tagfile);
            if(ft.exists()) lastTagFileModification=ft.lastModified();
        } catch(Exception e) { }
        FileReader fr=null;
        try {
            fr=new FileReader(tagfile);
        } catch(Exception e) {
            return;
        }
        try {
            BufferedReader br=new BufferedReader(fr);
            String line=br.readLine();
            if(!line.startsWith("!_TAG_FILE")) abort("tag file has wrong format");
            if(verbose) System.out.println("JTags: reading tag file "+tagfile);
            while(line!=null) {
                line=br.readLine();
                if(line==null) break;
                if(line.startsWith("!_TAG")) continue;
                try { 
                    tags.addElement(new Tag(line));
                } catch(Exception e)  { }
            }
            fr.close();
        } catch(Exception e) {
            if(verbose) e.printStackTrace();
            abort("error reading tag file (maybe its empty?): "+e.getMessage());
        }
    }

    static private void abort(String s) {
        System.err.println("Error: "+s);
        usage(System.err);
        System.exit(1);
    }

    static private void usage(PrintStream s) {
        s.println("  Usage: jtags [-acBFnNuv] [-{f|o} name] [-h list] [-i [+-=]types]");
        s.println("               [-I list] [-L file] [-p path] [--append] [--excmd=n|p]");
        s.println("               [--help] [--sort] [--version] file(s) and/or directorie(s)");
        s.println("");
        s.println("  -a   Append the tags to an existing tag file (default: remove the");
        s.println("       file)");
        s.println("  -c   Read only files that have changed since the last time the tag");
        s.println("       file has been written.");
        s.println("  -B   Use backward searching patterns (?...?).");
        s.println("  -f <name>");
        s.println("       Output tags to the specified file (default is \"tags\"");
        s.println("  -F   Use forward searching patterns (/.../) (default).");
        s.println("  -i <types>");
        s.println("       Specifies the list of tag types to include in the output file.");
        s.println("       \"Types\" is a group of letters designating the types of tags");
        s.println("       affected. Each letter or group of letters may be preceded by");
        s.println("       either a '+' sign (default, if omitted) to add it to those already");
        s.println("       included, a '-' sign to exclude it from the list (e.g. to exclude");
        s.println("       a default tag type), or an '=' sign to include its corresponding");
        s.println("       tag type at the exclusion of those not listed. A space separating");
        s.println("       the option letter from the list is optional. The following tag");
        s.println("       types are supported (default settings are shown in brackets):");
        s.println("          c   class names [on]");
        s.println("          e   constants [on]");
        s.println("          f   methods [on]");
        s.println("          m   class data members [on]");
        s.println("       In addition, the following modifiers are accepted:");
        s.println("          F   include source filenames as tags [off]");
        s.println("          S   include tags that are only visible in one package");
        s.println("          P   include tags that are only visible in one file");
        s.println("  -L <file>");
        s.println("       A list of source file names are read from the specified file.");
        s.println("       If specified as \"-\", then standard input is read.");
        s.println("  -n   Equivalent to --excmd=number.");
        s.println("  -N   Equivalent to --excmd=pattern.");
        s.println("  -o   Alternative for -f.");
        s.println("  -u   Equivalent to --sort=no.");
        s.println("  -v   turn on verbose mode");
        s.println("  --append=[yes|no]");
        s.println("       Indicates whether tags should be appended to existing tag file");
        s.println("       (default=no).");
        s.println("  --excmd=number|pattern");
        s.println("       Uses the specified type of EX command to locate tags (default=pattern).");
        s.println("  --help");
        s.println("       Prints this option summary.");
        s.println("  --sort=[yes|no]");
        s.println("       Indicates whether tags should be sorted (default=yes).");
        s.println("  --version");
        s.println("       Prints a version identifier to standard output.");
    }

    public static void main(String[] a) {
        if(a.length==0) {
            System.err.println("jtags: No files specified. Try \"jtags --help\".");
            System.exit(1);
        }
        for(int i=0;i<a.length;i++) {
            if(a[i].equals("-a") || a[i].equals("--append=yes")) {
                append=true;
            } else if(a[i].equals("--append=no")) {
                append=false;
            } else if(a[i].equals("-c")) {
                changed=true;
            } else if(a[i].equals("--version")) {
                System.out.println("Exuberant JTags 0.5, by Claudio Fleiner (claudio@fleiner.com)");
            } else if(a[i].equals("--sort=yes")) {
                sorting=true;
            } else if(a[i].equals("-u") || a[i].equals("--sort=no")) {
                sorting=false;
            } else if(a[i].equals("-v")) {
                verbose=true;
            } else if(a[i].equals("-n") || a[i].equals("--excmd=number")) {
                patterns=false;
            } else if(a[i].equals("-N") || a[i].equals("--excmd=pattern")) {
                patterns=true;
            } else if(a[i].equals("-B")) {
                searchBackwards=true;
            } else if(a[i].equals("-F")) {
                searchBackwards=false;
            } else if(a[i].equals("-L")) {
                if(i==a.length-1) abort("the option -L needs an additional argument");
                readFileList(a[++i]);
            } else if(a[i].equals("-f") || a[i].equals("-o")) {
                if(i==a.length-1) abort("the option -f needs an additional argument");
                tagfile=a[++i];
                readTagFile();
            } else if(a[i].equals("-i")) {
                if(i==a.length-1) abort("open -i needs an additional argument");
                i++;
                String tp=a[i];
                boolean add=true;
                for(int j=0;j<tp.length();j++) {
                    switch(tp.charAt(j)) {
                        case '+': add=true;break;
                        case '-': add=false;break;
                        case '=': add=true;
                                  addClasses=addMethods=addConstants=
                                      addMembers=addFile=addPrivate=addDefault=false;
                                  break;
                        case 'c': addClasses=add;break;
                        case 'f': addMethods=add;break;
                        case 'e': addConstants=add;break;
                        case 'm': addMembers=add;break;
                        case 'F': addFile=add;break;
                        case 'P': addPrivate=add;if(add) addDefault=true;break;
                        case 'S': addDefault=add;if(!add) addPrivate=false;break;
                        default: abort("unknwon tag type "+tp.charAt(j));
                    }
                }
            } else if(a[i].equals("--help")) {
                usage(System.out);
                return;
            } else if(a[i].charAt(0)=='-') {
                abort("unknown option "+a[i]);
            } else {
                readFile(a[i]);
            }
        }
        try {
            FileWriter fw=new FileWriter(tagfile);
            PrintWriter pw=new PrintWriter(fw);
            pw.println("!_TAG_FILE_FORMAT\t2\t/extended format");
            pw.println("!_TAG_FILE_SORTED\t"+(sorting?'1':'0')+"\t/0=unsorted, 1=sorted/");
            pw.println("!_TAG_PROGRAM_AUTHOR\tClaudio Fleiner\t/claudio@fleiner.com/");
            pw.println("!_TAG_PROGRAM_NAME\tExuberant JTags\t/modeled after Exuberant CTags/");
            pw.println("!_TAG_PROGRAM_URL\thttp://www.fleiner.com/jtags\t/official site/");
            pw.println("!_TAG_PROGRAM_VERSION\t1.0\t//");
            if(sorting) tags.sort();
            for(int i=0;i<tags.size();i++) {
                Tag t=(Tag)tags.elementAt(i);
                pw.println(t);
            }
            pw.close();
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            abort("Error writing tag file: "+e.getMessage());
        }
    }

    private static void readFileList(String f) {
        try{ 
            FileReader fr=null;
            BufferedReader br=null;
            if(f.equals("-")) {
                br=new BufferedReader(new InputStreamReader(System.in));
            } else {
                fr=new FileReader(f);
                br=new BufferedReader(fr);
            }
            String line;
            while((line=br.readLine())!=null)
                readFile(line);
            br.close();
            if(fr!=null) fr.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void readDir(String sdir,File dir) {
        if(verbose) System.out.println("reading directory "+sdir);
        String[] f=dir.list();
        for(int i=0;i<f.length;i++) {
            if(f[i].endsWith(".java"))
                readFile(sdir+File.separator+f[i]);
            else {
                File nf=new File(sdir+File.separator+f[i]);
                if(nf.isDirectory()) 
                    readDir(sdir+File.separator+f[i],nf);
            }
        }
    }

    private static void readFile(String f) {
        if(!gottagfile) {
            readTagFile();
        }

        try {
            File ff=new File(f);
            if(ff.isDirectory()) {
                readDir(f,ff);return; 
            }

            if(changed && ff.lastModified()<lastTagFileModification) {
                return;
            }
            if(verbose) System.out.println("parsing "+f);
            if(!append) tags.deleteFile(f);
            FileInputStream fin=new FileInputStream(f);
            BufferedInputStream bin=new BufferedInputStream(fin);
            CompilationUnit cu;
            cu = JavaParser.parse(bin);
            CodeVisitor codeVisitor = new CodeVisitor();
            codeVisitor.visit(cu, f);
            bin.close();
            fin.close();

            for(int j=0;j<codeVisitor.tags.size();j++)
                tags.addElement(codeVisitor.tags.elementAt(j));

            codeVisitor.tags.setSize(0);
        } catch(FileNotFoundException e) {
            System.err.println("jtags: file "+f+" not found, ignoring it");
        } catch(ParseException e) {
            System.err.println("jtags: error while parsing file "+f+"\n"+e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
