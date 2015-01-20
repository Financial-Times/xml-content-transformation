package com.ft.bodyprocessing.html;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.HTML5VoidElementHandlingXMLBodyWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Html5SelfClosingTagBodyProcessorVoidElementsTest
 *
 * @author Simon.Gibbs
 */
@RunWith(Parameterized.class)
public class Html5SelfClosingTagBodyProcessorNonVoidElementsTest {

    private static final String[] NON_VOID_ELEMENT_EXAMPLES = new String[] {
            "p", "em" , "b", "i" , "strong"
    };
    private String elementName;

    @Parameterized.Parameters(name= "{index}: {0}")
    public static Collection<String[]> voidElements() {
        List<String[]> params = new ArrayList<String[]>(HTML5VoidElementHandlingXMLBodyWriter.VOID_ELEMENTS.size());
        for(String name : NON_VOID_ELEMENT_EXAMPLES) {
            params.add(new String[] { name });
        }
        return params;
    }

    public Html5SelfClosingTagBodyProcessorNonVoidElementsTest(String elementName) {
        this.elementName = elementName;
    }

    @Test
    public void shouldRenderVoidElementsAsTwoTagsWhenOneIsSupplied() {
            String example = String.format("<body><%s/></body>",elementName);
            String expectedResult = String.format("<body><%s></%s></body>",elementName,elementName);
            Html5SelfClosingTagBodyProcessor processor = new Html5SelfClosingTagBodyProcessor();
            String result = processor.process(example, someContext());

            assertThat(result,is(expectedResult));

    }


    private BodyProcessingContext someContext() {
        return new BodyProcessingContext() {};
    }
}
