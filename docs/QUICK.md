[Content](./CONTENT.md)

# Quickstart Guide


## Set an annotation on a certain level

You can set annotations on several levels in your test module. As you would expect, annotations in lower levels 
overwrite annotations in higher levels. Of course, not all annotations can be put on any level. 

### Module Level

* This feature works only if you want to use the Java 9 module system
* Add a new file `module-info.java` to your module, or edit the existing one
* At the start of the file, add all annotation you like
* Then add the module description itself 

Assume you have a module `my.project.module`, and you want to add a `@Size` annotation to it, 
to have a default size range from 3 to 7 for all generated collections. 
Then your `module-info.java` file should look like this:

```java
import org.genji.generators.collection.Size;

@Size(from = 3, to = 7) 
module my.project.module {  
   // other requires and exports declarations
}
```

### Package Level

* Add a new file `package-info.java` to your package, or edit the existing one
* At the start of the file, add all annotations you like
* Then add the package reference itself
* Then import the annotations

Assume you have a test package `my.project`, and you want to add a `@Size` annotation to it, 
to have a default size range from 3 to 7 for all generated collections. 
Then your `package-info.java` file should look like this:

```java
@Size(from = 3, to = 7)
package my.project;

import org.genji.generators.collection.Size;
```
  
### Class Level

Simply add the annotation on your test class:

```java
@Seed(42)
class MyTest {
  ...
}
```

### Method Level
Simply add the annotation on your test method:

```java
import org.genji.GenjiTest;class MyTest {
  @GenjiTest
  @Seed(42)
  void myTestMethod(String a) {
  }

}
```

### Method Parameter Level
Annotations can be also set for individual method parameters:

```java
import org.genji.GenjiTest;class MyTest {
  @GenjiTest
  void myTestMethod(
    @StringSpec(oneOf = {"a","b","c"}) String a, 
    @StringSpec(oneOf = {"1","2","3"}) String b) {
  }
}
```

### Method Parameter Generics Level
You can even control the behavior of individual generic type parameters. Here you get a list of sets, which have sizes between 1 and 3: 
```java
import org.genji.GenjiTest;class MyTest {
  @GenjiTest
  void myTestMethod(List<@Size(from = 1, to = 3) Set<String>> listOfSetsOfStrings) {
  }
}
```
Note that you could set the list size to some different range by using an annotation on parameter level or higher.