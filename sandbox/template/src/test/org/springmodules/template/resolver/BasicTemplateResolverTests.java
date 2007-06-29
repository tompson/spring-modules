package org.springmodules.template.resolver;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import junit.framework.*;
import org.easymock.MockControl;
import org.springmodules.template.resolver.BasicTemplateResolver;
import org.springmodules.template.TemplateEngine;
import org.springmodules.template.Template;
import org.springmodules.template.TemplateGenerationException;
import org.springmodules.util.StringResource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Uri Boness
 */
public class BasicTemplateResolverTests extends TestCase {

    private BasicTemplateResolver resolver;

    private TemplateEngine engine;
    private MockControl engineControl;

    private ResourceLoader loader;
    private MockControl loaderControl;

    protected void setUp() throws Exception {

        engineControl = MockControl.createControl(TemplateEngine.class);
        engine = (TemplateEngine)engineControl.getMock();

        loaderControl = MockControl.createControl(ResourceLoader.class);
        loader = (ResourceLoader)loaderControl.getMock();

        resolver = new BasicTemplateResolver();
        resolver.setEngine(engine);
        resolver.setResourceLoader(loader);
    }

    public void testResolve() throws Exception {
        String name = "name";

        StringResource resource = new StringResource("resource");
        loaderControl.expectAndReturn(loader.getResource("name"), resource);

        Template template = new DummyTemplate();
        engineControl.expectAndReturn(engine.createTemplate(resource, "UTF-8"), template);

        loaderControl.replay();
        engineControl.replay();

        Template result = resolver.resolve(name);

        assertSame(template, result);

        loaderControl.verify();
        engineControl.verify();
    }

    public void testResolve_WithEncoding() throws Exception {
        String name = "name";
        String encoding = "encoding";

        StringResource resource = new StringResource("resource");
        loaderControl.expectAndReturn(loader.getResource("name"), resource);

        Template template = new DummyTemplate();
        engineControl.expectAndReturn(engine.createTemplate(resource, encoding), template);

        loaderControl.replay();
        engineControl.replay();

        Template result = resolver.resolve(name, encoding);

        assertSame(template, result);

        loaderControl.verify();
        engineControl.verify();
    }
    

    //============================================== Inner Classes =====================================================

    protected class DummyTemplate implements Template {

        public void generate(OutputStream out, Map model) throws TemplateGenerationException {
        }

        public void generate(Writer writer, Map model) throws TemplateGenerationException {
        }

        public String generate(Map model) throws TemplateGenerationException {
            return null;
        }
    }
}