# GenjiTest

This is a small library to help writing randomized parametrized Tests in JUnit 5.

The goals are:
- support many data types of the Java API
- good support for generic types
- give some sensible default values
- allow to configure the behaviour with annotations on class, method and parameter level
- make it easy to derive and register your own generators for arbitrary types

The project is in pre-alpha status, so expect frequent changes.

# Example

```
@GenjiTest
void appendRule(String s1, String s2) {
    var appended = s1 + s2;
    assertEquals(appended.length(), s1.length() + s2.length());
}
```

In this example, the `appendRule` method will be called several times with different random `String`s, using some default values for size, length and kind of data etc. 

# Similar (and actually working) projects

- [jkwik](https://github.com/jlink/jqwik)
- [junit-quickcheck](https://github.com/pholser/junit-quickcheck)
