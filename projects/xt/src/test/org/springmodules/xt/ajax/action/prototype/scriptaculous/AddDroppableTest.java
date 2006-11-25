package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class AddDroppableTest extends XMLEnhancedTestCase {
    
    public AddDroppableTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        AddDroppable action = new AddDroppable("id");
        action.addOption("k1", "v1");
        action.addOption("k2", "v2");
        
        String rendering = action.execute();
        
        System.out.println(rendering);
    }
}
