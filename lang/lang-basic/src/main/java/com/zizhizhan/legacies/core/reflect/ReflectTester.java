package com.zizhizhan.legacies.core.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectTester {

    public Object copy(Object object) throws Exception {
        Class<?> classType = object.getClass();
        System.out.println("Class:" + classType.getName());

        Object objectCopy = classType.getConstructor(new Class[]{}).newInstance();

        Field[] fields = classType.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            String fieldName = field.getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();

            String getMethodName = "get" + firstLetter + fieldName.substring(1);

            String setMethodName = "set" + firstLetter + fieldName.substring(1);

            Method getMethod = classType.getMethod(getMethodName);

            Method setMethod = classType.getMethod(setMethodName,
					field.getType());

            Object value = getMethod.invoke(object);
            System.out.println(fieldName + ":" + value);

            setMethod.invoke(objectCopy, value);
        }
        return objectCopy;

    }

    public static void main(String[] args) throws Exception {
        Customer customer = new Customer("Tom", 21);
        customer.setId(1L);

        Customer customerCopy = (Customer) new ReflectTester().copy(customer);
        System.out.println("Copy information:" + customerCopy.getId() + " "
                + customerCopy.getName() + " " + customerCopy.getAge());

    }

    static class Customer {

        private Long id;
        private String name;
        private int age;

        public Customer() {
        }

        public Customer(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}


