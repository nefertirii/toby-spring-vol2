package tobyspring.springbook.learningtest.spring.ioc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValueInjectionTest {
    @Test
    public void valueInjection() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanSP.class, ConfigSP.class);
        BeanSP bean = ac.getBean(BeanSP.class);
        assertThat(bean.name).isEqualTo("Mac OS X");
        assertThat(bean.username).isEqualTo("Spring");

        assertThat(bean.hello.name).isEqualTo("Spring");
    }

    static class BeanSP {
        @Value("#{systemProperties['os.name']}")    // 시스템 프로퍼티
        String name;
        @Value("${database.username}")
        String username;
        @Value("${os.name}")
        String osname;
        @Autowired
        Hello hello;
    }

    @Configuration
    @PropertySource("database.properties")
    static class ConfigSP {
        @Bean
        Hello hello(@Value("${database.username}") String username) {
            Hello hello = new Hello();
            hello.name = username;
            return hello;
        }

        @Bean
        static PropertySourcesPlaceholderConfigurer pspc() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    static class Hello {
        String name;
    }

    @Test
    void importResource() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ConfigIR.class);
        BeanSP bean = ac.getBean(BeanSP.class);

        assertThat(bean.name).isEqualTo("Mac OS X");
        assertThat(bean.username).isEqualTo("Spring");
    }

    @ImportResource("/properties2.xml")
    @Configuration
    static class ConfigIR {
        @Bean
        public BeanSP beanSp() {
            return new BeanSP();
        }
        @Bean Hello hello() {
            return new Hello();
        }
    }

    @Test
    void propertyEditor() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanPE.class);
        BeanPE bean = ac.getBean(BeanPE.class);

        assertThat(bean.charset).isEqualTo(Charset.forName("UTF-8"));
        assertThat(bean.intarr).isEqualTo(new int[] {1,2,3});
        assertThat(bean.flag).isTrue();
        assertThat(bean.rate).isEqualTo(1.2);
        assertThat(bean.file.exists()).isTrue();
    }

    static class BeanPE {
        @Value("UTF-8") Charset charset;
        @Value("1,2,3") int[] intarr;
        @Value("true") boolean flag;
        @Value("1.2") double rate;
        @Value("classpath:/test-applicationContext.xml") File file;
    }

    @Test
    void collectionInject() {
        ApplicationContext ac = new GenericXmlApplicationContext("/collection.xml");
        BeanC bean = ac.getBean(BeanC.class);

        assertThat(bean.nameList.size()).isEqualTo(3);
        assertThat(bean.nameList.get(0)).isEqualTo("Spring");
        assertThat(bean.nameList.get(1)).isEqualTo("IoC");
        assertThat(bean.nameList.get(2)).isEqualTo("DI");

        assertThat(bean.nameSet.size()).isEqualTo(3);

        assertThat(bean.ages.get("Kim")).isEqualTo(30);
        assertThat(bean.ages.get("Lee")).isEqualTo(35);
        assertThat(bean.ages.get("Ahn")).isEqualTo(40);

        assertThat((String)bean.settings.get("username")).isEqualTo("Spring");
        assertThat((String)bean.settings.get("password")).isEqualTo("Book");

        assertThat(bean.beans.size()).isEqualTo(2);
    }

    static class BeanC {
        List<String> nameList;
        Set<String> nameSet;
        Map<String, Integer> ages;
        Properties settings;
        List beans;

        public void setNameList(List<String> names) {
            this.nameList = names;
        }

        public void setNameSet(Set<String> nameSet) {
            this.nameSet = nameSet;
        }

        public void setAges(Map<String, Integer> ages) {
            this.ages = ages;
        }

        public void setSettings(Properties settings) {
            this.settings = settings;
        }

        public void setBeans(List beans) {
            this.beans = beans;
        }
    }
}
