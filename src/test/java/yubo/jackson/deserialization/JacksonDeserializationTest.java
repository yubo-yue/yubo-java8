package yubo.jackson.deserialization;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NonNull;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JacksonDeserializationTest {
    @Test
    public void whenDeserializingUsingJsonInject_thenCorrect() throws IOException {
        final String jsonInput = "{\"name\":\"My bean\"}";

        final InjectableValues injectableValues = new InjectableValues.Std().addValue(int.class, 2);
        final BeanWithInject bean = new ObjectMapper()
                .reader(injectableValues)
                .forType(BeanWithInject.class)
                .readValue(jsonInput);

        assertThat(bean.getName(), is(Matchers.equalTo("My bean")));
        assertThat(bean.getId(), is(2));
    }

    @Test
    public void whenDeserializingUsingJsonAnySetter_thenCorrect() throws IOException {
        final String jsonInput = "{\"name\":\"My name\", \"attr1\":\"val1\", \"attr2\":\"val2\"}";

        final ExtendableBean bean = new ObjectMapper().readerFor(ExtendableBean.class).readValue(jsonInput);

        assertThat(bean.getName(), is(equalTo("My name")));
        assertThat(bean.getProperties().get("attr2"), is(equalTo("val2")));
    }

    @Data
    private static class BeanWithInject {
        @JacksonInject
        public int id;
        public String name;
    }

    @Data
    private static class ExtendableBean {
        private String name;
        private Map<String, String> properties = Maps.newHashMap();

        @JsonAnySetter
        public void add(@NonNull final String key, @NonNull final String value) {
            properties.put(key, value);
        }
    }
}
