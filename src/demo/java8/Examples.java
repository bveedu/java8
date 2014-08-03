package demo.java8;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by ByjuV on 6/20/2014.
 */
interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }

    static double getPi() {
        return 3.14;
    }
}

class Address {
    public String getHouseName() {
        return houseName;
    }
    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }
    public Address(String houseName,
            String city) {
        this.houseName = houseName;
        this.city= city;
    }
    String houseName;
    String city ;
}
class Person {

    String firstName;
    String lastName;
    String middleName;
    Address address;

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Optional<String> getMiddleName() {
        return Optional.ofNullable(middleName);
    }
    Person() {
    }
    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}

interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}

public class Examples {


    public static void main(String[] args) {
        // Interfaces
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return (a * 100) / 2;
            }
        };

        formula.calculate(100);     // 100.0
        formula.sqrt(16);
        Formula.getPi();

        // Lambda
        List<Person> persons = Arrays.asList(new Person("Byju", "Veedu"), new Person("Ram", "Kumar"));

        Collections.sort(persons, (Person a, Person b) -> {
            return b.firstName.compareTo(a.firstName);
        });
        Collections.sort(persons, (a, b) -> b.firstName.compareTo(a.firstName));
        
        // Functional Interfaces
        Converter<String, Integer> converter1 = (String from) -> Integer.valueOf(from);
        Integer converted = converter1.convert("123");
        Thread data = new Thread(()-> System.out.println("Running!"));
        data.start();

        // Method anc constructor reference
        Converter<String, Integer> converter2 = Integer::valueOf;
        Function<String,Integer>  converter = Integer::valueOf;
        converted = converter2.convert("123");
        // Non static method
        Converter<Integer, Double> converter3 = formula::calculate;
        Double convertedValue = converter3.convert(100);
         // Constructor
        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Peter", "Parker");

        final int  num = 1;
        Converter<Integer, String> stringConverter =
                (from) -> String.valueOf(from + num);
        //num = 3;

        // Built in Functional Interfaces
        Predicate<String> predicate = (s) -> s.length() > 0;
        predicate.test("foo");

        persons.stream().filter((p)-> p.firstName.startsWith("a"));

        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        backToString.apply("123");

        Supplier<Person> personSupplier = Person::new;
        personSupplier.get();   // new Person

        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
        greeter.accept(new Person("Luke", "Skywalker"));

        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");
        comparator.compare(p1, p2);             // > 0
        comparator.reversed().compare(p1, p2); // <0

        // Optional
        Optional<String> optional = Optional.of("bam");
        optional.isPresent();           // true
        optional.get();                 // "bam"
        optional.orElse("fallback");    // "bam"
        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"

        Person per = new Person("Syam","Sundar");
        per.address= new Address("No1","Bangalore");

        per.getAddress().flatMap(Address::getCity).ifPresent(s -> System.out.println(s.toUpperCase()));

        per.address= null;
        System.out.println(per.getAddress().flatMap(Address::getCity).orElse("No City"));

        //Streams
        List<String> strings = Arrays.asList("aaa", "bbb", "abc", "bcd","aab","bcc","bdd");

        strings.stream().map(String::toUpperCase).sorted().filter((s) -> s.startsWith("A")).forEach(System.out::println);

        boolean anyStartsWithA = strings.stream().anyMatch((s) -> s.startsWith("a"));
        //System.out.println(anyStartsWithA);

        long startsWithB = strings.stream().filter((s) -> s.startsWith("b")).count();
        //System.out.println(startsWithB);

        Optional<String> reduced =   strings.stream().sorted().reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(System.out::println);

        persons.stream().sorted((a,b)->a.firstName.compareTo(b.firstName));

        String sortedAndCombined =persons.stream().sorted((a, b) -> a.firstName.compareTo(b.firstName)).map(x -> x.firstName).collect(Collectors.joining(", "));

        List<String> sorted =persons.stream().sorted((a, b) -> a.firstName.compareTo(b.firstName)).map(x -> x.firstName).collect(Collectors.toList());

        System.out.println(IntStream.range(1,100).sum());

        Stream<List<Integer>> integerListStream = Stream.of(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5)
        );

        Stream<Integer> integerStream = integerListStream .flatMap((integerList) -> integerList.stream());
        integerStream.forEach(System.out::println);

        // Sequential and Parallel
        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        long t0 = System.nanoTime();
        long count = values.stream().sorted().count();
        //System.out.println(count);
        long t1 = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("sequential sort took: %d ms", millis));

        t0 = System.nanoTime();
        count = values.parallelStream().sorted().count();
        //System.out.println(count);
        t1 = System.nanoTime();
        millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("parallel sort took: %d ms", millis));

        // Maps
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }
        map.forEach((id, val) -> System.out.println(val));

        // Date and Time
        Clock clock = Clock.systemDefaultZone();
        long milliseconds = clock.millis();
        Instant instant = clock.instant();
        Date legacyDate = Date.from(instant);   // legacy java.util.Date

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone1);
        //System.out.println(now1.isBefore(now2));

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        LocalDate yesterday = tomorrow.minusDays(2);
        //System.out.println("today:" + today);
        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);
        DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
        //System.out.println(dayOfWeek);

        instant = sylvester
                .atZone(ZoneId.systemDefault())
                .toInstant();

        legacyDate = Date.from(instant);
        //System.out.println(legacyDate);
    }
}