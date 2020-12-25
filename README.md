# GenjiTest

[![Build Status](https://travis-ci.org/DanielGronau/genjiTest.svg?branch=master)](https://travis-ci.org/DanielGronau/genjiTest)

This is a small library to help writing randomized parametrized Tests in JUnit 5.

The goals are:
- support many data types of the Java API
- good support for generic types
- give some sensible default values
- allow to configure the behaviour with annotations on class, method and parameter level
- make it easy to write your own generators for arbitrary types

The project is in pre-alpha status, so expect frequent changes. As this phase is about
exploring the possibilities, there will be not much consideration regarding performance
at this point, so some tests might run much slower than possible.

At the moment, the implementation of shrinking (finding the "smallest" failing example) 
is not planned. While it certainly is a useful feature, it doesn't fit well with the
light-weight approach and tight JUnit integration of the project.   

Please note that GenjiTest is written in Java 11, and will not work in older Java versions.

## Documentation

Start [here](./docs/CONTENT.md)

## How does it work?

* The mechanism for integration with JUnit 5 is described in [JUnit 5 Documentation - Parameterized Tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests).
* The `@GenjiTest` annotation is just a shortcut to mark a method as a parametrized JUnit 5 test with `GenjiProvider` as `ArgumentsProvider`.
* While `ArgumentsProvider` classes usually produce specialized data, `GenjiProvider` tries to make the best out of the method parameters it finds. There are predefined generators for many Java classes, and it is also possible to provide custom generators.
* The generators can be configured with annotations.

## Minimal Example

```
@GenjiTest
void appendRule(String s1, String s2) {
    var appended = s1 + s2;
    assertEquals(appended.length(), s1.length() + s2.length());
}
```

In this example, the `appendRule` method will be called several times with different random `String`s, using some default values for size, length and kind of data etc. 

## Fine-grained control

Of course, in the previous example you could also control the generated values:

* with the `@Samples` annotation, you can specify how often the method is invoked
* with the `@WithNulls` annotation, there are sometimes null values included
* with the `@StringSpec` annotation, you can determine the length or used char set, or define a pool of strings to be used (of course, other types have similar annotations)
* with the `@Custom` annotation, you can replace a built-in generator with your own implementation

You can put the annotations on the scope which is most convenient for you: 
* on package level
* on class level
* on method level
* on method parameters (except `@Samples`)
* on type parameters of method arguments (except `@Samples`)

## Generic method parameters

GenjiTest will try to assemble generators for generic method parameters as well. E.g. 
there are built-in generators for `List` and `String`, hence method parameters of type
 `List<String>` or `List<List<String>>` will work out of the box.
 
## Similar but mature projects

- [jkwik](https://github.com/jlink/jqwik)
- [junit-quickcheck](https://github.com/pholser/junit-quickcheck)
