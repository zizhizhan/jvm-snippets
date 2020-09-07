package com.zizhizhan.legacies.restful.party.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zizhizhan.legacies.restful.util.Utils;

public class Party {

    private final static String[] RESERVED_KEYS = {"company", "university", "nativePlace"};
    private final static String[] CONTACTS_KEYS = {"email", "tel", "cellphone", "addr"};

    private final static Party PARTY = new Party();

    private final Map<String, Person> storage = new HashMap<String, Person>();

    private Party() {
        Person james = createPerson("james", "Vanceinfo", "JXNU", "Jiangxi");
        james.setContact(createContact("zhiqiangzhan@gmail.com", "13798520139", "Shenzhen"));
        james.getContact().put("QQ", "28927352");
        Person peter = createPerson("peter", "Microsoft", "JXNU", "Guangdong");
        peter.setContact(createContact("peter@gmail.com", "15998520139", "Shenzhen"));
        peter.getContact().put("QQ", "28927353");
        Person serena = createPerson("serena", "Apple", "JXNU", "Hubei");
        serena.setContact(createContact("serena@gmail.com", "15998520158", "Shenzhen"));
        serena.getContact().put("QQ", "28927354");
        createPerson("cyber", "HP", "JXNU", "Guangdong");
        createPerson("hannah", "Sun", "JXNU", "Hubei");
        createPerson("vensent", "Vanceinfo", "JXNU", "Hubei");
    }

    public static Party singleton() {
        return PARTY;
    }

    public boolean exists(Person p) {
        return storage.containsKey(p.getName());
    }

    public Person put(String key, Person value) {
        return storage.put(key, value);
    }

    public Person remove(Object key) {
        return storage.remove(key);
    }

    public Person getPersonByName(String name) {
        return storage.get(name);
    }

    public List<Person> listAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Person> getPersons(String category, String searchKey) {
        return getPersons(storage.values(), category, searchKey);
    }

    public List<Person> getPersons(Collection<Person> person, String category, String searchKey) {
        List<Person> persons = new ArrayList<>();

        for (Person p : storage.values()) {
            String item = null;
            if (Utils.contains(RESERVED_KEYS, category)) {
                item = (String) Utils.getProperty(p, category);
            } else if (Utils.contains(CONTACTS_KEYS, category) && p.getContact() != null) {
                item = (String) Utils.getProperty(p.getContact(), category);
            } else {
                if (p.getContact() != null) {
                    item = p.getContact().getContacts().get(category);
                }
            }
            if (item != null && item.contains(searchKey)) {
                persons.add(p);
            }
        }
        return persons;
    }

    public void update(Person target, Person replace)
            throws IllegalArgumentException, IllegalAccessException {
        String name = target.getName();
        Utils.update(target, replace, true);
        target.setName(name);
    }

    Person createPerson(String name, String company, String university, String nativePlace) {
        Person person = new Person(name);
        person.setCompany(company);
        person.setNativePlace(nativePlace);
        person.setUniversity(university);
        storage.put(person.getName(), person);
        return person;
    }

    Contact createContact(String email, String cellphone, String addr) {
        Contact contact = new Contact();
        contact.setAddr(addr);
        contact.setCellphone(cellphone);
        contact.setEmail(email);
        return contact;
    }

}
