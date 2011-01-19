package frontier.common;

import net.oauth.OAuthException;

/**
 * Indicates that the server should redirect the HTTP client.
 * 
 * @author John Kristian
 */
public class RedirectException extends OAuthException{

    public RedirectException(String targetURL) {
        super(targetURL);
    }

    public String getTargetURL() {
        return getMessage();
    }

    private static final long serialVersionUID = 1L;

}
