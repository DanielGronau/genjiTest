[Content](./CONTENT.md)

# General Purpose Annotations

## @GenjiTest

Marks a method as parameterized JUnit 5 test with the `ArgumentsProvider` set to `GenjiProvider`. 

```java
@GenjiTest
void testMethod(String a) {
}
```
... is equivalent to:

```java
@ArgumentsSource(GenjiProvider.class)
@ParameterizedTest
void testMethod(String a) {
}
```

## @Samples

Allowed on: Module, Package, Class, Methode

<table>
<tr>
<th> Element </th>
<th> Default</th>
<th> Description</th>
<th>Example</th>
</tr>

<tr>
<td> <code>int value</code> </td>
<td> 20 </td>
<td> Number of randomized argument lists to be generated</td>
<td><code>@Samples(10)</code></td>
</tr>
</table>

This annotation determines, how many randomized argument lists should be passed to the parameterized test method.
Cannot specified at the method parameter level or method parameter generics level. 

## @Seed

<table>
<tr>
<th> Element </th>
<th> Default </th>
<th> Description</th>
<th>Example</th>
</tr>

<tr>
<td> <code>long value</code> </td>
<td> none </td>
<td> The seed used for random generators</td>
<td><code>@Seed(42)</code></td>
</tr>
</table>

The `@Seed` annotation sets a certain seed for the used random generator, so that the generated values are deterministic. 
As JUnit doesn't guarantee a certain execution order of tests, seed values are always set on method level or below,
even when the package or class was annotated. E.g. this ...

```java
@Seed(12)
class Test {
  @GenjiTest
  void methodA(String a) {
  }

  @GenjiTest
  void methodB(String b) {
  }
}
```
...will give the exact same results as this:
 
```java
class Test {
  @Seed(12)
  @GenjiTest
  void methodA(String a) {
  }

  @Seed(12)
  @GenjiTest
  void methodB(String b) {
  }
}
```
  
## @WithNulls

<table>
<tr>
<th> Element </th>
<th> Default </th>
<th> Description</th>
<th>Example</th>
</tr>

<tr>
<td> <code>double probability</code> </td>
<td> 0.05 = 5% </td>
<td> How often will be nulls inserted</td>
<td><code>@Seed(42)</code></td>
</tr>
</table>
  
All built-in generators will provide only non-null values. Using the `@WithNulls` annotation, a certain percentage of
nulls can be inserted in the generated samples. The probability value will be clamped to the range 0.0 (no null) to 1.0 (all nulls), and defaults to 0.05, which means that about 1 in 20 values will be a `null` value.  