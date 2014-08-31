package epp;

/**
 *
 * @author medafi
 */
public final class reportToR {

        // Put here the same thing what will be written into the file
        public static String reportThis;

        public static void setResult (String putThis){
              reportThis = putThis;
        }

        public static String getResult (){
              return reportThis ;
        }


}
