package yubo.v1.jcip.threadsafe;

import java.util.HashSet;
import java.util.Set;

public class PersonSet {
    //Guarded by this
    private final Set<Person> mySet = new HashSet<>();

    public synchronized void addPerson(final Person person) {
        this.mySet.add(person);
    }

    public synchronized boolean containsPerson(final Person p) {
        return mySet.contains(p);
    }
}
