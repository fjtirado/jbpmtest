package test;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class DummyContext implements Context {

    private static Map<String, Object> map = new ConcurrentHashMap<>();

    private static class DummyName implements Name {

        private String string;

        public Object clone() {
            return this;
        }

        public DummyName(String string) {
            this.string = string;
        }

        @Override
        public int compareTo(Object obj) {
            return string.compareTo(obj.toString());
        }

        @Override
        public int size() {
            return string.length();
        }

        @Override
        public boolean isEmpty() {
            return string.isEmpty();
        }

        @Override
        public Enumeration<String> getAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String get(int posn) {
            return string;
        }

        @Override
        public Name getPrefix(int posn) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public Name getSuffix(int posn) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public boolean startsWith(Name n) {
            return false;
        }

        @Override
        public boolean endsWith(Name n) {
            return false;
        }

        @Override
        public Name addAll(Name suffix) throws InvalidNameException {
            return this;
        }

        @Override
        public Name addAll(int posn, Name n) throws InvalidNameException {
            return this;
        }

        @Override
        public Name add(String comp) throws InvalidNameException {
            return this;
        }

        @Override
        public Name add(int posn, String comp) throws InvalidNameException {
            return this;
        }

        @Override
        public Object remove(int posn) throws InvalidNameException {
            return string;
        }

        public String toString() {
            return string;
        }

    }

    private NameParser nameParser = new NameParser() {

        @Override
        public Name parse(String name) throws NamingException {
            return new DummyName(name);
        }
    };

    @Override
    public Object lookup(Name name) throws NamingException {
        return lookup(name.toString());
    }

    @Override
    public Object lookup(String name) throws NamingException {
        return map.get(name);
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {
        bind(name.toString(), obj);

    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        map.put(name, obj);
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {

        rebind(name.toString(), obj);
    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {
        map.put(name, obj);
    }

    @Override
    public void unbind(Name name) throws NamingException {
        unbind(name.toString());
    }

    @Override
    public void unbind(String name) throws NamingException {
        map.remove(name);

    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {

    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {

    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {

    }

    @Override
    public void destroySubcontext(String name) throws NamingException {}

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        return this;
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return this;
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        return lookupLink(name.toString());
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return map.get(name);
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {

        return nameParser;
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        return nameParser;
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {

        return name;
    }

    @Override
    public String composeName(String name, String prefix) throws NamingException {
        return name;
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return propVal;
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        return null;
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return new Hashtable();
    }

    @Override
    public void close() throws NamingException {

    }

    @Override
    public String getNameInNamespace() throws NamingException {
        return "";
    }

}
