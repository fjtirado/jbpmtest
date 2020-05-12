package test;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class DummyInitialContextFactory implements InitialContextFactory {

    public DummyInitialContextFactory() {}

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return new DummyContext();
    }

}
