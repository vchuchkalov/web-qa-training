package test;

import app.Application;
import org.junit.After;
import org.junit.Before;

import java.net.MalformedURLException;


/**
 * Created by Иван on 17.03.2018.
 */
public class BaseRunner {

    public static ThreadLocal<Application> tlApp = new ThreadLocal<>();
    public Application app;

    @Before
    public void start() throws MalformedURLException {
        if (tlApp.get() != null) {
            app = tlApp.get();
            return;
        }
        app = new Application();
        tlApp.set(app);
    }

    @After
    public void tearDown() {
        app.quit();
    }
}
