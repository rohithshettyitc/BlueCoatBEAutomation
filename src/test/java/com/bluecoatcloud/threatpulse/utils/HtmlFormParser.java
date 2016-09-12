package com.bluecoatcloud.threatpulse.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlFormParser {

private static String testHtml = "<html>  <body>    <h2>      Enter Following Details.    </h2>    <form id=\"myform\" name=\"myform\">      <table>      <tr>          <td>            UserName :          </td>          <td>            <input name=\"username\" type=\"text\" value=\"somevalue\">          </td>        </tr>      </table>    </form>  </body></html>";

public static void main(String[] args) {
    //parseHtml(testHtml);
}

public Map<String, String> parseHtml(String html) {
    Matcher matcher;
    Map<String, String> parameters;
    // pull out all parameters in the form
    parameters = new HashMap<String, String>();
    
    matcher = formPattern.matcher(html);
    while (matcher.find()) {
        Map<String, String> attributes = parseAttributes(matcher.group(1));
        // ignore buttons        
        String action = attributes.get("action");
        String encType = attributes.get("encType");
        String accept = attributes.get("accept-charset");
        
        parameters.put("action", action);
        parameters.put("encType", encType);
        parameters.put("accept-charset", accept);
    }
    
    
    matcher = inputPattern.matcher(html);
    while (matcher.find()) {
        Map<String, String> attributes = parseAttributes(matcher.group(1));
        // ignore buttons
        String type = attributes.get("type");
        if (type != null
                && (type.equalsIgnoreCase("submit") || type
                        .equalsIgnoreCase("button"))) {
            continue;
        }
        String name = attributes.get("name");
        if (name != null) {
            String value = attributes.get("value");
            if (value == null) {
                value = "";
            }
            parameters.put(name, value);
           // System.out.println("Key  : "+name+"  , value : "+value);
        }
    }
    return parameters;
}

private static Map<String, String> parseAttributes(String attributesStr) {
    Map<String, String> attributes = new HashMap<String, String>();
    Matcher matcher = attributePattern.matcher(attributesStr);
    while (matcher.find()) {
        String key = matcher.group(1);
        String value = "";
        String g = matcher.group(2).trim();
        if (g != null) {
            value = g;
        }
        System.out.println(key + " ,  " + value);
        attributes.put(key, value.trim());
    }
    return attributes;
}
/**
 * The regex pattern used to find a form element in HTML.
 */
private static final Pattern formPattern = Pattern.compile("<form(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
/**
 * The regex pattern to find a form input parameter in HTML.
 */
private static final Pattern inputPattern = Pattern.compile("<input(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

private static final Pattern attributePattern = Pattern.compile("(\\w+)=\"(.*?)\"");

}
