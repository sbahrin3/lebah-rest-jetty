/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class UIDGenerator {
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
    
    public static String getUID() {
        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = new Integer( prng.nextInt() ).toString();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result =  sha.digest( randomNum.getBytes() );
            return hexEncode(result);
        }
        catch ( NoSuchAlgorithmException ex ) {
            System.err.println(ex);
            return "";
        }
    }

    static private String hexEncode( byte[] aInput){
        StringBuffer result = new StringBuffer();
        char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
        for ( int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append( digits[ (b&0xf0) >> 4 ] );
            result.append( digits[ b&0x0f] );
        }
        return result.toString();
    }
    
    public static void main(String[] args) {
    	for ( int i = 0; i < 10; i++ ) {
	        String uid = UIDGenerator.getUID();
	        System.out.println(i + ") " + uid);
    	}
    }

}
